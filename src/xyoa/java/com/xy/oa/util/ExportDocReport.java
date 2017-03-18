package com.xy.oa.util;

/**
 * 导出
 * 
 * @author Luo
 *
 */
public class ExportDocReport {
	/**
	 * 制单日期
	 */
	private String docSysDate;
	/**
	 * 凭证类别
	 */
	private String docCategory;
	/**
	 * 凭证编号
	 */
	private String docNumber;
	/**
	 * 来源类型
	 */
	private String docSourceType;
	/**
	 * 差异凭证
	 */
	private String docDiffVoucher;
	/**
	 * 附单据数
	 */
	private String docAttaNumber;
	/**
	 * 摘要
	 */
	private String docSummary;
	/**
	 * 科目编码
	 */
	private String docAccountCode;
	/**
	 * 科目
	 */
	private String docSubject;
	/**
	 * 币种
	 */
	private String docCurrency;
	/**
	 * 数量
	 */
	private String docQuantity;
	/**
	 * 单价
	 */
	private String docUnitPrice;
	/**
	 * 借贷方向
	 */
	private String docDirOfBorrow;
	/**
	 * 原币
	 */
	private Double docOrigCurrency;
	/**
	 * 本币
	 */
	private Double docLocalCurrency;
	/**
	 * 票据号
	 */
	private String docBillNumber;
	/**
	 * 票据日期
	 */
	private String docNotesDate;
	/**
	 * 业务单号
	 */
	private String docBusinessOrderNum;
	/**
	 * 业务日期
	 */
	private String docBussinessDate;
	/**
	 * 到期日
	 */
	private String docExpiryDate;
	/**
	 * 业务员编码
	 */
	private String docClerkCode;
	/**
	 * 业务员
	 */
	private String docSalesMan;
	/**
	 * 银行账号
	 */
	private String docBankAccout;
	/**
	 * 结算方式
	 */
	private String docSettlement;
	/**
	 * 往来单位编码
	 */
	private String docUnitOfComm;
	/**
	 * 往来单位
	 */
	private String docBetweenUnits;
	/**
	 * 部门编码
	 */
	private String docDeptCode;
	/**
	 * 部门
	 */
	private String docDept;
	/**
	 * 存货编码
	 */
	private String docInventoryCode;
	/**
	 * 存货
	 */
	private String docStock;
	/**
	 * 人员编码
	 */
	private String docPersonnelCode;
	/**
	 * 人员
	 */
	private String docPersonnel;
	/**
	 * 项目编码
	 */
	private String docProjectCode;
	/**
	 * 项目
	 */
	private String docProject;
	
	public ExportDocReport() {
		this.docCategory = "记账凭证";
		this.docCurrency = "人民币";
	}

	public ExportDocReport(String docSysDate, String docCategory, String docNumber, String docSourceType,
			String docDiffVoucher, String docAttaNumber, String docSummary, String docAccountCode, String docSubject,
			String docCurrency, String docQuantity, String docUnitPrice, String docDirOfBorrow, Double docOrigCurrency,
			Double docLocalCurrency, String docBillNumber, String docNotesDate, String docBusinessOrderNum,
			String docBussinessDate, String docExpiryDate, String docClerkCode, String docSalesMan,
			String docBankAccout, String docSettlement, String docUnitOfComm, String docBetweenUnits,
			String docDeptCode, String docDept, String docInventoryCode, String docStock, String docPersonnelCode,
			String docPersonnel, String docProjectCode, String docProject) {
		super();
		this.docSysDate = docSysDate;
		this.docCategory = docCategory;
		this.docNumber = docNumber;
		this.docSourceType = docSourceType;
		this.docDiffVoucher = docDiffVoucher;
		this.docAttaNumber = docAttaNumber;
		this.docSummary = docSummary;
		this.docAccountCode = docAccountCode;
		this.docSubject = docSubject;
		this.docCurrency = docCurrency;
		this.docQuantity = docQuantity;
		this.docUnitPrice = docUnitPrice;
		this.docDirOfBorrow = docDirOfBorrow;
		this.docOrigCurrency = docOrigCurrency;
		this.docLocalCurrency = docLocalCurrency;
		this.docBillNumber = docBillNumber;
		this.docNotesDate = docNotesDate;
		this.docBusinessOrderNum = docBusinessOrderNum;
		this.docBussinessDate = docBussinessDate;
		this.docExpiryDate = docExpiryDate;
		this.docClerkCode = docClerkCode;
		this.docSalesMan = docSalesMan;
		this.docBankAccout = docBankAccout;
		this.docSettlement = docSettlement;
		this.docUnitOfComm = docUnitOfComm;
		this.docBetweenUnits = docBetweenUnits;
		this.docDeptCode = docDeptCode;
		this.docDept = docDept;
		this.docInventoryCode = docInventoryCode;
		this.docStock = docStock;
		this.docPersonnelCode = docPersonnelCode;
		this.docPersonnel = docPersonnel;
		this.docProjectCode = docProjectCode;
		this.docProject = docProject;
	}

	public String getDocSysDate() {
		return docSysDate;
	}

	public void setDocSysDate(String docSysDate) {
		this.docSysDate = docSysDate;
	}

	public String getDocCategory() {
		return docCategory;
	}

	public void setDocCategory(String docCategory) {
		this.docCategory = docCategory;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public String getDocSourceType() {
		return docSourceType;
	}

	public void setDocSourceType(String docSourceType) {
		this.docSourceType = docSourceType;
	}

	public String getDocDiffVoucher() {
		return docDiffVoucher;
	}

	public void setDocDiffVoucher(String docDiffVoucher) {
		this.docDiffVoucher = docDiffVoucher;
	}

	public String getDocAttaNumber() {
		return docAttaNumber;
	}

	public void setDocAttaNumber(String docAttaNumber) {
		this.docAttaNumber = docAttaNumber;
	}

	public String getDocSummary() {
		return docSummary;
	}

	public void setDocSummary(String docSummary) {
		this.docSummary = docSummary;
	}

	public String getDocAccountCode() {
		return docAccountCode;
	}

	public void setDocAccountCode(String docAccountCode) {
		this.docAccountCode = docAccountCode;
	}

	public String getDocSubject() {
		return docSubject;
	}

	public void setDocSubject(String docSubject) {
		this.docSubject = docSubject;
	}

	public String getDocCurrency() {
		return docCurrency;
	}

	public void setDocCurrency(String docCurrency) {
		this.docCurrency = docCurrency;
	}

	public String getDocQuantity() {
		return docQuantity;
	}

	public void setDocQuantity(String docQuantity) {
		this.docQuantity = docQuantity;
	}

	public String getDocUnitPrice() {
		return docUnitPrice;
	}

	public void setDocUnitPrice(String docUnitPrice) {
		this.docUnitPrice = docUnitPrice;
	}

	public String getDocDirOfBorrow() {
		return docDirOfBorrow;
	}

	public void setDocDirOfBorrow(String docDirOfBorrow) {
		this.docDirOfBorrow = docDirOfBorrow;
	}

	public Double getDocOrigCurrency() {
		return docOrigCurrency;
	}

	public void setDocOrigCurrency(Double docOrigCurrency) {
		this.docOrigCurrency = docOrigCurrency;
	}

	public Double getDocLocalCurrency() {
		return docLocalCurrency;
	}

	public void setDocLocalCurrency(Double docLocalCurrency) {
		this.docLocalCurrency = docLocalCurrency;
	}

	public String getDocBillNumber() {
		return docBillNumber;
	}

	public void setDocBillNumber(String docBillNumber) {
		this.docBillNumber = docBillNumber;
	}

	public String getDocNotesDate() {
		return docNotesDate;
	}

	public void setDocNotesDate(String docNotesDate) {
		this.docNotesDate = docNotesDate;
	}

	public String getDocBusinessOrderNum() {
		return docBusinessOrderNum;
	}

	public void setDocBusinessOrderNum(String docBusinessOrderNum) {
		this.docBusinessOrderNum = docBusinessOrderNum;
	}

	public String getDocBussinessDate() {
		return docBussinessDate;
	}

	public void setDocBussinessDate(String docBussinessDate) {
		this.docBussinessDate = docBussinessDate;
	}

	public String getDocExpiryDate() {
		return docExpiryDate;
	}

	public void setDocExpiryDate(String docExpiryDate) {
		this.docExpiryDate = docExpiryDate;
	}

	public String getDocClerkCode() {
		return docClerkCode;
	}

	public void setDocClerkCode(String docClerkCode) {
		this.docClerkCode = docClerkCode;
	}

	public String getDocSalesMan() {
		return docSalesMan;
	}

	public void setDocSalesMan(String docSalesMan) {
		this.docSalesMan = docSalesMan;
	}

	public String getDocBankAccout() {
		return docBankAccout;
	}

	public void setDocBankAccout(String docBankAccout) {
		this.docBankAccout = docBankAccout;
	}

	public String getDocSettlement() {
		return docSettlement;
	}

	public void setDocSettlement(String docSettlement) {
		this.docSettlement = docSettlement;
	}

	public String getDocUnitOfComm() {
		return docUnitOfComm;
	}

	public void setDocUnitOfComm(String docUnitOfComm) {
		this.docUnitOfComm = docUnitOfComm;
	}

	public String getDocBetweenUnits() {
		return docBetweenUnits;
	}

	public void setDocBetweenUnits(String docBetweenUnits) {
		this.docBetweenUnits = docBetweenUnits;
	}

	public String getDocDeptCode() {
		return docDeptCode;
	}

	public void setDocDeptCode(String docDeptCode) {
		this.docDeptCode = docDeptCode;
	}

	public String getDocDept() {
		return docDept;
	}

	public void setDocDept(String docDept) {
		this.docDept = docDept;
	}

	public String getDocInventoryCode() {
		return docInventoryCode;
	}

	public void setDocInventoryCode(String docInventoryCode) {
		this.docInventoryCode = docInventoryCode;
	}

	public String getDocStock() {
		return docStock;
	}

	public void setDocStock(String docStock) {
		this.docStock = docStock;
	}

	public String getDocPersonnelCode() {
		return docPersonnelCode;
	}

	public void setDocPersonnelCode(String docPersonnelCode) {
		this.docPersonnelCode = docPersonnelCode;
	}

	public String getDocPersonnel() {
		return docPersonnel;
	}

	public void setDocPersonnel(String docPersonnel) {
		this.docPersonnel = docPersonnel;
	}

	public String getDocProjectCode() {
		return docProjectCode;
	}

	public void setDocProjectCode(String docProjectCode) {
		this.docProjectCode = docProjectCode;
	}

	public String getDocProject() {
		return docProject;
	}

	public void setDocProject(String docProject) {
		this.docProject = docProject;
	}

}
