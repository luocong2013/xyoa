package com.xy.oa.checkinout.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jeecgframework.poi.excel.annotation.Excel;

/**   
 * @Title: Entity
 * @Description: 考勤统计表
 * @author onlineGenerator
 * @date 2016-08-19 11:43:37
 * @version V1.0   
 *
 */
@Entity
@Table(name = "s_xy_checkinout", schema = "")
@SuppressWarnings("serial")
public class SXyCheckinoutEntity implements java.io.Serializable {
	/**id*/
	private java.lang.Integer id;
	/**姓名*/
	private java.lang.String name;
	/**员工id*/
	private java.lang.Integer staffId;
	/**部门id*/
	private java.lang.String deptId;
	/**考勤日期*/
	@Excel(name="考勤日期")
	private java.lang.String checkDate;
	/**上班时间*/
	@Excel(name="上班时间")
	private java.lang.String workTime;
	/**下班时间*/
	@Excel(name="下班时间")
	private java.lang.String offWorkTime;
	/**异常时间*/
	@Excel(name="异常时间")
	private java.lang.Integer exceptionMinute;
	/**异常原因*/
	@Excel(name="异常原因")
	private java.lang.String exceptionRemarks;
	/**考勤状态*/
	@Excel(name="考勤状态")
	private java.lang.String isCheckTrue;
	/**考勤类型*/
	@Excel(name="考勤类型")
	private java.lang.String checkType;
	/**考勤原因*/
	@Excel(name="考勤原因")
	private java.lang.String checkRemarks;
	/**申请人ID*/
	private java.lang.String applyId;
	/**日期类型*/
	@Excel(name="日期类型")
	private java.lang.String dateType;
	/**加班时长*/
	@Excel(name="工作时长")
	private java.lang.Double workHour;
	/**创建时间*/
	@Excel(name="创建时间",format = "yyyy-MM-dd")
	private java.util.Date cTime;
	/**创建人*/
	private java.lang.Integer cUser;
	/**更新时间*/
	private java.util.Date uTime;
	/**更新人*/
	private java.lang.Integer uUser;
	/**考勤系统员工ID*/
	private java.lang.Integer userId;
	
	
	private java.lang.Boolean isUse;
	private java.lang.Integer lateMinute;
	private java.lang.Integer earlierMinute;
	private java.lang.String flowState;
	private java.lang.String flowInstId;
	
	
	@Column(name ="is_use",nullable=true,length=11)
	public java.lang.Boolean getIsUse() {
		return isUse;
	}
	public void setIsUse(java.lang.Boolean isUse) {
		this.isUse = isUse;
	}

	@Column(name ="LATE_MINUTE",nullable=true,length=11)
	public java.lang.Integer getLateMinute() {
		return lateMinute;
	}
	public void setLateMinute(java.lang.Integer lateMinute) {
		this.lateMinute = lateMinute;
	}

	@Column(name = "EARLIER_MINUTE",nullable=true,length=11)
	public java.lang.Integer getEarlierMinute() {
		return earlierMinute;
	}
	public void setEarlierMinute(java.lang.Integer earlierMinute) {
		this.earlierMinute = earlierMinute;
	}
	
	@Column(name = "FLOW_STATE", nullable = true, length = 4)
	public java.lang.String getFlowState() {
		return flowState;
	}
	public void setFlowState(java.lang.String flowState) {
		this.flowState = flowState;
	}
	
	@Column(name = "FLOW_INST_ID", nullable = true, length = 64)
	public java.lang.String getFlowInstId() {
		return flowInstId;
	}
	public void setFlowInstId(java.lang.String flowInstId) {
		this.flowInstId = flowInstId;
	}

	@Column(name ="user_id",nullable=true,length=11)
	public java.lang.Integer getUserId() {
		return userId;
	}
	public void setUserId(java.lang.Integer userId) {
		this.userId = userId;
	}

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name ="ID",nullable=false,length=10)
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  id
	 */
	public void setId(java.lang.Integer id){
		this.id = id;
	}
	
	@Column(name ="NAME",nullable=true,length=50)
	public java.lang.String getName() {
		return name;
	}
	
	public void setName(java.lang.String name) {
		this.name = name;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  员工id
	 */
	@Column(name ="STAFF_ID",nullable=true,length=11)
	public java.lang.Integer getStaffId(){
		return this.staffId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  员工id
	 */
	public void setStaffId(java.lang.Integer staffId){
		this.staffId = staffId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  部门id
	 */
	@Column(name ="DEPT_ID",nullable=true,length=32)
	public java.lang.String getDeptId(){
		return this.deptId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  部门id
	 */
	public void setDeptId(java.lang.String deptId){
		this.deptId = deptId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  考勤日期
	 */
	@Column(name ="CHECK_DATE",nullable=true,length=10)
	public java.lang.String getCheckDate(){
		return this.checkDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  考勤日期
	 */
	public void setCheckDate(java.lang.String checkDate){
		this.checkDate = checkDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  上班时间
	 */
	@Column(name ="WORK_TIME",nullable=true,length=10)
	public java.lang.String getWorkTime(){
		return this.workTime;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  上班时间
	 */
	public void setWorkTime(java.lang.String workTime){
		this.workTime = workTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  下班时间
	 */
	@Column(name ="OFF_WORK_TIME",nullable=true,length=10)
	public java.lang.String getOffWorkTime(){
		return this.offWorkTime;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  下班时间
	 */
	public void setOffWorkTime(java.lang.String offWorkTime){
		this.offWorkTime = offWorkTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  异常时间
	 */
	@Column(name ="EXCEPTION_MINUTE",nullable=true,length=10)
	public java.lang.Integer getExceptionMinute(){
		return this.exceptionMinute;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  异常时间
	 */
	public void setExceptionMinute(java.lang.Integer exceptionMinute){
		this.exceptionMinute = exceptionMinute;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  异常原因
	 */
	@Column(name ="EXCEPTION_REMARKS",nullable=true,length=200)
	public java.lang.String getExceptionRemarks(){
		return this.exceptionRemarks;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  异常原因
	 */
	public void setExceptionRemarks(java.lang.String exceptionRemarks){
		this.exceptionRemarks = exceptionRemarks;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  考勤状态
	 */
	@Column(name ="IS_CHECK_TRUE",nullable=true,length=2)
	public java.lang.String getIsCheckTrue(){
		return this.isCheckTrue;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  考勤状态
	 */
	public void setIsCheckTrue(java.lang.String isCheckTrue){
		this.isCheckTrue = isCheckTrue;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  考勤类型
	 */
	@Column(name ="CHECK_TYPE",nullable=true,length=2)
	public java.lang.String getCheckType(){
		return this.checkType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  考勤类型
	 */
	public void setCheckType(java.lang.String checkType){
		this.checkType = checkType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  考勤原因
	 */
	@Column(name ="CHECK_REMARKS",nullable=true,length=200)
	public java.lang.String getCheckRemarks(){
		return this.checkRemarks;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  考勤原因
	 */
	public void setCheckRemarks(java.lang.String checkRemarks){
		this.checkRemarks = checkRemarks;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  申请人ID
	 */
	@Column(name ="APPLY_ID",nullable=true,length=32)
	public java.lang.String getApplyId(){
		return this.applyId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  申请人ID
	 */
	public void setApplyId(java.lang.String applyId){
		this.applyId = applyId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  日期类型
	 */
	@Column(name ="DATE_TYPE",nullable=true,length=2)
	public java.lang.String getDateType(){
		return this.dateType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  日期类型
	 */
	public void setDateType(java.lang.String dateType){
		this.dateType = dateType;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  工作时长
	 */
	@Column(name ="WORK_HOUR",nullable=true,scale=1,length=5)
	public java.lang.Double getWorkHour(){
		return this.workHour;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  工作时长
	 */
	public void setWorkHour(java.lang.Double workHour){
		this.workHour = workHour;
	}
	
	
	@Column(name ="C_TIME",nullable=true)
	public java.util.Date getcTime() {
		return cTime;
	}

	public void setcTime(java.util.Date cTime) {
		this.cTime = cTime;
	}

	@Column(name ="C_USER",nullable=true)
	public java.lang.Integer getcUser() {
		return cUser;
	}

	public void setcUser(java.lang.Integer cUser) {
		this.cUser = cUser;
	}

	@Column(name ="U_TIME",nullable=true)
	public java.util.Date getuTime() {
		return uTime;
	}

	public void setuTime(java.util.Date uTime) {
		this.uTime = uTime;
	}
	@Column(name ="U_USER",nullable=true)
	public java.lang.Integer getuUser() {
		return uUser;
	}

	public void setuUser(java.lang.Integer uUser) {
		this.uUser = uUser;
	}
	
}
