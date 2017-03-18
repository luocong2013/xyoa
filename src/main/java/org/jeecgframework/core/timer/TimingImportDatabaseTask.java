package org.jeecgframework.core.timer;

import org.jeecgframework.web.system.service.CheckInOutDataService;
import org.jeecgframework.web.system.service.MysqlImportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("DatabaseTimingTask")
public class TimingImportDatabaseTask {
	@Autowired
	private MysqlImportDataService mysqlImportDataService;
	
	@Autowired
	private CheckInOutDataService checkInOutDataService;
	
	public void  insertDataInto() throws Exception{
		System.out.println("考勤批处理开始，");

		String batchDate = mysqlImportDataService.addMysql();//获取考勤信息以及导入考勤信息
		checkInOutDataService.checkInOut(batchDate);
		System.out.println("考勤批处理结束，");
	}
	

}

