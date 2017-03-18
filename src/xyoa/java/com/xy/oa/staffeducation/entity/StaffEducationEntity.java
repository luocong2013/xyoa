package com.xy.oa.staffeducation.entity;

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
 * @Description: 教育经历
 * @author onlineGenerator
 * @date 2016-09-29 14:39:37
 * @version V1.0   
 *
 */
@Entity
@Table(name = "s_xy_staff_education", schema = "")
@SuppressWarnings("serial")
public class StaffEducationEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**员工id*/
	@Excel(name="员工id")
	private java.lang.Integer staffId;
	/**学历*/
	@Excel(name="学历")
	private java.lang.String educationType;
	/**专业*/
	@Excel(name="专业")
	private java.lang.String major;
	/**开始时间*/
	@Excel(name="开始时间")
	private java.lang.String startDate;
	/**结束时间*/
	@Excel(name="结束时间")
	private java.lang.String endDate;
	/**学校名称*/
	@Excel(name="学校名称")
	private java.lang.String schoolName;
	/**证明人*/
	@Excel(name="证明人")
	private java.lang.String certifyName;
	/**证明人电话*/
	@Excel(name="证明人电话")
	private java.lang.String tel;
	/**creatUser*/
	private java.lang.Integer creatUser;
	/**creatTime*/
	private java.util.Date creatTime;
	/**updateUser*/
	private java.lang.Integer updateUser;
	/**updateTime*/
	private java.util.Date updateTime;
	private java.lang.String educationWay;
	
	@Column(name ="education_way",nullable=true,length=2)
	public java.lang.String getEducationWay() {
		return educationWay;
	}

	public void setEducationWay(java.lang.String educationWay) {
		this.educationWay = educationWay;
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
	 *@return: java.lang.String  学历
	 */
	@Column(name ="EDUCATION_TYPE",nullable=true,length=2)
	public java.lang.String getEducationType(){
		return this.educationType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  学历
	 */
	public void setEducationType(java.lang.String educationType){
		this.educationType = educationType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  专业
	 */
	@Column(name ="MAJOR",nullable=true,length=80)
	public java.lang.String getMajor(){
		return this.major;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  专业
	 */
	public void setMajor(java.lang.String major){
		this.major = major;
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
	 *@return: java.lang.String  学校名称
	 */
	@Column(name ="SCHOOL_NAME",nullable=true,length=80)
	public java.lang.String getSchoolName(){
		return this.schoolName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  学校名称
	 */
	public void setSchoolName(java.lang.String schoolName){
		this.schoolName = schoolName;
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
	 *@return: java.lang.Integer  creatUser
	 */
	@Column(name ="CREAT_USER",nullable=true,length=10)
	public java.lang.Integer getCreatUser(){
		return this.creatUser;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  creatUser
	 */
	public void setCreatUser(java.lang.Integer creatUser){
		this.creatUser = creatUser;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  creatTime
	 */
	@Column(name ="CREAT_TIME",nullable=true)
	public java.util.Date getCreatTime(){
		return this.creatTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  creatTime
	 */
	public void setCreatTime(java.util.Date creatTime){
		this.creatTime = creatTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  updateUser
	 */
	@Column(name ="UPDATE_USER",nullable=true,length=10)
	public java.lang.Integer getUpdateUser(){
		return this.updateUser;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  updateUser
	 */
	public void setUpdateUser(java.lang.Integer updateUser){
		this.updateUser = updateUser;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  updateTime
	 */
	@Column(name ="UPDATE_TIME",nullable=true)
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  updateTime
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
