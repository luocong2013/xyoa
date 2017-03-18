package com.xy.oa.checkinout.service;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.web.system.pojo.base.TSBaseUser;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;

import com.xy.oa.checkinout.entity.SXyCheckinoutEntity;

public interface SXyCheckinoutServiceI extends CommonService{
	
 	public void delete(SXyCheckinoutEntity entity) throws Exception;
 	
 	public Serializable save(SXyCheckinoutEntity entity) throws Exception;
 	
 	public void saveOrUpdate(SXyCheckinoutEntity entity) throws Exception;
 	
 	public List<TSDepart> getDeptName();
 	
 	public List<TSRoleUser> geTsRoleUsers(String id);
 	public List<TSUserOrg> getDeptId(String id);
 	public List<TSBaseUser> getStaffId(String id);


	public List<SXyCheckinoutEntity> getImportData(Date startDate, Date endDate, String name,  String deptId,String companyId);

 	
 	public List<SXyCheckinoutEntity> disCheckInOut(Integer staffId,String startDate,String endDate,String applyId,String checkType,String checkRemarks) throws Exception;
 	
 	public List<Map<String,Object>> impCheckSumData(String startDate,String endDate,String departName);

}
