package com.xy.oa.checkinout.service.imp;


import com.xy.oa.checkinout.service.SXyCheckinoutServiceI;
import com.xy.oa.util.Constants;

import org.hibernate.Query;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

import com.xy.oa.checkinout.dao.CheckinoutDao;
import com.xy.oa.checkinout.entity.SXyCheckinoutEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import java.io.Serializable;
import java.math.BigDecimal;

import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;
import org.jeecgframework.web.system.pojo.base.TSBaseUser;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;

@Service("sXyCheckinoutService")
@Transactional
public class SXyCheckinoutServiceImpl extends CommonServiceImpl implements SXyCheckinoutServiceI {
	private CheckinoutDao checkinoutDao;

	
 	public CheckinoutDao getCheckinoutDao() {
		return checkinoutDao;
	}
 	@Resource
	public void setCheckinoutDao(CheckinoutDao checkinoutDao) {
		this.checkinoutDao = checkinoutDao;
	}

	public void delete(SXyCheckinoutEntity entity) throws Exception{
 		super.delete(entity);
 		//执行删除操作增强业务
		this.doDelBus(entity);
 	}
 	
 	public Serializable save(SXyCheckinoutEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		//执行新增操作增强业务
 		this.doAddBus(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(SXyCheckinoutEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 		//执行更新操作增强业务
 		this.doUpdateBus(entity);
 	}
 	
 	/**
	 * 新增操作增强业务
	 * @param t
	 * @return
	 */
	private void doAddBus(SXyCheckinoutEntity t) throws Exception{
 	}
 	/**
	 * 更新操作增强业务
	 * @param t
	 * @return
	 */
	private void doUpdateBus(SXyCheckinoutEntity t) throws Exception{
 	}
 	/**
	 * 删除操作增强业务
	 * @param id
	 * @return
	 */
	private void doDelBus(SXyCheckinoutEntity t) throws Exception{
 	}
 	
 	private Map<String,Object> populationMap(SXyCheckinoutEntity t){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", t.getId());
		map.put("name", t.getName());
		map.put("staff_id", t.getStaffId());
		map.put("dept_id", t.getDeptId());
		map.put("check_date", t.getCheckDate());
		map.put("work_time", t.getWorkTime());
		map.put("off_work_time", t.getOffWorkTime());
		map.put("exception_minute", t.getExceptionMinute());
		map.put("exception_remarks", t.getExceptionRemarks());
		map.put("is_check_true", t.getIsCheckTrue());
		map.put("check_type", t.getCheckType());
		map.put("check_remarks", t.getCheckRemarks());
		map.put("apply_id", t.getApplyId());
		map.put("date_type", t.getDateType());
		map.put("work_hour", t.getWorkHour());
		map.put("c_time", t.getcTime());
		map.put("c_user", t.getcUser());
		map.put("u_time", t.getuTime());
		map.put("u_user", t.getuUser());
		return map;
	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @param t
	 * @return
	 */
 	public String replaceVal(String sql,SXyCheckinoutEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{name}",String.valueOf(t.getName()));
 		sql  = sql.replace("#{staff_id}",String.valueOf(t.getStaffId()));
 		sql  = sql.replace("#{dept_id}",String.valueOf(t.getDeptId()));
 		sql  = sql.replace("#{check_date}",String.valueOf(t.getCheckDate()));
 		sql  = sql.replace("#{work_time}",String.valueOf(t.getWorkTime()));
 		sql  = sql.replace("#{off_work_time}",String.valueOf(t.getOffWorkTime()));
 		sql  = sql.replace("#{exception_minute}",String.valueOf(t.getExceptionMinute()));
 		sql  = sql.replace("#{exception_remarks}",String.valueOf(t.getExceptionRemarks()));
 		sql  = sql.replace("#{is_check_true}",String.valueOf(t.getIsCheckTrue()));
 		sql  = sql.replace("#{check_type}",String.valueOf(t.getCheckType()));
 		sql  = sql.replace("#{check_remarks}",String.valueOf(t.getCheckRemarks()));
 		sql  = sql.replace("#{apply_id}",String.valueOf(t.getApplyId()));
 		sql  = sql.replace("#{date_type}",String.valueOf(t.getDateType()));
 		sql  = sql.replace("#{work_hour}",String.valueOf(t.getWorkHour()));
 		sql  = sql.replace("#{c_time}",String.valueOf(t.getcTime()));
 		sql  = sql.replace("#{c_user}",String.valueOf(t.getcUser()));
 		sql  = sql.replace("#{u_time}",String.valueOf(t.getuTime()));
 		sql  = sql.replace("#{u_user}",String.valueOf(t.getuUser()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}
 	
 	/**
	 * 执行JAVA增强
	 */
 	private void executeJavaExtend(String cgJavaType,String cgJavaValue,Map<String,Object> data) throws Exception {
 		if(StringUtil.isNotEmpty(cgJavaValue)){
			Object obj = null;
			try {
				if("class".equals(cgJavaType)){
					//因新增时已经校验了实例化是否可以成功，所以这块就不需要再做一次判断
					obj = MyClassLoader.getClassByScn(cgJavaValue).newInstance();
				}else if("spring".equals(cgJavaType)){
					obj = ApplicationContextUtil.getContext().getBean(cgJavaValue);
				}
				if(obj instanceof CgformEnhanceJavaInter){
					CgformEnhanceJavaInter javaInter = (CgformEnhanceJavaInter) obj;
					javaInter.execute(data);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("执行JAVA增强出现异常！");
			} 
		}
 	}

	@Override
	public List<TSDepart> getDeptName() {
		return checkinoutDao.getDeptName();
	}
	@Override
	public List<TSRoleUser> geTsRoleUsers(String id) {
		return checkinoutDao.geTsRoleUsers(id);
	}
	@Override
	public List<TSUserOrg> getDeptId(String id) {
		return checkinoutDao.getdeptid(id);
	}
	@Override
	public List<TSBaseUser> getStaffId(String id) {
		return checkinoutDao.getStaffId(id);
	}

	/**  
	* @Name: getImportData
	* @Description: 通过Date startDate, Date endDate, String name, String companyId,
			String deptId这些条件，得到要导出的考勤
	* @author xiaoyong
	* @Parameters: Date startDate, Date endDate, String name, String companyId,
			String deptId
	* @Return: List<SXyCheckinoutEntity>
	*/
	@Override
	public List<SXyCheckinoutEntity> getImportData(Date startDate, Date endDate, String name,  String deptId,String companyId) {
		// TODO Auto-generated method stub
		return checkinoutDao.getImportData( startDate, endDate,name, deptId,companyId);
	}

	@Override
	public List<SXyCheckinoutEntity> disCheckInOut(Integer staffId,String startDate, String endDate, 
			String applyId, String checkType,String checkRemarks) throws Exception { 
		//查询员工考勤记录
		String hql = "from SXyCheckinoutEntity where staffId ="+staffId+" and checkDate >='"+startDate+"' and checkDate <='"+endDate+"' order by checkDate";
        Query query = this.getSession().createQuery(hql);  
        List<SXyCheckinoutEntity> sXyCheckinoutEntityList = query.list();  
		//更新考勤记录
        if(sXyCheckinoutEntityList == null || sXyCheckinoutEntityList.size()==0)
        	return null;
		for(SXyCheckinoutEntity sXyCheckinoutEntity : sXyCheckinoutEntityList){
			sXyCheckinoutEntity.setCheckType(checkType);
			sXyCheckinoutEntity.setApplyId(applyId);
			sXyCheckinoutEntity.setCheckRemarks(checkRemarks);
			sXyCheckinoutEntity.setIsCheckTrue(Constants.CHECKINOUT_IS_TRUE);
			sXyCheckinoutEntity.setEarlierMinute(0);
			sXyCheckinoutEntity.setLateMinute(0);
			saveOrUpdate(sXyCheckinoutEntity);
		}
		return sXyCheckinoutEntityList;
	}
	
	public List<Map<String,Object>> impCheckSumData(String startDate,String endDate,String departName){
		String sql = "select s.staff_id,ss.name,d.departname, ss.off_work_count,"
				+ "sum(case  when (s.check_Type ='"+Constants.XY_CHECK_TYPE_00+"' or s.check_Type ='"+Constants.XY_CHECK_TYPE_09+"' or s.check_Type ='"+Constants.XY_CHECK_TYPE_10+"' "
				+ "or s.check_Type ='"+Constants.XY_CHECK_TYPE_11+"' OR s.check_Type ='"+Constants.XY_CHECK_TYPE_12+"' OR s.check_Type ='"+Constants.XY_CHECK_TYPE_13+"' "
				+ "OR s.check_Type ='"+Constants.XY_CHECK_TYPE_15+"' OR s.check_Type ='"+Constants.XY_CHECK_TYPE_16+"')  then 1 else 0 end) as workCount, "
				+ "sum(case  when s.late_minute >0 then 1 else 0 end) as lateCount, "
				+ "sum(case  when s.late_minute >0 then s.late_minute else 0 end) as lateMinute, "
				+ "SUM(CASE  WHEN s.earlier_minute >0 THEN 1 ELSE 0 END) AS earlierCount, "
				+ "SUM(CASE  WHEN s.earlier_minute >0 THEN s.earlier_minute ELSE 0 END) AS earlierMinute, "
				+ "SUM(CASE  WHEN s.check_Type ='"+Constants.XY_CHECK_TYPE_20+"' THEN 1 ELSE 0 END) AS overTimeCount, "
				+ "SUM(CASE  WHEN s.check_Type ='"+Constants.XY_CHECK_TYPE_20+"' THEN s.work_hour ELSE 0 END) AS overTimeHour ,"
				+ "SUM(CASE  WHEN s.check_Type ='"+Constants.XY_CHECK_TYPE_14+"' THEN 1 ELSE 0 END) AS noWorkCount "
				+ "from s_xy_checkinout as s join t_s_depart d on s.dept_id = d.id "
				+ "join s_xy_staff ss on s.staff_id = ss.sttaff_id "
				+ " where s.check_date >='"+startDate+"' and "
				+"s.check_date <='"+endDate+"' ";
		
		if(StringUtil.isNotEmpty(departName))
			sql += " and s.dept_id in("+departName+")";
		sql += "group by s.staff_id,ss.name,d.departname ";
		List<Map<String,Object>> list = this.commonDao.findForJdbc(sql);
		if(list ==null || list.size()==0)
			return null;
		
		List<Map<String,Object>> newList = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map :list){
			Map<String,Object> newMap = new HashMap<String,Object>();
			newMap.put("checkSum_0", map.get("staff_id"));//员工ID
			newMap.put("checkSum_1", map.get("name"));//员工姓名
			newMap.put("checkSum_2", map.get("departname"));//部门ID


			int workCount = ((BigDecimal)map.get("workCount")).intValue();
			int lateCount = ((BigDecimal)map.get("lateCount")).intValue();
			int earlierCount = ((BigDecimal)map.get("earlierCount")).intValue();
			int lateMinute = ((BigDecimal)map.get("lateMinute")).intValue();
			int earlierMinute = ((BigDecimal)map.get("earlierMinute")).intValue();
			//计算工作时数
			Long monthWorkDay = this.commonDao.getCountForJdbc("select count(1) from s_xy_calendars where calendarday>='"+startDate+"' "
					+ "and calendarday<='"+endDate+"' and calendartype ='"+Constants.DATE_TYPE_B+"'");
			newMap.put("checkSum_3", monthWorkDay*7.5);//工作时数(标准)
			newMap.put("checkSum_4", new BigDecimal((workCount*7.5*60-lateMinute-earlierMinute)/60).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());//工作时数（实际）
			
			newMap.put("checkSum_5", lateCount);//迟到次数
			newMap.put("checkSum_6", lateMinute);//迟到分钟数
			
			newMap.put("checkSum_7", earlierCount);//早退次数
			newMap.put("checkSum_8", earlierMinute);//早退分钟数
			newMap.put("checkSum_9", monthWorkDay+"/"+workCount); //出勤天数(标准/实际)

			
	
			
			
			//出差(天)
			Map<String,Object> tripMap = this.commonDao.findOneForJdbc("select sum(trip_hour) as tripHour from s_xy_business_trip where "
					+ "apply_sttaff_id ="+map.get("staff_id")+" and start_time>='"+startDate+"' "
					+ "and end_time<='"+endDate+"'");
			newMap.put("checkSum_10", tripMap.get("tripMap")); 
			
			//外出（天）
			Map<String,Object> outMap = this.commonDao.findOneForJdbc("select sum(out_hour) as outHour from s_xy_out_work where "
					+ "apply_sttaff_id ="+map.get("staff_id")+" and start_time>='"+startDate+"' "
					+ "and end_time<='"+endDate+"'");
			newMap.put("checkSum_11", outMap.get("outHour")); 
		
			//旷工(天)
			newMap.put("checkSum_12", ((BigDecimal)map.get("noWorkCount")).intValue()*Constants.XY_WORK_HOUR); 
			
			
			//事假(天) ，病假（天），年假（天），婚假（天），产假（天），丧假（天）
			Map<String,Object> absenceMap = this.commonDao.findOneForJdbc("select SUM(CASE  WHEN absence_type ='"+Constants.XY_CHECK_TYPE_01+"' THEN absence_day ELSE 0 END) AS absenceHour_01,"
					+ "SUM(CASE  WHEN absence_type ='"+Constants.XY_CHECK_TYPE_02+"' THEN absence_day ELSE 0 END) AS absenceHour_02,"
					+ "SUM(CASE  WHEN absence_type ='"+Constants.XY_CHECK_TYPE_08+"' THEN absence_day ELSE 0 END) AS absenceHour_08,"
					+ "SUM(CASE  WHEN absence_type ='"+Constants.XY_CHECK_TYPE_04+"' THEN absence_day ELSE 0 END) AS absenceHour_04,"
					+ "SUM(CASE  WHEN absence_type ='"+Constants.XY_CHECK_TYPE_06+"' THEN absence_day ELSE 0 END) AS absenceHour_06,"
					+ "SUM(CASE  WHEN absence_type ='"+Constants.XY_CHECK_TYPE_03+"' THEN absence_day ELSE 0 END) AS absenceHour_03 from s_xy_absence "
					+ "where apply_sttaff_id ="+map.get("staff_id")+" and start_time>='"+startDate+"' "
					+ "and end_time<='"+endDate+"'");
			newMap.put("checkSum_13", absenceMap.get("absenceHour_01")); 
			newMap.put("checkSum_14", absenceMap.get("absenceHour_02")); 
			newMap.put("checkSum_15", absenceMap.get("absenceHour_08"));
			newMap.put("checkSum_16", absenceMap.get("absenceHour_04")); 
			newMap.put("checkSum_17", absenceMap.get("absenceHour_06")); 
			newMap.put("checkSum_18", absenceMap.get("absenceHour_03"));
			
			//获取加班数
			newMap.put("checkSum_19", map.get("overTimeHour"));//todo
			
			//调休（本月调休）
			Map<String,Object> leaveMap = this.commonDao.findOneForJdbc("select sum(leave_hour) as leaveHour from s_xy_compensate_leave where "
					+ "apply_sttaff_id ="+map.get("staff_id")+" and start_time>='"+startDate+"' "
					+ "and end_time<='"+endDate+"'");
			newMap.put("checkSum_20", map.get("off_work_count")); //可调休数
			newMap.put("checkSum_21", leaveMap.get("leaveHour")); //调休（本月调休）
			
			newList.add(newMap);
			
		}
		
		return newList;

	}

}