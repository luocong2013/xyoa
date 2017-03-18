package org.jeecgframework.core.timer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xy.oa.staff.entity.StaffEntity;
import com.xy.oa.staff.service.StaffServiceI;

@Service("DisStaffDataTimingTask")
public class DisStaffDataTimingTask {
	@Autowired
	private StaffServiceI staffService;
	
	
	public void  disStaff() throws Exception{
		List<StaffEntity> staffEntityList = staffService.getList(StaffEntity.class);
		for(StaffEntity staffEntity : staffEntityList){
			Date inCompanyDate = staffEntity.getGoXyDate();
			Date currDate = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			
			if(inCompanyDate != null){
				Long time = currDate.getTime() - inCompanyDate.getTime();
				double goXyMonthForD = time / (1000d*60*60*24*30);
				staffEntity.setSiling(((int)(Math.ceil(goXyMonthForD)))+"");
				staffEntity.setUpudateTime(new Date());
				
				staffService.saveOrUpdate(staffEntity);
			}
		}
		
	}
	
	
	public static void main(String[] args) throws ParseException {
		
		Date inCompanyDate = new SimpleDateFormat("yyyy-MM-dd").parse("2016-09-19");
		Date currDate = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		Long time = currDate.getTime() - inCompanyDate.getTime();
		
		double s1 = time/(1000d*60*60*24*30) ;
		System.out.println(s1);
		System.out.println((int)Math.ceil(s1));
	}
	

}

