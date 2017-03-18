package com.xy.oa.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ApplyNoUtils {
	/**
	 * 每日工作日常：450分钟（7.5 = 3 （9：00 --12：00） + 4.5（13：30 --18：00））个小时
	 * 
	 * @param applyType
	 * @return 申请编号 
	 */
	public static String getApplyNo(ApplyTypeEnum applyType){
		SimpleDateFormat time = new SimpleDateFormat("yyMMdd");
		String nowDate = time.format(new Date());
		return applyType.getCode()+"-"+nowDate+"01";
	}
	
	public static String getNextApplyNo(String applyNo){
		if(applyNo == null || applyNo.trim().length()<11){
			return null;
		}
		
		String str = applyNo.substring(applyNo.length()-2);
		int no = Integer.parseInt(str);
		if(no>=9){
			str = no +1+"";
		}else{
			str = "0"+(no+1);
		}
		
		return applyNo.substring(0,applyNo.length()-2)+str;
	}
	
	public static void main(String[] args) {
		System.out.println(getApplyNo(ApplyTypeEnum.CC));
		System.out.println(getNextApplyNo("CC-16091409"));
	}
	
	

}
