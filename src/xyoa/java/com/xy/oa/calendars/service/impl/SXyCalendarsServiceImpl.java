package com.xy.oa.calendars.service.impl;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;
import org.jeecgframework.web.cgform.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xy.oa.calendars.dao.CalendarsDao;
import com.xy.oa.calendars.dao.MiCalendarsDao;
import com.xy.oa.calendars.entity.SXyCalendarsEntity;
import com.xy.oa.calendars.service.SXyCalendarsServiceI;

@Service("sXyCalendarsService")
@Transactional
public class SXyCalendarsServiceImpl extends CommonServiceImpl implements SXyCalendarsServiceI {

	private CalendarsDao calendarsDao;
	@Autowired
	private MiCalendarsDao miCalendarsDao;
	
 	public CalendarsDao getCalendarsDao() {
		return calendarsDao;
	}
 	@Resource
	public void setCalendarsDao(CalendarsDao calendarsDao) {
		this.calendarsDao = calendarsDao;
	}

	public void delete(SXyCalendarsEntity entity) throws Exception{
 		super.delete(entity);
 		//执行删除操作增强业务
		this.doDelBus(entity);
 	}
 	
 	public Serializable save(SXyCalendarsEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		//执行新增操作增强业务
 		this.doAddBus(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(SXyCalendarsEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 		//执行更新操作增强业务
 		this.doUpdateBus(entity);
 	}
 	
 	/**
	 * 新增操作增强业务
	 * @param t
	 * @return
	 */
	private void doAddBus(SXyCalendarsEntity t) throws Exception{
 	}
 	/**
	 * 更新操作增强业务
	 * @param t
	 * @return
	 */
	private void doUpdateBus(SXyCalendarsEntity t) throws Exception{
 	}
 	/**
	 * 删除操作增强业务
	 * @param id
	 * @return
	 */
	private void doDelBus(SXyCalendarsEntity t) throws Exception{
 	}
 	
 	private Map<String,Object> populationMap(SXyCalendarsEntity t){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", t.getId());
		map.put("calendarday", t.getCalendarday());
		map.put("calendartype", t.getCalendartype());
		map.put("remarks", t.getRemarks());
		map.put("ctime", t.getCtime());
		map.put("utime", t.getUtime());
		map.put("cuser", t.getCuser());
		map.put("uuser", t.getUuser());
		return map;
	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @param t
	 * @return
	 */
 	public String replaceVal(String sql,SXyCalendarsEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{calendarday}",String.valueOf(t.getCalendarday()));
 		sql  = sql.replace("#{calendartype}",String.valueOf(t.getCalendartype()));
 		sql  = sql.replace("#{remarks}",String.valueOf(t.getRemarks()));
 		sql  = sql.replace("#{ctime}",String.valueOf(t.getCtime()));
 		sql  = sql.replace("#{utime}",String.valueOf(t.getUtime()));
 		sql  = sql.replace("#{cuser}",String.valueOf(t.getCuser()));
 		sql  = sql.replace("#{uuser}",String.valueOf(t.getUuser()));
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
	public void deleteAll( Date startdate,Date enddate) {
		calendarsDao.deleteAll(startdate,enddate);
		
	}
	@Override
	public float getTwoTimeFar(Date frontdate, Date behinddate) {
	return calendarsDao.getTwoTimeFar(frontdate, behinddate);
	}
	
	@Override
	public BigDecimal getTimeInterval(Date startTime,Date endTime,boolean isOnlyWorkDate)throws BusinessException, ParseException{
		if(startTime.getTime() >=endTime.getTime() )
			return new BigDecimal(0.00);
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		List<SXyCalendarsEntity> calendarsList = miCalendarsDao.queryCalendersForDate(df.format(startTime),df.format(endTime));
		if(calendarsList == null || calendarsList.size() ==0)
			return new BigDecimal(0.00);
		
		Calendar start = Calendar.getInstance();  
		Calendar end = Calendar.getInstance();  
		start.setTime(startTime);
		end.setTime(endTime);
		int startDay = start.get(Calendar.DAY_OF_MONTH);
		int endDay = end.get(Calendar.DAY_OF_MONTH);
		int startHour = start.get(Calendar.HOUR_OF_DAY);
		int endHour = end.get(Calendar.HOUR_OF_DAY);
		int startMinutes = start.get(Calendar.MINUTE) >=30 ? 30 :0; //开始 时间以30分钟间隔，小于30的按照0算，大于等于30的按照30算
		int endMinutes = end.get(Calendar.MINUTE);
		
		int startMonth = start.get(Calendar.MONTH);
		int endMonth = end.get(Calendar.MONTH);
		
		if(endMinutes >30){ //结束 时间以30分钟间隔，大于30的按照小时+1，分钟数为0算，小于等于30的按照30算
			endMinutes = 0;
			endHour = endHour+1;
		}

		if(endMinutes >0 && endMinutes <30){
			endMinutes = 30;
		}
		

		BigDecimal returnHour = new BigDecimal(0.0);
		
		for(SXyCalendarsEntity entity : calendarsList){
			if(isOnlyWorkDate){
				start.setTime(entity.getCalendarday());
				if("B".equals(entity.getCalendartype())){
					if(startDay == start.get(Calendar.DAY_OF_MONTH)){
						if(startMonth == start.get(Calendar.MONTH)){
							if(startDay == endDay && endMonth== startMonth)
								returnHour = returnHour.add(getHour(startHour,endHour,startMinutes,endMinutes));
							else
								returnHour = returnHour.add(getHour(startHour,18,startMinutes,0));
						}else
							returnHour = returnHour.add(new BigDecimal(7.5));
						continue;
					}else if(endDay == start.get(Calendar.DAY_OF_MONTH)){
						if(endMonth == start.get(Calendar.MONTH))
							returnHour = returnHour.add(getHour(9,endHour,0,endMinutes));
						else
							returnHour = returnHour.add(new BigDecimal(7.5));
						continue;
					}else{
						returnHour = returnHour.add(new BigDecimal(7.5));
						continue;
					}
				}
			}else{
				if(startDay == start.get(Calendar.DAY_OF_MONTH)){
					if(startMonth == start.get(Calendar.MONTH)){
						if(startDay == endDay && endMonth== startMonth)
							returnHour = returnHour.add(getHour(startHour,endHour,startMinutes,endMinutes));
						else
							returnHour = returnHour.add(getHour(startHour,18,startMinutes,0));
					}else
						returnHour = returnHour.add(new BigDecimal(7.5));
					continue;
				}else if(endDay == start.get(Calendar.DAY_OF_MONTH)){
					if(endMonth == start.get(Calendar.MONTH))
						returnHour = returnHour.add(getHour(9,endHour,0,endMinutes));
					else
						returnHour = returnHour.add(new BigDecimal(7.5));
					continue;
				}else{
					returnHour = returnHour.add(new BigDecimal(7.5));
					continue;
				}
			}
			if(startMonth == endMonth && startDay == endDay){
				break;
			}
		}
		return returnHour;
	}
	
	private static BigDecimal getHour(int startHour,int endHour,int startMinutes,int endMinutes) throws BusinessException{
		BigDecimal returnHour = new BigDecimal(0);
		if(startHour > endHour ||(startHour == endHour && startMinutes > endMinutes)){
			return new BigDecimal(0);
		}else{
			if(startHour<=9){
				if(endHour >=18){
					return returnHour.add(new BigDecimal(7.5));
				}else if(endHour >=14 ||(endHour ==13 && endMinutes>30)){
					returnHour = returnHour.add(new BigDecimal(3));
					return returnHour.add(getHourInterval(13,endHour,30,endMinutes));
				}else if(endHour >=12){
					return returnHour.add(new BigDecimal(3));
				}else{
					return returnHour.add(getHourInterval(startHour,endHour,startMinutes,endMinutes));
				}
			}else if(startHour>9 && startHour<=12){
				if(endHour >=18){
					returnHour = returnHour.add(new BigDecimal(4.5));
					return returnHour.add(getHourInterval(startHour,12,startMinutes,0));
				}else if(endHour >=14 ||(endHour ==13 && endMinutes>30) ){
					returnHour = returnHour.add(getHourInterval(startHour,endHour,startMinutes,endMinutes));
					return returnHour.subtract(new BigDecimal(1.5));
				}else if(endHour >=12){
					return returnHour.add(getHourInterval(startHour,12,startMinutes,0));
				}else{
					return returnHour.add(getHourInterval(startHour,endHour,startMinutes,endMinutes));
				}
			}else if(startHour>12 && startHour<=13 && startMinutes<=30){
				if(endHour >=18){
					return returnHour.add(new BigDecimal(4.5));
				}else{
					return returnHour.add(getHourInterval(13,endHour,30,endMinutes));
				}
			}else{
				if(endHour >=18){
					return returnHour.add(getHourInterval(startHour,18,startMinutes,0));
				}else{
					return returnHour.add(getHourInterval(startHour,endHour,startMinutes,endMinutes));
				}
			}
		}
	}
	
	private static BigDecimal getHourInterval(int startHour,int endHour,int startMinutes,int endMinutes)throws BusinessException{
		long minutes =0;
		double hours = 0;
		try
		{
			DateFormat df = new SimpleDateFormat("HH:mm:ss");
			Date start = df.parse(startHour+":"+startMinutes+":00");
			Date end = df.parse(endHour+":"+endMinutes+":00");
			//获取分钟数差
			minutes = (end.getTime() - start.getTime())/(1000 * 60 * 60);
			hours = (end.getTime() - start.getTime())/(1000 * 60 * 60.0);
		}catch(Exception e){
			throw new BusinessException("时间间隔计算错误。请确保日期格式是否正确");
		}
		
		if(hours -minutes>=0.5 ){
			return new BigDecimal(minutes+0.5);
		}
		return new BigDecimal(minutes);
	}
}