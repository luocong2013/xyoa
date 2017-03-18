package com.xy.oa.checkinout.contoller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.pojo.base.TSBaseUser;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;
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

import com.xy.oa.checkinout.entity.SXyCheckinoutEntity;
import com.xy.oa.checkinout.pcheck.service.PCheckinoutServiceI;
import com.xy.oa.checkinout.pcheck.entity.PCheckinoutEntity;
import com.xy.oa.checkinout.service.SXyCheckinoutServiceI;
import com.xy.oa.deptjob.service.DeptJobServiceI;
import com.xy.oa.staff.service.StaffServiceI;
import com.xy.oa.util.Constants;
import com.xy.oa.util.ImpCheckSumXlsUtil;
import com.xy.oa.util.OutCheck;
import com.xy.oa.util.PoiExcelExport;
import com.xy.oa.util.TimerUtils;

/**   
 * @Title: Controller  
 * @Description: 考勤统计表
 * @author onlineGenerator
 * @date 2016-08-19 11:43:37
 * @version V1.0   
 *
 */
@Controller("sXyCheckinoutController")
@RequestMapping("/sXyCheckinoutController")
public class SXyCheckinoutController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SXyCheckinoutController.class);

	@Autowired
	private SXyCheckinoutServiceI sXyCheckinoutService;
	@Autowired
	private PCheckinoutServiceI pCheckinoutService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private DeptJobServiceI deptJobService;
	 
	@Autowired
	private StaffServiceI staffService;
	
	
	/**
	 * 考勤统计表导出跳转页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "goImportChecklist")
	public ModelAndView goImportChecklist( HttpServletRequest req) {
		List<TSDepart> company = staffService.getAllCompany();
		req.setAttribute("TSDeparts", company);
		return new ModelAndView("xyoa/checkinout/sXyCheckinout-import");
	}
	/**
	 * 考勤统计表导出
	 * 
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@RequestMapping(params = "doImportChecklist")
	public void doImportChecklist(Date startDate,Date endDate, HttpServletRequest req, HttpServletResponse response) throws IOException, ParseException {
		//获得查询条件
		String name="";
		if(!(req.getParameter("name")==null)||!"".equals(req.getParameter("name"))) name=req.getParameter("name");				 
		
		String deptId="";
		if(!(req.getParameter("deptId")==null)||!"".equals(req.getParameter("deptId"))){
			deptId=sXyCheckinoutService.getSSDeptIdForString(req.getParameter("deptId"));	
		}
		
		String companyId="";
		if(!(req.getParameter("companyId")==null)||!"".equals(req.getParameter("companyId"))){
			companyId=req.getParameter("companyId");	
		}
				
		List<SXyCheckinoutEntity> datalist=sXyCheckinoutService.getImportData( startDate, endDate,name,deptId,companyId);
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyyMMddHHmmss" );
		name="考勤记录"+sdf.format(new Date());
		 
		
		HSSFWorkbook wb = new HSSFWorkbook();    //建立新HSSFWorkbook对象		
		HSSFSheet sheet = wb.createSheet("考勤记录表");  //建立新的sheet对象
		for(int i=3;i<=33;i++){
			sheet.setColumnWidth(i, (short) (256 * 6));//设置宽度
		}
		
		//标题
		HSSFRow headrow = sheet.createRow((short)0);
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 33));
		HSSFCell csCell =headrow.createCell((short)0);
		csCell.setCellValue("考 勤 记 录 表");
		HSSFFont headfont=wb.createFont();           //标题样式
		headfont.setFontName("宋体"); 
		headfont.setFontHeightInPoints((short)20); 
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); 	
		HSSFCellStyle headStyle1=wb.createCellStyle(); 
		headStyle1.setFont(headfont);
		headStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);		
/*		headStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headStyle1.setBottomBorderColor(HSSFColor.BLACK.index);	*/	
		csCell.setCellStyle (headStyle1);
		
		//第一行
		HSSFRow row1 = sheet.createRow((short)2);
		HSSFFont font1=wb.createFont();  
		font1.setFontName("宋体");
		font1.setFontHeightInPoints((short)11);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle  Style1=wb.createCellStyle(); 
		Style1.setFont(font1);
		Style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		Style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);		
		 
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 2));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 3, 5)); 
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 13)); 
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 14, 16));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 17, 24));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 25, 33));
		csCell=row1 .createCell((short)3);
		csCell.setCellValue("考勤时间：");
		csCell.setCellStyle(Style1);
		csCell=row1 .createCell((short)14);		
		csCell.setCellValue("制表时间：");
		csCell.setCellStyle(Style1);
		
		 
		HSSFFont datefont=wb.createFont();  
		datefont.setFontName("仿宋");
		datefont.setFontHeightInPoints((short)10);
		HSSFCellStyle  dateStyle =wb.createCellStyle(); 
		dateStyle.setFont(datefont);
		dateStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		
		 
		csCell=row1 .createCell((short)6);	
		SimpleDateFormat dateSimpleDateFormat=   new SimpleDateFormat( "yyyy年MM月dd日" );
		csCell.setCellValue(dateSimpleDateFormat.format(startDate)+"—"+dateSimpleDateFormat.format(endDate));
		csCell.setCellStyle(dateStyle);
		csCell=row1 .createCell((short)17);	
		SimpleDateFormat simpleDateFormat =   new SimpleDateFormat( "yyyy年MM月dd日  HH时mm分ss秒" );
		csCell.setCellValue(simpleDateFormat.format(new Date()));
		csCell.setCellStyle(dateStyle);
		//第二行
		HSSFRow row2 = sheet.createRow((short)3);
		HSSFFont font2=wb.createFont();  
		font2.setFontName("宋体");
		font2.setFontHeightInPoints((short)11);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle  Style2=wb.createCellStyle(); 
		Style2.setFont(font2);
		Style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		Style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);		
		
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 1, 1)); 
		sheet.addMergedRegion(new CellRangeAddress(3, 4, 2, 2)); 
		csCell=row2 .createCell((short)0);
		csCell.setCellValue("工号");
		csCell.setCellStyle(Style2);
		csCell=row2 .createCell((short)1);
		csCell.setCellValue("姓名");
		csCell.setCellStyle(Style2);
		csCell=row2 .createCell((short)2);
		csCell.setCellValue("部门");
		csCell.setCellStyle(Style2);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 33));
		csCell=row2 .createCell((short)3);
		csCell.setCellValue("考勤日期");
		csCell.setCellStyle(Style2);
		
		HSSFFont font3=wb.createFont();  
		font3.setFontName("宋体");
		font3.setFontHeightInPoints((short)10);		 
		HSSFCellStyle  Style3=wb.createCellStyle(); 
		Style3.setFont(font3);
		Style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);		
		Style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFRow row3 = sheet.createRow((short)4);
		 int j=21;
		for(int i=3;i<=33;i++)
		{  
			if(j>31)j=1;
			csCell=row3 .createCell((short)i);
			csCell.setCellValue(j);
			csCell.setCellStyle(Style3);
			j++;
		}
		
		if(datalist!=null)
		{	
			SimpleDateFormat oldworkDateFormat =   new SimpleDateFormat( "HH:mm:ss" );
			Date oldworkdate;
			Date oldoffworkdate;
			SimpleDateFormat newworkDateFormat =   new SimpleDateFormat( "HH:mm" );
			Date newworkdate;
			Date newoffworkdate;
			
			HSSFFont font4=wb.createFont();  
			font4.setFontName("Arial");
			font4.setFontHeightInPoints((short)10);		 
			HSSFCellStyle  Style4=wb.createCellStyle(); 
			Style4.setFont(font4);
			Style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			Style4.setWrapText( true );
			
			int h=5;
			HSSFRow rowdata=null;
			int flag=0;
			for(int i=0;i<datalist.size();i++)
			{
			
			if(datalist.get(i).getStaffId()!=flag)//判断是不是同一个人
				{
				flag=datalist.get(i).getStaffId();
				rowdata = sheet.createRow((short)h);
				h++;
			//填写工号、名字、部门	
				csCell=rowdata .createCell((short)0);
				csCell.setCellValue(datalist.get(i).getStaffId());
				csCell.setCellStyle(Style3);
				csCell=rowdata .createCell((short)1);
				csCell.setCellValue(datalist.get(i).getName());
				csCell.setCellStyle(Style3);
				csCell=rowdata .createCell((short)2);
				TSDepart dept=null;
				if(datalist.get(i).getDeptId()!=null&&!"".equals(datalist.get(i).getDeptId()))
				{dept= systemService.getEntity(TSDepart.class, datalist.get(i).getDeptId());
				csCell.setCellValue(dept.getDepartname());
				}
				csCell.setCellStyle(Style3);
				}
			SimpleDateFormat dataSimpleDateFormat=   new SimpleDateFormat( "yyyy-MM-dd" );
			Date datadate =dataSimpleDateFormat.parse(datalist.get(i).getCheckDate());
			//填写 上下班时间
			Calendar cal = Calendar.getInstance();
			cal.setTime(datadate);
			int date = cal.get(Calendar.DATE); //得到号数
			String worktime="无     ";
			String offworktime="无     ";
			if(datalist.get(i).getWorkTime()!=null&&!"".equals(datalist.get(i).getWorkTime())){
				oldworkdate= oldworkDateFormat.parse(datalist.get(i).getWorkTime());				
				worktime=newworkDateFormat.format(oldworkdate);
				}
			if(datalist.get(i).getOffWorkTime()!=null&&!"".equals(datalist.get(i).getOffWorkTime())){	
					oldoffworkdate= oldworkDateFormat.parse(datalist.get(i).getOffWorkTime());						
					offworktime=newworkDateFormat.format(oldoffworkdate);
					}
			if(date==21)
			{
				csCell=rowdata .createCell((short)3);
				 csCell.setCellValue(worktime+offworktime);					
				csCell.setCellStyle(Style4);
			}
			if(date==22)
			{
				csCell=rowdata .createCell((short)4);
				 csCell.setCellValue(worktime+offworktime);	
				 csCell.setCellStyle(Style4);
			}
			if(date==23)
			{
				csCell=rowdata .createCell((short)5);
				 csCell.setCellValue(worktime+offworktime);	
				 csCell.setCellStyle(Style4);
			}
			if(date==24)
			{
				csCell=rowdata .createCell((short)6);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}
			if(date==25)
			{
				csCell=rowdata .createCell((short)7);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}
			if(date==26)
			{
				csCell=rowdata .createCell((short)8);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}
			if(date==27)
			{
				csCell=rowdata .createCell((short)9);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}
			if(date==28)
			{
				csCell=rowdata .createCell((short)10);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}
			if(date==29)
			{
				csCell=rowdata .createCell((short)11);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}
			if(date==30)
			{
				csCell=rowdata .createCell((short)12);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}
			if(date==31)
			{
				csCell=rowdata .createCell((short)13);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}
			if(date==1)
			{
				csCell=rowdata .createCell((short)14);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==2)
			{
				csCell=rowdata .createCell((short)15);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==3)
			{
				csCell=rowdata .createCell((short)16);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==4)
			{
				csCell=rowdata .createCell((short)17);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==5)
			{
				csCell=rowdata .createCell((short)18);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==6)
			{
				csCell=rowdata .createCell((short)19);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==7)
			{
				csCell=rowdata .createCell((short)20);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==8)
			{
				csCell=rowdata .createCell((short)21);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==9)
			{
				csCell=rowdata .createCell((short)22);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==10)
			{
				csCell=rowdata .createCell((short)23);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==11)
			{
				csCell=rowdata .createCell((short)24);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==12)
			{
				csCell=rowdata .createCell((short)25);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==13)
			{
				csCell=rowdata .createCell((short)26);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==14)
			{
				csCell=rowdata .createCell((short)27);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==15)
			{
				csCell=rowdata .createCell((short)28);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==16)
			{
				csCell=rowdata .createCell((short)29);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==17)
			{
				csCell=rowdata .createCell((short)30);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==18)
			{
				csCell=rowdata .createCell((short)31);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==19)
			{
				csCell=rowdata .createCell((short)32);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}if(date==20)
			{
				csCell=rowdata .createCell((short)33);
				csCell.setCellValue(worktime+offworktime);	
				csCell.setCellStyle(Style4);
			}
			
			}
			
		}
		
		
		//处理中文问题，或者使用：name = URLEncoder.encode(name, "UTF-8");
		name = new String(name.getBytes("gbk"),"iso-8859-1");
		//2：使用response对象设置文件下载的信息（头部信息，文件类型…)
		response.setHeader("Content-disposition", "attachment;filename="+name+".xls");
		response.setBufferSize(1024);
		//3：使用InputStream输入流读到path路径下对应文件，将InputStream的输入流写到输出流（response对象中获取）中
 
		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
	}

	/**
	 * 考勤统计表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		
		HttpSession session = ContextHolderUtils.getSession();
		TSUser u = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
		
		/**获取登录用户code根据code展示考勤页面*/
		List<TSRoleUser> userlist=sXyCheckinoutService.geTsRoleUsers(u.getId());
		int  code= 0;
		for (TSRoleUser t:userlist) {
			String roleCode = t.getTSRole().getRoleCode();
			if (Constants.HR.equals(roleCode) || Constants.HRDM.equals(roleCode) || Constants.VICE.equals(roleCode) || 
					Constants.CEO.equals(roleCode) || roleCode.contains("admin")) {
				code = 3;
				break;
			}
			if (Constants.DM.equals(roleCode) || Constants.HEADMAN.equals(roleCode)) {
				code = 2;
			}
		}
		
		if(code ==0){
			code =1;
		}
		
		/**获取部门Id*/
		String deptid="";
		List<TSUserOrg> deptlist=sXyCheckinoutService.getDeptId(u.getId());
		for (TSUserOrg d:deptlist) {
			deptid=d.getTsDepart().getId();
		}
		
		request.setAttribute("deptid", deptid);
		
		int staffid=0;
		List<TSBaseUser> baseuserlist=sXyCheckinoutService.getStaffId(u.getId());
		for (TSBaseUser t:baseuserlist) {
			staffid=Integer.parseInt(t.getUserName());
		}
		request.setAttribute("staffid", staffid);
		
		
		/**得到所有部门*/
		List<TSDepart> deptname=sXyCheckinoutService.getDeptName();
		String str="";
		for (int i = 0; i < deptname.size(); i++) {
			if (deptname.get(i).getDepartname()==null||"".equals(deptname.get(i).getDepartname())) {
				str+="无"+"_"+deptname.get(i).getId()+",";	
			}else {
				if(deptname.get(i).getOrgCode().length()>9){
					str+=deptname.get(i).getTSPDepart().getDepartname()+"/"+deptname.get(i).getDepartname()+"_"+deptname.get(i).getId()+",";
				}else{
					str+=deptname.get(i).getDepartname()+"_"+deptname.get(i).getId()+",";
				}
			}
		}
		str=str.substring(0,str.length()-1);
		request.setAttribute("deptname", str);
		
		if (3 == code) {
			return new ModelAndView("xyoa/checkinout/sXyCheckinoutList");
		}
		if (2 == code) {
			return new ModelAndView("xyoa/checkinout/sXyCheckinoutList2");
		}
		if (1== code) {
			return new ModelAndView("xyoa/checkinout/sXyCheckinoutList3");
		}
		return new ModelAndView("没有权限");
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
	public void datagrid(SXyCheckinoutEntity sXyCheckinout,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SXyCheckinoutEntity.class, dataGrid);
		//查询条件组装器
		try{
			HttpSession session = ContextHolderUtils.getSession();
			TSUser u = (TSUser) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
			
			if(StringUtil.isNotEmpty(sXyCheckinout.getName()))
				sXyCheckinout.setName("*"+sXyCheckinout.getName()+"*");
			
			/**获取登录用户code根据code展示考勤页面*/
			List<TSRoleUser> userlist=sXyCheckinoutService.geTsRoleUsers(u.getId());
			int  code= 0;
			for (TSRoleUser t:userlist) {
				String roleCode = t.getTSRole().getRoleCode();
				if (Constants.HR.equals(roleCode) || Constants.HRDM.equals(roleCode) || Constants.VICE.equals(roleCode) || 
						Constants.CEO.equals(roleCode) || roleCode.contains("admin")) {
					code = 3;
					break;
				}
				if (Constants.DM.equals(roleCode) || Constants.HEADMAN.equals(roleCode)) {
					code = 2;
				}
				if (Constants.EMPLOYEE.equals(roleCode)) {
					code = 1;
				}
			}
			
			/**获取部门Id*/
			
			if(code ==2){
				String deptCode="";
				List<TSUserOrg> deptlist=sXyCheckinoutService.getDeptId(u.getId());
				for (TSUserOrg d:deptlist) {
					if(deptCode.length()>0){
						if(deptCode.length()>d.getTsDepart().getOrgCode().length()){
							deptCode = d.getTsDepart().getOrgCode();
						}
					}else{
						deptCode = d.getTsDepart().getOrgCode();
					}
				}
				
				
				List<TSDepart> tsDepartList = sXyCheckinoutService.findByQueryString("from TSDepart tsDepart where tsDepart.orgCode like '"+deptCode+"%'");
				List <String> deptIdList = new ArrayList<String>();
				
				for(TSDepart tsD : tsDepartList){
					deptIdList.add(tsD.getId());
				}
				
				if (deptIdList != null && deptIdList.size()>0) {
					cq.in("deptId", deptIdList.toArray());
				}
			}
			sXyCheckinout.setDeptId(null);
			org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyCheckinout, request.getParameterMap());

			
			//自定义追加查询条件
			//部门查询
			String orgId = request.getParameter("orgId");
			if (StringUtil.isNotEmpty(orgId)) {
				cq.in("deptId", staffService.getSSDeptId(orgId).toArray());
			}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.sXyCheckinoutService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除考勤统计表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(SXyCheckinoutEntity sXyCheckinout, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		sXyCheckinout = systemService.getEntity(SXyCheckinoutEntity.class, sXyCheckinout.getId());
		message = "考勤统计表删除成功";
		try{
			sXyCheckinoutService.delete(sXyCheckinout);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "考勤统计表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除考勤统计表
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "考勤统计表删除成功";
		try{
			for(String id:ids.split(",")){
				SXyCheckinoutEntity sXyCheckinout = systemService.getEntity(SXyCheckinoutEntity.class, 
				Integer.parseInt(id)
				);
				sXyCheckinoutService.delete(sXyCheckinout);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "考勤统计表删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加考勤统计表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(SXyCheckinoutEntity sXyCheckinout, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "考勤统计表添加成功";
		try{
			sXyCheckinoutService.save(sXyCheckinout);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "考勤统计表添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新考勤统计表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(SXyCheckinoutEntity sXyCheckinout, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "考勤统计表更新成功";
		SXyCheckinoutEntity t = sXyCheckinoutService.get(SXyCheckinoutEntity.class, sXyCheckinout.getId());
		t.setuTime(new Date());
		t.setuUser(Integer.parseInt(ResourceUtil.getSessionUserName().getUserName()));
		try {
			MyBeanUtils.copyBeanNotNull2Bean(sXyCheckinout, t);
			sXyCheckinoutService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);			
		} catch (Exception e) {
			e.printStackTrace();
			message = "考勤统计表更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	@RequestMapping(params = "impCheckSumXls")
	public void impCheckSumXls(HttpServletRequest request, HttpServletResponse response) {
		try {
			String orgId = request.getParameter("orgId");
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			
			String newOrgId =sXyCheckinoutService.getSSDeptIdForString(orgId);	
			
			List<Map<String,Object>> checkSumData = sXyCheckinoutService.impCheckSumData(startDate,endDate,newOrgId);
			
			String fileName = "考勤汇总表-" + DateUtils.getDate("yyyyMMddHHmmssSSS") + ".xls";
			String headStr = "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8");
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", headStr);
			OutputStream out = response.getOutputStream();
			ImpCheckSumXlsUtil.impCheckSumXls(out,checkSumData,startDate,endDate);
			out.close();
		} catch (Exception e) {
			logger.error("考勤汇总表统计失败，失败原因：", e);
			throw new BusinessException(e.getMessage());
		}
	}
	
	/**
	 * 导出考勤异常统计信息
	 * @param sXyCheckinout
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "doPutOutXls")
	public void doPutOutXls(HttpServletRequest request, HttpServletResponse response) {
		try {
			String orgId = request.getParameter("orgId");
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			List<SXyCheckinoutEntity> entities = null;
			StringBuilder hql = new StringBuilder("from SXyCheckinoutEntity s where (s.checkDate between ? and ?) and s.isCheckTrue = ?");
			
			String newOrgId =sXyCheckinoutService.getSSDeptIdForString(orgId);	
			
			if (StringUtil.isNotEmpty(newOrgId)) {
				hql.append(" and s.deptId in("+newOrgId+") order by s.checkDate");
				entities = sXyCheckinoutService.findHql(hql.toString(), startDate, endDate, "01");
			} else {
				hql.append(" order by s.checkDate");
				entities = sXyCheckinoutService.findHql(hql.toString(), startDate, endDate, "01");
			}
			PoiExcelExport<OutCheck> pex = new PoiExcelExport<OutCheck>();
			String[] headers = {"工号", "姓名", "所属部门", "日期", "上班打卡时间", "下班打卡时间", 
					"异常原因", "总计时间\n(分钟)\n真实上班时间", "迟到时间\n(分钟)", "早退时间\n(分钟)", "缺勤时间\n(分钟)", "备注"};
			List<OutCheck> dataset = new ArrayList<OutCheck>();
			
			for (SXyCheckinoutEntity sEntity : entities) {
				//所属部门
				TSDepart tsDepart = sXyCheckinoutService.getEntity(TSDepart.class, sEntity.getDeptId());
				OutCheck outChect = new OutCheck(sEntity.getStaffId(), sEntity.getName(), tsDepart.getDepartname(), sEntity.getCheckDate(), sEntity.getWorkTime(), 
						sEntity.getOffWorkTime(), sEntity.getExceptionRemarks(), 450-sEntity.getExceptionMinute(), sEntity.getLateMinute(), 
						sEntity.getEarlierMinute(), sEntity.getExceptionMinute(), null);
				dataset.add(outChect);
			}
			String fileName = "考勤异常统计表-" + DateUtils.getDate("yyyyMMddHHmmssSSS") + ".xls";
			String headStr = "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8");
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", headStr);
			OutputStream out = response.getOutputStream();
			pex.exportExcel(headers, dataset, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * 考勤统计表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(SXyCheckinoutEntity sXyCheckinout, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(sXyCheckinout.getId())) {
			sXyCheckinout = sXyCheckinoutService.getEntity(SXyCheckinoutEntity.class, sXyCheckinout.getId());
			req.setAttribute("sXyCheckinoutPage", sXyCheckinout);
		}
		return new ModelAndView("xyoa/checkinout/sXyCheckinout-add");
	}
	/**
	 * 考勤统计表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(SXyCheckinoutEntity sXyCheckinout, HttpServletRequest req) {
		String loadtype=req.getParameter("load");

		if (StringUtil.isNotEmpty(sXyCheckinout.getId())) {
			sXyCheckinout = sXyCheckinoutService.getEntity(SXyCheckinoutEntity.class, sXyCheckinout.getId());
			req.setAttribute("sXyCheckinoutPage", sXyCheckinout);
			

			TSDepart depart = deptJobService.getEntity(TSDepart.class, sXyCheckinout.getDeptId());
			String departname =depart.getDepartname();
			req.setAttribute("departname", departname);
			
		}
		return new ModelAndView("xyoa/checkinout/sXyCheckinout-update");
	}
	
	
	/**
	 * 导出考勤异常统计信息 页面跳转
	 * @param sXyCheckinout
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goPutOutXls")
	public ModelAndView goPutOut(HttpServletRequest request) {
		return new ModelAndView("xyoa/checkinout/sXyCheckinout-putoutxls");
	}
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","sXyCheckinoutController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(SXyCheckinoutEntity sXyCheckinout,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(SXyCheckinoutEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, sXyCheckinout, request.getParameterMap());
		List<SXyCheckinoutEntity> sXyCheckinouts = this.sXyCheckinoutService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"考勤统计表");
		modelMap.put(NormalExcelConstants.CLASS,SXyCheckinoutEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("考勤统计表列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,sXyCheckinouts);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(SXyCheckinoutEntity sXyCheckinout,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"考勤统计表");
    	modelMap.put(NormalExcelConstants.CLASS,SXyCheckinoutEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("考勤统计表列表", "导出人:"+ResourceUtil.getSessionUserName().getRealName(),
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
				List<SXyCheckinoutEntity> listSXyCheckinoutEntitys = ExcelImportUtil.importExcel(file.getInputStream(),SXyCheckinoutEntity.class,params);
				for (SXyCheckinoutEntity sXyCheckinout : listSXyCheckinoutEntitys) {
					sXyCheckinoutService.save(sXyCheckinout);
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
	public List<SXyCheckinoutEntity> list() {
		List<SXyCheckinoutEntity> listSXyCheckinouts=sXyCheckinoutService.getList(SXyCheckinoutEntity.class);
		return listSXyCheckinouts;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		SXyCheckinoutEntity task = sXyCheckinoutService.get(SXyCheckinoutEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody SXyCheckinoutEntity sXyCheckinout, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyCheckinoutEntity>> failures = validator.validate(sXyCheckinout);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyCheckinoutService.save(sXyCheckinout);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		Integer id = sXyCheckinout.getId();
		URI uri = uriBuilder.path("/rest/sXyCheckinoutController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody SXyCheckinoutEntity sXyCheckinout) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<SXyCheckinoutEntity>> failures = validator.validate(sXyCheckinout);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			sXyCheckinoutService.saveOrUpdate(sXyCheckinout);
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
		sXyCheckinoutService.deleteEntityById(SXyCheckinoutEntity.class, id);
	}
	
	
	
	@RequestMapping(params = "pCheckDatagrid")
	public void datagrid(PCheckinoutEntity pCheckinout,HttpServletRequest request, HttpServletResponse response ,Integer userId,String checkDate,DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(PCheckinoutEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, pCheckinout, request.getParameterMap());
		try{
			cq.between("checktime", new SimpleDateFormat("yyyy-MM-dd").parse(checkDate) ,TimerUtils.addDate(checkDate, 1));
			cq.eq("userid", userId);
			cq.addOrder("checktime", SortDirection.asc);
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.pCheckinoutService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	@RequestMapping(params = "pCheckList")
	public ModelAndView userList(HttpServletRequest request,Integer userId,String checkDate) {
		request.setAttribute("userId", userId);
		request.setAttribute("checkDate",checkDate);
		return new ModelAndView("xyoa/checkinout/pCheckinoutList");
	}
}
