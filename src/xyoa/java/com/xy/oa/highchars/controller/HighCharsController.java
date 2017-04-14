package com.xy.oa.highchars.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.fop.svg.PDFTranscoder;
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
	 * 跳转到统计集合页面(关联规则)
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "staticTabsRelation")
	public ModelAndView statisticTabsRelation(HttpServletRequest request) {
		return new ModelAndView("xyoa/highchars/staticTabsRelation");
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
	 * 关联规则
	 * @param reportType
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "userAbsenceRelation")
	public ModelAndView userBroswerRelation(String reportType, HttpServletRequest request) {
		request.setAttribute("reportType", reportType);
		if("pie".equals(reportType)){
			return new ModelAndView("xyoa/highchars/userAbsenceRelationPie");
		}else if("line".equals(reportType)) {
			return new ModelAndView("xyoa/highchars/userAbsenceRelationLine");
		}
		return new ModelAndView("xyoa/highchars/userAbsenceRelation");
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
		String sql = "SELECT u.`realname`, d.`departname`, t.`typename`, CONCAT('S-', s.`start_time`), CONCAT('E-', s.`end_time`) FROM s_xy_absence s "
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
			map.put("name", apriori.split(fcKey));
			map.put("y", frequentCollectionMap.get(fcKey));
			lt.add(map);
		}
		hc.setType(reportType);
		hc.setName("支持度计数");
		hc.setData(lt);
		list.add(hc);
		return list;
	}
	
	
	/**
	 * 报表数据生成
	 * 
	 * @return
	 */
	@RequestMapping(params = "getAbsenceBarRelation")
	@ResponseBody
	public List<Highchart> getAbsenceBarRelation(HttpServletRequest request,String reportType, HttpServletResponse response) {
		List<Highchart> list = new ArrayList<Highchart>();
		List<String> transList = new ArrayList<String>();
		Highchart hc = new Highchart();
		List<Object> lt = new ArrayList<Object>();
		Map<String, Object> map = null;
		String sql = "SELECT u.`realname`, d.`departname`, t.`typename`, CONCAT('S-', s.`start_time`), CONCAT('E-', s.`end_time`) FROM s_xy_absence s "
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
		Map<String, Double> relationRulesMap = apriori.getRelationRules(frequentCollectionMap);
		Set<String> rrKeySet = relationRulesMap.keySet();
		for (String rrKey : rrKeySet) {
			map = new HashMap<String, Object>();
			map.put("name", apriori.splitRelation(rrKey));
			map.put("y", apriori.formatD(relationRulesMap.get(rrKey))*100);
			lt.add(map);
		}
		hc.setType(reportType);
		hc.setName("置信度");
		hc.setData(lt);
		list.add(hc);
		return list;
	}
	
	
	/**
	 * hightchart导出图片
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(params = "export")
	public void export(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String type = request.getParameter("type");
		String svg = request.getParameter("svg");
		String filename = request.getParameter("filename");

		filename = filename == null ? "chart" : filename;
		ServletOutputStream out = response.getOutputStream();
		try {
			if (null != type && null != svg) {
				svg = svg.replaceAll(":rect", "rect");
				String ext = "";
				Transcoder t = null;
				if (type.equals("image/png")) {
					ext = "png";
					t = new PNGTranscoder();
				} else if (type.equals("image/jpeg")) {
					ext = "jpg";
					t = new JPEGTranscoder();
				} else if (type.equals("application/pdf")) {
					ext = "pdf";
					t = (Transcoder) new PDFTranscoder();
				} else if (type.equals("image/svg+xml"))
					ext = "svg";
				response.addHeader("Content-Disposition",
						"attachment; filename=" + new String(filename.getBytes("GBK"),"ISO-8859-1") + "." + ext);
				response.addHeader("Content-Type", type);

				if (null != t) {
					TranscoderInput input = new TranscoderInput(
							new StringReader(svg));
					TranscoderOutput output = new TranscoderOutput(out);

					try {
						t.transcode(input, output);
					} catch (TranscoderException e) {
						out.print("Problem transcoding stream. See the web logs for more details.");
						e.printStackTrace();
					}
				} else if (ext.equals("svg")) {
					// out.print(svg);
					OutputStreamWriter writer = new OutputStreamWriter(out,
							"UTF-8");
					writer.append(svg);
					writer.close();
				} else
					out.print("Invalid type: " + type);
			} else {
				response.addHeader("Content-Type", "text/html");
				out
						.println("Usage:\n\tParameter [svg]: The DOM Element to be converted."
								+ "\n\tParameter [type]: The destination MIME type for the elment to be transcoded.");
			}
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
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
