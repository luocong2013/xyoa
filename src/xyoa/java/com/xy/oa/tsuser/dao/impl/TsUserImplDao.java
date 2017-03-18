package com.xy.oa.tsuser.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.xy.oa.tsdepart.dao.TsdepartDao;
import com.xy.oa.tsuser.dao.TsUserDao;
@Repository("tsUserDao")
public class TsUserImplDao implements TsUserDao{
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
	
	public TSUser getUserByUsername(String username ) {
		String hql = "from TSUser t where t.userName=?";
		Query query =this.getSession().createQuery(hql);
		query.setParameter(0, username);
		List<TSUser> listAll= query.list();
		if(listAll.size()>0)
		return listAll.get(0);
		else return null;
	}

}
