$(document).ready(function(){
 		//给时间控件加上样式
		$("#sXyWorkOvertimeListtb").find("input[name='applyStartTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		$("#sXyWorkOvertimeListtb").find("input[name='applyStartTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		$("#sXyWorkOvertimeListtb").find("input[name='applyEndTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		$("#sXyWorkOvertimeListtb").find("input[name='applyEndTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		$("#sXyWorkOvertimeListtb").find("input[name='startTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		$("#sXyWorkOvertimeListtb").find("input[name='endTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		$("#sXyWorkOvertimeListtb").find("input[name='applyDate']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		$("#sXyWorkOvertimeListtb").find("input[name='cTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		$("#sXyWorkOvertimeListtb").find("input[name='uTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		//$("#sXyWorkOvertimeListtb").find("div[name='searchColums']").find("form#sXyWorkOvertimeListForm").append($("#tempSearchColums div[name='searchColums']").html());
	    //$("#tempSearchColums").html('');
 });
 
 //编辑加班申请表
 function editor(id) {
	 var url = 'sXyWorkOvertimeController.do?goUpdate&id='+id;
	 createwindow("编辑", url);
 }
 
 //提交加班申请
 function goSubmitOvertime(id) {
	 var url = 'sXyWorkOvertimeController.do?goSubmitOvertime&id='+id;
	 createwindow("选择第一审批人", url, 600, 140);
 }
 
//启动流程
function startFlow(id) {
 	var url = 'sXyWorkOvertimeController.do?startFlow&id='+id;
 	var content = "确定启动该流程？";
 	var gridname = 'sXyWorkOvertimeList';
 	confirm(url, content, gridname);
}
 
//查看流程图片
 function lookFlowImage(flowInstId) {
	 var url = 'sXyWorkOvertimeController.do?lookFlowImage&flowInstId='+flowInstId;
	 createdetailwindow("流程图片",url,1000,500);
 }
 
 //取消加班申请
 function cancelOvertime(id) {
	 var url = 'sXyWorkOvertimeController.do?cancelOvertime&id='+id;
	 var content = "确定取消该申请？";
	 var gridname = 'sXyWorkOvertimeList';
	 confirm(url, content, gridname);
 }