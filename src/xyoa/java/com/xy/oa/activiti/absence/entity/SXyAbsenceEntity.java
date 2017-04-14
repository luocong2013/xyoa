package com.xy.oa.activiti.absence.entity;

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
 * @Description: 请假表
 * @author onlineGenerator
 * @date 2016-08-16 11:24:09
 * @version V1.0   
 *
 */
@Entity
@Table(name = "s_xy_absence", schema = "")
@SuppressWarnings("serial")
public class SXyAbsenceEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**申请人*/
	@Excel(name="申请人")
	private TSUser tsUser;
	/**部门*/
	@Excel(name="部门")
	private TSDepart tsDept;
	/**申请人编号*/
	@Excel(name="申请人编号")
	private java.lang.Integer applySttaffId;
	/**休假类型*/
	@Excel(name="休假类型")
	private java.lang.String absenceType;
	/**申请请假开始时间*/
	@Excel(name="申请请假开始时间",format = "yyyy-MM-dd")
	private java.util.Date startTime;
	/**申请请假结束时间*/
	@Excel(name="申请请假结束时间",format = "yyyy-MM-dd")
	private java.util.Date endTime;
	/**请假结束工作时间*/
	private java.util.Date workTime;
	/**申请请假小时数*/
	@Excel(name="申请请假小时数")
	private java.lang.Float applyAbsenceDay;
	/**申请日期*/
	private java.util.Date applyDate;
	/**销假日期*/
	private java.util.Date backDate;
	/**请假原因*/
	private java.lang.String remarks;
	/**工作备份安排*/
	private java.lang.String transferWork;
	/**销假原因*/
	private java.lang.String backRemarks;
	/**真实请假小时数*/
	private java.lang.Float absenceDay;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建人*/
	private java.lang.Integer cUser;
	/**更新时间*/
	private java.util.Date uTime;
	/**更新人*/
	private java.lang.Integer uUser;
	/**流程状态*/
	@Excel(name="流程状态")
	private java.lang.String flowState;
	/**流程实例ID*/
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
	 * 一对一关系
	 * 方法：取得TSUser
	 * @return: TSUser 申请人
	 */
	@OneToOne(optional=true)
	@JoinColumn(name="TS_USER_ID")
	public TSUser getTsUser() {
		return this.tsUser;
	}
	/**
	 * 方法：设置TSUser
	 * @param: TSUser 申请人
	 */
	public void setTsUser(TSUser tsUser) {
		this.tsUser = tsUser;
	}
	/**
	 *一对一关系
	 *方法: 取得TSDepart
	 *@return: TSDepart  部门
	 */
	@OneToOne(optional=true)
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
	 *@return: java.lang.Integer  申请人编号
	 */
	@Column(name ="APPLY_STTAFF_ID",nullable=true,length=11)
	public java.lang.Integer getApplySttaffId(){
		return this.applySttaffId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  申请人编号
	 */
	public void setApplySttaffId(java.lang.Integer applySttaffId){
		this.applySttaffId = applySttaffId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  休假类型
	 */
	@Column(name ="ABSENCE_TYPE",nullable=true,length=4)
	public java.lang.String getAbsenceType(){
		return this.absenceType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  休假类型
	 */
	public void setAbsenceType(java.lang.String absenceType){
		this.absenceType = absenceType;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  申请请假开始时间
	 */
	@Column(name ="START_TIME",nullable=true,length=20)
	public java.util.Date getStartTime(){
		return this.startTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  申请请假开始时间
	 */
	public void setStartTime(java.util.Date startTime){
		this.startTime = startTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  申请请假结束时间
	 */
	@Column(name ="END_TIME",nullable=true,length=20)
	public java.util.Date getEndTime(){
		return this.endTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  申请请假结束时间
	 */
	public void setEndTime(java.util.Date endTime){
		this.endTime = endTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  请假结束工作时间
	 */
	@Column(name ="WORK_TIME",nullable=true,length=20)
	public java.util.Date getWorkTime(){
		return this.workTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  请假结束工作时间
	 */
	public void setWorkTime(java.util.Date workTime){
		this.workTime = workTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  申请请假小时数
	 */
	@Column(name ="APPLY_ABSENCE_DAY",nullable=true,length=5)
	public java.lang.Float getApplyAbsenceDay(){
		return this.applyAbsenceDay;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  申请请假小时数
	 */
	public void setApplyAbsenceDay(java.lang.Float applyAbsenceDay){
		this.applyAbsenceDay = applyAbsenceDay;
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
	 *@return: java.lang.String  请假原因
	 */
	@Column(name ="REMARKS",nullable=true,length=500)
	public java.lang.String getRemarks(){
		return this.remarks;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  请假原因
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  真实请假小时数
	 */
	@Column(name ="ABSENCE_DAY",nullable=true,length=5)
	public java.lang.Float getAbsenceDay(){
		return this.absenceDay;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  真实请假小时数
	 */
	public void setAbsenceDay(java.lang.Float absenceDay){
		this.absenceDay = absenceDay;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="C_TIME",nullable=true,length=20)
	public java.util.Date getCreateTime(){
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
