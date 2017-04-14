$(document).ready(function(){
 		//给时间控件加上样式
 			$("#sXyBusinessTripListtb").find("input[name='tripStartTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyBusinessTripListtb").find("input[name='tripStartTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyBusinessTripListtb").find("input[name='tripEndTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyBusinessTripListtb").find("input[name='tripEndTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyBusinessTripListtb").find("input[name='startTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyBusinessTripListtb").find("input[name='endTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyBusinessTripListtb").find("input[name='applyDate']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyBusinessTripListtb").find("input[name='cTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyBusinessTripListtb").find("input[name='uTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 
 //编辑出差申请
 function goUpdate(id) {
	 var url = 'sXyBusinessTripController.do?goUpdate&id='+id;
	 createwindow("编辑", url);
 }
 
 //提交出差申请
 function goSubmitTrip(id) {
	 var url = 'sXyBusinessTripController.do?goSubmitTrip&id='+id;
	 createwindow("选择第一审批人",url,600,140);
 }
 
//启动流程
 function startFlow(id) {
	 var url = 'sXyBusinessTripController.do?startFlow&id='+id;
	 var content = "确定启动该流程？";
	 var gridname = 'sXyBusinessTripList';
	 confirm(url, content, gridname);
 }
 
 //查看流程图片
 function lookFlowImage(flowInstId) {
	 var url = 'sXyBusinessTripController.do?lookFlowImage&flowInstId='+flowInstId;
	 createdetailwindow("流程图片",url,1000,500);
 }
 
 //取消出差申请
 function cancelTrip(id) {
	 var url = 'sXyBusinessTripController.do?cancelTrip&id='+id;
	 var content = "确定取消该申请？";
	 var gridname = 'sXyBusinessTripList';
	 confirm(url, content, gridname);
 }
 