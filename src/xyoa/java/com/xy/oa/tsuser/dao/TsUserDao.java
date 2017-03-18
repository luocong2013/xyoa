package com.xy.oa.tsuser.dao;

import java.util.List;
import org.jeecgframework.web.system.pojo.base.TSUser;

public interface TsUserDao {
	public TSUser getUserByUsername(String username);
}
