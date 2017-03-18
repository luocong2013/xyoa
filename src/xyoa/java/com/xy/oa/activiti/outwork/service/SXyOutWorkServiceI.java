package com.xy.oa.activiti.outwork.service;


import java.io.Serializable;

import org.jeecgframework.core.common.service.CommonService;

import com.xy.oa.activiti.outwork.entity.SXyOutWorkEntity;

public interface SXyOutWorkServiceI extends CommonService{
	
 	public void delete(SXyOutWorkEntity entity) throws Exception;
 	
 	public Serializable save(SXyOutWorkEntity entity) throws Exception;
 	
 	public void saveOrUpdate(SXyOutWorkEntity entity) throws Exception;
 	
}
