package com.xy.oa.activiti.businesstrip.entity;

@SuppressWarnings("serial")
public class BusinessTripPo extends SXyBusinessTripEntity {
	/**
	 * 是否已审批
	 */
	private String isApprove;
	
	public String getIsApprove() {
		return isApprove;
	}
	public void setIsApprove(String isApprove) {
		this.isApprove = isApprove;
	}

}
