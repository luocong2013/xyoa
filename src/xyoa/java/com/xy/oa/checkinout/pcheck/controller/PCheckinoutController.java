package com.xy.oa.checkinout.pcheck.controller;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.xy.oa.checkinout.pcheck.entity.PCheckinoutEntity;
import com.xy.oa.checkinout.pcheck.service.PCheckinoutServiceI;
import com.xy.oa.util.TimerUtils;

/**   
 * @Title: Controller  
 * @Description: p_checkinout
 * @author onlineGenerator
 * @date 2016-08-22 10:02:21
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/pCheckinoutController")
public class PCheckinoutController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PCheckinoutController.class);

	@Autowired
	private PCheckinoutServiceI pCheckinoutService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	


	/**
	 * p_checkinout列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request,Integer userId,String checkDate) {
		request.setAttribute("userId", userId);
		request.setAttribute("checkDate",checkDate);
		return new ModelAndView("xyoa/pcheck/pCheckinoutList");
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
	public void datagrid(PCheckinoutEntity pCheckinout,HttpServletRequest request, HttpServletResponse response ,Integer userId,String checkDate,DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(PCheckinoutEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, pCheckinout, request.getParameterMap());
		try{
			cq.between("checktime", new SimpleDateFormat("yyyy-MM-dd").parse(checkDate) ,TimerUtils.addDate(checkDate, 1));
			cq.eq("userid", userId);
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.pCheckinoutService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除p_checkinout
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(PCheckinoutEntity pCheckinout, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		pCheckinout = systemService.getEntity(PCheckinoutEntity.class, pCheckinout.getId());
		message = "p_checkinout删除成功";
		try{
			pCheckinoutService.delete(pCheckinout);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "p_checkinout删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除p_checkinout
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "p_checkinout删除成功";
		try{
			for(String id:ids.split(",")){
				PCheckinoutEntity pCheckinout = systemService.getEntity(PCheckinoutEntity.class, 
				id
				);
				pCheckinoutService.delete(pCheckinout);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "p_checkinout删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加p_checkinout
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(PCheckinoutEntity pCheckinout, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "p_checkinout添加成功";
		try{
			pCheckinoutService.save(pCheckinout);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "p_checkinout添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新p_checkinout
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(PCheckinoutEntity pCheckinout, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "p_checkinout更新成功";
		PCheckinoutEntity t = pCheckinoutService.get(PCheckinoutEntity.class, pCheckinout.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(pCheckinout, t);
			pCheckinoutService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "p_checkinout更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * p_checkinout新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(PCheckinoutEntity pCheckinout, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(pCheckinout.getId())) {
			pCheckinout = pCheckinoutService.getEntity(PCheckinoutEntity.class, pCheckinout.getId());
			req.setAttribute("pCheckinoutPage", pCheckinout);
		}
		return new ModelAndView("xyoa/pcheck/pCheckinout-add");
	}
	/**
	 * p_checkinout编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(PCheckinoutEntity pCheckinout, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(pCheckinout.getId())) {
			pCheckinout = pCheckinoutService.getEntity(PCheckinoutEntity.class, pCheckinout.getId());
			req.setAttribute("pCheckinoutPage", pCheckinout);
		}
		return new ModelAndView("xyoa/pcheck/pCheckinout-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","pCheckinoutController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(PCheckinoutEntity pCheckinout,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(PCheckinoutEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, pCheckinout, request.getParameterMap());
		List<PCheckinoutEntity> pCheckinouts = this.pCheckinoutService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"p_checkinout");
		modelMap.put(NormalExcelConstants.CLASS,PCheckinoutEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("p_checkinout列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,pCheckinouts);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(PCheckinoutEntity pCheckinout,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"p_checkinout");
    	modelMap.put(NormalExcelConstants.CLASS,PCheckinoutEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("p_checkinout列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
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
				List<PCheckinoutEntity> listPCheckinoutEntitys = ExcelImportUtil.importExcel(file.getInputStream(),PCheckinoutEntity.class,params);
				for (PCheckinoutEntity pCheckinout : listPCheckinoutEntitys) {
					pCheckinoutService.save(pCheckinout);
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
	public List<PCheckinoutEntity> list() {
		List<PCheckinoutEntity> listPCheckinouts=pCheckinoutService.getList(PCheckinoutEntity.class);
		return listPCheckinouts;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		PCheckinoutEntity task = pCheckinoutService.get(PCheckinoutEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody PCheckinoutEntity pCheckinout, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<PCheckinoutEntity>> failures = validator.validate(pCheckinout);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			pCheckinoutService.save(pCheckinout);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = pCheckinout.getId();
		URI uri = uriBuilder.path("/rest/pCheckinoutController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody PCheckinoutEntity pCheckinout) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<PCheckinoutEntity>> failures = validator.validate(pCheckinout);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			pCheckinoutService.saveOrUpdate(pCheckinout);
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
		pCheckinoutService.deleteEntityById(PCheckinoutEntity.class, id);
	}
}
