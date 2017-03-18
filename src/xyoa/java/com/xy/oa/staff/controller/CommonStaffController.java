package com.xy.oa.staff.controller;
import com.xy.oa.deptjob.entity.DeptJobEntity;
import com.xy.oa.deptjob.service.DeptJobServiceI;
import com.xy.oa.staff.entity.DllDepart;
import com.xy.oa.staff.entity.StaffEntity;
import com.xy.oa.staff.service.StaffServiceI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import java.io.OutputStream;
import org.jeecgframework.core.util.BrowserUtils;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.vo.TemplateExcelConstants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jeecgframework.core.util.ResourceUtil;
import java.io.IOException;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Map;
import org.jeecgframework.core.util.ExceptionUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

/**   
 * @Title: Controller  
 * @Description: 普通查询员工表
 * @author xiaoyong
 * @date 2016-08-10 15:59:20
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/commonstaffController")
public class CommonStaffController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CommonStaffController.class);

	@Autowired
	private StaffServiceI staffService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private DeptJobServiceI deptJobService;

	/**
	 * 享宇员工表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		// 得到当前登录用户,由当前用户得到员工信息
		HttpSession session = ContextHolderUtils.getSession();
		TSUser u = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		TSUser user=staffService.getEntity(TSUser.class, u.getId());
		StaffEntity staff=null;
		//得到当前登录用户在员工表中的id
		if(user.getUserName()!=null&&!"".equals(user.getUserName()))
		{
			int staffid=0;
			try {
			    staffid = Integer.parseInt(user.getUserName());
			  staff= staffService.getStaffByUsername(staffid);
			  request.setAttribute("staff", staff);
			} catch (NumberFormatException e) {
			    e.printStackTrace();
			}
		}
		

		//得到组织的名称和id ,表格上显示使用
		
		List<TSDepart> listcompany=staffService.getAllCompany();
		String replacecompany="";
		for(int i=0;i<listcompany.size();i++)
		{
			if(listcompany.get(i).getDepartname()==null||"".equals(listcompany.get(i).getDepartname()))
				replacecompany+="无"+"_"+listcompany.get(i).getId()+",";	
			else
				replacecompany+=listcompany.get(i).getDepartname()+"_"+listcompany.get(i).getId()+",";
		}
		replacecompany=replacecompany.substring(0,replacecompany.length()-1);
		 request.setAttribute("replacecompany", replacecompany);
		 
		 request.setAttribute("replacedepart", staffService.getDeptAllStr());
		 
		 List<DeptJobEntity> listjob=deptJobService.getAllDeptJob();
		 String replacejob="";
		 for(int i=0;i<listjob.size();i++)
			{
			 if(listjob.get(i).getJobName()==null||"".equals(listjob.get(i).getJobName()))
				 replacejob+="无"+"_"+listjob.get(i).getId()+",";	
			 	else
			 		replacejob+=listjob.get(i).getJobName()+"_"+listjob.get(i).getId()+",";
			}
		 replacejob=replacejob.substring(0,replacejob.length()-1);
		request.setAttribute("replacejob", replacejob);
		return new ModelAndView("xyoa/commonstaff/staffList");
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
	public void datagrid(StaffEntity staff,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		
		CriteriaQuery cq = new CriteriaQuery(StaffEntity.class, dataGrid);
		if(StringUtil.isNotEmpty(staff.getDeptId())){
			cq.in("deptId", staffService.getSSDeptId(staff.getDeptId()).toArray());
			staff.setDeptId(null); 
		}
		cq.between("state", "1", "7");
		cq.addOrder("sttaffId", SortDirection.asc);
		
		if(!"".equals(staff.getName())&&staff.getName()!=null)
		staff.setName("*"+staff.getName()+"*");
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, staff, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.staffService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	
	
	
	/**
	 * 更新享宇员工表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(StaffEntity staff, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇员工表更新成功";
		StaffEntity t = staffService.get(StaffEntity.class, staff.getId());
		//获取登录人信息
		HttpSession session = ContextHolderUtils.getSession();
		TSUser u = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		Date date =new Date();
		int createid =-1;
		//设置跟新人  、跟新时间
		if(!"".equals(u.getUserName())&&u.getUserName()!=null) createid =Integer.parseInt( u.getUserName());		
		staff.setUpudateTime(date);
		staff.setUpdateUer(createid);
		try {
			MyBeanUtils.copyBeanNotNull2Bean(staff, t);
			staffService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "享宇员工表更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	

	/**
	 * 新增页面中的ajax 二级 联动
	 * 
	 * @return
	 */
	@RequestMapping(params = "getDeparts")
	@ResponseBody
	public List<DllDepart> getDeparts(TSDepart company, HttpServletRequest req) {
		
		List<DllDepart> departs = staffService.getAllDepartByCompany(company);
		
		return departs;
	}
	/**
	 * 享宇员工表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(StaffEntity staff, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(staff.getId())) {
			staff = staffService.getEntity(StaffEntity.class, staff.getId());
			req.setAttribute("staffPage", staff);
		}
		
		String username =""+staff.getSttaffId();
		TSUser user= staffService.getUserByUsername(username);
		
		req.setAttribute("user", user);
		if(user!=null)
		{	
			idandname(req, user);
			getOrgInfos(req, user);
		}
		
		TSDepart depart = staffService.getEntity(TSDepart.class, staff.getDeptId());
		if(depart!=null)	
		req.setAttribute("departname", depart.getDepartname());
		List<TSDepart> company = staffService.getAllCompany();
		req.setAttribute("TSDeparts", company);
		
		String deptId = staff.getDeptId();
		if(depart.getOrgCode().length()>9){
			deptId = depart.getTSPDepart().getId();
		}
		List<DeptJobEntity> deptjobs=staffService.getDeptJobsByDepartId(deptId);
		req.setAttribute("deptjobs", deptjobs);
		String loadtype=req.getParameter("load");
		if("detail".equals(loadtype))
			return new ModelAndView("xyoa/commonstaff/detailStaff");
		else
			return new ModelAndView("xyoa/commonstaff/staff-update");
		
	}
	
	/*@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<StaffEntity> list() {
		List<StaffEntity> listStaffs=staffService.getList(StaffEntity.class);
		return listStaffs;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		StaffEntity task = staffService.get(StaffEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody StaffEntity staff, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<StaffEntity>> failures = validator.validate(staff);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			staffService.save(staff);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = staff.getId();
		URI uri = uriBuilder.path("/rest/staffController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody StaffEntity staff) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<StaffEntity>> failures = validator.validate(staff);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			staffService.saveOrUpdate(staff);
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
		staffService.deleteEntityById(StaffEntity.class, id);
	}
	*/
	public void idandname(HttpServletRequest req, TSUser user) {
		List<TSRoleUser> roleUsers = systemService.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
		String roleId = "";
		String roleName = "";
		if (roleUsers.size() > 0) {
			for (TSRoleUser tRoleUser : roleUsers) {
				roleId += tRoleUser.getTSRole().getId() + ",";
				roleName += tRoleUser.getTSRole().getRoleName() + ",";
			}
		}
		req.setAttribute("id", roleId);
		req.setAttribute("roleName", roleName);

	}
	
	public void getOrgInfos(HttpServletRequest req, TSUser user) {
		List<TSUserOrg> tSUserOrgs = systemService.findByProperty(TSUserOrg.class, "tsUser.id", user.getId());
		String orgIds = "";
		String departname = "";
		if (tSUserOrgs.size() > 0) {
			for (TSUserOrg tSUserOrg : tSUserOrgs) {
				orgIds += tSUserOrg.getTsDepart().getId() + ",";
				departname += tSUserOrg.getTsDepart().getDepartname() + ",";
			}
		}
		req.setAttribute("orgIds", orgIds);
		req.setAttribute("departname", departname);

	}
}
