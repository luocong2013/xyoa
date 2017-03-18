package com.xy.oa.stafftrain.controller;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
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
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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

import com.xy.oa.stafftrain.entity.SXyStaffTrainEntity;
import com.xy.oa.stafftrain.service.SXyStaffTrainServiceI;

/**   
 * @Title: Controller  
 * @Description: 员工培训经历
 * @author onlineGenerator
 * @date 2016-10-11 14:29:46
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/sXyStaffTrainController")
public class SXyStaffTrainController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SXyStaffTrainController.class);

	@Autowired
	private SXyStaffTrainServiceI sXyStaffTrainService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	


	/**
	 * 员工培训经历列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("xyoa/stafftrain/sXyStaffTrainList");
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
	public void datagrid(SXyStaffTrainEntity sXyStaffTrain,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SXyStaffTrainEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyStaffTrain, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyStaffTrainService.getDataGridReturn(cq, true);
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

	@RequestMapping(params = "traindatagrid")
	@ResponseBody
	public Map<String, Object> traindatagrid(SXyStaffTrainEntity sXyStaffTrain,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid,String isNotDel) throws IllegalAccessException, InvocationTargetException {
		CriteriaQuery cq = new CriteriaQuery(SXyStaffTrainEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyStaffTrain, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyStaffTrainService.getDataGridReturn(cq, true);
		
		Map< String, Object> map=new HashMap<>();
		map.put("total", dataGrid.getTotal());
		List <SXyStaffTrainEntity> result=dataGrid.getResults();
		List <SXyStaffTrainEntity> list=new ArrayList<>();
		
		for(SXyStaffTrainEntity entity:result)
		{
			SXyStaffTrainEntity trainEntity=new SXyStaffTrainEntity();
			org.apache.commons.beanutils.BeanUtils.copyProperties(trainEntity,entity);
			
			if(!"1".equals(isNotDel))
				trainEntity.setUpdate("<button  class=\"easyui-linkbutton\" onclick=\"addeducation('修改培训经历','sXyStaffTrainController.do?goUpdate&id="+entity.getId()+"',510,200);\" >编辑</button> "
					+ "<button  class=\"easyui-linkbutton\" onclick=\"createdetailwindow('查看培训经历','sXyStaffTrainController.do?goUpdate&load=detail&id="+entity.getId()+"',510,200);\" >查看</button> "
							+ "<button  class=\"easyui-linkbutton\" onclick=\"createdialog('删除确认 ', '确定删除该文件吗 ?','sXyStaffTrainController.do?doDel&id="+entity.getId()+"','');\" >删除</button> " );
			else
				trainEntity.setUpdate("<button  class=\"easyui-linkbutton\" onclick=\"addeducation('修改培训经历','sXyStaffTrainController.do?goUpdate&id="+entity.getId()+"',510,200);\" >编辑</button> "
						+ "<button  class=\"easyui-linkbutton\" onclick=\"createdetailwindow('查看培训经历','sXyStaffTrainController.do?goUpdate&load=detail&id="+entity.getId()+"',510,200);\" >查看</button> ");
			trainEntity.setDetail("<button  class=\"easyui-linkbutton\" onclick=\"createdetailwindow('查看培训经历','sXyStaffTrainController.do?goUpdate&load=detail&id="+entity.getId()+"',510,200);\" >查看</button> ");
			list.add(trainEntity);
		}
		map.put("rows", list);
		dataGrid.getTotal();
		return map;
	}
	/**
	 * 删除员工培训经历
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(SXyStaffTrainEntity sXyStaffTrain, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		sXyStaffTrain = systemService.getEntity(SXyStaffTrainEntity.class, sXyStaffTrain.getId());
		message = "培训经历删除成功";
		try{
			sXyStaffTrainService.delete(sXyStaffTrain);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "培训经历删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除员工培训经历
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "员工培训经历删除成功";
		try{
			for(String id:ids.split(",")){
				SXyStaffTrainEntity sXyStaffTrain = systemService.getEntity(SXyStaffTrainEntity.class, 
				id
				);
				sXyStaffTrainService.delete(sXyStaffTrain);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "员工培训经历删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加员工培训经历
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(SXyStaffTrainEntity sXyStaffTrain, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "员工培训经历添加成功";
		try{
			sXyStaffTrainService.save(sXyStaffTrain);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "员工培训经历添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新员工培训经历
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(SXyStaffTrainEntity sXyStaffTrain, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "员工培训经历更新成功";
		SXyStaffTrainEntity t = sXyStaffTrainService.get(SXyStaffTrainEntity.class, sXyStaffTrain.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(sXyStaffTrain, t);
			sXyStaffTrainService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "员工培训经历更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 员工培训经历新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(SXyStaffTrainEntity sXyStaffTrain, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyStaffTrain.getId())) {
			sXyStaffTrain = sXyStaffTrainService.getEntity(SXyStaffTrainEntity.class, sXyStaffTrain.getId());
			req.setAttribute("sXyStaffTrainPage", sXyStaffTrain);
		}
		req.setAttribute("staffId", sXyStaffTrain.getStaffId());
		return new ModelAndView("xyoa/stafftrain/sXyStaffTrain-add");
	}
	/**
	 * 员工培训经历编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(SXyStaffTrainEntity sXyStaffTrain, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyStaffTrain.getId())) {
			sXyStaffTrain = sXyStaffTrainService.getEntity(SXyStaffTrainEntity.class, sXyStaffTrain.getId());
			req.setAttribute("sXyStaffTrainPage", sXyStaffTrain);
		}
		return new ModelAndView("xyoa/stafftrain/sXyStaffTrain-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","sXyStaffTrainController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(SXyStaffTrainEntity sXyStaffTrain,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(SXyStaffTrainEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyStaffTrain, request.getParameterMap());
		List<SXyStaffTrainEntity> sXyStaffTrains = this.sXyStaffTrainService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"员工培训经历");
		modelMap.put(NormalExcelConstants.CLASS,SXyStaffTrainEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("员工培训经历列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,sXyStaffTrains);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(SXyStaffTrainEntity sXyStaffTrain,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"员工培训经历");
    	modelMap.put(NormalExcelConstants.CLASS,SXyStaffTrainEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("员工培训经历列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
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
				List<SXyStaffTrainEntity> listSXyStaffTrainEntitys = ExcelImportUtil.importExcel(file.getInputStream(),SXyStaffTrainEntity.class,params);
				for (SXyStaffTrainEntity sXyStaffTrain : listSXyStaffTrainEntitys) {
					sXyStaffTrainService.save(sXyStaffTrain);
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
	public List<SXyStaffTrainEntity> list() {
		List<SXyStaffTrainEntity> listSXyStaffTrains=sXyStaffTrainService.getList(SXyStaffTrainEntity.class);
		return listSXyStaffTrains;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		SXyStaffTrainEntity task = sXyStaffTrainService.get(SXyStaffTrainEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody SXyStaffTrainEntity sXyStaffTrain, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyStaffTrainEntity>> failures = validator.validate(sXyStaffTrain);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyStaffTrainService.save(sXyStaffTrain);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = sXyStaffTrain.getId();
		URI uri = uriBuilder.path("/rest/sXyStaffTrainController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody SXyStaffTrainEntity sXyStaffTrain) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyStaffTrainEntity>> failures = validator.validate(sXyStaffTrain);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyStaffTrainService.saveOrUpdate(sXyStaffTrain);
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
		sXyStaffTrainService.deleteEntityById(SXyStaffTrainEntity.class, id);
	}
}
