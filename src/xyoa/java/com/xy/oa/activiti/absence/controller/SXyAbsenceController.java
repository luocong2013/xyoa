package com.xy.oa.activiti.absence.controller;


import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.xy.oa.activiti.absence.entity.AbsencePo;
import com.xy.oa.activiti.absence.entity.SXyAbsenceEntity;
import com.xy.oa.activiti.absence.service.SXyAbsenceServiceI;
import com.xy.oa.activiti.util.CommentBean;
import com.xy.oa.calendars.service.SXyCalendarsServiceI;
import com.xy.oa.util.ApplyTypeEnum;
import com.xy.oa.util.Constants;

/**   
 * @Title: Controller  
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
	

	/**
	 * 请假表列表 页面跳转
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
			//查询当前用户提交的请假申请
			cq.eq("tsUser", tsUser);
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyAbsenceService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 审核请假表单 页面跳转
	 * 直接上级、部门DM、副总裁、总裁、人事审批页面
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
	public void datagridCheck(SXyAbsenceEntity sXyAbsence, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String orgId) {
		CriteriaQuery cq = new CriteriaQuery(SXyAbsenceEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyAbsence, request.getParameterMap());
		try {
			List<AbsencePo> lPos = new ArrayList<AbsencePo>();
			//自定义追加查询条件
			if (StringUtil.isNotEmpty(orgId)) {
				TSDepart tsDepart = sXyAbsenceService.getEntity(TSDepart.class, orgId);
				List<TSDepart> tSDepartList = tsDepart.getTSDeparts();
				tSDepartList.add(0, tsDepart);
				cq.in("tsDept", tSDepartList.toArray());
			}
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyAbsenceService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			List<Task> tasks = sXyAbsenceService.queryTask(tsUser.getUserName());
			if (tasks != null && !tasks.isEmpty()) {
				Set<String> set = new HashSet<String>();
				for (int i = 0; i < tasks.size(); i++) {
					set.add(tasks.get(i).getProcessInstanceId());
				}
				cq.in("flowInstId", set.toArray());
			} else {
				cq.eq("flowInstId", "没有任务");
			}
			cq.add();
			this.sXyAbsenceService.getDataGridReturn(cq, true);
			List<SXyAbsenceEntity> sXyAbsenceEntities = dataGrid.getResults();
			if (sXyAbsenceEntities != null && !sXyAbsenceEntities.isEmpty()) {
				for (SXyAbsenceEntity sXyAbsenceEntity : sXyAbsenceEntities) {
					AbsencePo aPo = new AbsencePo();
					List<String> nextLineNames = sXyAbsenceService.findNextLineNames(sXyAbsenceEntity.getFlowInstId(), tsUser.getUserName());
					if (nextLineNames != null && !nextLineNames.isEmpty()) {
						for (String nextLineName : nextLineNames) {
							if (nextLineName.startsWith("同意")) {
								if (Constants.HR.equals(roleCode)) {
									aPo.setIsHrAgree("01");
								} else {
									aPo.setIsAgree("01");
								}
							}
							if (nextLineName.startsWith("驳回")) {
								aPo.setIsNotAgree("01");
							}
						}
					}
					BeanUtils.copyProperties(sXyAbsenceEntity, aPo);
					lPos.add(aPo);
				}
				dataGrid.setResults(lPos);
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 请假申请历史记录列表 页面跳转
	 * 直接上级、部门DM、副总裁、总裁
	 * @return
	 */
	@RequestMapping(params = "listHis")
	public ModelAndView listHis(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyAbsenceService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/absence/sXyAbsenceListHis");
	}
	/**
	 * Ajax数据请求
	 * @param sXyAbsence
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridHis")
	public void datagridHis(SXyAbsenceEntity sXyAbsence, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String orgId) {
		CriteriaQuery cq = new CriteriaQuery(SXyAbsenceEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyAbsence, request.getParameterMap());
		try {
			//自定义追加查询条件
			if (StringUtil.isNotEmpty(orgId)) {
				TSDepart tsDepart = sXyAbsenceService.getEntity(TSDepart.class, orgId);
				List<TSDepart> tSDepartList = tsDepart.getTSDeparts();
				tSDepartList.add(0, tsDepart);
				cq.in("tsDept", tSDepartList.toArray());
			}
			TSUser tsUser = ResourceUtil.getSessionUserName();
			List<String> roleCodes = sXyAbsenceService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			List<TSUser> tsUsers = null;
			if (Constants.HEADMAN.equals(roleCode) || Constants.DM.equals(roleCode)) {
				//查询该部门或组下的所有员工
				tsUsers = sXyAbsenceService.findHqlTSUsersByUser(tsUser);
			} else if (Constants.HR.equals(roleCode) || Constants.HRDM.equals(roleCode) 
					|| Constants.VICE.equals(roleCode) || Constants.CEO.equals(roleCode)) {
				//查询普通员工、直接上级、人事专员、部门DM、人事DM和副总裁
				tsUsers = sXyAbsenceService.findHqlTSUsersByUR(tsUser);
			}
			List<HistoricTaskInstance> historicTaskInstances = sXyAbsenceService.queryHisTask(tsUser.getUserName());
			if (historicTaskInstances != null && !historicTaskInstances.isEmpty()) {
				Set<String> set = new HashSet<String>();
				for (int i = 0; i < historicTaskInstances.size(); i++) {
					set.add(historicTaskInstances.get(i).getProcessInstanceId());
				}
				if (tsUsers != null && !tsUsers.isEmpty()) {
					cq.or(cq.and(Restrictions.in("tsUser", tsUsers.toArray()), Restrictions.eq("flowState", "4")), cq.and(Restrictions.in("flowInstId", set.toArray()), Restrictions.ne("flowState", "4")));
				} else {
					cq.add(cq.and(Restrictions.in("flowInstId", set.toArray()), Restrictions.ne("flowState", "4")));
				}
			} else {
				if (tsUsers != null && !tsUsers.isEmpty()) {
					cq.add(cq.and(Restrictions.in("tsUser", tsUsers.toArray()), Restrictions.eq("flowState", "4")));
				} else {
					cq.eq("flowInstId", "没有历史任务");
				}
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyAbsenceService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除请假表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		sXyAbsence = systemService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
		message = "请假申请删除成功";
		try{
			sXyAbsenceService.delete(sXyAbsence);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "请假申请删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 添加请假表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "请假申请添加成功";
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
			//设置创建时间
			sXyAbsence.setCreateTime(new Date());
			//设置创建人
			sXyAbsence.setCUser(Integer.valueOf(tsUser.getUserName()));
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyAbsence.setFlowState("0");
			sXyAbsenceService.save(sXyAbsence);
		}catch(Exception e){
			e.printStackTrace();
			message = "请假申请添加失败";
			logger.error(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		return j;
	}
	
	/**
	 * 更新请假表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "请假申请更新成功";
		try {
			SXyAbsenceEntity t = sXyAbsenceService.get(SXyAbsenceEntity.class, sXyAbsence.getId());
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//设置更新时间
			sXyAbsence.setUTime(new Date());
			//设置更新人编号
			sXyAbsence.setUUser(Integer.valueOf(tsUser.getUserName()));
			MyBeanUtils.copyBeanNotNull2Bean(sXyAbsence, t);
			sXyAbsenceService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "请假申请更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 启动流程
	 * @param sXyAbsence
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "startFlow")
	@ResponseBody
	public AjaxJson startFlow(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "流程启动成功";
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//获取请假业务表信息
		sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
		try {
			//启动流程（以用户编号(userName)作为启动人）
			String flowInstId = sXyAbsenceService.startFlow("xyAbsence", sXyAbsence.getId(), tsUser.getUserName());
			//设置流程实例ID
			sXyAbsence.setFlowInstId(flowInstId);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyAbsence.setFlowState("1");
			//设置请假申请日期
			sXyAbsence.setApplyDate(new Date());
			//设置申请编号
			sXyAbsence.setApplyNo(sXyAbsenceService.getNextApplyNo(ApplyTypeEnum.QJ,"s_xy_absence"));
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "流程启动失败";
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 提交请假表
	 * @param sXyAbsence
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doSubmitAbsence")
	@ResponseBody
	public AjaxJson doSubmitAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "请假申请提交成功";
		try {
			//审批人（用户编号(userName)）
			String feaaApprover = request.getParameter("feaaApprover");
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
			Map<String, Object> variables = new HashMap<String, Object>();
			//向流程中传入数据
			//传入请假业务表的ID（归档、显示在考勤异常表中等操作）
			variables.put("absenceId", sXyAbsence.getId());
			//判断流程走向
			int startLineNum = 0;
			if (Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 12) {
				startLineNum = 1;
			}
			if ((Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 9) || Constants.HEADMAN.equals(roleCode)
					|| Constants.HR.equals(roleCode)) {
				startLineNum = 2;
			}
			if(Constants.DM.equals(roleCode) || Constants.HRDM.equals(roleCode)) {
				startLineNum = 3;
			}
			if(Constants.VICE.equals(roleCode)) {
				startLineNum = 4;
			}
			String nextApproverRoleCode = sXyAbsenceService.findNextApproverRoleCode(sXyAbsence.getFlowInstId(), "startLine=="+startLineNum, tsUser.getUserName());
			variables.put("action", "提交申请");
			variables.put("startLine", startLineNum);
			variables.put("day", sXyAbsence.getApplyAbsenceDay() / 7.5);
			variables.put(nextApproverRoleCode, feaaApprover);
			sXyAbsenceService.completeTask(sXyAbsence.getFlowInstId(), variables, tsUser.getUserName(), null);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyAbsence.setFlowState("2");
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "请假申请提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：批准请假表
	 * @param sXyAbsence
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAgreeAbsence")
	@ResponseBody
	public AjaxJson doAgreeAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "请假申请批准成功";
		//审批人（用户编号(userName)）
		String nextApprover = request.getParameter("nextApprover");
		//批准原因
		String passReason = request.getParameter("passReason");
		//获取请假业务表信息
		sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户的角色编码
		List<String> roleCodes = sXyAbsenceService.findHqlRoleCodes(tsUser);
		String roleCode = roleCodes.get(0);
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		String lineName = "同意";
		if(Constants.DM.equals(roleCode) || Constants.HRDM.equals(roleCode) || Constants.VICE.equals(roleCode)) {
			//申请请假天数（小时数转换为天数）
			double applyAbsenceDay = sXyAbsence.getApplyAbsenceDay() / 7.5;
			String fixLineName = "同意,day>";
			lineName = sXyAbsenceService.findLineName(sXyAbsence.getFlowInstId(), fixLineName, tsUser.getUserName());
			int day = Integer.parseInt(lineName.substring(lineName.indexOf(">")+1));
			if (applyAbsenceDay <= day) {
				lineName = "同意,day<="+day;
			}
		}
		String nextApproverRoleCode = sXyAbsenceService.findNextApproverRoleCode(sXyAbsence.getFlowInstId(), lineName, tsUser.getUserName());
		variables.put("isPass", "同意");
		variables.put(nextApproverRoleCode, nextApprover);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyAbsenceService.completeTask(sXyAbsence.getFlowInstId(), variables, tsUser.getUserName(), passReason);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "请假申请批准失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：驳回请假表
	 * @param sXyAbsence
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doNotAgreeAbsence")
	@ResponseBody
	public AjaxJson doNotAgreeAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "请假申请驳回成功";
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//获取请假业务表信息
		sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
		//驳回原因
		String nopassReason = request.getParameter("nopassReason");
		try {
			Map<String, Object> variables = new HashMap<String, Object>();
			//向流程中传入数据
			variables.put("isPass", "驳回");
			//完成任务（以用户编号(userName)完成任务）
			sXyAbsenceService.completeTask(sXyAbsence.getFlowInstId(), variables, tsUser.getUserName(), nopassReason);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyAbsence.setFlowState("3");
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "请假申请驳回失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * HR备案
	 * @param sXyAbsence
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "hrAgreeAbsence")
	@ResponseBody
	public AjaxJson hrAgreeAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "备案成功";
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//获取请假业务表信息
		sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
		try {
			Map<String, Object> variables = new HashMap<String, Object>();
			//向流程中传入数据
			variables.put("isPass", "同意");
			//完成任务（以用户编号(userName)完成任务）
			sXyAbsenceService.completeTask(sXyAbsence.getFlowInstId(), variables, tsUser.getUserName(), null);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyAbsence.setFlowState("4");
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "备案失败";
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
	 * 取消请假申请表
	 * @param sXyAbsence
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "cancelAbsence")
	@ResponseBody
	public AjaxJson cancelAbsence(SXyAbsenceEntity sXyAbsence, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "请假申请取消成功";
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//获取请假业务表信息
		sXyAbsence = sXyAbsenceService.getEntity(SXyAbsenceEntity.class, sXyAbsence.getId());
		try {
			Map<String, Object> variables = new HashMap<String, Object>();
			//向流程中传入数据
			variables.put("action", "取消申请");
			//完成任务（以用户编号(userName)完成任务）
			sXyAbsenceService.completeTask(sXyAbsence.getFlowInstId(), variables, tsUser.getUserName(), null);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyAbsence.setFlowState("5");
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "请假申请取消失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 请假表新增页面跳转
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
	 * 请假表编辑页面跳转
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
	 * 请假表详情页面跳转
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
	 * 请假表提交申请页面跳转
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
			String nextApproverRoleCode = null;
			if (Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 12) {
				nextApproverRoleCode = sXyAbsenceService.findNextApproverRoleCode(sXyAbsence.getFlowInstId(), "startLine==1", tsUser.getUserName());
				tsUsers = sXyAbsenceService.findHqlTSUsersByRD(nextApproverRoleCode, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门
					TSDepart tsPDepart = sXyAbsenceService.findHqlTSPDepart(tsUser);
					tsUsers = sXyAbsenceService.findHqlTSUsersByRD(nextApproverRoleCode, tsPDepart);
				}
			}
			if ((Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 9) || Constants.HEADMAN.equals(roleCode)) {
				nextApproverRoleCode = sXyAbsenceService.findNextApproverRoleCode(sXyAbsence.getFlowInstId(), "startLine==2", tsUser.getUserName());
				tsUsers = sXyAbsenceService.findHqlTSUsersByRD(nextApproverRoleCode, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门
					TSDepart tsPDepart = sXyAbsenceService.findHqlTSPDepart(tsUser);
					tsUsers = sXyAbsenceService.findHqlTSUsersByRD(nextApproverRoleCode, tsPDepart);
				}
			}
			if (Constants.HR.equals(roleCode)) {
				nextApproverRoleCode = sXyAbsenceService.findNextApproverRoleCode(sXyAbsence.getFlowInstId(), "startLine==2", tsUser.getUserName());
				tsUsers = sXyAbsenceService.findHqlTSUsersByRD(Constants.HR+nextApproverRoleCode, tsDepart);
			}
			if(Constants.DM.equals(roleCode) || Constants.HRDM.equals(roleCode)) {
				nextApproverRoleCode = sXyAbsenceService.findNextApproverRoleCode(sXyAbsence.getFlowInstId(), "startLine==3", tsUser.getUserName());
				tsUsers = sXyAbsenceService.findHqlTSUsersByRoleCode(nextApproverRoleCode);
			}
			if(Constants.VICE.equals(roleCode)) {
				nextApproverRoleCode = sXyAbsenceService.findNextApproverRoleCode(sXyAbsence.getFlowInstId(), "startLine==4", tsUser.getUserName());
				tsUsers = sXyAbsenceService.findHqlTSUsersByRoleCode(nextApproverRoleCode);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyAbsencePage", sXyAbsence);
		}
		return new ModelAndView("xyoa/activiti/absence/sXyAbsence-submit");
	}
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 请假表批准请假申请页面跳转
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
			//查询当前用户的下一级审批人
			List<TSUser> tsUsers = null;
			String nextApproverRoleCode = null;
			String lineName = "同意";
			if(Constants.HEADMAN.equals(roleCode)) {
				//查询当前用户所属的部门
				TSDepart tsDepart = sXyAbsenceService.findHqlTSDepart(tsUser);
				nextApproverRoleCode = sXyAbsenceService.findNextApproverRoleCode(sXyAbsence.getFlowInstId(), lineName, tsUser.getUserName());
				tsUsers = sXyAbsenceService.findHqlTSUsersByRD(nextApproverRoleCode, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门
					TSDepart tsPDepart = sXyAbsenceService.findHqlTSPDepart(tsUser);
					tsUsers = sXyAbsenceService.findHqlTSUsersByRD(nextApproverRoleCode, tsPDepart);
				}
			} else {
				if (Constants.DM.equals(roleCode) || Constants.HRDM.equals(roleCode) || Constants.VICE.equals(roleCode)) {
					//申请请假天数（小时数转换为天数）
					double applyAbsenceDay = sXyAbsence.getApplyAbsenceDay() / 7.5f;
					String fixLineName = "同意,day>";
					lineName = sXyAbsenceService.findLineName(sXyAbsence.getFlowInstId(), fixLineName, tsUser.getUserName());
					int day = Integer.parseInt(lineName.substring(lineName.indexOf(">")+1));
					if (applyAbsenceDay <= day) {
						lineName = "同意,day<="+day;
					}
				}
				nextApproverRoleCode = sXyAbsenceService.findNextApproverRoleCode(sXyAbsence.getFlowInstId(), lineName, tsUser.getUserName());
				tsUsers = sXyAbsenceService.findHqlTSUsersByRoleCode(nextApproverRoleCode);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyAbsencePage", sXyAbsence);
		}
		return new ModelAndView("xyoa/activiti/absence/sXyAbsence-check");
	}
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 请假表否决请假申请页面跳转
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
		modelMap.put(NormalExcelConstants.FILE_NAME,"请假表");
		modelMap.put(NormalExcelConstants.CLASS,SXyAbsenceEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("请假表列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,sXyAbsences);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
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
