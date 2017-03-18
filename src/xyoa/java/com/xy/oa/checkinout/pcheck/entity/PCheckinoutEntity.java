package com.xy.oa.checkinout.pcheck.entity;

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
 * @Description: p_checkinout
 * @author onlineGenerator
 * @date 2016-08-22 10:02:22
 * @version V1.0   
 *
 */
@Entity
@Table(name = "p_checkinout", schema = "")
@SuppressWarnings("serial")
public class PCheckinoutEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**员工ID号*/
	@Excel(name="员工ID号")
	private java.lang.Integer userid;
	/**打卡时间*/
	@Excel(name="打卡时间",format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date checktime;
	/**打卡类型*/
	@Excel(name="打卡类型")
	private java.lang.String checktype;
	/**验证方式*/
	@Excel(name="验证方式")
	private java.lang.Integer verifycode;
	/**设备ID*/
	@Excel(name="设备ID")
	private java.lang.String sensorid;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  id
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
	 *@param: java.lang.String  id
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  员工ID号
	 */
	@Column(name ="USERID",nullable=false,length=10)
	public java.lang.Integer getUserid(){
		return this.userid;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  员工ID号
	 */
	public void setUserid(java.lang.Integer userid){
		this.userid = userid;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  打卡时间
	 */
	@Column(name ="CHECKTIME",nullable=false)
	public java.util.Date getChecktime(){
		return this.checktime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  打卡时间
	 */
	public void setChecktime(java.util.Date checktime){
		this.checktime = checktime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  打卡类型
	 */
	@Column(name ="CHECKTYPE",nullable=true,length=1)
	public java.lang.String getChecktype(){
		return this.checktype;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  打卡类型
	 */
	public void setChecktype(java.lang.String checktype){
		this.checktype = checktype;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  验证方式
	 */
	@Column(name ="VERIFYCODE",nullable=true,length=10)
	public java.lang.Integer getVerifycode(){
		return this.verifycode;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  验证方式
	 */
	public void setVerifycode(java.lang.Integer verifycode){
		this.verifycode = verifycode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  设备ID
	 */
	@Column(name ="SENSORID",nullable=true,length=10)
	public java.lang.String getSensorid(){
		return this.sensorid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  设备ID
	 */
	public void setSensorid(java.lang.String sensorid){
		this.sensorid = sensorid;
	}
}
