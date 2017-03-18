package com.xy.oa.sheduletime.service.impl;
import com.xy.oa.sheduletime.service.XyWorkScheduleServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

import com.xy.oa.sheduletime.entity.XySheduleTimeEntity;
import com.xy.oa.sheduletime.entity.XyWorkScheduleEntity;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.io.Serializable;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;

@Service("xyWorkScheduleService")
@Transactional
public class XyWorkScheduleServiceImpl extends CommonServiceImpl implements XyWorkScheduleServiceI {

	
 	public void delete(XyWorkScheduleEntity entity) throws Exception{
 		super.delete(entity);
 		//执行删除操作增强业务
		this.doDelBus(entity);
 	}
 	
 	public Serializable save(XyWorkScheduleEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		//执行新增操作增强业务
 		this.doAddBus(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(XyWorkScheduleEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 		//执行更新操作增强业务
 		this.doUpdateBus(entity);
 	}
 	
 	/**
	 * 新增操作增强业务
	 * @param t
	 * @return
	 */
	private void doAddBus(XyWorkScheduleEntity t) throws Exception{
 	}
 	/**
	 * 更新操作增强业务
	 * @param t
	 * @return
	 */
	private void doUpdateBus(XyWorkScheduleEntity t) throws Exception{
 	}
 	/**
	 * 删除操作增强业务
	 * @param id
	 * @return
	 */
	private void doDelBus(XyWorkScheduleEntity t) throws Exception{
 	}
 	
 	private Map<String,Object> populationMap(XyWorkScheduleEntity t){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", t.getId());
		map.put("schedule_day", t.getScheduleDay());
		map.put("schedule_type", t.getScheduleType());
		map.put("staff_id", t.getStaffId());
		map.put("user_id", t.getUserId());
		map.put("dept_id", t.getDeptId());
		map.put("c_user", t.getCUser());
		map.put("u_user", t.getUUser());
		map.put("c_time", t.getCTime());
		map.put("u_time", t.getUTime());
		return map;
	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @param t
	 * @return
	 */
 	public String replaceVal(String sql,XyWorkScheduleEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{schedule_day}",String.valueOf(t.getScheduleDay()));
 		sql  = sql.replace("#{schedule_type}",String.valueOf(t.getScheduleType()));
 		sql  = sql.replace("#{staff_id}",String.valueOf(t.getStaffId()));
 		sql  = sql.replace("#{user_id}",String.valueOf(t.getUserId()));
 		sql  = sql.replace("#{dept_id}",String.valueOf(t.getDeptId()));
 		sql  = sql.replace("#{c_user}",String.valueOf(t.getCUser()));
 		sql  = sql.replace("#{u_user}",String.valueOf(t.getUUser()));
 		sql  = sql.replace("#{c_time}",String.valueOf(t.getCTime()));
 		sql  = sql.replace("#{u_time}",String.valueOf(t.getUTime()));
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