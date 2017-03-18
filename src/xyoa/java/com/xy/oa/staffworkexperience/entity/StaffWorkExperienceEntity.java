package com.xy.oa.staffworkexperience.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

/**   
 * @Title: Entity
 * @Description: 工作经历
 * @author onlineGenerator
 * @date 2016-10-09 10:51:26
 * @version V1.0   
 *
 */
@Entity
@Table(name = "s_xy_staff_work_experience", schema = "")
@SuppressWarnings("serial")
public class StaffWorkExperienceEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**员工id*/
	@Excel(name="员工id")
	private java.lang.Integer staffId;
	/**工作类型*/
	@Excel(name="工作类型")
	private java.lang.String workType;
	/**开始时间*/
	@Excel(name="开始时间")
	private java.lang.String startDate;
	/**结束时间*/
	@Excel(name="离职时间")
	private java.lang.String endDate;
	/**公司名称*/
	@Excel(name="公司名称")
	private java.lang.String companyName;
	/**公司规模*/
	@Excel(name="公司规模")
	private java.lang.String companyCount;
	/**职位*/
	@Excel(name="职位")
	private java.lang.String position;
	/**工作内容*/
	@Excel(name="工作内容")
	private java.lang.String workContent;
	@Excel(name="薪资")
	private java.lang.String salary;
	@Excel(name="离职原因")
	private java.lang.String leaveReason;
	@Excel(name="有保密协议")
	private java.lang.String haveProtocol;
	@Excel(name="有法律事宜")
	private java.lang.String haveLaw;
	/**具体事宜*/
	@Excel(name="具体事宜")
	private java.lang.String specificLaw;
	/**证明人*/
	@Excel(name="证明人")
	private java.lang.String certifyName;
	/**证明人电话*/
	@Excel(name="证明人电话")
	private java.lang.String tel;
	/**创建人*/
	@Excel(name="创建人")
	private java.lang.Integer creatUser;
	/**创建时间*/
	@Excel(name="创建时间",format = "yyyy-MM-dd")
	private java.util.Date creatTime;
	/**跟新人*/
	@Excel(name="跟新人")
	private java.lang.Integer updateUser;
	/**跟新时间*/
	@Excel(name="跟新时间",format = "yyyy-MM-dd")
	private java.util.Date updateTime;
	
	
	@Column(name ="have_protocol",nullable=true,length=2)
	public java.lang.String getHaveProtocol() {
		return haveProtocol;
	}

	public void setHaveProtocol(java.lang.String haveProtocol) {
		this.haveProtocol = haveProtocol;
	}
	@Column(name ="have_law",nullable=true,length=2)
	public java.lang.String getHaveLaw() {
		return haveLaw;
	}

	public void setHaveLaw(java.lang.String haveLaw) {
		this.haveLaw = haveLaw;
	}
	@Column(name ="specific_law",nullable=true,length=500)
	public java.lang.String getSpecificLaw() {
		return specificLaw;
	}

	public void setSpecificLaw(java.lang.String specificLaw) {
		this.specificLaw = specificLaw;
	}

	@Column(name ="salary",nullable=true,length=10)
	public java.lang.String getSalary() {
		return salary;
	}

	public void setSalary(java.lang.String salary) {
		this.salary = salary;
	}
	@Column(name ="leave_reason",nullable=true,length=500)
	public java.lang.String getLeaveReason() {
		return leaveReason;
	}

	public void setLeaveReason(java.lang.String leaveReason) {
		this.leaveReason = leaveReason;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  id
	 */
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="ID",nullable=true,length=36)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  id
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  员工id
	 */
	@Column(name ="STAFF_ID",nullable=true,length=10)
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
	 *@return: java.lang.String  工作类型
	 */
	@Column(name ="WORK_TYPE",nullable=true,length=2)
	public java.lang.String getWorkType(){
		return this.workType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  工作类型
	 */
	public void setWorkType(java.lang.String workType){
		this.workType = workType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  开始时间
	 */
	@Column(name ="START_DATE",nullable=true,length=10)
	public java.lang.String getStartDate(){
		return this.startDate;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  开始时间
	 */
	public void setStartDate(java.lang.String startDate){
		this.startDate = startDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  结束时间
	 */
	@Column(name ="END_DATE",nullable=true,length=10)
	public java.lang.String getEndDate(){
		return this.endDate;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  结束时间
	 */
	public void setEndDate(java.lang.String endDate){
		this.endDate = endDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  公司名称
	 */
	@Column(name ="COMPANY_NAME",nullable=true,length=80)
	public java.lang.String getCompanyName(){
		return this.companyName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  公司名称
	 */
	public void setCompanyName(java.lang.String companyName){
		this.companyName = companyName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  公司规模
	 */
	@Column(name ="COMPANY_COUNT",nullable=true,length=2)
	public java.lang.String getCompanyCount(){
		return this.companyCount;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  公司规模
	 */
	public void setCompanyCount(java.lang.String companyCount){
		this.companyCount = companyCount;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  职位
	 */
	@Column(name ="POSITION",nullable=true,length=80)
	public java.lang.String getPosition(){
		return this.position;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  职位
	 */
	public void setPosition(java.lang.String position){
		this.position = position;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  工作内容
	 */
	@Column(name ="WORK_CONTENT",nullable=true,length=500)
	public java.lang.String getWorkContent(){
		return this.workContent;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  工作内容
	 */
	public void setWorkContent(java.lang.String workContent){
		this.workContent = workContent;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  证明人
	 */
	@Column(name ="CERTIFY_NAME",nullable=true,length=50)
	public java.lang.String getCertifyName(){
		return this.certifyName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  证明人
	 */
	public void setCertifyName(java.lang.String certifyName){
		this.certifyName = certifyName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  证明人电话
	 */
	@Column(name ="TEL",nullable=true,length=20)
	public java.lang.String getTel(){
		return this.tel;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  证明人电话
	 */
	public void setTel(java.lang.String tel){
		this.tel = tel;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  创建人
	 */
	@Column(name ="CREAT_USER",nullable=true,length=10)
	public java.lang.Integer getCreatUser(){
		return this.creatUser;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  创建人
	 */
	public void setCreatUser(java.lang.Integer creatUser){
		this.creatUser = creatUser;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREAT_TIME",nullable=true)
	public java.util.Date getCreatTime(){
		return this.creatTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreatTime(java.util.Date creatTime){
		this.creatTime = creatTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  跟新人
	 */
	@Column(name ="UPDATE_USER",nullable=true,length=10)
	public java.lang.Integer getUpdateUser(){
		return this.updateUser;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  跟新人
	 */
	public void setUpdateUser(java.lang.Integer updateUser){
		this.updateUser = updateUser;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  跟新时间
	 */
	@Column(name ="UPDATE_TIME",nullable=true)
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  跟新时间
	 */
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}
	//非持久性属性
	
		private java.lang.String del;
		@Transient
		public java.lang.String getDel() {
			return del;
		}

		public void setDel(java.lang.String del) {
			this.del = del;
		}
		private java.lang.String update;
		@Transient
		public java.lang.String getUpdate() {
			return update;
		}

		public void setUpdate(java.lang.String update) {
			this.update = update;
		}
		private java.lang.String detail;
		@Transient
		public java.lang.String getDetail() {
			return detail;
		}

		public void setDetail(java.lang.String detail) {
			this.detail = detail;
		}
		
		
}
