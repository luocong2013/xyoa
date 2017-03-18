package com.xy.oa.test.service;
import java.io.Serializable;

import org.jeecgframework.core.common.service.CommonService;

import com.xy.oa.test.entity.SXyTestEntity;

public interface SXyTestServiceI extends CommonService{
	
 	public void delete(SXyTestEntity entity) throws Exception;
 	
 	public Serializable save(SXyTestEntity entity) throws Exception;
 	
 	public void saveOrUpdate(SXyTestEntity entity) throws Exception;
 	
}
