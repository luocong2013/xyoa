package com.xy.oa.activiti.absence.service;

import java.io.Serializable;

import org.jeecgframework.core.common.service.CommonService;

import com.xy.oa.activiti.absence.entity.SXyAbsenceEntity;
/**
 * 享宇请假表业务层接口定义
 * @author Luo
 *
 */
public interface SXyAbsenceServiceI extends CommonService{
	
 	public void delete(SXyAbsenceEntity entity) throws Exception;
 	
 	public Serializable save(SXyAbsenceEntity entity) throws Exception;
 	
 	public void saveOrUpdate(SXyAbsenceEntity entity) throws Exception;
 	
}
