package com.xy.oa.sheduletime.controller;
import com.xy.oa.sheduletime.entity.OneWeekShedule;
import com.xy.oa.sheduletime.entity.Shedulejson;
import com.xy.oa.sheduletime.entity.XySheduleTimeEntity;
import com.xy.oa.sheduletime.entity.XyWorkScheduleEntity;
import com.xy.oa.sheduletime.service.XySheduleTimeServiceI;
import com.xy.oa.sheduletime.service.XyWorkScheduleServiceI;
import com.xy.oa.staff.entity.StaffEntity;
import com.xy.oa.staff.service.StaffServiceI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
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
import org.jeecgframework.core.util.ResourceUtil;
import java.io.IOException;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Map;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.JSONHelper;
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
 * @Description: xy_work_schedule
 * @author onlineGenerator
 * @date 2016-09-17 10:51:03
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/xyWorkScheduleController")
public class XyWorkScheduleController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(XyWorkScheduleController.class);
	@Autowired
	private StaffServiceI staffService;
	@Autowired
	private XyWorkScheduleServiceI xyWorkScheduleService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private XySheduleTimeServiceI xySheduleTimeService;

	
	/**
	 * 排班的修改增加
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(params = "updateSchedule")
	@ResponseBody
	public AjaxJson updateSchedule( String rowstr , String sheduletimeId,HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "排班修改成功";	 
		 XySheduleTimeEntity sheduleTime=systemService.getEntity(XySheduleTimeEntity.class, sheduletimeId);	
		 	Calendar end = Calendar.getInstance();  
		    end.setTime(sheduleTime.getEndtime());  
		    Long endTIme = end.getTimeInMillis();  		  
		    Calendar start = Calendar.getInstance();  
		    start.setTime(sheduleTime.getStarttime());  
		    Long startTime = start.getTimeInMillis();  		  
		    Long oneDay = 1000 * 60 * 60 * 24l;  
		    
		    HttpSession session = ContextHolderUtils.getSession();
			TSUser u = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		List<OneWeekShedule> list=JSONHelper.toList(rowstr, OneWeekShedule.class);
		try{
			String hql = "from XyWorkScheduleEntity ws where ws.staffId=? and ws.scheduleDay=?";
			 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			List< XyWorkScheduleEntity> workshedulelist;			
			 for(int h=0;h<list.size();h++){
						  
				    Long time = endTIme; 	
				   for(int i=7;time>=startTime&&i>=1;i--) {  
				    	    XyWorkScheduleEntity shedule =new XyWorkScheduleEntity();
					        Date d = new Date(time); 
					        shedule.setScheduleDay(df.format(d));
					        shedule.setStaffId(list.get(h).getStaffid());
					        shedule.setUserId(staffService.getUserByUsername(list.get(h).getStaffid()+"").getId());
					       //设置部门id
					        TSDepart deptbyname= staffService.getDepartByName("客服组");
					        if(deptbyname!=null)shedule.setDeptId(deptbyname.getId());					        
					        workshedulelist=xyWorkScheduleService.findHql(hql, list.get(h).getStaffid(),df.format(d)); 
					        //修改人修改时间与创建人创建时间的设定
								if(workshedulelist!=null&&workshedulelist.size()>0)	{						
								shedule=workshedulelist.get(0);
								shedule.setUTime(new Date());
						        shedule.setUUser(Integer.parseInt(u.getUserName()));
								}else{
									shedule.setCTime(new Date());
							        shedule.setCUser(Integer.parseInt(u.getUserName()));
								}
							//修改一周某天	
								if(i==7)
									shedule.setScheduleType(list.get(h).getSun());
								if(i==6)
									shedule.setScheduleType(list.get(h).getSat());
								if(i==5)
									shedule.setScheduleType(list.get(h).getFri());
								if(i==4)		
									shedule.setScheduleType(list.get(h).getThu());
								if(i==3)
									shedule.setScheduleType(list.get(h).getWed());
								if(i==2)
									shedule.setScheduleType(list.get(h).getTue());
								if(i==1)
									shedule.setScheduleType(list.get(h).getMon());
							
								systemService.saveOrUpdate(shedule);
					        time -= oneDay; 
					        
					    }
				 
			 }
		systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "排班修改失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	/**
	 * 排班显示
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "getSchedulejson")
	@ResponseBody
	public Shedulejson getSchedulejson(String sheduletimeId, HttpServletRequest request) {
		Shedulejson shedulejson=new Shedulejson();
		XySheduleTimeEntity sheduleTimeEntity =xySheduleTimeService.getEntity(XySheduleTimeEntity.class, sheduletimeId);
		TSDepart deptbyname= staffService.getDepartByName("客服组");
		List<StaffEntity> stafflist=staffService.getStaffByDept(deptbyname.getId());
		if(sheduleTimeEntity!=null)
		{
			if(stafflist!=null&&stafflist.size()>0)
			{	OneWeekShedule[] oneweeks=new  OneWeekShedule[stafflist.size()];
				for(int i=0;i<stafflist.size();i++){
					OneWeekShedule oneweek=new OneWeekShedule();
					oneweek.setStaffid(stafflist.get(i).getSttaffId());
					oneweek.setStaffname(stafflist.get(i).getName());	
					String hql = "from XyWorkScheduleEntity ws where ws.staffId=? and ws.scheduleDay>=? and ws.scheduleDay<=? order by ws.scheduleDay desc";
					 DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
					List<XyWorkScheduleEntity>  workshedules=xyWorkScheduleService.findHql(hql, stafflist.get(i).getSttaffId(),df.format(sheduleTimeEntity.getStarttime()),df.format(sheduleTimeEntity.getEndtime()));
				if(workshedules!=null){
				for(int h=0;h<workshedules.size();h++)
				{	
					if(h==0)
						oneweek.setSun(workshedules.get(h).getScheduleType());
			        if(h==1)
			         	oneweek.setSat(workshedules.get(h).getScheduleType());
			        if(h==2)
			        		oneweek.setFri(workshedules.get(h).getScheduleType());
			        if(h==3)
			        	oneweek.setThu(workshedules.get(h).getScheduleType());
			        if(h==4)			        	
			        	oneweek.setWed(workshedules.get(h).getScheduleType());
			        if(h==5)
			        	oneweek.setTue(workshedules.get(h).getScheduleType());
			        if(h==6)
			        	oneweek.setMon(workshedules.get(h).getScheduleType());
				}
				
				oneweeks[i]=oneweek;
				}
				}
				shedulejson.setRows(oneweeks);
			}
			
			
		}
		return shedulejson;
	}
	/**
	 * xy_work_schedule列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("xyoa/sheduletime/xyWorkScheduleList");
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
	public void datagrid(XyWorkScheduleEntity xyWorkSchedule,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(XyWorkScheduleEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, xyWorkSchedule, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.xyWorkScheduleService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除xy_work_schedule
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(XyWorkScheduleEntity xyWorkSchedule, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		xyWorkSchedule = systemService.getEntity(XyWorkScheduleEntity.class, xyWorkSchedule.getId());
		message = "xy_work_schedule删除成功";
		try{
			xyWorkScheduleService.delete(xyWorkSchedule);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "xy_work_schedule删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除xy_work_schedule
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "xy_work_schedule删除成功";
		try{
			for(String id:ids.split(",")){
				XyWorkScheduleEntity xyWorkSchedule = systemService.getEntity(XyWorkScheduleEntity.class, 
				id
				);
				xyWorkScheduleService.delete(xyWorkSchedule);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "xy_work_schedule删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加xy_work_schedule
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(XyWorkScheduleEntity xyWorkSchedule, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "xy_work_schedule添加成功";
		try{
			xyWorkScheduleService.save(xyWorkSchedule);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "xy_work_schedule添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新xy_work_schedule
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(XyWorkScheduleEntity xyWorkSchedule, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "xy_work_schedule更新成功";
		XyWorkScheduleEntity t = xyWorkScheduleService.get(XyWorkScheduleEntity.class, xyWorkSchedule.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(xyWorkSchedule, t);
			xyWorkScheduleService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "xy_work_schedule更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * xy_work_schedule新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(XyWorkScheduleEntity xyWorkSchedule, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(xyWorkSchedule.getId())) {
			xyWorkSchedule = xyWorkScheduleService.getEntity(XyWorkScheduleEntity.class, xyWorkSchedule.getId());
			req.setAttribute("xyWorkSchedulePage", xyWorkSchedule);
		}
		return new ModelAndView("xyoa/sheduletime/xyWorkSchedule-add");
	}
	/**
	 * xy_work_schedule编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(XyWorkScheduleEntity xyWorkSchedule, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(xyWorkSchedule.getId())) {
			xyWorkSchedule = xyWorkScheduleService.getEntity(XyWorkScheduleEntity.class, xyWorkSchedule.getId());
			req.setAttribute("xyWorkSchedulePage", xyWorkSchedule);
		}
		return new ModelAndView("xyoa/sheduletime/xyWorkSchedule-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","xyWorkScheduleController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(XyWorkScheduleEntity xyWorkSchedule,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(XyWorkScheduleEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, xyWorkSchedule, request.getParameterMap());
		List<XyWorkScheduleEntity> xyWorkSchedules = this.xyWorkScheduleService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"xy_work_schedule");
		modelMap.put(NormalExcelConstants.CLASS,XyWorkScheduleEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("xy_work_schedule列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,xyWorkSchedules);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(XyWorkScheduleEntity xyWorkSchedule,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"xy_work_schedule");
    	modelMap.put(NormalExcelConstants.CLASS,XyWorkScheduleEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("xy_work_schedule列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
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
				List<XyWorkScheduleEntity> listXyWorkScheduleEntitys = ExcelImportUtil.importExcel(file.getInputStream(),XyWorkScheduleEntity.class,params);
				for (XyWorkScheduleEntity xyWorkSchedule : listXyWorkScheduleEntitys) {
					xyWorkScheduleService.save(xyWorkSchedule);
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
	public List<XyWorkScheduleEntity> list() {
		List<XyWorkScheduleEntity> listXyWorkSchedules=xyWorkScheduleService.getList(XyWorkScheduleEntity.class);
		return listXyWorkSchedules;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		XyWorkScheduleEntity task = xyWorkScheduleService.get(XyWorkScheduleEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody XyWorkScheduleEntity xyWorkSchedule, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<XyWorkScheduleEntity>> failures = validator.validate(xyWorkSchedule);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			xyWorkScheduleService.save(xyWorkSchedule);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = xyWorkSchedule.getId();
		URI uri = uriBuilder.path("/rest/xyWorkScheduleController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody XyWorkScheduleEntity xyWorkSchedule) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<XyWorkScheduleEntity>> failures = validator.validate(xyWorkSchedule);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			xyWorkScheduleService.saveOrUpdate(xyWorkSchedule);
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
		xyWorkScheduleService.deleteEntityById(XyWorkScheduleEntity.class, id);
	}
}
