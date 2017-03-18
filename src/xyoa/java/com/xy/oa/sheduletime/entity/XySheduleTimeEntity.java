package com.xy.oa.sheduletime.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

/**   
 * @Title: Entity
 * @Description: 排班
 * @author onlineGenerator
 * @date 2016-09-13 17:19:07
 * @version V1.0   
 *
 */
@Entity
@Table(name = "xy_shedule_time", schema = "")
@SuppressWarnings("serial")
public class XySheduleTimeEntity implements java.io.Serializable {
	//父菜单
	private XySheduleTimeEntity xySheduleTimeEntity;
	/**主键*/
	private java.lang.String id;
	/**时间*/
	@Excel(name="时间")
	private java.lang.String timename;
	/**排序*/
	@Excel(name="排序")
	private int orders=0;
	private Date starttime;
	private Date endtime;
	
	@Column(name ="starttime",nullable=true)
	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	@Column(name ="endtime",nullable=true)
	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}


	
	
	
	private List<XySheduleTimeEntity> xySheduleTimeEntitys =new ArrayList<>();
	
	
	
	@ManyToOne(fetch = FetchType.EAGER)

	@JoinColumn(name = "parentid")
	
	public XySheduleTimeEntity getXySheduleTimeEntity() {
		return xySheduleTimeEntity;
	}

	public void setXySheduleTimeEntity(XySheduleTimeEntity xySheduleTimeEntity) {
		this.xySheduleTimeEntity = xySheduleTimeEntity;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "xySheduleTimeEntity")
	public List<XySheduleTimeEntity> getXySheduleTimeEntitys() {
		return xySheduleTimeEntitys;
	}

	public void setXySheduleTimeEntitys(List<XySheduleTimeEntity> xySheduleTimeEntitys) {
		this.xySheduleTimeEntitys = xySheduleTimeEntitys;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  时间
	 */
	@Column(name ="TIMENAME",nullable=true,length=50)
	public java.lang.String getTimename(){
		return this.timename;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  时间
	 */
	public void setTimename(java.lang.String timename){
		this.timename = timename;
	}
	
	@Column(name ="ORDERS",length=11)
	public java.lang.Integer getOrders(){
		return this.orders;
	}

	
	public void setOrders(int order){
		this.orders = order;
	}
	
}
