package com.xy.oa.notice.service;
import com.xy.oa.notice.entity.TSNoticeDeptEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface TSNoticeDeptServiceI extends CommonService{
	
 	public void delete(TSNoticeDeptEntity entity) throws Exception;
 	
 	public Serializable save(TSNoticeDeptEntity entity) throws Exception;
 	
 	public void saveOrUpdate(TSNoticeDeptEntity entity) throws Exception;
 	
}
