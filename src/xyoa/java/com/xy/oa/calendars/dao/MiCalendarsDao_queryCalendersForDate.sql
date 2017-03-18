SELECT calendarday,calendartype FROM s_xy_calendars 
WHERE calendarday>= '${startTime}'
AND calendarday<= '${endTime}'