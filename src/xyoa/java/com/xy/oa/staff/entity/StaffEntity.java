package com.xy.oa.staff.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
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
@Entity
@Table(name = "s_xy_staff", schema = "")
@SuppressWarnings("serial")
@ExcelTarget("staffEntity")
public class StaffEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**归属公司*/
	@Excel(name="归属公司")
	private java.lang.String companyId;
	/**部门*/
	@Excel(name="部门")
	private java.lang.String deptId;
	@Excel(name="岗位")
	private java.lang.String jobNo;
	/**姓名*/
	@Excel(name="姓名")
	private java.lang.String name;
	/**员工编号*/
	@Excel(name="员工编号")
	private java.lang.Integer sttaffId;
	/**性别*/
	@Excel(name="性别" ,replace={"男_0","女_1"})
	private java.lang.String sex;
	//@Excel(name="政治面貌" ,replace={"党员_A","团员_B","无_C"})
	private java.lang.String politicsStatus;
	/**岗位*/
	
	/**民族*/
	@Excel(name="民族",replace={"汉族_0","蒙古族_1","满族_2","回族_3","藏族_4","维吾尔族_5","壮族_6","其他_7"})
	private java.lang.String nation;
	/**状态*/
	@Excel(name="状态", replace={"离职_0","已转正_1","试用期_2","其他_3","兼职_4","外包_5","实习_6"})
	private java.lang.String state;
	/**年龄*/
	@Excel(name="年龄" )
	private java.lang.Integer age;
	/**最高学历*/
	@Excel(name="最高学历" ,replace={"本科_0","专科_1","高中_2","硕士_3","博士_4","本科在读_5","硕士研究生在读_6","博士研究生在读_7","其它_8"})
	private java.lang.String maxDegree;
	/**毕业院校*/
	@Excel(name="毕业院校")
	private java.lang.String fromSchool;
	/**专业*/
	@Excel(name="专业")
	private java.lang.String major;
	/**毕业时间*/
	@Excel(name="毕业时间",format = "yyyy/MM/dd")
	private java.util.Date graduationDate;
	/**工作年限*/
	@Excel(name="工作年限")
	private java.lang.Integer workYear;
	/**入本单位日期*/
	@Excel(name="入本单位日期",format = "yyyy/MM/dd")
	private java.util.Date goXyDate;
	/**试用期开始时间*/
	@Excel(name="试用期开始时间",format = "yyyy/MM/dd")
	private java.util.Date trialStartData;
	/**试用期结束时间*/
	@Excel(name="试用期结束时间",format = "yyyy/MM/dd")
	private java.util.Date trialEndData;
	/**正式转正日期*/
	@Excel(name="正式转正日期",format = "yyyy/MM/dd")
	private java.util.Date positiveDate;

	@Excel(name="司龄")
	private java.lang.String siling;
	
	/**劳动合同开始时间*/
	@Excel(name="劳动合同开始时间",format = "yyyy/MM/dd")
	private java.util.Date contractStartDate;
	/**劳动合同结束时间*/
	@Excel(name="劳动合同结束时间",format = "yyyy/MM/dd")
	private java.util.Date contractEndDate;
	/**年假可用天数*/
	@Excel(name="年假可用天数")
	private java.lang.Double leaveCount;
	/**可调休天数*/
	@Excel(name="可调休天数")
	private java.lang.Double offWorkCount;
	/**手机*/
	@Excel(name="手机")
	private java.lang.String mobile;
	/**email*/
	@Excel(name="email")
	private java.lang.String email;
	/**出生日期*/
	@Excel(name="出生日期",format = "yyyy/MM/dd")
	private java.util.Date birthday;
	/**婚姻状况*/
	@Excel(name="婚姻状况",replace={"已婚_0","未婚_1","已婚已育_2","离异_3","其他_4"})
	private java.lang.String marryState;
	/**身份证号码*/
	@Excel(name="身份证号码")
	private java.lang.String certNo;
	/**户籍所在地*/
	@Excel(name="户籍所在地")
	private java.lang.String registerAddr;
	/**户籍类型*/
	@Excel(name="户籍类型" ,replace={"农村_0","城镇_1"})
	private java.lang.String registerType;
	/**家庭住址*/
	@Excel(name="家庭住址")
	private java.lang.String addr;
	/**联系人*/
	@Excel(name="联系人")
	private java.lang.String linkmanName;
	@Excel(name="与联系人关系")
	private java.lang.String linkmanRelative;
	/**紧急联系电话*/
	@Excel(name="紧急联系电话")
	private java.lang.String linkmanTel;
	
	/**社保账号*/
	@Excel(name="社保账号")
	private java.lang.String socialSecurityNo;
	/**公积金账号*/
	@Excel(name="公积金账号")
	private java.lang.String fundNo;
	/**简历链接*/
	@Excel(name="简历链接")
	private java.lang.String cvUrl;
	/**创建人*/
	
	private java.lang.Integer createUser;
	/**创建时间*/
	
	private java.util.Date createTime;
	/**更新人*/
	
	private java.lang.Integer updateUer;
	/**更新时间*/
	
	private java.util.Date upudateTime;
	/**招聘来源*/
	@Excel(name="招聘来源",replace={"智联招聘_0","拉勾网_1","前程无忧_2","猎聘网_3","Boss直聘_4","内部推荐_15"})
	private java.lang.String staffSource;
	/**推荐人*/
	@Excel(name="推荐人")
	private java.lang.Integer referenceId;
	
	
	@Excel(name="是否考勤",replace={"是_true","否_false"})
	private java.lang.Boolean isCheck;
	
	@Excel(name="考勤Id" )
	private java.lang.Integer checkId;
	
	
	@Excel(name="有无留学经历",replace={"有_1","无_2"})
	private java.lang.String isStudyAbroad;
	
	@Excel(name="是否外包人员",replace={"是_A","否_B"})

	private java.lang.String isOutsource;
	/**备注*/
	@Excel(name="备注")
	private java.lang.String remarks;
	private java.lang.String abdicateDate;
	private java.lang.String abdicateRemarks;
	
	//@Excel(name="是否统招",replace={"是_A","否_B"})
	private java.lang.String fullEducation;
	
	
	
	
	private java.lang.String isDistressed;
	private java.lang.String distressedLevel;	
	private java.lang.String isSick;
	private java.lang.String sick;
	private java.lang.String isCommit;
	private java.lang.String commit;
	
	
	
	@Column(name ="is_distressed",nullable=true,length=2)
	public java.lang.String getIsDistressed() {
		return isDistressed;
	}

	public void setIsDistressed(java.lang.String isDistressed) {
		this.isDistressed = isDistressed;
	}
	@Column(name ="distressed_level",nullable=true,length=2)
	public java.lang.String getDistressedLevel() {
		return distressedLevel;
	}

	public void setDistressedLevel(java.lang.String distressedLevel) {
		this.distressedLevel = distressedLevel;
	}
	@Column(name ="is_sick",nullable=true,length=2)
	public java.lang.String getIsSick() {
		return isSick;
	}
	
	public void setIsSick(java.lang.String isSick) {
		this.isSick = isSick;
	}
	@Column(name ="sick",nullable=true,length=300)
	public java.lang.String getSick() {
		return sick;
	}

	public void setSick(java.lang.String sick) {
		this.sick = sick;
	}
	@Column(name ="is_commit",nullable=true,length=2)
	public java.lang.String getIsCommit() {
		return isCommit;
	}

	public void setIsCommit(java.lang.String isCommit) {
		this.isCommit = isCommit;
	}
	@Column(name ="commit",nullable=true,length=300)
	public java.lang.String getCommit() {
		return commit;
	}

	public void setCommit(java.lang.String commit) {
		this.commit = commit;
	}

	@Column(name ="politics_status",nullable=true,length=2)
	public java.lang.String getPoliticsStatus() {
		return politicsStatus;
	}

	public void setPoliticsStatus(java.lang.String politicsStatus) {
		this.politicsStatus = politicsStatus;
	}
	@Column(name ="full_education",nullable=true,length=2)
	public java.lang.String getFullEducation() {
		return fullEducation;
	}

	public void setFullEducation(java.lang.String fullEducation) {
		this.fullEducation = fullEducation;
	}
	@Column(name ="linkman_relative",nullable=true,length=50)
	public java.lang.String getLinkmanRelative() {
		return linkmanRelative;
	}

	public void setLinkmanRelative(java.lang.String linkmanRelative) {
		this.linkmanRelative = linkmanRelative;
	}
	@Column(name ="siling",nullable=true,length=20)
	public java.lang.String getSiling() {
		return siling;
	}

	public void setSiling(java.lang.String siling) {
		this.siling = siling;
	}

	@Column(name ="abdicate_date",nullable=true,length=10)
	public java.lang.String getAbdicateDate() {
		return abdicateDate;
	}

	public void setAbdicateDate(java.lang.String abdicateDate) {
		this.abdicateDate = abdicateDate;
	}
	@Column(name ="abdicate_remarks",nullable=true,length=500)
	public java.lang.String getAbdicateRemarks() {
		return abdicateRemarks;
	}

	public void setAbdicateRemarks(java.lang.String abdicateRemarks) {
		this.abdicateRemarks = abdicateRemarks;
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
	 *@return: java.lang.String  归属公司
	 */
	@Column(name ="COMPANY_ID",nullable=true,length=32)
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
	@Column(name ="DEPT_ID",nullable=true,length=32)
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
	 *@return: java.lang.String  性别
	 */
	@Column(name ="SEX",nullable=true,length=1)
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
	@Column(name ="JOB_NO",nullable=true,length=32)
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
	@Column(name ="NATION",nullable=true,length=2)
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
	@Column(name ="STATE",nullable=true,length=2)
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
	@Column(name ="AGE",nullable=true,length=11)
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
	@Column(name ="MAX_DEGREE",nullable=true,length=2)
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
	@Column(name ="FROM_SCHOOL",nullable=true,length=80)
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
	@Column(name ="MAJOR",nullable=true,length=50)
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
	@Column(name ="GRADUATION_DATE",nullable=true,length=10)
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
	@Column(name ="WORK_YEAR",nullable=true,length=11)
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
	@Column(name ="GO_XY_DATE",nullable=true,length=10)
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
	@Column(name ="TRIAL_START_DATA",nullable=true,length=10)
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
	@Column(name ="TRIAL_END_DATA",nullable=true,length=10)
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
	@Column(name ="POSITIVE_DATE",nullable=true,length=10)
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
	@Column(name ="CONTRACT_START_DATE",nullable=true,length=10)
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
	@Column(name ="CONTRACT_END_DATE",nullable=true,length=10)
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
	@Column(name ="LEAVE_COUNT",nullable=true,length=11)
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
	@Column(name ="OFF_WORK_COUNT",nullable=true,length=11)
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
	@Column(name ="MOBILE",nullable=true,length=15)
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
	@Column(name ="EMAIL",nullable=true,length=50)
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
	@Column(name ="BIRTHDAY",nullable=true,length=10)
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
	@Column(name ="MARRY_STATE",nullable=true,length=2)
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
	@Column(name ="CERT_NO",nullable=true,length=20)
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
	@Column(name ="REGISTER_ADDR",nullable=true,length=100)
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
	@Column(name ="REGISTER_TYPE",nullable=true,length=2)
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
	@Column(name ="ADDR",nullable=true,length=100)
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
	@Column(name ="LINKMAN_NAME",nullable=true,length=50)
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
	@Column(name ="LINKMAN_TEL",nullable=true,length=15)
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
	@Column(name ="SOCIAL_SECURITY_NO",nullable=true,length=32)
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
	@Column(name ="FUND_NO",nullable=true,length=32)
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
	@Column(name ="CV_URL",nullable=true,length=100)
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
	@Column(name ="CREATE_USER",nullable=true,length=11)
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
	@Column(name ="CREATE_TIME",nullable=true,length=10)
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
	@Column(name ="UPDATE_UER",nullable=true,length=11)
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
	@Column(name ="UPUDATE_TIME",nullable=true,length=10)
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
	@Column(name ="STAFF_SOURCE",nullable=true,length=2)
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
	@Column(name ="reference_id",nullable=true,length=11)
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
	@Column(name ="STTAFF_ID",nullable=true,length=11)
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
	@Column(name ="REMARKS",nullable=true,length=200)
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
	@Column(name ="is_check",nullable=true,length=1)
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
	@Column(name ="is_study_abroad",nullable=true,length=1)
	public java.lang.String getIsStudyAbroad() {
		return isStudyAbroad;
	}

	public void setIsStudyAbroad(java.lang.String isStudyAbroad) {
		this.isStudyAbroad = isStudyAbroad;
	}
	@Column(name ="check_id",length=11)
	public java.lang.Integer getCheckId() {
		return checkId;
	}

	public void setCheckId(java.lang.Integer checkId) {
		this.checkId = checkId;
	}
	@Column(name ="is_outsource",length=1)
	public java.lang.String getIsOutsource() {
		return isOutsource;
	}

	public void setIsOutsource(java.lang.String isOutsource) {
		this.isOutsource = isOutsource;
	}
	
	
	
}
