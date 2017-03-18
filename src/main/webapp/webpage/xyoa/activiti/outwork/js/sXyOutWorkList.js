$(document).ready(function(){
 		//给时间控件加上样式
 			$("#sXyOutWorkListtb").find("input[name='outStartTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyOutWorkListtb").find("input[name='outStartTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyOutWorkListtb").find("input[name='outEndTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyOutWorkListtb").find("input[name='outEndTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyOutWorkListtb").find("input[name='startTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyOutWorkListtb").find("input[name='endTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyOutWorkListtb").find("input[name='backDate']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyOutWorkListtb").find("input[name='cTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyOutWorkListtb").find("input[name='uTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyOutWorkListtb").find("div[name='searchColums']").find("form#sXyOutWorkListForm").append($("#tempSearchColums div[name='searchColums']").html());
 	        $("#tempSearchColums").html('');
});
 
//编辑外出申请
 function goUpdate(id) {
	 var url = 'sXyOutWorkController.do?goUpdate&id='+id;
	 createwindow("编辑", url);
 }
 
 //提交外出申请
 function goSubmitOutwork(id) {
	 var url = 'sXyOutWorkController.do?goSubmitOutwork&id='+id;
	 createwindow("选择第一审批人",url,600,140);
 }
 
//撤销外出申请
 function delOutwork(id) {
	 var url = 'sXyOutWorkController.do?delOutwork&id='+id;
	 var content = "确定撤销该申请？";
	 var gridname = 'sXyOutWorkList';
	 confirm(url, content, gridname);
 }
 
 //查看流程图片
 function lookFlowImage(flowInstId) {
	 var url = 'sXyOutWorkController.do?lookFlowImage&flowInstId='+flowInstId;
	 createdetailwindow("流程图片",url,1000,500);
 }
 
 //重新提交外出申请
 function reSubmitOutwork(id) {
	 var url = 'sXyOutWorkController.do?reSubmitOutwork&id='+id;
	 var content = "确定重新提交该申请？";
	 var gridname = 'sXyOutWorkList';
	 confirm(url, content, gridname);
 }
 
 //取消提交外出申请
 function cancelOutwork(id) {
	 var url = 'sXyOutWorkController.do?cancelOutwork&id='+id;
	 var content = "确定取消该申请？";
	 var gridname = 'sXyOutWorkList';
	 confirm(url, content, gridname);
 }
 
 //销假
 function gobackOutwork(id) {
	 var url = 'sXyOutWorkController.do?gobackOutwork&id='+id;
	 createwindow("销假申请", url, 600, 300);
 }