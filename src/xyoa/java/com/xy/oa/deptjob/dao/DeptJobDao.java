package com.xy.oa.deptjob.dao;

import java.util.List;

import com.xy.oa.deptjob.entity.DeptJobEntity;

public interface DeptJobDao {
	public List<DeptJobEntity> getDeptJobByDepartId(String deptid);

	public List<DeptJobEntity> getAllDeptJob();
}
