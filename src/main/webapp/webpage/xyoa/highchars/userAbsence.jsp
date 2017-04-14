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
				url : "highCharsController.do?getAbsenceBar&reportType=${reportType}",
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
							text : "请假记录频繁项集挖掘"
						},
						xAxis : {
							//categories : [ 'IE9', 'MSIE 7.0', 'MSIE 8.0', 'MSIE 7.0', 'Firefox', 'Chrome' ]
						},
						yAxis: {
							title: {
								text: '支持度计数(次)'
							}
						},
						tooltip : {
							headerFormat: '<span style="font-size: 12px"><b>{point.key}</b></span><br/>',
							percentageDecimals : 1,
							valueSuffix: ' 次'
						},
						exporting:{  
			                buttons: {
			                	printButton: {
			                		enabled: true //是否允许打印按钮
			                	},
			                	exportButton: {
			                		enabled: true //是否允许显示导出按钮
			                	}
			                },
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
										return '<b>' + this.point.y + '</b> ' + '次';
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


