$(document).ready(function(){
 		//给时间控件加上样式
		$("#sXyCheckApplyListtb").find("input[name='applyDate_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		$("#sXyCheckApplyListtb").find("input[name='applyDate_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		$("#sXyCheckApplyListtb").find("input[name='cTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		$("#sXyCheckApplyListtb").find("input[name='uTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		$("#sXyCheckApplyListtb").find("div[name='searchColums']").find("form#sXyCheckApplyListForm").append($("#tempSearchColums div[name='searchColums']").html());
	    $("#tempSearchColums").html('');
 });
//编辑考勤异常申请
 function editor(id) {
	 var url = 'sXyCheckApplyController.do?goUpdate&id='+id;
	 createwindow("编辑", url, 900, 500);
 }
 
 //提交考勤异常申请
 function goSubmitCheck(id) {
	 var url = 'sXyCheckApplyController.do?goSubmitCheck&id='+id;
	 createwindow("选择第一审批人", url, 600, 140);
 }
 
//撤销考勤异常申请
 function delCheck(id) {
 	var url = 'sXyCheckApplyController.do?delCheck&id='+id;
 	var content = "确定撤销该申请？";
 	var gridname = 'sXyCheckApplyList';
 	confirm(url, content, gridname);
 }
 
//查看流程图片
 function lookFlowImage(flowInstId) {
	 var url = 'sXyCheckApplyController.do?lookFlowImage&flowInstId='+flowInstId;
	 createdetailwindow("流程图片",url,1000,500);
 }
 
 
//重新提交考勤异常申请
 function reSubmitCheck(id) {
 	var url = 'sXyCheckApplyController.do?reSubmitCheck&id='+id;
 	var content = "确定重新提交该申请？";
 	var gridname = 'sXyCheckApplyList';
 	confirm(url, content, gridname);
 }
 
 //取消考勤异常申请
 function cancelCheck(id) {
	 var url = 'sXyCheckApplyController.do?cancelCheck&id='+id;
	 var content = "确定取消该申请？";
	 var gridname = 'sXyCheckApplyList';
	 confirm(url, content, gridname);
 }