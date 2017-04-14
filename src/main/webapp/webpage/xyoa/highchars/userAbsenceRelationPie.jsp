<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" />
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
							renderTo : 'containerPie',
							plotBackgroundColor : null,
							plotBorderWidth : null,
							plotShadow : false
						},
						title : {
							text : "请假记录相关性分析"
						},
						tooltip : {
							percentageDecimals : 1,
							formatter: function() {
            					return  '<b>'+this.point.name + ': ' +  Highcharts.numberFormat(this.percentage, 1) +'%</b>';
         					}
						},
						exporting:{  
			                filename:'饼状图',  
			                 url:'${ctxPath}/highCharsController.do?export'  
			            },  
						plotOptions : {
							pie : {
								allowPointSelect : true,
								cursor : 'pointer',
								showInLegend : false,
								dataLabels : {
									enabled : true,
									color : '#000000',
									connectorColor : '#000000',
									formatter : function() {
										return '<b>' + this.point.name + '</b>: ' + Highcharts.numberFormat(this.percentage, 1)+"%";
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
<div id="containerPie" style="width: 85%; height: 85%"></div>


