package com.xy.oa.tsdepart.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.xy.oa.tsdepart.dao.TsdepartDao;
@Repository("tsdeparDao")
public class TsdepartDaoImpl implements TsdepartDao{

	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	public Session getSession() {
		// 事务必须是开启的(Required)，否则获取不到
		return sessionFactory.getCurrentSession();
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
		
		String hql = "from TSDepart t  where t.orgType=1";
		Query query =this.getSession().createQuery(hql);
		//List<TSDepart> listcompany= new ArrayList<>();
	    List <TSDepart> list= query.list();
	   /* for(int i=0;i<list.size();i++)
	    {
	    	if(list.get(i).getTSDeparts().size()>0){
	    		listcompany.add(list.get(i));
	    	}
	    }*/
	  
	    
		return list;
	}


	/**  
	* @Name: getAllDepartByCompany
	* @Description: 得到公司下的部门
	* @author xiaoyong
	* @Parameters: TSDepart company
	* @Return: List<TSDepart>
	*/
	@Override
	
	public List<TSDepart> getAllDepartByCompany(TSDepart company) {
		
		company=(TSDepart) this.getSession().get(TSDepart.class,company.getId());
	    
	    List<TSDepart>  list=company.getTSDeparts();
		return list;
	}


	@Override
	public List<TSDepart> getAllDepart() {
		String hql = "from TSDepart t ";
		Query query =this.getSession().createQuery(hql);
		List<TSDepart> depart= new ArrayList<>();
	    List <TSDepart> list= query.list();
	    for(int i=0;i<list.size();i++)
	    {
	    	if(list.get(i).getTSDeparts().size()==0){
	    		depart.add(list.get(i));
	    	}
	    }
	  
	    
		return depart;
	}

	/**  
	* @Name: getAll
	* @Description: 得到所有组织
	* @author xiaoyong
	* @Parameters: TSDepart company
	* @Return: List<TSDepart>
	*/
	@Override
	
	public List<TSDepart> getAll() {
		String hql = "from TSDepart t";
		Query query =this.getSession().createQuery(hql);		
	    List <TSDepart> listAll= query.list();
		return listAll;
	}

	/**  
	* @Name: getDepartByUpdeparts
	* @Description: 通过部门名字和 公司得到对应部门
	* @author xiaoyong
	* @Parameters: TSDepart company,String dept
	* @Return:  TSDepart 
	*/
	@Override
	public TSDepart getDepartByUpdeparts(TSDepart company,String dept) {
	company=(TSDepart) this.getSession().get(TSDepart.class,company.getId());    
	List<TSDepart>  list=company.getTSDeparts();
	 
	TSDepart deptdepart=new TSDepart();
	getDepartBynameAndCompany(list,deptdepart,dept);
	
	return deptdepart;
	}
public void getDepartBynameAndCompany(List<TSDepart>  list,TSDepart deptdepart,String dept){
	if(list!=null&&list.size()>0){
		for (int i=0;i<list.size();i++){
		
		String Departname=	list.get(i).getDepartname();
		if(Departname!=null &&!"".equals(Departname)&&Departname.equals(dept))
			deptdepart.setId(list.get(i).getId());
		else{
		if(list.get(i).getTSDeparts()!=null&&list.get(i).getTSDeparts().size()>0){
			this.getDepartBynameAndCompany(list.get(i).getTSDeparts(), deptdepart,dept);
				}
			}
			}
		}
}
	@Override
	public TSDepart getDepartByName(String name) {
		String hql = "from TSDepart t  where t.departname =?";
		Query query =this.getSession().createQuery(hql);
		query.setParameter(0, name);
		
	    List <TSDepart> list= query.list();
	    
	    if(list.size()>0)
	    return list.get(0);
	    
	    else return null;
	}

}


