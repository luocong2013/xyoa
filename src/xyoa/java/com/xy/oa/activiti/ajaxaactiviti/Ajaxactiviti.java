package com.xy.oa.activiti.ajaxaactiviti;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xy.oa.activiti.absence.service.SXyAbsenceServiceI;
import com.xy.oa.activiti.ajaxaactiviti.entity.Approve;
import com.xy.oa.checkinout.entity.SXyCheckinoutEntity;



	/**   
	 * @Title: Ajaxactiviti  
	 * @Description: 首页中的ajax 得到提醒流程条数
	 * @author xiaoyong
	 * @date 2016-08-11 15:54:46
	 *  
	 *
	 */
	@Controller
	@RequestMapping("/ajaxactiviti")
	public class Ajaxactiviti  extends BaseController {
		/**
		 * Logger for this class
		 */
		@Autowired
		private SXyAbsenceServiceI sXyAbsenceService;
	
		 /**
		 * 首页中的ajax 得到提醒流程条数
		 * @author xiaoyong
		 * @return
		 * @throws Exception 
		 */
		@RequestMapping(params = "getApprove")
		@ResponseBody
		public Approve getApprove(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) throws Exception {
			Approve approve=new Approve();	
			//得到当前用户
			TSUser tsUser = ResourceUtil.getSessionUserName();
			
			SimpleDateFormat dateFormater=new SimpleDateFormat("yyyy-MM-dd");
			
				
			List<SXyCheckinoutEntity> list=null;
			Calendar cal=Calendar.getInstance();//使用日历类	
			cal.setTime(new Date());
			cal.add(Calendar.MONTH,-1);
			cal.set(Calendar.DATE, 21);
			
			String date= dateFormater.format(cal.getTime());
			//获得异常条数
			String hql = "from SXyCheckinoutEntity sc where sc.staffId=? and sc.isCheckTrue='01' and checkDate >='"+date+"'";
			if(tsUser.getUserName()!=null&&!"".equals(tsUser.getUserName())){
				int id=Integer.parseInt(tsUser.getUserName());
		     list= sXyAbsenceService.findHql(hql,id );
			}
			if(list!=null&&list.size()>0)
			approve.setExceptionSum(list.size());
			
			String hrStr = "人事部";
			//查询当前登录用户的任务（以用户编号(userName)查询）			
			String tableName ="s_xy_absence";
			approve.setAbsenceSum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"<>","<>"));		
			approve.setHrAbsenceSum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"=",null));	
			approve.setSelfAbsenceSum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"<>","="));	
			
			//待处理出差审核条数
			tableName ="s_xy_business_trip";
			approve.setBusinesstripSum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"<>","<>"));		
			approve.setHrBusinesstripSum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"=",null));		
			approve.setSelfBusinesstripSum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"<>","="));	
			
			//待处理调休审核条数
			tableName ="s_xy_compensate_leave";
			approve.setCompensateleaveSum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"<>","<>"));				
			approve.setHrCompensateleaveSum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"=",null));	
			approve.setSelfCompensateleaveSum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"<>","="));	
			
			//待处理外出审核条数
			tableName ="s_xy_out_work";
			approve.setOutworkSum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"<>","<>"));					
			approve.setHrOutworkSum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"=",null));		
			approve.setSelfOutworkSum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"<>","="));	
			
			//待处理加班审核条数
			tableName ="s_xy_work_overtime";
			approve.setWorkovertimeSum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"<>","<>"));		
			approve.setHrWorkovertimeSum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"=",null));		
			approve.setSelfWorkovertimeSum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"<>","="));	
			
			//待处理考勤异常审核条数
			tableName ="s_xy_check_apply";
			approve.setCheckApplySum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"<>","<>"));			
			approve.setHrCheckApplySum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"=",null));		
			approve.setSelfCheckApplySum(datagridCheck(tableName,hrStr,tsUser.getUserName(),"<>","="));	
			
			return approve;
		}
		
		public int datagridCheck(String tableName,String hrStr,String userId,String fristSymbol,String nextSymbol) {
			String sql = "SELECT count(1) FROM ACT_RU_TASK T join "+tableName+" S on S.flow_inst_id = T.PROC_INST_ID_ WHERE "+
					"T.NAME_ "+fristSymbol+" '"+hrStr+"' AND T.ASSIGNEE_ = '"+userId+"'";
			if(nextSymbol != null){
				sql +=" AND T.ASSIGNEE_ "+nextSymbol+" S.apply_sttaff_id";
			}
			return sXyAbsenceService.getCountForJdbc(sql).intValue();
			
		}
		
}
