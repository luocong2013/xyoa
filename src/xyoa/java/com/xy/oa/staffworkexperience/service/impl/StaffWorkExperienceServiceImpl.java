package com.xy.oa.staffworkexperience.service.impl;
import com.xy.oa.staffworkexperience.service.StaffWorkExperienceServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.xy.oa.staffworkexperience.entity.StaffWorkExperienceEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.io.Serializable;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;

@Service("staffWorkExperienceService")
@Transactional
public class StaffWorkExperienceServiceImpl extends CommonServiceImpl implements StaffWorkExperienceServiceI {

	
 	public void delete(StaffWorkExperienceEntity entity) throws Exception{
 		super.delete(entity);
 		//执行删除操作增强业务
		this.doDelBus(entity);
 	}
 	
 	public Serializable save(StaffWorkExperienceEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		//执行新增操作增强业务
 		this.doAddBus(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(StaffWorkExperienceEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 		//执行更新操作增强业务
 		this.doUpdateBus(entity);
 	}
 	
 	/**
	 * 新增操作增强业务
	 * @param t
	 * @return
	 */
	private void doAddBus(StaffWorkExperienceEntity t) throws Exception{
 	}
 	/**
	 * 更新操作增强业务
	 * @param t
	 * @return
	 */
	private void doUpdateBus(StaffWorkExperienceEntity t) throws Exception{
 	}
 	/**
	 * 删除操作增强业务
	 * @param id
	 * @return
	 */
	private void doDelBus(StaffWorkExperienceEntity t) throws Exception{
 	}
 	
 	private Map<String,Object> populationMap(StaffWorkExperienceEntity t){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", t.getId());
		map.put("staff_id", t.getStaffId());
		map.put("work_type", t.getWorkType());
		map.put("start_date", t.getStartDate());
		map.put("end_date", t.getEndDate());
		map.put("company_name", t.getCompanyName());
		map.put("company_count", t.getCompanyCount());
		map.put("position", t.getPosition());
		map.put("work_content", t.getWorkContent());
		map.put("certify_name", t.getCertifyName());
		map.put("tel", t.getTel());
		map.put("creat_user", t.getCreatUser());
		map.put("creat_time", t.getCreatTime());
		map.put("update_user", t.getUpdateUser());
		map.put("update_time", t.getUpdateTime());
		return map;
	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @param t
	 * @return
	 */
 	public String replaceVal(String sql,StaffWorkExperienceEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{staff_id}",String.valueOf(t.getStaffId()));
 		sql  = sql.replace("#{work_type}",String.valueOf(t.getWorkType()));
 		sql  = sql.replace("#{start_date}",String.valueOf(t.getStartDate()));
 		sql  = sql.replace("#{end_date}",String.valueOf(t.getEndDate()));
 		sql  = sql.replace("#{company_name}",String.valueOf(t.getCompanyName()));
 		sql  = sql.replace("#{company_count}",String.valueOf(t.getCompanyCount()));
 		sql  = sql.replace("#{position}",String.valueOf(t.getPosition()));
 		sql  = sql.replace("#{work_content}",String.valueOf(t.getWorkContent()));
 		sql  = sql.replace("#{certify_name}",String.valueOf(t.getCertifyName()));
 		sql  = sql.replace("#{tel}",String.valueOf(t.getTel()));
 		sql  = sql.replace("#{creat_user}",String.valueOf(t.getCreatUser()));
 		sql  = sql.replace("#{creat_time}",String.valueOf(t.getCreatTime()));
 		sql  = sql.replace("#{update_user}",String.valueOf(t.getUpdateUser()));
 		sql  = sql.replace("#{update_time}",String.valueOf(t.getUpdateTime()));
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