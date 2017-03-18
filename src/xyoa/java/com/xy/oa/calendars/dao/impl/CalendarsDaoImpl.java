package com.xy.oa.calendars.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.xy.oa.calendars.dao.CalendarsDao;
import com.xy.oa.calendars.entity.SXyCalendarsEntity;

@Repository("calendarsDao")
public class CalendarsDaoImpl implements CalendarsDao {
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	
	

	public Session getSession() {
		// 事务必须是开启的(Required)，否则获取不到
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void deleteAll(Date startdate,Date enddate) {
		
		String hql = "delete from SXyCalendarsEntity where calendarday>=? and calendarday<=? ";
		Query query =this.getSession().createQuery(hql);
		query.setParameter(0, startdate);
		query.setParameter(1, enddate);
	//	Query query=this.getSessionFactory().getCurrentSession().createQuery(hql);
		query.executeUpdate();
		
	}

	
	/**  
	* @Name: getTwoTimeFar
	* @Description: 通过前后两个时间返回间隔的工作时间
	* @author xiaoyong
	* @Parameters: Date frontdate, Date behinddate   前后两个时间
	* @Return: float
	*/
	@Override
	public float getTwoTimeFar(Date frontdate, Date behinddate) {
		//SXyCalendarsEntity
		
		String hql = "from SXyCalendarsEntity c where c.calendarday>? and c.calendarday<?";
		Query query =this.getSession().createQuery(hql);
		query.setParameter(0, frontdate);
		query.setParameter(1, behinddate);
		List<TSUser> listAll= query.list();
		
		return 0;
	}

}
