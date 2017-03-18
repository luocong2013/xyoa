package com.xy.oa.stafftrain.entity;

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
 * @Description: 员工培训经历
 * @author onlineGenerator
 * @date 2016-10-11 14:29:46
 * @version V1.0   
 *
 */
@Entity
@Table(name = "s_xy_staff_train", schema = "")
@SuppressWarnings("serial")
public class SXyStaffTrainEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**课程名称*/
	@Excel(name="课程名称")
	private java.lang.String className;
	/**培训组织*/
	@Excel(name="培训组织")
	private java.lang.String trainOrganization;
	/**时间*/
	@Excel(name="时间")
	private java.lang.String startTime;
	/**所获证书*/
	@Excel(name="所获证书")
	private java.lang.String certificate;
	/**创建人*/
	@Excel(name="创建人")
	private java.lang.Integer creatUser;
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
	 *@return: java.lang.String  课程名称
	 */
	@Column(name ="CLASS_NAME",nullable=true,length=80)
	public java.lang.String getClassName(){
		return this.className;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  课程名称
	 */
	public void setClassName(java.lang.String className){
		this.className = className;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  培训组织
	 */
	@Column(name ="TRAIN_ORGANIZATION",nullable=true,length=80)
	public java.lang.String getTrainOrganization(){
		return this.trainOrganization;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  培训组织
	 */
	public void setTrainOrganization(java.lang.String trainOrganization){
		this.trainOrganization = trainOrganization;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  时间
	 */
	@Column(name ="START_TIME",nullable=true,length=10)
	public java.lang.String getStartTime(){
		return this.startTime;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  时间
	 */
	public void setStartTime(java.lang.String startTime){
		this.startTime = startTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所获证书
	 */
	@Column(name ="CERTIFICATE",nullable=true,length=80)
	public java.lang.String getCertificate(){
		return this.certificate;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所获证书
	 */
	public void setCertificate(java.lang.String certificate){
		this.certificate = certificate;
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
