package com.xy.oa.activiti.absence.controller;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
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
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.poi.util.PoiPublicUtil;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.xy.oa.activiti.absence.entity.AbsencePo;
import com.xy.oa.activiti.absence.entity.SXyAbsenceEntity;
import com.xy.oa.activiti.absence.service.SXyAbsenceServiceI;
import com.xy.oa.activiti.service.POIServiceI;
import com.xy.oa.activiti.util.CommentBean;
import com.xy.oa.calendars.service.SXyCalendarsServiceI;
import com.xy.oa.checkinout.service.SXyCheckinoutServiceI;
import com.xy.oa.staff.entity.StaffEntity;
import com.xy.oa.util.ApplyTypeEnum;
import com.xy.oa.util.Constants;
import com.xy.oa.util.FileMetaSub;

/**   
 * @Title: Controller  
 * @Description: 享宇请假表
 * @author onlineGenerator
 * @date 2016-08-11 15:54:46
 * @version V1.0   
 *
 */
@Controller("sXyAbsenceController")
@RequestMapping("/sXyAbsenceController")
public class SXyAbsenceController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SXyAbsenceController.class);

	@Autowired
	private SXyAbsenceServiceI sXyAbsenceService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private SXyCalendarsServiceI sXyCalendarsService;
	@Autowired
	private SXyCheckinoutServiceI sXyCheckinoutService;
	
	
	@Autowired
	private POIServiceI poiService;
	
	/**
	 * 显示在考勤记录表中，为请假中
	 * 归档显示在考勤记录表
	 * @param absenceId：请假申请表ID
	 * @param flag：1：显示在考勤记录表中，为请假中，2：归档显示在考勤记录表
	 */
	public void doSome(String absenceId, int flag) {
		try {
			SXyAbsenceEntity sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, absenceId);
			if (flag == 1) {
				//显示在考勤表中，为请假中 
				
			} else if (flag == 2) {
				//归档
				if (Constants.XY_CHECK_TYPE_08.equals(sXyAbsence.getAbsenceType())) {
					StaffEntity staffEntity = sXyAbsenceService.findUniqueByProperty(StaffEntity.class, "sttaffId", sXyAbsence.getApplySttaffId());
					//减少年假天数（小时数转换为天数）
					double absenceDay = sXyAbsence.getAbsenceDay() / 7.5;
					BigDecimal b = new BigDecimal(absenceDay);
					absenceDay = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
					staffEntity.setLeaveCount(staffEntity.getLeaveCount() - absenceDay);
				}
				sXyCheckinoutService.disCheckInOut(sXyAbsence.getApplySttaffId(), DateUtils.formatDate(sXyAbsence.getStartTime()),
						DateUtils.formatDate(sXyAbsence.getBackDate()), sXyAbsence.getId(), sXyAbsence.getAbsenceType(), sXyAbsence.getRemarks());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 享宇请假表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyAbsenceService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/absence/sXyAbsenceList");
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
	public void datagrid(SXyAbsenceEntity sXyAbsence,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SXyAbsenceEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyAbsence, request.getParameterMap());
		try{
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyAbsenceService.findHqlRoleCodes(tsUser);
			String roleCode = null;
			if (roleCodes.size() == 1) {
				roleCode = roleCodes.get(0);
			} else {
				roleCode = roleCodes.get(1);
			}
			//部门查询
			String orgId = request.getParameter("orgId");
			if (StringUtil.isNotEmpty(orgId)) {
				TSDepart tsDepart = sXyAbsenceService.getEntity(TSDepart.class, orgId);
				List<TSDepart> tSDepartList = tsDepart.getTSDeparts();
				tSDepartList.add(0, tsDepart);
				cq.in("tsDept", tSDepartList.toArray());
			}
			
			if(Constants.DM.equals(roleCode)) {
				//查询该部门下的所有员工以及各种组的所有员工
				List<TSUser> tsUsers = sXyAbsenceService.findHqlTSUsersByUser(tsUser);
				if (tsUsers == null || tsUsers.isEmpty()) {
					cq.eq("tsUser", tsUser);
				} else {
					cq.or(cq.and(Restrictions.in("tsUser", tsUsers.toArray()), Restrictions.eq("flowState", "7")), Restrictions.eq("tsUser", tsUser));
				}
			} else if (Constants.HR.equals(roleCode) || Constants.HRDM.equals(roleCode) || Constants.VICE.equals(roleCode) || Constants.CEO.equals(roleCode)) {
				//查询普通员工、直接上级、人事专员、部门DM、人事DM和副总裁
				List<TSUser> tsUsers = sXyAbsenceService.findHqlTSUsersByUR(tsUser);
				cq.or(cq.and(Restrictions.in("tsUser", tsUsers.toArray()), Restrictions.eq("flowState", "7")), Restrictions.eq("tsUser", tsUser));
			} else {
				//查询当前用户提交的请假申请
				cq.eq("tsUser", tsUser);
			}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyAbsenceService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 享宇审核请假表单 页面跳转
	 * 直接上级、部门DM、副总裁、总裁审批页面
	 * @return
	 */
	@RequestMapping(params = "listCheck")
	public ModelAndView listCheck(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyAbsenceService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/absence/sXyAbsenceListCheck");
	}
	/**
	 * Ajax数据请求
	 * @param sXyAbsence
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridCheck")
	public void datagridCheck(SXyAbsenceEntity sXyAbsence, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String spflag) {
		CriteriaQuery cq = new CriteriaQuery(SXyAbsenceEntity.class, dataGrid);
		//查询条件组装器
		String paramHql = org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHqlForFlow("S", cq, sXyAbsence, request.getParameterMap());
		try {
			//自定义追加查询条件
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//部门查询
			String orgId = request.getParameter("orgId");
			
			List<AbsencePo> lPos = new ArrayList<AbsencePo>();
			int total = 0;
			int beginNum = (cq.getCurPage()-1)*cq.getPageSize();
			if ("00".equals(spflag) || "01".equals(spflag)) {
				if ("00".equals(spflag)) {
					//查询当前登录用户的任务（以用户编号(userName)查询）
					sXyAbsenceService.getDataGridRunForHQL(SXyAbsenceEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyAbsenceService.getRunTotal(SXyAbsenceEntity.class, false, paramHql, orgId, tsUser.getUserName());
				} else if ("01".equals(spflag)) {
					//查看当前用户的历史任务（已审批过的）
					sXyAbsenceService.getDataGridHisForHQL(SXyAbsenceEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyAbsenceService.getHisTotal(SXyAbsenceEntity.class, false, paramHql, orgId, tsUser.getUserName());
				}
				List<SXyAbsenceEntity> lAbsences = dataGrid.getResults();
				for (SXyAbsenceEntity sAbsence : lAbsences) {
					AbsencePo aPo = new AbsencePo();
					BeanUtils.copyProperties(sAbsence, aPo);
					aPo.setIsApprove(spflag);
					lPos.add(aPo);
				}
			} else {
				//查询当前登录用户的任务的总条数（以用户编号(userName)查询）
				int totalR = sXyAbsenceService.getRunTotal(SXyAbsenceEntity.class, false, paramHql, orgId, tsUser.getUserName());
				//查看当前用户的历史任务的总条数（已审批过的）
				int totalH = sXyAbsenceService.getHisTotal(SXyAbsenceEntity.class, false, paramHql, orgId, tsUser.getUserName());
				total = totalR + totalH;
				if (totalR <= beginNum) {
					DataGrid dataGrid2 = new DataGrid();
					beginNum  -= totalR;
					//查看当前用户的历史任务（已审批过的）
					sXyAbsenceService.getDataGridHisForHQL(SXyAbsenceEntity.class, dataGrid2, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyAbsenceEntity> lAbsencesH = dataGrid2.getResults();
					for (SXyAbsenceEntity sAbsence : lAbsencesH) {
						AbsencePo aPo = new AbsencePo();
						BeanUtils.copyProperties(sAbsence, aPo);
						aPo.setIsApprove("01");
						lPos.add(aPo);
					}
				} else {
					//查询当前登录用户的任务（以用户编号(userName)查询）
					sXyAbsenceService.getDataGridRunForHQL(SXyAbsenceEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyAbsenceEntity> lAbsencesR = dataGrid.getResults();
					for (SXyAbsenceEntity sAbsence : lAbsencesR) {
						AbsencePo aPo = new AbsencePo();
						BeanUtils.copyProperties(sAbsence, aPo);
						aPo.setIsApprove("00");
						lPos.add(aPo);
					}
					if (lAbsencesR.size() < cq.getPageSize()) {
						DataGrid dataGrid2 = new DataGrid();
						//查看当前用户的历史任务（已审批过的）
						sXyAbsenceService.getDataGridHisForHQL(SXyAbsenceEntity.class, dataGrid2, false, paramHql, orgId, 0, cq.getPageSize()-lAbsencesR.size(), tsUser.getUserName());
						List<SXyAbsenceEntity> lAbsencesH = dataGrid2.getResults();
						for (SXyAbsenceEntity sAbsence : lAbsencesH) {
							AbsencePo aPo = new AbsencePo();
							BeanUtils.copyProperties(sAbsence, aPo);
							aPo.setIsApprove("01");
							lPos.add(aPo);
						}
					}
				}
			}
			dataGrid.setResults(lPos);
			dataGrid.setTotal(total);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		TagUtil.datagrid(response, dataGrid);
	}
	/**
	 * 享宇审核请假表单 页面跳转
	 * HR审批页面
	 * @return
	 */
	@RequestMapping(params = "listHr")
	public ModelAndView listHr(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyAbsenceService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/absence/sXyAbsenceListHr");
	}
	/**
	 * Ajax数据请求
	 * @param sXyAbsence
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridHr")
	public void datagridHr(SXyAbsenceEntity sXyAbsence, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String spflag) {
		CriteriaQuery cq = new CriteriaQuery(SXyAbsenceEntity.class, dataGrid);
		//查询条件组装器
		String paramHql = org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHqlForFlow("S", cq, sXyAbsence, request.getParameterMap());
		try {
			//自定义追加查询条件
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//部门查询
			String orgId = request.getParameter("orgId");
			
			List<AbsencePo> lPos = new ArrayList<AbsencePo>();
			int total = 0;
			int beginNum = (cq.getCurPage()-1)*cq.getPageSize();
			if ("00".equals(spflag) || "01".equals(spflag)) {
				if ("00".equals(spflag)) {
					//查询当前登录用户的任务（以用户编号(userName)查询）
					sXyAbsenceService.getDataGridRunForHQL(SXyAbsenceEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyAbsenceService.getRunTotal(SXyAbsenceEntity.class, true, paramHql, orgId, tsUser.getUserName());
				} else if ("01".equals(spflag)) {
					//查看当前用户的历史任务（已审批过的）
					sXyAbsenceService.getDataGridHisForHQL(SXyAbsenceEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyAbsenceService.getHisTotal(SXyAbsenceEntity.class, true, paramHql, orgId, tsUser.getUserName());
				}
				List<SXyAbsenceEntity> lAbsences = dataGrid.getResults();
				for (SXyAbsenceEntity sAbsence : lAbsences) {
					AbsencePo aPo = new AbsencePo();
					BeanUtils.copyProperties(sAbsence, aPo);
					aPo.setIsApprove(spflag);
					lPos.add(aPo);
				}
			} else {
				//查询当前登录用户的任务的总条数（以用户编号(userName)查询）
				int totalR = sXyAbsenceService.getRunTotal(SXyAbsenceEntity.class, true, paramHql, orgId, tsUser.getUserName());
				//查看当前用户的历史任务的总条数（已审批过的）
				int totalH = sXyAbsenceService.getHisTotal(SXyAbsenceEntity.class, true, paramHql, orgId, tsUser.getUserName());
				total = totalR + totalH;
				if (totalR <= beginNum) {
					DataGrid dataGrid2 = new DataGrid();
					beginNum  -= totalR;
					//查看当前用户的历史任务（已审批过的）
					sXyAbsenceService.getDataGridHisForHQL(SXyAbsenceEntity.class, dataGrid2, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyAbsenceEntity> lAbsencesH = dataGrid2.getResults();
					for (SXyAbsenceEntity sAbsence : lAbsencesH) {
						AbsencePo aPo = new AbsencePo();
						BeanUtils.copyProperties(sAbsence, aPo);
						aPo.setIsApprove("01");
						lPos.add(aPo);
					}
				} else {
					//查询当前登录用户的任务（以用户编号(userName)查询）
					sXyAbsenceService.getDataGridRunForHQL(SXyAbsenceEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyAbsenceEntity> lAbsencesR = dataGrid.getResults();
					for (SXyAbsenceEntity sAbsence : lAbsencesR) {
						AbsencePo aPo = new AbsencePo();
						BeanUtils.copyProperties(sAbsence, aPo);
						aPo.setIsApprove("00");
						lPos.add(aPo);
					}
					if (lAbsencesR.size() < cq.getPageSize()) {
						DataGrid dataGrid2 = new DataGrid();
						//查看当前用户的历史任务（已审批过的）
						sXyAbsenceService.getDataGridHisForHQL(SXyAbsenceEntity.class, dataGrid2, true, paramHql, orgId, 0, cq.getPageSize()-lAbsencesR.size(), tsUser.getUserName());
						List<SXyAbsenceEntity> lAbsencesH = dataGrid2.getResults();
						for (SXyAbsenceEntity sAbsence : lAbsencesH) {
							AbsencePo aPo = new AbsencePo();
							BeanUtils.copyProperties(sAbsence, aPo);
							aPo.setIsApprove("01");
							lPos.add(aPo);
						}
					}
				}
			}
			dataGrid.setResults(lPos);
			dataGrid.setTotal(total);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除享宇请假表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		sXyAbsence = systemService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
		message = "享宇请假表删除成功";
		try{
			sXyAbsenceService.delete(sXyAbsence);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇请假表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除享宇请假表
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇请假表删除成功";
		try{
			for(String id:ids.split(",")){
				SXyAbsenceEntity sXyAbsence = systemService.getEntity(SXyAbsenceEntity.class, id);
				sXyAbsenceService.delete(sXyAbsence);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇请假表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加享宇请假表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		try{
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的部门
			TSDepart tsDepart = sXyAbsenceService.findHqlTSDepart(tsUser);
			//设置申请人
			sXyAbsence.setTsUser(tsUser);
			//设置申请人编号
			sXyAbsence.setApplySttaffId(Integer.valueOf(tsUser.getUserName()));
			//设置部门
			sXyAbsence.setTsDept(tsDepart);
			if (Constants.XY_CHECK_TYPE_08.equals(sXyAbsence.getAbsenceType())) {
				StaffEntity staffEntity = sXyAbsenceService.findUniqueByProperty(StaffEntity.class, "sttaffId", Integer.valueOf(tsUser.getUserName()));
				if(sXyAbsence.getApplyAbsenceDay() > staffEntity.getLeaveCount() * 7.5) {
					message = "享宇请假申请添加失败，你申请的年假时长已经超出了你可用的年假时长";
					throw new BusinessException(message);
				}
			}
			message = "享宇请假表添加成功";
			//设置创建时间
			sXyAbsence.setCreateTime(new Date());
			//设置创建人
			sXyAbsence.setCUser(Integer.valueOf(tsUser.getUserName()));
			/**
			 * 流程状态：0：流程未启动、1：待审批、2：请假审批中、3：请假申请被否决、
			 * 4：请假审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
			 */
			sXyAbsence.setFlowState("0");
			sXyAbsenceService.save(sXyAbsence);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇请假表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新享宇请假表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		try {
			SXyAbsenceEntity t = sXyAbsenceService.get(SXyAbsenceEntity.class, sXyAbsence.getId());
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			if(Constants.XY_CHECK_TYPE_08.equals(sXyAbsence.getAbsenceType())) {
				StaffEntity staffEntity = sXyAbsenceService.findUniqueByProperty(StaffEntity.class, "sttaffId", Integer.valueOf(tsUser.getUserName()));
				if(sXyAbsence.getApplyAbsenceDay() > staffEntity.getLeaveCount() * 7.5) {
					message = "享宇请假申请更新失败，你申请的年假时长已经超出了你可用的年假时长";
					throw new BusinessException(message);
				}
			}
			message = "享宇请假申请更新成功";
			//设置更新时间
			sXyAbsence.setUTime(new Date());
			//设置更新人编号
			sXyAbsence.setUUser(Integer.valueOf(tsUser.getUserName()));
			MyBeanUtils.copyBeanNotNull2Bean(sXyAbsence, t);
			sXyAbsenceService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇请假申请更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 提交享宇请假表
	 * @param sXyAbsence
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doSubmitAbsence")
	@ResponseBody
	public AjaxJson doSubmitAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		try {
			//审批人（用户编号(userName)）
			String feaaApprover = request.getParameter("feaaApprover");
			//获取请假业务表信息
			sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			if (Constants.XY_CHECK_TYPE_08.equals(sXyAbsence.getAbsenceType())) {
				StaffEntity staffEntity = sXyAbsenceService.findUniqueByProperty(StaffEntity.class, "sttaffId", Integer.valueOf(tsUser.getUserName()));
				if (sXyAbsence.getApplyAbsenceDay() > staffEntity.getLeaveCount() * 7.5) {
					message = "享宇请假申请提交失败，你申请的年假时长已经超出了你可用的年假时长";
					throw new BusinessException(message);
				}
			}
			message = "享宇请假申请提交成功";
			//查询当前用户的角色编码
			List<String> roleCodes = sXyAbsenceService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			//查询当前用户的部门
			TSDepart tsDepart = sXyAbsenceService.findHqlTSDepart(tsUser);
			//机构编码长度
			int orgCodeLength = tsDepart.getOrgCode().length();
			Map<String, Object> variables = new HashMap<String, Object>();
			//向流程中传入数据
			//传入请假业务表的ID（归档、显示在考勤异常表中等操作）
			variables.put("absenceId", sXyAbsence.getId());
			//判断流程走向
			if (Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 12) {
				variables.put("startLine", 1);
				variables.put(Constants.HEADMAN, feaaApprover);
			}
			if ((Constants.EMPLOYEE.equals(roleCode) || Constants.HR.equals(roleCode)) && orgCodeLength == 9) {
				//查询当前部门下的直接上级
				List<TSUser> tsUsers = sXyAbsenceService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
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
			 * 流程状态：0：流程未启动、1：待审批、2：请假审批中、3：请假申请被否决、
			 * 4：请假审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
			 */
			sXyAbsence.setFlowState("1");
			//设置请假申请日期
			sXyAbsence.setApplyDate(new Date());
			//设置申请编号
			sXyAbsence.setApplyNo(sXyAbsenceService.getNextApplyNo(ApplyTypeEnum.QJ,"s_xy_absence"));
			//启动流程（以用户编号(userName)作为启动人）
			String flowInstId = sXyAbsenceService.startFlow("xyAbsence", sXyAbsence.getId(), variables, tsUser.getUserName());
			//设置流程实例ID
			sXyAbsence.setFlowInstId(flowInstId);
			//保存
			sXyAbsenceService.saveOrUpdate(sXyAbsence);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇请假申请提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：批准享宇请假表
	 * @param sXyAbsence
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAgreeAbsence")
	@ResponseBody
	public AjaxJson doAgreeAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇请假表批准成功";
		//审批人（用户编号(userName)）
		String nextApprover = request.getParameter("nextApprover");
		//批准原因
		String passReason = request.getParameter("passReason");
		//获取请假业务表信息
		sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
		/**
		 * 流程状态：0：流程未启动、1：待审批、2：请假审批中、3：请假申请被否决、
		 * 4：请假审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyAbsence.setFlowState("2");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户的角色编码
		List<String> roleCodes = sXyAbsenceService.findHqlRoleCodes(tsUser);
		String roleCode = roleCodes.get(0);
		//休假类型
		String absenceType = sXyAbsence.getAbsenceType();
		//申请请假天数（小时数转换为天数）
		double applyAbsenceDay = sXyAbsence.getApplyAbsenceDay() / 7.5;
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		if(Constants.HEADMAN.equals(roleCode)) {
			variables.put("isPass", 1);
			variables.put(Constants.DM, nextApprover);
		}
		if(Constants.DM.equals(roleCode) || Constants.HRDM.equals(roleCode)) {
			variables.put("isPass", 2);
			variables.put("day", applyAbsenceDay);
			variables.put("type", absenceType);
			if(applyAbsenceDay <= 2 && (Constants.XY_CHECK_TYPE_01.equals(absenceType) || Constants.XY_CHECK_TYPE_02.equals(absenceType))) {
				variables.put(Constants.HR, nextApprover);
			} else {
				variables.put(Constants.VICE, nextApprover);
			}
		}
		if(Constants.VICE.equals(roleCode)) {
			variables.put("isPass", 3);
			variables.put("day", applyAbsenceDay);
			variables.put("type", absenceType);
			if((applyAbsenceDay <= 5 && (Constants.XY_CHECK_TYPE_01.equals(absenceType) || Constants.XY_CHECK_TYPE_02.equals(absenceType))) || 
					((applyAbsenceDay > 0) && (Constants.XY_CHECK_TYPE_07.equals(absenceType)) || Constants.XY_CHECK_TYPE_05.equals(absenceType)) || Constants.XY_CHECK_TYPE_08.equals(absenceType)) {
				variables.put(Constants.HR, nextApprover);
			} else {
				variables.put(Constants.CEO, nextApprover);
			}
		}
		if(Constants.CEO.equals(roleCode)) {
			variables.put("isPass", 4);
			variables.put(Constants.HR, nextApprover);
		}
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyAbsenceService.completeTask(sXyAbsence.getFlowInstId(), variables, tsUser.getUserName(), passReason);
			//保存状态
			sXyAbsenceService.saveOrUpdate(sXyAbsence);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇请假表批准失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：否决享宇请假表
	 * @param sXyAbsence
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doNotAgreeAbsence")
	@ResponseBody
	public AjaxJson doNotAgreeAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇请假表否决成功";
		//获取请假业务表信息
		sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
		/**
		 * 流程状态：0：流程未启动、1：待审批、2：请假审批中、3：请假申请被否决、
		 * 4：请假审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyAbsence.setFlowState("3");
		//否决原因
		String nopassReason = request.getParameter("nopassReason");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("isPass", 0);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyAbsenceService.completeTask(sXyAbsence.getFlowInstId(), variables, tsUser.getUserName(), nopassReason);
			//保存状态
			sXyAbsenceService.saveOrUpdate(sXyAbsence);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇请假表否决失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * HR批准享宇请假表
	 * @param sXyAbsence
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doHrAgreeAbsence")
	@ResponseBody
	public AjaxJson doHrAgreeAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇请假表批准成功";
		//获取请假业务表信息
		sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
		/**
		 * 流程状态：0：流程未启动、1：待审批、2：请假审批中、3：请假申请被否决、
		 * 4：请假审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		if("2".equals(sXyAbsence.getFlowState())) {
			sXyAbsence.setFlowState("4");
			variables.put("isPass", 5);
		}
		if("5".equals(sXyAbsence.getFlowState())) {
			sXyAbsence.setFlowState("7");
			variables.put("isPass", 6);
		}
		//批准原因
		String hrpassReason = request.getParameter("hrpassReason");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyAbsenceService.completeTask(sXyAbsence.getFlowInstId(), variables, tsUser.getUserName(), hrpassReason);
			//保存状态
			sXyAbsenceService.saveOrUpdate(sXyAbsence);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇请假表批准失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * HR否决享宇请假表
	 * @param sXyAbsence
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doHrNotAgreeAbsence")
	@ResponseBody
	public AjaxJson doHrNotAgreeAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇请假表否决成功";
		//获取请假业务表信息
		sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
		/**
		 * 流程状态：0：流程未启动、1：待审批、2：请假审批中、3：请假申请被否决、
		 * 4：请假审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		if("2".equals(sXyAbsence.getFlowState())) {
			sXyAbsence.setFlowState("3");
			variables.put("isPass", 0);
		}
		if("5".equals(sXyAbsence.getFlowState())) {
			sXyAbsence.setFlowState("6");
			variables.put("isPass", 7);
		}
		//否决原因
		String hrnopassReason = request.getParameter("hrnopassReason");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyAbsenceService.completeTask(sXyAbsence.getFlowInstId(), variables, tsUser.getUserName(), hrnopassReason);
			//保存状态
			sXyAbsenceService.saveOrUpdate(sXyAbsence);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇请假表否决失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 销假申请
	 * @param sXyAbsence
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "dobackAbsence")
	@ResponseBody
	public AjaxJson dobackAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		try {
			//获取请假业务表信息
			SXyAbsenceEntity t = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
			/**
			 * 流程状态：0：流程未启动、1：待审批、2：请假审批中、3：请假申请被否决、
			 * 4：请假审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
			 */
			sXyAbsence.setFlowState("5");
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			Map<String, Object> variables = new HashMap<String, Object>();
			//计算真实请假小时数
			BigDecimal bigDecimal = sXyCalendarsService.getTimeInterval(t.getStartTime(), sXyAbsence.getBackDate(), true);
			if (Constants.XY_CHECK_TYPE_08.equals(t.getAbsenceType())) {
				StaffEntity staffEntity = sXyAbsenceService.findUniqueByProperty(StaffEntity.class, "sttaffId", Integer.valueOf(tsUser.getUserName()));
				if (bigDecimal.doubleValue() > staffEntity.getLeaveCount() * 7.5) {
					message = "销假申请提交失败，你销假的时长已经超出了你可用的年假时长，请重新设置销假时间";
					throw new BusinessException(message);
				}
			}
			message = "销假申请提交成功";
			//设置真实请假小时数
			sXyAbsence.setAbsenceDay(bigDecimal.floatValue());
			MyBeanUtils.copyBeanNotNull2Bean(sXyAbsence, t);
			//完成任务（以用户编号(userName)完成任务）
			sXyAbsenceService.completeTask(t.getFlowInstId(), variables, tsUser.getUserName(), null);
			//保存状态
			sXyAbsenceService.saveOrUpdate(t);
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
	 * Ajax 异步获取两个时间的差值
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
			BigDecimal bigDecimal = sXyCalendarsService.getTimeInterval(startT, endT, true);
			j.setObj(bigDecimal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	/**
	 * 查看流程图片
	 * @param sXyAbsence
	 * @param response
	 */
	@RequestMapping(params = "lookFlowImage")
	public void lookFlowImage(SXyAbsenceEntity sXyAbsence, HttpServletResponse response) {
		try {
			response.setContentType("image/png");
			InputStream inputStream = sXyAbsenceService.graphics(sXyAbsence.getFlowInstId());
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
	 * 重新提交享宇请假申请表
	 * @param sXyAbsence
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "reSubmitAbsence")
	@ResponseBody
	public AjaxJson reSubmitAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇请假表重新提交成功";
		//获取请假业务表信息
		sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
		/**
		 * 流程状态：0：流程未启动、1：待审批、2：请假审批中、3：请假申请被否决、
		 * 4：请假审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyAbsence.setFlowState("1");
		//重新设置申请时间
		sXyAbsence.setApplyDate(new Date());
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("reApply", true);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyAbsenceService.completeTask(sXyAbsence.getFlowInstId(), variables, tsUser.getUserName(), null);
			//保存状态
			sXyAbsenceService.saveOrUpdate(sXyAbsence);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇请假表重新提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 撤销享宇请假申请表
	 * @param sXyAbsence
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delAbsence")
	@ResponseBody
	public AjaxJson delAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇请假申请撤销成功";
		//获取请假业务表信息
		sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
		/**
		 * 流程状态：0：流程未启动、1：待审批、2：请假审批中、3：请假申请被否决、
		 * 4：请假审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyAbsence.setFlowState("8");
		try {
			//撤销请假申请
			sXyAbsenceService.delFlow(sXyAbsence.getFlowInstId());
			//保存状态
			sXyAbsenceService.saveOrUpdate(sXyAbsence);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇请假撤销失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 取消享宇请假申请表
	 * @param sXyAbsence
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "cancelAbsence")
	@ResponseBody
	public AjaxJson cancelAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇请假申请取消成功";
		//获取请假业务表信息
		sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
		/**
		 * 流程状态：0：流程未启动、1：待审批、2：请假审批中、3：请假申请被否决、
		 * 4：请假审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyAbsence.setFlowState("9");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("reApply", false);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyAbsenceService.completeTask(sXyAbsence.getFlowInstId(), variables, tsUser.getUserName(), null);
			//保存状态
			sXyAbsenceService.saveOrUpdate(sXyAbsence);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇请假申请取消失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 享宇请假表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(SXyAbsenceEntity sXyAbsence, HttpServletRequest req) {
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户的部门
		TSDepart tsDepart = sXyAbsenceService.findHqlTSDepart(tsUser);
		req.setAttribute("tsUserPage", tsUser);
		req.setAttribute("tsDepartPage", tsDepart);
		return new ModelAndView("xyoa/activiti/absence/sXyAbsence-add");
	}
	/**
	 * 享宇请假表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(SXyAbsenceEntity sXyAbsence, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyAbsence.getId())) {
			sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
			req.setAttribute("sXyAbsencePage", sXyAbsence);
		}
		return new ModelAndView("xyoa/activiti/absence/sXyAbsence-update");
	}
	
	/**
	 * 享宇请假表详情页面跳转
	 * @param sXyAbsence
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goDetail")
	public ModelAndView goDetail(SXyAbsenceEntity sXyAbsence, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyAbsence.getId())) {
			try {
				//业务表信息
				sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
				//业务表历史任务节点批注信息
				List<CommentBean> commentBeans = sXyAbsenceService.findComment(sXyAbsence.getFlowInstId());
				req.setAttribute("sXyAbsencePage", sXyAbsence);
				req.setAttribute("commentBeansPage", commentBeans);
			} catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}
		}
		return new ModelAndView("xyoa/activiti/absence/sXyAbsence-detail");
	}
	
	/**
	 * 享宇请假表提交申请页面跳转
	 * @param sXyAbsence
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goSubmitAbsence")
	public ModelAndView goSubmitAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyAbsence.getId())) {
			//获取请假业务表信息
			sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyAbsenceService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			//查询当前用户的部门
			TSDepart tsDepart = sXyAbsenceService.findHqlTSDepart(tsUser);
			//机构编码长度
			int orgCodeLength = tsDepart.getOrgCode().length();
			//查询该部门下的领导信息
			List<TSUser> tsUsers = null;
			if (Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 12) {
				tsUsers = sXyAbsenceService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的直接上级
					TSDepart tsPDepart = sXyAbsenceService.findHqlTSPDepart(tsUser);
					tsUsers = sXyAbsenceService.findHqlTSUsersByRD(Constants.HEADMAN, tsPDepart);
				}
			}
			if ((Constants.EMPLOYEE.equals(roleCode) || Constants.HR.equals(roleCode)) && orgCodeLength == 9) {
				//查询当前部门下的直接上级
				tsUsers = sXyAbsenceService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前部门下的部门DM
					tsUsers = sXyAbsenceService.findHqlTSUsersByRD(Constants.DM, tsDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的部门DM
						TSDepart tsPDepart = sXyAbsenceService.findHqlTSPDepart(tsUser);
						tsUsers = sXyAbsenceService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					}
				}
			}
			if (Constants.HEADMAN.equals(roleCode)) {
				tsUsers = sXyAbsenceService.findHqlTSUsersByRD(Constants.DM, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的部门DM
					TSDepart tsPDepart = sXyAbsenceService.findHqlTSPDepart(tsUser);
					tsUsers = sXyAbsenceService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的上一级部门的部门DM
						TSDepart tsPPDepart = sXyAbsenceService.findHqlTSPPDepart(tsUser);
						tsUsers = sXyAbsenceService.findHqlTSUsersByRD(Constants.DM, tsPPDepart);
					}
				}
			}
			if(Constants.DM.equals(roleCode) || Constants.HRDM.equals(roleCode)) {
				tsUsers = sXyAbsenceService.findHqlTSUsersByRoleCode(Constants.VICE);
			}
			if(Constants.VICE.equals(roleCode)) {
				tsUsers = sXyAbsenceService.findHqlTSUsersByRoleCode(Constants.CEO);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyAbsencePage", sXyAbsence);
		}
		return new ModelAndView("xyoa/activiti/absence/sXyAbsence-submit");
	}
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 享宇请假表批准请假申请页面跳转
	 * @param sXyAbsence
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goAgreeAbsence")
	public ModelAndView goAgreeAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest req) {
		if(StringUtil.isNotEmpty(sXyAbsence.getId())) {
			//获取请假业务表信息
			sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyAbsenceService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			//休假类型
			String absenceType = sXyAbsence.getAbsenceType();
			//申请请假天数（小时数转换为天数）
			double applyAbsenceDay = sXyAbsence.getApplyAbsenceDay() / 7.5f;
			//查询当前用户的下一级审批人
			List<TSUser> tsUsers = null;
			if(Constants.HEADMAN.equals(roleCode)) {
				//查询当前用户所属的部门
				TSDepart tsDepart = sXyAbsenceService.findHqlTSDepart(tsUser);
				tsUsers = sXyAbsenceService.findHqlTSUsersByRD(Constants.DM, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的部门DM
					TSDepart tsPDepart = sXyAbsenceService.findHqlTSPDepart(tsUser);
					tsUsers = sXyAbsenceService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的上一级部门的部门DM
						TSDepart tsPPDepart = sXyAbsenceService.findHqlTSPPDepart(tsUser);
						tsUsers = sXyAbsenceService.findHqlTSUsersByRD(Constants.DM, tsPPDepart);
					}
				}
			}
			if(Constants.DM.equals(roleCode) || Constants.HRDM.equals(roleCode)) {
				if(applyAbsenceDay <= 2 && (Constants.XY_CHECK_TYPE_01.equals(absenceType) || Constants.XY_CHECK_TYPE_02.equals(absenceType))) {
					//查询HRDM
					tsUsers = sXyAbsenceService.findHqlTSUsersByRoleCode(Constants.HRDM);
				} else {
					tsUsers = sXyAbsenceService.findHqlTSUsersByRoleCode(Constants.VICE);
				}
			}
			if(Constants.VICE.equals(roleCode)) {
				if((applyAbsenceDay <= 5 && (Constants.XY_CHECK_TYPE_01.equals(absenceType) || Constants.XY_CHECK_TYPE_02.equals(absenceType))) || 
						((applyAbsenceDay > 0) && (Constants.XY_CHECK_TYPE_07.equals(absenceType)) || Constants.XY_CHECK_TYPE_05.equals(absenceType)) || Constants.XY_CHECK_TYPE_08.equals(absenceType)) {
					//查询HRDM
					tsUsers = sXyAbsenceService.findHqlTSUsersByRoleCode(Constants.HRDM);
				} else {
					tsUsers = sXyAbsenceService.findHqlTSUsersByRoleCode(Constants.CEO);
				}
			}
			if(Constants.CEO.equals(roleCode)) {
				//查询HRDM
				tsUsers = sXyAbsenceService.findHqlTSUsersByRoleCode(Constants.HRDM);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyAbsencePage", sXyAbsence);
		}
		return new ModelAndView("xyoa/activiti/absence/sXyAbsence-check");
	}
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 享宇请假表否决请假申请页面跳转
	 * @param sXyAbsence
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goNotAgreeAbsence")
	public ModelAndView goNotAgreeAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest req) {
		if(StringUtil.isNotEmpty(sXyAbsence.getId())) {
			sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
			req.setAttribute("sXyAbsencePage", sXyAbsence);
		}
		return new ModelAndView("xyoa/activiti/absence/sXyAbsence-nocheck");
	}
	
	/**
	 * HR
	 * 享宇请假表批准请假申请页面跳转
	 * @param sXyAbsence
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goHrAgreeAbsence")
	public ModelAndView goHrAgreeAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest req) {
		if(StringUtil.isNotEmpty(sXyAbsence.getId())) {
			sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
			req.setAttribute("sXyAbsencePage", sXyAbsence);
		}
		return new ModelAndView("xyoa/activiti/absence/sXyAbsence-hrcheck");
	}
	
	/**
	 * HR
	 * 享宇请假表否决请假申请页面跳转
	 * @param sXyAbsence
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goHrNotAgreeAbsence")
	public ModelAndView goHrNotAgreeAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest req) {
		if(StringUtil.isNotEmpty(sXyAbsence.getId())) {
			sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
			req.setAttribute("sXyAbsencePage", sXyAbsence);
		}
		return new ModelAndView("xyoa/activiti/absence/sXyAbsence-hrnocheck");
	}
	
	
	/**
	 * 销假页面跳转
	 * @param sXyAbsence
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "gobackAbsence")
	public ModelAndView gobackAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest req) {
		if(StringUtil.isNotEmpty(sXyAbsence.getId())) {
			sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
			req.setAttribute("sXyAbsencePage", sXyAbsence);
		}
		return new ModelAndView("xyoa/activiti/absence/sXyAbsence-back");
	}
	
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","sXyAbsenceController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	
	/**
	 * 导入解析并导出
	 * @return
	 */
	@RequestMapping(params = "goImportAndExportXls")
	public ModelAndView goImportAndExportXls() {
		return new ModelAndView("xyoa/activiti/absence/importAndExportXls");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(SXyAbsenceEntity sXyAbsence,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(SXyAbsenceEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyAbsence, request.getParameterMap());
		List<SXyAbsenceEntity> sXyAbsences = this.sXyAbsenceService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"享宇请假表");
		modelMap.put(NormalExcelConstants.CLASS,SXyAbsenceEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("享宇请假表列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,sXyAbsences);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	
	/**
	 * 导入信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "importExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		MultipartFile file = null;
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			file = entity.getValue();// 获取上传文件对象
			break;
		}
		InputStream is = null;
		try {
			String srcfileName = file.getOriginalFilename();
			is = file.getInputStream();
			String path = PoiPublicUtil.getWebRootPath("upload/excelUpload");
			File savefile = new File(path);
			if (!savefile.exists()) {
				savefile.mkdirs();
			}
			//递归删除该目录下的所有文件，不包括删除该目录
			poiService.deleteDir(savefile);
			File[] srcFile = new File[4];
			srcFile[0] = new File(path, "yy_xls_sc_data.xls");
			srcFile[1] = new File(path, "yy_xls_sz_data.xls");
			srcFile[2] = new File(path, "yy_xls_bj_data.xls");
			srcFile[3] = new File(path, "yy_xls_sh_data.xls");
			File zipfile = new File(path, "allExcel_" + DateUtils.getDate("yyyy-MM-dd") + ".zip");
			poiService.getImportExcel(is, srcFile, zipfile, srcfileName);
			j.setMsg("文件导入成功！");
		} catch (Exception e) {
			j.setMsg("文件导入失败！"+e.getMessage());
			logger.error(ExceptionUtil.getExceptionMessage(e));
		} finally {
			try {
				if (is != null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return j;
	}
	
	
	@RequestMapping(params = "datagridUploadExcel")
	public void datagridUploadExcel(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		List<FileMetaSub> fileMetaSubs = new ArrayList<FileMetaSub>();
		String path = PoiPublicUtil.getWebRootPath("upload/excelUpload");
		File savefile = new File(path);
		String[] fileNames = savefile.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith("zip"))
					return true;
				else
					return false;
			}
		});
		if (fileNames != null && fileNames.length > 0) {
			for (String fileName : fileNames) {
				FileMetaSub fileMetaSub = new FileMetaSub();
				fileMetaSub.setId(fileName);
				fileMetaSub.setFileName(fileName);
				fileMetaSubs.add(fileMetaSub);
			}
		}
		dataGrid.setResults(fileMetaSubs);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 下载zip文件
	 * @param response
	 */
	@RequestMapping(params = "downFile")
	public void downFile(HttpServletResponse response, String fileName) {
		try {
			OutputStream outputStream = null;
			response.setContentType("application/x-download");
			response.setHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(fileName, "UTF-8"));
			outputStream = response.getOutputStream();
			String path = PoiPublicUtil.getWebRootPath("upload/excelUpload");
			File savefile = new File(path);
			if (!savefile.exists()) {
				savefile.mkdirs();
			}
			File zipfile = new File(path, fileName);
			poiService.downFile(outputStream, zipfile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<SXyAbsenceEntity> list() {
		List<SXyAbsenceEntity> listSXyAbsences=sXyAbsenceService.getList(SXyAbsenceEntity.class);
		return listSXyAbsences;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		SXyAbsenceEntity task = sXyAbsenceService.get(SXyAbsenceEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody SXyAbsenceEntity sXyAbsence, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyAbsenceEntity>> failures = validator.validate(sXyAbsence);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyAbsenceService.save(sXyAbsence);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = sXyAbsence.getId();
		URI uri = uriBuilder.path("/rest/sXyAbsenceController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody SXyAbsenceEntity sXyAbsence) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyAbsenceEntity>> failures = validator.validate(sXyAbsence);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyAbsenceService.saveOrUpdate(sXyAbsence);
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
		sXyAbsenceService.deleteEntityById(SXyAbsenceEntity.class, id);
	}
}
