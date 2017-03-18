package com.xy.oa.checkapply.service.impl;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xy.oa.checkapply.entity.SXyCheckApplyEntity;
import com.xy.oa.checkapply.service.SXyCheckApplyServiceI;

@Service("sXyCheckApplyService")
@Transactional
public class SXyCheckApplyServiceImpl extends CommonServiceImpl implements SXyCheckApplyServiceI {

	
 	public void delete(SXyCheckApplyEntity entity) throws Exception{
 		super.delete(entity);
 		//执行删除操作增强业务
		this.doDelBus(entity);
 	}
 	
 	public Serializable save(SXyCheckApplyEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		//执行新增操作增强业务
 		this.doAddBus(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(SXyCheckApplyEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 		//执行更新操作增强业务
 		this.doUpdateBus(entity);
 	}
 	
 	/**
	 * 新增操作增强业务
	 * @param t
	 * @return
	 */
	private void doAddBus(SXyCheckApplyEntity t) throws Exception{
 	}
 	/**
	 * 更新操作增强业务
	 * @param t
	 * @return
	 */
	private void doUpdateBus(SXyCheckApplyEntity t) throws Exception{
 	}
 	/**
	 * 删除操作增强业务
	 * @param id
	 * @return
	 */
	private void doDelBus(SXyCheckApplyEntity t) throws Exception{
 	}
 	
 	private Map<String,Object> populationMap(SXyCheckApplyEntity t){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", t.getId());
		map.put("check_ids", t.getCheckIds());
		map.put("apply_sttaff_id", t.getApplySttaffId());
		map.put("check_type", t.getCheckType());
		map.put("apply_date", t.getApplyDate());
		map.put("remarks", t.getRemarks());
		map.put("c_time", t.getCreateTime());
		map.put("c_user", t.getCUser());
		map.put("u_time", t.getUTime());
		map.put("u_user", t.getUUser());
		map.put("flow_state", t.getFlowState());
		map.put("flow_inst_id", t.getFlowInstId());
		map.put("apply_no", t.getApplyNo());
		return map;
	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @param t
	 * @return
	 */
 	public String replaceVal(String sql,SXyCheckApplyEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{check_ids}",String.valueOf(t.getCheckIds()));
 		sql  = sql.replace("#{apply_sttaff_id}",String.valueOf(t.getApplySttaffId()));
 		sql  = sql.replace("#{check_type}",String.valueOf(t.getCheckType()));
 		sql  = sql.replace("#{apply_date}",String.valueOf(t.getApplyDate()));
 		sql  = sql.replace("#{remarks}",String.valueOf(t.getRemarks()));
 		sql  = sql.replace("#{c_time}",String.valueOf(t.getCreateTime()));
 		sql  = sql.replace("#{c_user}",String.valueOf(t.getCUser()));
 		sql  = sql.replace("#{u_time}",String.valueOf(t.getUTime()));
 		sql  = sql.replace("#{u_user}",String.valueOf(t.getUUser()));
 		sql  = sql.replace("#{flow_state}",String.valueOf(t.getFlowState()));
 		sql  = sql.replace("#{flow_inst_id}",String.valueOf(t.getFlowInstId()));
 		sql  = sql.replace("#{apply_no}",String.valueOf(t.getApplyNo()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}
 	
 	/**
	 * 执行JAVA增强
	 */
 	private void executeJavaExtend(String cgJavaType,String cgJavaValue,Map<String,Object> data) throws Exception {
 		if(StringUtil.isNotEmpty(cgJavaValue)){
			Object obj = null;
			try {
				if("class".equals(cgJavaType)){
					//因新增时已经校验了实例化是否可以成功，所以这块就不需要再做一次判断
					obj = MyClassLoader.getClassByScn(cgJavaValue).newInstance();
				}else if("spring".equals(cgJavaType)){
					obj = ApplicationContextUtil.getContext().getBean(cgJavaValue);
				}
				if(obj instanceof CgformEnhanceJavaInter){
					CgformEnhanceJavaInter javaInter = (CgformEnhanceJavaInter) obj;
					javaInter.execute(data);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("执行JAVA增强出现异常！");
			} 
		}
 	}
}