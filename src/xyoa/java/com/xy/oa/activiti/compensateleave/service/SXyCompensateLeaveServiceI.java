package com.xy.oa.activiti.compensateleave.service;


import java.io.Serializable;

import org.jeecgframework.core.common.service.CommonService;

import com.xy.oa.activiti.compensateleave.entity.SXyCompensateLeaveEntity;

/**
 * 享宇调休申请 服务层接口
 * @author Luo
 *
 */
public interface SXyCompensateLeaveServiceI extends CommonService{
	
 	public void delete(SXyCompensateLeaveEntity entity) throws Exception;
 	
 	public Serializable save(SXyCompensateLeaveEntity entity) throws Exception;
 	
 	public void saveOrUpdate(SXyCompensateLeaveEntity entity) throws Exception;
 	
}
