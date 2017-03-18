package com.xy.oa.calendars.service;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.web.cgform.exception.BusinessException;

import com.xy.oa.calendars.entity.SXyCalendarsEntity;

public interface SXyCalendarsServiceI extends CommonService{
	
 	public void delete(SXyCalendarsEntity entity) throws Exception;
 	
 	public Serializable save(SXyCalendarsEntity entity) throws Exception;
 	
 	public void saveOrUpdate(SXyCalendarsEntity entity) throws Exception;

	public void deleteAll(Date startdate,Date enddate);
	
	public BigDecimal getTimeInterval(Date startTime,Date endTime,boolean isOnlyWorkDate) throws BusinessException, ParseException;
	
	public float getTwoTimeFar(Date frontdate,Date behinddate);
 	
}
