package com.xy.oa.sheduletime.entity;

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
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

/**   
 * @Title: Entity
 * @Description: xy_work_schedule
 * @author onlineGenerator
 * @date 2016-09-17 10:51:03
 * @version V1.0   
 *
 */
@Entity
@Table(name = "xy_work_schedule", schema = "")
@SuppressWarnings("serial")
public class XyWorkScheduleEntity implements java.io.Serializable {
	/**ID*/
	private java.lang.String id;
	/**工作日期*/
	@Excel(name="工作日期")
	private java.lang.String scheduleDay;
	/**排班类型*/
	@Excel(name="排班类型")
	private java.lang.String scheduleType;
	/**员工id*/
	@Excel(name="员工id")
	private java.lang.Integer staffId;
	/**员工uuid*/
	@Excel(name="员工uuid")
	private java.lang.String userId;
	/**部门ID*/
	@Excel(name="部门ID")
	private java.lang.String deptId;
	/**创建人*/
	@Excel(name="创建人")
	private java.lang.Integer cUser;
	/**更新人*/
	@Excel(name="更新人")
	private java.lang.Integer uUser;
	/**创建时间*/
	@Excel(name="创建时间",format = "yyyy-MM-dd")
	private java.util.Date cTime;
	/**更新时间*/
	@Excel(name="更新时间",format = "yyyy-MM-dd")
	private java.util.Date uTime;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  ID
	 */
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="ID",nullable=false,length=12)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  ID
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  工作日期
	 */
	@Column(name ="SCHEDULE_DAY",nullable=true,length=3)
	public java.lang.String getScheduleDay(){
		return this.scheduleDay;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  工作日期
	 */
	public void setScheduleDay(java.lang.String scheduleDay){
		this.scheduleDay = scheduleDay;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  排班类型
	 */
	@Column(name ="SCHEDULE_TYPE",nullable=true)
	public java.lang.String getScheduleType(){
		return this.scheduleType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  排班类型
	 */
	public void setScheduleType(java.lang.String scheduleType){
		this.scheduleType = scheduleType;
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
	 *@return: java.lang.String  员工uuid
	 */
	@Column(name ="USER_ID",nullable=true,length=10)
	public java.lang.String getUserId(){
		return this.userId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  员工uuid
	 */
	public void setUserId(java.lang.String userId){
		this.userId = userId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  部门ID
	 */
	@Column(name ="DEPT_ID",nullable=true,length=10)
	public java.lang.String getDeptId(){
		return this.deptId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  部门ID
	 */
	public void setDeptId(java.lang.String deptId){
		this.deptId = deptId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  创建人
	 */
	@Column(name ="C_USER",nullable=true,length=10)
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  更新人
	 */
	@Column(name ="U_USER",nullable=true,length=10)
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="C_TIME",nullable=true)
	public java.util.Date getCTime(){
		return this.cTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCTime(java.util.Date cTime){
		this.cTime = cTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新时间
	 */
	@Column(name ="U_TIME",nullable=true)
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
}
