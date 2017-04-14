<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" />
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
							renderTo : 'containerline',
							plotBackgroundColor : null,
							plotBorderWidth : null,
							plotShadow : false
						},
						title : {
							text : "请假记录频繁项集挖掘"
						},
						xAxis : {
							//categories : data.name
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
			                filename:'直线图',  
			                 url:'${ctxPath}/highCharsController.do?export'  
			            }, 
						plotOptions : {
							line : {
								allowPointSelect : true,//是否允许数据点的点击
								cursor : 'pointer',
								showInLegend : true,//是否在图注中显示
								visible: true, //加载时，数据序列默认是显示还是隐藏
								dataLabels : {
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
<div id="containerline" style="width: 85%; height: 85%"></div>


