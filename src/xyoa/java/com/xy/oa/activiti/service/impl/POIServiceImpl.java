package com.xy.oa.activiti.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.stereotype.Service;

import com.xy.oa.activiti.service.POIServiceI;
import com.xy.oa.util.ExcelHandle;
import com.xy.oa.util.ExportDocReport;

@Service
public class POIServiceImpl extends CommonServiceImpl implements POIServiceI {

	// public int docNumberSC = 0;
	// public int docNumberSZ = 0;
	// public int docNumberBJ = 0;
	// public int docNumberSH = 0;

	public Map<String, Object> getOneSubjectforJdbc(String feeTypeName, String deptName) {
		String sql = "SELECT subject_no, subject_name " + "FROM s_xy_yy_subject " + "WHERE subject_no = ("
				+ "SELECT sft.yy_subject_no " + "FROM s_xy_xy_fee_type sft "
				+ "WHERE sft.fee_type_name = ? AND sft.dept_name = ?)";
		Map<String, Object> map = this.findOneForJdbc(sql, feeTypeName, deptName);
		return map;
	}

	public <T> T getOneDeptforSQL(String deptName) {
		String sql = "SELECT dept_no FROM s_xy_yy_dept WHERE dept_name = ?";
		return this.singleResultSQL(sql, deptName);
	}

	public <T> T getOneProjectforSQL(String projectName) {
		String sql = "SELECT project_no FROM s_xy_yy_project WHERE project_name = ?";
		return this.singleResultSQL(sql, projectName);
	}

	public String getDocNumber(int docNumber) {
		String str = String.format("%05d", docNumber);
		return str;
	}

	/**
	 * 截取公司名称
	 * 
	 * @param str
	 */
	public String getDepAddressName(String str) {
		String orgName = null;
		if (StringUtil.isNotEmpty(str)) {
			if (str.indexOf("(") != -1 && str.indexOf(")") != -1) {
				orgName = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
			} else if (str.indexOf("(") != -1 && str.indexOf("）") != -1) {
				orgName = str.substring(str.indexOf("(") + 1, str.indexOf("）"));
			} else if (str.indexOf("（") != -1 && str.indexOf(")") != -1) {
				orgName = str.substring(str.indexOf("（") + 1, str.indexOf(")"));
			} else if (str.indexOf("（") != -1 && str.indexOf("）") != -1) {
				orgName = str.substring(str.indexOf("（") + 1, str.indexOf("）"));
			}
		}
		return orgName;
	}

	/**
	 * 截取部门名称
	 * 
	 * @param str
	 * @return
	 */
	public String getDepName(String str) {
		String deptName = null;
		if (StringUtil.isNotEmpty(str)) {
			if (str.indexOf("(") != -1) {
				deptName = str.substring(0, str.indexOf("("));
			} else if (str.indexOf("（") != -1) {
				deptName = str.substring(0, str.indexOf("（"));
			}
		}
		return deptName;
	}

	@Override
	public void getImportExcel(InputStream inputStream, File[] srcFile, File zipfile, String srcfileName) throws Exception {
		int docNumberSC = 0;
		int docNumberSZ = 0;
		int docNumberBJ = 0;
		int docNumberSH = 0;
		List<ExportDocReport> reports1 = new ArrayList<ExportDocReport>();
		List<ExportDocReport> reports2 = new ArrayList<ExportDocReport>();
		List<ExportDocReport> reports3 = new ArrayList<ExportDocReport>();
		List<ExportDocReport> reports4 = new ArrayList<ExportDocReport>();
		Workbook wb = null;
		if (srcfileName.endsWith(".xlsx")) {
			wb = new XSSFWorkbook(inputStream);
		} else if (srcfileName.endsWith(".xls")) {
			wb = new HSSFWorkbook(inputStream);
		}
		Sheet sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		for (int i = 1; i <= rowNum; i++) {
			Row row = sheet.getRow(i);
			String depAddressName = getDepAddressName(getCellFormatValue(row.getCell(3)).toString());
			if ("四川".equals(depAddressName)) {
				Object obj = getCellFormatValue(row.getCell(0));
				if (StringUtil.isNotEmpty(obj)) {
					String textVal = obj.toString();
					if (textVal.startsWith("DC") || textVal.startsWith("TC")) {// 日常费用报销、差旅费用报销
						docNumberSC++;
						String docNumber = getDocNumber(docNumberSC);// 凭证编号
						i++;
						String docType = getCellFormatValue(row.getCell(1)).toString();// 单据类型
						String deptName = getDepName(getCellFormatValue(row.getCell(3)).toString());// 部门名称
						Object docDeptCode = this.getOneDeptforSQL(deptName);// 部门编码
						if (!StringUtil.isNotEmpty(docDeptCode)) {
							throw new BusinessException("单据号：" + textVal + "--》部门：" + deptName + " 不存在！");
						}
						String docSysDate = getCellFormatValue(row.getCell(5)).toString();// 制单日期
						Double docMoney = Double.valueOf(getCellFormatValue(row.getCell(6)).toString());// 报销金额
						String docGetMoneyPerson = getCellFormatValue(row.getCell(14)).toString();// 收款人

						Row rowChild = sheet.getRow(i + 1);
						boolean hasNextChild = rowChild.getFirstCellNum() == 2 ? true : false;
						ExportDocReport report = null;
						while (hasNextChild) {
							report = new ExportDocReport();
							// 制单日期
							report.setDocSysDate(docSysDate);
							// 凭证编号
							report.setDocNumber(docNumber);
							// 费用类型
							String feeTypeName = getCellFormatValue(rowChild.getCell(5)).toString();
							// 摘要
							report.setDocSummary(docGetMoneyPerson + feeTypeName);
							Map<String, Object> map = this.getOneSubjectforJdbc(feeTypeName, deptName);
							if(map == null ){
								throw new BusinessException("单据号：" + textVal + "--》费用类型："+feeTypeName+" 与部门："+deptName+" 对应的科目不存在！");
							}
							// 科目编码
							report.setDocAccountCode(map.get("subject_no").toString());
							// 科目
							report.setDocSubject(map.get("subject_name").toString());
							// 借贷方向
							report.setDocDirOfBorrow("借方");
							// 原币
							Double docOrigCurrency = Double.valueOf(getCellFormatValue(rowChild.getCell(6)).toString());
							report.setDocOrigCurrency(docOrigCurrency);
							// 本币
							report.setDocLocalCurrency(docOrigCurrency);
							// 部门编码
							report.setDocDeptCode(docDeptCode.toString());
							// 部门
							report.setDocDept(deptName);
							// 项目编码
							String projectName = getCellFormatValue(rowChild.getCell(4)).toString();
							Object docProjectCode = this.getOneProjectforSQL(projectName);
							if (!StringUtil.isNotEmpty(docProjectCode)) {
								throw new BusinessException("单据号："+textVal+"--》项目名称："+projectName+" 不存在！");
							}
							report.setDocProjectCode(docProjectCode.toString());
							// 项目
							report.setDocProject(projectName);
							reports1.add(report);
							i++;
							rowChild = sheet.getRow(i + 1);
							if (rowChild != null)
								hasNextChild = rowChild.getFirstCellNum() == 2 ? true : false;
							else
								hasNextChild = false;
						}
						if (report != null) {
							report = new ExportDocReport();
							// 制单日期
							report.setDocSysDate(docSysDate);
							// 凭证编号
							report.setDocNumber(docNumber);
							// 摘要
							report.setDocSummary(docGetMoneyPerson + docType);
							// 科目编码
							report.setDocAccountCode("100202");
							// 科目
							report.setDocSubject("民生银行成都高新支行");
							// 借贷方向
							report.setDocDirOfBorrow("贷方");
							// 原币
							report.setDocOrigCurrency(docMoney);
							// 本币
							report.setDocLocalCurrency(docMoney);
							// // 部门编码
							// report.setDocDeptCode(docDeptCode.toString());
							// // 部门
							// report.setDocDept(deptName);
							reports1.add(report);
						}
					} else if (textVal.startsWith("AP")) {// 供应商预付款

					} else if (textVal.startsWith("AA")) {// 借款单

					} else if (textVal.startsWith("PA")) {// 申请单

					} else if (textVal.startsWith("VA")) {// 供应商支付

					}
				}
			} else if ("深圳".equals(depAddressName)) {
				Object obj = getCellFormatValue(row.getCell(0));
				if (StringUtil.isNotEmpty(obj)) {
					String textVal = obj.toString();
					if (textVal.startsWith("DC") || textVal.startsWith("TC")) {// 日常费用报销、差旅费用报销
						docNumberSZ++;
						String docNumber = getDocNumber(docNumberSZ);// 凭证编号
						i++;
						String docType = getCellFormatValue(row.getCell(1)).toString();// 单据类型
						String deptName = getDepName(getCellFormatValue(row.getCell(3)).toString());// 部门名称
						Object docDeptCode = this.getOneDeptforSQL(deptName);// 部门编码
						if (!StringUtil.isNotEmpty(docDeptCode)) {
							throw new BusinessException("单据号：" + textVal + "--》部门：" + deptName + " 不存在！");
						}
						String docSysDate = getCellFormatValue(row.getCell(5)).toString();// 制单日期
						Double docMoney = Double.valueOf(getCellFormatValue(row.getCell(6)).toString());// 报销金额
						String docGetMoneyPerson = getCellFormatValue(row.getCell(14)).toString();// 收款人

						Row rowChild = sheet.getRow(i + 1);
						boolean hasNextChild = rowChild.getFirstCellNum() == 2 ? true : false;
						ExportDocReport report = null;
						while (hasNextChild) {
							report = new ExportDocReport();
							// 制单日期
							report.setDocSysDate(docSysDate);
							// 凭证编号
							report.setDocNumber(docNumber);
							// 费用类型
							String feeTypeName = getCellFormatValue(rowChild.getCell(5)).toString();
							// 摘要
							report.setDocSummary(docGetMoneyPerson + feeTypeName);
							Map<String, Object> map = this.getOneSubjectforJdbc(feeTypeName, deptName);
							if(map == null ){
								throw new BusinessException("单据号：" + textVal + "--》费用类型："+feeTypeName+" 与部门："+deptName+" 对应的科目不存在！");
							}
							// 科目编码
							report.setDocAccountCode(map.get("subject_no").toString());
							// 科目
							report.setDocSubject(map.get("subject_name").toString());
							// 借贷方向
							report.setDocDirOfBorrow("借方");
							// 原币
							Double docOrigCurrency = Double.valueOf(getCellFormatValue(rowChild.getCell(6)).toString());
							report.setDocOrigCurrency(docOrigCurrency);
							// 本币
							report.setDocLocalCurrency(docOrigCurrency);
							// 部门编码
							report.setDocDeptCode(docDeptCode.toString());
							// 部门
							report.setDocDept(deptName);
							// 项目编码
							String projectName = getCellFormatValue(rowChild.getCell(4)).toString();
							Object docProjectCode = this.getOneProjectforSQL(projectName);
							if (!StringUtil.isNotEmpty(docProjectCode)) {
								throw new BusinessException("单据号："+textVal+"--》项目名称："+projectName+" 不存在！");
							}
							report.setDocProjectCode(docProjectCode.toString());
							// 项目
							report.setDocProject(projectName);
							reports2.add(report);
							i++;
							rowChild = sheet.getRow(i + 1);
							if (rowChild != null)
								hasNextChild = rowChild.getFirstCellNum() == 2 ? true : false;
							else
								hasNextChild = false;
						}
						if (report != null) {
							report = new ExportDocReport();
							// 制单日期
							report.setDocSysDate(docSysDate);
							// 凭证编号
							report.setDocNumber(docNumber);
							// 摘要
							report.setDocSummary(docGetMoneyPerson + docType);
							// 科目编码
							report.setDocAccountCode("100201");
							// 科目
							report.setDocSubject("平安银行深圳湾支行");
							// 借贷方向
							report.setDocDirOfBorrow("贷方");
							// 原币
							report.setDocOrigCurrency(docMoney);
							// 本币
							report.setDocLocalCurrency(docMoney);
							// // 部门编码
							// report.setDocDeptCode(docDeptCode.toString());
							// // 部门
							// report.setDocDept(deptName);
							reports2.add(report);
						}
					} else if (textVal.startsWith("AP")) {// 供应商预付款

					} else if (textVal.startsWith("AA")) {// 借款单

					} else if (textVal.startsWith("PA")) {// 申请单

					} else if (textVal.startsWith("VA")) {// 供应商支付

					}
				}
			} else if ("北京".equals(depAddressName)) {
				Object obj = getCellFormatValue(row.getCell(0));
				if (StringUtil.isNotEmpty(obj)) {
					String textVal = obj.toString();
					if (textVal.startsWith("DC") || textVal.startsWith("TC")) {// 日常费用报销、差旅费用报销
						docNumberBJ++;
						String docNumber = getDocNumber(docNumberBJ);// 凭证编号
						i++;
						String docType = getCellFormatValue(row.getCell(1)).toString();// 单据类型
						String deptName = getDepName(getCellFormatValue(row.getCell(3)).toString());// 部门名称
						Object docDeptCode = this.getOneDeptforSQL(deptName);// 部门编码
						if (!StringUtil.isNotEmpty(docDeptCode)) {
							throw new BusinessException("单据号：" + textVal + "--》部门：" + deptName + " 不存在！");
						}
						String docSysDate = getCellFormatValue(row.getCell(5)).toString();// 制单日期
						Double docMoney = Double.valueOf(getCellFormatValue(row.getCell(6)).toString());// 报销金额
						String docGetMoneyPerson = getCellFormatValue(row.getCell(14)).toString();// 收款人

						Row rowChild = sheet.getRow(i + 1);
						boolean hasNextChild = rowChild.getFirstCellNum() == 2 ? true : false;
						ExportDocReport report = null;
						while (hasNextChild) {
							report = new ExportDocReport();
							// 制单日期
							report.setDocSysDate(docSysDate);
							// 凭证编号
							report.setDocNumber(docNumber);
							// 费用类型
							String feeTypeName = getCellFormatValue(rowChild.getCell(5)).toString();
							// 摘要
							report.setDocSummary(docGetMoneyPerson + feeTypeName);
							Map<String, Object> map = this.getOneSubjectforJdbc(feeTypeName, deptName);
							if(map == null ){
								throw new BusinessException("单据号：" + textVal + "--》费用类型："+feeTypeName+" 与部门："+deptName+" 对应的科目不存在！");
							}
							// 科目编码
							report.setDocAccountCode(map.get("subject_no").toString());
							// 科目
							report.setDocSubject(map.get("subject_name").toString());
							// 借贷方向
							report.setDocDirOfBorrow("借方");
							// 原币
							Double docOrigCurrency = Double.valueOf(getCellFormatValue(rowChild.getCell(6)).toString());
							report.setDocOrigCurrency(docOrigCurrency);
							// 本币
							report.setDocLocalCurrency(docOrigCurrency);
							// 部门编码
							report.setDocDeptCode(docDeptCode.toString());
							// 部门
							report.setDocDept(deptName);
							// 项目编码
							String projectName = getCellFormatValue(rowChild.getCell(4)).toString();
							Object docProjectCode = this.getOneProjectforSQL(projectName);
							if (!StringUtil.isNotEmpty(docProjectCode)) {
								throw new BusinessException("单据号："+textVal+"--》项目名称："+projectName+" 不存在！");
							}
							report.setDocProjectCode(docProjectCode.toString());
							// 项目
							report.setDocProject(projectName);
							reports3.add(report);
							i++;
							rowChild = sheet.getRow(i + 1);
							if (rowChild != null)
								hasNextChild = rowChild.getFirstCellNum() == 2 ? true : false;
							else
								hasNextChild = false;
						}
						if (report != null) {
							report = new ExportDocReport();
							// 制单日期
							report.setDocSysDate(docSysDate);
							// 凭证编号
							report.setDocNumber(docNumber);
							// 摘要
							report.setDocSummary(docGetMoneyPerson + docType);
							// 科目编码
							report.setDocAccountCode("100204");
							// 科目
							report.setDocSubject("九江农村商业银行");
							// 借贷方向
							report.setDocDirOfBorrow("贷方");
							// 原币
							report.setDocOrigCurrency(docMoney);
							// 本币
							report.setDocLocalCurrency(docMoney);
							// // 部门编码
							// report.setDocDeptCode(docDeptCode.toString());
							// // 部门
							// report.setDocDept(deptName);
							reports3.add(report);
						}
					} else if (textVal.startsWith("AP")) {// 供应商预付款

					} else if (textVal.startsWith("AA")) {// 借款单

					} else if (textVal.startsWith("PA")) {// 申请单

					} else if (textVal.startsWith("VA")) {// 供应商支付

					}
				}
			} else if ("上海".equals(depAddressName)) {
				Object obj = getCellFormatValue(row.getCell(0));
				if (StringUtil.isNotEmpty(obj)) {
					String textVal = obj.toString();
					if (textVal.startsWith("DC") || textVal.startsWith("TC")) {// 日常费用报销、差旅费用报销
						docNumberSH++;
						String docNumber = getDocNumber(docNumberSH);// 凭证编号
						i++;
						String docType = getCellFormatValue(row.getCell(1)).toString();// 单据类型
						String deptName = getDepName(getCellFormatValue(row.getCell(3)).toString());// 部门名称
						Object docDeptCode = this.getOneDeptforSQL(deptName);// 部门编码
						if (!StringUtil.isNotEmpty(docDeptCode)) {
							throw new BusinessException("单据号：" + textVal + "--》部门：" + deptName + " 不存在！");
						}
						String docSysDate = getCellFormatValue(row.getCell(5)).toString();// 制单日期
						Double docMoney = Double.valueOf(getCellFormatValue(row.getCell(6)).toString());// 报销金额
						String docGetMoneyPerson = getCellFormatValue(row.getCell(14)).toString();// 收款人

						Row rowChild = sheet.getRow(i + 1);
						boolean hasNextChild = rowChild.getFirstCellNum() == 2 ? true : false;
						ExportDocReport report = null;
						while (hasNextChild) {
							report = new ExportDocReport();
							// 制单日期
							report.setDocSysDate(docSysDate);
							// 凭证编号
							report.setDocNumber(docNumber);
							// 费用类型
							String feeTypeName = getCellFormatValue(rowChild.getCell(5)).toString();
							// 摘要
							report.setDocSummary(docGetMoneyPerson + feeTypeName);
							Map<String, Object> map = this.getOneSubjectforJdbc(feeTypeName, deptName);
							if(map == null ){
								throw new BusinessException("单据号：" + textVal + "--》费用类型："+feeTypeName+" 与部门："+deptName+" 对应的科目不存在！");
							}
							// 科目编码
							report.setDocAccountCode(map.get("subject_no").toString());
							// 科目
							report.setDocSubject(map.get("subject_name").toString());
							// 借贷方向
							report.setDocDirOfBorrow("借方");
							// 原币
							Double docOrigCurrency = Double.valueOf(getCellFormatValue(rowChild.getCell(6)).toString());
							report.setDocOrigCurrency(docOrigCurrency);
							// 本币
							report.setDocLocalCurrency(docOrigCurrency);
							// 部门编码
							report.setDocDeptCode(docDeptCode.toString());
							// 部门
							report.setDocDept(deptName);
							// 项目编码
							String projectName = getCellFormatValue(rowChild.getCell(4)).toString();
							Object docProjectCode = this.getOneProjectforSQL(projectName);
							if (!StringUtil.isNotEmpty(docProjectCode)) {
								throw new BusinessException("单据号："+textVal+"--》项目名称："+projectName+" 不存在！");
							}
							report.setDocProjectCode(docProjectCode.toString());
							// 项目
							report.setDocProject(projectName);
							reports4.add(report);
							i++;
							rowChild = sheet.getRow(i + 1);
							if (rowChild != null)
								hasNextChild = rowChild.getFirstCellNum() == 2 ? true : false;
							else
								hasNextChild = false;
						}
						if (report != null) {
							report = new ExportDocReport();
							// 制单日期
							report.setDocSysDate(docSysDate);
							// 凭证编号
							report.setDocNumber(docNumber);
							// 摘要
							report.setDocSummary(docGetMoneyPerson + docType);
							// 科目编码
							report.setDocAccountCode("100204");
							// 科目
							report.setDocSubject("九江农商银行7745");
							// 借贷方向
							report.setDocDirOfBorrow("贷方");
							// 原币
							report.setDocOrigCurrency(docMoney);
							// 本币
							report.setDocLocalCurrency(docMoney);
							// // 部门编码
							// report.setDocDeptCode(docDeptCode.toString());
							// // 部门
							// report.setDocDept(deptName);
							reports4.add(report);
						}
					} else if (textVal.startsWith("AP")) {// 供应商预付款

					} else if (textVal.startsWith("AA")) {// 借款单

					} else if (textVal.startsWith("PA")) {// 申请单

					} else if (textVal.startsWith("VA")) {// 供应商支付

					}
				}
			}
		}

		OutputStream[] out = new OutputStream[4];
		for (int i = 0; i < srcFile.length; i++) {
			out[i] = new FileOutputStream(srcFile[i]);
		}
		exportExcel(reports1, out[0]);
		exportExcel(reports2, out[1]);
		exportExcel(reports3, out[2]);
		exportExcel(reports4, out[3]);
		// 打包成zip文件
		zipFiles(srcFile, zipfile);
	}

	/**
	 * 获取Excel列的数据
	 * 
	 * @param cell
	 * @return
	 */
	public Object getCellFormatValue(Cell cell) {
		Object cellvalue = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:// 数值类型
				cellvalue = cell.getNumericCellValue();
				break;
			case Cell.CELL_TYPE_STRING:// 字符串类型
				cellvalue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_FORMULA:// 公式
				if (DateUtil.isCellDateFormatted(cell)) {
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);
				}
				break;
			case Cell.CELL_TYPE_BLANK:// 空值
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				break;
			case Cell.CELL_TYPE_ERROR:
				break;
			default:
				cellvalue = "Unknown Cell Type：" + cell.getCellType();
			}
		}
		return cellvalue;
	}

	/**
	 * 导出Excel文件
	 * 
	 * @param reports
	 * @param out
	 * @throws Exception
	 */
	public void exportExcel(List<ExportDocReport> reports, OutputStream out) throws Exception {
		String tempFilePath = ExcelHandle.class.getResource("yy_xls_templet.xls").getPath();
		List<String> dataListCell = new ArrayList<String>();
		dataListCell.add("MadeDate");
		dataListCell.add("DocType");
		dataListCell.add("DocCode");
		dataListCell.add("Summary");

		dataListCell.add("accountCode");
		dataListCell.add("accountName");
		dataListCell.add("currency");
		dataListCell.add("Direction");

		dataListCell.add("OrigAmount");
		dataListCell.add("Amount");
		dataListCell.add("AuxAccDepartmentCode");
		dataListCell.add("AuxAccDepartmentName");

		dataListCell.add("AuxAccProjectCode");
		dataListCell.add("AuxAccProjectName");
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for (ExportDocReport report : reports) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("MadeDate", DateUtils.parseDate(report.getDocSysDate(), "yyyy-MM-dd"));
			map.put("DocType", report.getDocCategory());
			map.put("DocCode", report.getDocNumber());
			map.put("Summary", report.getDocSummary());

			map.put("accountCode", report.getDocAccountCode());
			map.put("accountName", report.getDocSubject());
			map.put("currency", report.getDocCurrency());
			map.put("Direction", report.getDocDirOfBorrow());

			map.put("OrigAmount", report.getDocOrigCurrency());
			map.put("Amount", report.getDocLocalCurrency());
			map.put("AuxAccDepartmentCode", report.getDocDeptCode());
			map.put("AuxAccDepartmentName", report.getDocDept());

			map.put("AuxAccProjectCode", report.getDocProjectCode());
			map.put("AuxAccProjectName", report.getDocProject());

			dataList.add(map);
		}

		ExcelHandle handle = new ExcelHandle();
		handle.writeListData(tempFilePath, dataListCell, dataList, 0);

		// 写到输出流并关闭资源
		handle.writeAndClose(tempFilePath, out);
		out.flush();
		out.close();
		handle.readClose(tempFilePath);
	}

	/**
	 * 打包成zip文件
	 * 
	 * @param srcFile
	 * @param zipfile
	 */
	public void zipFiles(File[] srcFile, File zipfile) {
		try {
			byte[] buf = new byte[1024 * 4];
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
			for (File srcf : srcFile) {
				FileInputStream in = new FileInputStream(srcf);
				out.putNextEntry(new ZipEntry(srcf.getName()));
				int len = 0;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载zip文件
	 * 
	 * @param out
	 * @param zipfile
	 */
	@Override
	public void downFile(OutputStream out, File zipfile) {
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zipfile));
			BufferedOutputStream bos = new BufferedOutputStream(out);
			int bytesRead = 0;
			byte[] buf = new byte[1024 * 8];
			while ((bytesRead = bis.read(buf, 0, buf.length)) != -1) {
				bos.write(buf, 0, bytesRead);
				bos.flush();
			}
			bis.close();
			out.close();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 递归删除目录下的所有文件
	 */
	@Override
	public void deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] childDirs = dir.list();
			for (String childDir : childDirs) {
				deleteDir(new File(dir, childDir));
			}
		} else {
			dir.delete();
		}
	}

}
