package com.xy.oa.checkinout.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.pojo.base.TSBaseUser;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.xy.oa.checkinout.dao.CheckinoutDao;
import com.xy.oa.checkinout.entity.SXyCheckinoutEntity;
import com.xy.oa.staff.entity.StaffEntity;

@Repository("CheckinoutDao")
public class CheckDaoImp implements CheckinoutDao {
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	public Session getSession() {
		// 事务必须是开启的(Required)，否则获取不到
		return sessionFactory.getCurrentSession();
	}

	public List<TSDepart> getDeptName() {
		String hql = "from TSDepart t";
		Query query =this.getSession().createQuery(hql);		
	    List <TSDepart> listAll= query.list();
		return listAll;
	}

	@Override
	public List<StaffEntity> getstaffname() {
		String hql="select s.name from StaffEntity s,SXyCheckinoutEntity c where s.sttaffId=c.staffId";
		Query query =this.getSession().createQuery(hql);
		List<StaffEntity> listAll= query.list();
		return listAll;
	}

	@Override
	public List<TSRoleUser> geTsRoleUsers(String id) {
		String hql="from TSRoleUser where userid=?";
		Query query=this.getSession().createQuery(hql);
		query.setParameter(0, id);
		List<TSRoleUser> list=query.list();
		return list;
	}

	@Override
	public List<TSUserOrg> getdeptid(String id) {
		String hql="from TSUserOrg where user_id=?";
		Query query=this.getSession().createQuery(hql);
		query.setParameter(0, id);
		List<TSUserOrg> list=query.list();
		return list;
	}

	@Override
	public List<TSBaseUser> getStaffId(String id) {
		String hql="from TSBaseUser where id=?";
		Query query=this.getSession().createQuery(hql);
		query.setParameter(0, id);
		List<TSBaseUser> list =query.list();
		return list;
	}
	/**  
	* @Name: getImportData
	* @Description: 通过Date startDate, Date endDate, String name, String companyId,
			String deptId这些条件，得到要导出的考勤
	* @author xiaoyong
	* @Parameters: Date startDate, Date endDate, String name, String companyId,
			String deptId
	* @Return: List<SXyCheckinoutEntity>
	*/
	@Override
	public List<SXyCheckinoutEntity> getImportData(Date start, Date end, String name, 	String deptId,String companyId) {
		SimpleDateFormat dataSimpleDateFormat=new SimpleDateFormat( "yyyy-MM-dd" );
		String startDate=dataSimpleDateFormat.format(start);
		String endDate=dataSimpleDateFormat.format(end);
		
		if(StringUtil.isEmpty(companyId)){
			if(StringUtil.isEmpty(name) &&StringUtil.isEmpty(deptId)){
				String hql="from SXyCheckinoutEntity s where s.checkDate>=? and s.checkDate <=? order by s.staffId asc,s.checkDate asc";
				Query query=this.getSession().createQuery(hql);
				query.setParameter(0, startDate);
				query.setParameter(1, endDate);
				List<SXyCheckinoutEntity> list =query.list();
				return list;
				}
				if(StringUtil.isNotEmpty(name)  &&StringUtil.isEmpty(deptId)){
					String hql="from SXyCheckinoutEntity s where s.checkDate>=? and s.checkDate <=? and s.name=? order by s.staffId asc,s.checkDate asc";
					Query query=this.getSession().createQuery(hql);
					query.setParameter(0, startDate);
					query.setParameter(1, endDate);
					query.setParameter(2, name);
					List<SXyCheckinoutEntity> list =query.list();
					return list;
					}
				if(StringUtil.isEmpty(name) &&StringUtil.isNotEmpty(deptId)){
					String hql="from SXyCheckinoutEntity s where s.checkDate>=? and s.checkDate <=? and s.deptId in ("+deptId+") order by s.staffId asc,s.checkDate asc";
					Query query=this.getSession().createQuery(hql);
					query.setParameter(0, startDate);
					query.setParameter(1, endDate);
					List<SXyCheckinoutEntity> list =query.list();
					return list;
					}
				if(StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(deptId)){
					String hql="from SXyCheckinoutEntity s where s.checkDate>=? and s.checkDate <=? and s.name=? and s.deptId in ("+deptId+") order by s.staffId asc,s.checkDate asc";
					Query query=this.getSession().createQuery(hql);
					query.setParameter(0, startDate);
					query.setParameter(1, endDate);
					query.setParameter(2, name);
					List<SXyCheckinoutEntity> list =query.list();
					return list;
					} 
		}else{
			if(StringUtil.isEmpty(name) &&StringUtil.isEmpty(deptId)){
				String hql="select s from SXyCheckinoutEntity s , StaffEntity e where e.sttaffId = s.staffId and e.companyId ='"+companyId+"' and s.checkDate>=? and s.checkDate <=? order by s.staffId asc,s.checkDate asc";
				Query query=this.getSession().createQuery(hql);
				query.setParameter(0, startDate);
				query.setParameter(1, endDate);
				List<SXyCheckinoutEntity> list =query.list();
				return list;
				}
				if(StringUtil.isNotEmpty(name)  &&StringUtil.isEmpty(deptId)){
					String hql="select s from SXyCheckinoutEntity s , StaffEntity e where e.sttaffId = s.staffId and e.companyId ='"+companyId+"' and s.checkDate>=? and s.checkDate <=? and s.name=? order by s.staffId asc,s.checkDate asc";
					Query query=this.getSession().createQuery(hql);
					query.setParameter(0, startDate);
					query.setParameter(1, endDate);
					query.setParameter(2, name);
					List<SXyCheckinoutEntity> list =query.list();
					return list;
					}
				if(StringUtil.isEmpty(name) &&StringUtil.isNotEmpty(deptId)){
					String hql="select s from SXyCheckinoutEntity s , StaffEntity e where e.sttaffId = s.staffId and e.companyId ='"+companyId+"'and s.checkDate>=? and s.checkDate <=? and s.deptId in ("+deptId+") order by s.staffId asc,s.checkDate asc";
					Query query=this.getSession().createQuery(hql);
					query.setParameter(0, startDate);
					query.setParameter(1, endDate);
					List<SXyCheckinoutEntity> list =query.list();
					return list;
					}
				if(StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(deptId)){
					String hql="select s from SXyCheckinoutEntity s , StaffEntity e where e.sttaffId = s.staffId and e.companyId ='"+companyId+"' and s.checkDate>=? and s.checkDate <=? and s.name=? and s.deptId in ("+deptId+") order by s.staffId asc,s.checkDate asc";
					Query query=this.getSession().createQuery(hql);
					query.setParameter(0, startDate);
					query.setParameter(1, endDate);
					query.setParameter(2, name);
					List<SXyCheckinoutEntity> list =query.list();
					return list;
					} 
		}
		return null;
	}
}
