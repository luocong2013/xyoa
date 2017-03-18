package com.xy.oa.stafftrain.service;
import com.xy.oa.stafftrain.entity.SXyStaffTrainEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface SXyStaffTrainServiceI extends CommonService{
	
 	public void delete(SXyStaffTrainEntity entity) throws Exception;
 	
 	public Serializable save(SXyStaffTrainEntity entity) throws Exception;
 	
 	public void saveOrUpdate(SXyStaffTrainEntity entity) throws Exception;
 	
}
