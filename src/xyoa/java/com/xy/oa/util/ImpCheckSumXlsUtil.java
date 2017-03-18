package com.xy.oa.util;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.util.DateUtils;

public class ImpCheckSumXlsUtil {
	public static void impCheckSumXls( OutputStream out ,List<Map<String,Object>> checkSumData,String startDate,String endDate)
	{
		try{
			HSSFWorkbook	wb	= new HSSFWorkbook();           /* 建立新HSSFWorkbook对象 */
			HSSFSheet	sheet	= wb.createSheet( "考勤汇总表" );    /* 建立新的sheet对象 */

			/* 标题 */
			HSSFRow headrow = sheet.createRow( 0 );
			sheet.addMergedRegion( new CellRangeAddress( 0, 1, 0, 28 ) );
			HSSFCell csCell = headrow.createCell( 0 );
			csCell.setCellValue( "考 勤 汇总 表" );
			HSSFFont headfont = wb.createFont();                    /* 标题样式 */
			headfont.setFontName( "宋体" );
			headfont.setFontHeightInPoints( (short) 20 );
			headfont.setBoldweight( HSSFFont.BOLDWEIGHT_BOLD );
			HSSFCellStyle headStyle1 = wb.createCellStyle();
			headStyle1.setFont( headfont );
			headStyle1.setAlignment( HSSFCellStyle.ALIGN_CENTER );
			headStyle1.setVerticalAlignment( HSSFCellStyle.VERTICAL_CENTER );
			csCell.setCellStyle( headStyle1 );

			/* 第一行 */
			HSSFRow		row1	= sheet.createRow( 2 );
			HSSFFont	font1	= wb.createFont();
			font1.setFontName( "宋体" );
			font1.setFontHeightInPoints( (short) 11 );
			font1.setBoldweight( HSSFFont.BOLDWEIGHT_BOLD );
			HSSFCellStyle Style1 = wb.createCellStyle();
			Style1.setFont( font1 );
			Style1.setAlignment( HSSFCellStyle.ALIGN_CENTER );
			Style1.setVerticalAlignment( HSSFCellStyle.VERTICAL_CENTER );

			sheet.addMergedRegion( new CellRangeAddress( 2, 2, 0, 2 ) );
			sheet.addMergedRegion( new CellRangeAddress( 2, 2, 3, 5 ) );
			sheet.addMergedRegion( new CellRangeAddress( 2, 2, 6, 10 ) );
			sheet.addMergedRegion( new CellRangeAddress( 2, 2, 11, 12 ) );
			sheet.addMergedRegion( new CellRangeAddress( 2, 2, 13, 16 ) );
			sheet.addMergedRegion( new CellRangeAddress( 2, 2, 17, 20 ) );
			sheet.addMergedRegion( new CellRangeAddress( 2, 2, 21, 28 ) );
			csCell = row1.createCell( 3 );
			csCell.setCellValue( "考勤时间：" );
			csCell.setCellStyle( Style1 );
			csCell = row1.createCell( 13 );
			csCell.setCellValue( "制表时间：" );
			csCell.setCellStyle( Style1 );


			HSSFFont datefont = wb.createFont();
			datefont.setFontName( "仿宋" );
			datefont.setFontHeightInPoints( (short) 10 );
			HSSFCellStyle dateStyle = wb.createCellStyle();
			dateStyle.setFont( datefont );
			dateStyle.setAlignment( HSSFCellStyle.ALIGN_RIGHT );


			csCell = row1.createCell( 6 );
			SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat( "yyyy年MM月dd日" );
			csCell.setCellValue( dateSimpleDateFormat.format( new SimpleDateFormat( "yyyy-MM-dd").parse(startDate) ) + "—" + dateSimpleDateFormat.format(new SimpleDateFormat( "yyyy-MM-dd").parse(endDate)));
			csCell.setCellStyle( dateStyle );
			csCell = row1.createCell( 17 );
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy年MM月dd日  HH时mm分ss秒" );
			csCell.setCellValue( simpleDateFormat.format( new Date() ) );
			csCell.setCellStyle( dateStyle );
			/* 第二行 */
			HSSFRow		row2	= sheet.createRow( 3 );
			HSSFFont	font2	= wb.createFont();
			font2.setFontName( "宋体" );
			font2.setFontHeightInPoints( (short) 11 );
			font2.setBoldweight( HSSFFont.BOLDWEIGHT_BOLD );
			HSSFCellStyle Style2 = wb.createCellStyle();
			Style2.setFont( font2 );
			Style2.setAlignment( HSSFCellStyle.ALIGN_CENTER );
			Style2.setVerticalAlignment( HSSFCellStyle.VERTICAL_CENTER );
			Style2.setWrapText( true ); /* 设置为自动 */
			sheet.addMergedRegion( new CellRangeAddress( 3, 4, 0, 0 ) );
			sheet.addMergedRegion( new CellRangeAddress( 3, 4, 1, 1 ) );
			sheet.addMergedRegion( new CellRangeAddress( 3, 4, 2, 2 ) );
			csCell = row2.createCell( 0 );
			csCell.setCellValue( "工号" );
			csCell.setCellStyle( Style2 );
			csCell = row2.createCell( 1 );
			csCell.setCellValue( "姓名" );
			csCell.setCellStyle( Style2 );
			csCell = row2.createCell( 2 );
			csCell.setCellValue( "部门" );
			csCell.setCellStyle( Style2 );
			HSSFRow row3 = sheet.createRow( 4 );
			getRowMess( sheet, csCell, row2, Style2, 3, 3, 3, 4, "工作时长" );
			getRowMess( sheet, csCell, row3, Style2, 4, 4, 3, 3, "标准" );
			getRowMess( sheet, csCell, row3, Style2, 4, 4, 4, 4, "实际" );

			getRowMess( sheet, csCell, row2, Style2, 3, 3, 5, 6, "迟到" );
			getRowMess( sheet, csCell, row3, Style2, 4, 4, 5, 5, "次数" );
			getRowMess( sheet, csCell, row3, Style2, 4, 4, 6, 6, "分数" );

			getRowMess( sheet, csCell, row2, Style2, 3, 3, 7, 8, "早退" );
			getRowMess( sheet, csCell, row3, Style2, 4, 4, 7, 7, "次数" );
			getRowMess( sheet, csCell, row3, Style2, 4, 4, 8, 8, "分数" );

			

			getRowMess( sheet, csCell, row2, Style2, 3, 4, 9, 9, "出勤天数\n(标准/实际)" );
			sheet.setColumnWidth( 9, 15 * 256 );
			
			getRowMess( sheet, csCell, row2, Style2, 3, 4, 10, 10, "出差\n(小时)" );
			getRowMess( sheet, csCell, row2, Style2, 3, 4, 11, 11, "外出\n(小时)" );
			getRowMess( sheet, csCell, row2, Style2, 3, 4, 12, 12, "旷工\n(小时)" );
			getRowMess( sheet, csCell, row2, Style2, 3, 4, 13, 13, "事假\n(小时)" );
			getRowMess( sheet, csCell, row2, Style2, 3, 4, 14, 14, "病假\n(小时)" );
			getRowMess( sheet, csCell, row2, Style2, 3, 4, 15, 15, "年假\n(小时)" );
			getRowMess( sheet, csCell, row2, Style2, 3, 4, 16, 16, "婚假\n(小时)" );
			getRowMess( sheet, csCell, row2, Style2, 3, 4, 17, 17, "产假\n(小时)" );
			getRowMess( sheet, csCell, row2, Style2, 3, 4, 18, 18, "丧假\n(小时)" );
			getRowMess( sheet, csCell, row2, Style2, 3, 4, 19, 19, "加班时长\n(小时)" );

			getRowMess( sheet, csCell, row2, Style2, 3, 3, 20, 21, "调休" );
			getRowMess( sheet, csCell, row3, Style2, 4, 4, 20, 20, "可调休时长" );
			getRowMess( sheet, csCell, row3, Style2, 4, 4, 21, 21, "本月调休时长" );
			sheet.setColumnWidth( 20, 14 * 256 );
			sheet.setColumnWidth( 21, 15 * 256 );
			getRowMess( sheet, csCell, row2, Style2, 3, 3, 22, 27, "减项工资" );
			getRowMess( sheet, csCell, row3, Style2, 4, 4, 22, 22, "迟到" );
			getRowMess( sheet, csCell, row3, Style2, 4, 4, 23, 23, "早退" );
			getRowMess( sheet, csCell, row3, Style2, 4, 4, 24, 24, "事假" );
			getRowMess( sheet, csCell, row3, Style2, 4, 4, 25, 25, "病假" );
			getRowMess( sheet, csCell, row3, Style2, 4, 4, 26, 26, "产假" );
			getRowMess( sheet, csCell, row3, Style2, 4, 4, 27, 27, "扣款" );

			getRowMess( sheet, csCell, row2, Style2, 3, 4, 28, 28, "备注" );
			sheet.setColumnWidth( 28, 50 * 256 );

			HSSFFont font3 = wb.createFont();
			font3.setFontName( "宋体" );
			font3.setFontHeightInPoints( (short) 10 );
			HSSFCellStyle Style3 = wb.createCellStyle();
			Style3.setFont( font3 );
			Style3.setAlignment( HSSFCellStyle.ALIGN_CENTER );
			Style3.setVerticalAlignment( HSSFCellStyle.VERTICAL_CENTER );
			
			int row = 5;
			for(Map<String,Object> map :checkSumData){
				HSSFRow	rowData = sheet.createRow(row);
				for(int col=0;col<29;col++){
					getRowMess( sheet, csCell, rowData, Style3, row, row, col, col, map.get("checkSum_"+col) ==null ?"" : map.get("checkSum_"+col));
				}
				row++;
			}

			wb.write( out );
		}catch ( Exception e ) {
			new BusinessException("导出考勤汇总表失败，请联系管理员");
		}
	}


	public static void getRowMess( HSSFSheet sheet, HSSFCell csCell, HSSFRow row, HSSFCellStyle style,
				       int startRow, int endRow, int startCol, int endCol, Object cellValue )
	{
		sheet.addMergedRegion( new CellRangeAddress( startRow, endRow, startCol, endCol ) );
		csCell = row.createCell( startCol );
		csCell.setCellValue( cellValue.toString() );
		csCell.setCellStyle( style );
	}


	public static void main( String[] args )
	{
		List<Map<String,Object>> checkSumData = null;
		try {
			OutputStream out = new FileOutputStream( "E:/测试" + DateUtils.getDate( "yyyy-MM-dd HHmmssSSS" ) + ".xls" );
			impCheckSumXls( out ,checkSumData,"2016-08-21","2016-09-20");
			out.close();
			System.out.println( "Excel导出成功！" );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
}
