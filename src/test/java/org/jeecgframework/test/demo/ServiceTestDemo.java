package org.jeecgframework.test.demo;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.junit.AbstractUnitTest;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xy.oa.util.Constants;
/**
 * Service 单元测试Demo
 * @author  许国杰
 */
public class ServiceTestDemo extends AbstractUnitTest{
	@Autowired
	private UserService userService;
	
	@Test
	public void testCheckUserExits() {
		TSUser user = new TSUser();
		user.setUserName("admin");
		user.setPassword("c44b01947c9e6e3f");
		TSUser u = userService.checkUserExits(user);
		assert(u.getUserName().equals(user.getUserName()));
	}

	@Test
	public void testGetUserRole() {
		TSUser user = new TSUser();
		user.setId("8a8ab0b246dc81120146dc8181950052");
		String roles = userService.getUserRole(user);
		assert(roles.equals("admin,"));
	}


	/**
	 * 
	 */
	@Test
	public void testT(){
//		String userName = "10003";
//		String sql = "select r.rolecode from t_s_base_user u, t_s_role_user ru, t_s_role r where u.id = ru.userid and r.ID = ru.roleid and u.username = ?";
//		String string = userService.singleResultSQL(sql, userName);
//		System.err.println(string);
		
//		List<String> userIds = userService.executeProcedure("select user_id from t_s_user u left join t_s_user_org uo on u.ID = uo.org_id and uo.org_id = ?", id);
//		String hql = "select r.roleCode from TSUser u, TSRoleUser ru, TSRole r where u = ru.TSUser and r = ru.TSRole and u.userName = ?";
		
//		String hql = "select uo1.tsUser.realName from TSUserOrg uo1 left join uo1.tsUser where uo1.tsUser.userName <> '"+userName+"' and uo1.tsDepart in (select d1 from TSDepart d1 where "
//				+ "(d1 = (select d from TSDepart d, TSUserOrg uo, TSUser u where u = uo.tsUser and d = uo.tsDepart and u.userName = ?)) "
//				+ "or (d1.TSPDepart = (select d from TSDepart d, TSUserOrg uo, TSUser u where u = uo.tsUser and d = uo.tsDepart and u.userName = ?)))";
		
//		String hql = "select uo.tsUser.realName from TSUserOrg uo left join uo.tsDepart where uo.tsDepart.departname = ?";
//		String resultList = userService.singleResult(hql, userName);
//		List<String> resultList = userService.findHql(hql, "HR组");
//		for (String string : resultList) {
//			System.err.println(string);
//		}
//		List<TSUser> resultList = userService.findHql("select u from TSUser u, TSDepart d, TSUserOrg uo where d = uo.tsDepart and u = uo.tsUser and uo.id=?", id);
		
//		String mHql = "from TSUser u where u.userName = ?";
//		TSUser tsUser = userService.singleResult(mHql, userName);
//		//查询当前用户的部门
//		String dHql = "select uo.tsDepart.TSPDepart from TSUserOrg uo where uo.tsUser = ?";
//		TSDepart tsDepart = userService.singleResult(dHql, tsUser);
//		
//		String hql = "select ru.TSUser.realName from TSRoleUser ru, TSUserOrg uo where ru.TSUser = uo.tsUser and ru.TSRole.roleCode = ? and uo.tsDepart = ?";
//		List<String> ro = userService.findHql(hql, "dm", tsDepart);
//		for (String string : ro) {
//			System.err.println(string);
//		}
		
		String sql = "SELECT u.`realname`, d.`departname`, t.`typename`, CONCAT('s-', s.`start_time`) stime, CONCAT('e-', s.`end_time`) etime FROM s_xy_absence s "
				+ "LEFT JOIN `t_s_base_user` u ON s.`ts_user_id`=u.`ID` "
				+ "LEFT JOIN `t_s_depart` d ON s.`dept_id`=d.`ID` "
				+ "LEFT JOIN `t_s_type` t ON s.`absence_type`=t.`typecode` "
				+ "LEFT JOIN `t_s_typegroup` tg ON t.`typegroupid`=tg.`ID` "
				+ "WHERE tg.`typegroupcode`=?";
//		List<Map<String, Object>> lists = userService.findForJdbc(sql, "absence");
//		for (Map<String, Object> map : lists) {
//			System.out.println(map.get("realname"));
//			System.out.println(map.get("departname"));
//			System.out.println(map.get("typename"));
//			System.out.println(map.get("stime"));
//			System.out.println(map.get("etime"));
//		}
		
		List<Object> objs = userService.executeProcedure(sql, "absence");
		if (objs != null && !objs.isEmpty()) {
			for (Object object : objs) {
				Object[] obj = (Object[]) object;
				StringBuilder builder = new StringBuilder();
				for (Object o : obj) {
					builder.append(Constants.LEF_ITEM_SPLIT+o+Constants.RIGHT_ITEM_SPLIT);
				}
				System.out.println(builder.toString());
			}
		}
	}
}
