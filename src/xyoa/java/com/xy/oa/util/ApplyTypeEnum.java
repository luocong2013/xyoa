package com.xy.oa.util;

public enum ApplyTypeEnum {
	  QJ("QJ","请假"),
	  JB("JB","加班"),
	  TX("TX" ,"调休"),
	  GC("GC" ,"公出"),
	  CC("CC" ,"出差"),
	  KQ("KQ" ,"考勤");

	private final String code;
    private final String desc;

    private ApplyTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ApplyTypeEnum getByCode(String code) {
        if (code == null || code.trim().length() ==0) {
            return null;
        }

        for (ApplyTypeEnum item : ApplyTypeEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }

        return null;
    }
}