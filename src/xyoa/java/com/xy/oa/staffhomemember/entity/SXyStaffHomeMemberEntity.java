package com.xy.oa.staffhomemember.entity;

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
 * @Description: 员工家庭成员
 * @author onlineGenerator
 * @date 2016-10-11 14:29:25
 * @version V1.0   
 *
 */
@Entity
@Table(name = "s_xy_staff_home_member", schema = "")
@SuppressWarnings("serial")
public class SXyStaffHomeMemberEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**姓名*/
	@Excel(name="姓名")
	private java.lang.String name;
	/**关系*/
	@Excel(name="关系")
	private java.lang.String relative;
	/**年龄*/
	@Excel(name="年龄")
	private java.lang.Integer age;
	/**工作单位*/
	@Excel(name="工作单位")
	private java.lang.String workUnit;
	/**职务*/
	@Excel(name="职务")
	private java.lang.String workDuty;
	/**电话*/
	@Excel(name="电话")
	private java.lang.String tel;
	/**创建人*/
	@Excel(name="创建人")
	private java.lang.Integer createUser;
	/**创建时间*/
	@Excel(name="创建时间",format = "yyyy-MM-dd")
	private java.util.Date createTime;
	/**跟新人*/
	@Excel(name="跟新人")
	private java.lang.Integer updateUser;
	/**跟新时间*/
	@Excel(name="跟新时间",format = "yyyy-MM-dd")
	private java.util.Date updateTime;
	private java.lang.Integer staffId;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  姓名
	 */
	@Column(name ="NAME",nullable=true,length=50)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  姓名
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  关系
	 */
	@Column(name ="RELATIVE",nullable=true,length=80)
	public java.lang.String getRelative(){
		return this.relative;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  关系
	 */
	public void setRelative(java.lang.String relative){
		this.relative = relative;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  年龄
	 */
	@Column(name ="AGE",nullable=true,length=10)
	public java.lang.Integer getAge(){
		return this.age;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  年龄
	 */
	public void setAge(java.lang.Integer age){
		this.age = age;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  工作单位
	 */
	@Column(name ="WORK_UNIT",nullable=true,length=80)
	public java.lang.String getWorkUnit(){
		return this.workUnit;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  工作单位
	 */
	public void setWorkUnit(java.lang.String workUnit){
		this.workUnit = workUnit;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  职务
	 */
	@Column(name ="WORK_DUTY",nullable=true,length=80)
	public java.lang.String getWorkDuty(){
		return this.workDuty;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  职务
	 */
	public void setWorkDuty(java.lang.String workDuty){
		this.workDuty = workDuty;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  电话
	 */
	@Column(name ="TEL",nullable=true,length=20)
	public java.lang.String getTel(){
		return this.tel;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电话
	 */
	public void setTel(java.lang.String tel){
		this.tel = tel;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  创建人
	 */
	@Column(name ="CREATE_USER",nullable=true,length=10)
	public java.lang.Integer getCreateUser(){
		return this.createUser;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  创建人
	 */
	public void setCreateUser(java.lang.Integer createUser){
		this.createUser = createUser;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_TIME",nullable=true)
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
