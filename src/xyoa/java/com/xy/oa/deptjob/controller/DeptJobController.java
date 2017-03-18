package com.xy.oa.deptjob.controller;
import com.xy.oa.deptjob.entity.DeptJobEntity;
import com.xy.oa.deptjob.service.DeptJobServiceI;
import com.xy.oa.staff.entity.DllDepart;

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
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;
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
import org.hibernate.criterion.DetachedCriteria;
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
 * @Description: 岗位表
 * @author onlineGenerator
 * @date 2016-08-11 17:20:14
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/deptJobController")
public class DeptJobController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DeptJobController.class);

	@Autowired
	private DeptJobServiceI deptJobService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	


	/**
	 * 岗位表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		
		//得到部门的名称和id ,表格上显示使用
		List<TSDepart> listDepart=deptJobService.getALLDepart();
		String replace="";
		for(int i=0;i<listDepart.size();i++)
		{
			replace+=listDepart.get(i).getDepartname()+"_"+listDepart.get(i).getId()+",";
		}
		 replace=replace.substring(0,replace.length()-1);
		 request.setAttribute("replace", replace);
		 
		return new ModelAndView("xyoa/deptjob/deptJobList");
	}

	
	
	/**
	 * 新增页面中的ajax 二级 联动
	 * 
	 * @return
	 */
	@RequestMapping(params = "getDeptJobs")
	@ResponseBody
	public List<DeptJobEntity> getDeptJobs(String departid, HttpServletRequest req) {
		TSDepart dePart = this.deptJobService.getEntity(TSDepart.class, departid);
		
		if(dePart.getOrgCode().length() > 9)
			departid = dePart.getTSPDepart().getId();
		List<DeptJobEntity> deptJobs = deptJobService.getDeptJobs(departid);
		
		return deptJobs;
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
	public void datagrid(DeptJobEntity deptJob,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		
		CriteriaQuery cq = new CriteriaQuery(DeptJobEntity.class, dataGrid);
		//查询条件组装器
		if(!"".equals(deptJob.getJobName())&&deptJob.getJobName()!=null)
			deptJob.setJobName("*"+deptJob.getJobName()+"*");
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, deptJob, request.getParameterMap());
		try{
		//自定义追加查询条件
		String query_createTime_begin = request.getParameter("createTime_begin");
		String query_createTime_end = request.getParameter("createTime_end");
		if(StringUtil.isNotEmpty(query_createTime_begin)){
			cq.ge("createTime", new SimpleDateFormat("yyyy-MM-dd").parse(query_createTime_begin));
		}
		if(StringUtil.isNotEmpty(query_createTime_end)){
			cq.le("createTime", new SimpleDateFormat("yyyy-MM-dd").parse(query_createTime_end));
		}
		String query_updateTime_begin = request.getParameter("updateTime_begin");
		String query_updateTime_end = request.getParameter("updateTime_end");
		if(StringUtil.isNotEmpty(query_updateTime_begin)){
			cq.ge("updateTime", new SimpleDateFormat("yyyy-MM-dd").parse(query_updateTime_begin));
		}
		if(StringUtil.isNotEmpty(query_updateTime_end)){
			cq.le("updateTime", new SimpleDateFormat("yyyy-MM-dd").parse(query_updateTime_end));
		}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.deptJobService.getDataGridReturn(cq, true);
	   
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除岗位表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(DeptJobEntity deptJob, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		deptJob = systemService.getEntity(DeptJobEntity.class, deptJob.getId());
		message = "岗位表删除成功";
		
		try{
			deptJobService.delete(deptJob);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "岗位表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除岗位表
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "岗位表删除成功";
		try{
			for(String id:ids.split(",")){
				DeptJobEntity deptJob = systemService.getEntity(DeptJobEntity.class, 
				id
				);
				deptJobService.delete(deptJob);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "岗位表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加岗位表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(DeptJobEntity deptJob, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "岗位表添加成功";
		// 得到当前登录用户
		HttpSession session = ContextHolderUtils.getSession();
		TSUser u = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		Date date =new Date();
		deptJob.setCreateUser(u.getRealName());
		deptJob.setUpdateUser(u.getRealName());
		deptJob.setUpdateTime(date);
		deptJob.setCreateTime(date);
		try{
			deptJobService.save(deptJob);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "岗位表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新岗位表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(DeptJobEntity deptJob, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "岗位表更新成功";
		DeptJobEntity t = deptJobService.get(DeptJobEntity.class, deptJob.getId());
		// 得到当前登录用户
		HttpSession session = ContextHolderUtils.getSession();
		TSUser u = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		Date date =new Date();		
		deptJob.setUpdateUser(u.getRealName());
		deptJob.setUpdateTime(date);
		
		try {
			MyBeanUtils.copyBeanNotNull2Bean(deptJob, t);
			deptJobService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "岗位表更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 岗位表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(DeptJobEntity deptJob, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(deptJob.getId())) {
			deptJob = deptJobService.getEntity(DeptJobEntity.class, deptJob.getId());
			req.setAttribute("deptJobPage", deptJob);
		}
		List<TSDepart> listDepart=deptJobService.getALLDepart();
		req.setAttribute("listDepart", listDepart);
		return new ModelAndView("xyoa/deptjob/deptJob-add");
	}
	/**
	 * 岗位表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(DeptJobEntity deptJob, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(deptJob.getId())) {
			deptJob = deptJobService.getEntity(DeptJobEntity.class, deptJob.getId());
			req.setAttribute("deptJobPage", deptJob);
			TSDepart depart = deptJobService.getEntity(TSDepart.class, deptJob.getDeptId());
			String departname =depart.getDepartname();
			req.setAttribute("departname", departname);
			
		}
		
		
		
		return new ModelAndView("xyoa/deptjob/deptJob-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","deptJobController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(DeptJobEntity deptJob,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(DeptJobEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, deptJob, request.getParameterMap());
		List<DeptJobEntity> deptJobs = this.deptJobService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"岗位表");
		modelMap.put(NormalExcelConstants.CLASS,DeptJobEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("岗位表列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,deptJobs);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(DeptJobEntity deptJob,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"岗位表");
    	modelMap.put(NormalExcelConstants.CLASS,DeptJobEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("岗位表列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
    	"导出信息"));
    	modelMap.put(NormalExcelConstants.DATA_LIST,new ArrayList());
    	return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	
	@SuppressWarnings("unchecked")
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
				List<DeptJobEntity> listDeptJobEntitys = ExcelImportUtil.importExcel(file.getInputStream(),DeptJobEntity.class,params);
				for (DeptJobEntity deptJob : listDeptJobEntitys) {
					deptJobService.save(deptJob);
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
	public List<DeptJobEntity> list() {
		List<DeptJobEntity> listDeptJobs=deptJobService.getList(DeptJobEntity.class);
		return listDeptJobs;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		DeptJobEntity task = deptJobService.get(DeptJobEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody DeptJobEntity deptJob, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<DeptJobEntity>> failures = validator.validate(deptJob);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			deptJobService.save(deptJob);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = deptJob.getId();
		URI uri = uriBuilder.path("/rest/deptJobController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody DeptJobEntity deptJob) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<DeptJobEntity>> failures = validator.validate(deptJob);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			deptJobService.saveOrUpdate(deptJob);
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
		deptJobService.deleteEntityById(DeptJobEntity.class, id);
	}
}
