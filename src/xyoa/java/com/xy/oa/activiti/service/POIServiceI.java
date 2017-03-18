package com.xy.oa.activiti.service;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.jeecgframework.core.common.service.CommonService;

public interface POIServiceI extends CommonService {
	
	public void getImportExcel(InputStream inputStream, File[] srcFile, File zipfile, String srcfileName) throws Exception;
	
	
	public void downFile(OutputStream out, File zipfile);
	
	public void deleteDir(File dir);

}
