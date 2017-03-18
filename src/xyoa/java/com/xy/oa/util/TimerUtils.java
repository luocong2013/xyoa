package com.xy.oa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimerUtils {

	public static void main(String[] args) throws ParseException {
//		System.out.println(getWorkMinute("00:02:00", "05:03:00"));
		System.out.print(getExceptionMinute("13:22:00", "13:24:00", 0) + "\t");
//		System.out.print(getExceptionMinute("08:54:00", "10:00:00", 0) + "\t");
//		System.out.print(getExceptionMinute("08:54:00", "17:54:00", 0) + "\t");
//		System.out.print(getExceptionMinute("08:54:00", "18:01:00", 0) + "\t");
//		System.out.print(getExceptionMinute("08:54:00", "18:01:00", 30) + "\t");
//		System.out.print(getExceptionMinute("09:05:00", "18:01:00", 0) + "\t");
//		System.out.print(getExceptionMinute("09:05:00", "17:55:00", 0) + "\t");
//		System.out.print(getExceptionMinute("09:04:00", "18:05:00", 60) + "\t\n");
//
//		System.out.print(getExceptionMinute("09:04:00", "18:03:00", 60) + "\t");
//		System.out.print(getExceptionMinute("10:01:00", "19:02:00", 60) + "\t");
//		System.out.print(getExceptionMinute("10:01:00", "19:00:00", 60) + "\t");
//		System.out.print(getExceptionMinute("10:01:00", "18:58:00", 60) + "\t");
//		System.out.print(getExceptionMinute("10:01:00", "16:01:00", 60) + "\t\n");
//		
//		System.out.print(getExceptionMinute("13:04:00", "18:04:00", 60) + "\t");
//		System.out.print(getExceptionMinute("13:04:00", "18:03:00", 60) + "\t");
//		System.out.print(getExceptionMinute("13:01:00", "17:59:00", 60) + "\t");
//		System.out.print(getExceptionMinute("13:40:00", "18:41:00", 60) + "\t");
//		System.out.print(getExceptionMinute("13:40:00", "18:38:00", 60) + "\t\n");
//		
//		System.out.print(getExceptionMinute("13:40:00", "17:58:00", 60) + "\t");
//		System.out.print(getExceptionMinute("14:00:00", "18:06:00", 60) + "\t");
//		System.out.print(getExceptionMinute("14:01:00", "18:00:00", 60) + "\t");
//		System.out.print(getWorkMinute("11:58:00", "20:31:00") + "\t");
//		System.out.print(getExceptionMinute("17:58:00", "18:01:00", 1) + "\t");
//		System.out.print(getExceptionMinute("18:00:00", "18:38:00", 60) + "\t\n");
		
		System.out.print(getManyWorkMinute("10:11:11", "18:00:58") + "\t");
		
		System.out.println(Constants.XY_WORK_MINUTE.compareTo(500) <= 0);

	}

	/**
	 * 每日工作日常：450分钟（7.5 = 3 （9：00 --12：00） + 4.5（13：30 --18：00））个小时
	 * 
	 * @param startTime
	 *            上班时间
	 * @param endTime
	 *            下班时间
	 * @param changeMinute
	 *            可以延迟上班分钟数
	 * @return 异常分钟数
	 * @throws ParseException
	 */
	public static Integer getExceptionMinute(String startTime, String endTime, Integer changeMinute)
			throws ParseException {
		SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
		Date startDate = time.parse(startTime);
		Date endDate = time.parse(endTime);

		if (startDate.getTime() >= endDate.getTime()) {
			return 450;
		}

		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		int startHour = startCal.get(Calendar.HOUR_OF_DAY);// 小时
		int startMinute = startCal.get(Calendar.MINUTE);// 分

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		int endHour = endCal.get(Calendar.HOUR_OF_DAY);// 小时
		int endMinute = endCal.get(Calendar.MINUTE);// 分

		if (startHour < 9) {
			if (endHour >= 18) {
				return 0;
			}
			if (endHour >= 14 || (endHour == 13 && endMinute >= 30)) {
				return getMinuteValue(endHour, 18, endMinute, 0);
			}
			if (endHour >= 12) {
				return 270;
			}
			if (endHour >= 9) {
				return 450 - getMinuteValue(9, endHour, 0, endMinute);
			} else {
				return 450;
			}
		} else {
			Integer startLateMinute = getMinuteValue(9, startHour, 0, startMinute);
			Integer endDelayMinute = getMinuteValue(18, endHour, 0, endMinute);
			if (startLateMinute <= changeMinute) {
				if (endHour >= 18 && startLateMinute <= endDelayMinute) {
					return 0;
				}
				if (endHour >= 18 && startLateMinute > endDelayMinute) {
					return startLateMinute - endDelayMinute;
				}
				if (endHour >= 14 || (endHour == 13 && endMinute >= 30)) {
					return getMinuteValue(13, endHour, 30, endMinute) + startLateMinute;
				}
				if (endHour >= 12) {
					return 270 + startLateMinute;
				}
				if (startHour < endHour || (endHour == startHour && endMinute >= startMinute)) {
					return 450 - getMinuteValue(startHour, endHour, startMinute, endMinute);
				} else {
					return 450;
				}
			} else {
				if (startHour >= 18) {
					return 450;
				}
				Integer lateMinute = startLateMinute - changeMinute;
				if (startHour <= 12) {
					lateMinute = startLateMinute - changeMinute;
				}
				if (startHour == 12 || (startHour == 13 && startMinute <= 30)) {
					lateMinute = 180;
					changeMinute = 0;
				}
				if (startHour >= 14 || (startHour == 13 && startMinute > 30)) {
					lateMinute = 180 + getMinuteValue(13, startHour, 30, startMinute);
					changeMinute = 0;
				}
				if (endHour >= 18 && changeMinute <= endDelayMinute) {
					return lateMinute;
				}
				if (endHour >= 18 && changeMinute > endDelayMinute) {
					return lateMinute + (changeMinute - endDelayMinute);
				}
				if ((endHour >= 14 || (endHour == 13 && endMinute >= 30))) {
					return lateMinute - endDelayMinute;
				}
				if (endHour >= 12) {
					return lateMinute + 270;
				}
				if (startHour < endHour || (endHour == startHour && endMinute >= startMinute)) {
					return 450 - (getMinuteValue(startHour, endHour, startMinute, endMinute));
				} else {
					return 450;
				}
			}
		}
	}

	private static Integer getMinuteValue(Integer startHour, Integer endHour, Integer startMinute, Integer endMinute) {
		return (endHour - startHour) * 60 + (endMinute - startMinute);
	}

	/**
	 * 计算加班时长：加班无需计算迟到和早退，按照时间差计算（需要刨除中午吃饭90分钟时间。超过450分钟的按照450分钟计算）
	 * 
	 * @param startTime
	 *            上班时间
	 * @param endTime
	 *            下班时间
	 * @return 工作时长（分钟）
	 * @throws ParseException
	 */
	public static Integer getWorkMinute(String startTime, String endTime) throws ParseException {
		SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
		Date startDate = time.parse(startTime);
		Date endDate = time.parse(endTime);

		if (startDate.getTime() >= endDate.getTime()) {
			return 0;
		}

		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		int startHour = startCal.get(Calendar.HOUR_OF_DAY);// 小时
		int startMinute = startCal.get(Calendar.MINUTE);// 分

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		int endHour = endCal.get(Calendar.HOUR_OF_DAY);// 小时
		int endMinute = endCal.get(Calendar.MINUTE);// 分

		// 加班到半夜 ，默认加班半天（4.5小时）
		if (startHour < 4 && endHour < 6) {
			return 270;
		}

		// 加班通宵 ，默认加班一天（7.5小时）
		if (startHour < 4 && endHour > 6) {
			return 450;
		}

		if (startHour < 12) {
			Integer workHour = getMinuteValue(startHour, endHour, startMinute, endMinute);
			if (endHour >= 14 || (endHour == 13 && endMinute >= 30)) {
				workHour = workHour - 90;// 中午休息90分钟
				return workHour > 450 ? 450 : workHour;
			} else {
				return workHour > 270 ? 270 : workHour;
			}
		}
		if (startHour == 12 || (startHour == 13 && startMinute <= 30)) {
			Integer workHour = getMinuteValue(13, endHour, 30, endMinute);
			workHour = workHour < 0 ? 0 : workHour;
			return workHour > 270 ? 270 : workHour;
		} else {
			Integer workHour = getMinuteValue(startHour, endHour, startMinute, endMinute);
			return workHour > 270 ? 270 : workHour;
		}
	}
	
	public static Integer getManyWorkMinute(String startTime, String endTime) throws ParseException {
		SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
		Date startDate = time.parse(startTime);
		Date endDate = time.parse(endTime);

		if (startDate.getTime() >= endDate.getTime()) {
			return 0;
		}

		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		int startHour = startCal.get(Calendar.HOUR_OF_DAY);// 小时
		int startMinute = startCal.get(Calendar.MINUTE);// 分

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		int endHour = endCal.get(Calendar.HOUR_OF_DAY);// 小时
		int endMinute = endCal.get(Calendar.MINUTE);// 分
		
		Integer workHour = getMinuteValue(startHour, endHour, startMinute, endMinute);
		return workHour > 450 ? 450 : workHour;
	}
	
	public static Date addDate(String date,Integer addDay){
		//自定义追加查询条件
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date startDate = time.parse(date);
			Calendar calendar=Calendar.getInstance();  
			calendar.setTime(startDate);
			calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+addDay);//让日期加1
			return calendar.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 若startTime 大于 endTime 返回 1, 等于 返回 0 ，小于返回-1 
	 * @param time
	 * @param endTime
	 * @return
	 * @throws ParseException 
	 */
	public static int compTime(String startTime,String endTime) throws ParseException{
		SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
		if(time.parse(startTime).getTime()>time.parse(endTime).getTime()){
			return 1;
		}
		
		if(time.parse(startTime).getTime() == time.parse(endTime).getTime()){
			return 0;
		}
		
		return -1;
	}
	
	/**
	 * 若startTime 大于 endTime 返回 1, 等于 返回 0 ，小于返回-1 
	 * @param time
	 * @param endTime
	 * @return
	 */
	public static Date getTime(String workTime){
		//自定义追加查询条件
		SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
		try {
			return time.parse(workTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
