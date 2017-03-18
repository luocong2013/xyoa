package com.xy.oa.checkapply.controller;


import java.io.InputStream;
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
import com.xy.oa.checkapply.entity.CheckApplyPo;
import com.xy.oa.checkapply.entity.SXyCheckApplyEntity;
import com.xy.oa.checkapply.service.SXyCheckApplyServiceI;
import com.xy.oa.checkinout.entity.SXyCheckinoutEntity;
import com.xy.oa.util.ApplyTypeEnum;
import com.xy.oa.util.Constants;

/**   
 * @Title: Controller  
 * @Description: 享宇考勤异常申请表
 * @author onlineGenerator
 * @date 2016-09-12 16:48:58
 * @version V1.0   
 *
 */
@Controller("sXyCheckApplyController")
@RequestMapping("/sXyCheckApplyController")
public class SXyCheckApplyController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SXyCheckApplyController.class);

	@Autowired
	private SXyCheckApplyServiceI sXyCheckApplyService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	

	/**
	 * 归档、修改考勤状态等操作
	 * @param checkId
	 */
	public void doSome(String checkId) {
		try {
			SXyCheckApplyEntity sXyCheckApply = sXyCheckApplyService.getEntity(SXyCheckApplyEntity.class, checkId);
			for (String id : sXyCheckApply.getCheckIds().split(",")) {
				SXyCheckinoutEntity sXyCheckinout = sXyCheckApplyService.getEntity(SXyCheckinoutEntity.class, Integer.valueOf(id));
				sXyCheckinout.setCheckType(sXyCheckApply.getCheckType());
				sXyCheckinout.setApplyId(sXyCheckApply.getId());
				sXyCheckinout.setCheckRemarks(sXyCheckApply.getRemarks());
				sXyCheckinout.setIsCheckTrue(Constants.CHECKINOUT_IS_TRUE);
				sXyCheckinout.setEarlierMinute(0);
				sXyCheckinout.setLateMinute(0);
				sXyCheckApplyService.saveOrUpdate(sXyCheckinout);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 享宇考勤申请表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyCheckApplyService.getTSDepartAllStr());
		return new ModelAndView("xyoa/checkapply/sXyCheckApplyList");
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
	public void datagrid(SXyCheckApplyEntity sXyCheckApply,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SXyCheckApplyEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyCheckApply, request.getParameterMap());
		try{
			//自定义追加查询条件
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyCheckApplyService.findHqlRoleCodes(tsUser);
			String roleCode = null;
			if (roleCodes.size() == 1) {
				roleCode = roleCodes.get(0);
			} else {
			   roleCode = roleCodes.get(1);
			}
			
			//部门查询
			String orgId = request.getParameter("orgId");
			if (StringUtil.isNotEmpty(orgId)) {
//				TSDepart tsDepart = sXyCheckApplyService.getEntity(TSDepart.class, orgId);
//				cq.eq("tsDept", tsDepart);
				
				TSDepart tsDepart = sXyCheckApplyService.getEntity(TSDepart.class, orgId);
				List<TSDepart> tSDepartList = tsDepart.getTSDeparts();
				tSDepartList.add(0, tsDepart);
				cq.in("tsDept", tSDepartList.toArray());
			}
			
			if(Constants.DM.equals(roleCode)) {
				//查询该部门下的所有员工以及各种组的所有员工
				List<TSUser> tsUsers = sXyCheckApplyService.findHqlTSUsersByUser(tsUser);
				if (tsUsers == null || tsUsers.isEmpty()) {
					cq.eq("tsUser", tsUser);
				} else {
					cq.or(cq.and(Restrictions.in("tsUser", tsUsers.toArray()), Restrictions.eq("flowState", "4")), Restrictions.eq("tsUser", tsUser));
				}
			} else if (Constants.HR.equals(roleCode) || Constants.HRDM.equals(roleCode) || Constants.VICE.equals(roleCode) || Constants.CEO.equals(roleCode)) {
				//查询普通员工、直接上级、人事专员、部门DM、人事DM和副总裁
				List<TSUser> tsUsers = sXyCheckApplyService.findHqlTSUsersByUR(tsUser);
				cq.or(cq.and(Restrictions.in("tsUser", tsUsers.toArray()), Restrictions.eq("flowState", "4")), Restrictions.eq("tsUser", tsUser));
			} else {
				//查询当前登录用户提交的加班申请
				cq.eq("tsUser", tsUser);
			}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyCheckApplyService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 直接上级、部门DM、副总裁、总裁
	 * 审批考勤异常申请表单 页面跳转
	 * @return
	 */
	@RequestMapping(params = "listCheck")
	public ModelAndView listCheck(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyCheckApplyService.getTSDepartAllStr());
		return new ModelAndView("xyoa/checkapply/sXyCheckApplyListCheck");
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
	public void datagridCheck(SXyCheckApplyEntity sXyCheckApply,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String spflag) {
		CriteriaQuery cq = new CriteriaQuery(SXyCheckApplyEntity.class, dataGrid);
		//查询条件组装器
		String paramHql = org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHqlForFlow("S", cq, sXyCheckApply, request.getParameterMap());
		try{
			//自定义追加查询条件
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//部门查询
			String orgId = request.getParameter("orgId");
			
			List<CheckApplyPo> lPos = new ArrayList<CheckApplyPo>();
			int total = 0;
			int beginNum = (cq.getCurPage()-1)*cq.getPageSize();
			if ("00".equals(spflag) || "01".equals(spflag)) {
				if ("00".equals(spflag)) {
					//查看当前登录用户的任务（以用户编号(userName)查询）
					sXyCheckApplyService.getDataGridRunForHQL(SXyCheckApplyEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyCheckApplyService.getRunTotal(SXyCheckApplyEntity.class, false, paramHql, orgId, tsUser.getUserName());
				} else if ("01".equals(spflag)) {
					//查看当前用户的历史任务（已审批过的）
					sXyCheckApplyService.getDataGridHisForHQL(SXyCheckApplyEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyCheckApplyService.getHisTotal(SXyCheckApplyEntity.class, false, paramHql, orgId, tsUser.getUserName());
				}
				List<SXyCheckApplyEntity> lApplyEntities = dataGrid.getResults();
				for (SXyCheckApplyEntity sCheckApplyEntity : lApplyEntities) {
					CheckApplyPo cPo = new CheckApplyPo();
					BeanUtils.copyProperties(sCheckApplyEntity, cPo);
					cPo.setIsApprove(spflag);
					lPos.add(cPo);
				}
			} else {
				//查询当前登录用户的任务的总条数（以用户编号(userName)查询）
				int totalR = sXyCheckApplyService.getRunTotal(SXyCheckApplyEntity.class, false, paramHql, orgId, tsUser.getUserName());
				//查看当前用户的历史任务的总条数（已审批过的）
				int totalH = sXyCheckApplyService.getHisTotal(SXyCheckApplyEntity.class, false, paramHql, orgId, tsUser.getUserName());
				total = totalR + totalH;
				if (totalR <= beginNum) {
					DataGrid dataGrid2 = new DataGrid();
					beginNum  -= totalR;
					//查看当前用户的历史任务（已审批过的）
					sXyCheckApplyService.getDataGridHisForHQL(SXyCheckApplyEntity.class, dataGrid2, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyCheckApplyEntity> lApplyEntitiesH = dataGrid2.getResults();
					for (SXyCheckApplyEntity sCheckApplyEntity : lApplyEntitiesH) {
						CheckApplyPo cPo = new CheckApplyPo();
						BeanUtils.copyProperties(sCheckApplyEntity, cPo);
						cPo.setIsApprove("01");
						lPos.add(cPo);
					}
				} else {
					//查看当前登录用户的任务（以用户编号(userName)查询）
					sXyCheckApplyService.getDataGridRunForHQL(SXyCheckApplyEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyCheckApplyEntity> lApplyEntitiesR = dataGrid.getResults();
					for (SXyCheckApplyEntity sCheckApplyEntity : lApplyEntitiesR) {
						CheckApplyPo cPo = new CheckApplyPo();
						BeanUtils.copyProperties(sCheckApplyEntity, cPo);
						cPo.setIsApprove("00");
						lPos.add(cPo);
					}
					if (lApplyEntitiesR.size() < cq.getPageSize()) {
						DataGrid dataGrid2 = new DataGrid();
						//查看当前用户的历史任务（已审批过的）
						sXyCheckApplyService.getDataGridHisForHQL(SXyCheckApplyEntity.class, dataGrid2, false, paramHql, orgId, 0, cq.getPageSize()-lApplyEntitiesR.size(), tsUser.getUserName());
						List<SXyCheckApplyEntity> lApplyEntitiesH = dataGrid2.getResults();
						for (SXyCheckApplyEntity sCheckApplyEntity : lApplyEntitiesH) {
							CheckApplyPo cPo = new CheckApplyPo();
							BeanUtils.copyProperties(sCheckApplyEntity, cPo);
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
	 * HR
	 * 审批考勤异常申请表单 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "listHr")
	public ModelAndView listHr(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyCheckApplyService.getTSDepartAllStr());
		return new ModelAndView("xyoa/checkapply/sXyCheckApplyListHr");
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
	public void datagridHr(SXyCheckApplyEntity sXyCheckApply,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String spflag) {
		CriteriaQuery cq = new CriteriaQuery(SXyCheckApplyEntity.class, dataGrid);
		//查询条件组装器
		String paramHql = org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHqlForFlow("S", cq, sXyCheckApply, request.getParameterMap());
		try{
			//自定义追加查询条件
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//部门查询
			String orgId = request.getParameter("orgId");
			
			List<CheckApplyPo> lPos = new ArrayList<CheckApplyPo>();
			int total = 0;
			int beginNum = (cq.getCurPage()-1)*cq.getPageSize();
			if ("00".equals(spflag) || "01".equals(spflag)) {
				if ("00".equals(spflag)) {
					//查看当前登录用户的任务（以用户编号(userName)查询）
					sXyCheckApplyService.getDataGridRunForHQL(SXyCheckApplyEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyCheckApplyService.getRunTotal(SXyCheckApplyEntity.class, true, paramHql, orgId, tsUser.getUserName());
				} else if ("01".equals(spflag)) {
					//查看当前用户的历史任务（已审批过的）
					sXyCheckApplyService.getDataGridHisForHQL(SXyCheckApplyEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyCheckApplyService.getHisTotal(SXyCheckApplyEntity.class, true, paramHql, orgId, tsUser.getUserName());
				}
				List<SXyCheckApplyEntity> lApplyEntities = dataGrid.getResults();
				for (SXyCheckApplyEntity sCheckApplyEntity : lApplyEntities) {
					CheckApplyPo cPo = new CheckApplyPo();
					BeanUtils.copyProperties(sCheckApplyEntity, cPo);
					cPo.setIsApprove(spflag);
					lPos.add(cPo);
				}
			} else {
				//查询当前登录用户的任务的总条数（以用户编号(userName)查询）
				int totalR = sXyCheckApplyService.getRunTotal(SXyCheckApplyEntity.class, true, paramHql, orgId, tsUser.getUserName());
				//查看当前用户的历史任务的总条数（已审批过的）
				int totalH = sXyCheckApplyService.getHisTotal(SXyCheckApplyEntity.class, true, paramHql, orgId, tsUser.getUserName());
				total = totalR + totalH;
				if (totalR <= beginNum) {
					DataGrid dataGrid2 = new DataGrid();
					beginNum  -= totalR;
					//查看当前用户的历史任务（已审批过的）
					sXyCheckApplyService.getDataGridHisForHQL(SXyCheckApplyEntity.class, dataGrid2, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyCheckApplyEntity> lApplyEntitiesH = dataGrid2.getResults();
					for (SXyCheckApplyEntity sCheckApplyEntity : lApplyEntitiesH) {
						CheckApplyPo cPo = new CheckApplyPo();
						BeanUtils.copyProperties(sCheckApplyEntity, cPo);
						cPo.setIsApprove("01");
						lPos.add(cPo);
					}
				} else {
					//查看当前登录用户的任务（以用户编号(userName)查询）
					sXyCheckApplyService.getDataGridRunForHQL(SXyCheckApplyEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyCheckApplyEntity> lApplyEntitiesR = dataGrid.getResults();
					for (SXyCheckApplyEntity sCheckApplyEntity : lApplyEntitiesR) {
						CheckApplyPo cPo = new CheckApplyPo();
						BeanUtils.copyProperties(sCheckApplyEntity, cPo);
						cPo.setIsApprove("00");
						lPos.add(cPo);
					}
					if (lApplyEntitiesR.size() < cq.getPageSize()) {
						DataGrid dataGrid2 = new DataGrid();
						//查看当前用户的历史任务（已审批过的）
						sXyCheckApplyService.getDataGridHisForHQL(SXyCheckApplyEntity.class, dataGrid2, true, paramHql, orgId, 0, cq.getPageSize()-lApplyEntitiesR.size(), tsUser.getUserName());
						List<SXyCheckApplyEntity> lApplyEntitiesH = dataGrid2.getResults();
						for (SXyCheckApplyEntity sCheckApplyEntity : lApplyEntitiesH) {
							CheckApplyPo cPo = new CheckApplyPo();
							BeanUtils.copyProperties(sCheckApplyEntity, cPo);
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
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "datagridAdd")
	public void datagridAdd(SXyCheckinoutEntity sXyCheckinout, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SXyCheckinoutEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyCheckinout, request.getParameterMap());
		try{
			//自定义追加查询条件
			String flag = request.getParameter("flag");
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			cq.eq("staffId", Integer.valueOf(tsUser.getUserName()));
			//只查询考勤异常的
			cq.eq("isCheckTrue", "01");
			cq.ge("checkDate", DateUtils.getLastMonth(21));
			//考勤异常表状态：(flowState)  0：考勤异常、1：考勤异常已添加到考勤异常申请表
			if (StringUtil.isEmpty(flag)) {
				cq.isNull("flowState");
			} else {
				cq.or(Restrictions.isNull("flowState"), Restrictions.eq("flowState", "1"));
			}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyCheckApplyService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "datagridDetail")
	public void datagridDetail(SXyCheckinoutEntity sXyCheckinout, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SXyCheckinoutEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyCheckinout, request.getParameterMap());
		try{
			//自定义追加查询条件
			String checkIds = request.getParameter("checkIds");
			String[] ids = checkIds.split(",");
			Integer[] intids = new Integer[ids.length];
			for (int i = 0; i < ids.length; i++) {
				intids[i] = Integer.valueOf(ids[i]);
			}
			cq.in("id", intids);
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyCheckApplyService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	

	/**
	 * 删除享宇考勤申请表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		sXyCheckApply = systemService.getEntity(SXyCheckApplyEntity.class, sXyCheckApply.getId());
		message = "享宇考勤申请表删除成功";
		try{
			//修改考勤异常表状态
			for (String id : sXyCheckApply.getCheckIds().split(",")) {
				SXyCheckinoutEntity sXyCheckinout = sXyCheckApplyService.getEntity(SXyCheckinoutEntity.class, Integer.valueOf(id));
				sXyCheckinout.setFlowState(null);
				sXyCheckApplyService.saveOrUpdate(sXyCheckinout);
			}
			sXyCheckApplyService.delete(sXyCheckApply);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇考勤申请表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 添加享宇考勤申请表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇考勤申请表添加成功";
		try{
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户所属的部门
			TSDepart tsDepart = sXyCheckApplyService.findHqlTSDepart(tsUser);
			//设置申请人
			sXyCheckApply.setTsUser(tsUser);
			//设置申请人编号
			sXyCheckApply.setApplySttaffId(Integer.valueOf(tsUser.getUserName()));
			//设置部门
			sXyCheckApply.setTsDept(tsDepart);
			//设置创建时间
			sXyCheckApply.setCreateTime(new Date());
			//设置创建人
			sXyCheckApply.setCUser(Integer.valueOf(tsUser.getUserName()));
			/**
			 * 流程状态：0：流程未启动、1：异常待审批、2：异常审批中、3：异常申请被否决、
			 * 4：已完成、5：撤销申请、6：取消申请
			 */
			sXyCheckApply.setFlowState("0");
			//修改考勤异常表状态
			for (String id : sXyCheckApply.getCheckIds().split(",")) {
				SXyCheckinoutEntity sXyCheckinout = sXyCheckApplyService.getEntity(SXyCheckinoutEntity.class, Integer.valueOf(id));
				sXyCheckinout.setFlowState("1");
				sXyCheckApplyService.saveOrUpdate(sXyCheckinout);
			}
			sXyCheckApplyService.save(sXyCheckApply);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇考勤申请表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新享宇考勤申请表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇考勤申请表更新成功";
		SXyCheckApplyEntity t = sXyCheckApplyService.get(SXyCheckApplyEntity.class, sXyCheckApply.getId());
		try {
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//设置更新时间
			sXyCheckApply.setUTime(new Date());
			//设置更新人
			sXyCheckApply.setUUser(Integer.valueOf(tsUser.getUserName()));
			//修改考勤异常表状态
			for (String id : t.getCheckIds().split(",")) {
				SXyCheckinoutEntity sXyCheckinout = sXyCheckApplyService.getEntity(SXyCheckinoutEntity.class, Integer.valueOf(id));
				sXyCheckinout.setFlowState(null);
				sXyCheckApplyService.saveOrUpdate(sXyCheckinout);
			}
			for (String id : sXyCheckApply.getCheckIds().split(",")) {
				SXyCheckinoutEntity sXyCheckinout = sXyCheckApplyService.getEntity(SXyCheckinoutEntity.class, Integer.valueOf(id));
				sXyCheckinout.setFlowState("1");
				sXyCheckApplyService.saveOrUpdate(sXyCheckinout);
			}
			MyBeanUtils.copyBeanNotNull2Bean(sXyCheckApply, t);
			sXyCheckApplyService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇考勤申请表更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 提交考勤异常申请
	 * @param sXyCheckApply
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doSubmitCheck")
	@ResponseBody
	public AjaxJson doSubmitCheck(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "考勤异常申请提交成功";
		//审批人（用户编号(userName)）
		String feaaApprover = request.getParameter("feaaApprover");
		//考勤异常申请信息
		sXyCheckApply = sXyCheckApplyService.getEntity(SXyCheckApplyEntity.class, sXyCheckApply.getId());
		//当前登录的用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户的角色编码
		List<String> roleCodes = sXyCheckApplyService.findHqlRoleCodes(tsUser);
		String roleCode = roleCodes.get(0);
		//查询当前用户所属的部门
		TSDepart tsDepart = sXyCheckApplyService.findHqlTSDepart(tsUser);
		//机构编码长度
		int orgCodeLength = tsDepart.getOrgCode().length();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		//传入加班业务表的ID（显示在考勤记录表中、归档、可调休时间统计）
		variables.put("checkId", sXyCheckApply.getId());
		//判断流程走向
		if ((Constants.EMPLOYEE.equals(roleCode) || Constants.OUTEM.equals(roleCode)) && orgCodeLength == 12) {
			variables.put("startLine", 1);
			variables.put(Constants.HEADMAN, feaaApprover);
		}
		if ((Constants.EMPLOYEE.equals(roleCode) || Constants.OUTEM.equals(roleCode) || Constants.HR.equals(roleCode)) && orgCodeLength == 9) {
			//查询当前部门下的直接上级
			List<TSUser> tsUsers = sXyCheckApplyService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
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
			 * 流程状态：0：流程未启动、1：异常待审批、2：异常审批中、3：异常申请被否决、
			 * 4：已完成、5：撤销申请、6：取消申请
			 */
			sXyCheckApply.setFlowState("1");
			//设置申请日期
			sXyCheckApply.setApplyDate(new Date());
			//设置申请编号
			sXyCheckApply.setApplyNo(sXyCheckApplyService.getNextApplyNo(ApplyTypeEnum.KQ, "s_xy_check_apply"));
			//启动流程（以用户编号(userName)作为启动人）
			String flowInstId = sXyCheckApplyService.startFlow("xyCheckApply", sXyCheckApply.getId(), variables, tsUser.getUserName());
			//设置流程实例ID
			sXyCheckApply.setFlowInstId(flowInstId);
			//修改考勤异常表状态
			for (String id : sXyCheckApply.getCheckIds().split(",")) {
				SXyCheckinoutEntity sXyCheckinout = sXyCheckApplyService.getEntity(SXyCheckinoutEntity.class, Integer.valueOf(id));
				sXyCheckinout.setFlowState("2");
				sXyCheckinout.setFlowInstId(flowInstId);
				sXyCheckApplyService.saveOrUpdate(sXyCheckinout);
			}
			//保存
			sXyCheckApplyService.saveOrUpdate(sXyCheckApply);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "考勤异常申请提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 批准考勤异常申请
	 * @param sXyCheckApply
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAgreeCheck")
	@ResponseBody
	public AjaxJson doAgreeCheck(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "考勤异常申请批准成功";
		//审批人（用户编号(userName)）
		String nextApprover = request.getParameter("nextApprover");
		//批准原因
		String passReason = request.getParameter("passReason");
		//考勤异常申请信息
		sXyCheckApply = sXyCheckApplyService.getEntity(SXyCheckApplyEntity.class, sXyCheckApply.getId());
		/**
		 * 流程状态：0：流程未启动、1：异常待审批、2：异常审批中、3：异常申请被否决、
		 * 4：已完成、5：撤销申请、6：取消申请
		 */
		sXyCheckApply.setFlowState("2");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户的角色编码
		List<String> roleCodes = sXyCheckApplyService.findHqlRoleCodes(tsUser);
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
			sXyCheckApplyService.completeTask(sXyCheckApply.getFlowInstId(), variables, tsUser.getUserName(), passReason);
			//保存状态
			sXyCheckApplyService.saveOrUpdate(sXyCheckApply);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);	
		} catch (Exception e) {
			e.printStackTrace();
			message = "考勤异常申请批准失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 否决考勤异常申请
	 * @param sXyCheckApply
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doNotAgreeCheck")
	@ResponseBody
	public AjaxJson doNotAgreeCheck(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "考勤异常申请否决成功";
		//考勤异常申请信息
		sXyCheckApply = sXyCheckApplyService.getEntity(SXyCheckApplyEntity.class, sXyCheckApply.getId());
		/**
		 * 流程状态：0：流程未启动、1：异常待审批、2：异常审批中、3：异常申请被否决、
		 * 4：已完成、5：撤销申请、6：取消申请
		 */
		sXyCheckApply.setFlowState("3");
		//否决原因
		String nopassReason = request.getParameter("nopassReason");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("isPass", 0);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyCheckApplyService.completeTask(sXyCheckApply.getFlowInstId(), variables, tsUser.getUserName(), nopassReason);
			//保存状态
			sXyCheckApplyService.saveOrUpdate(sXyCheckApply);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "考勤异常申请否决失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	/**
	 * HR 批准考勤异常申请
	 * @param sXyCheckApply
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doHrAgreeCheck")
	@ResponseBody
	public AjaxJson doHrAgreeCheck(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "考勤异常申请批准成功";
		//考勤异常申请信息
		sXyCheckApply = sXyCheckApplyService.getEntity(SXyCheckApplyEntity.class, sXyCheckApply.getId());
		//批准原因
		String hrpassReason = request.getParameter("hrpassReason");
		/**
		 * 流程状态：0：流程未启动、1：异常待审批、2：异常审批中、3：异常申请被否决、
		 * 4：已完成、5：撤销申请、6：取消申请
		 */
		sXyCheckApply.setFlowState("4");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("isPass", 3);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyCheckApplyService.completeTask(sXyCheckApply.getFlowInstId(), variables, tsUser.getUserName(), hrpassReason);
			//修改考勤异常表状态
			for (String id : sXyCheckApply.getCheckIds().split(",")) {
				SXyCheckinoutEntity sXyCheckinout = sXyCheckApplyService.getEntity(SXyCheckinoutEntity.class, Integer.valueOf(id));
				sXyCheckinout.setFlowState("3");
				sXyCheckApplyService.saveOrUpdate(sXyCheckinout);
			}
			//保存状态
			sXyCheckApplyService.saveOrUpdate(sXyCheckApply);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "考勤异常申请批准失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	/**
	 * HR 否决考勤异常申请
	 * @param sXyCheckApply
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doHrNotAgreeCheck")
	@ResponseBody
	public AjaxJson doHrNotAgreeCheck(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "考勤异常申请否决成功";
		//考勤异常信息
		sXyCheckApply = sXyCheckApplyService.getEntity(SXyCheckApplyEntity.class, sXyCheckApply.getId());
		//否决原因
		String hrnopassReason = request.getParameter("hrnopassReason");
		/**
		 * 流程状态：0：流程未启动、1：异常待审批、2：异常审批中、3：异常申请被否决、
		 * 4：已完成、5：撤销申请、6：取消申请
		 */
		sXyCheckApply.setFlowState("3");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("isPass", 0);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyCheckApplyService.completeTask(sXyCheckApply.getFlowInstId(), variables, tsUser.getUserName(), hrnopassReason);
			//保存状态
			sXyCheckApplyService.saveOrUpdate(sXyCheckApply);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "考勤异常申请否决失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 *  查看流程图片
	 * @param sXyCheckApply
	 * @param response
	 */
	@RequestMapping(params = "lookFlowImage")
	public void lookFlowImage(SXyCheckApplyEntity sXyCheckApply, HttpServletResponse response) {
		try {
			response.setContentType("image/png");
			InputStream inputStream = sXyCheckApplyService.graphics(sXyCheckApply.getFlowInstId());
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
	 * 重新提交考勤异常申请
	 * @param sXyCheckApply
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "reSubmitCheck")
	@ResponseBody
	public AjaxJson reSubmitCheck(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "考勤异常申请重新提交成功";
		//考勤异常申请信息
		sXyCheckApply = sXyCheckApplyService.getEntity(SXyCheckApplyEntity.class, sXyCheckApply.getId());
		/**
		 * 流程状态：0：流程未启动、1：异常待审批、2：异常审批中、3：异常申请被否决、
		 * 4：已完成、5：撤销申请、6：取消申请
		 */
		sXyCheckApply.setFlowState("1");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("reApply", true);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyCheckApplyService.completeTask(sXyCheckApply.getFlowInstId(), variables, tsUser.getUserName(), null);
			//保存状态
			sXyCheckApplyService.saveOrUpdate(sXyCheckApply);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);	
		} catch (Exception e) {
			e.printStackTrace();
			message = "考勤异常申请重新提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 撤销考勤异常申请
	 * @param sXyCheckApply
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delCheck")
	@ResponseBody
	public AjaxJson delCheck(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "考勤异常申请撤销成功";
		//考勤异常申请信息
		sXyCheckApply = sXyCheckApplyService.getEntity(SXyCheckApplyEntity.class, sXyCheckApply.getId());
		/**
		 * 流程状态：0：流程未启动、1：异常待审批、2：异常审批中、3：异常申请被否决、
		 * 4：已完成、5：撤销申请、6：取消申请
		 */
		sXyCheckApply.setFlowState("5");
		try {
			//撤销申请
			sXyCheckApplyService.delFlow(sXyCheckApply.getFlowInstId());
			//修改考勤异常表状态
			for (String id : sXyCheckApply.getCheckIds().split(",")) {
				SXyCheckinoutEntity sXyCheckinout = sXyCheckApplyService.getEntity(SXyCheckinoutEntity.class, Integer.valueOf(id));
				sXyCheckinout.setFlowState("4");
				sXyCheckApplyService.saveOrUpdate(sXyCheckinout);
			}
			//保存状态
			sXyCheckApplyService.saveOrUpdate(sXyCheckApply);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);	
		} catch (Exception e) {
			e.printStackTrace();
			message = "考勤异常申请撤销失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 取消考勤异常申请
	 * @param sXyCheckApply
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "cancelCheck")
	@ResponseBody
	public AjaxJson cancelCheck(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "考勤异常申请取消成功";
		//考勤异常申请信息
		sXyCheckApply = sXyCheckApplyService.getEntity(SXyCheckApplyEntity.class, sXyCheckApply.getId());
		/**
		 * 流程状态：0：流程未启动、1：异常待审批、2：异常审批中、3：异常申请被否决、
		 * 4：已完成、5：撤销申请、6：取消申请
		 */
		sXyCheckApply.setFlowState("6");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("reApply", false);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyCheckApplyService.completeTask(sXyCheckApply.getFlowInstId(), variables, tsUser.getUserName(), null);
			//修改考勤异常表状态
			for (String id : sXyCheckApply.getCheckIds().split(",")) {
				SXyCheckinoutEntity sXyCheckinout = sXyCheckApplyService.getEntity(SXyCheckinoutEntity.class, Integer.valueOf(id));
				sXyCheckinout.setFlowState("5");
				sXyCheckApplyService.saveOrUpdate(sXyCheckinout);
			}
			//保存状态
			sXyCheckApplyService.saveOrUpdate(sXyCheckApply);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);	
		} catch (Exception e) {
			e.printStackTrace();
			message = "考勤异常申请取消失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 享宇考勤申请表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(HttpServletRequest req) {
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户所属的部门
		TSDepart tsDepart = sXyCheckApplyService.findHqlTSDepart(tsUser);
		req.setAttribute("tsUserPage", tsUser);
		req.setAttribute("tsDepartPage", tsDepart);
		return new ModelAndView("xyoa/checkapply/sXyCheckApply-add");
	}
	/**
	 * 享宇考勤申请表 编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCheckApply.getId())) {
			sXyCheckApply = sXyCheckApplyService.getEntity(SXyCheckApplyEntity.class, sXyCheckApply.getId());
			req.setAttribute("sXyCheckApplyPage", sXyCheckApply);
		}
		return new ModelAndView("xyoa/checkapply/sXyCheckApply-update");
	}
	/**
	 * 享宇考勤申请表 详情页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goDetail")
	public ModelAndView goDetail(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCheckApply.getId())) {
			try {
				//考勤异常申请信息
				sXyCheckApply = sXyCheckApplyService.getEntity(SXyCheckApplyEntity.class, sXyCheckApply.getId());
				//考勤异常申请审批 历史节点信息
				List<CommentBean> commentBeans = sXyCheckApplyService.findComment(sXyCheckApply.getFlowInstId());
				req.setAttribute("sXyCheckApplyPage", sXyCheckApply);
				req.setAttribute("commentBeansPage", commentBeans);
			} catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}
		}
		return new ModelAndView("xyoa/checkapply/sXyCheckApply-detail");
	}
	
	
	/**
	 * 提交考勤异常申请 界面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goSubmitCheck")
	public ModelAndView goSubmitCheck(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCheckApply.getId())) {
			//考勤异常申请信息
			sXyCheckApply = sXyCheckApplyService.getEntity(SXyCheckApplyEntity.class, sXyCheckApply.getId());
			//当前登录的用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyCheckApplyService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			//查询当前用户所属的部门
			TSDepart tsDepart = sXyCheckApplyService.findHqlTSDepart(tsUser);
			//机构编码长度
			int orgCodeLength = tsDepart.getOrgCode().length();
			//查询该部门下的领导信息
			List<TSUser> tsUsers = null;
			if ((Constants.EMPLOYEE.equals(roleCode) || Constants.OUTEM.equals(roleCode)) && orgCodeLength == 12) {
				tsUsers = sXyCheckApplyService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的直接上级
					TSDepart tsPDepart = sXyCheckApplyService.findHqlTSPDepart(tsUser);
					tsUsers = sXyCheckApplyService.findHqlTSUsersByRD(Constants.HEADMAN, tsPDepart);
				}
			}
			if ((Constants.EMPLOYEE.equals(roleCode) || Constants.OUTEM.equals(roleCode) || Constants.HR.equals(roleCode)) && orgCodeLength == 9) {
				//查询当前部门下的直接上级
				tsUsers = sXyCheckApplyService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前部门下的部门DM
					tsUsers = sXyCheckApplyService.findHqlTSUsersByRD(Constants.DM, tsDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的部门DM
						TSDepart tsPDepart = sXyCheckApplyService.findHqlTSPDepart(tsUser);
						tsUsers = sXyCheckApplyService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					}
				}
			}
			if (Constants.HEADMAN.equals(roleCode)) {
				tsUsers = sXyCheckApplyService.findHqlTSUsersByRD(Constants.DM, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的部门DM
					TSDepart tsPDepart = sXyCheckApplyService.findHqlTSPDepart(tsUser);
					tsUsers = sXyCheckApplyService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的上一级部门的部门DM
						TSDepart tsPPDepart = sXyCheckApplyService.findHqlTSPPDepart(tsUser);
						tsUsers = sXyCheckApplyService.findHqlTSUsersByRD(Constants.DM, tsPPDepart);
					}
				}
			}
			if(Constants.DM.equals(roleCode) || Constants.HRDM.equals(roleCode)) {
				tsUsers = sXyCheckApplyService.findHqlTSUsersByRoleCode(Constants.VICE);
			}
			if(Constants.VICE.equals(roleCode)) {
				tsUsers = sXyCheckApplyService.findHqlTSUsersByRoleCode(Constants.CEO);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyCheckApplyPage", sXyCheckApply);
		}
		return new ModelAndView("xyoa/checkapply/sXyCheckApply-submit");
	}
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 批准考勤异常申请 页面跳转
	 * @param sXyCheckApply
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goAgreeCheck")
	public ModelAndView goAgreeCheck(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCheckApply.getId())) {
			//考勤异常申请信息
			sXyCheckApply = sXyCheckApplyService.getEntity(SXyCheckApplyEntity.class, sXyCheckApply.getId());
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//查询当前用户的角色编码
			List<String> roleCodes = sXyCheckApplyService.findHqlRoleCodes(tsUser);
			String roleCode = roleCodes.get(0);
			//查询当前用户的下一级审批人
			List<TSUser> tsUsers = null;
			if(Constants.HEADMAN.equals(roleCode)) {
				//查询当前用户所属的部门
				TSDepart tsDepart = sXyCheckApplyService.findHqlTSDepart(tsUser);
				tsUsers = sXyCheckApplyService.findHqlTSUsersByRD(Constants.DM, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的部门DM
					TSDepart tsPDepart = sXyCheckApplyService.findHqlTSPDepart(tsUser);
					tsUsers = sXyCheckApplyService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的上一级部门的部门DM
						TSDepart tsPPDepart = sXyCheckApplyService.findHqlTSPPDepart(tsUser);
						tsUsers = sXyCheckApplyService.findHqlTSUsersByRD(Constants.DM, tsPPDepart);
					}
				}
			} else {
				//查询HRDM
				tsUsers = sXyCheckApplyService.findHqlTSUsersByRoleCode(Constants.HRDM);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyCheckApplyPage", sXyCheckApply);
		}
		return new ModelAndView("xyoa/checkapply/sXyCheckApply-check");
	}
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 否决考勤异常申请 页面跳转
	 * @param sXyCheckApply
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goNotAgreeCheck")
	public ModelAndView goNotAgreeCheck(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCheckApply.getId())) {
			sXyCheckApply = sXyCheckApplyService.getEntity(SXyCheckApplyEntity.class, sXyCheckApply.getId());
			req.setAttribute("sXyCheckApplyPage", sXyCheckApply);
		}
		return new ModelAndView("xyoa/checkapply/sXyCheckApply-nocheck");
	}
	/**
	 * HR 批准考勤异常申请 页面跳转
	 * @param sXyCheckApply
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goHrAgreeCheck")
	public ModelAndView goHrAgreeCheck(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCheckApply.getId())) {
			sXyCheckApply = sXyCheckApplyService.getEntity(SXyCheckApplyEntity.class, sXyCheckApply.getId());
			req.setAttribute("sXyCheckApplyPage", sXyCheckApply);
		}
		return new ModelAndView("xyoa/checkapply/sXyCheckApply-hrcheck");
	}
	/**
	 * HR 否决考勤异常申请 页面跳转
	 * @param sXyCheckApply
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goHrNotAgreeCheck")
	public ModelAndView goHrNotAgreeCheck(SXyCheckApplyEntity sXyCheckApply, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCheckApply.getId())) {
			sXyCheckApply = sXyCheckApplyService.getEntity(SXyCheckApplyEntity.class, sXyCheckApply.getId());
			req.setAttribute("sXyCheckApplyPage", sXyCheckApply);
		}
		return new ModelAndView("xyoa/checkapply/sXyCheckApply-hrnocheck");
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<SXyCheckApplyEntity> list() {
		List<SXyCheckApplyEntity> listSXyCheckApplys=sXyCheckApplyService.getList(SXyCheckApplyEntity.class);
		return listSXyCheckApplys;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		SXyCheckApplyEntity task = sXyCheckApplyService.get(SXyCheckApplyEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody SXyCheckApplyEntity sXyCheckApply, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyCheckApplyEntity>> failures = validator.validate(sXyCheckApply);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyCheckApplyService.save(sXyCheckApply);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = sXyCheckApply.getId();
		URI uri = uriBuilder.path("/rest/sXyCheckApplyController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody SXyCheckApplyEntity sXyCheckApply) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyCheckApplyEntity>> failures = validator.validate(sXyCheckApply);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyCheckApplyService.saveOrUpdate(sXyCheckApply);
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
		sXyCheckApplyService.deleteEntityById(SXyCheckApplyEntity.class, id);
	}
}
