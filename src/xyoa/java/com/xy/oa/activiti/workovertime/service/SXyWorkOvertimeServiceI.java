package com.xy.oa.activiti.workovertime.service;

import java.io.Serializable;

import org.jeecgframework.core.common.service.CommonService;

import com.xy.oa.activiti.workovertime.entity.SXyWorkOvertimeEntity;

/**
 * 享宇加班申请表服务层接口
 * @author Luo
 *
 */
public interface SXyWorkOvertimeServiceI extends CommonService{
	
 	public void delete(SXyWorkOvertimeEntity entity) throws Exception;
 	
 	public Serializable save(SXyWorkOvertimeEntity entity) throws Exception;
 	
 	public void saveOrUpdate(SXyWorkOvertimeEntity entity) throws Exception;
 	
}
