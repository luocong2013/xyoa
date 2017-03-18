package com.xy.oa.activiti.compensateleave.controller;


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

import com.xy.oa.activiti.compensateleave.entity.CompensateLeavePo;
import com.xy.oa.activiti.compensateleave.entity.SXyCompensateLeaveEntity;
import com.xy.oa.activiti.compensateleave.service.SXyCompensateLeaveServiceI;
import com.xy.oa.activiti.util.CommentBean;
import com.xy.oa.calendars.service.SXyCalendarsServiceI;
import com.xy.oa.checkinout.service.SXyCheckinoutServiceI;
import com.xy.oa.staff.service.StaffServiceI;
import com.xy.oa.util.ApplyTypeEnum;
import com.xy.oa.util.Constants;

/**   
 * @Title: Controller  
 * @Description: 享宇调休表
 * @author onlineGenerator
 * @date 2016-08-22 17:22:36
 * @version V1.0   
 *
 */
@Controller("sXyCompensateLeaveController")
@RequestMapping("/sXyCompensateLeaveController")
public class SXyCompensateLeaveController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SXyCompensateLeaveController.class);

	@Autowired
	private SXyCompensateLeaveServiceI sXyCompensateLeaveService;
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
	 * 显示在考勤记录表中，为调休中
	 * @param leaveId：调休申请表ID
	 * @param flag：1：显示在考勤记录表中，为调休中，2：归档、显示在考勤表中
	 */
	public void doSome(String leaveId, int flag) {
		SXyCompensateLeaveEntity sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, leaveId);
		if (flag == 1) {
			//显示在考勤表中，为调休中 
			
		} else if (flag == 2) {
			//减少用户的可调休天数（小时转换为天数）
			double leaveHour = sXyCompensateLeave.getLeaveHour().doubleValue() / 7.5;
			BigDecimal b = new BigDecimal(leaveHour);
			leaveHour = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
			staffService.reduceOffWorkCount(sXyCompensateLeave.getApplySttaffId(), leaveHour);
			//归档
			try {
				sXyCheckinoutService.disCheckInOut(sXyCompensateLeave.getApplySttaffId(), DateUtils.formatDate(sXyCompensateLeave.getStartTime()),
						DateUtils.formatDate(sXyCompensateLeave.getEndTime()), sXyCompensateLeave.getId(), Constants.XY_CHECK_TYPE_19, sXyCompensateLeave.getRemarks());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * 享宇调休表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyCompensateLeaveService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/compensateleave/sXyCompensateLeaveList");
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
	public void datagrid(SXyCompensateLeaveEntity sXyCompensateLeave,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SXyCompensateLeaveEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyCompensateLeave, request.getParameterMap());
		try{
			//自定义追加查询条件
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyCompensateLeaveService.findHqlRoleCodes(tsUser);
			String roleCode = null;
			if (roleCodes.size() == 1) {
				roleCode = roleCodes.get(0);
			} else {
				roleCode = roleCodes.get(1);
			}
			
			//部门查询
			String orgId = request.getParameter("orgId");
			if (StringUtil.isNotEmpty(orgId)) {
//				TSDepart tsDepart = sXyCompensateLeaveService.getEntity(TSDepart.class, orgId);
//				cq.eq("tsDept", tsDepart);
				TSDepart tsDepart = sXyCompensateLeaveService.getEntity(TSDepart.class, orgId);
				List<TSDepart> tSDepartList = tsDepart.getTSDeparts();
				tSDepartList.add(0, tsDepart);
				cq.in("tsDept", tSDepartList.toArray());
				
			}
			
			if(Constants.DM.equals(roleCode)) {
				List<TSUser> tsUsers = sXyCompensateLeaveService.findHqlTSUsersByUser(tsUser);
				if (tsUsers == null || tsUsers.isEmpty()) {
					cq.eq("tsUser", tsUser);
				} else {
					cq.or(cq.and(Restrictions.in("tsUser", tsUsers.toArray()), Restrictions.eq("flowState", "7")), Restrictions.eq("tsUser", tsUser));
				}
			} else if (Constants.HR.equals(roleCode) || Constants.HRDM.equals(roleCode) || Constants.VICE.equals(roleCode) || Constants.CEO.equals(roleCode)) {
				//查询普通员工、直接上级、人事专员、部门DM、人事DM和副总裁
				List<TSUser> tsUsers = sXyCompensateLeaveService.findHqlTSUsersByUR(tsUser);
				cq.or(cq.and(Restrictions.in("tsUser", tsUsers.toArray()), Restrictions.eq("flowState", "7")), Restrictions.eq("tsUser", tsUser));
			} else {
				//查询当前用户提交的出差申请
				cq.eq("tsUser", tsUser);
			}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyCompensateLeaveService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 直接上级、部门DM、副总裁、总裁审批页面
	 * 享宇审核调休表单 页面跳转
	 * @return
	 */
	@RequestMapping(params = "listCheck")
	public ModelAndView listCheck(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyCompensateLeaveService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/compensateleave/sXyCompensateLeaveListCheck");
	}
	
	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "datagridCheck")
	public void datagridCheck(SXyCompensateLeaveEntity sXyCompensateLeave,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String spflag) {
		CriteriaQuery cq = new CriteriaQuery(SXyCompensateLeaveEntity.class, dataGrid);
		//查询条件组装器
		String paramHql = org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHqlForFlow("S", cq, sXyCompensateLeave, request.getParameterMap());
		try{
			//自定义追加查询条件
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//部门查询
			String orgId = request.getParameter("orgId");
			
			List<CompensateLeavePo> lPos = new ArrayList<CompensateLeavePo>();
			int total = 0;
			int beginNum = (cq.getCurPage()-1)*cq.getPageSize();
			if ("00".equals(spflag) || "01".equals(spflag)) {
				if ("00".equals(spflag)) {
					//查询当前登录用户的任务（以用户编号(userName)查询）
					sXyCompensateLeaveService.getDataGridRunForHQL(SXyCompensateLeaveEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyCompensateLeaveService.getRunTotal(SXyCompensateLeaveEntity.class, false, paramHql, orgId, tsUser.getUserName());
				} else if ("01".equals(spflag)) {
					//查看当前用户的历史任务（已审批过的）
					sXyCompensateLeaveService.getDataGridHisForHQL(SXyCompensateLeaveEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyCompensateLeaveService.getHisTotal(SXyCompensateLeaveEntity.class, false, paramHql, orgId, tsUser.getUserName());
				}
				List<SXyCompensateLeaveEntity> leaveEntities = dataGrid.getResults();
				for (SXyCompensateLeaveEntity sLeaveEntity : leaveEntities) {
					CompensateLeavePo cPo = new CompensateLeavePo();
					BeanUtils.copyProperties(sLeaveEntity, cPo);
					cPo.setIsApprove(spflag);
					lPos.add(cPo);
				}
			} else {
				//查询当前登录用户的任务的总条数（以用户编号(userName)查询）
				int totalR = sXyCompensateLeaveService.getRunTotal(SXyCompensateLeaveEntity.class, false, paramHql, orgId, tsUser.getUserName());
				//查看当前用户的历史任务的总条数（已审批过的）
				int totalH = sXyCompensateLeaveService.getHisTotal(SXyCompensateLeaveEntity.class, false, paramHql, orgId, tsUser.getUserName());
				total = totalR + totalH;
				if (totalR <= beginNum) {
					DataGrid dataGrid2 = new DataGrid();
					beginNum  -= totalR;
					//查看当前用户的历史任务（已审批过的）
					sXyCompensateLeaveService.getDataGridHisForHQL(SXyCompensateLeaveEntity.class, dataGrid2, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyCompensateLeaveEntity> leaveEntitiesH = dataGrid2.getResults();
					for (SXyCompensateLeaveEntity sLeaveEntity : leaveEntitiesH) {
						CompensateLeavePo cPo = new CompensateLeavePo();
						BeanUtils.copyProperties(sLeaveEntity, cPo);
						cPo.setIsApprove("01");
						lPos.add(cPo);
					}
				} else {
					//查询当前登录用户的任务（以用户编号(userName)查询）
					sXyCompensateLeaveService.getDataGridRunForHQL(SXyCompensateLeaveEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyCompensateLeaveEntity> leaveEntitiesR = dataGrid.getResults();
					for (SXyCompensateLeaveEntity sLeaveEntity : leaveEntitiesR) {
						CompensateLeavePo cPo = new CompensateLeavePo();
						BeanUtils.copyProperties(sLeaveEntity, cPo);
						cPo.setIsApprove("00");
						lPos.add(cPo);
					}
					if (leaveEntitiesR.size() < cq.getPageSize()) {
						DataGrid dataGrid2 = new DataGrid();
						//查看当前用户的历史任务（已审批过的）
						sXyCompensateLeaveService.getDataGridHisForHQL(SXyCompensateLeaveEntity.class, dataGrid2, false, paramHql, orgId, 0, cq.getPageSize()-leaveEntitiesR.size(), tsUser.getUserName());
						List<SXyCompensateLeaveEntity> leaveEntitiesH = dataGrid2.getResults();
						for (SXyCompensateLeaveEntity sLeaveEntity : leaveEntitiesH) {
							CompensateLeavePo cPo = new CompensateLeavePo();
							BeanUtils.copyProperties(sLeaveEntity, cPo);
							cPo.setIsApprove("01");
							lPos.add(cPo);
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
	 * 享宇审核调休表单 页面跳转
	 * HR审批页面
	 * @return
	 */
	@RequestMapping(params = "listHr")
	public ModelAndView listHr(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyCompensateLeaveService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/compensateleave/sXyCompensateLeaveListHr");
	}
	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "datagridHr")
	public void datagridHr(SXyCompensateLeaveEntity sXyCompensateLeave,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String spflag) {
		CriteriaQuery cq = new CriteriaQuery(SXyCompensateLeaveEntity.class, dataGrid);
		//查询条件组装器
		String paramHql = org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHqlForFlow("S", cq, sXyCompensateLeave, request.getParameterMap());
		try{
			//自定义追加查询条件
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//部门查询
			String orgId = request.getParameter("orgId");
			
			List<CompensateLeavePo> lPos = new ArrayList<CompensateLeavePo>();
			int total = 0;
			int beginNum = (cq.getCurPage()-1)*cq.getPageSize();
			if ("00".equals(spflag) || "01".equals(spflag)) {
				if ("00".equals(spflag)) {
					//查询当前登录用户的任务（以用户编号(userName)查询）
					sXyCompensateLeaveService.getDataGridRunForHQL(SXyCompensateLeaveEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyCompensateLeaveService.getRunTotal(SXyCompensateLeaveEntity.class, true, paramHql, orgId, tsUser.getUserName());
				} else if ("01".equals(spflag)) {
					//查看当前用户的历史任务（已审批过的）
					sXyCompensateLeaveService.getDataGridHisForHQL(SXyCompensateLeaveEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyCompensateLeaveService.getHisTotal(SXyCompensateLeaveEntity.class, true, paramHql, orgId, tsUser.getUserName());
				}
				List<SXyCompensateLeaveEntity> leaveEntities = dataGrid.getResults();
				for (SXyCompensateLeaveEntity sLeaveEntity : leaveEntities) {
					CompensateLeavePo cPo = new CompensateLeavePo();
					BeanUtils.copyProperties(sLeaveEntity, cPo);
					cPo.setIsApprove(spflag);
					lPos.add(cPo);
				}
			} else {
				int totalR = sXyCompensateLeaveService.getRunTotal(SXyCompensateLeaveEntity.class, true, paramHql, orgId, tsUser.getUserName());
				//查看当前用户的历史任务的总条数（已审批过的）
				int totalH = sXyCompensateLeaveService.getHisTotal(SXyCompensateLeaveEntity.class, true, paramHql, orgId, tsUser.getUserName());
				total = totalR + totalH;
				if (totalR <= beginNum) {
					DataGrid dataGrid2 = new DataGrid();
					beginNum  -= totalR;
					//查看当前用户的历史任务（已审批过的）
					sXyCompensateLeaveService.getDataGridHisForHQL(SXyCompensateLeaveEntity.class, dataGrid2, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyCompensateLeaveEntity> leaveEntitiesH = dataGrid2.getResults();
					for (SXyCompensateLeaveEntity sLeaveEntity : leaveEntitiesH) {
						CompensateLeavePo cPo = new CompensateLeavePo();
						BeanUtils.copyProperties(sLeaveEntity, cPo);
						cPo.setIsApprove("01");
						lPos.add(cPo);
					}
				} else {
					//查询当前登录用户的任务（以用户编号(userName)查询）
					sXyCompensateLeaveService.getDataGridRunForHQL(SXyCompensateLeaveEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyCompensateLeaveEntity> leaveEntitiesR = dataGrid.getResults();
					for (SXyCompensateLeaveEntity sLeaveEntity : leaveEntitiesR) {
						CompensateLeavePo cPo = new CompensateLeavePo();
						BeanUtils.copyProperties(sLeaveEntity, cPo);
						cPo.setIsApprove("00");
						lPos.add(cPo);
					}
					if (leaveEntitiesR.size() < cq.getPageSize()) {
						DataGrid dataGrid2 = new DataGrid();
						//查看当前用户的历史任务（已审批过的）
						sXyCompensateLeaveService.getDataGridHisForHQL(SXyCompensateLeaveEntity.class, dataGrid2, true, paramHql, orgId, 0, cq.getPageSize()-leaveEntitiesR.size(), tsUser.getUserName());
						List<SXyCompensateLeaveEntity> leaveEntitiesH = dataGrid2.getResults();
						for (SXyCompensateLeaveEntity sLeaveEntity : leaveEntitiesH) {
							CompensateLeavePo cPo = new CompensateLeavePo();
							BeanUtils.copyProperties(sLeaveEntity, cPo);
							cPo.setIsApprove("01");
							lPos.add(cPo);
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
	 * 删除享宇调休表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		sXyCompensateLeave = systemService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
		message = "享宇调休表删除成功";
		try{
			sXyCompensateLeaveService.delete(sXyCompensateLeave);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇调休表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除享宇调休表
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇调休表删除成功";
		try{
			for(String id:ids.split(",")){
				SXyCompensateLeaveEntity sXyCompensateLeave = systemService.getEntity(SXyCompensateLeaveEntity.class, id);
				sXyCompensateLeaveService.delete(sXyCompensateLeave);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇调休表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加享宇调休表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		try{
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户所属的部门
			TSDepart tsDepart = sXyCompensateLeaveService.findHqlTSDepart(tsUser);
			//设置申请人
			sXyCompensateLeave.setTsUser(tsUser);
			//设置申请人编号
			sXyCompensateLeave.setApplySttaffId(Integer.valueOf(tsUser.getUserName()));
			//设置申请人部门
			sXyCompensateLeave.setTsDept(tsDepart);
			//判断调休时长是否超出 可调休的时长
			BigDecimal b = new BigDecimal(staffService.getOffWorkCount(Integer.valueOf(tsUser.getUserName())) * 7.5);
			if(sXyCompensateLeave.getApplyLeaveHour().doubleValue() > b.setScale(1, BigDecimal.ROUND_HALF_EVEN).doubleValue()) {
				message = "享宇调休申请添加失败，你申请的调休时长已经超出了你可用的调休时长";
				throw new BusinessException(message);
			}
			message = "享宇调休申请添加成功";
			//设置调休开始时间
			sXyCompensateLeave.setStartTime(sXyCompensateLeave.getLeaveStartTime());
			//设置创建 时间
			sXyCompensateLeave.setCreateTime(new Date());
			//设置创建人编号
			sXyCompensateLeave.setCUser(Integer.valueOf(tsUser.getUserName()));
			/**
			 * 流程状态：0：流程未启动、1：调休待审批、2：调休审批中、3：调休申请被否决、4：调休审批通过、
			 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
			 */
			sXyCompensateLeave.setFlowState("0");
			sXyCompensateLeaveService.save(sXyCompensateLeave);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇调休申请添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新享宇调休表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		SXyCompensateLeaveEntity t = sXyCompensateLeaveService.get(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		try {
			//判断调休时长是否超出 可调休的时长
			BigDecimal b = new BigDecimal(staffService.getOffWorkCount(Integer.valueOf(tsUser.getUserName())) * 7.5);
			if(sXyCompensateLeave.getApplyLeaveHour().doubleValue() > b.setScale(1, BigDecimal.ROUND_HALF_EVEN).doubleValue()) {
				message = "享宇调休申请更新失败，你申请的调休时长已经超出了你可用的调休时长";
				throw new BusinessException(message);
			}
			message = "享宇调休申请更新成功";
			//设置调休开始时间
			sXyCompensateLeave.setStartTime(sXyCompensateLeave.getLeaveStartTime());
			//设置更新时间
			sXyCompensateLeave.setUTime(new Date());
			//设置更新人编号
			sXyCompensateLeave.setUUser(Integer.valueOf(tsUser.getUserName()));
			MyBeanUtils.copyBeanNotNull2Bean(sXyCompensateLeave, t);
			sXyCompensateLeaveService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇调休申请更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 提交享宇调休表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doSubmitLeave")
	@ResponseBody
	public AjaxJson doSubmitLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		try {
			//审批人（用户编号(userName)）
			String feaaApprover = request.getParameter("feaaApprover");
			//获取调休表信息
			sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			BigDecimal b = new BigDecimal(staffService.getOffWorkCount(Integer.valueOf(tsUser.getUserName())) * 7.5);
			if(sXyCompensateLeave.getApplyLeaveHour().doubleValue() > b.setScale(1, BigDecimal.ROUND_HALF_EVEN).doubleValue()) {
				message = "享宇调休申请提交失败，你申请的调休时长已经超出了你可用的调休时长";
				throw new BusinessException(message);
			}
			message = "享宇调休申请提交成功";
			//查询当前用户的角色编码
			List<String> roleCodes = sXyCompensateLeaveService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			//查询当前用户所属的部门
			TSDepart tsDepart = sXyCompensateLeaveService.findHqlTSDepart(tsUser);
			//机构编码长度
			int orgCodeLength = tsDepart.getOrgCode().length();
			Map<String, Object> variables = new HashMap<String, Object>();
			//向流程中传入数据
			//传入调休表的ID（归档、显示在考勤异常表中等操作）
			variables.put("leaveId", sXyCompensateLeave.getId());
			//判断流程走向
			if (Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 12) {
				variables.put("startLine", 1);
				variables.put(Constants.HEADMAN, feaaApprover);
			}
			if ((Constants.EMPLOYEE.equals(roleCode) || Constants.HR.equals(roleCode)) && orgCodeLength == 9) {
				//查询当前部门下的直接上级
				List<TSUser> tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
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
			/**
			 * 流程状态：0：流程未启动、1：调休待审批、2：调休审批中、3：调休申请被否决、4：调休审批通过、
			 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
			 */
			sXyCompensateLeave.setFlowState("1");
			//设置提交申请日期
			sXyCompensateLeave.setApplyDate(new Date());
			//设置申请编号
			sXyCompensateLeave.setApplyNo(sXyCompensateLeaveService.getNextApplyNo(ApplyTypeEnum.TX, "s_xy_compensate_leave"));
			//启动流程（以用户编号(userName)作为启动人）
			String flowInstId = sXyCompensateLeaveService.startFlow("xyCompensateLeave", sXyCompensateLeave.getId(), variables, tsUser.getUserName());
			//设置流程实例ID
			sXyCompensateLeave.setFlowInstId(flowInstId);
			//保存
			sXyCompensateLeaveService.saveOrUpdate(sXyCompensateLeave);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇调休申请提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：批准享宇调休申请
	 * @param sXyCompensateLeave
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAgreeLeave")
	@ResponseBody
	public AjaxJson doAgreeLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇调休表批准成功";
		//审批人（用户编号(userName)）
		String nextApprover = request.getParameter("nextApprover");
		//批准原因
		String passReason = request.getParameter("passReason");
		//获取调休表信息
		sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
		/**
		 * 流程状态：0：流程未启动、1：调休待审批、2：调休审批中、3：调休申请被否决、4：调休审批通过、
		 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyCompensateLeave.setFlowState("2");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户的角色编码
		List<String> roleCodes = sXyCompensateLeaveService.findHqlRoleCodes(tsUser);
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
			sXyCompensateLeaveService.completeTask(sXyCompensateLeave.getFlowInstId(), variables, tsUser.getUserName(), passReason);
			//保存状态
			sXyCompensateLeaveService.saveOrUpdate(sXyCompensateLeave);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇调休表批准失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：否决享宇调休申请
	 * @param sXyCompensateLeave
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doNotAgreeLeave")
	@ResponseBody
	public AjaxJson doNotAgreeLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇调休表否决成功";
		//否决原因
		String nopassReason = request.getParameter("nopassReason");
		//获取调休表信息
		sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
		/**
		 * 流程状态：0：流程未启动、1：调休待审批、2：调休审批中、3：调休申请被否决、4：调休审批通过、
		 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyCompensateLeave.setFlowState("3");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("isPass", 0);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyCompensateLeaveService.completeTask(sXyCompensateLeave.getFlowInstId(), variables, tsUser.getUserName(), nopassReason);
			//保存状态
			sXyCompensateLeaveService.saveOrUpdate(sXyCompensateLeave);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇调休表否决失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * HR：批准享宇调休申请
	 * @param sXyCompensateLeave
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doHrAgreeLeave")
	@ResponseBody
	public AjaxJson doHrAgreeLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇调休表批准成功";
		//批准原因
		String hrpassReason = request.getParameter("hrpassReason");
		//获取调休表信息
		sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
		/**
		 * 流程状态：0：流程未启动、1：调休待审批、2：调休审批中、3：调休申请被否决、4：调休审批通过、
		 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		if("2".equals(sXyCompensateLeave.getFlowState())) {
			sXyCompensateLeave.setFlowState("4");
			variables.put("isPass", 3);
		}
		if("5".equals(sXyCompensateLeave.getFlowState())) {
			sXyCompensateLeave.setFlowState("7");
			variables.put("isPass", 6);
		}
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyCompensateLeaveService.completeTask(sXyCompensateLeave.getFlowInstId(), variables, tsUser.getUserName(), hrpassReason);
			//保存状态
			sXyCompensateLeaveService.saveOrUpdate(sXyCompensateLeave);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇调休表批准失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * HR：否决享宇调休申请
	 * @param sXyCompensateLeave
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doHrNotAgreeLeave")
	@ResponseBody
	public AjaxJson doHrNotAgreeLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇调休表否决成功";
		//否决原因
		String hrnopassReason = request.getParameter("hrnopassReason");
		//获取调休表信息
		sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
		/**
		 * 流程状态：0：流程未启动、1：调休待审批、2：调休审批中、3：调休申请被否决、4：调休审批通过、
		 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		if("2".equals(sXyCompensateLeave.getFlowState())) {
			sXyCompensateLeave.setFlowState("3");
			variables.put("isPass", 0);
		}
		if("5".equals(sXyCompensateLeave.getFlowState())) {
			sXyCompensateLeave.setFlowState("6");
			variables.put("isPass", 7);
		}
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyCompensateLeaveService.completeTask(sXyCompensateLeave.getFlowInstId(), variables, tsUser.getUserName(), hrnopassReason);
			//保存状态
			sXyCompensateLeaveService.saveOrUpdate(sXyCompensateLeave);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇调休表否决失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 享宇调休表 提交销假申请
	 * @param sXyCompensateLeave
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "dobackLeave")
	@ResponseBody
	public AjaxJson dobackLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		try {
			//获取出差表信息
			SXyCompensateLeaveEntity t = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
			/**
			 * 流程状态：0：流程未启动、1：调休待审批、2：调休审批中、3：调休申请被否决、4：调休审批通过、
			 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
			 */
			sXyCompensateLeave.setFlowState("5");
			//设置调休结束时间
			sXyCompensateLeave.setEndTime(sXyCompensateLeave.getBackDate());
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			Map<String, Object> variables = new HashMap<String, Object>();
			//计算真实调休时长
			BigDecimal bigDecimal = sXyCalendarsService.getTimeInterval(t.getStartTime(), sXyCompensateLeave.getEndTime(), true);
			if(bigDecimal.doubleValue() > staffService.getOffWorkCount(Integer.valueOf(tsUser.getUserName())) * 7.5) {
				message = "销假申请提交失败，你销假的时长已经超出了你可用的调休时长，请重新设置销假时间";
				throw new BusinessException(message);
			}
			message = "销假申请提交成功";
			//设置真实调休时长
			sXyCompensateLeave.setLeaveHour(bigDecimal);
			MyBeanUtils.copyBeanNotNull2Bean(sXyCompensateLeave, t);
			//完成任务（以用户编号(userName)完成任务）
			sXyCompensateLeaveService.completeTask(t.getFlowInstId(), variables, tsUser.getUserName(), null);
			//保存状态
			sXyCompensateLeaveService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "销假申请提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 查看流程图片
	 * @param sXyCompensateLeave
	 * @param response
	 */
	@RequestMapping(params = "lookFlowImage")
	public void lookFlowImage(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletResponse response) {
		try {
			response.setContentType("image/png");
			InputStream inputStream = sXyCompensateLeaveService.graphics(sXyCompensateLeave.getFlowInstId());
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
	 * 重新提交调休申请
	 * @param sXyCompensateLeave
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "reSubmitLeave")
	@ResponseBody
	public AjaxJson reSubmitLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇调休表重新提交成功";
		//获取出差表信息
		sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
		/**
		 * 流程状态：0：流程未启动、1：调休待审批、2：调休审批中、3：调休申请被否决、4：调休审批通过、
		 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyCompensateLeave.setFlowState("1");
		//重新设置申请时间
		sXyCompensateLeave.setApplyDate(new Date());
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("reApply", true);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyCompensateLeaveService.completeTask(sXyCompensateLeave.getFlowInstId(), variables, tsUser.getUserName(), null);
			//保存状态
			sXyCompensateLeaveService.saveOrUpdate(sXyCompensateLeave);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇调休表重新提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 撤销调休申请
	 * @param sXyCompensateLeave
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delLeave")
	@ResponseBody
	public AjaxJson delLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇调休申请撤销成功";
		//获取出差表信息
		sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
		/**
		 * 流程状态：0：流程未启动、1：调休待审批、2：调休审批中、3：调休申请被否决、4：调休审批通过、
		 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyCompensateLeave.setFlowState("8");
		try {
			//撤销申请
			sXyCompensateLeaveService.delFlow(sXyCompensateLeave.getFlowInstId());
			//保存状态
			sXyCompensateLeaveService.saveOrUpdate(sXyCompensateLeave);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇调休申请撤销失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 取消提交调休申请
	 * @param sXyCompensateLeave
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "cancelLeave")
	@ResponseBody
	public AjaxJson cancelLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇调休表取消提交成功";
		//获取出差表信息
		sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
		/**
		 * 流程状态：0：流程未启动、1：调休待审批、2：调休审批中、3：调休申请被否决、4：调休审批通过、
		 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyCompensateLeave.setFlowState("9");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("reApply", false);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyCompensateLeaveService.completeTask(sXyCompensateLeave.getFlowInstId(), variables, tsUser.getUserName(), null);
			//保存状态
			sXyCompensateLeaveService.saveOrUpdate(sXyCompensateLeave);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇调休表取消提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 享宇调休表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest req) {
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户所属的部门
		TSDepart tsDepart = sXyCompensateLeaveService.findHqlTSDepart(tsUser);
		req.setAttribute("tsUserPage", tsUser);
		req.setAttribute("tsDepartPage", tsDepart);
		return new ModelAndView("xyoa/activiti/compensateleave/sXyCompensateLeave-add");
	}
	
	/**
	 * 享宇调休表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCompensateLeave.getId())) {
			sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
			req.setAttribute("sXyCompensateLeavePage", sXyCompensateLeave);
		}
		return new ModelAndView("xyoa/activiti/compensateleave/sXyCompensateLeave-update");
	}
	
	
	/**
	 * 享宇调休表详情页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goDetail")
	public ModelAndView goDetail(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCompensateLeave.getId())) {
			try {
				//调休表信息
				sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
				//业务表历史任务节点批注信息
				List<CommentBean> commentBeans = sXyCompensateLeaveService.findComment(sXyCompensateLeave.getFlowInstId());
				req.setAttribute("sXyCompensateLeavePage", sXyCompensateLeave);
				req.setAttribute("commentBeansPage", commentBeans);
			} catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}
		}
		return new ModelAndView("xyoa/activiti/compensateleave/sXyCompensateLeave-detail");
	}
	
	
	/**
	 * 享宇调休表提交申请 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goSubmitLeave")
	public ModelAndView goSubmitLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCompensateLeave.getId())) {
			//获取调休表信息
			sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyCompensateLeaveService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			//查询当前用户所属的部门
			TSDepart tsDepart = sXyCompensateLeaveService.findHqlTSDepart(tsUser);
			//机构编码长度
			int orgCodeLength = tsDepart.getOrgCode().length();
			//查询该部门下的领导信息
			List<TSUser> tsUsers = null;
			if (Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 12) {
				tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的直接上级
					TSDepart tsPDepart = sXyCompensateLeaveService.findHqlTSPDepart(tsUser);
					tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(Constants.HEADMAN, tsPDepart);
				}
			}
			if ((Constants.EMPLOYEE.equals(roleCode) || Constants.HR.equals(roleCode)) && orgCodeLength == 9) {
				//查询当前部门下的直接上级
				tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前部门下的部门DM
					tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(Constants.DM, tsDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的部门DM
						TSDepart tsPDepart = sXyCompensateLeaveService.findHqlTSPDepart(tsUser);
						tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					}
				}
			}
			if (Constants.HEADMAN.equals(roleCode)) {
				tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(Constants.DM, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的部门DM
					TSDepart tsPDepart = sXyCompensateLeaveService.findHqlTSPDepart(tsUser);
					tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的上一级部门的部门DM
						TSDepart tsPPDepart = sXyCompensateLeaveService.findHqlTSPPDepart(tsUser);
						tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(Constants.DM, tsPPDepart);
					}
				}
			}
			if(Constants.DM.equals(roleCode) || Constants.HRDM.equals(roleCode)) {
				tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRoleCode(Constants.VICE);
			}
			if(Constants.VICE.equals(roleCode)) {
				tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRoleCode(Constants.CEO);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyCompensateLeavePage", sXyCompensateLeave);
		}
		return new ModelAndView("xyoa/activiti/compensateleave/sXyCompensateLeave-submit");
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 享宇调休表批准调休申请 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAgreeLeave")
	public ModelAndView goAgreeLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCompensateLeave.getId())) {
			//获取调休表信息
			sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyCompensateLeaveService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			//查询当前用户的下一级审批人
			List<TSUser> tsUsers = null;
			if(Constants.HEADMAN.equals(roleCode)) {
				//查询当前用户所属的部门
				TSDepart tsDepart = sXyCompensateLeaveService.findHqlTSDepart(tsUser);
				tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(Constants.DM, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户的上一级部门的部门DM
					TSDepart tsPDepart = sXyCompensateLeaveService.findHqlTSPDepart(tsUser);
					tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的上一级部门的部门DM
						TSDepart tsPPDepart = sXyCompensateLeaveService.findHqlTSPPDepart(tsUser);
						tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(Constants.DM, tsPPDepart);
					}
				}
			} else {
				//查询HRDM
				tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRoleCode(Constants.HRDM);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyCompensateLeavePage", sXyCompensateLeave);
		}
		return new ModelAndView("xyoa/activiti/compensateleave/sXyCompensateLeave-check");
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 享宇调休表否决调休申请 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goNotAgreeLeave")
	public ModelAndView goNotAgreeLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCompensateLeave.getId())) {
			sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
			req.setAttribute("sXyCompensateLeavePage", sXyCompensateLeave);
		}
		return new ModelAndView("xyoa/activiti/compensateleave/sXyCompensateLeave-nocheck");
	}
	
	
	/**
	 * HR
	 * 享宇调休表 批准调休申请 页面跳转
	 * @return
	 */
	@RequestMapping(params = "goHrAgreeLeave")
	public ModelAndView goHrAgreeLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCompensateLeave.getId())) {
			sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
			req.setAttribute("sXyCompensateLeavePage", sXyCompensateLeave);
		}
		return new ModelAndView("xyoa/activiti/compensateleave/sXyCompensateLeave-hrcheck");
	}
	
	
	/**
	 * HR
	 * 享宇调休表 否决调休申请 页面跳转
	 * @return
	 */
	@RequestMapping(params = "goHrNotAgreeLeave")
	public ModelAndView goHrNotAgreeLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCompensateLeave.getId())) {
			sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
			req.setAttribute("sXyCompensateLeavePage", sXyCompensateLeave);
		}
		return new ModelAndView("xyoa/activiti/compensateleave/sXyCompensateLeave-hrnocheck");
	}
	
	
	/**
	 * 享宇调休表销假 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "gobackLeave")
	public ModelAndView gobackLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCompensateLeave.getId())) {
			sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
			req.setAttribute("sXyCompensateLeavePage", sXyCompensateLeave);
		}
		return new ModelAndView("xyoa/activiti/compensateleave/sXyCompensateLeave-back");
	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<SXyCompensateLeaveEntity> list() {
		List<SXyCompensateLeaveEntity> listSXyCompensateLeaves=sXyCompensateLeaveService.getList(SXyCompensateLeaveEntity.class);
		return listSXyCompensateLeaves;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		SXyCompensateLeaveEntity task = sXyCompensateLeaveService.get(SXyCompensateLeaveEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody SXyCompensateLeaveEntity sXyCompensateLeave, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyCompensateLeaveEntity>> failures = validator.validate(sXyCompensateLeave);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyCompensateLeaveService.save(sXyCompensateLeave);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = sXyCompensateLeave.getId();
		URI uri = uriBuilder.path("/rest/sXyCompensateLeaveController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody SXyCompensateLeaveEntity sXyCompensateLeave) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyCompensateLeaveEntity>> failures = validator.validate(sXyCompensateLeave);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyCompensateLeaveService.saveOrUpdate(sXyCompensateLeave);
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
		sXyCompensateLeaveService.deleteEntityById(SXyCompensateLeaveEntity.class, id);
	}
}
