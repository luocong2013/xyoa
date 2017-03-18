package com.xy.oa.deptjob.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.xy.oa.deptjob.dao.DeptJobDao;
import com.xy.oa.deptjob.entity.DeptJobEntity;
@Repository("deptJobDao")
public class DeptJobDaoImpl implements DeptJobDao {


	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	public Session getSession() {
		// 事务必须是开启的(Required)，否则获取不到
		return sessionFactory.getCurrentSession();
	}
	
	public List<DeptJobEntity> getDeptJobByDepartId(String deptid)
	{
	
		String hql = "from DeptJobEntity d where d.deptId=? ";
		Query query =this.getSession().createQuery(hql);
		query.setParameter(0, deptid);
		
	    List <DeptJobEntity> list= query.list();
	   
	  
		return list;
		
	}

	@Override
	public List<DeptJobEntity> getAllDeptJob() {

		String hql = "from DeptJobEntity d";
		Query query =this.getSession().createQuery(hql);
		
	    List <DeptJobEntity> list= query.list();
	   
		return list;
	}
}
