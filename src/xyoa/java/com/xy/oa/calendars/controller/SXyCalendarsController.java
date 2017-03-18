package com.xy.oa.calendars.controller;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.xy.oa.calendars.entity.InitCalendars;
import com.xy.oa.calendars.entity.SXyCalendarsEntity;
import com.xy.oa.calendars.service.SXyCalendarsServiceI;

/**   
 * @Title: Controller  
 * @Description: 节假日明细表
 * @author onlineGenerator
 * @date 2016-08-04 23:27:58
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/sXyCalendarsController")
public class SXyCalendarsController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SXyCalendarsController.class);

	@Autowired
	private SXyCalendarsServiceI sXyCalendarsService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	


	/**
	 * 节假日明细表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("xyoa/calendars/sXyCalendarsList");
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
	public void datagrid(SXyCalendarsEntity sXyCalendars,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SXyCalendarsEntity.class, dataGrid);
		//查询条件组装器
		cq.addOrder("calendarday",  SortDirection.asc);
		if(!"".equals(sXyCalendars.getRemarks())&&sXyCalendars.getRemarks()!=null)
		sXyCalendars.setRemarks("*"+sXyCalendars.getRemarks()+"*");
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyCalendars, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyCalendarsService.getDataGridReturn(cq, true);
		
		
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除节假日明细表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(SXyCalendarsEntity sXyCalendars, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		sXyCalendars = systemService.getEntity(SXyCalendarsEntity.class, sXyCalendars.getId());
		message = "节假日明细表删除成功";
		try{
			sXyCalendarsService.delete(sXyCalendars);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "节假日明细表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	 /**
		 * 批量跟新节假日类型明细表
		 * 
		 * @return
		 */
		 @RequestMapping(params = "doBatchUpDayType")
		@ResponseBody
		public AjaxJson doBatchUpDayType(String ids,String daytype,String remarks, HttpServletRequest request){
			String message = null;
			AjaxJson j = new AjaxJson();
			message = "节假日明细表更新成功";
			
			// 得到当前登录用户
			HttpSession session = ContextHolderUtils.getSession();
			TSUser u = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
			Date date=new Date();
			try{
				for(String id:ids.split(",")){
					SXyCalendarsEntity sXyCalendars = systemService.getEntity(SXyCalendarsEntity.class, 
					id
					);
					sXyCalendars.setCalendartype(daytype);
					sXyCalendars.setRemarks(remarks);
					sXyCalendars.setUuser(u.getRealName());
					sXyCalendars.setUtime(date);
					sXyCalendarsService.updateEntitie(sXyCalendars);
					
					systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
				}
			}catch(Exception e){
				e.printStackTrace();
				message = "节假日明细表更新失败";
				throw new BusinessException(e.getMessage());
			}
			j.setMsg(message);
			return j;
		}

	/**
	 * 初始化节假日明细表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(InitCalendars initCalendars, HttpServletRequest request) {
		
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "节假日明细表添加成功";
		
		// 得到当前登录用户
		HttpSession session = ContextHolderUtils.getSession();
		TSUser u = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		
		
		Date calendarday_start=initCalendars.getCalendarday_start();
		Date calendarday_end=initCalendars.getCalendarday_end();
		if (calendarday_start.getTime()>calendarday_end.getTime())//判断开始时间是否大于结束时间
		{
			Date change= calendarday_start;
			calendarday_start=calendarday_end;
			calendarday_end=change;
		}
		sXyCalendarsService.deleteAll(calendarday_start,calendarday_end);//初始化之前将初始化时间段内原有数据删除
		DateFormat df1= new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		try{
			while(calendarday_start.getTime()<=calendarday_end.getTime()){
				SXyCalendarsEntity sXyCalendars =new SXyCalendarsEntity();
				sXyCalendars.setCalendarday(calendarday_start);
				//判断是不是周末
				Calendar calendar=new GregorianCalendar();
				calendar.setTime(calendarday_start);
				if((calendar.get(Calendar.DAY_OF_WEEK) == 7)||(calendar.get(Calendar.DAY_OF_WEEK) == 1)){
					sXyCalendars.setCalendartype("W");
					sXyCalendars.setRemarks("双休日");
				}else {
					sXyCalendars.setCalendartype("B");
					sXyCalendars.setRemarks("工作日");
				}
				sXyCalendars.setCtime(date);
				sXyCalendars.setUtime(date);
				sXyCalendars.setCuser(u.getRealName());
				sXyCalendars.setUuser(u.getRealName());
				sXyCalendarsService.save(sXyCalendars);
				
				
				//使开始日期加一天
				Calendar cal = Calendar.getInstance();
				cal.setTime(calendarday_start);
				cal.add(Calendar.DATE, 1);
				calendarday_start= cal.getTime();
				
			}
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);	
			
		}catch(Exception e){
			e.printStackTrace();
			message = "节假日明细表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 更新节假日明细表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(SXyCalendarsEntity sXyCalendars, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "节假日明细表更新成功";
		// 得到当前登录用户
		HttpSession session = ContextHolderUtils.getSession();
		TSUser u = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		sXyCalendars.setUuser(u.getRealName());
		Date date=new Date();
		sXyCalendars.setUtime(date);
		SXyCalendarsEntity t = sXyCalendarsService.get(SXyCalendarsEntity.class, sXyCalendars.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(sXyCalendars, t);
			sXyCalendarsService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "节假日明细表更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 节假日明细表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(SXyCalendarsEntity sXyCalendars, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCalendars.getId())) {
			sXyCalendars = sXyCalendarsService.getEntity(SXyCalendarsEntity.class, sXyCalendars.getId());
			req.setAttribute("sXyCalendarsPage", sXyCalendars);
		}
		return new ModelAndView("xyoa/calendars/sXyCalendars-init");
	}
	/**
	 * 节假日明细表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(SXyCalendarsEntity sXyCalendars, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCalendars.getId())) {
			sXyCalendars = sXyCalendarsService.getEntity(SXyCalendarsEntity.class, sXyCalendars.getId());
			req.setAttribute("sXyCalendarsPage", sXyCalendars);
		}
		return new ModelAndView("xyoa/calendars/sXyCalendars-update");
	}
	

	/**
	 * 节假日明细表批量编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "GoSetAllDayType")
	public ModelAndView GoSetAllDayType(String ids, HttpServletRequest req) {
			req.setAttribute("ids", ids);
		
		return new ModelAndView("xyoa/calendars/sXyCalendars-gosetalldaytype");
	}
	
	
	

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<SXyCalendarsEntity> list() {
		List<SXyCalendarsEntity> listSXyCalendarss=sXyCalendarsService.getList(SXyCalendarsEntity.class);
		return listSXyCalendarss;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		SXyCalendarsEntity task = sXyCalendarsService.get(SXyCalendarsEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody SXyCalendarsEntity sXyCalendars, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyCalendarsEntity>> failures = validator.validate(sXyCalendars);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyCalendarsService.save(sXyCalendars);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = sXyCalendars.getId();
		URI uri = uriBuilder.path("/rest/sXyCalendarsController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody SXyCalendarsEntity sXyCalendars) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyCalendarsEntity>> failures = validator.validate(sXyCalendars);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyCalendarsService.saveOrUpdate(sXyCalendars);
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
		sXyCalendarsService.deleteEntityById(SXyCalendarsEntity.class, id);
	}
}
