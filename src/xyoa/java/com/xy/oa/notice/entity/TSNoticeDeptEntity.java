package com.xy.oa.notice.entity;

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
 * @Description: 按部门通知授权
 * @author onlineGenerator
 * @date 2016-08-22 11:36:07
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_s_notice_dept", schema = "")
@SuppressWarnings("serial")
public class TSNoticeDeptEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**通知id*/
	@Excel(name="通知id")
	private java.lang.String noticeId;
	/**部门id*/
	@Excel(name="部门id")
	private java.lang.String deptId;
	
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
	 *@return: java.lang.String  通知id
	 */
	@Column(name ="NOTICE_ID",nullable=true,length=36)
	public java.lang.String getNoticeId(){
		return this.noticeId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  通知id
	 */
	public void setNoticeId(java.lang.String noticeId){
		this.noticeId = noticeId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  部门id
	 */
	@Column(name ="DEPT_ID",nullable=true,length=36)
	public java.lang.String getDeptId(){
		return this.deptId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  部门id
	 */
	public void setDeptId(java.lang.String deptId){
		this.deptId = deptId;
	}
}
