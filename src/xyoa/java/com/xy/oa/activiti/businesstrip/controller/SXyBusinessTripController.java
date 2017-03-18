package com.xy.oa.activiti.businesstrip.controller;

import java.io.IOException;
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
import com.xy.oa.calendars.service.SXyCalendarsServiceI;
import com.xy.oa.checkinout.service.SXyCheckinoutServiceI;
import com.xy.oa.util.ApplyTypeEnum;
import com.xy.oa.util.Constants;
/**   
 * @Title: Controller  
 * @Description: 享宇出差表
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
	@Autowired
	private SXyCalendarsServiceI sXyCalendarsService;
	@Autowired
	private SXyCheckinoutServiceI sXyCheckinoutService;


	/**
	 * 显示在考勤记录表中，为出差中
	 * 归档、显示在考勤表中
	 * @param tripId：出差申请表ID
	 * @param flag：1：显示在考勤记录表中，为出差中，2：归档、显示在考勤表中
	 */
	public void doSome(String tripId, int flag) {
		SXyBusinessTripEntity sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, tripId);
		if (flag == 1) {
			//显示在考勤记录表中，为出差中
			
		} else if (flag == 2) {
			//归档
			try {
				sXyCheckinoutService.disCheckInOut(sXyBusinessTrip.getApplySttaffId(), DateUtils.formatDate(sXyBusinessTrip.getStartTime()),
						DateUtils.formatDate(sXyBusinessTrip.getEndTime()), sXyBusinessTrip.getId(), Constants.XY_CHECK_TYPE_17, sXyBusinessTrip.getRemarks());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 享宇出差表列表 页面跳转
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
			//查询当前用户的角色编码
			String rHql = "select ru.TSRole.roleCode from TSRoleUser ru where ru.TSUser = ?";
			List<String> roleCodes = sXyBusinessTripService.findHql(rHql, tsUser);
			String roleCode = null;
			if(roleCodes.size() == 1) {
				roleCode = roleCodes.get(0);
			} else {
				roleCode = roleCodes.get(1);
			}
			
			//部门查询
			String orgId = request.getParameter("orgId");
			if (StringUtil.isNotEmpty(orgId)) {
//				TSDepart tsDepart = sXyBusinessTripService.getEntity(TSDepart.class, orgId);
//				cq.eq("tsDept", tsDepart);
				TSDepart tsDepart = sXyBusinessTripService.getEntity(TSDepart.class, orgId);
				List<TSDepart> tSDepartList = tsDepart.getTSDeparts();
				tSDepartList.add(0, tsDepart);
				cq.in("tsDept", tSDepartList.toArray());
			}
			
			if(Constants.DM.equals(roleCode)) {
				//查询该部门下的所有员工以及各种组的所有员工
				List<TSUser> tsUsers = sXyBusinessTripService.findHqlTSUsersByUser(tsUser);
				if (tsUsers == null || tsUsers.isEmpty()) {
					cq.eq("tsUser", tsUser);
				} else {
					cq.or(cq.and(Restrictions.in("tsUser", tsUsers.toArray()), Restrictions.eq("flowState", "7")), Restrictions.eq("tsUser", tsUser));
				}
			} else if (Constants.HR.equals(roleCode) || Constants.HRDM.equals(roleCode) || Constants.VICE.equals(roleCode) || Constants.CEO.equals(roleCode)) {
				//查询普通员工、直接上级、人事专员、部门DM、人事DM和副总裁
				List<TSUser> tsUsers = sXyBusinessTripService.findHqlTSUsersByUR(tsUser);
				cq.or(cq.and(Restrictions.in("tsUser", tsUsers.toArray()), Restrictions.eq("flowState", "7")), Restrictions.eq("tsUser", tsUser));
			} else {
				//查询当前用户提交的出差申请
				cq.eq("tsUser", tsUser);
			}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyBusinessTripService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁审批页面
	 * 享宇审核出差表单 页面跳转
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
	public void datagridCheck(SXyBusinessTripEntity sXyBusinessTrip,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String spflag) {
		CriteriaQuery cq = new CriteriaQuery(SXyBusinessTripEntity.class, dataGrid);
		//查询条件组装器
		String paramHql = org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHqlForFlow("S", cq, sXyBusinessTrip, request.getParameterMap());
		try{
			//自定义追加查询条件
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//部门查询
			String orgId = request.getParameter("orgId");
			
			List<BusinessTripPo> lPos = new ArrayList<BusinessTripPo>();
			int total = 0;
			int beginNum = (cq.getCurPage()-1)*cq.getPageSize();
			if ("00".equals(spflag) || "01".equals(spflag)) {
				if ("00".equals(spflag)) {
					//查询当前登录用户的任务（以用户编号(userName)查询）
					sXyBusinessTripService.getDataGridRunForHQL(SXyBusinessTripEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyBusinessTripService.getRunTotal(SXyBusinessTripEntity.class, false, paramHql, orgId, tsUser.getUserName());
				} else if ("01".equals(spflag)) {
					//查看当前用户的历史任务（已审批过的）
					sXyBusinessTripService.getDataGridHisForHQL(SXyBusinessTripEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyBusinessTripService.getHisTotal(SXyBusinessTripEntity.class, false, paramHql, orgId, tsUser.getUserName());
				}
				List<SXyBusinessTripEntity> lTripEntities = dataGrid.getResults();
				for (SXyBusinessTripEntity sXyBusinessTripEntity : lTripEntities) {
					BusinessTripPo bPo = new BusinessTripPo();
					BeanUtils.copyProperties(sXyBusinessTripEntity, bPo);
					bPo.setIsApprove(spflag);
					lPos.add(bPo);
				}
			} else {
				//查询当前登录用户的任务的总条数（以用户编号(userName)查询）
				int totalR = sXyBusinessTripService.getRunTotal(SXyBusinessTripEntity.class, false, paramHql, orgId, tsUser.getUserName());
				//查看当前用户的历史任务的总条数（已审批过的）
				int totalH = sXyBusinessTripService.getHisTotal(SXyBusinessTripEntity.class, false, paramHql, orgId, tsUser.getUserName());
				total = totalR + totalH;
				if (totalR <= beginNum) {
					DataGrid dataGrid2 = new DataGrid();
					beginNum -= totalR;
					//查看当前用户的历史任务（已审批过的）
					sXyBusinessTripService.getDataGridHisForHQL(SXyBusinessTripEntity.class, dataGrid2, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyBusinessTripEntity> lTripEntitiesH = dataGrid2.getResults();
					for (SXyBusinessTripEntity sXyBusinessTripEntity : lTripEntitiesH) {
						BusinessTripPo bPo = new BusinessTripPo();
						BeanUtils.copyProperties(sXyBusinessTripEntity, bPo);
						bPo.setIsApprove("01");
						lPos.add(bPo);
					}
				} else {
					//查询当前登录用户的任务（以用户编号(userName)查询）
					sXyBusinessTripService.getDataGridRunForHQL(SXyBusinessTripEntity.class, dataGrid, false, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyBusinessTripEntity> lTripEntitiesR = dataGrid.getResults();
					for (SXyBusinessTripEntity sXyBusinessTripEntity : lTripEntitiesR) {
						BusinessTripPo bPo = new BusinessTripPo();
						BeanUtils.copyProperties(sXyBusinessTripEntity, bPo);
						bPo.setIsApprove("00");
						lPos.add(bPo);
					}
					if (lTripEntitiesR.size() < cq.getPageSize()) {
						DataGrid dataGrid2 = new DataGrid();
						//查看当前用户的历史任务（已审批过的）
						sXyBusinessTripService.getDataGridHisForHQL(SXyBusinessTripEntity.class, dataGrid2, false, paramHql, orgId, 0, cq.getPageSize()-lTripEntitiesR.size(), tsUser.getUserName());
						List<SXyBusinessTripEntity> lTripEntitiesH = dataGrid2.getResults();
						for (SXyBusinessTripEntity sXyBusinessTripEntity : lTripEntitiesH) {
							BusinessTripPo bPo = new BusinessTripPo();
							BeanUtils.copyProperties(sXyBusinessTripEntity, bPo);
							bPo.setIsApprove("01");
							lPos.add(bPo);
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
	 * 享宇审核出差表单 页面跳转
	 * HR审批页面
	 * @return
	 */
	@RequestMapping(params = "listHr")
	public ModelAndView listHr(HttpServletRequest request) {
		request.setAttribute("replacedepart", sXyBusinessTripService.getTSDepartAllStr());
		return new ModelAndView("xyoa/activiti/businesstrip/sXyBusinessTripListHr");
	}
	/**
	 * Ajax数据请求
	 * @param sXyBusinessTrip
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridHr")
	public void datagridHr(SXyBusinessTripEntity sXyBusinessTrip,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, String spflag) {
		CriteriaQuery cq = new CriteriaQuery(SXyBusinessTripEntity.class, dataGrid);
		//查询条件组装器
		String paramHql = org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHqlForFlow("S", cq, sXyBusinessTrip, request.getParameterMap());
		try{
			//自定义追加查询条件
			//当前登录用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			//部门查询
			String orgId = request.getParameter("orgId");
			
			List<BusinessTripPo> lPos = new ArrayList<BusinessTripPo>();
			int total = 0;
			int beginNum = (cq.getCurPage()-1)*cq.getPageSize();
			if ("00".equals(spflag) || "01".equals(spflag)) {
				if ("00".equals(spflag)) {
					//查询当前登录用户的任务（以用户编号(userName)查询）
					sXyBusinessTripService.getDataGridRunForHQL(SXyBusinessTripEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyBusinessTripService.getRunTotal(SXyBusinessTripEntity.class, true, paramHql, orgId, tsUser.getUserName());
				} else if ("01".equals(spflag)) {
					//查看当前用户的历史任务（已审批过的）
					sXyBusinessTripService.getDataGridHisForHQL(SXyBusinessTripEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					total = sXyBusinessTripService.getHisTotal(SXyBusinessTripEntity.class, true, paramHql, orgId, tsUser.getUserName());
				}
				List<SXyBusinessTripEntity> lTripEntities = dataGrid.getResults();
				for (SXyBusinessTripEntity sXyBusinessTripEntity : lTripEntities) {
					BusinessTripPo bPo = new BusinessTripPo();
					BeanUtils.copyProperties(sXyBusinessTripEntity, bPo);
					bPo.setIsApprove(spflag);
					lPos.add(bPo);
				}
			} else {
				//查询当前登录用户的任务的总条数（以用户编号(userName)查询）
				int totalR = sXyBusinessTripService.getRunTotal(SXyBusinessTripEntity.class, true, paramHql, orgId, tsUser.getUserName());
				//查看当前用户的历史任务的总条数（已审批过的）
				int totalH = sXyBusinessTripService.getHisTotal(SXyBusinessTripEntity.class, true, paramHql, orgId, tsUser.getUserName());
				total = totalR + totalH;
				if (totalR <= beginNum) {
					DataGrid dataGrid2 = new DataGrid();
					beginNum -= totalR;
					//查看当前用户的历史任务（已审批过的）
					sXyBusinessTripService.getDataGridHisForHQL(SXyBusinessTripEntity.class, dataGrid2, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyBusinessTripEntity> lTripEntitiesH = dataGrid2.getResults();
					for (SXyBusinessTripEntity sXyBusinessTripEntity : lTripEntitiesH) {
						BusinessTripPo bPo = new BusinessTripPo();
						BeanUtils.copyProperties(sXyBusinessTripEntity, bPo);
						bPo.setIsApprove("01");
						lPos.add(bPo);
					}
				} else {
					//查询当前登录用户的任务（以用户编号(userName)查询）
					sXyBusinessTripService.getDataGridRunForHQL(SXyBusinessTripEntity.class, dataGrid, true, paramHql, orgId, beginNum, cq.getPageSize(), tsUser.getUserName());
					List<SXyBusinessTripEntity> lTripEntitiesR = dataGrid.getResults();
					for (SXyBusinessTripEntity sXyBusinessTripEntity : lTripEntitiesR) {
						BusinessTripPo bPo = new BusinessTripPo();
						BeanUtils.copyProperties(sXyBusinessTripEntity, bPo);
						bPo.setIsApprove("00");
						lPos.add(bPo);
					}
					if (lTripEntitiesR.size() < cq.getPageSize()) {
						DataGrid dataGrid2 = new DataGrid();
						//查看当前用户的历史任务（已审批过的）
						sXyBusinessTripService.getDataGridHisForHQL(SXyBusinessTripEntity.class, dataGrid2, true, paramHql, orgId, 0, cq.getPageSize()-lTripEntitiesR.size(), tsUser.getUserName());
						List<SXyBusinessTripEntity> lTripEntitiesH = dataGrid2.getResults();
						for (SXyBusinessTripEntity sXyBusinessTripEntity : lTripEntitiesH) {
							BusinessTripPo bPo = new BusinessTripPo();
							BeanUtils.copyProperties(sXyBusinessTripEntity, bPo);
							bPo.setIsApprove("01");
							lPos.add(bPo);
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
	 * 删除享宇出差表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		sXyBusinessTrip = systemService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
		message = "享宇出差表删除成功";
		try{
			sXyBusinessTripService.delete(sXyBusinessTrip);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇出差表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除享宇出差表
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇出差表删除成功";
		try{
			for(String id:ids.split(",")){
				SXyBusinessTripEntity sXyBusinessTrip = systemService.getEntity(SXyBusinessTripEntity.class, id);
				sXyBusinessTripService.delete(sXyBusinessTrip);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇出差表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加享宇出差表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇出差表添加成功";
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
			 * 流程状态：0：流程未启动、1：出差待审批、2：出差审批中、3：出差申请被否决、
			 * 4：出差审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
			 */
			sXyBusinessTrip.setFlowState("0");
			sXyBusinessTripService.save(sXyBusinessTrip);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇出差表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新享宇出差表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇出差表更新成功";
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
			message = "享宇出差表更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 提交享宇出差表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doSubmitTrip")
	@ResponseBody
	public AjaxJson doSubmitTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇出差表提交成功";
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
		if (Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 12) {
			variables.put("startLine", 1);
			variables.put(Constants.HEADMAN, feaaApprover);
		}
		if ((Constants.EMPLOYEE.equals(roleCode) || Constants.HR.equals(roleCode)) && orgCodeLength == 9) {
			//查询当前部门下的直接上级
			List<TSUser> tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
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
			 * 流程状态：0：流程未启动、1：出差待审批、2：出差审批中、3：出差申请被否决、
			 * 4：出差审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
			 */
			sXyBusinessTrip.setFlowState("1");
			//设置提交申请日期
			sXyBusinessTrip.setApplyDate(new Date());
			//设置申请编号
			sXyBusinessTrip.setApplyNo(sXyBusinessTripService.getNextApplyNo(ApplyTypeEnum.CC, "s_xy_business_trip"));
			//启动流程（以用户编号(userName)作为启动人）
			String flowInstId = sXyBusinessTripService.startFlow("xyBusinessTrip", sXyBusinessTrip.getId(), variables, tsUser.getUserName());
			//设置流程实例ID
			sXyBusinessTrip.setFlowInstId(flowInstId);
			//保存
			sXyBusinessTripService.saveOrUpdate(sXyBusinessTrip);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇出差表提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：批准享宇出差申请
	 * @param sXyBusinessTrip
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doAgreeTrip")
	@ResponseBody
	public AjaxJson doAgreeTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇出差表批准成功";
		//审批人（用户编号(userName)）
		String nextApprover = request.getParameter("nextApprover");
		//批准原因
		String passReason = request.getParameter("passReason");
		//获取出差表信息
		sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
		/**
		 * 流程状态：0：流程未启动、1：出差待审批、2：出差审批中、3：出差申请被否决、
		 * 4：出差审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyBusinessTrip.setFlowState("2");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户的角色编码
		List<String> roleCodes = sXyBusinessTripService.findHqlRoleCodes(tsUser);
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
			sXyBusinessTripService.completeTask(sXyBusinessTrip.getFlowInstId(), variables, tsUser.getUserName(), passReason);
			//保证状态
			sXyBusinessTripService.saveOrUpdate(sXyBusinessTrip);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇出差表批准失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：否决享宇出差申请
	 * @param sXyBusinessTrip
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doNotAgreeTrip")
	@ResponseBody
	public AjaxJson doNotAgreeTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇出差表否决成功";
		//否决原因
		String nopassReason = request.getParameter("nopassReason");
		//获取出差表信息
		sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
		/**
		 * 流程状态：0：流程未启动、1：出差待审批、2：出差审批中、3：出差申请被否决、
		 * 4：出差审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyBusinessTrip.setFlowState("3");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("isPass", 0);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyBusinessTripService.completeTask(sXyBusinessTrip.getFlowInstId(), variables, tsUser.getUserName(), nopassReason);
			//保存状态
			sXyBusinessTripService.saveOrUpdate(sXyBusinessTrip);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇出差表否决失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	
	/**
	 * HR：批准享宇出差申请
	 * @param sXyBusinessTrip
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doHrAgreeTrip")
	@ResponseBody
	public AjaxJson doHrAgreeTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇出差表批准成功";
		//批准原因
		String hrpassReason = request.getParameter("hrpassReason");
		//获取出差表信息
		sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
		/**
		 * 流程状态：0：流程未启动、1：出差待审批、2：出差审批中、3：出差申请被否决、
		 * 4：出差审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		if("2".equals(sXyBusinessTrip.getFlowState())) {
			sXyBusinessTrip.setFlowState("4");
			variables.put("isPass", 3);
		}
		if("5".equals(sXyBusinessTrip.getFlowState())) {
			sXyBusinessTrip.setFlowState("7");
			variables.put("isPass", 6);
		}
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyBusinessTripService.completeTask(sXyBusinessTrip.getFlowInstId(), variables, tsUser.getUserName(), hrpassReason);
			//保存状态
			sXyBusinessTripService.saveOrUpdate(sXyBusinessTrip);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇出差表批准失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * HR：否决享宇出差申请
	 * @param sXyBusinessTrip
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doHrNotAgreeTrip")
	@ResponseBody
	public AjaxJson doHrNotAgreeTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇出差表否决成功";
		//否决原因
		String hrnopassReason = request.getParameter("hrnopassReason");
		//获取出差表信息
		sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
		/**
		 * 流程状态：0：流程未启动、1：出差待审批、2：出差审批中、3：出差申请被否决、
		 * 4：出差审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		if("2".equals(sXyBusinessTrip.getFlowState())) {
			sXyBusinessTrip.setFlowState("3");
			variables.put("isPass", 0);
		}
		if("5".equals(sXyBusinessTrip.getFlowState())) {
			sXyBusinessTrip.setFlowState("6");
			variables.put("isPass", 7);
		}
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyBusinessTripService.completeTask(sXyBusinessTrip.getFlowInstId(), variables, tsUser.getUserName(), hrnopassReason);
			//保存状态
			sXyBusinessTripService.saveOrUpdate(sXyBusinessTrip);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇出差表否决失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 提交销假申请
	 * @param sXyBusinessTrip
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "dobackTrip")
	@ResponseBody
	public AjaxJson dobackTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "销假申请提交成功";
		//获取出差表信息
		SXyBusinessTripEntity t = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
		/**
		 * 流程状态：0：流程未启动、1：出差待审批、2：出差审批中、3：出差申请被否决、
		 * 4：出差审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyBusinessTrip.setFlowState("5");
		//设置出差结束时间
		sXyBusinessTrip.setEndTime(sXyBusinessTrip.getBackDate());
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		try {
			//真实出差时间
			BigDecimal bigDecimal = sXyCalendarsService.getTimeInterval(t.getStartTime(), sXyBusinessTrip.getEndTime(), true);
			sXyBusinessTrip.setTripHour(bigDecimal);
			MyBeanUtils.copyBeanNotNull2Bean(sXyBusinessTrip, t);
			//完成任务（以用户编号(userName)完成任务）
			sXyBusinessTripService.completeTask(t.getFlowInstId(), variables, tsUser.getUserName(), null);
			//保存状态
			sXyBusinessTripService.saveOrUpdate(t);
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
	 * 重新提交出差申请
	 * @param sXyBusinessTrip
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "reSubmitTrip")
	@ResponseBody
	public AjaxJson reSubmitTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇出差表重新提交成功";
		//获取出差表信息
		sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
		/**
		 * 流程状态：0：流程未启动、1：出差待审批、2：出差审批中、3：出差申请被否决、
		 * 4：出差审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyBusinessTrip.setFlowState("1");
		//重新设置申请时间
		sXyBusinessTrip.setApplyDate(new Date());
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("reApply", true);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyBusinessTripService.completeTask(sXyBusinessTrip.getFlowInstId(), variables, tsUser.getUserName(), null);
			//保存状态
			sXyBusinessTripService.saveOrUpdate(sXyBusinessTrip);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇出差表重新提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 撤销出差申请
	 * @param sXyBusinessTrip
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delTrip")
	@ResponseBody
	public AjaxJson delTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇出差申请撤销成功";
		//获取出差表信息
		sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
		/**
		 * 流程状态：0：流程未启动、1：出差待审批、2：出差审批中、3：出差申请被否决、
		 * 4：出差审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyBusinessTrip.setFlowState("8");
		try {
			//撤销出差申请
			sXyBusinessTripService.delFlow(sXyBusinessTrip.getFlowInstId());
			//保存状态
			sXyBusinessTripService.saveOrUpdate(sXyBusinessTrip);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇出差申请撤销失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
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
		message = "享宇出差表取消提交成功";
		//获取出差表信息
		sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
		/**
		 * 流程状态：0：流程未启动、1：出差待审批、2：出差审批中、3：出差申请被否决、
		 * 4：出差审批通过、5：销假审批中、6：销假申请被否决、7：已完成、8：撤销申请、9：取消申请
		 */
		sXyBusinessTrip.setFlowState("9");
		//当前登录用户
		TSUser tsUser = ResourceUtil.getSessionUserName();
		Map<String, Object> variables = new HashMap<String, Object>();
		//向流程中传入数据
		variables.put("reApply", false);
		try {
			//完成任务（以用户编号(userName)完成任务）
			sXyBusinessTripService.completeTask(sXyBusinessTrip.getFlowInstId(), variables, tsUser.getUserName(), null);
			//保存状态
			sXyBusinessTripService.saveOrUpdate(sXyBusinessTrip);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇出差表取消提交失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	
	/**
	 * 享宇出差表新增页面跳转
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
	 * 享宇出差表编辑页面跳转
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
	 * 享宇出差表详情页面 跳转
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
	 * 享宇出差表提交申请 页面跳转
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
			if (Constants.EMPLOYEE.equals(roleCode) && orgCodeLength == 12) {
				tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的直接上级
					TSDepart tsPDepart = sXyBusinessTripService.findHqlTSPDepart(tsUser);
					tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(Constants.HEADMAN, tsPDepart);
				}
			}
			if ((Constants.EMPLOYEE.equals(roleCode) || Constants.HR.equals(roleCode)) && orgCodeLength == 9) {
				//查询当前部门下的直接上级
				tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(Constants.HEADMAN, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前部门下的部门DM
					tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(Constants.DM, tsDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的部门DM
						TSDepart tsPDepart = sXyBusinessTripService.findHqlTSPDepart(tsUser);
						tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					}
				}
			}
			if (Constants.HEADMAN.equals(roleCode)) {
				tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(Constants.DM, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的部门DM
					TSDepart tsPDepart = sXyBusinessTripService.findHqlTSPDepart(tsUser);
					tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的上一级部门的部门DM
						TSDepart tsPPDepart = sXyBusinessTripService.findHqlTSPPDepart(tsUser);
						tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(Constants.DM, tsPPDepart);
					}
				}
			}
			if(Constants.DM.equals(roleCode) || Constants.HRDM.equals(roleCode)) {
				tsUsers = sXyBusinessTripService.findHqlTSUsersByRoleCode(Constants.VICE);
			}
			if(Constants.VICE.equals(roleCode)) {
				tsUsers = sXyBusinessTripService.findHqlTSUsersByRoleCode(Constants.CEO);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyBusinessTripPage", sXyBusinessTrip);
		}
		return new ModelAndView("xyoa/activiti/businesstrip/sXyBusinessTrip-submit");
	}
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 享宇出差表批准出差申请页面跳转
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
			if(Constants.HEADMAN.equals(roleCode)) {
				//查询当前用户所属的部门
				TSDepart tsDepart = sXyBusinessTripService.findHqlTSDepart(tsUser);
				tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(Constants.DM, tsDepart);
				if (tsUsers == null || tsUsers.isEmpty()) {
					//查询当前用户所属部门的上一级部门的部门DM
					TSDepart tsPDepart = sXyBusinessTripService.findHqlTSPDepart(tsUser);
					tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(Constants.DM, tsPDepart);
					if (tsUsers == null || tsUsers.isEmpty()) {
						//查询当前用户所属部门的上一级部门的上一级部门的部门DM
						TSDepart tsPPDepart = sXyBusinessTripService.findHqlTSPPDepart(tsUser);
						tsUsers = sXyBusinessTripService.findHqlTSUsersByRD(Constants.DM, tsPPDepart);
					}
				}
			} else {
				//查询HRDM
				tsUsers = sXyBusinessTripService.findHqlTSUsersByRoleCode(Constants.HRDM);
			}
			req.setAttribute("tsUsersPage", tsUsers);
			req.setAttribute("sXyBusinessTripPage", sXyBusinessTrip);
		}
		return new ModelAndView("xyoa/activiti/businesstrip/sXyBusinessTrip-check");
	}
	
	
	
	/**
	 * 直接上级、部门DM、副总裁、总裁：
	 * 享宇出差表否决出差申请 页面跳转
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
	
	
	/**
	 * HR
	 * 享宇出差表批准出差申请 页面跳转
	 * @param sXyBusinessTrip
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goHrAgreeTrip")
	public ModelAndView goHrAgreeTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyBusinessTrip.getId())) {
			sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
			req.setAttribute("sXyBusinessTripPage", sXyBusinessTrip);
		}
		return new ModelAndView("xyoa/activiti/businesstrip/sXyBusinessTrip-hrcheck");
	}
	
	
	/**
	 * HR
	 * 享宇出差表否决出差申请 页面跳转
	 * @param sXyBusinessTrip
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goHrNotAgreeTrip")
	public ModelAndView goHrNotAgreeTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyBusinessTrip.getId())) {
			sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
			req.setAttribute("sXyBusinessTripPage", sXyBusinessTrip);
		}
		return new ModelAndView("xyoa/activiti/businesstrip/sXyBusinessTrip-hrnocheck");
	}
	
	
	
	/**
	 * 销假页面 跳转
	 * @param sXyBusinessTrip
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "gobackTrip")
	public ModelAndView gobackTrip(SXyBusinessTripEntity sXyBusinessTrip, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyBusinessTrip.getId())) {
			sXyBusinessTrip = sXyBusinessTripService.getEntity(SXyBusinessTripEntity.class, sXyBusinessTrip.getId());
			req.setAttribute("sXyBusinessTripPage", sXyBusinessTrip);
		}
		return new ModelAndView("xyoa/activiti/businesstrip/sXyBusinessTrip-back");
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
