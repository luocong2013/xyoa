package com.xy.oa.staff.service.impl;
import com.xy.oa.staff.service.StaffServiceI;
import com.xy.oa.tsdepart.dao.TsdepartDao;
import com.xy.oa.tsuser.dao.TsUserDao;
import com.xy.oa.util.Constants;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

import com.xy.oa.deptjob.dao.DeptJobDao;
import com.xy.oa.deptjob.entity.DeptJobEntity;
import com.xy.oa.staff.dao.StaffDao;
import com.xy.oa.staff.entity.DllDepart;
import com.xy.oa.staff.entity.StaffEntity;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import java.io.Serializable;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;

@Service("staffService")
@Transactional
public class StaffServiceImpl extends CommonServiceImpl implements StaffServiceI {

	private TsdepartDao tsdepartDao;
	
	private DeptJobDao deptJobDao;
	
	private TsUserDao tsUserDao;
	
	private StaffDao staffDao;
	

	public StaffDao getStaffDao() {
		return staffDao;
	}
	@Resource
	public void setStaffDao(StaffDao staffDao) {
		this.staffDao = staffDao;
	}
	public TsUserDao getTsUserDao() {
		return tsUserDao;
	}
	@Resource
	public void setTsUserDao(TsUserDao tsUserDao) {
		this.tsUserDao = tsUserDao;
	}
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

	public void delete(StaffEntity entity) throws Exception{
 		super.delete(entity);
 		//执行删除操作增强业务
		this.doDelBus(entity);
 	}
 	
 	public Serializable save(StaffEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		//执行新增操作增强业务
 		this.doAddBus(entity);
 		return t;
 	}
 	
 	public void saveOrUpdate(StaffEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 		//执行更新操作增强业务
 		this.doUpdateBus(entity);
 	}
 	
 	/**
	 * 新增操作增强业务
	 * @param t
	 * @return
	 */
	private void doAddBus(StaffEntity t) throws Exception{
 	}
 	/**
	 * 更新操作增强业务
	 * @param t
	 * @return
	 */
	private void doUpdateBus(StaffEntity t) throws Exception{
 	}
 	/**
	 * 删除操作增强业务
	 * @param id
	 * @return
	 */
	private void doDelBus(StaffEntity t) throws Exception{
 	}
 	
 	private Map<String,Object> populationMap(StaffEntity t){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", t.getId());
		map.put("company_id", t.getCompanyId());
		map.put("dept_id", t.getDeptId());
		map.put("name", t.getName());
		map.put("sex", t.getSex());
		map.put("job_no", t.getJobNo());
		map.put("nation", t.getNation());
		map.put("state", t.getState());
		map.put("age", t.getAge());
		map.put("max_degree", t.getMaxDegree());
		map.put("from_school", t.getFromSchool());
		map.put("major", t.getMajor());
		map.put("graduation_date", t.getGraduationDate());
		map.put("work_year", t.getWorkYear());
		map.put("go_xy_date", t.getGoXyDate());
		map.put("trial_start_data", t.getTrialStartData());
		map.put("trial_end_data", t.getTrialEndData());
		map.put("positive_date", t.getPositiveDate());
		map.put("contract_start_date", t.getContractStartDate());
		map.put("contract_end_date", t.getContractEndDate());
		map.put("leave_count", t.getLeaveCount());
		map.put("off_work_count", t.getOffWorkCount());
		map.put("mobile", t.getMobile());
		map.put("email", t.getEmail());
		map.put("birthday", t.getBirthday());
		map.put("marry_state", t.getMarryState());
		map.put("cert_no", t.getCertNo());
		map.put("register_addr", t.getRegisterAddr());
		map.put("register_type", t.getRegisterType());
		map.put("addr", t.getAddr());
		map.put("linkman_name", t.getLinkmanName());
		map.put("linkman_tel", t.getLinkmanTel());
		map.put("social_security_no", t.getSocialSecurityNo());
		map.put("fund_no", t.getFundNo());
		map.put("cv_url", t.getCvUrl());
		map.put("create_user", t.getCreateUser());
		map.put("create_time", t.getCreateTime());
		map.put("update_uer", t.getUpdateUer());
		map.put("upudate_time", t.getUpudateTime());
		map.put("staff_source", t.getStaffSource());
		map.put("reffers", t.getReferenceId());
		map.put("sttaff_id", t.getSttaffId());
		map.put("remarks", t.getRemarks());
		map.put("isCheck",t.getIsCheck());
		map.put("isStudyAbroad", t.getIsStudyAbroad());
		map.put("checkId", t.getCheckId());
		map.put("is_outsource", t.getIsOutsource());
		return map;
	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @param t
	 * @return
	 */
 	public String replaceVal(String sql,StaffEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{company_id}",String.valueOf(t.getCompanyId()));
 		sql  = sql.replace("#{dept_id}",String.valueOf(t.getDeptId()));
 		sql  = sql.replace("#{name}",String.valueOf(t.getName()));
 		sql  = sql.replace("#{sex}",String.valueOf(t.getSex()));
 		sql  = sql.replace("#{job_no}",String.valueOf(t.getJobNo()));
 		sql  = sql.replace("#{nation}",String.valueOf(t.getNation()));
 		sql  = sql.replace("#{state}",String.valueOf(t.getState()));
 		sql  = sql.replace("#{age}",String.valueOf(t.getAge()));
 		sql  = sql.replace("#{max_degree}",String.valueOf(t.getMaxDegree()));
 		sql  = sql.replace("#{from_school}",String.valueOf(t.getFromSchool()));
 		sql  = sql.replace("#{major}",String.valueOf(t.getMajor()));
 		sql  = sql.replace("#{graduation_date}",String.valueOf(t.getGraduationDate()));
 		sql  = sql.replace("#{work_year}",String.valueOf(t.getWorkYear()));
 		sql  = sql.replace("#{go_xy_date}",String.valueOf(t.getGoXyDate()));
 		sql  = sql.replace("#{trial_start_data}",String.valueOf(t.getTrialStartData()));
 		sql  = sql.replace("#{trial_end_data}",String.valueOf(t.getTrialEndData()));
 		sql  = sql.replace("#{positive_date}",String.valueOf(t.getPositiveDate()));
 		sql  = sql.replace("#{contract_start_date}",String.valueOf(t.getContractStartDate()));
 		sql  = sql.replace("#{contract_end_date}",String.valueOf(t.getContractEndDate()));
 		sql  = sql.replace("#{leave_count}",String.valueOf(t.getLeaveCount()));
 		sql  = sql.replace("#{off_work_count}",String.valueOf(t.getOffWorkCount()));
 		sql  = sql.replace("#{mobile}",String.valueOf(t.getMobile()));
 		sql  = sql.replace("#{email}",String.valueOf(t.getEmail()));
 		sql  = sql.replace("#{birthday}",String.valueOf(t.getBirthday()));
 		sql  = sql.replace("#{marry_state}",String.valueOf(t.getMarryState()));
 		sql  = sql.replace("#{cert_no}",String.valueOf(t.getCertNo()));
 		sql  = sql.replace("#{register_addr}",String.valueOf(t.getRegisterAddr()));
 		sql  = sql.replace("#{register_type}",String.valueOf(t.getRegisterType()));
 		sql  = sql.replace("#{addr}",String.valueOf(t.getAddr()));
 		sql  = sql.replace("#{linkman_name}",String.valueOf(t.getLinkmanName()));
 		sql  = sql.replace("#{linkman_tel}",String.valueOf(t.getLinkmanTel()));
 		sql  = sql.replace("#{social_security_no}",String.valueOf(t.getSocialSecurityNo()));
 		sql  = sql.replace("#{fund_no}",String.valueOf(t.getFundNo()));
 		sql  = sql.replace("#{cv_url}",String.valueOf(t.getCvUrl()));
 		sql  = sql.replace("#{create_user}",String.valueOf(t.getCreateUser()));
 		sql  = sql.replace("#{create_time}",String.valueOf(t.getCreateTime()));
 		sql  = sql.replace("#{update_uer}",String.valueOf(t.getUpdateUer()));
 		sql  = sql.replace("#{upudate_time}",String.valueOf(t.getUpudateTime()));
 		sql  = sql.replace("#{staff_source}",String.valueOf(t.getStaffSource()));
 		sql  = sql.replace("#{reffers}",String.valueOf(t.getReferenceId()));
 		sql  = sql.replace("#{sttaff_id}",String.valueOf(t.getSttaffId()));
 		sql  = sql.replace("#{remarks}",String.valueOf(t.getRemarks()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		sql  = sql.replace("#{isCheck}",String.valueOf(t.getIsCheck()));
 		sql  = sql.replace("#{isStudyAbroad}",String.valueOf(t.getIsStudyAbroad()));
 		sql  = sql.replace("#{checkId}",String.valueOf(t.getCheckId()));
 		sql  = sql.replace("#{isOutsource}",String.valueOf(t.getIsOutsource()));	 
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
 	
 	/**  
	* @Name: etAllCompany
	* @Description: 得到所有公司
	* @author xiaoyong
	* @Parameters: 无
	* @Return: List<TSDepart>
	*/
	@Override
	public List<TSDepart> getAllCompany() {
		
		return tsdepartDao.getAllCompany();
	}
	
	/**  
	* @Name: getAllDepartByCompany
	* @Description: 得到公司下的部门
	* @author xiaoyong
	* @Parameters: TSDepart company
	* @Return: List<TSDepart>
	*/
	@Override
	public List<DllDepart> getAllDepartByCompany(TSDepart company) {
		List<TSDepart> list =tsdepartDao.getAllDepartByCompany(company);
		List<DllDepart> listDllDepart=new ArrayList<>();
		
		for(int i=0;i<list.size();i++)
		{
			DllDepart dll=new DllDepart();
			dll.setId(list.get(i).getId()); 
			dll.setDepartname(list.get(i).getDepartname());
			listDllDepart.add(dll);			
		}
		
		return listDllDepart;
	}
//	@Override
//	public List<TSDepart> getAll() {
//		List<TSDepart> tSDeparts = new ArrayList<TSDepart>();
//		//当前登录的用户
//		TSUser tsUser = ResourceUtil.getSessionUserName();
//		//查询当前用户的角色编码
//		String rHql = "select ru.TSRole.roleCode from TSRoleUser ru where ru.TSUser = ?";
//		List<String> roleCodes = this.commonDao.findHql(rHql, tsUser);
//		boolean isQueryAll = false;
//		for(String newRoleCode : roleCodes){
//			if (Constants.HR.equals(newRoleCode) || Constants.HRDM.equals(newRoleCode) || 
//					Constants.VICE.equals(newRoleCode) || Constants.CEO.equals(newRoleCode) || Constants.ADMIN.equals(newRoleCode)) {
//				isQueryAll = true;
//			}
//		}
//		
//		StringBuffer hql = new StringBuffer(" from TSDepart t where 1=1 ");
//		if (isQueryAll) {
//			hql.append(" and t.orgType = ? order by t.orgCode ");
//			tSDeparts = this.commonDao.findHql(hql.toString(), "2");
//		}else {
//				
//				TSDepart dePart = this.commonDao.getEntity(TSDepart.class, tsUser.getCurrentDepart().getId());
//				hql.append(" and t.TSPDepart = ?  order by t.orgCode ");
//				tSDeparts = this.commonDao.findHql(hql.toString(), dePart);
//				tSDeparts.add(0, dePart);
//		}
//		
//		return tSDeparts;
//	}
	
//	@Override
//	public List<String> getSSDeptId(String deptId) {
//		TSDepart dePart = this.commonDao.getEntity(TSDepart.class, deptId);
//		List<String>  deptList = new ArrayList<String>();
//		deptList.add(dePart.getId());
//		for(TSDepart subDept : dePart.getTSDeparts()){
//			deptList.add(subDept.getId());
//		}
//		return deptList;
//	}
	
	@Override
	public List<DeptJobEntity> getDeptJobsByDepartId(String deptId) {
		List <DeptJobEntity> list=deptJobDao.getDeptJobByDepartId(deptId);
		return list;
	}
	@Override
	public TSUser getUserByUsername(String username ) {
		return tsUserDao.getUserByUsername(username);
	}
	@Override
	public StaffEntity getStaffByUsername(int staffid) {
		
		return staffDao.getStaffByUsername(staffid);
	}
	@Override
	public int getMaxStaffidByCompanyId(String companyid) {
		// TODO Auto-generated method stub
		return staffDao.getMaxStaffidByCompanyId(companyid);
	}
	@Override
	public TSDepart getDepartByUpdeparts(TSDepart company, String dept) {
		
		return tsdepartDao.getDepartByUpdeparts(company, dept);
	}
	@Override
	public TSDepart getDepartByName(String name) {
		
		return tsdepartDao.getDepartByName( name);
	}
	/**  
	* @Name: reduceOffWorkCount
	* @Description: 增加可调休天数
	* @author xiaoyong
	* @Parameters: int staffid, double addtime
	* @Return: boolean  成功true  失败返回false
	*/
	@Override
	public boolean addOffWorkCount(int staffid, double addtime) {
		
		StaffEntity staff=this.getStaffByUsername(staffid);
		if(staff!=null){
		 staff.setOffWorkCount(staff.getOffWorkCount()+addtime);
		 commonDao.updateEntitie(staff);
		 return true ;
		}else return false ;
		
	}
	
	/**  
	* @Name: getOffWorkCount
	* @Description: 得到可调休天数
	* @author xiaoyong
	* @Parameters: int staffid
	* @Return: boolean  成功 返回可调休天数   失败返回0
	*/
	@Override
	public double getOffWorkCount(int staffid) {
		StaffEntity staff=this.getStaffByUsername(staffid);
		if (staff!=null)
		return staff.getOffWorkCount();
		else return  0;
	}
	
	/**  
	* @Name: reduceOffWorkCount
	* @Description: 减少可调休天数
	* @author xiaoyong
	* @Parameters: int staffid, double reducetime
	* @Return: boolean  成功true  失败返回false
	*/
	@Override
	public boolean reduceOffWorkCount(int staffid, double reducetime) {
		StaffEntity staff=this.getStaffByUsername(staffid);
		if(staff!=null&&(staff.getOffWorkCount()-reducetime)>=0){
			staff.setOffWorkCount(staff.getOffWorkCount()-reducetime);
			commonDao.updateEntitie(staff);
			return true;
		}else return false;
		
	}
	@Override
	public StaffEntity getStaffByCheckId(int checkid) {
		// TODO Auto-generated method stub
		return staffDao.getStaffByCheckId(checkid);
	}
	
	//得到所有即将转正员工
	@Override
	public List<StaffEntity> getTrialEndStaff() {
		// TODO Auto-generated method stub
		return staffDao.getTrialEndStaff();
	}
	
	//得到某门下的所有即将转正员工
	@SuppressWarnings("unchecked")
	@Override
	public List<StaffEntity> getTrialEndStaff(String deptId) {
		List list=new ArrayList<>();
        TSDepart dept=(TSDepart) this.getSession().get(TSDepart.class,deptId);  
        list.add(dept.getId());
	    List<TSDepart>  tslist=dept.getTSDeparts();	
	    
	    getDeptidBylist(tslist, list);
	    
		return staffDao.getTrialEndStaff(list);
	}
	//得到某部门的所有组
	public void getDeptidBylist(List<TSDepart> tslist, List list)
	{
		 if(tslist!=null&&tslist.size()>0){
			    for(int i=0;i<tslist.size();i++)
			    {
			    	list.add(tslist.get(i).getId());
			    	getDeptidBylist(tslist.get(i).getTSDeparts(), list);
			    }
			    }
	}
	@Override
	public List<StaffEntity> getStaffByDept(String deptId) {
		// TODO Auto-generated method stub
		return staffDao.getStaffByDept( deptId);
	}
}