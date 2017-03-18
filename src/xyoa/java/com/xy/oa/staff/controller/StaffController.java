package com.xy.oa.staff.controller;
	import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.model.json.ValidForm;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.PasswordUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSRole;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.xy.oa.deptjob.entity.DeptJobEntity;
import com.xy.oa.deptjob.service.DeptJobServiceI;
import com.xy.oa.staff.entity.DllDepart;
import com.xy.oa.staff.entity.StaffEntity;
import com.xy.oa.staff.entity.StaffEntityendtrial;
import com.xy.oa.staff.service.StaffServiceI;
import com.xy.oa.staffeducation.entity.StaffEducationEntity;
import com.xy.oa.staffhomemember.entity.SXyStaffHomeMemberEntity;
import com.xy.oa.stafftrain.entity.SXyStaffTrainEntity;
import com.xy.oa.staffworkexperience.entity.StaffWorkExperienceEntity;
import com.xy.oa.util.Constants;
import com.xy.oa.util.StaffHtmlConstants;

/**   
 * @Title: Controller  
 * @Description: 享宇员工表
 * @author xiaoyong
 * @date 2016-08-10 15:59:20
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/staffController")
public class StaffController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(StaffController.class);

	@Autowired
	private StaffServiceI staffService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private DeptJobServiceI deptJobService;
	
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	/**
	 * 享宇员工表列表 页面跳转
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request,String state) {
		TSUser tsUser = ResourceUtil.getSessionUserName();
		String rHql = "select ru.TSRole.roleCode from TSRoleUser ru where ru.TSUser = ?";
		List<String> roleCodes = this.deptJobService.findHql(rHql, tsUser);
		String isQueryAll = "false";
		for(String newRoleCode : roleCodes){
			if (Constants.HR.equals(newRoleCode) || Constants.HRDM.equals(newRoleCode) || 
					Constants.VICE.equals(newRoleCode) || Constants.CEO.equals(newRoleCode) || Constants.ADMIN.equals(newRoleCode)) {
				isQueryAll = "true";
			}
		}
		
		request.setAttribute("isQueryAll", isQueryAll);
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
		 
		 request.setAttribute("replacedepart", staffService.getTSDepartAllStr());
		 
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
		if("leave".equals(state))
		return new ModelAndView("xyoa/staff/leavestaffList");
		else
		return new ModelAndView("xyoa/staff/staffList");
	}

	
	
	/**
	 * 享宇员工表列表 页面跳转 
	 * 选择内部推荐人时使用
	 * @return
	 */
	@RequestMapping(params = "listforReference")
	public ModelAndView listforReference(HttpServletRequest request) {
		request.setAttribute("replace", staffService.getTSDepartAllStr());		
		return new ModelAndView("xyoa/staff/listforReference");
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
	public void datagrid(StaffEntity staff,HttpServletRequest request, HttpServletResponse response, String staffstate,DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(StaffEntity.class, dataGrid);
		TSUser tsUser = ResourceUtil.getSessionUserName();
		//查询当前用户的角色编码
		String rHql = "select ru.TSRole.roleCode from TSRoleUser ru where ru.TSUser = ?";
		List<String> roleCodes = staffService.findHql(rHql, tsUser);
		
		boolean isQueryAll = false;
		for(String newRoleCode : roleCodes){
			if (Constants.HR.equals(newRoleCode) || Constants.HRDM.equals(newRoleCode) || 
					Constants.VICE.equals(newRoleCode) || Constants.CEO.equals(newRoleCode) || Constants.ADMIN.equals(newRoleCode)) {
				isQueryAll = true;
			}
		}
		
		if(!isQueryAll){
			cq.in("deptId", staffService.getSSDeptId(tsUser.getCurrentDepart().getId()).toArray());
		}
		
		if(StringUtil.isNotEmpty(staff.getDeptId())){
			cq.in("deptId", staffService.getSSDeptId(staff.getDeptId()).toArray());
			staff.setDeptId(null); 

		}
		
		if("leave".equals(staffstate))cq.eq("state", "0");
		else
			cq.between("state", "1", "7");
		 cq.addOrder("sttaffId", SortDirection.asc);
		//查询条件组装器
		if(!"".equals(staff.getName())&&staff.getName()!=null)
			staff.setName("*"+staff.getName()+"*");
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
	 * 删除享宇员工表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(StaffEntity staff, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		staff = systemService.getEntity(StaffEntity.class, staff.getId());
		if(!"".equals(staff.getId())&&staff.getId()!=null){
			String username=""+staff.getSttaffId();
			if("999999".equals(username)){
				message = "超级管理员[999999]不可删除";
				j.setMsg(message);
				return j;
				}
			else
			{
				message = "享宇员工表删除成功";
				try{
					staffService.delete(staff);
					this.deluser(staff);
					systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
				}catch(Exception e){
					e.printStackTrace();
					message = "享宇员工表删除失败";
					throw new BusinessException(e.getMessage());
				}		
			}			
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除享宇员工表
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇员工表删除成功";
		try{
			for(String id:ids.split(",")){
				StaffEntity staff = new StaffEntity();
				staff.setId(id);
				this.doDel(staff, request);
				//staffService.delete(staff);
				//删除用户表
				//this.deluser(staff);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇员工表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	 
	 /**
		 * 新增页面中的ajax 二级 联动 验证考勤id唯一
		 * 
		 * @return
	 * @throws ParseException 
		 */
		@RequestMapping(params = "getcheck")
		@ResponseBody
		public StaffEntity getCheck(String checkid, HttpServletRequest req) throws ParseException {
			
			StaffEntity staffAge=new StaffEntity();   			 
			if(!"".equals(checkid)&&checkid!=null){
			int checkId=	Integer.parseInt(checkid);
			 StaffEntity staff = staffService.getStaffByCheckId(checkId);
			if(staff!=null)
			staffAge.setCheckId( staff.getCheckId());
			}
			return staffAge;
		}
		
		
		 /**
		 * 首页中的ajax 转正提醒
		 * 
		 * @return
	 * @throws ParseException 
		 */
		@RequestMapping(params = "getTrialEndData")
		@ResponseBody
		public List<StaffEntityendtrial> getTrialEndData( HttpServletRequest req) throws ParseException {
			List<StaffEntity> staffendtriallist=null;
			// 得到当前登录用户
			HttpSession session = ContextHolderUtils.getSession();
			TSUser u = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
			
			//获得角色
			String hql = "from TSRoleUser ru where ru.TSUser.id= ? and ru.TSRole.roleCode in ( 'hr',  'hrdm', 'vice','ceo','admin')";		
		    List<TSRoleUser> roles = staffService.findHql(hql, u.getId());
		    if(roles!=null&&roles.size()>0)	//是hr 	    
		     staffendtriallist = staffService.getTrialEndStaff();
		   else 
		    {
		    	hql = "from TSRoleUser ru where ru.TSUser.id= ? and ru.TSRole.roleCode in ( 'headman',  'dm')";		
			    roles = staffService.findHql(hql, u.getId());
			    if(roles!=null&&roles.size()>0)//是dm 
			    {   
			    	StaffEntity st=new StaffEntity();
			    	if(u.getUserName()!=null&&!"".equals(u.getUserName())){
			    	st=staffService.getStaffByUsername(Integer.parseInt(u.getUserName()));
			    	}
			    	 staffendtriallist = staffService.getTrialEndStaff(st.getDeptId());
			    }
			    else return null;
		    }
		    
		    //翻译部门名称和试用期结束格式
			List<StaffEntityendtrial> staffendtriallist1 =new ArrayList<>();
			if(staffendtriallist!=null&& staffendtriallist.size()>0){
			for(int i=0;i<staffendtriallist.size();i++)
			{
				StaffEntityendtrial s=new StaffEntityendtrial();
				s.setName(staffendtriallist.get(i).getName());
				if(staffendtriallist.get(i).getDeptId()!=null)
				{
					TSDepart dept=staffService.getEntity(TSDepart.class, staffendtriallist.get(i).getDeptId());
					if(dept!=null){
						s.setDeptId(dept.getDepartname());
					}
						
				}else staffendtriallist.get(i).setDeptId("");
				SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
				s.setCertNo(sf.format(staffendtriallist.get(i).getTrialEndData()));
				staffendtriallist1.add(s);
			}
				
			}
			return staffendtriallist1;
		}
		 /**
		 * 新增页面中的ajax 二级 联动
		 * 
		 * @return
	 * @throws ParseException 
		 */
		@RequestMapping(params = "getSiling")
		@ResponseBody
		public int  getSiling(String trialStartData, HttpServletRequest req) throws ParseException {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date trialStartDate=null;
			int siling=0;
			if(trialStartData!=null&&!"".equals(trialStartData))
				{trialStartDate=format.parse(trialStartData);
				Date nowdate=new Date();
				if(nowdate.getTime()>trialStartDate.getTime()){
				Calendar c1 = Calendar.getInstance();
				Calendar c2 = Calendar.getInstance();

				c1.setTime(trialStartDate);
				c2.setTime(nowdate);
				;
				siling = (c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR))*12+c2.get(Calendar.MONDAY)-c1.get(Calendar.MONTH);
				return siling == 0 ? 1 :new Double( Math.ceil(siling)).intValue();
				}
				
				
				}
			
			return siling ;		
			
		}

	 /**
		 * 新增页面中的ajax 二级 联动
		 * 
		 * @return
	 * @throws ParseException 
		 */
		@RequestMapping(params = "getAge")
		@ResponseBody
		public StaffEntity getAge(String certno, HttpServletRequest req) throws ParseException {
			
			StaffEntity staffAge=new StaffEntity();
			staffAge.setAge(0);
			staffAge.setBirthday(new Date());
			int age=0;	
			if(!"".equals(certno)&&certno!=null){
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			String age1;
			if(certno.length()==15)
				age1="19"+certno.substring(6, 12);	
			else age1 =certno.substring(6, 14);
		    String bir=	age1.substring( 0, 4)+"-"+age1.substring( 4, 6)+"-"+age1.substring( 6, 8);
		    staffAge.setName(bir);
			Date start=format.parse(age1);
			Date end=new Date();
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(start);
			c2.setTime(end);
			int year1 = c1.get(Calendar.YEAR);
			int year2 = c2.get(Calendar.YEAR);
			age=year2-year1;
			staffAge.setAge(age);
			
			staffAge.setBirthday( start);
			}
			return staffAge;
		}

	/**
	 * 添加享宇员工表
	 * 
	 * @param ids
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(StaffEntity staff, HttpServletRequest request) throws ParseException {
		
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇员工表添加成功";
		
		//设置员工编码
		int staffid=staffService.getMaxStaffidByCompanyId(staff.getCompanyId());
		if(staffid!=0){
			staff.setSttaffId(staffid+1);
		}else{
			TSDepart company= staffService.getEntity(TSDepart.class, staff.getCompanyId());
			
			if("北京享宇金融服务外包有限公司".equals(company.getDepartname())){				
				staff.setSttaffId(100001);	
			}
				
			else if("上海享宇投资管理有限公司".equals(company.getDepartname())){
				staff.setSttaffId(110001);	
			}
			else if("深圳市享宇金融服务有限公司".equals(company.getDepartname())){
				staff.setSttaffId(120001);	
			}
			else if("四川享宇金信金融服务外包有限公司".equals(company.getDepartname())){
				staff.setSttaffId(130001);	
			}
			else if("拓保".equals(company.getDepartname())){
				staff.setSttaffId(210001);	
			}
			else if("沐融".equals(company.getDepartname())){
				staff.setSttaffId(220001);	
			}
			else {
				staff.setSttaffId(230001); 
			}
		}
		
		// 得到当前登录用户
		HttpSession session = ContextHolderUtils.getSession();
		TSUser u = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		Date date =new Date();
		//设置创建人  、创建时间
		staff.setCreateTime(date);
		int createid =-1;
		if(!"".equals(u.getUserName())&&u.getUserName()!=null) 
			createid =Integer.parseInt( u.getUserName());
		//设置年龄.出生日期
		if(!"".equals(staff.getCertNo())&&staff.getCertNo()!=null) 
		{
			//在前台处理
		}else{
			staff.setAge(0);
			staff.setBirthday( date);
		}
		//设置工作年限
				if(!"".equals(staff.getGraduationDate())&&staff.getGraduationDate()!=null) 
				{					
					Date end=new Date();
					Calendar c1 = Calendar.getInstance();
					Calendar c2 = Calendar.getInstance();
					c1.setTime(staff.getGraduationDate());
					c2.setTime(end);
					int year1 = c1.get(Calendar.YEAR);
					int year2 = c2.get(Calendar.YEAR);
					if((year2-year1)>=0)
					staff.setWorkYear(year2-year1);
					else staff.setWorkYear(0);
				}else{
					staff.setWorkYear(0);
				}
				if(staff.getGoXyDate()==null)
					staff.setGoXyDate(date);
		staff.setCreateUser(createid);
		staff.setUpudateTime(date);
		staff.setUpdateUer(createid);
		if(staff.getOffWorkCount()==null)
		staff.setOffWorkCount(0.0);
		if(staff.getLeaveCount()==null)
		staff.setLeaveCount(0.0);
		try{
			staffService.save(staff);
			
			 this.saveUser(request,staff);
			 
			//设置锁定状态
				TSUser user= staffService.getUserByUsername(staff.getSttaffId()+"");
				if("0".equals(staff.getState()))
					this.lock(user.getId(), "0");// 锁定账户
				else 
					this.lock(user.getId(), "1");//激活
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "享宇员工表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
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
			//跟新员工表
			this.updateUser(request,t);
			//设置锁定状态
			TSUser user= staffService.getUserByUsername(t.getSttaffId()+"");
			if("0".equals(staff.getState()))
				this.lock(user.getId(), "0");// 锁定账户
			else 
				this.lock(user.getId(), "1");//激活
			
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
	 * 享宇员工表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(StaffEntity staff, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(staff.getId())) {
			staff = staffService.getEntity(StaffEntity.class, staff.getId());
			req.setAttribute("staffPage", staff);
		}
		List<TSDepart> company = staffService.getAllCompany();
		req.setAttribute("TSDeparts", company);
		return new ModelAndView("xyoa/staff/staff-add");
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
			return new ModelAndView("xyoa/staff/detailStaff");
		else
			return new ModelAndView("xyoa/staff/staff-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","staffController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(StaffEntity staff,String staffstate,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) throws IllegalAccessException, InvocationTargetException {
		CriteriaQuery cq = new CriteriaQuery(StaffEntity.class, dataGrid);
		if("leave".equals(staffstate))cq.eq("state", "0");
		else
			cq.between("state", "1", "7");
		 cq.addOrder("createTime", SortDirection.asc);
		
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, staff, request.getParameterMap());
		List<StaffEntity> staffs = this.staffService.getListByCriteriaQuery(cq,false);
		List<StaffEntity> stafflist=new ArrayList<>();
		if(staffs!=null&&staffs.size()>0){
			for(StaffEntity sta:staffs){
			StaffEntity s=new StaffEntity();
			org.apache.commons.beanutils.BeanUtils.copyProperties(s,sta);
			TSDepart company=systemService.getEntity(TSDepart.class,s.getCompanyId());
			if(company!=null)s.setCompanyId(company.getDepartname());
			DeptJobEntity job=systemService.getEntity(DeptJobEntity.class,s.getJobNo());
			if(job!=null)s.setJobNo(job.getJobName());
			TSDepart d=systemService.getEntity(TSDepart.class,s.getDeptId());
			if(d!=null)s.setDeptId(d.getDepartname());
			stafflist.add(s);
			}
		}
		modelMap.put(NormalExcelConstants.FILE_NAME,"享宇员工表");
		modelMap.put(NormalExcelConstants.CLASS,StaffEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("享宇员工表列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,stafflist);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	
	
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(StaffEntity staff,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"享宇员工表");
    	modelMap.put(NormalExcelConstants.CLASS,StaffEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("享宇员工表列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
    	"导出信息"));
    	modelMap.put(NormalExcelConstants.DATA_LIST,new ArrayList());
    	return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	

	@RequestMapping(params = "importExcel")
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
				List<StaffEntity> listStaffEntitys = ExcelImportUtil.importExcel(file.getInputStream(),StaffEntity.class,params);
				for (StaffEntity staff : listStaffEntitys) {
					//得到公司
					 TSDepart company=null;
					TSDepart companybyid= staffService.getEntity(TSDepart.class, staff.getCompanyId());
					TSDepart companybyname= staffService.getDepartByName(staff.getCompanyId());
					if(companybyid==null&&companybyname==null){//如果公司不存在，导入失败。
						staff.setCompanyId("");
					    j.setMsg("文件导入失败！归属公司不能为空！");
					}else{ 
						if (companybyname==null) company=companybyid;
						 else company=companybyname;				
					if(company!=null)staff.setCompanyId(company.getId());
					//设置部门id 
					TSDepart dept= staffService.getEntity(TSDepart.class, staff.getDeptId());
					if(dept==null){
						
					//	TSDepart deptdepart=staffService.getDepartByUpdeparts(company, staff.getDeptId());
					//	if(deptdepart!=null &&!"".equals(deptdepart))staff.setDeptId(deptdepart.getId());
						TSDepart deptyname= staffService.getDepartByName(staff.getDeptId());			
						if	(deptyname!=null &&!"".equals(deptyname))staff.setDeptId(deptyname.getId());
						else staff.setDeptId("");
					}
					//设置岗位
					DeptJobEntity deptjob=systemService.getEntity(DeptJobEntity.class, staff.getJobNo());
					if(deptjob==null){
					String hql = "from DeptJobEntity dj where dj.jobName=?";		
					List<DeptJobEntity> list= systemService.findHql(hql,staff.getJobNo());
					if(list!=null&&list.size()>0)deptjob=list.get(0);
					}
					if(deptjob!=null)
					staff.setJobNo(deptjob.getId());
					// 得到当前登录用户
					HttpSession session = ContextHolderUtils.getSession();
					TSUser u = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
					Date date =new Date();
					//设置创建人  、创建时间
					staff.setCreateTime(date);
					int createid =-1;
					if(!"".equals(u.getUserName())&&u.getUserName()!=null) 
						createid =Integer.parseInt( u.getUserName());
					//设置年龄.出生日期
					if(!"".equals(staff.getCertNo())&&staff.getCertNo()!=null) 
					{
						int age=0;			
						SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
						String age1 =staff.getCertNo().substring(6, 14);
						Date start=format.parse(age1);
						Date end=new Date();
						Calendar c1 = Calendar.getInstance();
						Calendar c2 = Calendar.getInstance();
						c1.setTime(start);
						c2.setTime(end);
						int year1 = c1.get(Calendar.YEAR);
						int year2 = c2.get(Calendar.YEAR);
						age=year2-year1;	
						if(age>=0)
						staff.setAge(age);
						else staff.setAge(0);
						staff.setBirthday( start);
					}else{
						staff.setAge(0);
						staff.setBirthday( date);
					}
					//根据试用期设置试用结束日期
					if(!"".equals(staff.getTrialStartData())&&staff.getTrialStartData()!=null&&staff.getTrialEndData()==null)
						{						
						Calendar cl = Calendar.getInstance();  
				        cl.setTime(staff.getTrialStartData());  
				        cl.add(Calendar.MONTH, 3);  				     
				        staff.setTrialEndData( cl.getTime());
						}
					//设置工作年限
					if(!"".equals(staff.getGraduationDate())&&staff.getGraduationDate()!=null) 
							{					
								Date end=new Date();
								Calendar c1 = Calendar.getInstance();
								Calendar c2 = Calendar.getInstance();
								c1.setTime(staff.getGraduationDate());
								c2.setTime(end);
								int year1 = c1.get(Calendar.YEAR);
								int year2 = c2.get(Calendar.YEAR);
								if((year2-year1)>=0)
									staff.setWorkYear(year2-year1);
								else staff.setWorkYear(0);
								
							}else{
								staff.setWorkYear(0);
							}
					
					staff.setCreateUser(createid);
					staff.setUpudateTime(date);
					staff.setUpdateUer(createid);
				
					//设置角色  （默认普通员工）
					request.setAttribute("roleid", "297ed504567c959401567cc1beb80001");
					
					
					//保存员工
					if(staff.getSttaffId()==null||staff.getSttaffId()==0){
						if("北京享宇金融服务外包有限公司".equals(company.getDepartname()))staff.setSttaffId(100001);	
						else if("上海享宇投资管理有限公司".equals(company.getDepartname()))staff.setSttaffId(110001);	
						else if("深圳市享宇金融服务有限公司".equals(company.getDepartname()))staff.setSttaffId(120001);	
						else if("四川享宇金信金融服务外包有限公司".equals(company.getDepartname()))staff.setSttaffId(130001);	
						else if("拓保".equals(company.getDepartname()))staff.setSttaffId(210001);	
						else if("沐融".equals(company.getDepartname()))staff.setSttaffId(220001);	
						else staff.setSttaffId(230001); 
						staffService.save(staff);
						
						
					
					}else{
						
						String hql  = "from StaffEntity s where s.sttaffId=?";		
						List<StaffEntity> list= systemService.findHql(hql,staff.getSttaffId());
						if(list!=null&&list.size()>0&&list.get(0)!=null){//如果通过staffid 员工存在
							StaffEntity staffold=list.get(0);
							staff.setId(staffold.getId());
							//org.apache.commons.beanutils.BeanUtils.copyProperties(staffold,staff);
							org.jeecgframework.core.util.MyBeanUtils.copyBeanNotNull2Bean(staff,staffold);
							staffService.updateEntitie(staffold);  //跟新
						}else { //员工不存在，保存
							//自动设置员工编码
							int staffid=staffService.getMaxStaffidByCompanyId(staff.getCompanyId());
								if(staffid==0)staffid=130000;
								/*{
									if("北京享宇金融服务外包有限公司".equals(company.getDepartname()))staff.setSttaffId(100001);	
									else if("上海享宇投资管理有限公司".equals(company.getDepartname()))staff.setSttaffId(110001);	
									else if("深圳市享宇金融服务有限公司".equals(company.getDepartname()))staff.setSttaffId(120001);	
									else if("四川享宇金信金融服务外包有限公司".equals(company.getDepartname()))staff.setSttaffId(130001);	
									else if("拓保".equals(company.getDepartname()))staff.setSttaffId(210001);	
									else if("沐融".equals(company.getDepartname()))staff.setSttaffId(220001);	
									else staff.setSttaffId(230001);
								}else*/
								if(staff.getSttaffId()==null)								
								{
									staff.setSttaffId(staffid+1);
								}
							staffService.save(staff);
						}
					}
					//保存用户
					this.saveUserByImport(request,staff);
					//设置锁定状态
					TSUser user= staffService.getUserByUsername(staff.getSttaffId()+"");
					if("0".equals(staff.getState()))
						this.lock(user.getId(), "0");// 锁定账户
					else 
						this.lock(user.getId(), "1");//激活
					}
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
	
	
	//usercontroller部分，在用户表增删改的时候同步用户表
	

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	
	public String changepassword(HttpServletRequest request) {
		TSUser user = ResourceUtil.getSessionUserName();
		request.setAttribute("user", user);
		return "system/user/changepassword";
	}
	
	

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	
	public AjaxJson savenewpwd(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		TSUser user = ResourceUtil.getSessionUserName();
		String password = oConvertUtils.getString(request.getParameter("password"));
		String newpassword = oConvertUtils.getString(request.getParameter("newpassword"));
		String pString = PasswordUtil.encrypt(user.getUserName(), password, PasswordUtil.getStaticSalt());
		if (!pString.equals(user.getPassword())) {
			j.setMsg("原密码不正确");
			j.setSuccess(false);
		} else {
			try {
				user.setPassword(PasswordUtil.encrypt(user.getUserName(), newpassword, PasswordUtil.getStaticSalt()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			systemService.updateEntitie(user);
			j.setMsg("修改成功");

		}
		return j;
	}
	

	/**
	 * 
	 * 修改用户密码
	 * @author Chj
	 */
	
	
	public ModelAndView changepasswordforuser(TSUser user, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(user.getId())) {
			user = systemService.getEntity(TSUser.class, user.getId());
			req.setAttribute("user", user);
			idandname(req, user);
			//System.out.println(user.getPassword()+"-----"+user.getRealName());
		}
		return new ModelAndView("system/user/adminchangepwd");
	}
	
	
	
	
	public AjaxJson savenewpwdforuser(HttpServletRequest req) {
		String message = null;
		AjaxJson j = new AjaxJson();
		String id = oConvertUtils.getString(req.getParameter("id"));
		String password = oConvertUtils.getString(req.getParameter("password"));
		if (StringUtil.isNotEmpty(id)) {
			TSUser users = systemService.getEntity(TSUser.class,id);
			//System.out.println(users.getUserName());
			users.setPassword(PasswordUtil.encrypt(users.getUserName(), password, PasswordUtil.getStaticSalt()));
			users.setStatus(Globals.User_Normal);
			users.setActivitiSync(users.getActivitiSync());
			systemService.updateEntitie(users);	
			message = "用户: " + users.getUserName() + "密码重置成功";
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} 
		
		j.setMsg(message);

		return j;
	}
	



	
	/**
	 * 用户信息删除更新
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	
	public AjaxJson deluser(StaffEntity staff) {
		String message =  "用户: [" + staff.getName() +" ]员工表信息已删除，但用户表删除失败!";
		String username =""+staff.getSttaffId();
		TSUser user= staffService.getUserByUsername(username);
		AjaxJson j = new AjaxJson();
	if(user.getId()!=null){
		if("999999".equals(user.getUserName())){
			message = "超级管理员[999999]不可删除";
			j.setMsg(message);
			return j;
		}
	//	user = systemService.getEntity(TSUser.class, user.getId());
		List<TSRoleUser> roleUser = systemService.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
		if (!user.getStatus().equals(Globals.User_ADMIN)) {

			user.setDeleteFlag(Globals.Delete_Forbidden);
			userService.updateEntitie(user);
			message = "用户：" + user.getUserName() + "删除成功";

			

			if (roleUser.size()>0) {
				// 删除用户时先删除用户和角色关系表
				delRoleUser(user);

                systemService.executeSql("delete from t_s_user_org where user_id=?", user.getId()); // 删除 用户-机构 数据

                userService.delete(user);
				message = "用户：" + user.getUserName() + "删除成功";
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			} else {
				userService.delete(user);
				message = "用户：" + user.getUserName() + "删除成功";
			}
	
		} else {
			message = "超级管理员不可删除";
		}
		}
		j.setMsg(message);
		return j;
	}

	public void delRoleUser(TSUser user) {
		// 同步删除用户角色关联表
		List<TSRoleUser> roleUserList = systemService.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
		if (roleUserList.size() >= 1) {
			for (TSRoleUser tRoleUser : roleUserList) {
				systemService.delete(tRoleUser);
			}
		}
	}
	/**
	 * 检查用户名
	 * 
	 * @param ids
	 * @return
	 */
	
	public ValidForm checkUser(HttpServletRequest request) {
		ValidForm v = new ValidForm();
		String userName=oConvertUtils.getString(request.getParameter("param"));
		String code=oConvertUtils.getString(request.getParameter("code"));
		List<TSUser> roles=systemService.findByProperty(TSUser.class,"userName",userName);
		if(roles.size()>0&&!code.equals(userName))
		{
			v.setInfo("用户名已存在");
			v.setStatus("n");
		}
		return v;
	}

	/**
	 * 用户录入
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	public void saveUser( HttpServletRequest req,StaffEntity staff) {
		String message =  "用户: [" + staff.getName() +" ]员工表信息已保存，但用户表保存失败!";
			
			TSUser user=new TSUser();
			user.setId(staff.getId());
			// 得到用户的角色
			String roleid = oConvertUtils.getString(req.getParameter("roleid"));
			String username=""+staff.getSttaffId();
			String password="123456";
			user.setPassword(password);
			user.setEmail(staff.getEmail());
			user.setOfficePhone(staff.getMobile());
			user.setMobilePhone(staff.getLinkmanTel());
			user.setRealName(staff.getName());
			user.setUserName(username);
			
			TSUser users = systemService.findUniqueByProperty(TSUser.class, "userName",user.getUserName());
			if (users != null) {
				message = "用户: " + users.getUserName() + "已经存在";
			} else {
				user.setPassword(PasswordUtil.encrypt(user.getUserName(), password, PasswordUtil.getStaticSalt()));
				user.setStatus(Globals.User_Normal);
				user.setDeleteFlag(Globals.Delete_Normal);
				systemService.save(user);
                // todo zhanggm 保存组织机构
				saveUserOrgList(staff.getDeptId(),  user);
				message = "用户: " + user.getUserName() + "添加成功";
				if (StringUtil.isNotEmpty(roleid)) {
					saveRoleUser(user, roleid);
				}
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}
		
	}

	/**
	 * 用户导入
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	public void saveUserByImport( HttpServletRequest req,StaffEntity staff) {
		String message =  "用户: [" + staff.getName() +" ]员工表信息已保存，但用户表保存失败!";
			
			TSUser user=new TSUser();
			//user.setId(staff.getId());
			// 得到用户的角色
			String roleid = oConvertUtils.getString(req.getAttribute("roleid"));
			String username=""+staff.getSttaffId();
			String password="123456";
			user.setPassword(password);
			user.setEmail(staff.getEmail());
			user.setOfficePhone(staff.getMobile());
			user.setMobilePhone(staff.getLinkmanTel());
			user.setRealName(staff.getName());
			user.setUserName(username);
			
			TSUser users = systemService.findUniqueByProperty(TSUser.class, "userName",user.getUserName());
			if (users != null) {
				message = "用户: " + users.getUserName() + "已经存在";
				
			} else {
				user.setPassword(PasswordUtil.encrypt(user.getUserName(), password, PasswordUtil.getStaticSalt()));
				user.setStatus(Globals.User_Normal);
				user.setDeleteFlag(Globals.Delete_Normal);
				systemService.save(user);
                // todo zhanggm 保存组织机构
				if(staff.getDeptId()!=null&&!"".equals(staff.getDeptId()))
				saveUserOrgList(staff.getDeptId(),  user);
				message = "用户: " + user.getUserName() + "添加成功";
				if (StringUtil.isNotEmpty(roleid)) {
					saveRoleUser(user, roleid);
				}
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}
		
	}
	/**
	 * 用户更改
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	public void updateUser(HttpServletRequest req, StaffEntity staff) {
		String message =  "用户: [" + staff.getName() +" ]员工表信息已修改，但用户表修改失败!";
		String username=""+staff.getSttaffId();
		TSUser user= staffService.getUserByUsername(username);
		// 得到用户的角色
		String roleid = oConvertUtils.getString(req.getParameter("roleid"));
		
		//String password="123456";
	//	user.setPassword(password);
		user.setEmail(staff.getEmail());
		user.setOfficePhone(staff.getMobile());
		user.setMobilePhone(staff.getLinkmanTel());
		user.setRealName(staff.getName());		
		if (user.getId()!=null&&!"".equals(user.getId())) {
			TSUser users = systemService.getEntity(TSUser.class, user.getId());
			users.setEmail(user.getEmail());
			users.setOfficePhone(user.getOfficePhone());
			users.setMobilePhone(user.getMobilePhone());

            systemService.executeSql("delete from t_s_user_org where user_id=?", user.getId());
            saveUserOrgList(staff.getDeptId(), user);
			users.setRealName(user.getRealName());
			users.setStatus(Globals.User_Normal);
			users.setActivitiSync(user.getActivitiSync());
			systemService.updateEntitie(users);
			List<TSRoleUser> ru = systemService.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
			systemService.deleteAllEntitie(ru);
			message = "用户: " + users.getUserName() + "更新成功";
			if (StringUtil.isNotEmpty(roleid)) {
				saveRoleUser(users, roleid);
			}
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} 			
		

		
	}
	
    /**
     * 保存 用户-组织机构 关系信息
     * @param request request
     * @param user user
     */
 //   private void saveUserOrgList(HttpServletRequest request, TSUser user) {
	 private void saveUserOrgList(String orgId, TSUser user) {

   //     String orgIds = oConvertUtils.getString(request.getParameter("orgIds"));

        List<TSUserOrg> userOrgList = new ArrayList<TSUserOrg>();
    //    List<String> orgIdList = extractIdListByComma(orgIds);
     //   for (String orgId : orgIdList) {
            TSDepart depart = new TSDepart();
            depart.setId(orgId);

            TSUserOrg userOrg = new TSUserOrg();
            userOrg.setTsUser(user);
            userOrg.setTsDepart(depart);

            userOrgList.add(userOrg);
      //  }
        if (!userOrgList.isEmpty()) {
            systemService.batchSave(userOrgList);
        }
    }


    protected void saveRoleUser(TSUser user, String roleidstr) {
		String[] roleids = roleidstr.split(",");
		for (int i = 0; i < roleids.length; i++) {
			TSRoleUser rUser = new TSRoleUser();
			TSRole role = systemService.getEntity(TSRole.class, roleids[i]);
			rUser.setTSRole(role);
			rUser.setTSUser(user);
			systemService.save(rUser);

		}
	}

	/**
	 * 用户选择角色跳转页面
	 * 
	 * @return
	 */
	
	public ModelAndView roles(HttpServletRequest request) {
		//--author：zhoujf-----start----date:20150531--------for: 编辑用户，选择角色,弹出的角色列表页面，默认没选中
		ModelAndView mv = new ModelAndView("system/user/users");
		String ids = oConvertUtils.getString(request.getParameter("ids"));
		mv.addObject("ids", ids);
		return mv;
	}

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
	
	
	
	/**
	 * 员工状态编辑页面跳转
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(params = "gochangestate")
	public ModelAndView goChangestate(StaffEntity staff, HttpServletRequest req) throws Exception {
		if (StringUtil.isNotEmpty(staff.getId())) {
			staff = staffService.getEntity(StaffEntity.class, staff.getId());
			
			StaffEntity sta =new StaffEntity();
			MyBeanUtils.copyBeanNotNull2Bean(staff, sta);
			TSDepart	depart = staffService.getEntity(TSDepart.class, sta.getDeptId());
			sta.setDeptId(depart.getDepartname());
			
			req.setAttribute("staff", sta);
		}
		
		String username =""+staff.getSttaffId();
		TSUser user= staffService.getUserByUsername(username);
		
		req.setAttribute("user", user);
		return new ModelAndView("xyoa/staff/staff-changestate");
	}
	
	
	/**
	 * 更新享宇员工表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "dochangestate")
	@ResponseBody
	public AjaxJson doChangestate(String staffid, String userid,String abdicateDate,String abdicateRemarks,HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "享宇员工状态更新成功";
		StaffEntity t = staffService.get(StaffEntity.class, staffid);
		
		HttpSession session = ContextHolderUtils.getSession();
		TSUser u = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		Date date =new Date();
		int createid =-1;
		//设置跟新人  、跟新时间
		if(!"".equals(u.getUserName())&&u.getUserName()!=null) createid =Integer.parseInt( u.getUserName());	
		t.setUpdateUer(createid);
		t.setUpudateTime(date);
		t.setState("0");
		t.setAbdicateDate(abdicateDate);
		t.setAbdicateRemarks(abdicateRemarks);
		try {
			
			staffService.saveOrUpdate(t);
			this.lock(userid, "0");// 锁定账户
			
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
	 * 锁定账户
	
	 * 
	 * @author xiaoyong
	 */
	@RequestMapping(params = "lock")
	@ResponseBody
	public AjaxJson lock(String id, String lockvalue) {
		AjaxJson j = new AjaxJson();
		String message = null;
		TSUser user = systemService.getEntity(TSUser.class, id);
		if("999999".equals(user.getUserName())){
			message = "超级管理员[999999]不可操作";
			j.setMsg(message);
			return j;
		}
		user.setStatus(new Short(lockvalue));
		try{
		userService.updateEntitie(user);
		if("0".equals(lockvalue)){
			message = "用户：" + user.getUserName() + "锁定成功!";
		}else if("1".equals(lockvalue)){
			message = "用户：" + user.getUserName() + "激活成功!";
		}
		systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			message = "操作失败!";
		}
		j.setMsg(message);
		return j;
	}

	@RequestMapping(params = "doadWordDoc")
	public void doadWordDoc( String id,HttpServletRequest req,HttpServletResponse rep) throws IllegalArgumentException, IllegalAccessException, IOException, InvocationTargetException, NoSuchMethodException
	{
		htmlConvertWordDoc(id, rep);
	}
	@RequestMapping(params = "htmlConvertWordDoc")
	public void htmlConvertWordDoc(String id, HttpServletResponse response) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		StaffHtmlConstants constantHtml=new StaffHtmlConstants();
		String html=constantHtml.html;	
		String education=constantHtml.education;
		String trainhtml=constantHtml.trainhtml;
		String homehtml=constantHtml.homehtml;
		String workhtml=constantHtml.workhtml;
		StaffEntity sta = systemService.getEntity(StaffEntity.class, id);
		StaffEntity staff= new StaffEntity();
		org.apache.commons.beanutils.PropertyUtils.copyProperties(staff, sta);
		
		TSDepart dept=systemService.getEntity(TSDepart.class, staff.getDeptId());
		if(dept!=null)staff.setDeptId(dept.getDepartname());
		DeptJobEntity job=systemService.getEntity(DeptJobEntity.class, staff.getJobNo());
		if(job!=null)staff.setJobNo(job.getJobName());
				//性别翻译
				TSTypegroup tstype=systemService.getEntity(TSTypegroup.class, "8a8ab0b246dc81120146dc8181cd005f");
				if(tstype!=null){
				List<TSType> tstypes=tstype.getTSTypes();
				if(tstypes!=null)
				for(int i=0; i<tstypes.size();i++){
					if(staff.getSex()!=null)
					if(staff.getSex().equals(tstypes.get(i).getTypecode()))staff.setSex(tstypes.get(i).getTypename());
				}
				}
				//名族翻译
				 tstype=systemService.getEntity(TSTypegroup.class, "297eb42c566d02ef01566e0f0c1d0044");
				if(tstype!=null){
				List<TSType> tstypes=tstype.getTSTypes();
				if(tstypes!=null)
				for(int i=0; i<tstypes.size();i++){
					if(staff.getNation()!=null)
					if(staff.getNation().equals(tstypes.get(i).getTypecode()))staff.setNation(tstypes.get(i).getTypename());
				}
				}
				//政治面貌翻译
				 tstype=systemService.getEntity(TSTypegroup.class, "402882ce57b7a32e0157b7a98e630001");
				if(tstype!=null){
				List<TSType> tstypes=tstype.getTSTypes();
				if(tstypes!=null)
				
				for(int i=0; i<tstypes.size();i++){
					if(staff.getPoliticsStatus()!=null)
					if(staff.getPoliticsStatus().equals(tstypes.get(i).getTypecode()))staff.setPoliticsStatus(tstypes.get(i).getTypename());
				}
				}
				//户籍翻译
				 tstype=systemService.getEntity(TSTypegroup.class, "297eb42c566d02ef01566e18d580006e");
				if(tstype!=null){
				List<TSType> tstypes=tstype.getTSTypes();
				if(tstypes!=null)
				for(int i=0; i<tstypes.size();i++){
					if(staff.getRegisterType()!=null)
					if(staff.getRegisterType().equals(tstypes.get(i).getTypecode()))staff.setRegisterType(tstypes.get(i).getTypename());
				}
				}
				//是否统招翻译
				 tstype=systemService.getEntity(TSTypegroup.class, "402882ce57b7a32e0157b7ab81550009");
				if(tstype!=null){
				List<TSType> tstypes=tstype.getTSTypes();
				if(tstypes!=null)
				for(int i=0; i<tstypes.size();i++){
					if(staff.getFullEducation()!=null)
					if(staff.getFullEducation().equals(tstypes.get(i).getTypecode()))staff.setFullEducation(tstypes.get(i).getTypename());
				}
				}
				//婚姻状况翻译
				 tstype=systemService.getEntity(TSTypegroup.class, "297eb42c566d02ef01566e148f4e005f");
				if(tstype!=null){
				List<TSType> tstypes=tstype.getTSTypes();
				if(tstypes!=null)
				for(int i=0; i<tstypes.size();i++){
					if(staff.getMarryState()!=null)
					if(staff.getMarryState().equals(tstypes.get(i).getTypecode()))staff.setMarryState(tstypes.get(i).getTypename());
				}
				}
				//是否伤残翻译
				 tstype=systemService.getEntity(TSTypegroup.class, "402882ce57b824a30157b829b21e0001");
				if(tstype!=null){
				List<TSType> tstypes=tstype.getTSTypes();
				if(tstypes!=null)
				for(int i=0; i<tstypes.size();i++){
					if(staff.getIsDistressed()!=null)
					if(staff.getIsDistressed().equals(tstypes.get(i).getTypecode()))staff.setIsDistressed(tstypes.get(i).getTypename());
				}
				}
				//伤残等级翻译
				 tstype=systemService.getEntity(TSTypegroup.class, "402882ce57b824a30157b82b88c80008");
				if(tstype!=null){
				List<TSType> tstypes=tstype.getTSTypes();
				if(tstypes!=null)
				for(int i=0; i<tstypes.size();i++){
					if(staff.getDistressedLevel()!=null)
					if(staff.getDistressedLevel().equals(tstypes.get(i).getTypecode()))staff.setDistressedLevel(tstypes.get(i).getTypename());
				}
				}
				//是否犯罪翻译
				 tstype=systemService.getEntity(TSTypegroup.class, "402882ce57b824a30157b830220c002d");
				if(tstype!=null){
				List<TSType> tstypes=tstype.getTSTypes();
				if(tstypes!=null)
				for(int i=0; i<tstypes.size();i++){
					if(staff.getIsCommit()!=null)
					if(staff.getIsCommit().equals(tstypes.get(i).getTypecode()))staff.setIsCommit(tstypes.get(i).getTypename());
				}
				}
				//是否有传染疾病翻译
				 tstype=systemService.getEntity(TSTypegroup.class, "402882ce57b824a30157b82f576a0027");
				if(tstype!=null){
				List<TSType> tstypes=tstype.getTSTypes();
				if(tstypes!=null)
				for(int i=0; i<tstypes.size();i++){
					if(staff.getIsSick()!=null)
					if(staff.getIsSick().equals(tstypes.get(i).getTypecode()))staff.setIsSick(tstypes.get(i).getTypename());
				}
				}
				//最高学历翻译
				 tstype=systemService.getEntity(TSTypegroup.class, "297eb42c566d02ef01566e125f0b0053");
				if(tstype!=null){
				List<TSType> tstypes=tstype.getTSTypes();
				if(tstypes!=null)
				for(int i=0; i<tstypes.size();i++){
					if(staff.getMaxDegree()!=null)
					if(staff.getMaxDegree().equals(tstypes.get(i).getTypecode()))staff.setMaxDegree(tstypes.get(i).getTypename());
				}
				}
				
		//得到类对象  
	       Class staffCla = (Class) staff.getClass();  	        
	       /* 
	        * 得到类中的所有属性集合 
	        */  
	       Field[] fs = staffCla.getDeclaredFields();  
	       for(int i = 0 ; i < fs.length; i++){  
	           Field f = fs[i];  
	           f.setAccessible(true); //设置些属性是可以访问的  
	           //Object val = f.get(bean);//得到此属性的值     
	          String name= "${"+f.getName()+"}";
	         
	         String replace= f.get(staff)+"";
	         if("null".equals(replace))replace="";
	         String type = f.getType().toString();//得到此属性的类型  
	           if (type.endsWith("Date")) {  
	        	   if(replace.length()>10)
	        	   replace=replace.substring(0,10);   
	           }
	      
	       html=   html.replace(name, replace);
	     //  String s="■□";
	   
	       if("0".equals(staff.getStaffSource()))html=   html.replace("$62@", "■ "); 
      		else html=   html.replace("$62@", "□");
	       if("1".equals(staff.getStaffSource()))html=   html.replace("$55@", "■ "); 
	       		else html=   html.replace("$55@", "□");
	       if("2".equals(staff.getStaffSource()))html=   html.replace("$56@", "■"); 
	       		else html=   html.replace("$56@", "□");
	       if("3".equals(staff.getStaffSource()))html=   html.replace("$57@", "■"); 
	       		else html=   html.replace("$57@", "□");
	       if("4".equals(staff.getStaffSource()))html=   html.replace("$58@", "■"); 
	       		else html=   html.replace("$58@", "□");
	       //内部推荐
	       if("15".equals(staff.getStaffSource()))
	    	   {//替换推荐人
		    	   	html=   html.replace("$59@", "■"); 
					String hql = "from StaffEntity s where s.sttaffId=?";	
					
				    List<StaffEntity> edulist= systemService.findHql(hql,staff.getReferenceId());
				    if(edulist!=null&&edulist.get(0)!=null)
				    html=   html.replace("$61@", edulist.get(0).getName());	
				    else html=   html.replace("$61@", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	    	   }else{
	    		   html=   html.replace("$59@", "□");
	    		   html=   html.replace("$61@", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	    	   }
	       if("6".equals(staff.getStaffSource()))html=   html.replace("$60@", "■"); 
      		else html=   html.replace("$60@", "□");	      
	       html=   html.replace(name, replace); 	            
	       }  
	       //替换教育经历
			String hql = "from StaffEducationEntity edu where edu.staffId=?";
			
		    List<StaffEducationEntity> edulist= systemService.findHql(hql,staff.getSttaffId() );
			if(edulist!=null&&edulist.size()>0&&edulist.get(0)!=null)
			{
				String educations="";
				for(int i=0;i<edulist.size();i++)
				{ 
					String educationOne=education;
					StaffEducationEntity beforeedu=	edulist.get(i);
					StaffEducationEntity edu=new StaffEducationEntity();
					org.apache.commons.beanutils.PropertyUtils.copyProperties(edu, beforeedu);
					//教育形式翻译
					 tstype=systemService.getEntity(TSTypegroup.class, "402882ce57b697ec0157b6feca27002d");
					if(tstype!=null){
					List<TSType> tstypes=tstype.getTSTypes();
					if(tstypes!=null)
					for(int h=0; h<tstypes.size();h++){
						if(edu.getEducationWay()!=null)
						if(edu.getEducationWay().equals(tstypes.get(h).getTypecode()))edu.setEducationWay(tstypes.get(h).getTypename());
					}
					}
					//学历翻译
					 tstype=systemService.getEntity(TSTypegroup.class, "297eb42c566d02ef01566e125f0b0053");
					if(tstype!=null){
					List<TSType> tstypes=tstype.getTSTypes();
					if(tstypes!=null)
					for(int h=0; h<tstypes.size();h++){
						if(edu.getEducationType()!=null)
						if(edu.getEducationType().equals(tstypes.get(h).getTypecode()))edu.setEducationType(tstypes.get(h).getTypename());
					}
					}
					//得到类对象  
				       Class eduCla = (Class) edu.getClass();  	        
				      
				        fs = eduCla.getDeclaredFields(); //  得到类中的所有属性集合 
				       for(int j = 0 ; j < fs.length; j++){  
				           Field f = fs[j];  
				           f.setAccessible(true); //设置些属性是可以访问的  
				           //Object val = f.get(bean);//得到此属性的值     
				          String name= "${"+f.getName()+"}";
				         
				         String replace= f.get(edu)+"";
				         if("null".equals(replace))replace="";
				         
				           if("${startDate}".equals(name)){
				        	   if(replace.length()>7)
					        	   replace=replace.substring(0,7);
				           }
				           if("${endDate}".equals(name)){
				        	   if(replace.length()>7)
					        	   replace=replace.substring(0,7);
				           }
				           educationOne=educationOne.replace(name, replace);
				      }
				       educations+=  educationOne;
				       
				       
				}
				html=html.replace("${教育经历}", educations);
			}else html=html.replace("${教育经历}", "");
			
			  //替换工作经历		    
				 hql = "from StaffWorkExperienceEntity work where work.staffId=? order by work.endDate desc ";
				
			    List<StaffWorkExperienceEntity> worklist= systemService.findHql(hql,staff.getSttaffId() );
				if(worklist!=null&&worklist.size()>0&&worklist.get(0)!=null)
				{
					
					//前用人单位替换
					StaffWorkExperienceEntity wone=worklist.get(0);
					StaffWorkExperienceEntity w= new StaffWorkExperienceEntity();
					org.apache.commons.beanutils.PropertyUtils.copyProperties(w, wone);
					//是否保密翻译
					 tstype=systemService.getEntity(TSTypegroup.class, "402882ce57b7cf1d0157b7d919830001");
					if(tstype!=null){
					List<TSType> tstypes=tstype.getTSTypes();
					if(tstypes!=null)
					for(int i=0; i<tstypes.size();i++){
						if(w.getHaveProtocol()!=null)
						if(w.getHaveProtocol().equals(tstypes.get(i).getTypecode()))w.setHaveProtocol(tstypes.get(i).getTypename());
					}
					}
					//是否有法律事务翻译
					 tstype=systemService.getEntity(TSTypegroup.class, "402882ce57b7cf1d0157b7dae1170007");
					if(tstype!=null){
					List<TSType> tstypes=tstype.getTSTypes();
					if(tstypes!=null)
					for(int i=0; i<tstypes.size();i++){
						if(w.getHaveLaw()!=null)
						if(w.getHaveLaw().equals(tstypes.get(i).getTypecode()))w.setHaveLaw(tstypes.get(i).getTypename());
					}
					}
					html=html.replace("${haveProtocol}",w.getHaveProtocol() );
					html=html.replace("${haveLaw}",w.getHaveLaw() );
					html=html.replace("${specificLaw}",w.getSpecificLaw());
					
					String works="";
					for(int i=0;i<worklist.size();i++)
					{ 
						String workOne=workhtml;
						StaffWorkExperienceEntity work=	worklist.get(i);
						//得到类对象  
					       Class eduCla = (Class) work.getClass();  	        
					      
					        fs = eduCla.getDeclaredFields(); //  得到类中的所有属性集合 
					       for(int j = 0 ; j < fs.length; j++){  
					           Field f = fs[j];  
					           f.setAccessible(true); //设置些属性是可以访问的  
					           //Object val = f.get(bean);//得到此属性的值     
					          String name= "${"+f.getName()+"}";
					         
					         String replace= f.get(work)+"";
					         if("null".equals(replace))replace="";
					         if("${startDate}".equals(name)){
					        	   if(replace.length()>7)
						        	   replace=replace.substring(0,7);
					           }
					           if("${endDate}".equals(name)){
					        	   if(replace.length()>7)
						        	   replace=replace.substring(0,7);
					           }
					           workOne=workOne.replace(name, replace);
					      }
					       works+=  workOne;
					       
					       
					}
					html=html.replace("${工作经历}", works);
				}else 
				{html=html.replace("${haveProtocol}","" );
				html=html.replace("${haveLaw}","");
				html=html.replace("${specificLaw}","");
				html=html.replace("${工作经历}", "");
				}
					
	        
				  //替换曾受培训或所证照情况	    
				 hql = "from SXyStaffTrainEntity train where train.staffId=?";
				
			    List<SXyStaffTrainEntity> trainlist= systemService.findHql(hql,staff.getSttaffId() );
				if(trainlist!=null&&trainlist.size()>0&&trainlist.get(0)!=null)
				{
					String trains="";
					for(int i=0;i<trainlist.size();i++)
					{ 
						String trainOne=trainhtml;
						SXyStaffTrainEntity train=	trainlist.get(i);
						//得到类对象  
					       Class eduCla = (Class) train.getClass();  	        
					      
					        fs = eduCla.getDeclaredFields(); //  得到类中的所有属性集合 
					       for(int j = 0 ; j < fs.length; j++){  
					           Field f = fs[j];  
					           f.setAccessible(true); //设置些属性是可以访问的  
					           //Object val = f.get(bean);//得到此属性的值     
					          String name= "${"+f.getName()+"}";
					         
					         String replace= f.get(train)+"";
					         if("null".equals(replace))replace="";
					         String type = f.getType().toString();//得到此属性的类型  
					           if (type.endsWith("Date")) {  
					        	   if(replace.length()>10)
					        	   replace=replace.substring(0,10);   
					           }
					           trainOne=trainOne.replace(name, replace);
					      }
					       trains+=  trainOne;
					       
					       
					}
					html=html.replace("${培训经历}", trains);
				}else html=html.replace("${培训经历}", "");

				  //替换家庭成员	    
				 hql = "from SXyStaffHomeMemberEntity home where home.staffId=?";
				
			    List<SXyStaffHomeMemberEntity> homelist= systemService.findHql(hql,staff.getSttaffId() );
				if(homelist!=null&&homelist.size()>0&&homelist.get(0)!=null)
				{
					String homes="";
					for(int i=0;i<homelist.size();i++)
					{ 
						String homeOne=homehtml;
						SXyStaffHomeMemberEntity home=	homelist.get(i);
						//得到类对象  
					       Class eduCla = (Class) home.getClass();  	        
					      
					        fs = eduCla.getDeclaredFields(); //  得到类中的所有属性集合 
					       for(int j = 0 ; j < fs.length; j++){  
					           Field f = fs[j];  
					           f.setAccessible(true); //设置些属性是可以访问的  
					           //Object val = f.get(bean);//得到此属性的值     
					          String name= "${"+f.getName()+"}";
					         
					         String replace= f.get(home)+"";
					         if("null".equals(replace))replace="";
					         String type = f.getType().toString();//得到此属性的类型  
					           if (type.endsWith("Date")) {  
					        	   if(replace.length()>10)
					        	   replace=replace.substring(0,10);   
					           }
					           homeOne=homeOne.replace(name, replace);
					      }
					       homes+=  homeOne;
					       
					       
					}
					html=html.replace("${家庭成员}", homes);
				}else html=html.replace("${家庭成员}", "");
		ByteArrayInputStream bais = null;
		//FileOutputStream fos = null;
		OutputStream ostream=null;
		byte b[] = null;
		try {
			b = html.getBytes("GBK");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		bais = new ByteArrayInputStream(b);
		POIFSFileSystem poifs = new POIFSFileSystem();
		DirectoryEntry directory = poifs.getRoot();
		try {
			DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
			String name=staff.getName()+ "-入职登记表";
            response.reset();  
            response.setHeader("Content-Disposition",  
                     "attachment;filename=" +  
                     new String( (name + ".doc").getBytes("gbk"),  
                                "iso-8859-1"));  
           
            response.setContentType("application/msword");  
            ostream = response.getOutputStream();   
            //输出到本地文件的话，new一个文件流  
            //FileOutputStream ostream = new FileOutputStream(path+ fileName);    
            poifs.writeFilesystem(ostream);    
		} catch (FileNotFoundException e) {
			// log.error("");
			logger.error("文件未找到", e);
		} catch (IOException e) {
			// log.error("文件写入,IO异常");
			logger.error("文件写入,IO异常", e);
		} finally {
			try {
				bais.close();
				ostream.close();
			} catch (Exception e) {
				logger.error("流关闭,IO异常", e);
			}
		}

		
	}
}
