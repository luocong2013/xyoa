package com.xy.oa.staffhomemember.controller;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xy.oa.staffhomemember.entity.SXyStaffHomeMemberEntity;
import com.xy.oa.staffhomemember.service.SXyStaffHomeMemberServiceI;

/**   
 * @Title: Controller  
 * @Description: 员工家庭成员
 * @author onlineGenerator
 * @date 2016-10-11 14:29:25
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/sXyStaffHomeMemberController")
public class SXyStaffHomeMemberController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SXyStaffHomeMemberController.class);

	@Autowired
	private SXyStaffHomeMemberServiceI sXyStaffHomeMemberService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	


	/**
	 * 员工家庭成员列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("xyoa/staffhomemember/sXyStaffHomeMemberList");
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
	public void datagrid(SXyStaffHomeMemberEntity sXyStaffHomeMember,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SXyStaffHomeMemberEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyStaffHomeMember, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyStaffHomeMemberService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
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

	@RequestMapping(params = "homedatagrid")
	@ResponseBody
	public Map<String, Object>  homedatagrid(SXyStaffHomeMemberEntity sXyStaffHomeMember,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid,String isNotDel) throws IllegalAccessException, InvocationTargetException {
		CriteriaQuery cq = new CriteriaQuery(SXyStaffHomeMemberEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyStaffHomeMember, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyStaffHomeMemberService.getDataGridReturn(cq, true);
	
	Map< String, Object> map=new HashMap<>();
	map.put("total", dataGrid.getTotal());
	List <SXyStaffHomeMemberEntity> result=dataGrid.getResults();
	List <SXyStaffHomeMemberEntity> list=new ArrayList<>();
	
	for(SXyStaffHomeMemberEntity entity:result)
	{
		SXyStaffHomeMemberEntity homeEntity=new SXyStaffHomeMemberEntity();
		org.apache.commons.beanutils.BeanUtils.copyProperties(homeEntity,entity);
		
		if(!"1".equals(isNotDel))
			homeEntity.setUpdate("<button  class=\"easyui-linkbutton\" onclick=\"addeducation('修改家庭成员','sXyStaffHomeMemberController.do?goUpdate&id="+entity.getId()+"',510,280);\" >编辑</button> "
				+ "<button  class=\"easyui-linkbutton\" onclick=\"createdetailwindow('查看家庭成员','sXyStaffHomeMemberController.do?goUpdate&load=detail&id="+entity.getId()+"',510,280);\" >查看</button> "
						+ "<button  class=\"easyui-linkbutton\" onclick=\"createdialog('删除确认 ', '确定删除该文件吗 ?','sXyStaffHomeMemberController.do?doDel&id="+entity.getId()+"','');\" >删除</button> " );
		else
			homeEntity.setUpdate("<button  class=\"easyui-linkbutton\" onclick=\"addeducation('修改家庭成员','sXyStaffHomeMemberController.do?goUpdate&id="+entity.getId()+"',510,280);\" >编辑</button> "
					+ "<button  class=\"easyui-linkbutton\" onclick=\"createdetailwindow('查看家庭成员','sXyStaffHomeMemberController.do?goUpdate&load=detail&id="+entity.getId()+"',510,280);\" >查看</button> ");
		homeEntity.setDetail("<button  class=\"easyui-linkbutton\" onclick=\"createdetailwindow('查看家庭成员','sXyStaffHomeMemberController.do?goUpdate&load=detail&id="+entity.getId()+"',510,280);\" >查看</button>");
		list.add(homeEntity);
	}
	map.put("rows", list);
	dataGrid.getTotal();
	return map;
	}
	
	/**
	 * 删除员工家庭成员
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(SXyStaffHomeMemberEntity sXyStaffHomeMember, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		sXyStaffHomeMember = systemService.getEntity(SXyStaffHomeMemberEntity.class, sXyStaffHomeMember.getId());
		message = "员工家庭成员删除成功";
		try{
			sXyStaffHomeMemberService.delete(sXyStaffHomeMember);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "员工家庭成员删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除员工家庭成员
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "员工家庭成员删除成功";
		try{
			for(String id:ids.split(",")){
				SXyStaffHomeMemberEntity sXyStaffHomeMember = systemService.getEntity(SXyStaffHomeMemberEntity.class, 
				id
				);
				sXyStaffHomeMemberService.delete(sXyStaffHomeMember);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "员工家庭成员删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加员工家庭成员
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(SXyStaffHomeMemberEntity sXyStaffHomeMember, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "员工家庭成员添加成功";
		try{
			sXyStaffHomeMemberService.save(sXyStaffHomeMember);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "员工家庭成员添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新员工家庭成员
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(SXyStaffHomeMemberEntity sXyStaffHomeMember, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "员工家庭成员更新成功";
		SXyStaffHomeMemberEntity t = sXyStaffHomeMemberService.get(SXyStaffHomeMemberEntity.class, sXyStaffHomeMember.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(sXyStaffHomeMember, t);
			sXyStaffHomeMemberService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "员工家庭成员更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 员工家庭成员新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(SXyStaffHomeMemberEntity sXyStaffHomeMember, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyStaffHomeMember.getId())) {
			sXyStaffHomeMember = sXyStaffHomeMemberService.getEntity(SXyStaffHomeMemberEntity.class, sXyStaffHomeMember.getId());
			req.setAttribute("sXyStaffHomeMemberPage", sXyStaffHomeMember);
		}
		req.setAttribute("staffId", sXyStaffHomeMember.getStaffId());
		return new ModelAndView("xyoa/staffhomemember/sXyStaffHomeMember-add");
	}
	/**
	 * 员工家庭成员编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(SXyStaffHomeMemberEntity sXyStaffHomeMember, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyStaffHomeMember.getId())) {
			sXyStaffHomeMember = sXyStaffHomeMemberService.getEntity(SXyStaffHomeMemberEntity.class, sXyStaffHomeMember.getId());
			req.setAttribute("sXyStaffHomeMemberPage", sXyStaffHomeMember);
		}
		return new ModelAndView("xyoa/staffhomemember/sXyStaffHomeMember-update");
	}
	
	
	
}
