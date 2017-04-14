package com.xy.oa.highchars.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.model.json.Highchart;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xy.oa.highchars.apriori.Apriori;
import com.xy.oa.util.Constants;
/**
 * 结果图形化展示
 * @author LuoC
 *
 */
@Controller("highCharsController")
@RequestMapping("/highCharsController")
public class HighCharsController {
	@Autowired
	private SystemService systemService;
	
	/**
	 * 跳转到统计集合页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "staticTabs")
	public ModelAndView statisticTabs(HttpServletRequest request) {
		return new ModelAndView("xyoa/highchars/staticTabs");
	}
	
	
	/**
	 * 员工请假数据统计分析
	 * 
	 * @return
	 */
	@RequestMapping(params = "userAbsence")
	public ModelAndView userBroswer(String reportType, HttpServletRequest request) {
		request.setAttribute("reportType", reportType);
		if("pie".equals(reportType)){
			return new ModelAndView("xyoa/highchars/userAbsencePie");
		}else if("line".equals(reportType)) {
			return new ModelAndView("xyoa/highchars/userAbsenceLine");
		}
		return new ModelAndView("xyoa/highchars/userAbsence");
	}
	
	/**
	 * 报表数据生成
	 * 
	 * @return
	 */
	@RequestMapping(params = "getAbsenceBar")
	@ResponseBody
	public List<Highchart> getAbsenceBar(HttpServletRequest request,String reportType, HttpServletResponse response) {
		List<Highchart> list = new ArrayList<Highchart>();
		List<String> transList = new ArrayList<String>();
		Highchart hc = new Highchart();
		List<Object> lt = new ArrayList<Object>();
		Map<String, Object> map = null;
		String sql = "SELECT u.`realname`, d.`departname`, t.`typename`, CONCAT('s-', s.`start_time`), CONCAT('e-', s.`end_time`) FROM s_xy_absence s "
				+ "LEFT JOIN `t_s_base_user` u ON s.`ts_user_id`=u.`ID` "
				+ "LEFT JOIN `t_s_depart` d ON s.`dept_id`=d.`ID` "
				+ "LEFT JOIN `t_s_type` t ON s.`absence_type`=t.`typecode` "
				+ "LEFT JOIN `t_s_typegroup` tg ON t.`typegroupid`=tg.`ID` "
				+ "WHERE tg.`typegroupcode`=?";
		List<Object> objs = systemService.executeProcedure(sql, "absence");
		if (objs != null && !objs.isEmpty()) {
			for (Object object : objs) {
				Object[] obj = (Object[]) object;
				StringBuilder builder = new StringBuilder();
				for (Object o : obj) {
					builder.append(Constants.LEF_ITEM_SPLIT+o+Constants.RIGHT_ITEM_SPLIT);
				}
				transList.add(builder.toString());
			}
		}
		Apriori apriori = new Apriori(transList);
		Map<String, Integer> frequentCollectionMap = apriori.getFC();
		Set<String> fcKeySet = frequentCollectionMap.keySet();
		for (String fcKey : fcKeySet) {
			map = new HashMap<String, Object>();
			map.put("name", fcKey);
			map.put("y", frequentCollectionMap.get(fcKey));
			lt.add(map);
		}
		hc.setType(reportType);
		hc.setName("频繁项集");
		hc.setData(lt);
		list.add(hc);
		return list;
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 报表数据生成
	 * 
	 * @return
	 */
	@RequestMapping(params = "getAbsenceBar2")
	@ResponseBody
	public List<Highchart> getAbsenceBar2(HttpServletRequest request,String reportType, HttpServletResponse response) {
		List<Highchart> list = new ArrayList<Highchart>();
		Highchart hc = null;
		List<Object> lt = null;
		Map<String, Object> map = null;
		String hql = "SELECT d FROM TSDepart d  WHERE LENGTH(d.orgCode) BETWEEN ? AND ?";
		List<TSDepart> tsDeparts = systemService.findHql(hql, 6, 9);
		for (TSDepart tsDepart : tsDeparts) {
			hc = new Highchart();
			lt = new ArrayList<Object>();
			hc.setType(reportType);
			hc.setName(tsDepart.getDepartname());
			hql = "select count(*) from SXyAbsenceEntity s, TSDepart d where s.tsDept=d and d.orgCode like ?";
			Long count = systemService.singleResult(hql, tsDepart.getOrgCode()+"%");
			
			
			hql = "SELECT t.typename, count(*) FROM SXyAbsenceEntity s, TSType t, TSDepart d "
					+ "WHERE s.absenceType=t.typecode and s.tsDept=d "
					+ "AND t.TSTypegroup.typegroupcode=? AND d.orgCode like ? GROUP BY s.absenceType";
			
			List<Object> objs = systemService.findHql(hql, "absence", tsDepart.getOrgCode()+"%");
			
			if (objs != null && !objs.isEmpty()) {
				for (Object obj : objs) {
					map = new HashMap<String, Object>();
					Object[] objects = (Object[]) obj;
					map.put("name", objects[0]);
					map.put("y", objects[1]);
					Long groupCount = (Long) objects[1];
					Double  percentage = 0.0;
					if (count != null && count.intValue() != 0) {
						percentage = new Double(groupCount)/count;
					}
					map.put("percentage", percentage*100);
					lt.add(map);
				}
			}
			hc.setData(lt);
			list.add(hc);
		}
		return list;
	}

}
