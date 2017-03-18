package com.xy.oa.staffeducation.controller;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xy.oa.staffeducation.entity.StaffEducationEntity;
import com.xy.oa.staffeducation.service.StaffEducationServiceI;

/**   
 * @Title: Controller  
 * @Description: 教育经历
 * @author onlineGenerator
 * @date 2016-09-29 14:39:37
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/staffEducationController")
public class StaffEducationController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(StaffEducationController.class);

	@Autowired
	private StaffEducationServiceI staffEducationService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	


	/**
	 * 教育经历列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("xyoa/staffeducation/staffEducationList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	/*@RequestMapping(params = "datagrid")
	public void datagrid(StaffEducationEntity staffEducation,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(StaffEducationEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, staffEducation, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.staffEducationService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}*/
	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */

	@RequestMapping(params = "educationdatagrid")
	@ResponseBody
	public Map<String, Object>  educationdatagrid(StaffEducationEntity staffEducation,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid,String isNotDel) throws IllegalAccessException, InvocationTargetException {
		CriteriaQuery cq = new CriteriaQuery(StaffEducationEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, staffEducation, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.staffEducationService.getDataGridReturn(cq, true);
//		TagUtil.datagrid(response, dataGrid);
			Map< String, Object> map=new HashMap<>();
			map.put("total", dataGrid.getTotal());
			List <StaffEducationEntity> result=dataGrid.getResults();
			List <StaffEducationEntity> list=new ArrayList<>();
			
			for(StaffEducationEntity entity:result)
			{
				StaffEducationEntity doct=new StaffEducationEntity();
				org.apache.commons.beanutils.BeanUtils.copyProperties(doct,entity);
				
				if(!"1".equals(isNotDel))
				doct.setUpdate("<button  class=\"easyui-linkbutton\" onclick=\"updateObj('"+entity.getId()+"');\" >编辑</button> "
						+ "<button  class=\"easyui-linkbutton\" onclick=\"detailObj('"+entity.getId()+"');\" >查看</button> "
								+ "<button  class=\"easyui-linkbutton\" onclick=\"delEducationObj('"+entity.getId()+"');\" >删除</button> " );
				else
					doct.setUpdate("<button  class=\"easyui-linkbutton\" onclick=\"updateObj('"+entity.getId()+"');\" >编辑</button> "
							+ "<button  class=\"easyui-linkbutton\" onclick=\"detailObj('"+entity.getId()+"');\" >查看</button> " );
				doct.setDetail("<button  class=\"easyui-linkbutton\" onclick=\"detailObj('"+entity.getId()+"');\" >查看</button> ");
				list.add(doct);
			}
			map.put("rows", list);
			dataGrid.getTotal();
			return map;
	}
	/**
	 * 删除教育经历
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(StaffEducationEntity staffEducation, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		staffEducation = systemService.getEntity(StaffEducationEntity.class, staffEducation.getId());
		message = "教育经历删除成功";
		try{
			staffEducationService.delete(staffEducation);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "教育经历删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除教育经历
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "教育经历删除成功";
		try{
			for(String id:ids.split(",")){
				StaffEducationEntity staffEducation = systemService.getEntity(StaffEducationEntity.class, 
				id
				);
				staffEducationService.delete(staffEducation);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "教育经历删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加教育经历
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(StaffEducationEntity staffEducation, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "教育经历添加成功";
		try{
			HttpSession session = ContextHolderUtils.getSession();
			TSUser u = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
			Date date =new Date();
			staffEducation.setCreatUser(Integer.parseInt( u.getUserName()));
			//staffEducation.setUpdateUser(Integer.parseInt( u.getUserName()));
			staffEducation.setCreatTime(date);
			//	staffEducation.setUpdateTime(date);
			staffEducationService.save(staffEducation);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "教育经历添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新教育经历
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(StaffEducationEntity staffEducation, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "教育经历更新成功";
		StaffEducationEntity t = staffEducationService.get(StaffEducationEntity.class, staffEducation.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(staffEducation, t);
			staffEducationService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "教育经历更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 教育经历新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(StaffEducationEntity staffEducation, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(staffEducation.getId())) {
			staffEducation = staffEducationService.getEntity(StaffEducationEntity.class, staffEducation.getId());
			req.setAttribute("staffEducationPage", staffEducation);
		}	
		req.setAttribute("staffId", staffEducation.getStaffId());
		return new ModelAndView("xyoa/staffeducation/staffEducation-add");
	}
	/**
	 * 教育经历编辑页面跳转
	 * 
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(StaffEducationEntity staffEducation, HttpServletRequest req) throws IllegalAccessException, InvocationTargetException {
		if (StringUtil.isNotEmpty(staffEducation.getId())) {
			staffEducation = staffEducationService.getEntity(StaffEducationEntity.class, staffEducation.getId());
			StaffEducationEntity staffedu=new StaffEducationEntity();
			org.apache.commons.beanutils.BeanUtils.copyProperties(staffedu, staffEducation);
			SimpleDateFormat dateFormater=new SimpleDateFormat("yyyy-MM-dd");
		//	staffedu.setStartDate(dateFormater.format(staffedu.getStartDate()));
		//	staffedu.setEndDate(dateFormater.format(staffedu.getEndDate()));
			req.setAttribute("staffEducationPage", staffedu);
		}
		return new ModelAndView("xyoa/staffeducation/staffEducation-update");
	}
	
	
}
