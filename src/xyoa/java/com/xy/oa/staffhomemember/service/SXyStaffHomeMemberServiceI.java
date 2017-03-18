package com.xy.oa.staffhomemember.service;
import com.xy.oa.staffhomemember.entity.SXyStaffHomeMemberEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface SXyStaffHomeMemberServiceI extends CommonService{
	
 	public void delete(SXyStaffHomeMemberEntity entity) throws Exception;
 	
 	public Serializable save(SXyStaffHomeMemberEntity entity) throws Exception;
 	
 	public void saveOrUpdate(SXyStaffHomeMemberEntity entity) throws Exception;
 	
}
