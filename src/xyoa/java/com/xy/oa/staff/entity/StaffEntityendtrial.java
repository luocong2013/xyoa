package com.xy.oa.staff.entity;

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
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

/**   
 * @Title: Entity
 * @Description: 享宇员工表
 * @author onlineGenerator
 * @date 2016-08-10 15:59:20
 * @version V1.0   
 *
 */

public class StaffEntityendtrial implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**归属公司*/
		private java.lang.String companyId;
	/**部门*/
		private java.lang.String deptId;
	/**姓名*/
	private java.lang.String name;
	/**性别*/
	private java.lang.String sex;
	/**岗位*/
	private java.lang.String jobNo;
	/**民族*/
	private java.lang.String nation;
	/**状态*/
	private java.lang.String state;
	/**年龄*/
//	@Excel(name="年龄" )
	private java.lang.Integer age;
	/**最高学历*/
	private java.lang.String maxDegree;
	/**毕业院校*/
	private java.lang.String fromSchool;
	/**专业*/
	private java.lang.String major;
	/**毕业时间*/
	private java.util.Date graduationDate;
	/**工作年限*/
	//@Excel(name="工作年限")
	private java.lang.Integer workYear;
	/**入本单位日期*/
	private java.util.Date goXyDate;
	/**试用期开始时间*/
	private java.util.Date trialStartData;
	/**试用期结束时间*/
	private java.util.Date trialEndData;
	/**正式转正日期*/
	private java.util.Date positiveDate;
	/**劳动合同开始时间*/
	private java.util.Date contractStartDate;
	/**劳动合同结束时间*/
	private java.util.Date contractEndDate;
	/**年假可用天数*/
	private java.lang.Double leaveCount;
	/**可调休天数*/
	private java.lang.Double offWorkCount;
	/**手机*/
	private java.lang.String mobile;
	/**email*/
	private java.lang.String email;
	/**出生日期*/
	private java.util.Date birthday;
	/**婚姻状况*/
	private java.lang.String marryState;
	/**身份证号码*/
	private java.lang.String certNo;
	/**户籍所在地*/
	private java.lang.String registerAddr;
	/**户籍类型*/
	private java.lang.String registerType;
	/**家庭住址*/
	private java.lang.String addr;
	/**联系人*/
	@Excel(name="联系人")
	private java.lang.String linkmanName;
	/**紧急联系电话*/
		private java.lang.String linkmanTel;
	/**社保账号*/
	private java.lang.String socialSecurityNo;
	/**公积金账号*/
	private java.lang.String fundNo;
	/**简历链接*/
	private java.lang.String cvUrl;
	/**创建人*/
	@Excel(name="创建人")
	private java.lang.Integer createUser;
	/**创建时间*/
	private java.util.Date createTime;
	/**更新人*/
	private java.lang.Integer updateUer;
	/**更新时间*/
	private java.util.Date upudateTime;
	/**招聘来源*/
	private java.lang.String staffSource;
	/**推荐人*/
private java.lang.Integer referenceId;
	/**员工编号*/
	private java.lang.Integer sttaffId;
	/**备注*/
	private java.lang.String remarks;
	private java.lang.Boolean isCheck;
	
	private java.lang.Integer checkId;
	private java.lang.String isStudyAbroad;
	
	private java.lang.String isOutsource;
	
	
	
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
	 *@return: java.lang.String  归属公司
	 */
	public java.lang.String getCompanyId(){
		return this.companyId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  归属公司
	 */
	public void setCompanyId(java.lang.String companyId){
		this.companyId = companyId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  部门
	 */
	public java.lang.String getDeptId(){
		return this.deptId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  部门
	 */
	public void setDeptId(java.lang.String deptId){
		this.deptId = deptId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  姓名
	 */
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
	 *@return: java.lang.String  性别
	 */
	public java.lang.String getSex(){
		return this.sex;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  性别
	 */
	public void setSex(java.lang.String sex){
		this.sex = sex;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  岗位
	 */
	public java.lang.String getJobNo(){
		return this.jobNo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  岗位
	 */
	public void setJobNo(java.lang.String jobNo){
		this.jobNo = jobNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  民族
	 */
	public java.lang.String getNation(){
		return this.nation;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  民族
	 */
	public void setNation(java.lang.String nation){
		this.nation = nation;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  状态
	 */
	public java.lang.String getState(){
		return this.state;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  状态
	 */
	public void setState(java.lang.String state){
		this.state = state;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  年龄
	 */
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
	 *@return: java.lang.String  最高学历
	 */
	public java.lang.String getMaxDegree(){
		return this.maxDegree;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  最高学历
	 */
	public void setMaxDegree(java.lang.String maxDegree){
		this.maxDegree = maxDegree;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  毕业院校
	 */
	public java.lang.String getFromSchool(){
		return this.fromSchool;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  毕业院校
	 */
	public void setFromSchool(java.lang.String fromSchool){
		this.fromSchool = fromSchool;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  专业
	 */
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  毕业时间
	 */
	public java.util.Date getGraduationDate(){
		return this.graduationDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  毕业时间
	 */
	public void setGraduationDate(java.util.Date graduationDate){
		this.graduationDate = graduationDate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  工作年限
	 */
	public java.lang.Integer getWorkYear(){
		return this.workYear;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  工作年限
	 */
	public void setWorkYear(java.lang.Integer workYear){
		this.workYear = workYear;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  入本单位日期
	 */
	public java.util.Date getGoXyDate(){
		return this.goXyDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  入本单位日期
	 */
	public void setGoXyDate(java.util.Date goXyDate){
		this.goXyDate = goXyDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  试用期开始时间
	 */
	public java.util.Date getTrialStartData(){
		return this.trialStartData;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  试用期开始时间
	 */
	public void setTrialStartData(java.util.Date trialStartData){
		this.trialStartData = trialStartData;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  试用期结束时间
	 */
	public java.util.Date getTrialEndData(){
		return this.trialEndData;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  试用期结束时间
	 */
	public void setTrialEndData(java.util.Date trialEndData){
		this.trialEndData = trialEndData;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  正式转正日期
	 */
	public java.util.Date getPositiveDate(){
		return this.positiveDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  正式转正日期
	 */
	public void setPositiveDate(java.util.Date positiveDate){
		this.positiveDate = positiveDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  劳动合同开始时间
	 */
	public java.util.Date getContractStartDate(){
		return this.contractStartDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  劳动合同开始时间
	 */
	public void setContractStartDate(java.util.Date contractStartDate){
		this.contractStartDate = contractStartDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  劳动合同结束时间
	 */
	public java.util.Date getContractEndDate(){
		return this.contractEndDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  劳动合同结束时间
	 */
	public void setContractEndDate(java.util.Date contractEndDate){
		this.contractEndDate = contractEndDate;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  年假可用天数
	 */
	public java.lang.Double getLeaveCount(){
		return this.leaveCount;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  年假可用天数
	 */
	public void setLeaveCount(java.lang.Double leaveCount){
		this.leaveCount = leaveCount;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  可调休天数
	 */
	public java.lang.Double getOffWorkCount(){
		return this.offWorkCount;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  可调休天数
	 */
	public void setOffWorkCount(java.lang.Double offWorkCount){
		this.offWorkCount = offWorkCount;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  手机
	 */
	public java.lang.String getMobile(){
		return this.mobile;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  手机
	 */
	public void setMobile(java.lang.String mobile){
		this.mobile = mobile;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  email
	 */
	public java.lang.String getEmail(){
		return this.email;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  email
	 */
	public void setEmail(java.lang.String email){
		this.email = email;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  出生日期
	 */
	public java.util.Date getBirthday(){
		return this.birthday;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  出生日期
	 */
	public void setBirthday(java.util.Date birthday){
		this.birthday = birthday;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  婚姻状况
	 */
	public java.lang.String getMarryState(){
		return this.marryState;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  婚姻状况
	 */
	public void setMarryState(java.lang.String marryState){
		this.marryState = marryState;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  身份证号码
	 */
	public java.lang.String getCertNo(){
		return this.certNo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  身份证号码
	 */
	public void setCertNo(java.lang.String certNo){
		this.certNo = certNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  户籍所在地
	 */
	public java.lang.String getRegisterAddr(){
		return this.registerAddr;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  户籍所在地
	 */
	public void setRegisterAddr(java.lang.String registerAddr){
		this.registerAddr = registerAddr;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  户籍类型
	 */
	public java.lang.String getRegisterType(){
		return this.registerType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  户籍类型
	 */
	public void setRegisterType(java.lang.String registerType){
		this.registerType = registerType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  家庭住址
	 */
	public java.lang.String getAddr(){
		return this.addr;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  家庭住址
	 */
	public void setAddr(java.lang.String addr){
		this.addr = addr;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  联系人
	 */
	public java.lang.String getLinkmanName(){
		return this.linkmanName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  联系人
	 */
	public void setLinkmanName(java.lang.String linkmanName){
		this.linkmanName = linkmanName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  紧急联系电话
	 */
	public java.lang.String getLinkmanTel(){
		return this.linkmanTel;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  紧急联系电话
	 */
	public void setLinkmanTel(java.lang.String linkmanTel){
		this.linkmanTel = linkmanTel;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  社保账号
	 */
	public java.lang.String getSocialSecurityNo(){
		return this.socialSecurityNo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  社保账号
	 */
	public void setSocialSecurityNo(java.lang.String socialSecurityNo){
		this.socialSecurityNo = socialSecurityNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  公积金账号
	 */
	public java.lang.String getFundNo(){
		return this.fundNo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  公积金账号
	 */
	public void setFundNo(java.lang.String fundNo){
		this.fundNo = fundNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  简历链接
	 */
	public java.lang.String getCvUrl(){
		return this.cvUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  简历链接
	 */
	public void setCvUrl(java.lang.String cvUrl){
		this.cvUrl = cvUrl;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  创建人
	 */
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
	 *@return: java.lang.Integer  更新人
	 */
	public java.lang.Integer getUpdateUer(){
		return this.updateUer;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  更新人
	 */
	public void setUpdateUer(java.lang.Integer updateUer){
		this.updateUer = updateUer;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新时间
	 */
	public java.util.Date getUpudateTime(){
		return this.upudateTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新时间
	 */
	public void setUpudateTime(java.util.Date upudateTime){
		this.upudateTime = upudateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  招聘来源
	 */
	public java.lang.String getStaffSource(){
		return this.staffSource;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  招聘来源
	 */
	public void setStaffSource(java.lang.String staffSource){
		this.staffSource = staffSource;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  推荐人
	 */
	public java.lang.Integer getReferenceId(){
		return this.referenceId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  推荐人
	 */
	public void setReferenceId(java.lang.Integer reffers){
		this.referenceId = reffers;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  员工编号
	 */
	public java.lang.Integer getSttaffId(){
		return this.sttaffId;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  员工编号
	 */
	public void setSttaffId(java.lang.Integer sttaffId){
		this.sttaffId = sttaffId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
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
	 *方法: 设置java.lang.Boolean
	 *
	 */
	public java.lang.Boolean getIsCheck() {
		return isCheck;
	}
	/**
	 *方法: 设置java.lang.Boolean
	 *@param: java.lang.Boolean  是否考勤
	 */
	public void setIsCheck(java.lang.Boolean isCheck) {
		this.isCheck = isCheck;
	}
	public java.lang.String getIsStudyAbroad() {
		return isStudyAbroad;
	}

	public void setIsStudyAbroad(java.lang.String isStudyAbroad) {
		this.isStudyAbroad = isStudyAbroad;
	}
	public java.lang.Integer getCheckId() {
		return checkId;
	}

	public void setCheckId(java.lang.Integer checkId) {
		this.checkId = checkId;
	}
	public java.lang.String getIsOutsource() {
		return isOutsource;
	}

	public void setIsOutsource(java.lang.String isOutsource) {
		this.isOutsource = isOutsource;
	}
	
	
	
}
