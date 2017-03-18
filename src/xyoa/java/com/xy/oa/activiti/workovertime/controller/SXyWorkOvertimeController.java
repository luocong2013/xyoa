package com.xy.oa.activiti.workovertime.controller;


import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.xy.oa.activiti.util.CommentBean;
import com.xy.oa.activiti.workovertime.entity.SXyWorkOvertimeEntity;
import com.xy.oa.activiti.workovertime.entity.WorkOvertimePo;
import com.xy.oa.activiti.workovertime.service.SXyWorkOvertimeServiceI;
import com.xy.oa.calendars.service.SXyCalendarsServiceI;
import com.xy.oa.checkinout.entity.SXyCheckinoutEntity;
import com.xy.oa.checkinout.service.SXyCheckinoutServiceI;
import com.xy.oa.staff.service.StaffServiceI;
import com.xy.oa.util.ApplyTypeEnum;
import com.xy.oa.util.Constants;

/**   
 * @Title: Controller  
 * @Description: 享宇加班表
 * @author onlineGenerator
 * @date 2016-08-17 15:59:06
 * @version V1.0   
 *
 */
@Controller("sXyWorkOvertimeController")
@RequestMapping("/sXyWorkOvertimeController")
public class SXyWorkOvertimeController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SXyWorkOvertimeController.class);

	@Autowired
	private SXyWorkOvertimeServiceI sXyWorkOvertimeService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private SXyCalendarsServiceI sXyCalendarsService;
	@Autowired
	private StaffServiceI staffService;
	@Autowired
	private SXyCheckinoutServiceI sXyCheckinoutService;
	

	/**
	 * 显示在考勤记录表中、归档、可调休时间统计
	 * @param overTimeId：加班申请表ID
	 */
	public void doSome(String overTimeId) {
		try {
			//获取加班表信息
			SXyWorkOvertimeEntity sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, overTimeId);
			//归档
			List<SXyCheckinoutEntity> sEntities = sXyCheckinoutService.disCheckInOut(sXyWorkOvertime.getApplySttaffId(), DateUtils.formatDate(sXyWorkOvertime.getApplyStartTime()), 
					DateUtils.formatDate(sXyWorkOvertime.getApplyEndTime()), sXyWorkOvertime.getId(), Constants.XY_CHECK_TYPE_20, sXyWorkOvertime.getRemarks());
			//加班类型
			String workType = sXyWorkOvertime.getWorkType();
			if("01".equals(workType) || "02".equals(workType)) {
				//所属部门名称
				String departname = sXyWorkOvertime.getTsDept().getDepartname();
				//上一级部门名称
				String pdepartname = sXyWorkOvertime.getTsDept().getTSPDepart().getDepartname();
				if (sEntities != null && sEntities.size() > 0) {
					SXyCheckinoutEntity sXyCheckinoutEntity = sEntities.get(0);
					Date startTime = DateUtils.parseDate(sXyCheckinoutEntity.getCheckDate()+" "+sXyCheckinoutEntity.getWorkTime(), "yyyy-MM-dd HH:mm:ss");
					SXyCheckinoutEntity sXyCheckinoutEntity2 = sEntities.get(sEntities.size() - 1);
					Date endTime = DateUtils.parseDate(sXyCheckinoutEntity2.getCheckDate()+" "+sXyCheckinoutEntity2.getOffWorkTime(), "yyyy-MM-dd HH:mm:ss");
					//设置真实加班开始时间
					sXyWorkOvertime.setStartTime(startTime);
					//设置真实加班结束时间
					sXyWorkOvertime.setEndTime(endTime);
					//计算真实加班时长
					double workHour = 0;
					for (SXyCheckinoutEntity sXyCheckinout : sEntities) {
						workHour += sXyCheckinout.getWorkHour();
					}
					//设置真实加班时长
					if (sXyWorkOvertime.getApplyWorkHour() > workHour) {
						sXyWorkOvertime.setWorkHour(workHour);
					} else {
						sXyWorkOvertime.setWorkHour(sXyWorkOvertime.getApplyWorkHour());
					}
				} else {
					//设置真实加班开始时间
					sXyWorkOvertime.setStartTime(sXyWorkOvertime.getApplyStartTime());
					//设置真实加班结束时间
					sXyWorkOvertime.setEndTime(sXyWorkOvertime.getApplyEndTime());
					//设置真实加班时长
					sXyWorkOvertime.setWorkHour(sXyWorkOvertime.getApplyWorkHour());
				}
				//设置可调休时长
				if("运营部".equals(departname) || "运营部".equals(pdepartname)) {
					sXyWorkOvertime.setOnWorkHour(sXyWorkOvertime.getWorkHour()*Constants.YUNYINGBU);
				} else {
					sXyWorkOvertime.setOnWorkHour(sXyWorkOvertime.getWorkHour()*Constants.OTHER);
				}
				sXyWorkOvertimeService.saveOrUpdate(sXyWorkOvertime);
			}
			//增加用户的可调休天数（小时转换为天数）
			double onWorkHour = sXyWorkOvertime.getOnWorkHour() / 7.5;
			BigDecimal b = new BigDecimal(onWorkHour);
			onWorkHour = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
			staffService.addOffWorkCount(sXyWorkOvertime.getApplySttaffId(), onWorkHour);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * 享宇加班表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyWorkOvertimeService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/workovertime/sXyWorkOvertimeList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(SXyWorkOvertimeEntity sXyWorkOvertime,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SXyWorkOvertimeEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyWorkOvertime, request.getParameterMap());
		try{
			//自定义追加查询条件
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyWorkOvertimeService.findHqlRoleCodes(tsUser);
			String roleCode = null;
			if (roleCodes.size() == 1) {
				roleCode = roleCodes.get(0);
			} else {
			   roleCode = roleCodes.get(1);
			}
			
			//部门查询
			String orgId = request.getParameter("orgId");
			if (StringUtil.isNotEmpty(orgId)) {
//				TSDepart tsDepart = sXyWorkOvertimeService.getEntity(TSDepart.class, orgId);
//				cq.eq("tsDept", tsDepart);
				
				TSDepart tsDepart = sXyWorkOvertimeService.getEntity(TSDepart.class, orgId);
				List<TSDepart> tSDepartList = tsDepart.getTSDeparts();
				tSDepartList.add(0, tsDepart);
				cq.in("tsDept", tSDepartList.toArray());
			}
			
			if(Constants.DM.equals(roleCode)) {
				//查询该部门下的所有员工以及各种组的所有员工
				List<TSUser> tsUsers = sXyWorkOvertimeService.findHqlTSUsersByUser(tsUser);
				if (tsUsers == null || tsUsers.isEmpty()) {
					cq.eq("tsUser", tsUser);
				} else {
					cq.or(cq.and(Restrictions.in("tsUser", tsUsers.toArray()), Restrictions.eq("flowState", "4")), Restrictions.eq("tsUser", tsUser));
				}
			} else if (Constants.HR.equals(roleCode) || Constants.HRDM.equals(roleCode) || Constants.VICE.equals(roleCode) || Constants.CEO.equals(roleCode)) {
				//查询普通员工、直接上级、人事专员、部门DM、人事DM和副总裁
				List<TSUser> tsUsers = sXyWorkOvertimeService.findHqlTSUsersByUR(tsUser);
				cq.or(cq.and(Restrictions.in("tsUser", tsUsers.toArray()), Restrictions.eq("flowState", "4")), Restrictions.eq("tsUser", tsUser));
			} else {
				//查询当前登录用户提交的加班申请
				cq.eq("tsUser", tsUser);
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyWorkOvertimeService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 直接上级、部门DM、副总裁、总裁
	 * 享宇审核加班申请表单 页面跳转
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "listCheck")
	public ModelAndView listCheck(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyWorkOvertimeService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/workovertime/sXyWorkOvertimeListCheck");
	}
	
	
	/**
	 * Ajax数据请求
	 * @param sXyWorkOvertime
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridCheck")
	public void datagridCheck(SXyWorkOvertimeEntity sXyWorkOvertime,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String spflag) {
		CriteriaQuery cq = new CriteriaQuery(SXyWorkOvertimeEntity.class, dataGrid);
		//查询条件组装器
		String paramHql = org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHqlForFlow("S", cq, sXyWorkOvertime, request.getParameterMap());
		try{
			//自定义追加查询条件
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//部门查询
			String orgId = request.getParameter("orgId");
			
			List<WorkOvertimePo> lPos = new ArrayList<WorkOvertimePo>();
			int total = 0;
			int beginNum = (cq.getCurPage()-1)*cq.getPageSize();
			if ("00".equals(spflag) || "01".equals(spflag)) {
				if ("00".equals(spflag)) {
					//查看当前登录用户的任务（以用户编号(userName)查询）
					sXyWorkOvertimeService.getDataGridRunForHQL(SXyWorkOvertimeEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyWorkOvertimeService.getRunTotal(SXyWorkOvertimeEntity.class, false, paramHql, orgId, tsUser.getUserName());
				} else if ("01".equals(spflag)) {
					//查看当前用户的历史任务（已审批过的）
					sXyWorkOvertimeService.getDataGridHisForHQL(SXyWorkOvertimeEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyWorkOvertimeService.getHisTotal(SXyWorkOvertimeEntity.class, false, paramHql, orgId, tsUser.getUserName());
				}
				List<SXyWorkOvertimeEntity> lOvertimeEntities = dataGrid.getResults();
				for (SXyWorkOvertimeEntity sWorkOvertimeEntity : lOvertimeEntities) {
					WorkOvertimePo wPo = new WorkOvertimePo();
					BeanUtils.copyProperties(sWorkOvertimeEntity, wPo);
					wPo.setIsApprove(spflag);
					lPos.add(wPo);
				}
			} else {
				//查询当前登录用户的任务（以用户编号(userName)查询）
				int totalR = sXyWorkOvertimeService.getRunTotal(SXyWorkOvertimeEntity.class, false, paramHql, orgId, tsUser.getUserName());
				//查看当前用户的历史任务的总条数（已审批过的）
				int totalH = sXyWorkOvertimeService.getHisTotal(SXyWorkOvertimeEntity.class, false, paramHql, orgId, tsUser.getUserName());
				total = totalR + totalH;
				if (totalR <= beginNum) {
					DataGrid dataGrid2 = new DataGrid();
					beginNum  -= totalR;
					//查看当前用户的历史任务（已审批过的）
					sXyWorkOvertimeService.getDataGridHisForHQL(SXyWorkOvertimeEntity.class, dataGrid2, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyWorkOvertimeEntity> lOvertimeEntitiesH = dataGrid2.getResults();
					for (SXyWorkOvertimeEntity sWorkOvertimeEntity : lOvertimeEntitiesH) {
						WorkOvertimePo wPo = new WorkOvertimePo();
						BeanUtils.copyProperties(sWorkOvertimeEntity, wPo);
						wPo.setIsApprove("01");
						lPos.add(wPo);
					}
				} else {
					//查看当前登录用户的任务（以用户编号(userName)查询）
					sXyWorkOvertimeService.getDataGridRunForHQL(SXyWorkOvertimeEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyWorkOvertimeEntity> lOvertimeEntitiesR = dataGrid.getResults();
					for (SXyWorkOvertimeEntity sWorkOvertimeEntity : lOvertimeEntitiesR) {
						WorkOvertimePo wPo = new WorkOvertimePo();
						BeanUtils.copyProperties(sWorkOvertimeEntity, wPo);
						wPo.setIsApprove("00");
						lPos.add(wPo);
					}
					if (lOvertimeEntitiesR.size() < cq.getPageSize()) {
						DataGrid dataGrid2 = new DataGrid();
						//查看当前用户的历史任务（已审批过的）
						sXyWorkOvertimeService.getDataGridHisForHQL(SXyWorkOvertimeEntity.class, dataGrid2, false, paramHql, orgId, 0, cq.getPageSize()-lOvertimeEntitiesR.size(), tsUser.getUserName());
						List<SXyWorkOvertimeEntity> lOvertimeEntitiesH = dataGrid2.getResults();
						for (SXyWorkOvertimeEntity sWorkOvertimeEntity : lOvertimeEntitiesH) {
							WorkOvertimePo wPo = new WorkOvertimePo();
							BeanUtils.copyProperties(sWorkOvertimeEntity, wPo);
							wPo.setIsApprove("01");
							lPos.add(wPo);
						}
					}
				}
			}
			dataGrid.setResults(lPos);
			dataGrid.setTotal(total);
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		TagUtil.datagrid(response, dataGrid);
	}
	/**
	 * HR
	 * 享宇审核加班申请表单 页面跳转
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "listHr")
	public ModelAndView listHr(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyWorkOvertimeService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/workovertime/sXyWorkOvertimeListHr");
	}
	/**
	 * Ajax数据请求
	 * @param sXyWorkOvertime
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridHr")
	public void datagridHr(SXyWorkOvertimeEntity sXyWorkOvertime,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String spflag) {
		CriteriaQuery cq = new CriteriaQuery(SXyWorkOvertimeEntity.class, dataGrid);
		//查询条件组装器
		String paramHql = org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHqlForFlow("S", cq, sXyWorkOvertime, request.getParameterMap());
		try{
			//自定义追加查询条件
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//部门查询
			String orgId = request.getParameter("orgId");
			
			List<WorkOvertimePo> lPos = new ArrayList<WorkOvertimePo>();
			int total = 0;
			int beginNum = (cq.getCurPage()-1)*cq.getPageSize();
			if ("00".equals(spflag) || "01".equals(spflag)) {
				if ("00".equals(spflag)) {
					//查看当前登录用户的任务（以用户编号(userName)查询）
					sXyWorkOvertimeService.getDataGridRunForHQL(SXyWorkOvertimeEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyWorkOvertimeService.getRunTotal(SXyWorkOvertimeEntity.class, true, paramHql, orgId, tsUser.getUserName());
				} else if ("01".equals(spflag)) {
					//查看当前用户的历史任务（已审批过的）
					sXyWorkOvertimeService.getDataGridHisForHQL(SXyWorkOvertimeEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyWorkOvertimeService.getHisTotal(SXyWorkOvertimeEntity.class, true, paramHql, orgId, tsUser.getUserName());
				}
				List<SXyWorkOvertimeEntity> lOvertimeEntities = dataGrid.getResults();
				for (SXyWorkOvertimeEntity sWorkOvertimeEntity : lOvertimeEntities) {
					WorkOvertimePo wPo = new WorkOvertimePo();
					BeanUtils.copyProperties(sWorkOvertimeEntity, wPo);
					wPo.setIsApprove(spflag);
					lPos.add(wPo);
				}
			} else {
				//查询当前登录用户的任务（以用户编号(userName)查询）
				int totalR = sXyWorkOvertimeService.getRunTotal(SXyWorkOvertimeEntity.class, true, paramHql, orgId, tsUser.getUserName());
				//查看当前用户的历史任务的总条数（已审批过的）
				int totalH = sXyWorkOvertimeService.getHisTotal(SXyWorkOvertimeEntity.class, true, paramHql, orgId, tsUser.getUserName());
				total = totalR + totalH;
				if (totalR <= beginNum) {
					DataGrid dataGrid2 = new DataGrid();
					beginNum  -= totalR;
					//查看当前用户的历史任务（已审批过的）
					sXyWorkOvertimeService.getDataGridHisForHQL(SXyWorkOvertimeEntity.class, dataGrid2, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyWorkOvertimeEntity> lOvertimeEntitiesH = dataGrid2.getResults();
					for (SXyWorkOvertimeEntity sWorkOvertimeEntity : lOvertimeEntitiesH) {
						WorkOvertimePo wPo = new WorkOvertimePo();
						BeanUtils.copyProperties(sWorkOvertimeEntity, wPo);
						wPo.setIsApprove("01");
						lPos.add(wPo);
					}
				} else {
					//查看当前登录用户的任务（以用户编号(userName)查询）
					sXyWorkOvertimeService.getDataGridRunForHQL(SXyWorkOvertimeEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyWorkOvertimeEntity> lOvertimeEntitiesR = dataGrid.getResults();
					for (SXyWorkOvertimeEntity sWorkOvertimeEntity : lOvertimeEntitiesR) {
						WorkOvertimePo wPo = new WorkOvertimePo();
						BeanUtils.copyProperties(sWorkOvertimeEntity, wPo);
						wPo.setIsApprove("00");
						lPos.add(wPo);
					}
					if (lOvertimeEntitiesR.size() < cq.getPageSize()) {
						DataGrid dataGrid2 = new DataGrid();
						//查看当前用户的历史任务（已审批过的）
						sXyWorkOvertimeService.getDataGridHisForHQL(SXyWorkOvertimeEntity.class, dataGrid2, true, paramHql, orgId, 0, cq.getPageSize()-lOvertimeEntitiesR.size(), tsUser.getUserName());
						List<SXyWorkOvertimeEntity> lOvertimeEntitiesH = dataGrid2.getResults();
						for (SXyWorkOvertimeEntity sWorkOvertimeEntity : lOvertimeEntitiesH) {
							WorkOvertimePo wPo = new WorkOvertimePo();
							BeanUtils.copyProperties(sWorkOvertimeEntity, wPo);
							wPo.setIsApprove("01");
							lPos.add(wPo);
						}
					}
				}
			}
			dataGrid.setResults(lPos);
			dataGrid.setTotal(total);
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		TagUtil.datagrid(response, dataGrid);
	}
	

	/**
	 * 删除享宇加班表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		sXyWorkOvertime = systemService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
		message = "享宇加班表删除成功";
		try{
			sXyWorkOvertimeService.delete(sXyWorkOvertime);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇加班表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除享宇加班表
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇加班表删除成功";
		try{
			for(String id:ids.split(",")){
				SXyWorkOvertimeEntity sXyWorkOvertime = systemService.getEntity(SXyWorkOvertimeEntity.class, id);
				sXyWorkOvertimeService.delete(sXyWorkOvertime);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇加班表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加享宇加班表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		//加班类型
		String workType = sXyWorkOvertime.getWorkType();
		message = "享宇加班表添加成功";
		try{
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户所属的部门
			TSDepart tsDepart = sXyWorkOvertimeService.findHqlTSDepart(tsUser);
			//所属部门名称
			String departname = tsDepart.getDepartname();
			//查询当前用户所属部门的上一级部门
			TSDepart tsPDepart = sXyWorkOvertimeService.findHqlTSPDepart(tsUser);
			//当前用户的上一级部门的名称
			String pdepartname = tsPDepart.getDepartname();
			//设置申请人
			sXyWorkOvertime.setTsUser(tsUser);
			//设置申请人编号
			sXyWorkOvertime.setApplySttaffId(Integer.valueOf(tsUser.getUserName()));
			//设置部门
			sXyWorkOvertime.setTsDept(tsDepart);
			if("03".equals(workType)) {
				sXyWorkOvertime.setStartTime(sXyWorkOvertime.getApplyStartTime());
				sXyWorkOvertime.setEndTime(sXyWorkOvertime.getApplyEndTime());
				sXyWorkOvertime.setWorkHour(sXyWorkOvertime.getApplyWorkHour());
				if("运营部".equals(departname) || "运营部".equals(pdepartname)) {
					sXyWorkOvertime.setOnWorkHour(4*Constants.YUNYINGBU);
				} else {
					sXyWorkOvertime.setOnWorkHour(4*Constants.OTHER);
				}
			} else if("04".equals(workType)) {
				sXyWorkOvertime.setStartTime(sXyWorkOvertime.getApplyStartTime());
				sXyWorkOvertime.setEndTime(sXyWorkOvertime.getApplyEndTime());
				sXyWorkOvertime.setWorkHour(sXyWorkOvertime.getApplyWorkHour());
				if("运营部".equals(departname) || "运营部".equals(pdepartname)) {
					sXyWorkOvertime.setOnWorkHour(8*Constants.YUNYINGBU);
				} else {
					sXyWorkOvertime.setOnWorkHour(8*Constants.OTHER);
				}
			}
			//设置创建时间
			sXyWorkOvertime.setCreateTime(new Date());
			//设置加班申请创建人编号
			sXyWorkOvertime.setCUser(Integer.valueOf(tsUser.getUserName()));
			/**
			 * 设置流程状态：0：流程未启动、1：加班待审批、2：加班审批中、3：加班申请被否决、
			 * 4：已完成、5：撤销申请、6：取消申请
			 */
			sXyWorkOvertime.setFlowState("0");
			sXyWorkOvertimeService.save(sXyWorkOvertime);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇加班表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新享宇加班表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		//加班类型
		String workType = sXyWorkOvertime.getWorkType();
		
		message = "享宇加班表更新成功";
		SXyWorkOvertimeEntity t = sXyWorkOvertimeService.get(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户所属的部门
		TSDepart tsDepart = sXyWorkOvertimeService.findHqlTSDepart(tsUser);
		//所属部门名称
		String departname = tsDepart.getDepartname();
		//查询当前用户所属部门的上一级部门
		TSDepart tsPDepart = sXyWorkOvertimeService.findHqlTSPDepart(tsUser);
		//当前用户的上一级部门的名称
		String pdepartname = tsPDepart.getDepartname();
		try {
			if("03".equals(workType)) {
				sXyWorkOvertime.setStartTime(sXyWorkOvertime.getApplyStartTime());
				sXyWorkOvertime.setEndTime(sXyWorkOvertime.getApplyEndTime());
				sXyWorkOvertime.setWorkHour(sXyWorkOvertime.getApplyWorkHour());
				if("运营部".equals(departname) || "运营部".equals(pdepartname)) {
					sXyWorkOvertime.setOnWorkHour(4*Constants.YUNYINGBU);
				} else {
					sXyWorkOvertime.setOnWorkHour(4*Constants.OTHER);
				}
			}
			else if("04".equals(workType)) {
				sXyWorkOvertime.setStartTime(sXyWorkOvertime.getApplyStartTime());
				sXyWorkOvertime.setEndTime(sXyWorkOvertime.getApplyEndTime());
				sXyWorkOvertime.setWorkHour(sXyWorkOvertime.getApplyWorkHour());
				if("运营部".equals(departname) || "运营部".equals(pdepartname)) {
					sXyWorkOvertime.setOnWorkHour(8*Constants.YUNYINGBU);
				} else {
					sXyWorkOvertime.setOnWorkHour(8*Constants.OTHER);
				}
			}
			//设置更新时间
			sXyWorkOvertime.setUTime(new Date());
			//设置更新人编号
			sXyWorkOvertime.setUUser(Integer.valueOf(tsUser.getUserName()));
			MyBeanUtils.copyBeanNotNull2Bean(sXyWorkOvertime, t);
			sXyWorkOvertimeService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇加班表更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 提交享宇加班申请表
	 * @param sXyWorkOvertime
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doSubmitOvertime")
	@ResponseBody
	public AjaxJson doSubmitOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇加班表提交成功";
		//审批人（用户编号(userName)）
		String feaaApprover = request.getParameter("feaaApprover");
		//获取加班申请表信息
		sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户的角色编码
		List<String> roleCodes = sXyWorkOvertimeService.findHqlRoleCodes(tsUser);
		String roleCode = roleCodes.get(0);
		//查询当前用户所属的部门
		TSDepart tsDepart = sXyWorkOvertimeService.findHqlTSDepart(tsUser);
		//机构编码长度
		int orgCodeLength = tsDepart.getOrgCode().length();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		//传入加班业务表的ID（显示在考勤记录表中、归档、可调休时间统计）
		variables.put("overTimeId", sXyWorkOvertime.getId());
		//判断流程走向
		if (Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 12) {
			variables.put("startLine", 1);
			variables.put(Constants.HEADMAN, feaaApprover);
		}
		if ((Constants.EMPLOYEE.equals(roleCode) || Constants.HR.equals(roleCode)) && orgCodeLength == 9) {
			//查询当前部门下的直接上级
			List<TSUser> tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
			if (tsUsers == null || tsUsers.isEmpty()) {
				variables.put("startLine", 2);
				variables.put(Constants.DM, feaaApprover);
			} else {
				variables.put("startLine", 1);
				variables.put(Constants.HEADMAN, feaaApprover);
			}
		}
		if (Constants.HEADMAN.equals(roleCode)) {
			variables.put("startLine", 2);
			variables.put(Constants.DM, feaaApprover);
		}
		if(Constants.DM.equals(roleCode) || Constants.HRDM.equals(roleCode)) {
			variables.put("startLine", 3);
			variables.put(Constants.VICE, feaaApprover);
		}
		if(Constants.VICE.equals(roleCode)) {
			variables.put("startLine", 4);
			variables.put(Constants.CEO, feaaApprover);
		}
		try {
			/**
			 * 设置流程状态：0：流程未启动、1：加班待审批、2：加班审批中、3：加班申请被否决、
			 * 4：已完成、5：撤销申请、6：取消申请
			 */
			sXyWorkOvertime.setFlowState("1");
			//设置加班申请日期
			sXyWorkOvertime.setApplyDate(new Date());
			//设置申请编号
			sXyWorkOvertime.setApplyNo(sXyWorkOvertimeService.getNextApplyNo(ApplyTypeEnum.JB, "s_xy_work_overtime"));
			//启动流程（以用户编号(userName)启动流程）
			String flowInstId = sXyWorkOvertimeService.startFlow("xyWorkOvertime", sXyWorkOvertime.getId(), variables, tsUser.getUserName());
			//设置流程实例ID
			sXyWorkOvertime.setFlowInstId(flowInstId);
			//保存
			sXyWorkOvertimeService.saveOrUpdate(sXyWorkOvertime);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇加班表提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 批准享宇加班表
	 * @param sXyWorkOvertime
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAgreeOvertime")
	@ResponseBody
	public AjaxJson doAgreeOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇加班表批准成功";
		//审批人（用户编号(userName)）
		String nextApprover = request.getParameter("nextApprover");
		//批准原因
		String passReason = request.getParameter("passReason");
		//获取加班表信息
		sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
		/**
		 * 设置流程状态：0：流程未启动、1：加班待审批、2：加班审批中、3：加班申请被否决、
		 * 4：已完成、5：撤销申请、6：取消申请
		 */
		sXyWorkOvertime.setFlowState("2");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户的角色编码
		List<String> roleCodes = sXyWorkOvertimeService.findHqlRoleCodes(tsUser);
		String roleCode = roleCodes.get(0);
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		if(Constants.HEADMAN.equals(roleCode)) {
			variables.put("isPass", 1);
			variables.put(Constants.DM, nextApprover);
		} else {
			variables.put("isPass", 2);
			variables.put(Constants.HR, nextApprover);
		}
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyWorkOvertimeService.completeTask(sXyWorkOvertime.getFlowInstId(), variables, tsUser.getUserName(), passReason);
			//保存状态
			sXyWorkOvertimeService.saveOrUpdate(sXyWorkOvertime);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇加班表批准失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：否决享宇加班表
	 * @param sXyWorkOvertime
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doNotAgreeOvertime")
	@ResponseBody
	public AjaxJson doNotAgreeOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇加班表否决成功";
		//获取加班表信息
		sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
		/**
		 * 设置流程状态：0：流程未启动、1：加班待审批、2：加班审批中、3：加班申请被否决、
		 * 4：已完成、5：撤销申请、6：取消申请
		 */
		sXyWorkOvertime.setFlowState("3");
		//否决原因
		String nopassReason = request.getParameter("nopassReason");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("isPass", 0);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyWorkOvertimeService.completeTask(sXyWorkOvertime.getFlowInstId(), variables, tsUser.getUserName(), nopassReason);
			//保存状态
			sXyWorkOvertimeService.saveOrUpdate(sXyWorkOvertime);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇加班表否决失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 人事部批准享宇加班申请
	 * @param sXyWorkOvertime
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doHrAgreeOvertime")
	@ResponseBody
	public AjaxJson doHrAgreeOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇加班表批准成功";
		//获取加班表信息
		sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
		//批准原因
		String hrpassReason = request.getParameter("hrpassReason");
		/**
		 * 设置流程状态：0：流程未启动、1：加班待审批、2：加班审批中、3：加班申请被否决、
		 * 4：已完成、5：撤销申请、6：取消申请
		 */
		sXyWorkOvertime.setFlowState("4");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("isPass", 3);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyWorkOvertimeService.completeTask(sXyWorkOvertime.getFlowInstId(), variables, tsUser.getUserName(), hrpassReason);
			//保存状态
			sXyWorkOvertimeService.saveOrUpdate(sXyWorkOvertime);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇加班表批准失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	
	/**
	 * 人事部否决享宇加班申请
	 * @param sXyWorkOvertime
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doHrNotAgreeOvertime")
	@ResponseBody
	public AjaxJson doHrNotAgreeOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇加班表否决成功";
		//获取加班表信息
		sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
		//否决原因
		String hrnopassReason = request.getParameter("hrnopassReason");
		/**
		 * 设置流程状态：0：流程未启动、1：加班待审批、2：加班审批中、3：加班申请被否决、
		 * 4：已完成、5：撤销申请、6：取消申请
		 */
		sXyWorkOvertime.setFlowState("3");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("isPass", 0);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyWorkOvertimeService.completeTask(sXyWorkOvertime.getFlowInstId(), variables, tsUser.getUserName(), hrnopassReason);
			//保存状态
			sXyWorkOvertimeService.saveOrUpdate(sXyWorkOvertime);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇加班表否决失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * Ajax 异步获取两个时间的差值（节假日）
	 * @param startT
	 * @param endT
	 * @return
	 */
	@RequestMapping(params = "getTwoDayDiffer")
	@ResponseBody
	public AjaxJson getTwoDayDiffer(Date startT, Date endT) {
		AjaxJson j = new AjaxJson();
		//计算两个时间的差值 小时数
		try {
			BigDecimal bigDecimal = sXyCalendarsService.getTimeInterval(startT, endT, false);
			j.setObj(bigDecimal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	/**
	 * 查看流程图片
	 * @param sXyWorkOvertime
	 * @param response
	 */
	@RequestMapping(params = "lookFlowImage")
	public void lookFlowImage(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletResponse response) {
		try {
			response.setContentType("image/png");
			InputStream inputStream = sXyWorkOvertimeService.graphics(sXyWorkOvertime.getFlowInstId());
			int len = 0;
			byte [] b = new byte[1024];
			while ((len = inputStream.read(b, 0, 1024)) != -1) {
				response.getOutputStream().write(b, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 重新提交加班申请
	 * @param sXyWorkOvertime
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "reSubmitOvertime")
	@ResponseBody
	public AjaxJson reSubmitOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇加班表重新提交成功";
		//获取加班表信息
		sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
		/**
		 * 设置流程状态：0：流程未启动、1：加班待审批、2：加班审批中、3：加班申请被否决、
		 * 4：已完成、5：撤销申请、6：取消申请
		 */
		sXyWorkOvertime.setFlowState("1");
		//重新设置申请时间
		sXyWorkOvertime.setApplyDate(new Date());
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("reApply", true);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyWorkOvertimeService.completeTask(sXyWorkOvertime.getFlowInstId(), variables, tsUser.getUserName(), null);
			//保存状态
			sXyWorkOvertimeService.saveOrUpdate(sXyWorkOvertime);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇加班表重新提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 撤销加班申请
	 * @param sXyWorkOvertime
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delOvertime")
	@ResponseBody
	public AjaxJson delOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "加班申请撤销成功";
		//获取加班表信息
		sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
		/**
		 * 设置流程状态：0：流程未启动、1：加班待审批、2：加班审批中、3：加班申请被否决、
		 * 4：已完成、5：撤销申请、6：取消申请
		 */
		sXyWorkOvertime.setFlowState("5");
		try {
			//撤销申请
			sXyWorkOvertimeService.delFlow(sXyWorkOvertime.getFlowInstId());
			//保存状态
			sXyWorkOvertimeService.saveOrUpdate(sXyWorkOvertime);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "加班申请撤销失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 重新提交加班申请
	 * @param sXyWorkOvertime
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "cancelOvertime")
	@ResponseBody
	public AjaxJson cancelOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇加班表取消申请成功";
		//获取加班表信息
		sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
		/**
		 * 设置流程状态：0：流程未启动、1：加班待审批、2：加班审批中、3：加班申请被否决、
		 * 4：已完成、5：撤销申请、6：取消申请
		 */
		sXyWorkOvertime.setFlowState("6");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("reApply", false);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyWorkOvertimeService.completeTask(sXyWorkOvertime.getFlowInstId(), variables, tsUser.getUserName(), null);
			//保存状态
			sXyWorkOvertimeService.saveOrUpdate(sXyWorkOvertime);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇加班表取消申请失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	

	/**
	 * 享宇加班表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest req) {
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户所属的部门
		TSDepart tsDepart = sXyWorkOvertimeService.findHqlTSDepart(tsUser);
		req.setAttribute("tsUserPage", tsUser);
		req.setAttribute("tsDepartPage", tsDepart);
		return new ModelAndView("xyoa/activiti/workovertime/sXyWorkOvertime-add");
	}
	/**
	 * 享宇加班表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyWorkOvertime.getId())) {
			sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
			req.setAttribute("sXyWorkOvertimePage", sXyWorkOvertime);
		}
		return new ModelAndView("xyoa/activiti/workovertime/sXyWorkOvertime-update");
	}
	
	
	/**
	 * 享宇加班表详情页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goDetail")
	public ModelAndView goDetail(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyWorkOvertime.getId())) {
			try {
				//加班表信息
				sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
				//加班表历史任务节点批注信息
				List<CommentBean> commentBeans = sXyWorkOvertimeService.findComment(sXyWorkOvertime.getFlowInstId());
				req.setAttribute("sXyWorkOvertimePage", sXyWorkOvertime);
				req.setAttribute("commentBeansPage", commentBeans);
			} catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}
		}
		return new ModelAndView("xyoa/activiti/workovertime/sXyWorkOvertime-detail");
	}
	
	/**
	 * 享宇加班表提交申请页面跳转
	 * @param sXyWorkOvertime
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goSubmitOvertime")
	public ModelAndView goSubmitOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyWorkOvertime.getId())) {
			//获取加班表信息
			sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyWorkOvertimeService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			//查询当前用户所属的部门
			TSDepart tsDepart = sXyWorkOvertimeService.findHqlTSDepart(tsUser);
			//机构编码长度
			int orgCodeLength = tsDepart.getOrgCode().length();
			//查询该部门下的领导信息
			List<TSUser> tsUsers = null;
			if (Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 12) {
				tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的直接上级
					TSDepart tsPDepart = sXyWorkOvertimeService.findHqlTSPDepart(tsUser);
					tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(Constants.HEADMAN, tsPDepart);
				}
			}
			if ((Constants.EMPLOYEE.equals(roleCode) || Constants.HR.equals(roleCode)) && orgCodeLength == 9) {
				//查询当前部门下的直接上级
				tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前部门下的部门DM
					tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(Constants.DM, tsDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的部门DM
						TSDepart tsPDepart = sXyWorkOvertimeService.findHqlTSPDepart(tsUser);
						tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					}
				}
			}
			if (Constants.HEADMAN.equals(roleCode)) {
				tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(Constants.DM, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的部门DM
					TSDepart tsPDepart = sXyWorkOvertimeService.findHqlTSPDepart(tsUser);
					tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的上一级部门的部门DM
						TSDepart tsPPDepart = sXyWorkOvertimeService.findHqlTSPPDepart(tsUser);
						tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(Constants.DM, tsPPDepart);
					}
				}
			}
			if(Constants.DM.equals(roleCode) || Constants.HRDM.equals(roleCode)) {
				tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRoleCode(Constants.VICE);
			}
			if(Constants.VICE.equals(roleCode)) {
				tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRoleCode(Constants.CEO);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyWorkOvertimePage", sXyWorkOvertime);
		}
		return new ModelAndView("xyoa/activiti/workovertime/sXyWorkOvertime-submit");
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 享宇加班表批准加班申请页面跳转
	 * @param sXyWorkOvertime
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goAgreeOvertime")
	public ModelAndView goAgreeOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyWorkOvertime.getId())) {
			//获取加班表信息
			sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyWorkOvertimeService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			//查询当前用户的下一级审批人
			List<TSUser> tsUsers = null;
			if(Constants.HEADMAN.equals(roleCode)) {
				//查询当前用户所属的部门
				TSDepart tsDepart = sXyWorkOvertimeService.findHqlTSDepart(tsUser);
				tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(Constants.DM, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的部门DM
					TSDepart tsPDepart = sXyWorkOvertimeService.findHqlTSPDepart(tsUser);
					tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的上一级部门的部门DM
						TSDepart tsPPDepart = sXyWorkOvertimeService.findHqlTSPPDepart(tsUser);
						tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(Constants.DM, tsPPDepart);
					}
				}
			} else {
				//查询HRDM
				tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRoleCode(Constants.HRDM);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyWorkOvertimePage", sXyWorkOvertime);
		}
		return new ModelAndView("xyoa/activiti/workovertime/sXyWorkOvertime-check");
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 享宇加班表否决加班申请页面跳转
	 * @param sXyWorkOvertime
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goNotAgreeOvertime")
	public ModelAndView goNotAgreeOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyWorkOvertime.getId())) {
			sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
			req.setAttribute("sXyWorkOvertimePage", sXyWorkOvertime);
		}
		return new ModelAndView("xyoa/activiti/workovertime/sXyWorkOvertime-nocheck");
	}
	
	
	/**
	 * HR批准享宇加班申请页面跳转
	 * @param sXyWorkOvertime
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goHrAgreeOvertime")
	public ModelAndView goHrAgreeOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyWorkOvertime.getId())) {
			sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
			req.setAttribute("sXyWorkOvertimePage", sXyWorkOvertime);
		}
		return new ModelAndView("xyoa/activiti/workovertime/sXyWorkOvertime-hrcheck");
	}
	
	
	/**
	 * HR否决享宇加班申请页面跳转
	 * @param sXyWorkOvertime
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goHrNotAgreeOvertime")
	public ModelAndView goHrNotAgreeOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyWorkOvertime.getId())) {
			sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
			req.setAttribute("sXyWorkOvertimePage", sXyWorkOvertime);
		}
		return new ModelAndView("xyoa/activiti/workovertime/sXyWorkOvertime-hrnocheck");
	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<SXyWorkOvertimeEntity> list() {
		List<SXyWorkOvertimeEntity> listSXyWorkOvertimes=sXyWorkOvertimeService.getList(SXyWorkOvertimeEntity.class);
		return listSXyWorkOvertimes;
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		SXyWorkOvertimeEntity task = sXyWorkOvertimeService.get(SXyWorkOvertimeEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody SXyWorkOvertimeEntity sXyWorkOvertime, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyWorkOvertimeEntity>> failures = validator.validate(sXyWorkOvertime);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyWorkOvertimeService.save(sXyWorkOvertime);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = sXyWorkOvertime.getId();
		URI uri = uriBuilder.path("/rest/sXyWorkOvertimeController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody SXyWorkOvertimeEntity sXyWorkOvertime) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyWorkOvertimeEntity>> failures = validator.validate(sXyWorkOvertime);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyWorkOvertimeService.saveOrUpdate(sXyWorkOvertime);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		sXyWorkOvertimeService.deleteEntityById(SXyWorkOvertimeEntity.class, id);
	}
}
