package com.xy.oa.deptjob.service.impl;
import com.xy.oa.deptjob.service.DeptJobServiceI;
import com.xy.oa.staff.entity.DllDepart;
import com.xy.oa.tsdepart.dao.TsdepartDao;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

import com.xy.oa.deptjob.dao.DeptJobDao;
import com.xy.oa.deptjob.entity.DeptJobEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import java.io.Serializable;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;
import org.jeecgframework.web.system.pojo.base.TSDepart;

@Service("deptJobService")
@Transactional
public class DeptJobServiceImpl extends CommonServiceImpl implements DeptJobServiceI {

	private TsdepartDao tsdepartDao;
	private DeptJobDao deptJobDao;
	
 	public DeptJobDao getDeptJobDao() {
		return deptJobDao;
	}
 	@Resource
	public void setDeptJobDao(DeptJobDao deptJobDao) {
		this.deptJobDao = deptJobDao;
	}
	public TsdepartDao getTsdepartDao() {
		return tsdepartDao;
	}
 	@Resource
	public void setTsdepartDao(TsdepartDao tsdepartDao) {
		this.tsdepartDao = tsdepartDao;
	}

	public void delete(DeptJobEntity entity) throws Exception{
 		super.delete(entity);
 		//执行删除操作增强业务
		this.doDelBus(entity);
 	}
 	
 	public Serializable save(DeptJobEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		//执行新增操作增强业务
 		this.doAddBus(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(DeptJobEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 		//执行更新操作增强业务
 		this.doUpdateBus(entity);
 	}
 	
 	/**
	 * 新增操作增强业务
	 * @param t
	 * @return
	 */
	private void doAddBus(DeptJobEntity t) throws Exception{
 	}
 	/**
	 * 更新操作增强业务
	 * @param t
	 * @return
	 */
	private void doUpdateBus(DeptJobEntity t) throws Exception{
 	}
 	/**
	 * 删除操作增强业务
	 * @param id
	 * @return
	 */
	private void doDelBus(DeptJobEntity t) throws Exception{
 	}
 	
 	private Map<String,Object> populationMap(DeptJobEntity t){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", t.getId());
		map.put("dept_id", t.getDeptId());
		map.put("job_name", t.getJobName());
		map.put("create_time", t.getCreateTime());
		map.put("create_user", t.getCreateUser());
		map.put("update_time", t.getUpdateTime());
		map.put("update_user", t.getUpdateUser());
		
		return map;
	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @param t
	 * @return
	 */
 	public String replaceVal(String sql,DeptJobEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{dept_id}",String.valueOf(t.getDeptId()));
 		sql  = sql.replace("#{job_name}",String.valueOf(t.getJobName()));
 		sql  = sql.replace("#{create_time}",String.valueOf(t.getCreateTime()));
 		sql  = sql.replace("#{create_user}",String.valueOf(t.getCreateUser()));
 		sql  = sql.replace("#{update_time}",String.valueOf(t.getUpdateTime()));
 		sql  = sql.replace("#{update_user}",String.valueOf(t.getUpdateUser()));
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

	@Override
	public List<TSDepart> getALLDepart() {
		
		return tsdepartDao.getAll();
	}
	@Override
	public List<DeptJobEntity> getDeptJobs(String departid) {
		
		return deptJobDao.getDeptJobByDepartId(departid);
	}
	@Override
	public List<DeptJobEntity> getAllDeptJob() {
		// TODO Auto-generated method stub
		return deptJobDao.getAllDeptJob();
	}
}