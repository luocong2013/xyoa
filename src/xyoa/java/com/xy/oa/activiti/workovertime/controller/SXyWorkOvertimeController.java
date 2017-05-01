package com.xy.oa.activiti.workovertime.controller;


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
import com.xy.oa.util.ApplyTypeEnum;
import com.xy.oa.util.Constants;

/**   
 * @Title: Controller  
 * @Description: 加班表
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
	
	/**
	 * 加班表列表 页面跳转
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
			cq.eq("tsUser", tsUser);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyWorkOvertimeService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 直接上级、部门DM、副总裁、总裁、人事
	 * 审核加班申请表单 页面跳转
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
	public void datagridCheck(SXyWorkOvertimeEntity sXyWorkOvertime,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String orgId) {
		CriteriaQuery cq = new CriteriaQuery(SXyWorkOvertimeEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyWorkOvertime, request.getParameterMap());
		try{
			List<WorkOvertimePo> wPos = new ArrayList<WorkOvertimePo>();
			//自定义追加查询条件
			if (StringUtil.isNotEmpty(orgId)) {
				TSDepart tsDepart = sXyWorkOvertimeService.getEntity(TSDepart.class, orgId);
				List<TSDepart> tSDepartList = tsDepart.getTSDeparts();
				tSDepartList.add(0, tsDepart);
				cq.in("tsDept", tSDepartList.toArray());
			}
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyWorkOvertimeService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			List<Task> tasks = sXyWorkOvertimeService.queryTask(tsUser.getUserName());
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
			this.sXyWorkOvertimeService.getDataGridReturn(cq, true);
			List<SXyWorkOvertimeEntity> sXyWorkOvertimeEntities = dataGrid.getResults();
			if (sXyWorkOvertimeEntities != null && !sXyWorkOvertimeEntities.isEmpty()) {
				for (SXyWorkOvertimeEntity sXyWorkOvertimeEntity : sXyWorkOvertimeEntities) {
					WorkOvertimePo wPo = new WorkOvertimePo();
					List<String> nextLineNames = sXyWorkOvertimeService.findNextLineNames(sXyWorkOvertimeEntity.getFlowInstId(), tsUser.getUserName());
					if (nextLineNames != null && !nextLineNames.isEmpty()) {
						for (String nextLineName : nextLineNames) {
							if (nextLineName.startsWith("同意")) {
								if (Constants.HR.equals(roleCode)) {
									wPo.setIsHrAgree("01");
								} else {
									wPo.setIsAgree("01");
								}
							}
							if (nextLineName.startsWith("驳回")) {
								wPo.setIsNotAgree("01");
							}
						}
					}
					BeanUtils.copyProperties(sXyWorkOvertimeEntity, wPo);
					wPos.add(wPo);
				}
				dataGrid.setResults(wPos);
			}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		TagUtil.datagrid(response, dataGrid);
	}
	/**
	 * 加班申请历史记录列表 页面跳转
	 * 直接上级、部门DM、副总裁、总裁
	 * @return
	 */
	@RequestMapping(params = "listHis")
	public ModelAndView listHis(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyWorkOvertimeService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/workovertime/sXyWorkOvertimeListHis");
	}
	/**
	 * Ajax数据请求
	 * @param sXyWorkOvertime
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridHis")
	public void datagridHis(SXyWorkOvertimeEntity sXyWorkOvertime,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String orgId) {
		CriteriaQuery cq = new CriteriaQuery(SXyWorkOvertimeEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyWorkOvertime, request.getParameterMap());
		try{
			//自定义追加查询条件
			if (StringUtil.isNotEmpty(orgId)) {
				TSDepart tsDepart = sXyWorkOvertimeService.getEntity(TSDepart.class, orgId);
				List<TSDepart> tSDepartList = tsDepart.getTSDeparts();
				tSDepartList.add(0, tsDepart);
				cq.in("tsDept", tSDepartList.toArray());
			}
			TSUser tsUser = ResourceUtil.getSessionUserName();
			List<String> roleCodes = sXyWorkOvertimeService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			List<TSUser> tsUsers = null;
			if (Constants.HEADMAN.equals(roleCode) || Constants.DM.equals(roleCode)) {
				//查询该部门或组下的所有员工
				tsUsers = sXyWorkOvertimeService.findHqlTSUsersByUser(tsUser);
			} else if (Constants.HR.equals(roleCode) || Constants.HRDM.equals(roleCode) 
					|| Constants.VICE.equals(roleCode) || Constants.CEO.equals(roleCode)) {
				//查询普通员工、直接上级、人事专员、部门DM、人事DM和副总裁
				tsUsers = sXyWorkOvertimeService.findHqlTSUsersByUR(tsUser);
			}
			List<HistoricTaskInstance> historicTaskInstances = sXyWorkOvertimeService.queryHisTask(tsUser.getUserName());
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
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyWorkOvertimeService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	

	/**
	 * 删除加班表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		sXyWorkOvertime = systemService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
		message = "加班申请删除成功";
		try{
			sXyWorkOvertimeService.delete(sXyWorkOvertime);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "加班申请删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	


	/**
	 * 添加加班表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "加班申请添加成功";
		try{
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户所属的部门
			TSDepart tsDepart = sXyWorkOvertimeService.findHqlTSDepart(tsUser);
			//设置申请人
			sXyWorkOvertime.setTsUser(tsUser);
			//设置申请人编号
			sXyWorkOvertime.setApplySttaffId(Integer.valueOf(tsUser.getUserName()));
			//设置部门
			sXyWorkOvertime.setTsDept(tsDepart);
			sXyWorkOvertime.setStartTime(sXyWorkOvertime.getApplyStartTime());
			sXyWorkOvertime.setEndTime(sXyWorkOvertime.getApplyEndTime());
			sXyWorkOvertime.setWorkHour(sXyWorkOvertime.getApplyWorkHour());
			sXyWorkOvertime.setOnWorkHour(sXyWorkOvertime.getApplyWorkHour());
			//设置创建时间
			sXyWorkOvertime.setCreateTime(new Date());
			//设置加班申请创建人
			sXyWorkOvertime.setCUser(Integer.valueOf(tsUser.getUserName()));
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyWorkOvertime.setFlowState("0");
			sXyWorkOvertimeService.save(sXyWorkOvertime);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "加班申请添加失败";
			logger.error(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新加班表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "加班申请更新成功";
		SXyWorkOvertimeEntity t = sXyWorkOvertimeService.get(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		try {
			sXyWorkOvertime.setStartTime(sXyWorkOvertime.getApplyStartTime());
			sXyWorkOvertime.setEndTime(sXyWorkOvertime.getApplyEndTime());
			sXyWorkOvertime.setWorkHour(sXyWorkOvertime.getApplyWorkHour());
			sXyWorkOvertime.setOnWorkHour(sXyWorkOvertime.getApplyWorkHour());
			//设置更新时间
			sXyWorkOvertime.setUTime(new Date());
			//设置更新人编号
			sXyWorkOvertime.setUUser(Integer.valueOf(tsUser.getUserName()));
			MyBeanUtils.copyBeanNotNull2Bean(sXyWorkOvertime, t);
			sXyWorkOvertimeService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "加班申请更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 启动流程
	 * @param sXyWorkOvertime
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "startFlow")
	@ResponseBody
	public AjaxJson startFlow(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "流程启动成功";
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//获取加班申请表信息
		sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
		try {
			//启动流程（以用户编号(userName)启动流程）
			String flowInstId = sXyWorkOvertimeService.startFlow("xyWorkOvertime", sXyWorkOvertime.getId(), tsUser.getUserName());
			//设置流程实例ID
			sXyWorkOvertime.setFlowInstId(flowInstId);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyWorkOvertime.setFlowState("1");
			//设置请假申请日期
			sXyWorkOvertime.setApplyDate(new Date());
			//设置申请编号
			sXyWorkOvertime.setApplyNo(sXyWorkOvertimeService.getNextApplyNo(ApplyTypeEnum.JB, "s_xy_work_overtime"));
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			message = "流程启动失败";
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 提交加班申请
	 * @param sXyWorkOvertime
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doSubmitOvertime")
	@ResponseBody
	public AjaxJson doSubmitOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "加班申请提交成功";
		try {
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
			String nextApproverRoleCode = sXyWorkOvertimeService.findNextApproverRoleCode(sXyWorkOvertime.getFlowInstId(), "startLine=="+startLineNum, tsUser.getUserName());
			variables.put("action", "提交申请");
			variables.put("startLine", startLineNum);
			variables.put(nextApproverRoleCode, feaaApprover);
			sXyWorkOvertimeService.completeTask(sXyWorkOvertime.getFlowInstId(), variables, tsUser.getUserName(), null);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyWorkOvertime.setFlowState("2");
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "加班申请提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 批准加班表
	 * @param sXyWorkOvertime
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAgreeOvertime")
	@ResponseBody
	public AjaxJson doAgreeOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "批准成功";
		//审批人（用户编号(userName)）
		String nextApprover = request.getParameter("nextApprover");
		//批准原因
		String passReason = request.getParameter("passReason");
		//获取加班表信息
		sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		String lineName = "同意";
		String nextApproverRoleCode = sXyWorkOvertimeService.findNextApproverRoleCode(sXyWorkOvertime.getFlowInstId(), lineName, tsUser.getUserName());
		variables.put("isPass", "同意");
		variables.put(nextApproverRoleCode, nextApprover);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyWorkOvertimeService.completeTask(sXyWorkOvertime.getFlowInstId(), variables, tsUser.getUserName(), passReason);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "批准失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：否决加班表
	 * @param sXyWorkOvertime
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doNotAgreeOvertime")
	@ResponseBody
	public AjaxJson doNotAgreeOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "加班申请驳回成功";
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//获取加班表信息
		sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
		//驳回原因
		String nopassReason = request.getParameter("nopassReason");
		try {
			Map<String, Object> variables = new HashMap<String, Object>();
			//向流程中传入数据
			variables.put("isPass", "驳回");
			//完成任务（以用户编号(userName)完成任务）
			sXyWorkOvertimeService.completeTask(sXyWorkOvertime.getFlowInstId(), variables, tsUser.getUserName(), nopassReason);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyWorkOvertime.setFlowState("3");
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "加班申请驳回失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * HR备案
	 * @param sXyWorkOvertime
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "hrAgreeOvertime")
	@ResponseBody
	public AjaxJson hrAgreeOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "加班申请备案成功";
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//获取加班表信息
		sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
		try {
			Map<String, Object> variables = new HashMap<String, Object>();
			//向流程中传入数据
			variables.put("isPass", "同意");
			//完成任务（以用户编号(userName)完成任务）
			sXyWorkOvertimeService.completeTask(sXyWorkOvertime.getFlowInstId(), variables, tsUser.getUserName(), null);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyWorkOvertime.setFlowState("4");
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "加班申请备案失败";
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
	 * 取消加班申请
	 * @param sXyWorkOvertime
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "cancelOvertime")
	@ResponseBody
	public AjaxJson cancelOvertime(SXyWorkOvertimeEntity sXyWorkOvertime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "加班申请取消申请成功";
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//获取加班表信息
		sXyWorkOvertime = sXyWorkOvertimeService.getEntity(SXyWorkOvertimeEntity.class, sXyWorkOvertime.getId());
		try {
			Map<String, Object> variables = new HashMap<String, Object>();
			//向流程中传入数据
			variables.put("action", "取消申请");
			//完成任务（以用户编号(userName)完成任务）
			sXyWorkOvertimeService.completeTask(sXyWorkOvertime.getFlowInstId(), variables, tsUser.getUserName(), null);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyWorkOvertime.setFlowState("5");
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "加班申请取消申请失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	

	/**
	 * 加班表新增页面跳转
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
	 * 加班表编辑页面跳转
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
	 * 加班表详情页面跳转
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
	 * 加班表提交申请页面跳转
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
			String nextApproverRoleCode = null;
			if (Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 12) {
				nextApproverRoleCode = sXyWorkOvertimeService.findNextApproverRoleCode(sXyWorkOvertime.getFlowInstId(), "startLine==1", tsUser.getUserName());
				tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(nextApproverRoleCode, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门
					TSDepart tsPDepart = sXyWorkOvertimeService.findHqlTSPDepart(tsUser);
					tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(nextApproverRoleCode, tsPDepart);
				}
			}
			if ((Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 9) || Constants.HEADMAN.equals(roleCode)) {
				nextApproverRoleCode = sXyWorkOvertimeService.findNextApproverRoleCode(sXyWorkOvertime.getFlowInstId(), "startLine==2", tsUser.getUserName());
				tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(nextApproverRoleCode, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门
					TSDepart tsPDepart = sXyWorkOvertimeService.findHqlTSPDepart(tsUser);
					tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(nextApproverRoleCode, tsPDepart);
				}
			}
			if (Constants.HR.equals(roleCode)) {
				nextApproverRoleCode = sXyWorkOvertimeService.findNextApproverRoleCode(sXyWorkOvertime.getFlowInstId(), "startLine==2", tsUser.getUserName());
				tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(Constants.HR+nextApproverRoleCode, tsDepart);
			}
			if(Constants.DM.equals(roleCode) || Constants.HRDM.equals(roleCode)) {
				nextApproverRoleCode = sXyWorkOvertimeService.findNextApproverRoleCode(sXyWorkOvertime.getFlowInstId(), "startLine==3", tsUser.getUserName());
				tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRoleCode(nextApproverRoleCode);
			}
			if(Constants.VICE.equals(roleCode)) {
				nextApproverRoleCode = sXyWorkOvertimeService.findNextApproverRoleCode(sXyWorkOvertime.getFlowInstId(), "startLine==4", tsUser.getUserName());
				tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRoleCode(nextApproverRoleCode);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyWorkOvertimePage", sXyWorkOvertime);
		}
		return new ModelAndView("xyoa/activiti/workovertime/sXyWorkOvertime-submit");
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 加班表批准加班申请页面跳转
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
			String lineName = "同意";
			String nextApproverRoleCode = sXyWorkOvertimeService.findNextApproverRoleCode(sXyWorkOvertime.getFlowInstId(), lineName, tsUser.getUserName());
			if(Constants.HEADMAN.equals(roleCode)) {
				//查询当前用户所属的部门
				TSDepart tsDepart = sXyWorkOvertimeService.findHqlTSDepart(tsUser);
				tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(nextApproverRoleCode, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的部门DM
					TSDepart tsPDepart = sXyWorkOvertimeService.findHqlTSPDepart(tsUser);
					tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRD(nextApproverRoleCode, tsPDepart);
				}
			} else {
				tsUsers = sXyWorkOvertimeService.findHqlTSUsersByRoleCode(nextApproverRoleCode);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyWorkOvertimePage", sXyWorkOvertime);
		}
		return new ModelAndView("xyoa/activiti/workovertime/sXyWorkOvertime-check");
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 加班表否决加班申请页面跳转
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
