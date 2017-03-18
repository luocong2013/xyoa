package com.xy.oa.sheduletime.controller;
import com.xy.oa.sheduletime.entity.OneWeekShedule;
import com.xy.oa.sheduletime.entity.XySheduleTimeEntity;
import com.xy.oa.sheduletime.service.XySheduleTimeServiceI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.jeecgframework.core.common.model.json.TreeGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.tag.vo.easyui.TreeGridModel;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import java.io.OutputStream;
import org.jeecgframework.core.util.BrowserUtils;
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
 * @Description: 排班
 * @author onlineGenerator
 * @date 2016-09-13 17:19:07
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/xySheduleTimeController")
public class XySheduleTimeController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(XySheduleTimeController.class);

	@Autowired
	private XySheduleTimeServiceI xySheduleTimeService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	
	/** 
	 * 部门列表，树形展示 
	 * @param request 
	 * @param response 
	 * @param treegrid 
	 * @return 
	 */  
	@RequestMapping(params = "sheduletimeid")  
	@ResponseBody  
	public  List<TreeGrid> departgrid(XySheduleTimeEntity sheduleTimeEntity,HttpServletRequest request, HttpServletResponse response, TreeGrid treegrid) {  
	    CriteriaQuery cq = new CriteriaQuery(XySheduleTimeEntity.class);  
	  
	    if(null != sheduleTimeEntity.getTimename()){  
	        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sheduleTimeEntity);  
	    }  
	    if (treegrid.getId() != null) {  
	        cq.eq("xySheduleTimeEntity.id", treegrid.getId());  
	    }  
	    if (treegrid.getId() == null) {  
	        cq.isNull("xySheduleTimeEntity");  
	    }  
	    cq.addOrder("orders", SortDirection.asc);
	    cq.add();  
	    List<TreeGrid> departList =null;  
	    departList = systemService.getListByCriteriaQuery(cq, false);  
	    if(departList.size() == 0 && sheduleTimeEntity.getTimename() != null){   
	        cq = new CriteriaQuery(XySheduleTimeEntity.class);  
	        XySheduleTimeEntity sheduleTime = new XySheduleTimeEntity();  
	        sheduleTimeEntity.setXySheduleTimeEntity(sheduleTime); 
	        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sheduleTimeEntity);  
	        departList = systemService.getListByCriteriaQuery(cq, false);  
	    }  
	    List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();  
	    TreeGridModel treeGridModel = new TreeGridModel();  
	    treeGridModel.setTextField("timename");  
	    treeGridModel.setParentText("xySheduleTimeEntity_timename");  
	    treeGridModel.setParentId("xySheduleTimeEntity_id");  
	    treeGridModel.setIdField("id");
	    treeGridModel.setSrc("orders");
	    treeGridModel.setChildList("xySheduleTimeEntitys");  
	  
	 // 添加排序字段
	    treeGridModel.setOrder("orders");
	    treeGrids = systemService.treegrid(departList, treeGridModel);  
	    return treeGrids;  
	} 

	/**
	 * 排班列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("xyoa/sheduletime/xySheduleTimeList");
	}

	
	
	/**
	 * 排班列表 客服部跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goCustomer")
	public ModelAndView goCustomer(String sheduletimeId,HttpServletRequest request) {
		XySheduleTimeEntity sheduleTime=systemService.getEntity(XySheduleTimeEntity.class, sheduletimeId);
		OneWeekShedule oneweek=new OneWeekShedule();
			  
		    Calendar end = Calendar.getInstance();  
		    end.setTime(sheduleTime.getEndtime());  
		    Long endTIme = end.getTimeInMillis();  
		  
		    Calendar start = Calendar.getInstance();  
		    start.setTime(sheduleTime.getStarttime());  
		    Long startTime = start.getTimeInMillis();  
		  
		    Long oneDay = 1000 * 60 * 60 * 24l;  
		  
		    Long time = endTIme;  
		    int i=7;
		    while (time >= startTime&&i>=1) {  
		        Date d = new Date(time);  
		        DateFormat df = new SimpleDateFormat("MM月dd号"); 
		        if(i==7)
		        	oneweek.setSun(df.format(d));
		        if(i==6)
		        	oneweek.setSat(df.format(d));
		        if(i==5)
		        	oneweek.setFri(df.format(d));
		        if(i==4)
		        	oneweek.setThu(df.format(d));
		        if(i==3)
		        	oneweek.setWed(df.format(d));
		        if(i==2)
		        	oneweek.setTue(df.format(d));
		        if(i==1)
		        	oneweek.setMon(df.format(d));
		        
		        time -= oneDay; 
		        i--;
		    }  
		
		request.setAttribute("oneweek", oneweek);
		request.setAttribute("sheduletimeId", sheduletimeId);	
		return new ModelAndView("xyoa/sheduletime/customerList");
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
	public void datagrid(XySheduleTimeEntity xySheduleTime,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(XySheduleTimeEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, xySheduleTime, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.xySheduleTimeService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除排班
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(XySheduleTimeEntity xySheduleTime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		xySheduleTime = systemService.getEntity(XySheduleTimeEntity.class, xySheduleTime.getId());
		message = "排班删除成功";
		try{
			xySheduleTimeService.delete(xySheduleTime);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "排班删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除排班
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "排班删除成功";
		try{
			for(String id:ids.split(",")){
				XySheduleTimeEntity xySheduleTime = systemService.getEntity(XySheduleTimeEntity.class, 
				id
				);
				xySheduleTimeService.delete(xySheduleTime);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "排班删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加排班
	 * 
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(XySheduleTimeEntity xySheduleTime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "排班添加成功";

		Calendar cal=Calendar.getInstance();//使用日历类				
		cal.clear(); 
		int year=Integer.parseInt(xySheduleTime.getTimename());
		cal.set(Calendar.YEAR,year); 
		//year年
		
		try{
			xySheduleTime.setOrders(Integer.parseInt(xySheduleTime.getTimename()));
		    xySheduleTime.setTimename(xySheduleTime.getTimename()+"年");	
		    
			String id=(String) xySheduleTimeService.save(xySheduleTime);
			xySheduleTime.setId(id);
			int week=1;	
			int lastday=1;
			for (int i=1;i<=12;i++){
				XySheduleTimeEntity sheduleTime	=new XySheduleTimeEntity();
				sheduleTime.setXySheduleTimeEntity(xySheduleTime);
				sheduleTime.setOrders(i);;
				sheduleTime.setTimename(i+"月");
				
				String idson=(String) xySheduleTimeService.save(sheduleTime);
				sheduleTime.setId(idson);
				
				cal.set(Calendar.MONTH,i-1);				
				cal.set(Calendar.DATE, 1);
				cal.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天  
				int maxday = cal.get(Calendar.DATE);
				int start=1;
				int end=0;
				int newweek=0;
				boolean monthend=false;
				for (int h=1;h<=maxday;h++){//日
					cal.set(Calendar.DATE, h);
					if(week!=cal.get(Calendar.WEEK_OF_YEAR)){	
						if(h==maxday)monthend=true;
							end=h;
						XySheduleTimeEntity sheduleTimeson	=new XySheduleTimeEntity();
						sheduleTimeson.setXySheduleTimeEntity(sheduleTime);
						sheduleTimeson.setOrders(h);
		
						if(h<7)	{	
							if(i==1){
						sheduleTimeson.setTimename("第"+week+"周 ("+year+"."+(i)+"."+lastday+"~"+year+"."+i+"."+end+")");
						sheduleTimeson.setStarttime(new Date(year-1900,(i-1),lastday));
						sheduleTimeson.setEndtime(new Date(year-1900,i-1,end));
							}else
							{
								sheduleTimeson.setTimename("第"+week+"周 ("+year+"."+(i-1)+"."+lastday+"~"+year+"."+i+"."+end+")");
								sheduleTimeson.setStarttime(new Date(year-1900,(i-2),lastday));
								sheduleTimeson.setEndtime(new Date(year-1900,i-1,end));
								
							}
						}
						else{
						sheduleTimeson.setTimename("第"+week+"周 ("+year+"."+i+"."+start+"~"+year+"."+i+"."+end+")");
						sheduleTimeson.setStarttime(new Date(year-1900,i-1,start));
						sheduleTimeson.setEndtime(new Date(year-1900,i-1,end));
						}
						if(i==12&&1==cal.get(Calendar.WEEK_OF_YEAR))
							newweek=week+1;
						week = cal.get(Calendar.WEEK_OF_YEAR); 
						start=h+1;												
						xySheduleTimeService.save(sheduleTimeson);
							
					}
					if(h==maxday&&!monthend){
						
					XySheduleTimeEntity sheduleTimeson	=new XySheduleTimeEntity();
					sheduleTimeson.setXySheduleTimeEntity(sheduleTime);
					sheduleTimeson.setOrders(h);
					if(i==12){
						if(newweek==0)newweek=week;//如果12月30号是星期六
						sheduleTimeson.setTimename("第"+(newweek)+"周 ("+year+"."+i+"."+start+"~"+(year+1)+"."+(1)+"."+(7-maxday+end)+")");
						sheduleTimeson.setStarttime(new Date(year-1900,i-1,start));
						sheduleTimeson.setEndtime(new Date(year+1-1900,0,(7-maxday+end)));
						}
						else{
						sheduleTimeson.setTimename("第"+week+"周 ("+year+"."+i+"."+start+"~"+year+"."+(i+1)+"."+(7-maxday+end)+")");
						sheduleTimeson.setStarttime(new Date(year-1900,i-1,start));
						sheduleTimeson.setEndtime(new Date(year-1900,i,(7-maxday+end)));
						}
					xySheduleTimeService.save(sheduleTimeson);
					lastday=start;
					}
				}
			}
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "排班添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新排班
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(XySheduleTimeEntity xySheduleTime, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "排班更新成功";
		XySheduleTimeEntity t = xySheduleTimeService.get(XySheduleTimeEntity.class, xySheduleTime.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(xySheduleTime, t);
			xySheduleTimeService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "排班更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 排班新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(XySheduleTimeEntity xySheduleTime, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(xySheduleTime.getId())) {
			xySheduleTime = xySheduleTimeService.getEntity(XySheduleTimeEntity.class, xySheduleTime.getId());
			req.setAttribute("xySheduleTimePage", xySheduleTime);
		}
		return new ModelAndView("xyoa/sheduletime/xySheduleTime-add");
	}
	/**
	 * 排班编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(XySheduleTimeEntity xySheduleTime, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(xySheduleTime.getId())) {
			xySheduleTime = xySheduleTimeService.getEntity(XySheduleTimeEntity.class, xySheduleTime.getId());
			req.setAttribute("xySheduleTimePage", xySheduleTime);
		}
		return new ModelAndView("xyoa/sheduletime/xySheduleTime-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","xySheduleTimeController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(XySheduleTimeEntity xySheduleTime,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(XySheduleTimeEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, xySheduleTime, request.getParameterMap());
		List<XySheduleTimeEntity> xySheduleTimes = this.xySheduleTimeService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"排班");
		modelMap.put(NormalExcelConstants.CLASS,XySheduleTimeEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("排班列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,xySheduleTimes);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(XySheduleTimeEntity xySheduleTime,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"排班");
    	modelMap.put(NormalExcelConstants.CLASS,XySheduleTimeEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("排班列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
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
				List<XySheduleTimeEntity> listXySheduleTimeEntitys = ExcelImportUtil.importExcel(file.getInputStream(),XySheduleTimeEntity.class,params);
				for (XySheduleTimeEntity xySheduleTime : listXySheduleTimeEntitys) {
					xySheduleTimeService.save(xySheduleTime);
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
	public List<XySheduleTimeEntity> list() {
		List<XySheduleTimeEntity> listXySheduleTimes=xySheduleTimeService.getList(XySheduleTimeEntity.class);
		return listXySheduleTimes;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		XySheduleTimeEntity task = xySheduleTimeService.get(XySheduleTimeEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody XySheduleTimeEntity xySheduleTime, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<XySheduleTimeEntity>> failures = validator.validate(xySheduleTime);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			xySheduleTimeService.save(xySheduleTime);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = xySheduleTime.getId();
		URI uri = uriBuilder.path("/rest/xySheduleTimeController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody XySheduleTimeEntity xySheduleTime) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<XySheduleTimeEntity>> failures = validator.validate(xySheduleTime);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			xySheduleTimeService.saveOrUpdate(xySheduleTime);
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
		xySheduleTimeService.deleteEntityById(XySheduleTimeEntity.class, id);
	}
}
