package com.xy.oa.activiti.compensateleave.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSUser;

/**   
 * @Title: Entity
 * @Description: 享宇调休表
 * @author onlineGenerator
 * @date 2016-08-22 17:22:36
 * @version V1.0   
 *
 */
@Entity
@Table(name = "s_xy_compensate_leave", schema = "")
@SuppressWarnings("serial")
public class SXyCompensateLeaveEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**申请人UUID*/
	@Excel(name="申请人UUID")
	private TSUser tsUser;
	/**部门ID*/
	@Excel(name="部门ID")
	private TSDepart tsDept;
	/**申请人ID*/
	@Excel(name="申请人ID")
	private java.lang.Integer applySttaffId;
	/**调休类别*/
	@Excel(name="调休类别")
	private java.lang.String leaveType;
	/**申请调休开始时间*/
	@Excel(name="申请调休开始时间",format = "yyyy-MM-dd")
	private java.util.Date leaveStartTime;
	/**申请调休结束时间*/
	@Excel(name="申请调休结束时间",format = "yyyy-MM-dd")
	private java.util.Date leaveEndTime;
	/**申请调休时长*/
	@Excel(name="申请调休时长")
	private java.math.BigDecimal applyLeaveHour;
	/**调休开始时间*/
	@Excel(name="调休开始时间",format = "yyyy-MM-dd")
	private java.util.Date startTime;
	/**调休结束时间*/
	@Excel(name="调休结束时间",format = "yyyy-MM-dd")
	private java.util.Date endTime;
	/**真实调休时长*/
	@Excel(name="真实调休时长")
	private java.math.BigDecimal leaveHour;
	/**申请日期*/
	@Excel(name="申请日期",format = "yyyy-MM-dd")
	private java.util.Date applyDate;
	/**调休原因*/
	@Excel(name="调休原因")
	private java.lang.String remarks;
	/**工作备份安排*/
	@Excel(name="工作备份安排")
	private java.lang.String transferWork;
	/**销假日期*/
	@Excel(name="销假日期",format = "yyyy-MM-dd")
	private java.util.Date backDate;
	/**销假原因*/
	@Excel(name="销假原因")
	private java.lang.String backRemarks;
	/**创建时间*/
	@Excel(name="创建时间",format = "yyyy-MM-dd")
	private java.util.Date createTime;
	/**创建人*/
	@Excel(name="创建人")
	private java.lang.Integer cUser;
	/**更新时间*/
	@Excel(name="更新时间",format = "yyyy-MM-dd")
	private java.util.Date uTime;
	/**更新人*/
	@Excel(name="更新人")
	private java.lang.Integer uUser;
	/**流程状态*/
	@Excel(name="流程状态")
	private java.lang.String flowState;
	/**流程实例ID*/
	@Excel(name="流程实例ID")
	private java.lang.String flowInstId;
	/**申请编号*/
	private java.lang.String applyNo;
	
	
	@Column(name ="APPLY_NO",nullable=true,length=20)
	public java.lang.String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(java.lang.String applyNo) {
		this.applyNo = applyNo;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
	 */
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="ID",nullable=false,length=36)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  主键
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得TSUser
	 *@return: TSUser  申请人UUID
	 */
	@OneToOne(optional=true)//允许为空
	@JoinColumn(name="TS_USER_ID")
	public TSUser getTsUser() {
		return this.tsUser;
	}

	/**
	 *方法: 设置TSUser
	 *@param: TSUser  申请人UUID
	 */
	public void setTsUser(TSUser tsUser) {
		this.tsUser = tsUser;
	}
	/**
	 *方法: 取得TSDepart
	 *@return: TSDepart  部门
	 */
	@OneToOne(optional=true)//允许为空
	@JoinColumn(name="DEPT_ID")
	public TSDepart getTsDept() {
		return this.tsDept;
	}

	/**
	 *方法: 设置TSDepart
	 *@param: TSDepart  部门
	 */
	public void setTsDept(TSDepart tsDept) {
		this.tsDept = tsDept;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  申请人ID
	 */
	@Column(name ="APPLY_STTAFF_ID",nullable=true,length=11)
	public java.lang.Integer getApplySttaffId(){
		return this.applySttaffId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  申请人ID
	 */
	public void setApplySttaffId(java.lang.Integer applySttaffId){
		this.applySttaffId = applySttaffId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  调休类别
	 */
	@Column(name ="LEAVE_TYPE",nullable=true,length=4)
	public java.lang.String getLeaveType(){
		return this.leaveType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  调休类别
	 */
	public void setLeaveType(java.lang.String leaveType){
		this.leaveType = leaveType;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  申请调休开始时间
	 */
	@Column(name ="LEAVE_START_TIME",nullable=true,length=20)
	public java.util.Date getLeaveStartTime(){
		return this.leaveStartTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  申请调休开始时间
	 */
	public void setLeaveStartTime(java.util.Date leaveStartTime){
		this.leaveStartTime = leaveStartTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  申请调休结束时间
	 */
	@Column(name ="LEAVE_END_TIME",nullable=true,length=20)
	public java.util.Date getLeaveEndTime(){
		return this.leaveEndTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  申请调休结束时间
	 */
	public void setLeaveEndTime(java.util.Date leaveEndTime){
		this.leaveEndTime = leaveEndTime;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  申请调休时长
	 */
	@Column(name ="APPLY_LEAVE_HOUR",nullable=true,scale=1,length=5)
	public java.math.BigDecimal getApplyLeaveHour(){
		return this.applyLeaveHour;
	}

	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  申请调休时长
	 */
	public void setApplyLeaveHour(java.math.BigDecimal applyLeaveHour){
		this.applyLeaveHour = applyLeaveHour;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  调休开始时间
	 */
	@Column(name ="START_TIME",nullable=true,length=20)
	public java.util.Date getStartTime(){
		return this.startTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  调休开始时间
	 */
	public void setStartTime(java.util.Date startTime){
		this.startTime = startTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  调休结束时间
	 */
	@Column(name ="END_TIME",nullable=true,length=20)
	public java.util.Date getEndTime(){
		return this.endTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  调休结束时间
	 */
	public void setEndTime(java.util.Date endTime){
		this.endTime = endTime;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  真实调休时长
	 */
	@Column(name ="LEAVE_HOUR",nullable=true,scale=1,length=5)
	public java.math.BigDecimal getLeaveHour(){
		return this.leaveHour;
	}

	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  真实调休时长
	 */
	public void setLeaveHour(java.math.BigDecimal leaveHour){
		this.leaveHour = leaveHour;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  申请日期
	 */
	@Column(name ="APPLY_DATE",nullable=true,length=20)
	public java.util.Date getApplyDate(){
		return this.applyDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  申请日期
	 */
	public void setApplyDate(java.util.Date applyDate){
		this.applyDate = applyDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  调休原因
	 */
	@Column(name ="REMARKS",nullable=true,length=500)
	public java.lang.String getRemarks(){
		return this.remarks;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  调休原因
	 */
	public void setRemarks(java.lang.String remarks){
		this.remarks = remarks;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  工作备份安排
	 */
	@Column(name ="TRANSFER_WORK",nullable=true,length=500)
	public java.lang.String getTransferWork(){
		return this.transferWork;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  工作备份安排
	 */
	public void setTransferWork(java.lang.String transferWork){
		this.transferWork = transferWork;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  销假日期
	 */
	@Column(name ="BACK_DATE",nullable=true,length=20)
	public java.util.Date getBackDate(){
		return this.backDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  销假日期
	 */
	public void setBackDate(java.util.Date backDate){
		this.backDate = backDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  销假原因
	 */
	@Column(name ="BACK_REMARKS",nullable=true,length=500)
	public java.lang.String getBackRemarks(){
		return this.backRemarks;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  销假原因
	 */
	public void setBackRemarks(java.lang.String backRemarks){
		this.backRemarks = backRemarks;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="C_TIME",nullable=true,length=20)
	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  创建人
	 */
	@Column(name ="C_USER",nullable=true,length=11)
	public java.lang.Integer getCUser(){
		return this.cUser;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  创建人
	 */
	public void setCUser(java.lang.Integer cUser){
		this.cUser = cUser;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新时间
	 */
	@Column(name ="U_TIME",nullable=true,length=20)
	public java.util.Date getUTime(){
		return this.uTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新时间
	 */
	public void setUTime(java.util.Date uTime){
		this.uTime = uTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  更新人
	 */
	@Column(name ="U_USER",nullable=true,length=11)
	public java.lang.Integer getUUser(){
		return this.uUser;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  更新人
	 */
	public void setUUser(java.lang.Integer uUser){
		this.uUser = uUser;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流程状态
	 */
	@Column(name ="FLOW_STATE",nullable=true,length=4)
	public java.lang.String getFlowState(){
		return this.flowState;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流程状态
	 */
	public void setFlowState(java.lang.String flowState){
		this.flowState = flowState;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流程实例ID
	 */
	@Column(name ="FLOW_INST_ID",nullable=true,length=64)
	public java.lang.String getFlowInstId(){
		return this.flowInstId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流程实例ID
	 */
	public void setFlowInstId(java.lang.String flowInstId){
		this.flowInstId = flowInstId;
	}
}
