package com.xy.oa.activiti.compensateleave.entity;

@SuppressWarnings("serial")
public class CompensateLeavePo extends SXyCompensateLeaveEntity {
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
