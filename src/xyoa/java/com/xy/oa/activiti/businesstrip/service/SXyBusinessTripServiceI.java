package com.xy.oa.activiti.businesstrip.service;


import java.io.Serializable;

import org.jeecgframework.core.common.service.CommonService;

import com.xy.oa.activiti.businesstrip.entity.SXyBusinessTripEntity;

/**
 * 享宇出差申请表服务层接口
 * @author Luo
 *
 */
public interface SXyBusinessTripServiceI extends CommonService{
	
 	public void delete(SXyBusinessTripEntity entity) throws Exception;
 	
 	public Serializable save(SXyBusinessTripEntity entity) throws Exception;
 	
 	public void saveOrUpdate(SXyBusinessTripEntity entity) throws Exception;
 	
}
