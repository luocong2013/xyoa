package com.xy.oa.calendars.dao;

import java.util.Date;
import java.util.List;

import org.jeecgframework.minidao.annotation.Arguments;
import org.jeecgframework.minidao.annotation.ResultType;
import org.springframework.stereotype.Repository;

import com.xy.oa.calendars.entity.SXyCalendarsEntity;

@Repository("miCalendarsDao")
public interface MiCalendarsDao {
	@Arguments({"startTime", "endTime"})
	@ResultType(SXyCalendarsEntity.class)
	public List<SXyCalendarsEntity> queryCalendersForDate(String startTime,String endTime);

}
