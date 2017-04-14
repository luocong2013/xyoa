package com.xy.oa.activiti.businesstrip.controller;

import java.io.IOException;
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
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.xy.oa.activiti.businesstrip.entity.BusinessTripPo;
import com.xy.oa.activiti.businesstrip.entity.SXyBusinessTripEntity;
import com.xy.oa.activiti.businesstrip.service.SXyBusinessTripServiceI;
import com.xy.oa.activiti.util.CommentBean;
import com.xy.oa.util.ApplyTypeEnum;
import com.xy.oa.util.Constants;
/**   
 * @Title: Controller  
 * @Description: 出差表
 * @author onlineGenerator
 * @date 2016-08-22 17:09:01
 * @version V1.0   
 *
 */
@Controller("sXyBusinessTripController")
@RequestMapping("/sXyBusinessTripController")
public class SXyBusinessTripController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SXyBusinessTripController.class);

	@Autowired
	private SXyBusinessTripServiceI sXyBusinessTripService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;

	/**
	 * 出差表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyBusinessTripService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/businesstrip/sXyBusinessTripList");
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
	public void datagrid(SXyBusinessTripEntity sXyBusinessTrip,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SXyBusinessTripEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyBusinessTrip, request.getParameterMap());
		try{
			//自定义追加查询条件
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			cq.eq("tsUser", tsUser);
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyBusinessTripService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 直接上级、部门DM、副总裁、总裁审批页面
	 * 审核出差表单 页面跳转
	 * @return
	 */
	@RequestMapping(params = "listCheck")
	public ModelAndView listCheck(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyBusinessTripService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/businesstrip/sXyBusinessTripListCheck");
	}
	/**
	 * Ajax数据请求
	 * @param sXyBusinessTrip
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridCheck")
	public void datagridCheck(SXyBusinessTripEntity sXyBusinessTrip,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String orgId) {
		CriteriaQuery cq = new CriteriaQuery(SXyBusinessTripEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyBusinessTrip, request.getParameterMap());
		try{
			List<BusinessTripPo> bPos = new ArrayList<BusinessTripPo>();
			//自定义追加查询条件
			if (StringUtil.isNotEmpty(orgId)) {
				TSDepart tsDepart = sXyBusinessTripService.getEntity(TSDepart.class, orgId);
				List<TSDepart> tSDepartList = tsDepart.getTSDeparts();
				tSDepartList.add(0, tsDepart);
				cq.in("tsDept", tSDepartList.toArray());
			}
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyBusinessTripService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			List<Task> tasks = sXyBusinessTripService.queryTask(tsUser.getUserName());
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
			this.sXyBusinessTripService.getDataGridReturn(cq, true);
			List<SXyBusinessTripEntity> sXyBusinessTripEntities = dataGrid.getResults();
			if (sXyBusinessTripEntities != null && !sXyBusinessTripEntities.isEmpty()) {
				for (SXyBusinessTripEntity sXyBusinessTripEntity : sXyBusinessTripEntities) {
					BusinessTripPo bPo = new BusinessTripPo();
					List<String> nextLineNames = sXyBusinessTripService.findNextLineNames(sXyBusinessTripEntity.getFlowInstId(), tsUser.getUserName());
					if (nextLineNames != null && !nextLineNames.isEmpty()) {
						for (String nextLineName : nextLineNames) {
							if (nextLineName.startsWith("同意")) {
								if (Constants.HR.equals(roleCode)) {
									bPo.setIsHrAgree("01");
								} else {
									bPo.setIsAgree("01");
								}
							}
							if (nextLineName.startsWith("驳回")) {
								bPo.setIsNotAgree("01");
							}
						}
					}
					BeanUtils.copyProperties(sXyBusinessTripEntity, bPo);
					bPos.add(bPo);
				}
				dataGrid.setResults(bPos);
			}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		TagUtil.datagrid(response, dataGrid);
	}
	/**
	 * 出差申请历史记录列表 页面跳转
	 * @return
	 */
	@RequestMapping(params = "listHis")
	public ModelAndView listHis(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyBusinessTripService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/businesstrip/sXyBusinessTripListHis");
	}
	/**
	 * Ajax数据请求
	 * @param sXyBusinessTrip
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridHis")
	public void datagridHis(SXyBusinessTripEntity sXyBusinessTrip,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String orgId) {
		CriteriaQuery cq = new CriteriaQuery(SXyBusinessTripEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyBusinessTrip, request.getParameterMap());
		try{
			//自定义追加查询条件
			if (StringUtil.isNotEmpty(orgId)) {
				TSDepart tsDepart = sXyBusinessTripService.getEntity(TSDepart.class, orgId);
				List<TSDepart> tSDepartList = tsDepart.getTSDeparts();
				tSDepartList.add(0, tsDepart);
				cq.in("tsDept", tSDepartList.toArray());
			}
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			List<String> roleCodes = sXyBusinessTripService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			List<TSUser> tsUsers = null;
			if (Constants.HEADMAN.equals(roleCode) || Constants.DM.equals(roleCode)) {
				//查询该部门或组下的所有员工
				tsUsers = sXyBusinessTripService.findHqlTSUsersByUser(tsUser);
			} else if (Constants.HR.equals(roleCode) || Constants.HRDM.equals(roleCode) 
					|| Constants.VICE.equals(roleCode) || Constants.CEO.equals(roleCode)) {
				//查询普通员工、直接上级、人事专员、部门DM、人事DM和副总裁
				tsUsers = sXyBusinessTripService.findHqlTSUsersByUR(tsUser);
			}
			List<HistoricTaskInstance> historicTaskInstances = sXyBusinessTripService.queryHisTask(tsUser.getUserName());
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
		this.sXyBusinessTripService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	/**
	 * 删除出差表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		sXyBusinessTrip = systemService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
		message = "出差申请删除成功";
		try{
			sXyBusinessTripService.delete(sXyBusinessTrip);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "出差申请删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加出差表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "出差申请添加成功";
		try{
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户所属的部门
			TSDepart tsDepart = sXyBusinessTripService.findHqlTSDepart(tsUser);
			//设置申请人
			sXyBusinessTrip.setTsUser(tsUser);
			//设置申请人编号
			sXyBusinessTrip.setApplySttaffId(Integer.valueOf(tsUser.getUserName()));
			//设置申请人部门
			sXyBusinessTrip.setTsDept(tsDepart);
			//设置出差开始时间
			sXyBusinessTrip.setStartTime(sXyBusinessTrip.getTripStartTime());
			//设置创建时间
			sXyBusinessTrip.setCreateTime(new Date());
			//设置创建人编号
			sXyBusinessTrip.setCUser(Integer.valueOf(tsUser.getUserName()));
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyBusinessTrip.setFlowState("0");
			sXyBusinessTripService.save(sXyBusinessTrip);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "出差申请添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新出差表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "出差申请更新成功";
		SXyBusinessTripEntity t = sXyBusinessTripService.get(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		try {
			//设置出差开始时间
			sXyBusinessTrip.setStartTime(sXyBusinessTrip.getTripStartTime());
			//设置更新时间
			sXyBusinessTrip.setUTime(new Date());
			//设置更新人编号
			sXyBusinessTrip.setUUser(Integer.valueOf(tsUser.getUserName()));
			MyBeanUtils.copyBeanNotNull2Bean(sXyBusinessTrip, t);
			sXyBusinessTripService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "出差申请更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 启动流程
	 * @param sXyBusinessTrip
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "startFlow")
	@ResponseBody
	public AjaxJson startFlow(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "流程启动成功";
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//获取出差表信息
		sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
		try {
			//启动流程（以用户编号(userName)作为启动人）
			String flowInstId = sXyBusinessTripService.startFlow("xyWorkOvertime", sXyBusinessTrip.getId(), tsUser.getUserName());
			//设置流程实例ID
			sXyBusinessTrip.setFlowInstId(flowInstId);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyBusinessTrip.setFlowState("1");
			//设置提交申请日期
			sXyBusinessTrip.setApplyDate(new Date());
			//设置申请编号
			sXyBusinessTrip.setApplyNo(sXyBusinessTripService.getNextApplyNo(ApplyTypeEnum.CC, "s_xy_business_trip"));
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
	 * 提交出差表
	 * @return
	 */
	@RequestMapping(params = "doSubmitTrip")
	@ResponseBody
	public AjaxJson doSubmitTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "出差申请提交成功";
		try {
			//审批人（用户编号(userName)）
			String feaaApprover = request.getParameter("feaaApprover");
			//获取出差表信息
			sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyBusinessTripService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			//查询当前用户所属的部门
			TSDepart tsDepart = sXyBusinessTripService.findHqlTSDepart(tsUser);
			//机构编码长度
			int orgCodeLength = tsDepart.getOrgCode().length();
			Map<String, Object> variables = new HashMap<String, Object>();
			//向流程中传入数据
			//传入出差表的ID（归档、显示在考勤异常表中等操作）
			variables.put("tripId", sXyBusinessTrip.getId());
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
			String nextApproverRoleCode = sXyBusinessTripService.findNextApproverRoleCode(sXyBusinessTrip.getFlowInstId(), "startLine=="+startLineNum, tsUser.getUserName());
			variables.put("action", "提交申请");
			variables.put("startLine", startLineNum);
			variables.put(nextApproverRoleCode, feaaApprover);
			sXyBusinessTripService.completeTask(sXyBusinessTrip.getFlowInstId(), variables, tsUser.getUserName(), null);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyBusinessTrip.setFlowState("2");
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "出差申请提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：批准出差申请
	 * @param sXyBusinessTrip
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAgreeTrip")
	@ResponseBody
	public AjaxJson doAgreeTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "批准成功";
		//审批人（用户编号(userName)）
		String nextApprover = request.getParameter("nextApprover");
		//批准原因
		String passReason = request.getParameter("passReason");
		//获取出差表信息
		sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		String lineName = "同意";
		String nextApproverRoleCode = sXyBusinessTripService.findNextApproverRoleCode(sXyBusinessTrip.getFlowInstId(), lineName, tsUser.getUserName());
		variables.put("isPass", "同意");
		variables.put(nextApproverRoleCode, nextApprover);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyBusinessTripService.completeTask(sXyBusinessTrip.getFlowInstId(), variables, tsUser.getUserName(), passReason);
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
	 * 直接上级、部门DM、副总裁、总裁：否决出差申请
	 * @param sXyBusinessTrip
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doNotAgreeTrip")
	@ResponseBody
	public AjaxJson doNotAgreeTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "驳回成功";
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//获取出差表信息
		sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
		//驳回原因
		String nopassReason = request.getParameter("nopassReason");
		try {
			Map<String, Object> variables = new HashMap<String, Object>();
			//向流程中传入数据
			variables.put("isPass", "驳回");
			//完成任务（以用户编号(userName)完成任务）
			sXyBusinessTripService.completeTask(sXyBusinessTrip.getFlowInstId(), variables, tsUser.getUserName(), nopassReason);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyBusinessTrip.setFlowState("3");
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
	 * @param sXyBusinessTrip
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "hrAgreeTrip")
	@ResponseBody
	public AjaxJson hrAgreeTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "出差申请备案成功";
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//获取出差表信息
		sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
		try {
			Map<String, Object> variables = new HashMap<String, Object>();
			//向流程中传入数据
			variables.put("isPass", "同意");
			//完成任务（以用户编号(userName)完成任务）
			sXyBusinessTripService.completeTask(sXyBusinessTrip.getFlowInstId(), variables, tsUser.getUserName(), null);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyBusinessTrip.setFlowState("4");
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "出差申请备案失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 查看流程图片
	 * @param sXyBusinessTrip
	 * @param response
	 */
	@RequestMapping(params = "lookFlowImage")
	public void lookFlowImage(SXyBusinessTripEntity sXyBusinessTrip, HttpServletResponse response) {
		try {
			response.setContentType("image/png");
			InputStream inputStream = sXyBusinessTripService.graphics(sXyBusinessTrip.getFlowInstId());
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
	 * 取消提交出差申请
	 * @param sXyBusinessTrip
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "cancelTrip")
	@ResponseBody
	public AjaxJson cancelTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "出差申请取消提交成功";
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//获取出差表信息
		sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
		try {
			Map<String, Object> variables = new HashMap<String, Object>();
			//向流程中传入数据
			variables.put("action", "取消申请");
			//完成任务（以用户编号(userName)完成任务）
			sXyBusinessTripService.completeTask(sXyBusinessTrip.getFlowInstId(), variables, tsUser.getUserName(), null);
			/**
			 * 流程状态：0：流程未启动、1：申请未提交、2：申请审批中、3：申请被驳回、
			 * 4：审批已完成、5：申请已取消
			 */
			sXyBusinessTrip.setFlowState("5");
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "出差申请取消提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 出差表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest req) {
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户所属的部门
		TSDepart tsDepart = sXyBusinessTripService.findHqlTSDepart(tsUser);
		req.setAttribute("tsUserPage", tsUser);
		req.setAttribute("tsDepartPage", tsDepart);
		return new ModelAndView("xyoa/activiti/businesstrip/sXyBusinessTrip-add");
	}
	/**
	 * 出差表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyBusinessTrip.getId())) {
			sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
			req.setAttribute("sXyBusinessTripPage", sXyBusinessTrip);
		}
		return new ModelAndView("xyoa/activiti/businesstrip/sXyBusinessTrip-update");
	}
	
	/**
	 * 出差表详情页面 跳转
	 * @param sXyBusinessTrip
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goDetail")
	public ModelAndView goDetail(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyBusinessTrip.getId())) {
			try {
				//出差表信息
				sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
				//业务表历史任务节点批注信息
				List<CommentBean> commentBeans = sXyBusinessTripService.findComment(sXyBusinessTrip.getFlowInstId());
				req.setAttribute("sXyBusinessTripPage", sXyBusinessTrip);
				req.setAttribute("commentBeansPage", commentBeans);
			} catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}
		}
		return new ModelAndView("xyoa/activiti/businesstrip/sXyBusinessTrip-detail");
	}
	/**
	 * 出差表提交申请 页面跳转
	 * @param sXyBusinessTrip
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goSubmitTrip")
	public ModelAndView goSubmitTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyBusinessTrip.getId())) {
			//获取出差表信息
			sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyBusinessTripService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			//查询当前用户所属的部门
			TSDepart tsDepart = sXyBusinessTripService.findHqlTSDepart(tsUser);
			//机构编码长度
			int orgCodeLength = tsDepart.getOrgCode().length();
			//查询该部门下的领导信息
			List<TSUser> tsUsers = null;
			String nextApproverRoleCode = null;
			if (Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 12) {
				nextApproverRoleCode = sXyBusinessTripService.findNextApproverRoleCode(sXyBusinessTrip.getFlowInstId(), "startLine==1", tsUser.getUserName());
				tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(nextApproverRoleCode, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门
					TSDepart tsPDepart = sXyBusinessTripService.findHqlTSPDepart(tsUser);
					tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(nextApproverRoleCode, tsPDepart);
				}
			}
			if ((Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 9) || Constants.HEADMAN.equals(roleCode)) {
				nextApproverRoleCode = sXyBusinessTripService.findNextApproverRoleCode(sXyBusinessTrip.getFlowInstId(), "startLine==2", tsUser.getUserName());
				tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(nextApproverRoleCode, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门
					TSDepart tsPDepart = sXyBusinessTripService.findHqlTSPDepart(tsUser);
					tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(nextApproverRoleCode, tsPDepart);
				}
			}
			if (Constants.HR.equals(roleCode)) {
				nextApproverRoleCode = sXyBusinessTripService.findNextApproverRoleCode(sXyBusinessTrip.getFlowInstId(), "startLine==2", tsUser.getUserName());
				tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(Constants.HR+nextApproverRoleCode, tsDepart);
			}
			if(Constants.DM.equals(roleCode) || Constants.HRDM.equals(roleCode)) {
				nextApproverRoleCode = sXyBusinessTripService.findNextApproverRoleCode(sXyBusinessTrip.getFlowInstId(), "startLine==3", tsUser.getUserName());
				tsUsers = sXyBusinessTripService.findHqlTSUsersByRoleCode(nextApproverRoleCode);
			}
			if(Constants.VICE.equals(roleCode)) {
				nextApproverRoleCode = sXyBusinessTripService.findNextApproverRoleCode(sXyBusinessTrip.getFlowInstId(), "startLine==4", tsUser.getUserName());
				tsUsers = sXyBusinessTripService.findHqlTSUsersByRoleCode(nextApproverRoleCode);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyBusinessTripPage", sXyBusinessTrip);
		}
		return new ModelAndView("xyoa/activiti/businesstrip/sXyBusinessTrip-submit");
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 出差表批准出差申请页面跳转
	 * @param sXyBusinessTrip
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goAgreeTrip")
	public ModelAndView goAgreeTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyBusinessTrip.getId())) {
			//获取出差表信息
			sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyBusinessTripService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			//查询当前用户的下一级审批人
			List<TSUser> tsUsers = null;
			String lineName = "同意";
			String nextApproverRoleCode = sXyBusinessTripService.findNextApproverRoleCode(sXyBusinessTrip.getFlowInstId(), lineName, tsUser.getUserName());
			if(Constants.HEADMAN.equals(roleCode)) {
				//查询当前用户所属的部门
				TSDepart tsDepart = sXyBusinessTripService.findHqlTSDepart(tsUser);
				tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(nextApproverRoleCode, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的部门DM
					TSDepart tsPDepart = sXyBusinessTripService.findHqlTSPDepart(tsUser);
					tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(nextApproverRoleCode, tsPDepart);
				}
			} else {
				tsUsers = sXyBusinessTripService.findHqlTSUsersByRoleCode(nextApproverRoleCode);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyBusinessTripPage", sXyBusinessTrip);
		}
		return new ModelAndView("xyoa/activiti/businesstrip/sXyBusinessTrip-check");
	}
	
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 出差表否决出差申请 页面跳转
	 * @param sXyBusinessTrip
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goNotAgreeTrip")
	public ModelAndView goNotAgreeTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyBusinessTrip.getId())) {
			sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
			req.setAttribute("sXyBusinessTripPage", sXyBusinessTrip);
		}
		return new ModelAndView("xyoa/activiti/businesstrip/sXyBusinessTrip-nocheck");
	}
	
	@RequestMapping(params = "importExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获取上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<SXyBusinessTripEntity> listSXyBusinessTripEntitys = ExcelImportUtil.importExcel(file.getInputStream(),SXyBusinessTripEntity.class,params);
				for (SXyBusinessTripEntity sXyBusinessTrip : listSXyBusinessTripEntitys) {
					sXyBusinessTripService.save(sXyBusinessTrip);
				}
				j.setMsg("文件导入成功！");
			} catch (Exception e) {
				j.setMsg("文件导入失败！");
				logger.error(ExceptionUtil.getExceptionMessage(e));
			}finally{
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return j;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<SXyBusinessTripEntity> list() {
		List<SXyBusinessTripEntity> listSXyBusinessTrips=sXyBusinessTripService.getList(SXyBusinessTripEntity.class);
		return listSXyBusinessTrips;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		SXyBusinessTripEntity task = sXyBusinessTripService.get(SXyBusinessTripEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody SXyBusinessTripEntity sXyBusinessTrip, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyBusinessTripEntity>> failures = validator.validate(sXyBusinessTrip);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyBusinessTripService.save(sXyBusinessTrip);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = sXyBusinessTrip.getId();
		URI uri = uriBuilder.path("/rest/sXyBusinessTripController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody SXyBusinessTripEntity sXyBusinessTrip) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyBusinessTripEntity>> failures = validator.validate(sXyBusinessTrip);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyBusinessTripService.saveOrUpdate(sXyBusinessTrip);
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
		sXyBusinessTripService.deleteEntityById(SXyBusinessTripEntity.class, id);
	}
}
