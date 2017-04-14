<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!-- context path -->
<t:base type="jquery,easyui"></t:base>
<script type="text/javascript" src="plug-in/Highcharts-2.2.5/js/highcharts.src.js"></script>
<script type="text/javascript" src="plug-in/Highcharts-2.2.5/js/modules/exporting.src.js"></script>
<t:tabs id="tt" iframe="false">
	<t:tab href="highCharsController.do?userAbsence&reportType=line" icon="icon-search" title="直线图" id="pnode"></t:tab>
	<t:tab href="highCharsController.do?userAbsence&reportType=pie" icon="icon-search" title="饼状图" id="pnode"></t:tab>
	<t:tab href="highCharsController.do?userAbsence&reportType=column" icon="icon-search" title="柱状图" id="pnode"></t:tab>
</t:tabs>
