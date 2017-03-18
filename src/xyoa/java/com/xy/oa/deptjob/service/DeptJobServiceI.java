package com.xy.oa.deptjob.service;
import com.xy.oa.deptjob.entity.DeptJobEntity;
import com.xy.oa.staff.entity.DllDepart;

import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.web.system.pojo.base.TSDepart;

import java.io.Serializable;
import java.util.List;

public interface DeptJobServiceI extends CommonService{
	
 	public void delete(DeptJobEntity entity) throws Exception;
 	
 	public Serializable save(DeptJobEntity entity) throws Exception;
 	
 	public void saveOrUpdate(DeptJobEntity entity) throws Exception;

	public List<TSDepart> getALLDepart();

	public List<DeptJobEntity> getDeptJobs(String departid);

	public List<DeptJobEntity> getAllDeptJob();
 	
}
