package com.xy.oa.activiti.compensateleave.controller;


import java.io.InputStream;
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

import com.xy.oa.activiti.compensateleave.entity.CompensateLeavePo;
import com.xy.oa.activiti.compensateleave.entity.SXyCompensateLeaveEntity;
import com.xy.oa.activiti.compensateleave.service.SXyCompensateLeaveServiceI;
import com.xy.oa.activiti.util.CommentBean;
import com.xy.oa.util.ApplyTypeEnum;
import com.xy.oa.util.Constants;

/**   
 * @Title: Controller  
 * @Description: 调休表
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

	/**
	 * 调休表列表 页面跳转
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
			cq.eq("tsUser", tsUser);
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyCompensateLeaveService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 直接上级、部门DM、副总裁、总裁审批页面
	 * 审核调休表单 页面跳转
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
	public void datagridCheck(SXyCompensateLeaveEntity sXyCompensateLeave,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String orgId) {
		CriteriaQuery cq = new CriteriaQuery(SXyCompensateLeaveEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyCompensateLeave, request.getParameterMap());
		try{
			List<CompensateLeavePo> cPos = new ArrayList<CompensateLeavePo>();
			//自定义追加查询条件
			if (StringUtil.isNotEmpty(orgId)) {
				TSDepart tsDepart = sXyCompensateLeaveService.getEntity(TSDepart.class, orgId);
				List<TSDepart> tSDepartList = tsDepart.getTSDeparts();
				tSDepartList.add(0, tsDepart);
				cq.in("tsDept", tSDepartList.toArray());
			}
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyCompensateLeaveService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			List<Task> tasks = sXyCompensateLeaveService.queryTask(tsUser.getUserName());
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
			this.sXyCompensateLeaveService.getDataGridReturn(cq, true);
			List<SXyCompensateLeaveEntity> sXyCompensateLeaveEntities = dataGrid.getResults();
			if (sXyCompensateLeaveEntities != null && !sXyCompensateLeaveEntities.isEmpty()) {
				for (SXyCompensateLeaveEntity sXyCompensateLeaveEntity : sXyCompensateLeaveEntities) {
					CompensateLeavePo cPo = new CompensateLeavePo();
					List<String> nextLineNames = sXyCompensateLeaveService.findNextLineNames(sXyCompensateLeaveEntity.getFlowInstId(), tsUser.getUserName());
					if (nextLineNames != null && !nextLineNames.isEmpty()) {
						for (String nextLineName : nextLineNames) {
							if (nextLineName.startsWith("同意")) {
								if (Constants.HR.equals(roleCode)) {
									cPo.setIsHrAgree("01");
								} else {
									cPo.setIsAgree("01");
								}
							}
							if (nextLineName.startsWith("驳回")) {
								cPo.setIsNotAgree("01");
							}
						}
					}
					BeanUtils.copyProperties(sXyCompensateLeaveEntity, cPo);
					cPos.add(cPo);
				}
				dataGrid.setResults(cPos);
			}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 调休申请历史记录列表 页面跳转
	 * 直接上级、部门DM、副总裁、总裁
	 * @return
	 */
	@RequestMapping(params = "listHis")
	public ModelAndView listHis(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyCompensateLeaveService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/compensateleave/sXyCompensateLeaveListHis");
	}
	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "datagridHis")
	public void datagridHis(SXyCompensateLeaveEntity sXyCompensateLeave,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String orgId) {
		CriteriaQuery cq = new CriteriaQuery(SXyCompensateLeaveEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyCompensateLeave, request.getParameterMap());
		try{
			//自定义追加查询条件
			if (StringUtil.isNotEmpty(orgId)) {
				TSDepart tsDepart = sXyCompensateLeaveService.getEntity(TSDepart.class, orgId);
				List<TSDepart> tSDepartList = tsDepart.getTSDeparts();
				tSDepartList.add(0, tsDepart);
				cq.in("tsDept", tSDepartList.toArray());
			}
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			List<String> roleCodes = sXyCompensateLeaveService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			List<TSUser> tsUsers = null;
			if (Constants.HEADMAN.equals(roleCode) || Constants.DM.equals(roleCode)) {
				//查询该部门或组下的所有员工
				tsUsers = sXyCompensateLeaveService.findHqlTSUsersByUser(tsUser);
			} else if (Constants.HR.equals(roleCode) || Constants.HRDM.equals(roleCode) 
					|| Constants.VICE.equals(roleCode) || Constants.CEO.equals(roleCode)) {
				//查询普通员工、直接上级、人事专员、部门DM、人事DM和副总裁
				tsUsers = sXyCompensateLeaveService.findHqlTSUsersByUR(tsUser);
			}
			List<HistoricTaskInstance> historicTaskInstances = sXyCompensateLeaveService.queryHisTask(tsUser.getUserName());
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
		this.sXyCompensateLeaveService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除调休表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		sXyCompensateLeave = systemService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
		message = "调休申请删除成功";
		try{
			sXyCompensateLeaveService.delete(sXyCompensateLeave);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "调休申请删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加调休表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "调休申请添加成功";
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
			//设置调休开始时间
			sXyCompensateLeave.setStartTime(sXyCompensateLeave.getLeaveStartTime());
			//设置创建 时间
			sXyCompensateLeave.setCreateTime(new Date());
			//设置创建人编号
			sXyCompensateLeave.setCUser(Integer.valueOf(tsUser.getUserName()));
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyCompensateLeave.setFlowState("0");
			sXyCompensateLeaveService.save(sXyCompensateLeave);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "调休申请添加失败";
			logger.error(e);
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新调休表
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "调休申请更新成功";
		SXyCompensateLeaveEntity t = sXyCompensateLeaveService.get(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		try {
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
			message = "调休申请更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 启动流程
	 * @param sXyCompensateLeave
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "startFlow")
	@ResponseBody
	public AjaxJson startFlow(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "流程启动成功";
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//获取调休表信息
		sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
		try {
			//启动流程（以用户编号(userName)作为启动人）
			String flowInstId = sXyCompensateLeaveService.startFlow("xyWorkOvertime", sXyCompensateLeave.getId(), tsUser.getUserName());
			//设置流程实例ID
			sXyCompensateLeave.setFlowInstId(flowInstId);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyCompensateLeave.setFlowState("1");
			//设置提交申请日期
			sXyCompensateLeave.setApplyDate(new Date());
			//设置申请编号
			sXyCompensateLeave.setApplyNo(sXyCompensateLeaveService.getNextApplyNo(ApplyTypeEnum.TX, "s_xy_compensate_leave"));
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
	 * 提交调休表
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doSubmitLeave")
	@ResponseBody
	public AjaxJson doSubmitLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "调休申请提交成功";
		try {
			//审批人（用户编号(userName)）
			String feaaApprover = request.getParameter("feaaApprover");
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
			Map<String, Object> variables = new HashMap<String, Object>();
			//向流程中传入数据
			//传入调休表的ID（归档、显示在考勤异常表中等操作）
			variables.put("leaveId", sXyCompensateLeave.getId());
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
			String nextApproverRoleCode = sXyCompensateLeaveService.findNextApproverRoleCode(sXyCompensateLeave.getFlowInstId(), "startLine=="+startLineNum, tsUser.getUserName());
			variables.put("action", "提交申请");
			variables.put("startLine", startLineNum);
			variables.put(nextApproverRoleCode, feaaApprover);
			sXyCompensateLeaveService.completeTask(sXyCompensateLeave.getFlowInstId(), variables, tsUser.getUserName(), null);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyCompensateLeave.setFlowState("2");
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "调休申请提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：批准调休申请
	 * @param sXyCompensateLeave
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAgreeLeave")
	@ResponseBody
	public AjaxJson doAgreeLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "批准成功";
		//审批人（用户编号(userName)）
		String nextApprover = request.getParameter("nextApprover");
		//批准原因
		String passReason = request.getParameter("passReason");
		//获取调休表信息
		sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		String lineName = "同意";
		String nextApproverRoleCode = sXyCompensateLeaveService.findNextApproverRoleCode(sXyCompensateLeave.getFlowInstId(), lineName, tsUser.getUserName());
		variables.put("isPass", "同意");
		variables.put(nextApproverRoleCode, nextApprover);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyCompensateLeaveService.completeTask(sXyCompensateLeave.getFlowInstId(), variables, tsUser.getUserName(), passReason);
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
	 * 直接上级、部门DM、副总裁、总裁：否决调休申请
	 * @param sXyCompensateLeave
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doNotAgreeLeave")
	@ResponseBody
	public AjaxJson doNotAgreeLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "驳回成功";
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//驳回原因
		String nopassReason = request.getParameter("nopassReason");
		//获取调休表信息
		sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
		try {
			Map<String, Object> variables = new HashMap<String, Object>();
			//向流程中传入数据
			variables.put("isPass", "驳回");
			//完成任务（以用户编号(userName)完成任务）
			sXyCompensateLeaveService.completeTask(sXyCompensateLeave.getFlowInstId(), variables, tsUser.getUserName(), nopassReason);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyCompensateLeave.setFlowState("3");
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "驳回失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * HR备案
	 * @param sXyCompensateLeave
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "hrAgreeLeave")
	@ResponseBody
	public AjaxJson hrAgreeLeave(SXyCompensateLeaveEntity sXyCompensateLeave, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "调休申请备案成功";
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//获取调休表信息
		sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
		try {
			Map<String, Object> variables = new HashMap<String, Object>();
			//向流程中传入数据
			variables.put("isPass", "同意");
			//完成任务（以用户编号(userName)完成任务）
			sXyCompensateLeaveService.completeTask(sXyCompensateLeave.getFlowInstId(), variables, tsUser.getUserName(), null);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyCompensateLeave.setFlowState("4");
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "调休申请备案失败";
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
		message = "调休申请取消提交成功";
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//获取出差表信息
		sXyCompensateLeave = sXyCompensateLeaveService.getEntity(SXyCompensateLeaveEntity.class, sXyCompensateLeave.getId());
		try {
			Map<String, Object> variables = new HashMap<String, Object>();
			//向流程中传入数据
			variables.put("action", "取消申请");
			//完成任务（以用户编号(userName)完成任务）
			sXyCompensateLeaveService.completeTask(sXyCompensateLeave.getFlowInstId(), variables, tsUser.getUserName(), null);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyCompensateLeave.setFlowState("5");
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "调休申请取消提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 调休表新增页面跳转
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
	 * 调休表编辑页面跳转
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
	 * 调休表详情页面跳转
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
	 * 调休表提交申请 页面跳转
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
			String nextApproverRoleCode = null;
			if (Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 12) {
				nextApproverRoleCode = sXyCompensateLeaveService.findNextApproverRoleCode(sXyCompensateLeave.getFlowInstId(), "startLine==1", tsUser.getUserName());
				tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(nextApproverRoleCode, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门
					TSDepart tsPDepart = sXyCompensateLeaveService.findHqlTSPDepart(tsUser);
					tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(nextApproverRoleCode, tsPDepart);
				}
			}
			if ((Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 9) || Constants.HEADMAN.equals(roleCode)) {
				nextApproverRoleCode = sXyCompensateLeaveService.findNextApproverRoleCode(sXyCompensateLeave.getFlowInstId(), "startLine==2", tsUser.getUserName());
				tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(nextApproverRoleCode, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门
					TSDepart tsPDepart = sXyCompensateLeaveService.findHqlTSPDepart(tsUser);
					tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(nextApproverRoleCode, tsPDepart);
				}
			}
			if (Constants.HR.equals(roleCode)) {
				nextApproverRoleCode = sXyCompensateLeaveService.findNextApproverRoleCode(sXyCompensateLeave.getFlowInstId(), "startLine==2", tsUser.getUserName());
				tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(Constants.HR+nextApproverRoleCode, tsDepart);
			}
			if(Constants.DM.equals(roleCode) || Constants.HRDM.equals(roleCode)) {
				nextApproverRoleCode = sXyCompensateLeaveService.findNextApproverRoleCode(sXyCompensateLeave.getFlowInstId(), "startLine==3", tsUser.getUserName());
				tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRoleCode(nextApproverRoleCode);
			}
			if(Constants.VICE.equals(roleCode)) {
				nextApproverRoleCode = sXyCompensateLeaveService.findNextApproverRoleCode(sXyCompensateLeave.getFlowInstId(), "startLine==4", tsUser.getUserName());
				tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRoleCode(nextApproverRoleCode);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyCompensateLeavePage", sXyCompensateLeave);
		}
		return new ModelAndView("xyoa/activiti/compensateleave/sXyCompensateLeave-submit");
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 调休表批准调休申请 页面跳转
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
			String lineName = "同意";
			String nextApproverRoleCode = sXyCompensateLeaveService.findNextApproverRoleCode(sXyCompensateLeave.getFlowInstId(), lineName, tsUser.getUserName());
			if(Constants.HEADMAN.equals(roleCode)) {
				//查询当前用户所属的部门
				TSDepart tsDepart = sXyCompensateLeaveService.findHqlTSDepart(tsUser);
				tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(nextApproverRoleCode, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的部门DM
					TSDepart tsPDepart = sXyCompensateLeaveService.findHqlTSPDepart(tsUser);
					tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRD(nextApproverRoleCode, tsPDepart);
				}
			} else {
				tsUsers = sXyCompensateLeaveService.findHqlTSUsersByRoleCode(nextApproverRoleCode);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyCompensateLeavePage", sXyCompensateLeave);
		}
		return new ModelAndView("xyoa/activiti/compensateleave/sXyCompensateLeave-check");
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 调休表否决调休申请 页面跳转
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
