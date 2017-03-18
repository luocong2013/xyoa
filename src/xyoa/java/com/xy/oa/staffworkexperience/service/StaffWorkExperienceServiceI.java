package com.xy.oa.staffworkexperience.service;
import com.xy.oa.staffworkexperience.entity.StaffWorkExperienceEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface StaffWorkExperienceServiceI extends CommonService{
	
 	public void delete(StaffWorkExperienceEntity entity) throws Exception;
 	
 	public Serializable save(StaffWorkExperienceEntity entity) throws Exception;
 	
 	public void saveOrUpdate(StaffWorkExperienceEntity entity) throws Exception;
 	
}
