package com.xy.oa.calendars.entity;

import org.jeecgframework.poi.excel.annotation.Excel;

public class InitCalendars {
	//非持久化属性
		 
		private java.util.Date calendarday_start;
		 
		private java.util.Date calendarday_end;
		
		public java.util.Date getCalendarday_start() {
			return calendarday_start;
		}

		public void setCalendarday_start(java.util.Date calendarday_start) {
			this.calendarday_start = calendarday_start;
		}

		public java.util.Date getCalendarday_end() {
			return calendarday_end;
		}

		public void setCalendarday_end(java.util.Date calendarday_end) {
			this.calendarday_end = calendarday_end;
		}

}
