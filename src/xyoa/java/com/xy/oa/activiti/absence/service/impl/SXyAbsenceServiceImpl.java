package com.xy.oa.activiti.absence.service.impl;

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

import com.xy.oa.activiti.absence.entity.SXyAbsenceEntity;
import com.xy.oa.activiti.absence.service.SXyAbsenceServiceI;
/**
 * 享宇请假表业务层接口实现
 * @author Luo
 *
 */
@Service("sXyAbsenceService")
@Transactional
public class SXyAbsenceServiceImpl extends CommonServiceImpl implements SXyAbsenceServiceI {
	
 	public void delete(SXyAbsenceEntity entity) throws Exception{
 		super.delete(entity);
 		//执行删除操作增强业务
		this.doDelBus(entity);
 	}
 	
 	public Serializable save(SXyAbsenceEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		//执行新增操作增强业务
 		this.doAddBus(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(SXyAbsenceEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 		//执行更新操作增强业务
 		this.doUpdateBus(entity);
 	}
 	
 	/**
	 * 新增操作增强业务
	 * @param t
	 * @return
	 */
	private void doAddBus(SXyAbsenceEntity t) throws Exception{
 	}
 	/**
	 * 更新操作增强业务
	 * @param t
	 * @return
	 */
	private void doUpdateBus(SXyAbsenceEntity t) throws Exception{
 	}
 	/**
	 * 删除操作增强业务
	 * @param id
	 * @return
	 */
	private void doDelBus(SXyAbsenceEntity t) throws Exception{
 	}
 	
	private Map<String,Object> populationMap(SXyAbsenceEntity t){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", t.getId());
		map.put("apply_sttaff_id", t.getApplySttaffId());
		map.put("absence_type", t.getAbsenceType());
		map.put("start_time", t.getStartTime());
		map.put("end_time", t.getEndTime());
		map.put("work_time", t.getWorkTime());
		map.put("apply_absence_day", t.getApplyAbsenceDay());
		map.put("apply_date", t.getApplyDate());
		map.put("back_date", t.getBackDate());
		map.put("remarks", t.getRemarks());
		map.put("transfer_work", t.getTransferWork());
		map.put("back_remarks", t.getBackRemarks());
		map.put("absence_day", t.getAbsenceDay());
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
	public String replaceVal(String sql,SXyAbsenceEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{apply_sttaff_id}",String.valueOf(t.getApplySttaffId()));
 		sql  = sql.replace("#{absence_type}",String.valueOf(t.getAbsenceType()));
 		sql  = sql.replace("#{start_time}",String.valueOf(t.getStartTime()));
 		sql  = sql.replace("#{end_time}",String.valueOf(t.getEndTime()));
 		sql  = sql.replace("#{work_time}",String.valueOf(t.getWorkTime()));
 		sql  = sql.replace("#{apply_absence_day}",String.valueOf(t.getApplyAbsenceDay()));
 		sql  = sql.replace("#{apply_date}",String.valueOf(t.getApplyDate()));
 		sql  = sql.replace("#{back_date}",String.valueOf(t.getBackDate()));
 		sql  = sql.replace("#{remarks}",String.valueOf(t.getRemarks()));
 		sql  = sql.replace("#{transfer_work}",String.valueOf(t.getTransferWork()));
 		sql  = sql.replace("#{back_remarks}",String.valueOf(t.getBackRemarks()));
 		sql  = sql.replace("#{absence_day}",String.valueOf(t.getAbsenceDay()));
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