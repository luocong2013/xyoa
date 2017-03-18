package com.xy.oa.calendars.dao;

import java.util.Date;

public interface CalendarsDao {

	public void deleteAll(Date startdate,Date enddate);

	public float getTwoTimeFar(Date frontdate, Date behinddate);

	
}
