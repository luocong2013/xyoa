package com.xy.oa.checkapply.service;


import java.io.Serializable;

import org.jeecgframework.core.common.service.CommonService;

import com.xy.oa.checkapply.entity.SXyCheckApplyEntity;

public interface SXyCheckApplyServiceI extends CommonService{
	
 	public void delete(SXyCheckApplyEntity entity) throws Exception;
 	
 	public Serializable save(SXyCheckApplyEntity entity) throws Exception;
 	
 	public void saveOrUpdate(SXyCheckApplyEntity entity) throws Exception;
 	
}
