package com.xy.oa.calendars.entity;

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
 * @Description: 节假日明细表
 * @author onlineGenerator
 * @date 2016-08-04 23:27:58
 * @version V1.0   
 *
 */
@Entity
@Table(name = "S_XY_CALENDARS", schema = "")
@SuppressWarnings("serial")
public class SXyCalendarsEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**日期*/
	@Excel(name="日期",format = "yyyy-MM-dd")
	private java.util.Date calendarday;
	/**日期类型*/
	@Excel(name="日期类型")
	private java.lang.String calendartype;
	/**备注*/
	@Excel(name="备注")
	private java.lang.String remarks="";
	/**创建时间*/
	@Excel(name="创建时间",format = "yyyy-MM-dd")
	private java.util.Date ctime;
	/**更新时间*/
	@Excel(name="更新时间",format = "yyyy-MM-dd")
	private java.util.Date utime;
	/**创建人*/
	@Excel(name="创建人")
	private java.lang.String cuser;
	/**更新人*/
	@Excel(name="更新人")
	private java.lang.String uuser;
	
	
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  日期
	 */
	@Column(name ="CALENDARDAY",nullable=true,length=50)
	public java.util.Date getCalendarday(){
		return this.calendarday;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  日期
	 */
	public void setCalendarday(java.util.Date calendarday){
		this.calendarday = calendarday;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  日期类型
	 */
	@Column(name ="CALENDARTYPE",nullable=true,length=1)
	public java.lang.String getCalendartype(){
		return this.calendartype;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  日期类型
	 */
	public void setCalendartype(java.lang.String calendartype){
		this.calendartype = calendartype;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="REMARKS",nullable=true,length=50)
	public java.lang.String getRemarks(){
		return this.remarks;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRemarks(java.lang.String remarks){
		this.remarks = remarks;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CTIME",nullable=true,length=50)
	public java.util.Date getCtime(){
		return this.ctime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCtime(java.util.Date ctime){
		this.ctime = ctime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新时间
	 */
	@Column(name ="UTIME",nullable=true,length=50)
	public java.util.Date getUtime(){
		return this.utime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新时间
	 */
	public void setUtime(java.util.Date utime){
		this.utime = utime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人
	 */
	@Column(name ="CUSER",nullable=true,length=50)
	public java.lang.String getCuser(){
		return this.cuser;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人
	 */
	public void setCuser(java.lang.String cuser){
		this.cuser = cuser;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人
	 */
	@Column(name ="UUSER",nullable=true,length=50)
	public java.lang.String getUuser(){
		return this.uuser;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人
	 */
	public void setUuser(java.lang.String uuser){
		this.uuser = uuser;
	}
}
