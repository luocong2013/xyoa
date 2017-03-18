package com.xy.oa.checkinout.pcheck.service;
import org.jeecgframework.core.common.service.CommonService;

import com.xy.oa.checkinout.pcheck.entity.PCheckinoutEntity;

import java.io.Serializable;

public interface PCheckinoutServiceI extends CommonService{
	
 	public void delete(PCheckinoutEntity entity) throws Exception;
 	
 	public Serializable save(PCheckinoutEntity entity) throws Exception;
 	
 	public void saveOrUpdate(PCheckinoutEntity entity) throws Exception;
 	
}
