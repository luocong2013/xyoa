package com.xy.oa.sheduletime.service;
import com.xy.oa.sheduletime.entity.XySheduleTimeEntity;
import com.xy.oa.sheduletime.entity.XyWorkScheduleEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;
import java.util.List;

public interface XyWorkScheduleServiceI extends CommonService{
	
 	public void delete(XyWorkScheduleEntity entity) throws Exception;
 	
 	public Serializable save(XyWorkScheduleEntity entity) throws Exception;
 	
 	public void saveOrUpdate(XyWorkScheduleEntity entity) throws Exception;

	
}
