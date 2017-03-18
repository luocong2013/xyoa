package com.xy.oa.util;

/**
 * 导出异常统计信息
 * @author Luo
 *
 */
public class OutCheck {
	/**
	 * 工号
	 */
	private Integer staffId;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 所属部门
	 */
	private String departName;
	/**
	 * 考勤日期
	 */
	private String checkDate;
	/**
	 * 上班时间
	 */
	private String workTime;
	/**
	 * 下班时间
	 */
	private String offWorkTime;
	/**
	 * 异常原因
	 */
	private String exceptionRemarks;
	/**
	 * 总计时间（分钟）
	 * 真实上班时间
	 */
	private double workHour;
	/**
	 * 迟到时间（分钟）
	 */
	private Integer lateMinute;
	/**
	 * 早退时间（分钟）
	 */
	private Integer earlierMinute;
	/**
	 * 缺勤时间（分钟）
	 */
	private Integer exceptionMinute;
	/**
	 * 备注
	 */
	private String outInfo;
	
	public OutCheck() {
		super();
	}
	
	public OutCheck(Integer staffId, String name, String departName, String checkDate, String workTime, String offWorkTime, 
			String exceptionRemarks, double workHour, Integer lateMinute, Integer earlierMinute, Integer exceptionMinute, String outInfo) {
		this.staffId = staffId;
		this.name = name;
		this.departName = departName;
		this.checkDate = checkDate;
		this.workTime = workTime;
		this.offWorkTime = offWorkTime;
		this.exceptionRemarks = exceptionRemarks;
		this.workHour = workHour;
		this.lateMinute = lateMinute;
		this.earlierMinute = earlierMinute;
		this.exceptionMinute = exceptionMinute;
		this.outInfo = outInfo;
	}
	
	
	public Integer getStaffId() {
		return staffId;
	}
	public void setStaffId(Integer staffId) {
		this.staffId = staffId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartName() {
		return departName;
	}
	public void setDepartName(String departName) {
		this.departName = departName;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	public String getWorkTime() {
		return workTime;
	}
	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	public String getOffWorkTime() {
		return offWorkTime;
	}
	public void setOffWorkTime(String offWorkTime) {
		this.offWorkTime = offWorkTime;
	}
	public String getExceptionRemarks() {
		return exceptionRemarks;
	}
	public void setExceptionRemarks(String exceptionRemarks) {
		this.exceptionRemarks = exceptionRemarks;
	}
	public double getWorkHour() {
		return workHour;
	}
	public void setWorkHour(double workHour) {
		this.workHour = workHour;
	}
	public Integer getLateMinute() {
		return lateMinute;
	}
	public void setLateMinute(Integer lateMinute) {
		this.lateMinute = lateMinute;
	}
	public Integer getEarlierMinute() {
		return earlierMinute;
	}
	public void setEarlierMinute(Integer earlierMinute) {
		this.earlierMinute = earlierMinute;
	}
	public Integer getExceptionMinute() {
		return exceptionMinute;
	}
	public void setExceptionMinute(Integer exceptionMinute) {
		this.exceptionMinute = exceptionMinute;
	}
	public String getOutInfo() {
		return outInfo;
	}
	public void setOutInfo(String outInfo) {
		this.outInfo = outInfo;
	}
	
}
