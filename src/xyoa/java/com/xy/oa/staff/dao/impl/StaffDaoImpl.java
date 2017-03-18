package com.xy.oa.staff.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.xy.oa.staff.dao.StaffDao;
import com.xy.oa.staff.entity.StaffEntity;
@Repository("staffDao")
public class StaffDaoImpl implements StaffDao{
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	public Session getSession() {
		// 事务必须是开启的(Required)，否则获取不到
		return sessionFactory.getCurrentSession();
	}
	
	
	/**  
	* @Name: getUserByUsername
	* @Description: 通过username得到该tsuser对象。username在员工表中是staffid
	* @author xiaoyong
	* @Parameters: String username
	* @Return: TSUser
	*/
	@Override
	
	public StaffEntity getStaffByUsername(int staffid ) {
		String hql = "from StaffEntity s where s.sttaffId=?";
		Query query =this.getSession().createQuery(hql);
		query.setParameter(0, staffid);
		List<StaffEntity> listAll= query.list();
		if(listAll.size()>0)
		return listAll.get(0);
		else return null;
	}


	@Override
	public int getMaxStaffidByCompanyId(String companyid) {
		String hql = "select max(s.sttaffId) from StaffEntity s where s.companyId=?";		
		Query query =this.getSession().createQuery(hql);
		query.setParameter(0, companyid);
		List list= query.list();
		if(list.get(0)!=null)
		return (Integer) list.get(0);
		else return 0;
	}


	@Override
	public StaffEntity  getStaffByCheckId(int checkid  ) {
		String hql = "from StaffEntity s where s.checkId=?";		
		Query query =this.getSession().createQuery(hql);
		query.setParameter(0, checkid);
		List<StaffEntity> list= query.list();
		if(list!=null && list.size()>0)
			return   list.get(0);
		return null;
	}

	/**  
	* @Name: getTrialEndStaff
	* @Description: 首页中的ajax 转正提醒
	* @author xiaoyong
	*  
	* @Return: list<>
	*/
	@Override
	public List<StaffEntity> getTrialEndStaff() {
		String hql = "from StaffEntity s where s.trialEndData<=? and s.state='2'";		
		Query query =this.getSession().createQuery(hql);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 7);
		query.setParameter(0, cal.getTime());
		 List<StaffEntity> list= query.list();
		
		return   list;
		
	}


	@Override
	public List<StaffEntity> getTrialEndStaff(List  list) {
		String hql = "from StaffEntity s where s.trialEndData<=? and s.deptId in (:dept) and s.state='2'";		
		Query query =this.getSession().createQuery(hql);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 7);
		query.setParameter(0, cal.getTime());		
		query.setParameterList("dept",list);
		
		 List<StaffEntity> stafflist= query.list();
		
		return   stafflist;
		
		
	}


	@Override
	public List<StaffEntity> getStaffByDept(String deptId) {
		String hql = "from StaffEntity s where s.deptId=? and s.state!=0";		
		 Query query =this.getSession().createQuery(hql);
		
		 query.setParameter(0, deptId);
		 List<StaffEntity> list= query.list();
		
		return   list;
	}


	
	
}
