package org.jeecgframework.web.system.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.exception.BusinessException;

import com.xy.oa.calendars.service.SXyCalendarsServiceI;
import com.xy.oa.util.Constants;

public class ApproveSuccUtils {
	private static final Logger logger = Logger.getLogger(ApproveSuccUtils.class);

	public static int getAbsenceApprove(String checkDate,int staffid,Connection conn,Map<String,Object> approveMap,SXyCalendarsServiceI sXyCalendarsService){
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();

			//请假审批
			String tableName ="s_xy_absence";
			String col = "start_time,back_date,absence_type" ;
			String where = " and start_time <= '"+checkDate+"' and back_date >='"+checkDate+"'";
			String sql =getSql(tableName,col,where,staffid);
			rs = stmt.executeQuery(sql);
			String startTime = null ;
			String back_date = null;
			
			int approveMinute = 0;
			String absenceType =null;
			while(rs.next()){
				startTime = rs.getString(1);
				back_date =rs.getString(2);
				absenceType = rs.getString(3);
				approveMinute += (int)(getApproveMinute(startTime,back_date,checkDate,sXyCalendarsService).doubleValue() * 60);
			}
			approveMap.put("checkType", absenceType);
			return approveMinute;
		} catch (Exception e) {
			logger.error("获取请假申请表失败"+e);
			throw new BusinessException("获取请假申请表失败，请重试");
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public static int getTripApprove(String checkDate,int staffid,Connection conn,SXyCalendarsServiceI sXyCalendarsService){
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			
			//待处理出差审核条数
			String tableName ="s_xy_business_trip";
			String col = "trip_start_time,back_date" ;
			String where = " and trip_start_time <= '"+checkDate+"' and back_date >='"+checkDate+"'";
			String sql =getSql(tableName,col,where,staffid);
			rs = stmt.executeQuery(sql);
			String startTime = null ;
			String back_date = null;
			
			int approveMinute = 0;
			while(rs.next()){
				startTime = rs.getString(1);
				back_date =rs.getString(2);
				approveMinute += (int)(getApproveMinute(startTime,back_date,checkDate,sXyCalendarsService).doubleValue() * 60);
			}
			return approveMinute;
		} catch (Exception e) {
			logger.error("获取请假出差表失败"+e);
			throw new BusinessException("获取请假出差表失败，请重试");
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public static int getLeaveApprove(String checkDate,int staffid,Connection conn,SXyCalendarsServiceI sXyCalendarsService){
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			//待处理调休审核条数
			String tableName ="s_xy_compensate_leave";
			String col = "leave_start_time,back_date" ;
			String where = " and leave_start_time <= '"+checkDate+"' and back_date >='"+checkDate+"'";
			String sql =getSql(tableName,col,where,staffid);
			rs = stmt.executeQuery(sql);
			String startTime = null ;
			String back_date = null;
			
			int approveMinute = 0;
			while(rs.next()){
				startTime = rs.getString(1);
				back_date =rs.getString(2);
				approveMinute += (int)(getApproveMinute(startTime,back_date,checkDate,sXyCalendarsService).doubleValue() * 60);
			}
			return approveMinute;
		} catch (Exception e) {
			logger.error("获取请假调休表失败"+e);
			throw new BusinessException("获取请假调休表失败，请重试");
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public static void getOvertimeApprove(String checkDate,int staffid,double overHour ,String startTime,String endTime,Connection conn,boolean isHalf,SXyCalendarsServiceI sXyCalendarsService){
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			//待处理调休审核条数
			String sql ="select apply_start_time,apply_end_time,id,start_time  from  s_xy_work_overtime where apply_sttaff_id ="+staffid+" and flow_state='"+Constants.FLOW_STATE_7+"'  and apply_start_time <= '"+checkDate+"' and apply_end_time >='"+checkDate+"'";
			rs = stmt.executeQuery(sql);			
			while(rs.next()){
				String workstartTime = rs.getString(1);
				String back_date  =rs.getString(2);
				String start_time  =rs.getString(2);

				
				double overtimeHour = getApproveMinute(workstartTime,back_date,checkDate,sXyCalendarsService).doubleValue();
				if(overtimeHour<overHour){
					updateOvertimeApprove(rs.getString(3),overtimeHour , (isHalf ? overtimeHour/2 : overtimeHour) , 
							( start_time == null || start_time.trim().length() ==0)? checkDate+" "+startTime : start_time, checkDate+" "+endTime,conn);
				}else{
					updateOvertimeApprove(rs.getString(3),overHour , (isHalf ? overHour/2 : overHour) ,
							( start_time == null || start_time.trim().length() ==0)? checkDate+" "+startTime : start_time, checkDate+" "+endTime,conn);
					break;
				}
			}
		} catch (Exception e) {
			logger.error("获取外出表失败"+e);
			throw new BusinessException("获取外出表失败，请重试");
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public static void updateOvertimeApprove(String id,double overtimeHour ,double onWorkHour ,String startTime , String endTime ,Connection conn){
		PreparedStatement pst = null;
		String sql = "UPDATE s_xy_work_overtime SET start_time='"+startTime+"',end_time ='"+endTime+"',work_hour ="+overtimeHour+" ,on_work_hour ="+onWorkHour+" where id ='"+id+"'";
		try {
			pst = conn.prepareStatement(sql);
			pst.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			logger.error("更新加班表失败"+e);
			throw new BusinessException("更新加班表失败，请重试");
		} finally {
			try {
				if (pst != null)
					pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static int getOutWorkApprove(String checkDate,int staffid,Connection conn,SXyCalendarsServiceI sXyCalendarsService){
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			//请假审批
			String tableName ="s_xy_out_work";
			String col = "out_start_time,back_date" ;
			String where = " and out_start_time <= '"+checkDate+"' and back_date >='"+checkDate+"'";
			String sql =getSql(tableName,col,where,staffid);
			rs = stmt.executeQuery(sql);
			String startTime = null ;
			String back_date = null;
			
			int approveMinute = 0;
			while(rs.next()){
				startTime = rs.getString(1);
				back_date =rs.getString(2);
				approveMinute += (int)(getApproveMinute(startTime,back_date,checkDate,sXyCalendarsService).doubleValue() * 60);
			}
			return approveMinute;
		} catch (Exception e) {
			logger.error("获取外出表失败"+e);
			throw new BusinessException("获取外出表失败，请重试");
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	
	

	/**
	 * 考勤异常的情况，查询是是否存在审批通过的相关流程，若存在，判断是否考勤异常
	 * @param checkDate
	 * @param staffid
	 * @return
	 */
	public static Map<String,Object> checkApproveSucc(String checkDate,int staffid,int exceptionMinute,Connection conn,SXyCalendarsServiceI sXyCalendarsService){
		Map<String,Object> approveMap = new HashMap<String,Object> ();
		int approveMinute = getAbsenceApprove (checkDate,staffid,conn,approveMap,sXyCalendarsService);
		if(approveMinute>0){
			if(exceptionMinute <= approveMinute){
				approveMap.put("check_remarks", "请假");
				approveMap.put("isTrue", Constants.CHECKINOUT_IS_TRUE);
				return approveMap;
			}
		}
		
		approveMinute += getTripApprove (checkDate,staffid,conn,sXyCalendarsService); 
		if(approveMinute>0){
			if(exceptionMinute <= approveMinute){
				approveMap.put("checkType", Constants.XY_CHECK_TYPE_17);
				approveMap.put("check_remarks", "出差");
				approveMap.put("isTrue", Constants.CHECKINOUT_IS_TRUE);
				return approveMap;
			}
		}
		
		approveMinute += getLeaveApprove (checkDate,staffid,conn,sXyCalendarsService);
		if(approveMinute>0){
			if(exceptionMinute <= approveMinute){
				approveMap.put("checkType", Constants.XY_CHECK_TYPE_19);
				approveMap.put("check_remarks", "调休");
				approveMap.put("isTrue", Constants.CHECKINOUT_IS_TRUE);
				return approveMap;
			}
		}
		
		approveMinute += getOutWorkApprove (checkDate,staffid,conn,sXyCalendarsService); 
		if(approveMinute>0){
			if(exceptionMinute <= approveMinute){
				approveMap.put("checkType", Constants.XY_CHECK_TYPE_18);
				approveMap.put("check_remarks", "外出");
				approveMap.put("isTrue", Constants.CHECKINOUT_IS_TRUE);
				return approveMap;
			}
		}
		
		approveMap.put("exceptionMinute", exceptionMinute-approveMinute);
		
		return approveMap;
	}
	
	public static String getSql(String tableName,String col,String where,int staffid){
		return "select "+col+" from " +tableName+" where apply_sttaff_id ="+staffid+" and flow_state='"+Constants.FLOW_STATE_7+"' "+where;
	}
	
	public static BigDecimal getApproveMinute(String startTime,String endTime ,String checkDate,SXyCalendarsServiceI sXyCalendarsService) throws Exception{
		if(startTime == null || endTime == null)
			return new BigDecimal(0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = sdf1.parse(startTime);
		Date endDate= sdf1.parse(endTime);
		Date check = sdf1.parse(checkDate);

		if(startDate.equals(check))
			return  sXyCalendarsService.getTimeInterval(sdf.parse(startTime),sdf.parse(checkDate+" "+Constants.XY_END_WORK_TIME),false);
		if(endDate.equals(check))
			return  sXyCalendarsService.getTimeInterval(sdf.parse(checkDate+" "+Constants.XY_START_WORK_TIME),sdf.parse(endTime),false);
		return  sXyCalendarsService.getTimeInterval(sdf.parse(startTime),sdf.parse(endTime),false);

	}
	
	public static double getWorkHour(int workMinute){
		int hourInt = workMinute / 60;
		double hourDouble = workMinute / 60.00;

		if (hourDouble - hourInt >= 0.5) {
			return hourInt + 0.5;
		} else {
			return hourInt;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getWorkHour(450));
	}
}
