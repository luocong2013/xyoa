package com.xy.oa.checkinout.dao;

import java.util.Date;
import java.util.List;

import org.jeecgframework.web.system.pojo.base.TSBaseUser;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSRole;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;

import com.xy.oa.checkinout.entity.SXyCheckinoutEntity;
import com.xy.oa.staff.entity.StaffEntity;


public interface CheckinoutDao {
	List<TSDepart> getDeptName();
	List<StaffEntity> getstaffname();
	List<TSUserOrg> getdeptid(String  id);
	List<TSBaseUser> getStaffId(String id);
	
	List<TSRoleUser> geTsRoleUsers(String id);
	List<SXyCheckinoutEntity> getImportData(Date startDate, Date endDate, String name,   String deptId,String companyId);
}
