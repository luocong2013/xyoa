package com.xy.oa.tsdepart.dao;

import java.util.List;

import org.jeecgframework.web.system.pojo.base.TSDepart;

public interface TsdepartDao {
	public List<TSDepart> getAllCompany();
	public  List getAllDepartByCompany(TSDepart depart);
	public List<TSDepart> getAllDepart();
	public List<TSDepart> getAll();
	public TSDepart getDepartByUpdeparts(TSDepart company ,String dept);
	public TSDepart getDepartByName(String name);
}
