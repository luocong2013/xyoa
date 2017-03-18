package com.xy.oa.util;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jeecgframework.core.util.DateUtils;

/**
 * 利用POI动态导出Excel文档
 * @author Luo
 *
 * @param <T>
 */
public class PoiExcelExport<T> {
	
	public void exportExcel(String[] headers, Collection<T> dataset, OutputStream out) {
		exportExcel("考勤异常统计表", headers, dataset, out, "yyyy-MM-dd", "异 常 统 计 表");
	}
	
	public void exportExcel(String title, String[] headers, Collection<T> dataset, OutputStream out, String pattern, String topTitle) {
		//声明一个工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		//生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		//设置表格默认列宽为12个字节
		sheet.setDefaultColumnWidth(12);
		//声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		
		//产生表格标题行
		HSSFRow row = sheet.createRow(0);
		row.setHeight((short)1000);
		HSSFCell cellTop = row.createCell(0);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length -1));
		cellTop.setCellStyle(getColumnTopStyle(workbook));
		cellTop.setCellValue(new HSSFRichTextString(topTitle));
		
		//统计日期行
		row = sheet.createRow(1);
		row.setHeight((short)800);
		HSSFCell cellD = row.createCell(0);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, headers.length -1));
		cellD.setCellStyle(getColumnDateStyle(workbook));
		cellD.setCellValue(new HSSFRichTextString("统计日期：" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss")));
		
		//产生表格标题行
		row = sheet.createRow(2);
		row.setHeight((short)1000);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(getColumnStyle(workbook, true));
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		//设置列宽
		sheet.setColumnWidth(6, 30*256);
		sheet.setColumnWidth(7, 30*256);
		sheet.setColumnWidth(11, 30*256);
		
		/**
		 * 修改BUG  
		 * 导出数据时超过4000次 workbook.createCellStyle(); 报错问题
		 * 将其放在循环体外
		 */
		HSSFCellStyle cellStyle = getColumnStyle(workbook, false);
		
		//遍历集合数据，产生数据行
		Iterator<T> iterator = dataset.iterator();
		int index = 2;
		while (iterator.hasNext()) {
			index++;
			row = sheet.createRow(index);
			row.setHeight((short)600);
			T t = (T) iterator.next();
			//利用反射，根据JavaBean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(cellStyle);
				Field field = fields[i];
				String fieldName = field.getName();
				String getMethodName = "get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
				try {
					Class<?> tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
					Object value = getMethod.invoke(t, new Object[]{});
					if (value != null) {
						//判断值的类型后进行强制类型转换
						String textValue = null;
						if (value instanceof Boolean) {
							boolean bValue = (Boolean)value;
							textValue = "男";
							if (!bValue) {
								textValue = "女";
							}
						} else if (value instanceof Date) {
							Date date = (Date)value;
							SimpleDateFormat sdf = new SimpleDateFormat(pattern);
							textValue = sdf.format(date);
						} else if (value instanceof byte[]) {
							//有图片时，设置行高为60px
							row.setHeightInPoints(60);
							//设置图片所在列的宽度为80px，注意这里单位的一个换算
							sheet.setColumnWidth(i, (short)(35.7 * 80));
							byte[] bsValue = (byte[])value;
							HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short)6, index, (short)6, index);
							anchor.setAnchorType(2);
							patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
						} else {
							//其他数据类型都当做字符串简单处理
							textValue = value.toString();
						}
						//如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
						if (textValue != null) {
							Pattern p = Pattern.compile("^//d+(//.//d+)?$");
							Matcher matcher = p.matcher(textValue);
							if (matcher.matches()) {
								cell.setCellValue(Double.parseDouble(textValue));
							} else {
								HSSFRichTextString richString = new HSSFRichTextString(textValue);
								HSSFFont font3 = workbook.createFont();
								richString.applyFont(font3);
								cell.setCellValue(richString);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			workbook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 列头单元格样式
	 * @param workbook
	 * @return
	 */
	public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
		//生成一个字体
		HSSFFont font = workbook.createFont();
		//设置字体大小
		font.setFontHeightInPoints((short)24);
		//字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		//设置字体名字
		font.setFontName("新宋体");
		//生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
//		//设置边框以及边框颜色
//		setBorderAndBorderColor(style);
		//在样式用应用设置的字体
		style.setFont(font);
		//设置自动换行
		style.setWrapText(true);
		//设置水平对齐的样式为居中对齐
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		//设置垂直对齐的样式为居中对齐
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return style;
	}
	
	/**
	 * 标题单元格样式
	 * @param workbook
	 * @return
	 */
	public HSSFCellStyle getColumnStyle(HSSFWorkbook workbook, boolean isHeaders) {
		//生成一个字体
		HSSFFont font = workbook.createFont();
		//设置字体颜色
		font.setColor(HSSFColor.BLACK.index);
		if (isHeaders) {
			//设置字体大小
			font.setFontHeightInPoints((short)10);
			//设置字体名字
			font.setFontName("新宋体");
		} else {
			//设置字体大小
			font.setFontHeightInPoints((short)10);
			//设置字体名字
			font.setFontName("宋体");
		}
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		//生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
//		//设置边框以及边框颜色
//		setBorderAndBorderColor(style);
		//把字体应用到当前的样式
		style.setFont(font);
		//设置自动换行
		style.setWrapText(true);
		if (isHeaders) {
			//左右居中
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		}
		//上下居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return style;
	}
	
	public HSSFCellStyle getColumnDateStyle(HSSFWorkbook workbook) {
		//生成一个字体
		HSSFFont font = workbook.createFont();
		//设置字体颜色
		font.setColor(HSSFColor.BLACK.index);
		//设置字体大小
		font.setFontHeightInPoints((short)10);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		//设置字体名字
		font.setFontName("宋体");
		//生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
//		//设置边框以及边框颜色
//		setBorderAndBorderColor(style);
		//把字体应用到当前的样式
		style.setFont(font);
		//左右 左对齐
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		//上下 居下
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return style;
	}
	/**
	 * 设置边框以及边框颜色
	 * @param style
	 */
	public void setBorderAndBorderColor(HSSFCellStyle style) {
		//设置底边框
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		//设置底边框颜色
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		//设置左边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		//设置左边框颜色
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		//设置右边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		//设置右边框颜色
		style.setRightBorderColor(HSSFColor.BLACK.index);
		//设置顶边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		//设置顶边框颜色
		style.setTopBorderColor(HSSFColor.BLACK.index);
	}
}