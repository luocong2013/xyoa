package com.xy.oa.activiti.businesstrip.entity;

@SuppressWarnings("serial")
public class BusinessTripPo extends SXyBusinessTripEntity {
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
