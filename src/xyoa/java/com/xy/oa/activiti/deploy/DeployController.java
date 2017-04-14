package com.xy.oa.activiti.deploy;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.repository.ProcessDefinition;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller("deployController")
@RequestMapping("/deployController")
public class DeployController {
	
	private static final Logger logger = Logger.getLogger(DeployController.class);
	
	@Autowired
	private SystemService systemService;
	
	/**
	 * 流程部署列表
	 * @return
	 */
	@RequestMapping(params = "deploy")
	public ModelAndView deploy() {
		return new ModelAndView("xyoa/activiti/deploy/deployList");
	}
	
	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		List<ProcessDefinition> processDefinitions = systemService.findProcessDefinitions();
		dataGrid.setResults(processDefinitions);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","deployController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导入信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "importExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		String message = null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		MultipartFile file = null;
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			file = entity.getValue();// 获取上传文件对象
			break;
		}
		InputStream inputStream = null;
		try {
			String fileName = file.getOriginalFilename();
			inputStream = file.getInputStream();
			systemService.deploy(inputStream, fileName);
			message = "流程部署成功！";
		} catch (Exception e) {
			message = "流程部署失败！";
			logger.error(ExceptionUtil.getExceptionMessage(e));
		} finally {
			try {
				if (inputStream != null){
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 查看流程定义图片
	 */
	@RequestMapping(params = "viewImage")
	public void viewImage(String pdid, HttpServletResponse response) {
		try {
			InputStream inputStream = systemService.viewImage(pdid);
			response.setContentType("image/png");
			int len = 0;
			byte [] b = new byte[1024];
			while ((len = inputStream.read(b, 0, 1024)) != -1) {
				response.getOutputStream().write(b, 0, len);
			}
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除部署信息
	 * @param deploymentId
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson delDeployment(String deploymentId) {
		AjaxJson j = new AjaxJson();
		String message = "部署信息删除成功";
		systemService.deleteProcessDefinition(deploymentId);
		j.setMsg(message);
		return j;
	}
	
}
