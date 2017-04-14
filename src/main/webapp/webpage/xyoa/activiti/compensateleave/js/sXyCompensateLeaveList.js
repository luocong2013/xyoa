$(document).ready(function(){
 		//给时间控件加上样式
 			$("#sXyCompensateLeaveListtb").find("input[name='leaveStartTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyCompensateLeaveListtb").find("input[name='leaveStartTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyCompensateLeaveListtb").find("input[name='leaveEndTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyCompensateLeaveListtb").find("input[name='leaveEndTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyCompensateLeaveListtb").find("input[name='startTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyCompensateLeaveListtb").find("input[name='endTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyCompensateLeaveListtb").find("input[name='applyDate']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyCompensateLeaveListtb").find("input[name='backDate']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyCompensateLeaveListtb").find("input[name='cTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyCompensateLeaveListtb").find("input[name='uTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 
//编辑调休申请
 function goUpdate(id) {
	 var url = 'sXyCompensateLeaveController.do?goUpdate&id='+id;
	 createwindow("编辑", url);
 }
 
 //提交调休申请
 function goSubmitLeave(id) {
	 var url = 'sXyCompensateLeaveController.do?goSubmitLeave&id='+id;
	 createwindow("选择第一审批人",url,600,140);
 }
 
//启动流程
 function startFlow(id) {
	 var url = 'sXyCompensateLeaveController.do?startFlow&id='+id;
	 var content = "确定启动该流程？";
	 var gridname = 'sXyCompensateLeaveList';
	 confirm(url, content, gridname);
 }
 
 //查看流程图片
 function lookFlowImage(flowInstId) {
	 var url = 'sXyCompensateLeaveController.do?lookFlowImage&flowInstId='+flowInstId;
	 createdetailwindow("流程图片",url,1000,500);
 }
 
 //取消调休申请
 function cancelLeave(id) {
	 var url = 'sXyCompensateLeaveController.do?cancelLeave&id='+id;
	 var content = "确定取消该申请？";
	 var gridname = 'sXyCompensateLeaveList';
	 confirm(url, content, gridname);
 }