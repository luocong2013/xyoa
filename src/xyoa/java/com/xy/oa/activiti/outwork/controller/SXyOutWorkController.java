package com.xy.oa.activiti.outwork.controller;


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

import com.xy.oa.activiti.outwork.entity.OutWorkPo;
import com.xy.oa.activiti.outwork.entity.SXyOutWorkEntity;
import com.xy.oa.activiti.outwork.service.SXyOutWorkServiceI;
import com.xy.oa.activiti.util.CommentBean;
import com.xy.oa.calendars.service.SXyCalendarsServiceI;
import com.xy.oa.checkinout.service.SXyCheckinoutServiceI;
import com.xy.oa.util.ApplyTypeEnum;
import com.xy.oa.util.Constants;

/**   
 * @Title: Controller  
 * @Description: 享宇外出表
 * @author onlineGenerator
 * @date 2016-08-22 16:54:00
 * @version V1.0   
 *
 */
@Controller("sXyOutWorkController")
@RequestMapping("/sXyOutWorkController")
public class SXyOutWorkController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SXyOutWorkController.class);

	@Autowired
	private SXyOutWorkServiceI sXyOutWorkService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private SXyCalendarsServiceI sXyCalendarsService;
	@Autowired
	private SXyCheckinoutServiceI sXyCheckinoutService;
	

	/**
	 * 显示在考勤记录表中，为外出中
	 * 归档等操作
	 * @param outworkId：外出申请表ID
	 * @param flag：标志：1、显示在考勤表中，为外出中，2、归档等操作
	 */
	public void doSome(String outworkId, int flag) {
		SXyOutWorkEntity sXyOutWork = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, outworkId);
		if (flag == 1) {
			//显示在考勤表中，为外出中
			
		} else if (flag == 2) {
			//归档
			try {
				sXyCheckinoutService.disCheckInOut(sXyOutWork.getApplySttaffId(), DateUtils.formatDate(sXyOutWork.getStartTime()),
						DateUtils.formatDate(sXyOutWork.getEndTime()), sXyOutWork.getId(), Constants.XY_CHECK_TYPE_18, sXyOutWork.getRemarks());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 享宇外出表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyOutWorkService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/outwork/sXyOutWorkList");
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
	public void datagrid(SXyOutWorkEntity sXyOutWork,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SXyOutWorkEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyOutWork, request.getParameterMap());
		try{
			//自定义追加查询条件
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyOutWorkService.findHqlRoleCodes(tsUser);
			String roleCode = null;
			if (roleCodes.size() == 1) {
				roleCode = roleCodes.get(0);
			} else {
				roleCode = roleCodes.get(1);
			}
			
			//部门查询
			String orgId = request.getParameter("orgId");
			if (StringUtil.isNotEmpty(orgId)) {
//				TSDepart tsDepart = sXyOutWorkService.getEntity(TSDepart.class, orgId);
//				cq.eq("tsDept", tsDepart);
				
				TSDepart tsDepart = sXyOutWorkService.getEntity(TSDepart.class, orgId);
				List<TSDepart> tSDepartList = tsDepart.getTSDeparts();
				tSDepartList.add(0, tsDepart);
				cq.in("tsDept", tSDepartList.toArray());
			}
			
			if(Constants.DM.equals(roleCode)) {
				//查询该部门下的所有员工以及各种组的所有员工
				List<TSUser> tsUsers = sXyOutWorkService.findHqlTSUsersByUser(tsUser);
				if (tsUsers == null || tsUsers.isEmpty()) {
					cq.eq("tsUser", tsUser);
				} else {
					cq.or(cq.and(Restrictions.in("tsUser", tsUsers.toArray()), Restrictions.eq("flowState", "7")), Restrictions.eq("tsUser", tsUser));
				}
			} else if (Constants.HR.equals(roleCode) || Constants.HRDM.equals(roleCode) || Constants.VICE.equals(roleCode) || Constants.CEO.equals(roleCode)) {
				//查询普通员工、直接上级、人事专员、部门DM、人事DM和副总裁
				List<TSUser> tsUsers = sXyOutWorkService.findHqlTSUsersByUR(tsUser);
				cq.or(cq.and(Restrictions.in("tsUser", tsUsers.toArray()), Restrictions.eq("flowState", "7")), Restrictions.eq("tsUser", tsUser));
			} else {
				//查询当前用户提交的出差申请
				cq.eq("tsUser", tsUser);
			}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyOutWorkService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 直接上级、部门DM、副总裁、总裁审批页面
	 * 享宇 审核外出表单 页面跳转
	 * @return
	 */
	@RequestMapping(params = "listCheck")
	public ModelAndView listCheck(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyOutWorkService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/outwork/sXyOutWorkListCheck");
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
	public void datagridCheck(SXyOutWorkEntity sXyOutWork,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String spflag) {
		CriteriaQuery cq = new CriteriaQuery(SXyOutWorkEntity.class, dataGrid);
		//查询条件组装器
		String paramHql = org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHqlForFlow("S", cq, sXyOutWork, request.getParameterMap());
		try{
			//自定义追加查询条件
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//部门查询
			String orgId = request.getParameter("orgId");
			
			List<OutWorkPo> lPos = new ArrayList<OutWorkPo>();
			int total = 0;
			int beginNum = (cq.getCurPage()-1)*cq.getPageSize();
			if ("00".equals(spflag) || "01".equals(spflag)) {
				if ("00".equals(spflag)) {
					//查询当前登录用户的任务（以用户编号(userName)查询）
					sXyOutWorkService.getDataGridRunForHQL(SXyOutWorkEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyOutWorkService.getRunTotal(SXyOutWorkEntity.class, false, paramHql, orgId, tsUser.getUserName());
				} else if ("01".equals(spflag)) {
					//查看当前用户的历史任务（已审批过的）
					sXyOutWorkService.getDataGridHisForHQL(SXyOutWorkEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyOutWorkService.getHisTotal(SXyOutWorkEntity.class, false, paramHql, orgId, tsUser.getUserName());
				}
				List<SXyOutWorkEntity> lWorkEntities = dataGrid.getResults();
				for (SXyOutWorkEntity sOutWorkEntity : lWorkEntities) {
					OutWorkPo oPo = new OutWorkPo();
					BeanUtils.copyProperties(sOutWorkEntity, oPo);
					oPo.setIsApprove(spflag);
					lPos.add(oPo);
				}
			} else {
				//查询当前登录用户的任务的总条数（以用户编号(userName)查询）
				int totalR = sXyOutWorkService.getRunTotal(SXyOutWorkEntity.class, false, paramHql, orgId, tsUser.getUserName());
				//查看当前用户的历史任务的总条数（已审批过的）
				int totalH = sXyOutWorkService.getHisTotal(SXyOutWorkEntity.class, false, paramHql, orgId, tsUser.getUserName());
				total = totalR + totalH;
				if (totalR <= beginNum) {
					DataGrid dataGrid2 = new DataGrid();
					beginNum  -= totalR;
					//查看当前用户的历史任务（已审批过的）
					sXyOutWorkService.getDataGridHisForHQL(SXyOutWorkEntity.class, dataGrid2, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyOutWorkEntity> lWorkEntitiesH = dataGrid2.getResults();
					for (SXyOutWorkEntity sOutWorkEntity : lWorkEntitiesH) {
						OutWorkPo oPo = new OutWorkPo();
						BeanUtils.copyProperties(sOutWorkEntity, oPo);
						oPo.setIsApprove("01");
						lPos.add(oPo);
					}
				} else {
					//查询当前登录用户的任务（以用户编号(userName)查询）
					sXyOutWorkService.getDataGridRunForHQL(SXyOutWorkEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyOutWorkEntity> lWorkEntitiesR = dataGrid.getResults();
					for (SXyOutWorkEntity sOutWorkEntity : lWorkEntitiesR) {
						OutWorkPo oPo = new OutWorkPo();
						BeanUtils.copyProperties(sOutWorkEntity, oPo);
						oPo.setIsApprove("00");
						lPos.add(oPo);
					}
					if (lWorkEntitiesR.size() < cq.getPageSize()) {
						DataGrid dataGrid2 = new DataGrid();
						//查看当前用户的历史任务（已审批过的）
						sXyOutWorkService.getDataGridHisForHQL(SXyOutWorkEntity.class, dataGrid2, false, paramHql, orgId, 0, cq.getPageSize()-lWorkEntitiesR.size(), tsUser.getUserName());
						List<SXyOutWorkEntity> lWorkEntitiesH = dataGrid2.getResults();
						for (SXyOutWorkEntity sOutWorkEntity : lWorkEntitiesH) {
							OutWorkPo oPo = new OutWorkPo();
							BeanUtils.copyProperties(sOutWorkEntity, oPo);
							oPo.setIsApprove("01");
							lPos.add(oPo);
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
	 * HR 审批页面
	 * 享宇 审核外出表单 页面跳转
	 * @return
	 */
	@RequestMapping(params = "listHr")
	public ModelAndView listHr(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyOutWorkService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/outwork/sXyOutWorkListHr");
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
	public void datagridHr(SXyOutWorkEntity sXyOutWork,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String spflag) {
		CriteriaQuery cq = new CriteriaQuery(SXyOutWorkEntity.class, dataGrid);
		//查询条件组装器
		String paramHql = org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHqlForFlow("S", cq, sXyOutWork, request.getParameterMap());
		try{
			//自定义追加查询条件
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//部门查询
			String orgId = request.getParameter("orgId");
			
			List<OutWorkPo> lPos = new ArrayList<OutWorkPo>();
			int total = 0;
			int beginNum = (cq.getCurPage()-1)*cq.getPageSize();
			if ("00".equals(spflag) || "01".equals(spflag)) {
				if ("00".equals(spflag)) {
					//查询当前登录用户的任务（以用户编号(userName)查询）
					sXyOutWorkService.getDataGridRunForHQL(SXyOutWorkEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyOutWorkService.getRunTotal(SXyOutWorkEntity.class, true, paramHql, orgId, tsUser.getUserName());
				} else if ("01".equals(spflag)) {
					//查看当前用户的历史任务（已审批过的）
					sXyOutWorkService.getDataGridHisForHQL(SXyOutWorkEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyOutWorkService.getHisTotal(SXyOutWorkEntity.class, true, paramHql, orgId, tsUser.getUserName());
				}
				List<SXyOutWorkEntity> lWorkEntities = dataGrid.getResults();
				for (SXyOutWorkEntity sOutWorkEntity : lWorkEntities) {
					OutWorkPo oPo = new OutWorkPo();
					BeanUtils.copyProperties(sOutWorkEntity, oPo);
					oPo.setIsApprove(spflag);
					lPos.add(oPo);
				}
			} else {
				//查询当前登录用户的任务的总条数（以用户编号(userName)查询）
				int totalR = sXyOutWorkService.getRunTotal(SXyOutWorkEntity.class, true, paramHql, orgId, tsUser.getUserName());
				//查看当前用户的历史任务的总条数（已审批过的）
				int totalH = sXyOutWorkService.getHisTotal(SXyOutWorkEntity.class, true, paramHql, orgId, tsUser.getUserName());
				total = totalR + totalH;
				if (totalR <= beginNum) {
					DataGrid dataGrid2 = new DataGrid();
					beginNum  -= totalR;
					//查看当前用户的历史任务（已审批过的）
					sXyOutWorkService.getDataGridHisForHQL(SXyOutWorkEntity.class, dataGrid2, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyOutWorkEntity> lWorkEntitiesH = dataGrid2.getResults();
					for (SXyOutWorkEntity sOutWorkEntity : lWorkEntitiesH) {
						OutWorkPo oPo = new OutWorkPo();
						BeanUtils.copyProperties(sOutWorkEntity, oPo);
						oPo.setIsApprove("01");
						lPos.add(oPo);
					}
				} else {
					//查询当前登录用户的任务（以用户编号(userName)查询）
					sXyOutWorkService.getDataGridRunForHQL(SXyOutWorkEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyOutWorkEntity> lWorkEntitiesR = dataGrid.getResults();
					for (SXyOutWorkEntity sOutWorkEntity : lWorkEntitiesR) {
						OutWorkPo oPo = new OutWorkPo();
						BeanUtils.copyProperties(sOutWorkEntity, oPo);
						oPo.setIsApprove("00");
						lPos.add(oPo);
					}
					if (lWorkEntitiesR.size() < cq.getPageSize()) {
						DataGrid dataGrid2 = new DataGrid();
						//查看当前用户的历史任务（已审批过的）
						sXyOutWorkService.getDataGridHisForHQL(SXyOutWorkEntity.class, dataGrid2, true, paramHql, orgId, 0, cq.getPageSize()-lWorkEntitiesR.size(), tsUser.getUserName());
						List<SXyOutWorkEntity> lWorkEntitiesH = dataGrid2.getResults();
						for (SXyOutWorkEntity sOutWorkEntity : lWorkEntitiesH) {
							OutWorkPo oPo = new OutWorkPo();
							BeanUtils.copyProperties(sOutWorkEntity, oPo);
							oPo.setIsApprove("01");
							lPos.add(oPo);
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
	 * 删除享宇外出表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(SXyOutWorkEntity sXyOutWork, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		sXyOutWork = systemService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
		message = "享宇外出表删除成功";
		try{
			sXyOutWorkService.delete(sXyOutWork);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇外出表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除享宇外出表
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇外出表删除成功";
		try{
			for(String id:ids.split(",")){
				SXyOutWorkEntity sXyOutWork = systemService.getEntity(SXyOutWorkEntity.class, id);
				sXyOutWorkService.delete(sXyOutWork);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇外出表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加享宇外出表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(SXyOutWorkEntity sXyOutWork, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇外出表添加成功";
		try{
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户所属的部门
			TSDepart tsDepart = sXyOutWorkService.findHqlTSDepart(tsUser);
			//设置申请人
			sXyOutWork.setTsUser(tsUser);
			//设置申请人编号
			sXyOutWork.setApplySttaffId(Integer.valueOf(tsUser.getUserName()));
			//设置申请人部门
			sXyOutWork.setTsDept(tsDepart);
			//设置外出开始时间
			sXyOutWork.setStartTime(sXyOutWork.getOutStartTime());
			//设置创建时间
			sXyOutWork.setCreateTime(new Date());
			//设置创建人编号
			sXyOutWork.setCUser(Integer.valueOf(tsUser.getUserName()));
			/**
			 * 流程状态：0：流程未启动、1：外出待审批、2：外出审批中、3：外出申请被否决、4：外出审批通过、
			 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
			 */
			sXyOutWork.setFlowState("0");
			sXyOutWorkService.save(sXyOutWork);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇外出表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新享宇外出表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(SXyOutWorkEntity sXyOutWork, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇外出表更新成功";
		SXyOutWorkEntity t = sXyOutWorkService.get(SXyOutWorkEntity.class, sXyOutWork.getId());
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		try {
			//设置外出开始时间
			sXyOutWork.setStartTime(sXyOutWork.getOutStartTime());
			//设置更新时间
			sXyOutWork.setUTime(new Date());
			//设置更新人编号
			sXyOutWork.setUUser(Integer.valueOf(tsUser.getUserName()));
			MyBeanUtils.copyBeanNotNull2Bean(sXyOutWork, t);
			sXyOutWorkService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇外出表更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 提交享宇外出表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doSubmitOutwork")
	@ResponseBody
	public AjaxJson doSubmitOutwork(SXyOutWorkEntity sXyOutWork, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇外出表提交成功";
		//审批人（用户编号(userName)）
		String feaaApprover = request.getParameter("feaaApprover");
		//获取外出表信息
		sXyOutWork = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户的角色编码
		List<String> roleCodes = sXyOutWorkService.findHqlRoleCodes(tsUser);
		String roleCode = roleCodes.get(0);
		//查询当前用户的部门
		TSDepart tsDepart = sXyOutWorkService.findHqlTSDepart(tsUser);
		//机构编码长度
		int orgCodeLength = tsDepart.getOrgCode().length();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		//传入外出表的ID（归档、显示在考勤异常表中等操作）
		variables.put("outworkId", sXyOutWork.getId());
		//判断流程走向
		if (Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 12) {
			variables.put("startLine", 1);
			variables.put(Constants.HEADMAN, feaaApprover);
		}
		if ((Constants.EMPLOYEE.equals(roleCode) || Constants.HR.equals(roleCode)) && orgCodeLength == 9) {
			//查询当前部门下的直接上级
			List<TSUser> tsUsers = sXyOutWorkService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
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
			 * 流程状态：0：流程未启动、1：外出待审批、2：外出审批中、3：外出申请被否决、4：外出审批通过、
			 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
			 */
			sXyOutWork.setFlowState("1");
			//设置申请日期
			sXyOutWork.setApplyDate(new Date());
			//设置申请编号
			sXyOutWork.setApplyNo(sXyOutWorkService.getNextApplyNo(ApplyTypeEnum.GC, "s_xy_out_work"));
			//启动流程（以用户编号(userName)作为启动人）
			String flowInstId = sXyOutWorkService.startFlow("xyOutWork", sXyOutWork.getId(), variables, tsUser.getUserName());
			//设置流程实例ID
			sXyOutWork.setFlowInstId(flowInstId);
			//保存
			sXyOutWorkService.saveOrUpdate(sXyOutWork);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇外出表提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：批准享宇外出申请
	 * @param sXyOutWork
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAgreeOutwork")
	@ResponseBody
	public AjaxJson doAgreeOutwork(SXyOutWorkEntity sXyOutWork, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇外出表批准成功";
		//审批人（用户编号(userName)）
		String nextApprover = request.getParameter("nextApprover");
		//批准原因
		String passReason = request.getParameter("passReason");
		//获取外出表信息
		sXyOutWork = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
		/**
		 * 流程状态：0：流程未启动、1：外出待审批、2：外出审批中、3：外出申请被否决、4：外出审批通过、
		 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyOutWork.setFlowState("2");
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户的角色编码
		List<String> roleCodes = sXyOutWorkService.findHqlRoleCodes(tsUser);
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
			sXyOutWorkService.completeTask(sXyOutWork.getFlowInstId(), variables, tsUser.getUserName(), passReason);
			//保存状态
			sXyOutWorkService.saveOrUpdate(sXyOutWork);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇外出表批准失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：否决享宇外出申请
	 * @param sXyOutWork
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doNotAgreeOutwork")
	@ResponseBody
	public AjaxJson doNotAgreeOutwork(SXyOutWorkEntity sXyOutWork, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇外出表否决成功";
		//否决原因
		String nopassReason = request.getParameter("nopassReason");
		//获取外出表信息
		sXyOutWork = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
		/**
		 * 流程状态：0：流程未启动、1：外出待审批、2：外出审批中、3：外出申请被否决、4：外出审批通过、
		 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyOutWork.setFlowState("3");
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("isPass", 0);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyOutWorkService.completeTask(sXyOutWork.getFlowInstId(), variables, tsUser.getUserName(), nopassReason);
			//保存状态
			sXyOutWorkService.saveOrUpdate(sXyOutWork);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇外出表否决失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * HR：批准享宇外出申请
	 * @param sXyOutWork
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doHrAgreeOutwork")
	@ResponseBody
	public AjaxJson doHrAgreeOutwork(SXyOutWorkEntity sXyOutWork, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇外出表批准成功";
		//批准原因
		String hrpassReason = request.getParameter("hrpassReason");
		//获取外出表信息
		sXyOutWork = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
		/**
		 * 流程状态：0：流程未启动、1：外出待审批、2：外出审批中、3：外出申请被否决、4：外出审批通过、
		 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		if("2".equals(sXyOutWork.getFlowState())) {
			sXyOutWork.setFlowState("4");
			variables.put("isPass", 3);
		}
		if("5".equals(sXyOutWork.getFlowState())) {
			sXyOutWork.setFlowState("7");
			variables.put("isPass", 6);
		}
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyOutWorkService.completeTask(sXyOutWork.getFlowInstId(), variables, tsUser.getUserName(), hrpassReason);
			//保存状态
			sXyOutWorkService.saveOrUpdate(sXyOutWork);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇外出表批准失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * HR：否决享宇外出申请
	 * @param sXyOutWork
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doHrNotAgreeOutwork")
	@ResponseBody
	public AjaxJson doHrNotAgreeOutwork(SXyOutWorkEntity sXyOutWork, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇外出表否决成功";
		//否决原因
		String hrnopassReason = request.getParameter("hrnopassReason");
		//获取外出表信息
		sXyOutWork = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
		/**
		 * 流程状态：0：流程未启动、1：外出待审批、2：外出审批中、3：外出申请被否决、4：外出审批通过、
		 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		if("2".equals(sXyOutWork.getFlowState())) {
			sXyOutWork.setFlowState("3");
			variables.put("isPass", 0);
		}
		if("5".equals(sXyOutWork.getFlowState())) {
			sXyOutWork.setFlowState("6");
			variables.put("isPass", 7);
		}
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyOutWorkService.completeTask(sXyOutWork.getFlowInstId(), variables, tsUser.getUserName(), hrnopassReason);
			//保存状态
			sXyOutWorkService.saveOrUpdate(sXyOutWork);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇外出表否决失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 享宇外出申请  提交销假申请
	 * @param sXyOutWork
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "dobackOutwork")
	@ResponseBody
	public AjaxJson dobackOutwork(SXyOutWorkEntity sXyOutWork, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇外出表销假申请提交成功";
		//获取外出表信息
		SXyOutWorkEntity t = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
		/**
		 * 流程状态：0：流程未启动、1：外出待审批、2：外出审批中、3：外出申请被否决、4：外出审批通过、
		 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyOutWork.setFlowState("5");
		//设置外出结束时间
		sXyOutWork.setEndTime(sXyOutWork.getBackDate());
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		try {
			//真实外出时长
			BigDecimal bigDecimal = sXyCalendarsService.getTimeInterval(t.getStartTime(), sXyOutWork.getEndTime(), true);
			sXyOutWork.setOutHour(bigDecimal);
			MyBeanUtils.copyBeanNotNull2Bean(sXyOutWork, t);
			//完成任务（以用户编号(userName)完成任务）
			sXyOutWorkService.completeTask(t.getFlowInstId(), variables, tsUser.getUserName(), null);
			//保存状态
			sXyOutWorkService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇外出表销假申请提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 查看流程图片
	 * @param sXyOutWork
	 * @param response
	 */
	@RequestMapping(params = "lookFlowImage")
	public void lookFlowImage(SXyOutWorkEntity sXyOutWork, HttpServletResponse response) {
		try {
			response.setContentType("image/png");
			InputStream inputStream = sXyOutWorkService.graphics(sXyOutWork.getFlowInstId());
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
	 * 重新提交外出申请
	 * @param sXyOutWork
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "reSubmitOutwork")
	@ResponseBody
	public AjaxJson reSubmitOutwork(SXyOutWorkEntity sXyOutWork, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇外出表重新提交成功";
		//获取外出表信息
		sXyOutWork = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
		/**
		 * 流程状态：0：流程未启动、1：外出待审批、2：外出审批中、3：外出申请被否决、4：外出审批通过、
		 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyOutWork.setFlowState("1");
		//重新设置申请日期
		sXyOutWork.setApplyDate(new Date());
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("reApply", true);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyOutWorkService.completeTask(sXyOutWork.getFlowInstId(), variables, tsUser.getUserName(), null);
			//保存状态
			sXyOutWorkService.saveOrUpdate(sXyOutWork);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇外出表重新提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 撤销外出申请
	 * @param sXyOutWork
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delOutwork")
	@ResponseBody
	public AjaxJson delOutwork(SXyOutWorkEntity sXyOutWork, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇外出申请撤销成功";
		//获取外出表信息
		sXyOutWork = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
		/**
		 * 流程状态：0：流程未启动、1：外出待审批、2：外出审批中、3：外出申请被否决、4：外出审批通过、
		 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyOutWork.setFlowState("8");
		try {
			//撤销申请
			sXyOutWorkService.delFlow(sXyOutWork.getFlowInstId());
			//保存状态
			sXyOutWorkService.saveOrUpdate(sXyOutWork);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇外出申请撤销失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 取消提交外出申请
	 * @param sXyOutWork
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "cancelOutwork")
	@ResponseBody
	public AjaxJson cancelOutwork(SXyOutWorkEntity sXyOutWork, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇外出表取消提交成功";
		//获取外出表信息
		sXyOutWork = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
		/**
		 * 流程状态：0：流程未启动、1：外出待审批、2：外出审批中、3：外出申请被否决、4：外出审批通过、
		 * 5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyOutWork.setFlowState("9");
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("reApply", false);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyOutWorkService.completeTask(sXyOutWork.getFlowInstId(), variables, tsUser.getUserName(), null);
			//保存状态
			sXyOutWorkService.saveOrUpdate(sXyOutWork);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇外出表取消提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 享宇外出表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(SXyOutWorkEntity sXyOutWork, HttpServletRequest req) {
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户所属的部门
		TSDepart tsDepart = sXyOutWorkService.findHqlTSDepart(tsUser);
		req.setAttribute("tsUserPage", tsUser);
		req.setAttribute("tsDepartPage", tsDepart);
		return new ModelAndView("xyoa/activiti/outwork/sXyOutWork-add");
	}
	/**
	 * 享宇外出表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(SXyOutWorkEntity sXyOutWork, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyOutWork.getId())) {
			sXyOutWork = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
			req.setAttribute("sXyOutWorkPage", sXyOutWork);
		}
		return new ModelAndView("xyoa/activiti/outwork/sXyOutWork-update");
	}
	/**
	 * 享宇外出表详情页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goDetail")
	public ModelAndView goDetail(SXyOutWorkEntity sXyOutWork, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyOutWork.getId())) {
			try {
				//外出表信息
				sXyOutWork = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
				//业务表历史任务节点批注信息
				List<CommentBean> commentBeans = sXyOutWorkService.findComment(sXyOutWork.getFlowInstId());
				req.setAttribute("sXyOutWorkPage", sXyOutWork);
				req.setAttribute("commentBeansPage", commentBeans);
			} catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}
		}
		return new ModelAndView("xyoa/activiti/outwork/sXyOutWork-detail");
	}
	/**
	 * 享宇外出表提交申请页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goSubmitOutwork")
	public ModelAndView goSubmitOutwork(SXyOutWorkEntity sXyOutWork, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyOutWork.getId())) {
			//获取外出表信息
			sXyOutWork = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyOutWorkService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			//查询当前用户所属的部门
			TSDepart tsDepart = sXyOutWorkService.findHqlTSDepart(tsUser);
			//机构编码长度
			int orgCodeLength = tsDepart.getOrgCode().length();
			//查询该部门下的领导信息
			List<TSUser> tsUsers = null;
			if (Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 12) {
				tsUsers = sXyOutWorkService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的直接上级
					TSDepart tsPDepart = sXyOutWorkService.findHqlTSPDepart(tsUser);
					tsUsers = sXyOutWorkService.findHqlTSUsersByRD(Constants.HEADMAN, tsPDepart);
				}
			}
			if ((Constants.EMPLOYEE.equals(roleCode) || Constants.HR.equals(roleCode)) && orgCodeLength == 9) {
				//查询当前部门下的直接上级
				tsUsers = sXyOutWorkService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前部门下的部门DM
					tsUsers = sXyOutWorkService.findHqlTSUsersByRD(Constants.DM, tsDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的部门DM
						TSDepart tsPDepart = sXyOutWorkService.findHqlTSPDepart(tsUser);
						tsUsers = sXyOutWorkService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					}
				}
			}
			if (Constants.HEADMAN.equals(roleCode)) {
				tsUsers = sXyOutWorkService.findHqlTSUsersByRD(Constants.DM, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的部门DM
					TSDepart tsPDepart = sXyOutWorkService.findHqlTSPDepart(tsUser);
					tsUsers = sXyOutWorkService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的上一级部门的部门DM
						TSDepart tsPPDepart = sXyOutWorkService.findHqlTSPPDepart(tsUser);
						tsUsers = sXyOutWorkService.findHqlTSUsersByRD(Constants.DM, tsPPDepart);
					}
				}
			}
			if(Constants.DM.equals(roleCode) || Constants.HRDM.equals(roleCode)) {
				tsUsers = sXyOutWorkService.findHqlTSUsersByRoleCode(Constants.VICE);
			}
			if(Constants.VICE.equals(roleCode)) {
				tsUsers = sXyOutWorkService.findHqlTSUsersByRoleCode(Constants.CEO);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyOutWorkPage", sXyOutWork);
		}
		return new ModelAndView("xyoa/activiti/outwork/sXyOutWork-submit");
	}
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 享宇外出表 批准外出申请页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAgreeOutwork")
	public ModelAndView goAgreeOutwork(SXyOutWorkEntity sXyOutWork, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyOutWork.getId())) {
			//获取外出表信息
			sXyOutWork = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyOutWorkService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			//查询当前用户的下一级审批人
			List<TSUser> tsUsers = null;
			if(Constants.HEADMAN.equals(roleCode)) {
				//查询当前用户所属的部门
				TSDepart tsDepart = sXyOutWorkService.findHqlTSDepart(tsUser);
				tsUsers = sXyOutWorkService.findHqlTSUsersByRD(Constants.DM, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的部门DM
					TSDepart tsPDepart = sXyOutWorkService.findHqlTSPDepart(tsUser);
					tsUsers = sXyOutWorkService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的上一级部门的部门DM
						TSDepart tsPPDepart = sXyOutWorkService.findHqlTSPPDepart(tsUser);
						tsUsers = sXyOutWorkService.findHqlTSUsersByRD(Constants.DM, tsPPDepart);
					}
				}
			} else {
				//查询HRDM
				tsUsers = sXyOutWorkService.findHqlTSUsersByRoleCode(Constants.HRDM);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyOutWorkPage", sXyOutWork);
		}
		return new ModelAndView("xyoa/activiti/outwork/sXyOutWork-check");
	}
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 享宇外出表 否决外出申请页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goNotAgreeOutwork")
	public ModelAndView goNotAgreeOutwork(SXyOutWorkEntity sXyOutWork, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyOutWork.getId())) {
			sXyOutWork = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
			req.setAttribute("sXyOutWorkPage", sXyOutWork);
		}
		return new ModelAndView("xyoa/activiti/outwork/sXyOutWork-nocheck");
	}
	/**
	 *  HR
	 * 享宇外出表 批准外出申请页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goHrAgreeOutwork")
	public ModelAndView goHrAgreeOutwork(SXyOutWorkEntity sXyOutWork, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyOutWork.getId())) {
			sXyOutWork = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
			req.setAttribute("sXyOutWorkPage", sXyOutWork);
		}
		return new ModelAndView("xyoa/activiti/outwork/sXyOutWork-hrcheck");
	}
	/**
	 *  HR
	 * 享宇外出表 否决外出申请页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goHrNotAgreeOutwork")
	public ModelAndView goHrNotAgreeOutwork(SXyOutWorkEntity sXyOutWork, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyOutWork.getId())) {
			sXyOutWork = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
			req.setAttribute("sXyOutWorkPage", sXyOutWork);
		}
		return new ModelAndView("xyoa/activiti/outwork/sXyOutWork-hrnocheck");
	}
	/**
	 * 享宇外出表 销假页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "gobackOutwork")
	public ModelAndView gobackOutwork(SXyOutWorkEntity sXyOutWork, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyOutWork.getId())) {
			sXyOutWork = sXyOutWorkService.getEntity(SXyOutWorkEntity.class, sXyOutWork.getId());
			req.setAttribute("sXyOutWorkPage", sXyOutWork);
		}
		return new ModelAndView("xyoa/activiti/outwork/sXyOutWork-back");
	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<SXyOutWorkEntity> list() {
		List<SXyOutWorkEntity> listSXyOutWorks=sXyOutWorkService.getList(SXyOutWorkEntity.class);
		return listSXyOutWorks;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		SXyOutWorkEntity task = sXyOutWorkService.get(SXyOutWorkEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody SXyOutWorkEntity sXyOutWork, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyOutWorkEntity>> failures = validator.validate(sXyOutWork);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyOutWorkService.save(sXyOutWork);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = sXyOutWork.getId();
		URI uri = uriBuilder.path("/rest/sXyOutWorkController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody SXyOutWorkEntity sXyOutWork) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyOutWorkEntity>> failures = validator.validate(sXyOutWork);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyOutWorkService.saveOrUpdate(sXyOutWork);
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
		sXyOutWorkService.deleteEntityById(SXyOutWorkEntity.class, id);
	}
}
