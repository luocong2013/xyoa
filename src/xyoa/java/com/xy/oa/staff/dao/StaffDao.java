package com.xy.oa.staff.dao;

import java.util.List;

import com.xy.oa.staff.entity.StaffEntity;

public interface StaffDao {
	public StaffEntity getStaffByUsername(int staffid );
	public int getMaxStaffidByCompanyId(String companyid);
	public StaffEntity getStaffByCheckId(int checkid);
	public List<StaffEntity> getTrialEndStaff();
	public List<StaffEntity> getTrialEndStaff(List  list);
	public List<StaffEntity> getStaffByDept(String deptId);

}
