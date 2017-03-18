package com.xy.oa.staff.service;
import com.xy.oa.deptjob.entity.DeptJobEntity;
import com.xy.oa.staff.entity.DllDepart;
import com.xy.oa.staff.entity.StaffEntity;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;

import java.io.Serializable;
import java.util.List;

public interface StaffServiceI extends CommonService{
	
 	public void delete(StaffEntity entity) throws Exception;
 	
 	public Serializable save(StaffEntity entity) throws Exception;
 	
 	public void saveOrUpdate(StaffEntity entity) throws Exception;

	@SuppressWarnings("rawtypes")
	public List getAllCompany();

	public  List<DllDepart> getAllDepartByCompany(TSDepart company);

//	public List<TSDepart> getAll();

	public List<DeptJobEntity> getDeptJobsByDepartId(String deptId);
	public TSUser getUserByUsername(String username );
	
	public StaffEntity getStaffByUsername(int staffid );

	public int getMaxStaffidByCompanyId(String companyid);
	
	public TSDepart getDepartByUpdeparts(TSDepart company ,String dept);
	
	public TSDepart getDepartByName(String name);
	
	public boolean addOffWorkCount(int  staffid, double addtime);
	
	public double getOffWorkCount(int  staffid);
	
	public boolean reduceOffWorkCount(int  staffid, double reducetime);

	public StaffEntity getStaffByCheckId(int checkid);

	public List<StaffEntity> getTrialEndStaff();

	public List<StaffEntity> getTrialEndStaff(String deptId);

	public List<StaffEntity> getStaffByDept(String deptId);
	
}
