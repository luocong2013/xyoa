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
 			
 			$("#sXyBusinessTripListtb").find("div[name='searchColums']").find("form#sXyBusinessTripListForm").append($("#tempSearchColums div[name='searchColums']").html());
 	        $("#tempSearchColums").html('');
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
 
//撤销出差申请
 function delTrip(id) {
	 var url = 'sXyBusinessTripController.do?delTrip&id='+id;
	 var content = "确定撤销该申请？";
	 var gridname = 'sXyBusinessTripList';
	 confirm(url, content, gridname);
 }
 
 //查看流程图片
 function lookFlowImage(flowInstId) {
	 var url = 'sXyBusinessTripController.do?lookFlowImage&flowInstId='+flowInstId;
	 createdetailwindow("流程图片",url,1000,500);
 }
 
 //重新提交出差申请
 function reSubmitTrip(id) {
	 var url = 'sXyBusinessTripController.do?reSubmitTrip&id='+id;
	 var content = "确定重新提交该申请？";
	 var gridname = 'sXyBusinessTripList';
	 confirm(url, content, gridname);
 }
 
 //取消出差申请
 function cancelTrip(id) {
	 var url = 'sXyBusinessTripController.do?cancelTrip&id='+id;
	 var content = "确定取消该申请？";
	 var gridname = 'sXyBusinessTripList';
	 confirm(url, content, gridname);
 }
 
 //销假
 function gobackTrip(id) {
	 var url = 'sXyBusinessTripController.do?gobackTrip&id='+id;
	 createwindow("销假申请", url, 600, 300);
 }