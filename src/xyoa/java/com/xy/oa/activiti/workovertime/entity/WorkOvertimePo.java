package com.xy.oa.activiti.workovertime.entity;

@SuppressWarnings("serial")
public class WorkOvertimePo extends SXyWorkOvertimeEntity {
	private String isAgree;
	private String isNotAgree;
	private String isHrAgree;
	public String getIsAgree() {
		return isAgree;
	}
	public void setIsAgree(String isAgree) {
		this.isAgree = isAgree;
	}
	public String getIsNotAgree() {
		return isNotAgree;
	}
	public void setIsNotAgree(String isNotAgree) {
		this.isNotAgree = isNotAgree;
	}
	public String getIsHrAgree() {
		return isHrAgree;
	}
	public void setIsHrAgree(String isHrAgree) {
		this.isHrAgree = isHrAgree;
	}
}
