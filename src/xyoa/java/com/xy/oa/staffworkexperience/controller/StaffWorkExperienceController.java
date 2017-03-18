package com.xy.oa.staffworkexperience.controller;
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

import com.xy.oa.staffworkexperience.entity.StaffWorkExperienceEntity;
import com.xy.oa.staffworkexperience.service.StaffWorkExperienceServiceI;

/**   
 * @Title: Controller  
 * @Description: 工作经历
 * @author onlineGenerator
 * @date 2016-10-09 10:51:26
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/staffWorkExperienceController")
public class StaffWorkExperienceController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(StaffWorkExperienceController.class);

	@Autowired
	private StaffWorkExperienceServiceI staffWorkExperienceService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	


	/**
	 * 工作经历列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("xyoa/staffworkexperience/staffWorkExperienceList");
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
	public void datagrid(StaffWorkExperienceEntity staffWorkExperience,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(StaffWorkExperienceEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, staffWorkExperience, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.staffWorkExperienceService.getDataGridReturn(cq, true);
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

	@RequestMapping(params = "experiencedatagrid")
	@ResponseBody
	public Map<String, Object>  experiencedatagrid(StaffWorkExperienceEntity staffWorkExperience,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid,String isNotDel) throws IllegalAccessException, InvocationTargetException {
		CriteriaQuery cq = new CriteriaQuery(StaffWorkExperienceEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, staffWorkExperience, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.staffWorkExperienceService.getDataGridReturn(cq, true);
		Map< String, Object> map=new HashMap<>();
		map.put("total", dataGrid.getTotal());
		List <StaffWorkExperienceEntity> result=dataGrid.getResults();
		List <StaffWorkExperienceEntity> list=new ArrayList<>();
		
		for(StaffWorkExperienceEntity entity:result)
		{
			StaffWorkExperienceEntity experienceEntity=new StaffWorkExperienceEntity();
			org.apache.commons.beanutils.BeanUtils.copyProperties(experienceEntity,entity);
			
			if(!"1".equals(isNotDel))
				experienceEntity.setUpdate("<button  class=\"easyui-linkbutton\" onclick=\"updateExprienceObj('"+entity.getId()+"');\" >编辑</button> "
						+ "<button  class=\"easyui-linkbutton\" onclick=\"detailExprienceObj('"+entity.getId()+"');\" >查看</button> "
								+ "<button  class=\"easyui-linkbutton\" onclick=\"delExprienceObj('"+entity.getId()+"');\" >删除</button> " );
			else
				experienceEntity.setUpdate("<button  class=\"easyui-linkbutton\" onclick=\"updateExprienceObj('"+entity.getId()+"');\" >编辑</button> "
						+ "<button  class=\"easyui-linkbutton\" onclick=\"detailExprienceObj('"+entity.getId()+"');\" >查看</button> ");
			experienceEntity.setDetail("<button  class=\"easyui-linkbutton\" onclick=\"detailExprienceObj('"+entity.getId()+"');\" >查看</button> ");
			list.add(experienceEntity);
		}
		map.put("rows", list);
		dataGrid.getTotal();
		return map;
	}

	/**
	 * 删除工作经历
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(StaffWorkExperienceEntity staffWorkExperience, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		staffWorkExperience = systemService.getEntity(StaffWorkExperienceEntity.class, staffWorkExperience.getId());
		message = "工作经历删除成功";
		try{
			staffWorkExperienceService.delete(staffWorkExperience);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "工作经历删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	/*
	*//**
	 * 批量删除工作经历
	 * 
	 * @return
	 *//*
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "工作经历删除成功";
		try{
			for(String id:ids.split(",")){
				StaffWorkExperienceEntity staffWorkExperience = systemService.getEntity(StaffWorkExperienceEntity.class, 
				id
				);
				staffWorkExperienceService.delete(staffWorkExperience);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "工作经历删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
*/

	/**
	 * 添加工作经历
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(StaffWorkExperienceEntity staffWorkExperience, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "工作经历添加成功";
		try{
			staffWorkExperienceService.save(staffWorkExperience);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "工作经历添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新工作经历
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(StaffWorkExperienceEntity staffWorkExperience, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "工作经历更新成功";
		StaffWorkExperienceEntity t = staffWorkExperienceService.get(StaffWorkExperienceEntity.class, staffWorkExperience.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(staffWorkExperience, t);
			staffWorkExperienceService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "工作经历更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 工作经历新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(StaffWorkExperienceEntity staffWorkExperience, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(staffWorkExperience.getId())) {
			staffWorkExperience = staffWorkExperienceService.getEntity(StaffWorkExperienceEntity.class, staffWorkExperience.getId());
			req.setAttribute("staffWorkExperiencePage", staffWorkExperience);
		}
		req.setAttribute("staffId", staffWorkExperience.getStaffId());
		return new ModelAndView("xyoa/staffworkexperience/staffWorkExperience-add");
	}
	/**
	 * 工作经历编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(StaffWorkExperienceEntity staffWorkExperience, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(staffWorkExperience.getId())) {
			staffWorkExperience = staffWorkExperienceService.getEntity(StaffWorkExperienceEntity.class, staffWorkExperience.getId());
			req.setAttribute("staffWorkExperiencePage", staffWorkExperience);
		}
		return new ModelAndView("xyoa/staffworkexperience/staffWorkExperience-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 *//*
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","staffWorkExperienceController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	*//**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 *//*
	@RequestMapping(params = "exportXls")
	public String exportXls(StaffWorkExperienceEntity staffWorkExperience,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(StaffWorkExperienceEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, staffWorkExperience, request.getParameterMap());
		List<StaffWorkExperienceEntity> staffWorkExperiences = this.staffWorkExperienceService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"工作经历");
		modelMap.put(NormalExcelConstants.CLASS,StaffWorkExperienceEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("工作经历列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,staffWorkExperiences);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	*//**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 *//*
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(StaffWorkExperienceEntity staffWorkExperience,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"工作经历");
    	modelMap.put(NormalExcelConstants.CLASS,StaffWorkExperienceEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("工作经历列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
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
				List<StaffWorkExperienceEntity> listStaffWorkExperienceEntitys = ExcelImportUtil.importExcel(file.getInputStream(),StaffWorkExperienceEntity.class,params);
				for (StaffWorkExperienceEntity staffWorkExperience : listStaffWorkExperienceEntitys) {
					staffWorkExperienceService.save(staffWorkExperience);
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
	public List<StaffWorkExperienceEntity> list() {
		List<StaffWorkExperienceEntity> listStaffWorkExperiences=staffWorkExperienceService.getList(StaffWorkExperienceEntity.class);
		return listStaffWorkExperiences;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		StaffWorkExperienceEntity task = staffWorkExperienceService.get(StaffWorkExperienceEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody StaffWorkExperienceEntity staffWorkExperience, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<StaffWorkExperienceEntity>> failures = validator.validate(staffWorkExperience);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			staffWorkExperienceService.save(staffWorkExperience);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = staffWorkExperience.getId();
		URI uri = uriBuilder.path("/rest/staffWorkExperienceController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody StaffWorkExperienceEntity staffWorkExperience) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<StaffWorkExperienceEntity>> failures = validator.validate(staffWorkExperience);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			staffWorkExperienceService.saveOrUpdate(staffWorkExperience);
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
		staffWorkExperienceService.deleteEntityById(StaffWorkExperienceEntity.class, id);
	}*/
}
