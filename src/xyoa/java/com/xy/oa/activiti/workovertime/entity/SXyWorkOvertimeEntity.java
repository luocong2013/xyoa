package com.xy.oa.activiti.workovertime.entity;


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
 * @Description: 加班表
 * @author onlineGenerator
 * @date 2016-08-17 15:59:06
 * @version V1.0   
 *
 */
@Entity
@Table(name = "s_xy_work_overtime", schema = "")
@SuppressWarnings("serial")
public class SXyWorkOvertimeEntity implements java.io.Serializable {
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
	/**加班类型*/
	@Excel(name="加班类型")
	private java.lang.String workType;
	/**申请加班开始时间*/
	@Excel(name="申请加班开始时间",format = "yyyy-MM-dd")
	private java.util.Date applyStartTime;
	/**申请加班结束时间*/
	@Excel(name="申请加班结束时间",format = "yyyy-MM-dd")
	private java.util.Date applyEndTime;
	/**申请加班时长*/
	@Excel(name="申请加班时长")
	private java.lang.Double applyWorkHour;
	/**加班开始时间*/
	@Excel(name="加班开始时间",format = "yyyy-MM-dd")
	private java.util.Date startTime;
	/**加班结束时间*/
	@Excel(name="加班结束时间",format = "yyyy-MM-dd")
	private java.util.Date endTime;
	/**真实加班时长*/
	@Excel(name="真实加班时长")
	private java.lang.Double workHour;
	/**可调休时长*/
	@Excel(name="可调休时长")
	private java.lang.Double onWorkHour;
	/**申请日期*/
	@Excel(name="申请日期",format = "yyyy-MM-dd")
	private java.util.Date applyDate;
	/**加班原因*/
	@Excel(name="加班原因")
	private java.lang.String remarks;
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
	 *@return: java.lang.String  加班类型
	 */
	@Column(name="WORK_TYPE",nullable=true,length=12)
	public java.lang.String getWorkType() {
		return this.workType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  加班类型
	 */
	public void setWorkType(java.lang.String workType) {
		this.workType = workType;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  申请加班开始时间
	 */
	@Column(name ="APPLY_START_TIME",nullable=true,length=20)
	public java.util.Date getApplyStartTime(){
		return this.applyStartTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  申请加班开始时间
	 */
	public void setApplyStartTime(java.util.Date applyStartTime){
		this.applyStartTime = applyStartTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  申请加班结束时间
	 */
	@Column(name ="APPLY_END_TIME",nullable=true,length=20)
	public java.util.Date getApplyEndTime(){
		return this.applyEndTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  申请加班结束时间
	 */
	public void setApplyEndTime(java.util.Date applyEndTime){
		this.applyEndTime = applyEndTime;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  申请加班时长
	 */
	@Column(name ="APPLY_WORK_HOUR",nullable=true,scale=1,length=5)
	public java.lang.Double getApplyWorkHour(){
		return this.applyWorkHour;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  申请加班时长
	 */
	public void setApplyWorkHour(java.lang.Double applyWorkHour){
		this.applyWorkHour = applyWorkHour;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  加班开始时间
	 */
	@Column(name ="START_TIME",nullable=true,length=20)
	public java.util.Date getStartTime(){
		return this.startTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  加班开始时间
	 */
	public void setStartTime(java.util.Date startTime){
		this.startTime = startTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  加班结束时间
	 */
	@Column(name ="END_TIME",nullable=true,length=20)
	public java.util.Date getEndTime(){
		return this.endTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  加班结束时间
	 */
	public void setEndTime(java.util.Date endTime){
		this.endTime = endTime;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  真实加班时长
	 */
	@Column(name ="WORK_HOUR",nullable=true,scale=1,length=5)
	public java.lang.Double getWorkHour(){
		return this.workHour;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  真实加班时长
	 */
	public void setWorkHour(java.lang.Double workHour){
		this.workHour = workHour;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  可调休时长
	 */
	@Column(name ="ON_WORK_HOUR",nullable=true,scale=1,length=5)
	public java.lang.Double getOnWorkHour() {
		return this.onWorkHour;
	}
	
	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  可调休时长
	 */
	public void setOnWorkHour(java.lang.Double onWorkHour){
		this.onWorkHour = onWorkHour;
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
	 *@return: java.lang.String  加班原因
	 */
	@Column(name ="REMARKS",nullable=true,length=500)
	public java.lang.String getRemarks(){
		return this.remarks;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  加班原因
	 */
	public void setRemarks(java.lang.String remarks){
		this.remarks = remarks;
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
