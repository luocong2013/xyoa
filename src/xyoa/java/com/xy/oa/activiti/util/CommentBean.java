package com.xy.oa.activiti.util;

/**
 * 历史批准信息
 * @author Luo
 *
 */
public class CommentBean {
	/**
	 * 用户真实姓名
	 */
	private String realName;
	/**
	 * 批注信息
	 */
	private String fullMessage;
	/**
	 * 是否同意
	 */
	private String isAgree;
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getFullMessage() {
		return fullMessage;
	}
	public void setFullMessage(String fullMessage) {
		this.fullMessage = fullMessage;
	}
	public String getIsAgree() {
		return isAgree;
	}
	public void setIsAgree(String isAgree) {
		this.isAgree = isAgree;
	}
	
}
