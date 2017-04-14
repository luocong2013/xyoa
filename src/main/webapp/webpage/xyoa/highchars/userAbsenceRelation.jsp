<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html >
<script type="text/javascript">
	$(function() {
		$(document).ready(function() {
			var chart;
			$.ajax({
				type : "POST",
				url : "highCharsController.do?getAbsenceBarRelation&reportType=${reportType}",
				success : function(jsondata) {
					data = eval(jsondata);
					chart = new Highcharts.Chart({
						chart : {
							renderTo : 'containerCol',
							plotBackgroundColor : null,
							plotBorderWidth : null,
							plotShadow : false
						},
						title : {
							text : "请假记录相关性分析"
						},
						xAxis : {
							//categories : [ 'IE9', 'MSIE 7.0', 'MSIE 8.0', 'MSIE 7.0', 'Firefox', 'Chrome' ]
						},
						yAxis: {
							title: {
								text: '置信度(%)'
							}
						},
						tooltip : {
							headerFormat: '<span style="font-size: 12px"><b>{point.key}</b></span><br/>',
							percentageDecimals : 1,
							valueSuffix: ' %'
						},
						exporting:{  
			                filename:'柱状图',  
			                url:'${ctxPath}/highCharsController.do?export'//
			            },
						plotOptions : {
							column : {
								allowPointSelect : true,
								cursor : 'pointer',
								showInLegend : true,
								dataLabels : {
									inside: true,
									enabled : true,
									color : '#000000',
									connectorColor : '#000000',
									formatter : function() {
										return '<b>' + this.point.y + '</b> ' + '%';
									}
								}
							}
						},
						series : data
					});
				}
			});
		});
	});
</script>
<div id="containerCol" style="width: 85%; height: 85%"></div>


