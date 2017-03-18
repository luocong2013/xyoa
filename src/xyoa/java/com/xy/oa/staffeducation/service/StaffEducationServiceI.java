package com.xy.oa.staffeducation.service;
import com.xy.oa.staffeducation.entity.StaffEducationEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface StaffEducationServiceI extends CommonService{
	
 	public void delete(StaffEducationEntity entity) throws Exception;
 	
 	public Serializable save(StaffEducationEntity entity) throws Exception;
 	
 	public void saveOrUpdate(StaffEducationEntity entity) throws Exception;
 	
}
