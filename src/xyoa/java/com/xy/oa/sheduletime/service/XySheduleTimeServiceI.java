package com.xy.oa.sheduletime.service;
import com.xy.oa.sheduletime.entity.XySheduleTimeEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface XySheduleTimeServiceI extends CommonService{
	
 	public void delete(XySheduleTimeEntity entity) throws Exception;
 	
 	public Serializable save(XySheduleTimeEntity entity) throws Exception;
 	
 	public void saveOrUpdate(XySheduleTimeEntity entity) throws Exception;
 	
}
