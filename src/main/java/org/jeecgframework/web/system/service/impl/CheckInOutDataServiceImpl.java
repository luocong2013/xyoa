package org.jeecgframework.web.system.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.timer.DynamicTask;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.pojo.base.TSTimeTaskEntity;
import org.jeecgframework.web.system.service.CheckInOutDataService;
import org.jeecgframework.web.system.service.TimeTaskServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xy.oa.calendars.service.SXyCalendarsServiceI;
import com.xy.oa.checkinout.entity.SXyCheckinoutEntity;
import com.xy.oa.util.Constants;
import com.xy.oa.util.TimerUtils;

/**
 * 金融部：lastEndTime>=21:00:00&&changeCount=5&&changeMinute=60 OR startTime
 * <=09:15:00&&weekWorkMinute==15&&changeCount==5
 * 运营部：lastEndTime>=23:00:00&&changeMinute=120 OR startTime
 * <=09:15:00&&changeCount==10&&workMinute==15
 * 
 * @author masp
 *
 */
@Service("checkInOutDataService")
@Transactional
public class CheckInOutDataServiceImpl extends CommonServiceImpl implements CheckInOutDataService {
	private static final Logger logger = Logger.getLogger(CheckInOutDataServiceImpl.class);
	private SXyCheckinoutEntity updateOldCheck;
	
	private int changeMinute = 0;
	
	@Autowired
	private DynamicTask dynamicTask;
	@Autowired
	private TimeTaskServiceI timeTaskService;
	@Autowired
	private SXyCalendarsServiceI sXyCalendarsService;
	
	@Override
	public void checkInOut(String batchDate) {
		// 获取批处理日期
		if (batchDate == null) {
			throw new BusinessException("批处理失败，失败原因：获取批处理日期失败");
		}
		
		if(batchDate.equals((new SimpleDateFormat("yyyy-MM-dd")).format(new Date()))){
			List<TSTimeTaskEntity> ls = commonDao.findHql("from TSTimeTaskEntity where taskId = ? ",Constants.DATA_IMP_TASK_ID);
			if(ls == null || ls.size() ==0){
				throw new BusinessException("批处理失败，失败原因：未配置数据导入任务（“"+Constants.DATA_IMP_TASK_ID+"”）");
			}
			//停止批处理任务
			TSTimeTaskEntity timeTask = ls.get(0);
			boolean isSuccess = dynamicTask.startOrStop(timeTask.getTaskId() ,false);
			if(isSuccess){
				timeTask.setIsStart("0");
				timeTaskService.updateEntitie(timeTask);
			}
			
			return;
		}
		
		Connection conn = null;
		try {
			conn = SessionFactoryUtils.getDataSource(getSession().getSessionFactory()).getConnection();
			conn.setAutoCommit(false);

			// 获取带考勤的员工信息
			List<SXyCheckinoutEntity> listEntity = getXYStaffData(conn,batchDate);
			// 获取当日的考勤记录
			List<PCheckInOutPO> listPo = queryCheckInOutData(conn, batchDate);
			// 获取部门考勤规则
			Map<String, String> mapRule = queryCheckRule(conn);
			// 获取日期类型
			String dateType = getDateType(conn, batchDate);
			
			//获取多班制度的员工信息
			Map<Integer, String> manyWorkUser = getManyWorkUser(batchDate,conn);
			
			// 获取带考勤的员工信息
			deleteCheckInOut(batchDate, conn);
			
			// 插入员工考勤
			insertSXYCheckInOutDate(listEntity, listPo, mapRule, dateType, batchDate,manyWorkUser,conn);
			// 更新批处理日期
			updateBatchDate(batchDate, conn);
		} catch (Exception e) {
			logger.error("考勤处理失败，失败原因：",e);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public Map<Integer, String>  getManyWorkUser(String batchDate,Connection conn){
		String query = "select staff_id,schedule_type from xy_work_schedule where schedule_day ='"+batchDate+"'";//todo
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			Map<Integer, String>  mapPo = new HashMap<Integer,String>();
			while (rs.next()) {
				mapPo.put(rs.getInt(1),rs.getString(2));
			}
			return mapPo;
		} catch (Exception e) {
			logger.error("查询 xy_work_schedule 失败："+e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	public void deleteCheckInOut(String batchDate, Connection conn) throws ParseException{
		String delete="delete from s_xy_checkinout "
				+ " WHERE  check_date ='"+batchDate+"'"; 
		PreparedStatement pst = null; 
		try{
			pst = conn.prepareStatement(delete);
			pst.executeUpdate();
			conn.commit();
		}catch(Exception e){
			logger.error("删除  s_xy_checkinout 失败："+e);
		}finally{
			try{
				if(pst != null) pst.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}    
	}
	
	public void updateBatchDate(String batchDate, Connection conn) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date date = sdf.parse(batchDate);
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(date);
		startCal.add(Calendar.DAY_OF_MONTH, 1);

		PreparedStatement pst = null;
		String sql = "UPDATE s_xy_sys_conf SET cur_values='"+sdf.format(startCal.getTime())+"',last_values ='"+batchDate+"' WHERE sys_code ='100001'";
		try {

			pst = conn.prepareStatement(sql);
			pst.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			logger.error("更新批处理日期失败，失败原因："+e);
		} finally {
			try {
				if (pst != null)
					pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	/** 考勤数据插入考勤统计表 */
	public List<PCheckInOutPO> queryCheckInOutData(Connection conn, String batchDate) {
		String query = "select userid,date_format(checktime,'%Y-%m-%d'),date_format(MIN(checktime),'%H:%i:%s'),date_format(MAX(checktime),'%H:%i:%s') FROM p_checkinout "
				+ " WHERE  checktime LIKE '" + batchDate + "%' GROUP BY USERID";
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			List<PCheckInOutPO> listPo = new ArrayList<PCheckInOutPO>();
			while (rs.next()) {
				PCheckInOutPO po = new PCheckInOutPO();
				po.setUserid(rs.getInt(1));
				po.setCheckDate(rs.getString(2));
				po.setStartTime(rs.getString(3));
				po.setEndTime(rs.getString(4));
				listPo.add(po);
			}
			return listPo;
		} catch (Exception e) {
			logger.error("查询  p_checkinout 失败："+e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 更新日期类型
	 */
	public List<SXyCheckinoutEntity> getXYStaffData(Connection conn,String currDate) {
		String sql = "SELECT sxys.sttaff_id,sxys.name,tsd.id,sxys.check_id "
				+ "from  s_xy_staff sxys join t_s_depart tsd on  sxys.dept_id = tsd.id" 
				+ " WHERE sxys.is_check = 1 and (state > 0 or (state = 0 and sxys.abdicate_date >='"+currDate+"'))"
				+ " and go_xy_date <'"+currDate+"'";
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			List<SXyCheckinoutEntity> list = new ArrayList<SXyCheckinoutEntity>();
			while (rs.next()) {
				SXyCheckinoutEntity sXyCheckinoutEntity = new SXyCheckinoutEntity();
				sXyCheckinoutEntity.setStaffId(rs.getInt(1));
				sXyCheckinoutEntity.setName(rs.getString(2));
				sXyCheckinoutEntity.setDeptId(rs.getString(3));
				sXyCheckinoutEntity.setUserId(rs.getInt(4));

				list.add(sXyCheckinoutEntity);
			}
			return list;
		} catch (SQLException e) {
			logger.error("查询  s_xy_staff 失败："+e);
			throw new BusinessException("查询  s_xy_staff 失败");
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 更新日期类型
	 */
	public String getDateType(Connection conn, String currDate) {
		String sql = "select calendartype from  s_xy_calendars " + "WHERE date_format(calendarday,'%Y-%m-%d') = '"
				+ currDate + "'";
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next())
				return rs.getString(1);
		} catch (Exception e) {
			logger.error("查询  s_xy_calendars 失败："+e);
			throw new BusinessException("查询  s_xy_calendars 失败");
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/** 获取工作日考勤状态 */
	public Map<String, String> queryCheckRule(Connection conn) {
		String sql = "SELECT d.OUT_RULE, c.id  FROM S_XY_DEPT_CHEAK_RULE d LEFT JOIN t_s_depart c ON d.dept_id = c.id OR d.dept_id = c.parentdepartid";
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			Map<String, String> map = new HashMap<String, String>();
			while (rs.next()) {
				map.put(rs.getString(2), rs.getString(1));
			}
			return map;
		} catch (Exception e) {
			logger.error("查询  S_XY_DEPT_CHEAK_RULE 失败："+e);
			throw new BusinessException("查询  S_XY_DEPT_CHEAK_RULE 失败");
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/** 更新考勤是否正常 */
	public void updateOldCheckinout(Connection conn,String batchDate) throws ParseException {
		if (updateOldCheck != null && 
				updateOldCheck.getStaffId() != null &&
				updateOldCheck.getCheckDate() != null && 
				!(batchDate.equals(updateOldCheck.getCheckDate()))) {
			PreparedStatement pst = null;
			String sql = "UPDATE s_xy_checkinout SET is_use=1 WHERE staff_id=? and check_date=? ";
			try {

				pst = conn.prepareStatement(sql);
				pst.setInt(1, updateOldCheck.getStaffId());
				pst.setString(2, updateOldCheck.getCheckDate());
				pst.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				logger.error("更新  s_xy_checkinout 失败："+e);
				throw new BusinessException("更新  s_xy_checkinout 失败");
			} finally {
				try {
					if (pst != null)
						pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 金融产品部判断前一天是否加班到指定下班点后
	 * 
	 * @param checkdate
	 * @param staffid
	 * @return true为21点以后 false为21点以前
	 * @throws SQLException
	 */
	public String getLastDayEndWorkTime(String checkdate, int staffid, Connection conn) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ResultSet rs = null;
		Statement stmt = null;
		try {
			Date date = sdf.parse(checkdate);
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(date);
			startCal.add(Calendar.DAY_OF_MONTH, -1);
			Date date1 = startCal.getTime();
			String sql = "SELECT off_work_time FROM s_xy_checkinout where date_format(check_date,'%Y-%m-%d')= '"
					+ sdf.format(date1) + "' and  staff_id= " + staffid;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next())
				return rs.getString(1);
		} catch (Exception e) {
			logger.error("查询前一日  s_xy_checkinout 失败："+e);
			throw new BusinessException("查询前一日  s_xy_checkinout 失败");
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
		return Constants.XY_END_WORK_TIME;
	}

	/**
	 * 插入考勤
	 * 
	 * @param rs
	 * @throws ParseException
	 */
	public void insertSXYCheckInOutDate(List<SXyCheckinoutEntity> listEntity, List<PCheckInOutPO> listPo,
			Map<String, String> mapRule, String dateType,String batchDate, Map<Integer, String> manyWorkUser ,Connection conn) throws Exception {

		// 重置员工ID
		String sql = "insert into s_xy_checkinout(user_id,staff_id,check_date," 
				+ "work_time,off_work_time,dept_id,"
				+ "exception_minute,exception_remarks,is_check_true,"
				+ "date_type,work_hour,name,"
				+ "check_type,check_remarks,is_use,"
				+ "late_minute,earlier_minute,"
				+ "c_time,c_user)" + "values (?,?,?," 
				+ "?,?,?,"
				+ "?,?,?," 
				+ "?,?,?,"
				+ "?,?,?,"
				+ "?,?,"
				+ "now(),999999)";
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			int i = 0;
			for (SXyCheckinoutEntity entity : listEntity) {
				changeMinute = 0;
				boolean isExistCheckInOut = false;
				boolean isManyWork = false;
				String manyWorkType = null;
				//判断是否是存在多班制度
				if(manyWorkUser != null){//todo
					manyWorkType = manyWorkUser.get(entity.getStaffId());
					if(manyWorkType != null){
						isManyWork = true;

					}
				}
				po:for (PCheckInOutPO po : listPo) {
					updateOldCheck = new SXyCheckinoutEntity();
					SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

					if (entity.getUserId().equals(po.getUserid())) {
						isExistCheckInOut = true;
						i++;
						pst.setInt(1, po.getUserid());
						pst.setInt(2, entity.getStaffId());
						pst.setString(3, po.getCheckDate());
						pst.setString(4, po.getStartTime());
						pst.setString(5, po.getEndTime());

						pst.setString(6, entity.getDeptId());
						pst.setString(10, dateType); // 日期类型
						pst.setString(12, entity.getName()); // 工作日默认为0
						
						if(isManyWork){
							if(Constants.XY_WORK_TYPE_04.equals(manyWorkType)){//加班
								pst.setBigDecimal(7, new BigDecimal(0)); // 计算异常分钟数
								pst.setString(8, null); // 异常原因
								pst.setString(9, Constants.CHECKINOUT_IS_TRUE); // 考勤是否正常

								Integer workMinute = TimerUtils.getManyWorkMinute(po.getStartTime(), po.getEndTime());
								pst.setBigDecimal(11, new BigDecimal(ApproveSuccUtils.getWorkHour(workMinute)));
								
								pst.setString(13, Constants.XY_CHECK_TYPE_00);
								pst.setString(14, "正常上下班");
								pst.setString(15, "1");
								pst.setInt(17, 0);
								pst.setInt(16, 0);
								pst.addBatch();

								ApproveSuccUtils.getOvertimeApprove(batchDate,entity.getStaffId(),ApproveSuccUtils.getWorkHour(workMinute) ,po.getStartTime(),po.getEndTime(),conn,true,sXyCalendarsService);
								
								break po;
							}
							
							
							Integer lateMinute = 0;
							Integer earlierMinute =0;
							
							//查询当前员工上班时段（01-早班，02-晚班,03-休息，04-加班）
							if(Constants.XY_WORK_TYPE_01.equals(manyWorkType)){//早班
								pst.setString(10, Constants.DATE_TYPE_B); // 日期类型
								if (cheakRuleValue(time.parse(Constants.XY_START_WORK_TIME).getTime(), time.parse(po.getStartTime()).getTime(),"<")){
									lateMinute = TimerUtils.getManyWorkMinute( Constants.XY_START_WORK_TIME,po.getStartTime());
								}
								
								if (cheakRuleValue(time.parse(Constants.XY_END_WORK_TIME).getTime(), time.parse(po.getEndTime()).getTime(),">")){
									earlierMinute = TimerUtils.getManyWorkMinute(po.getEndTime(),Constants.XY_END_WORK_TIME_NIGHT);
								}
							}
							
							if(Constants.XY_WORK_TYPE_02.equals(manyWorkType)){//晚班
								pst.setString(10, Constants.DATE_TYPE_B); // 日期类型
								if (cheakRuleValue(time.parse(Constants.XY_START_WORK_TIME_NIGHT).getTime(), time.parse(po.getStartTime()).getTime(),"<")){
									lateMinute = TimerUtils.getManyWorkMinute(Constants.XY_START_WORK_TIME,po.getStartTime());
								}
								
								if (cheakRuleValue(time.parse(Constants.XY_END_WORK_TIME_NIGHT).getTime(), time.parse(po.getEndTime()).getTime(),">")){
									earlierMinute = TimerUtils.getManyWorkMinute(po.getEndTime(),Constants.XY_END_WORK_TIME_NIGHT);
								}
							}
							pst.setBigDecimal(7, new BigDecimal(0)); // 计算异常分钟数
							pst.setString(8, null); // 异常原因
							pst.setString(9, Constants.CHECKINOUT_IS_TRUE); // 考勤是否正常
							pst.setBigDecimal(11, new BigDecimal(0)); // 工作日默认为0
							pst.setString(13, Constants.XY_CHECK_TYPE_00);
							pst.setString(14, "正常上下班");
							pst.setString(15, "1");
							pst.setInt(16, 0);
							pst.setInt(17, 0);
							
							if(lateMinute>0){
								pst.setString(8, "迟到"); // 异常原因
								pst.setString(9, Constants.CHECKINOUT_IS_FALSE); // 考勤是否正常
								pst.setString(13, Constants.XY_CHECK_TYPE_11);
								pst.setString(14, "迟到");
								pst.setInt(16, lateMinute);
								pst.setInt(17, 0);
							}
							
							if(earlierMinute>0){
								if(lateMinute>0){
									pst.setString(13, Constants.XY_CHECK_TYPE_13);
									pst.setString(14, "迟到和早退");
									pst.setString(8, "早退和早退"); // 异常原因
								}else{
									pst.setString(13, Constants.XY_CHECK_TYPE_12);
									pst.setString(14, "早退");
									pst.setString(8, "早退"); // 异常原因
									pst.setString(9, Constants.CHECKINOUT_IS_FALSE); // 考勤是否正常
									pst.setInt(16, 0);
								}
								pst.setInt(17, earlierMinute);
							}
							
							if(Constants.XY_WORK_MINUTE.compareTo(lateMinute+earlierMinute) <= 0){
								pst.setString(13, Constants.XY_CHECK_TYPE_14);
								pst.setString(14, null);
								pst.setString(8, null); // 异常原因
							}
							
							if(lateMinute+earlierMinute >0){
								Map<String,Object> map = ApproveSuccUtils.checkApproveSucc(batchDate,entity.getStaffId(),(lateMinute+earlierMinute),conn,sXyCalendarsService);
								if(map.get("isTrue") != null ){
									if(Constants.CHECKINOUT_IS_TRUE.equals(map.get("isTrue").toString())){
										pst.setString(13, map.get("checkType").toString());
										pst.setString(14, map.get("check_remarks").toString());
										pst.setString(9, Constants.CHECKINOUT_IS_TRUE); // 考勤是否正常
										pst.setInt(16, 0);
										pst.setInt(17, 0);
										pst.setBigDecimal(7, new BigDecimal(0)); // 计算异常分钟数
									}
									
								}else{
									pst.setBigDecimal(7, new BigDecimal((Integer)map.get("exceptionMinute")) ); // 计算异常分钟数
								}	
							}
							
							pst.addBatch();
							break po;
						}
						
						
						if (Constants.DATE_TYPE_B.equals(dateType)) {// 工作日计算考勤异常
							Map<String, Object> exceptionMap = getExceptionCheck(entity, po, mapRule, conn);
							pst.setBigDecimal(7, new BigDecimal((Integer) exceptionMap.get("exceptionMinute"))); // 计算异常分钟数
							pst.setString(8, (String) exceptionMap.get("exceptionRemarks")); // 异常原因
							pst.setString(9, (String) exceptionMap.get("isCheckTrue")); // 考勤是否正常
							pst.setBigDecimal(11, new BigDecimal(0)); // 工作日默认为0
							if(updateOldCheck == null || updateOldCheck.getStaffId() == null){
								if(Constants.CHECKINOUT_IS_TRUE.equals(exceptionMap.get("isCheckTrue").toString())){
									pst.setString(13, Constants.XY_CHECK_TYPE_00);
									pst.setString(14, "正常上下班");
									pst.setInt(17, 0);
									pst.setInt(16, 0);
								}else{
									Integer earlierMinute =0;
									
									if(Constants.XY_WORK_MINUTE <= (Integer) exceptionMap.get("exceptionMinute")){
										pst.setBigDecimal(7, new BigDecimal(Constants.XY_WORK_MINUTE)); // 计算异常分钟数
										pst.setString(8, "缺勤"); // 异常原因
										pst.setString(13, Constants.XY_CHECK_TYPE_14);
										pst.setString(14, "缺勤");
										pst.setInt(16, 0);
										pst.setInt(17,0);
									} else{
										if (!cheakRuleValue(time.parse(Constants.XY_END_WORK_TIME).getTime(), time.parse(po.getEndTime()).getTime(),"<")){
											earlierMinute = TimerUtils.getManyWorkMinute(po.getEndTime(), Constants.XY_END_WORK_TIME);
											Integer lateMinut = (Integer) exceptionMap.get("exceptionMinute") - earlierMinute;
											if(earlierMinute> (Integer) exceptionMap.get("exceptionMinute") ){
												earlierMinute = (Integer) exceptionMap.get("exceptionMinute");
												lateMinut = Constants.XY_WORK_MINUTE - earlierMinute;
											}
											pst.setString(13, Constants.XY_CHECK_TYPE_12);
											pst.setString(14, "早退");
											pst.setInt(16, lateMinut);
											pst.setInt(17,earlierMinute);
										}else{
											pst.setString(13, Constants.XY_CHECK_TYPE_11);
											pst.setString(14, "迟到");
											pst.setInt(16, (Integer) exceptionMap.get("exceptionMinute"));
											pst.setInt(17,earlierMinute);										
										}
									}
									
									//检查是否存在审批信息
									if((Integer) exceptionMap.get("exceptionMinute") >0){
										Map<String,Object> map = ApproveSuccUtils.checkApproveSucc(batchDate,entity.getStaffId(),(Integer) exceptionMap.get("exceptionMinute"),conn,sXyCalendarsService);
										if(map.get("isTrue") != null && Constants.CHECKINOUT_IS_TRUE.equals(map.get("isTrue").toString())){
											pst.setString(13, map.get("checkType").toString());
											pst.setString(14, map.get("check_remarks").toString());
											pst.setString(9, Constants.CHECKINOUT_IS_TRUE); // 考勤是否正常
											pst.setInt(16, 0);
											pst.setInt(17, 0);
											pst.setBigDecimal(7, new BigDecimal(0) ); // 计算异常分钟数
										}	
									}
										
								}
								pst.setString(15, "0");
								
							}else{
								pst.setString(13, updateOldCheck.getCheckType());
								if (Constants.XY_CHECK_TYPE_09.equals(updateOldCheck.getCheckType())) {
									pst.setString(14, "推迟打卡");
								}
								if (Constants.XY_CHECK_TYPE_10.equals(updateOldCheck.getCheckType())) {
									pst.setString(14, "前日加班");
								}
								
								if(batchDate.equals(updateOldCheck.getCheckDate())){
									pst.setString(15, "1");
								}else{
									pst.setString(15, "0");
								}
								pst.setInt(17, 0);
								pst.setInt(16, 0);
								
							}
							// 更新前日期的状态
							pst.addBatch();
							updateOldCheckinout(conn,batchDate);
						} else {// 非工作日计算加班小时数,并且不存在考勤异常的情况
							pst.setBigDecimal(7, new BigDecimal(0)); // 计算异常分钟数
							pst.setString(8, null); // 异常原因
							pst.setString(9, Constants.CHECKINOUT_IS_TRUE); // 考勤是否正常

							Integer workMinute = TimerUtils.getWorkMinute(po.getStartTime(), po.getEndTime());
							
							pst.setBigDecimal(11, new BigDecimal(ApproveSuccUtils.getWorkHour(workMinute)));
							ApproveSuccUtils.getOvertimeApprove(batchDate,entity.getStaffId(),ApproveSuccUtils.getWorkHour(workMinute) ,po.getStartTime(),po.getEndTime(),conn,true,sXyCalendarsService);

							
							pst.setString(13, Constants.XY_CHECK_TYPE_00);
							pst.setString(14, "正常上下班");
							pst.setString(15, "1");
							pst.setInt(17, 0);
							pst.setInt(16, 0);
							pst.addBatch();
						}
						break po;
					}
				}
				
				
				if((!isExistCheckInOut && isManyWork && (Constants.XY_WORK_TYPE_01.equals(manyWorkType) || Constants.XY_WORK_TYPE_02.equals(manyWorkType)))
						||(!isExistCheckInOut && !isManyWork && Constants.DATE_TYPE_B.equals(dateType))){
					i++;
					pst.setInt(1, entity.getUserId());
					pst.setInt(2, entity.getStaffId());
					pst.setString(3, batchDate);
					pst.setString(4, null);
					pst.setString(5, null);
					pst.setString(6, entity.getDeptId());
					pst.setString(10, dateType); // 日期类型
					
					pst.setBigDecimal(7, new BigDecimal(450)); // 计算异常分钟数
					pst.setString(8, "缺勤"); // 异常原因
					pst.setString(9, Constants.CHECKINOUT_IS_FALSE); // 考勤是否正常
					pst.setBigDecimal(11, new BigDecimal(0)); // 工作日默认为0
					pst.setString(12, entity.getName()); // 工作日默认为0
					pst.setString(13, Constants.XY_CHECK_TYPE_14);
					pst.setString(14, "缺勤");
					pst.setString(15, "0");
					pst.setInt(17, 0);
					pst.setInt(16, 0);
					pst.addBatch();
				}
				// 100条记录插入一次
				if (i % 100 == 0) {
					pst.executeBatch();
					conn.commit();
				}
			}
			
			// 最后插入不足100条的数据
			pst.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			logger.error("考勤处理异常，异常原因：",e);
			throw new BusinessException("考勤处理异常，异常原因");
		} catch (Exception e) {
			logger.error("考勤处理异常，异常原因：",e);
			throw new BusinessException("考勤处理异常，异常原因");
		} finally {
			try {
				if (pst != null)
					pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
		

	private Map<String, Object> getExceptionCheck(SXyCheckinoutEntity entity, PCheckInOutPO po,
			Map<String, String> mapRule, Connection conn) throws Exception {
		Map<String, Object> exceptionMap = new HashMap<String, Object>();

		if (mapRule.get(entity.getDeptId()) == null || "".equals(mapRule.get(entity.getDeptId()))) { // 该部门存在规则时，默认为9：00
																										// 上班。18点下班，若不满足上班条件，则计算异常时间，异常原因。异常状态
			Integer exceptionMinute = TimerUtils.getExceptionMinute(po.getStartTime(), po.getEndTime(), 0);
			if (exceptionMinute > 0) {
				if (exceptionMinute.equals(Constants.XY_WORK_MINUTE)) {
					exceptionMap.put("exceptionRemarks", "缺勤"); // 异常原因
				} else {
					exceptionMap.put("exceptionRemarks", "迟到或早退"); // 异常原因
				}
				exceptionMap.put("exceptionMinute", exceptionMinute); // 计算异常分钟数
				exceptionMap.put("isCheckTrue", Constants.CHECKINOUT_IS_FALSE); // 考勤是否正常
			} else { // 正常上下班
				exceptionMap.put("exceptionMinute", 0);
				exceptionMap.put("exceptionRemarks", null);
				exceptionMap.put("isCheckTrue", Constants.CHECKINOUT_IS_TRUE);
			}
			return exceptionMap;
		} else {
			SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

			if (cheakRuleValue(time.parse(Constants.XY_END_WORK_TIME).getTime(), time.parse(po.getEndTime()).getTime(),
					">")) {
				Integer exceptionMinute = TimerUtils.getExceptionMinute(po.getStartTime(), po.getEndTime(), 0);
				exceptionMap.put("exceptionMinute", exceptionMinute);
				exceptionMap.put("exceptionRemarks", "迟到或早退");
				exceptionMap.put("isCheckTrue", Constants.CHECKINOUT_IS_FALSE);
				return exceptionMap;
			}

			if (cheakRuleValue(time.parse(Constants.XY_START_WORK_TIME).getTime(),
					time.parse(po.getStartTime()).getTime(), ">=")) {
				exceptionMap.put("exceptionMinute", 0);
				exceptionMap.put("exceptionRemarks", "正常上下班");
				exceptionMap.put("isCheckTrue", Constants.CHECKINOUT_IS_TRUE);
				return exceptionMap;
			}
			
			String ruleStr = mapRule.get(entity.getDeptId());
			if(ruleStr.indexOf(Constants.XY_WORK_CHANGE_MINUTE)>=0 &&
					ruleStr.indexOf(Constants.XY_WORK_CHANGE_COUNT)<0){
				changeMinute = -1;
			}

			String[] lateRo = ruleStr.split(Constants.XY_RULE_OR);
			for (String andRule : lateRo) {
				if (getRuleValue(andRule, entity, po, conn)) {
					exceptionMap.put("exceptionMinute", 0);
					exceptionMap.put("exceptionRemarks", "正常上下班");
					exceptionMap.put("isCheckTrue", Constants.CHECKINOUT_IS_TRUE);
					return exceptionMap;
				}
			}

			if(changeMinute >0){
				exceptionMap.put("exceptionMinute", TimerUtils.getExceptionMinute(po.getStartTime(), po.getEndTime(), changeMinute));
			}else{
				exceptionMap.put("exceptionMinute", TimerUtils.getExceptionMinute(po.getStartTime(), po.getEndTime(), 0));
			}
			
			exceptionMap.put("exceptionRemarks", "迟到或早退");
			exceptionMap.put("isCheckTrue", Constants.CHECKINOUT_IS_FALSE);
			return exceptionMap;
		}
	}

	private boolean getRuleValue(String ruleStr, SXyCheckinoutEntity entity, PCheckInOutPO po, Connection conn)
			throws Exception {
		boolean ruleFlg = true;
		String[] lateAnd = ruleStr.split(Constants.XY_RULE_AND);

		String[] compStr = { "<=", ">=", "<", ">", "==" };
		for (String rule : lateAnd) {
			comp:for (String comp : compStr) {
				if (rule.split(comp).length > 1) {
					if(!cheakRuleName(entity, po, comp, rule, rule.split(comp)[1], conn)){
						return false;
					}
					break comp;
				}
			}
		}
		return ruleFlg;
	}

	private boolean cheakRuleName(SXyCheckinoutEntity entity, PCheckInOutPO po, String comp, String rule, String value,
			Connection conn) throws Exception {
		SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

		if (rule.indexOf(Constants.XY_WORK_START_TIME) != -1) {
			return cheakRuleValue(time.parse(po.getStartTime()).getTime(), time.parse(value).getTime(), comp);
		}

		if (rule.indexOf(Constants.XY_WORK_CHANGE_COUNT) != -1) {
			// 获取单月已使用次数
			long chengeCount = getChangeCount(po.getCheckDate(), entity.getStaffId(), Constants.XY_CHECK_TYPE_09, conn);
			return cheakRuleValue(chengeCount, Long.parseLong(value),comp);

		}

		if (rule.indexOf(Constants.XY_WORK_LAST_CHANGE_COUNT) != -1) {
			// 获取单月加班已使用次数
			long chengeCount = getChangeCount(po.getCheckDate(), entity.getStaffId(), Constants.XY_CHECK_TYPE_10, conn);
			return cheakRuleValue( chengeCount,Long.parseLong(value), comp);
		}

		if (rule.indexOf(Constants.XY_WORK_LAST_END_TIME) != -1) {
			// 获取前一日的下班时间
			String lastEndWorkTime = getLastDayEndWorkTime(po.getCheckDate(), entity.getStaffId(), conn);
			if(StringUtil.isEmpty(lastEndWorkTime))
				return false;
			if(cheakRuleValue(time.parse(lastEndWorkTime).getTime(),time.parse(value).getTime(),  comp)){
				updateOldCheck.setStaffId(entity.getStaffId());
				updateOldCheck.setCheckDate(po.getCheckDate());
				updateOldCheck.setCheckType(Constants.XY_CHECK_TYPE_10);
				return true;
			}
			return false;
		}

		if (rule.indexOf(Constants.XY_WORK_CHANGE_MINUTE) != -1) {
			Integer exceptionMinute = TimerUtils.getExceptionMinute(po.getStartTime(), po.getEndTime(),
					Integer.parseInt(value));
			if(changeMinute <0){
				changeMinute = Integer.parseInt(value);
			}
			return exceptionMinute <= 0;
		}

		if (rule.indexOf(Constants.XY_WORK_WORK_MINUTE) != -1) {
			// 获取当前星期已加班的天对应的加班时间
			Integer exceptionMinute = TimerUtils.getExceptionMinute(po.getStartTime(), po.getEndTime(),
					Integer.parseInt(value));
			if (exceptionMinute <= 0) {

				updateOldCheck.setStaffId(entity.getStaffId());
				updateOldCheck.setCheckDate(po.getCheckDate());
				updateOldCheck.setCheckType(Constants.XY_CHECK_TYPE_09);
				return true;
			} else {
				String lastOffWorkTime = getWorkMinute(po.getCheckDate(), entity.getStaffId(), conn);
				if(StringUtil.isEmpty(lastOffWorkTime))
					return false;
				Integer lastExceptionMinute = TimerUtils.getExceptionMinute(po.getStartTime(), lastOffWorkTime,
						Integer.parseInt(value));
				if (lastExceptionMinute <= 0) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date date = sdf.parse(po.getCheckDate());
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.add(Calendar.DAY_OF_MONTH, -1);
					String lastTime = sdf.format(cal.getTime());

					updateOldCheck.setStaffId(entity.getStaffId());
					updateOldCheck.setCheckDate(lastTime);
					updateOldCheck.setCheckType(Constants.XY_CHECK_TYPE_09);
					return true;
				}
			}
			return false;
		}
		if (rule.indexOf(Constants.XY_WORK_WEEK_WORK_MINUTE) != -1) {
			// 获取当前星期已加班的天对应的加班时间
			Integer exceptionMinute = TimerUtils.getExceptionMinute(po.getStartTime(), po.getEndTime(),
					Integer.parseInt(value));
			if (exceptionMinute <= 0) {
				updateOldCheck.setStaffId(entity.getStaffId());
				updateOldCheck.setCheckDate(po.getCheckDate());
				updateOldCheck.setCheckType(Constants.XY_CHECK_TYPE_09);
				return true;
			} else {
				Map<String, String> map = getWeekWorkMinute(po.getCheckDate(), entity.getStaffId(), conn);
	
				for (Map.Entry<String, String> entry : map.entrySet()) {
					Integer lastExceptionMinute = TimerUtils.getExceptionMinute(po.getStartTime(), entry.getValue(),
							Integer.parseInt(value));
					if (lastExceptionMinute <= 0) {
						updateOldCheck.setStaffId(entity.getStaffId());
						updateOldCheck.setCheckDate(entry.getValue());
						updateOldCheck.setCheckType(Constants.XY_CHECK_TYPE_09);
						return true;
					}
				}
				return false;
			}
		}
		return false;
	}

	/**
	 * 获取昨日下班打卡时间次数
	 * 
	 * @param checkdate
	 * @param staffid
	 * @return 使用次数
	 */
	private String getWorkMinute(String checkdate, int staffid, Connection conn) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ResultSet rs = null;
		Statement stmt = null;
		try {
			Date date = sdf.parse(checkdate);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			String time = sdf.format(cal.getTime());

			String sql = "SELECT off_work_time FROM s_xy_checkinout where is_use=0 and check_date = '" + time
					+ "' and  staff_id= " + staffid;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next())
				return rs.getString(1);
		} catch (Exception e) {
			logger.error("获取推迟上班次数失败，异常原因："+e);
			throw new BusinessException("获取推迟上班次数失败，请重试");
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
		return null;

	}

	/**
	 * 获取推迟上班打卡时间使用次数
	 * 
	 * @param checkdate
	 * @param staffid
	 * @return 使用次数
	 */
	private Map<String, String> getWeekWorkMinute(String checkdate, int staffid, Connection conn) {
		ResultSet rs = null;
		Statement stmt = null;
		try {

			String sql = "SELECT check_date,off_work_time FROM s_xy_checkinout WHERE is_use=0 and WEEK(check_date) = WEEK('"
					+ checkdate + "') AND date_type='" + Constants.DATE_TYPE_B + "' AND off_work_time> '"
					+ Constants.XY_END_WORK_TIME + "' and  staff_id= " + staffid;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			Map<String, String> map = new HashMap<String, String>();
			while (rs.next()) {
				map.put(rs.getString(1), rs.getString(2));
			}
			return map;
		} catch (Exception e) {
			logger.error("获取推迟上班次数失败，请重试"+e);
			throw new BusinessException("获取推迟上班次数失败，请重试");
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
	 * 获取推迟上班打卡时间使用次数
	 * 
	 * @param checkdate
	 * @param staffid
	 * @return 使用次数
	 */
	private int getChangeCount(String checkdate, int staffid, String checkType, Connection conn) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ResultSet rs = null;
		Statement stmt = null;
		try {
			Date date = sdf.parse(checkdate);
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(date);

			String checkTime;
			if (startCal.get(Calendar.DAY_OF_MONTH) > 20) {
				checkTime = checkdate.substring(0, 8) + "21";
			} else {
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.MONTH, -1);

				String time = sdf.format(cal.getTime());
				checkTime = time.substring(0, 8) + "20";
			}

			String sql = "SELECT count(1) FROM s_xy_checkinout where check_type='" + checkType + "' and check_date>= '"
					+ checkTime + "' and check_date < '" + checkdate + "' and  staff_id= " + staffid;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			logger.error("获取推迟上班次数失败"+e);
			throw new BusinessException("获取推迟上班次数失败，请重试");
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
		return 0;
	}

	private boolean cheakRuleValue(Long startValue, Long endValue, String comp) {
		if ("<=".equals(comp)) {
			return startValue <= endValue;
		}
		if (">=".equals(comp)) {
			return startValue >= endValue;
		}
		if ("<".equals(comp)) {
			return startValue < endValue;
		}
		if (">".equals(comp)) {
			return startValue > endValue;
		}
		if ("==".equals(comp)) {
			return startValue == endValue;
		}

		return false;
	}
	
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse("2016-09-01");
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(date);
		System.out.println(startCal.get(Calendar.DAY_OF_MONTH));
		String s = "changeMinute==60";
		System.out.println(s.substring(s.indexOf("changeMinute")+"changeMinute".length()+2));
	}

}
