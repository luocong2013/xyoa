package com.xy.oa.activiti.workovertime.entity;

@SuppressWarnings("serial")
public class WorkOvertimePo extends SXyWorkOvertimeEntity {
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
