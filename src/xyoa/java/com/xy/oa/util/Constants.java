package com.xy.oa.util;

public interface Constants {

    // 定义短信验证码长度
    public static final int MOBILE_CODE_LENGTH = 6;

    public static final String DATE_TYPE_B="B";//B-工作日
    public static final String DATE_TYPE_W="W";//W-休息日
    public static final String DATE_TYPE_H="H";//H-节假日
    
    public static final String CHECKINOUT_IS_TRUE="00";//00-正常
    public static final String CHECKINOUT_IS_FALSE="01";//01-异常
    
    
    public static final String XY_START_WORK_TIME="09:00:00";//上班时间
    public static final String XY_END_WORK_TIME="18:00:00";//下班时间
    
    public static final String XY_END_WORK_TIME_21="21:00:00";//下班时间
    
    public static final String XY_END_WORK_TIME_23="23:00:00";//下班时间

    public static final Double XY_WORK_HOUR = 7.5 ;//工作分钟数
    public static final Integer XY_WORK_MINUTE= 450 ;//工作分钟数
    
    public static final String XY_WORK_CHANGE_MINUTE= "changeMinute" ;//可推迟打卡分钟数
    public static final String XY_WORK_LAST_END_TIME= "lastEndTime" ;//上日下班时间
    public static final String XY_WORK_LAST_CHANGE_COUNT= "lastChangeCount" ;//使用次数
    public static final String XY_WORK_START_TIME= "startTime" ;//上班时间
    public static final String XY_WORK_WEEK_WORK_MINUTE= "weekWorkMinute" ;//每周的加班分钟数

    public static final String XY_WORK_CHANGE_COUNT= "changeCount" ;//使用次数
    public static final String XY_WORK_WORK_MINUTE= "workMinute" ;//前日或当日加班小时数
    
    public static final String XY_START_WORK_TIME_NIGHT="12:00:00";//上班时间
    public static final String XY_END_WORK_TIME_NIGHT="20:30:00";//下班时间
    
    public static final String XY_WORK_TYPE_01="01";//早班
    public static final String XY_WORK_TYPE_02="02";//晚班
    public static final String XY_WORK_TYPE_03="03";//休息
    public static final String XY_WORK_TYPE_04="04";//加班
    
    public static final String XY_RULE_OR = "\\|\\|";
    
    public static final String XY_RULE_AND = "&&";
    
    public static final String XY_CHECK_TYPE_00 = "00" ;//00-正常上下班，
    public static final String XY_CHECK_TYPE_01 = "01" ;//01-事假           
    public static final String XY_CHECK_TYPE_02 = "02" ;//02-病假
    public static final String XY_CHECK_TYPE_03 = "03" ;//03-生育假
    public static final String XY_CHECK_TYPE_04 = "04" ;//04-婚假  ，
    public static final String XY_CHECK_TYPE_05 = "05" ;//05-陪产假，
    public static final String XY_CHECK_TYPE_06 = "06" ;//06-产假 ，
    public static final String XY_CHECK_TYPE_07 = "07" ;//07-哺乳假，
    public static final String XY_CHECK_TYPE_08 = "08" ;//08-年假,
    public static final String XY_CHECK_TYPE_09 = "09" ;//09-推迟打卡,
    public static final String XY_CHECK_TYPE_10 = "10" ;//10-前日加班
    public static final String XY_CHECK_TYPE_11 = "11" ;//11-迟到
    public static final String XY_CHECK_TYPE_12 = "12" ;//12-早退
    public static final String XY_CHECK_TYPE_13 = "13" ;//13-迟到早退
    public static final String XY_CHECK_TYPE_14 = "14" ;//14-缺勤
    public static final String XY_CHECK_TYPE_15 = "15" ;//15-忘记打卡，
    public static final String XY_CHECK_TYPE_16 = "16" ;//16-打卡异常
    public static final String XY_CHECK_TYPE_17 = "17" ;//17-出差
    public static final String XY_CHECK_TYPE_18 = "18" ;//18-公出
    public static final String XY_CHECK_TYPE_19 = "19" ;//19-调休
    public static final String XY_CHECK_TYPE_20 = "20" ;//20-加班

    //外包人员
    public static final String OUTEM = "outEm";
    
  //普通员工
  	public static final String EMPLOYEE = "employee";
  	//直接上级
  	public static final String HEADMAN = "headman";
  	//人事专员
  	public static final String HR = "hr";
  	//部门DM
  	public static final String DM = "dm";
  	//人事DM
  	public static final String HRDM = "hrdm";
  	//副总裁
  	public static final String VICE = "vice";
  	//总裁
  	public static final String CEO = "ceo";
  	//管理员
  	public static final String ADMIN = "admin";
  	//运营部加班折合调休倍数
  	public static final double YUNYINGBU = 0.5;
  	//其他部门加班折合调休倍数
  	public static final double OTHER = 1;
  	
  	
  	public static final String DATA_IMP_TASK_ID = "DatabaseTaskCronTrigger";
  	
  	public static final String FLOW_STATE_7 = "7";
}
