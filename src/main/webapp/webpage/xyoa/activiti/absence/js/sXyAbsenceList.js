 $(document).ready(function(){
 		//给时间控件加上样式
 			var datagrid = $("#sXyAbsenceListtb");
 			datagrid.find("input[name='startTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			datagrid.find("input[name='startTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			datagrid.find("input[name='endTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			datagrid.find("input[name='endTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			datagrid.find("input[name='workTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			datagrid.find("input[name='applyDate']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			datagrid.find("input[name='backDate']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			datagrid.find("input[name='cTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			datagrid.find("input[name='uTime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			
 	        datagrid.find("div[name='searchColums']").find("form#sXyAbsenceListForm").append($("#tempSearchColums div[name='searchColums']").html());
 	        $("#tempSearchColums").html('');
 });
 
//编辑请假表
function editor(id) {
	var url = 'sXyAbsenceController.do?goUpdate&id='+id;
	createwindow("编辑", url);
}
 
//提交请假申请
function goSubmitAbsence(id) {
	var url = 'sXyAbsenceController.do?goSubmitAbsence&id='+id;
	createwindow("选择第一审批人",url,600,140);
}

//撤销请假申请
function delAbsence(id) {
	var url = 'sXyAbsenceController.do?delAbsence&id='+id;
	var content = "确定撤销该申请？";
	var gridname = 'sXyAbsenceList';
	confirm(url, content, gridname);
}

//查看流程图片
function lookFlowImage(flowInstId) {
	var url = 'sXyAbsenceController.do?lookFlowImage&flowInstId='+flowInstId;
	createdetailwindow("流程图片",url,1000,500);
}

//重新提交请假申请
function reSubmitAbsence(id) {
	var url = 'sXyAbsenceController.do?reSubmitAbsence&id='+id;
	var content = "确定重新提交该申请？";
	var gridname = 'sXyAbsenceList';
	confirm(url, content, gridname);
}
 
 //取消请假申请
 function cancelAbsence(id) {
	 var url = 'sXyAbsenceController.do?cancelAbsence&id='+id;
	 var content = "确定取消该申请？";
	 var gridname = 'sXyAbsenceList';
	 confirm(url, content, gridname);
 }
 
 //销假
 function gobackAbsence(id) {
	 var url = 'sXyAbsenceController.do?gobackAbsence&id='+id;
	 createwindow("销假申请", url, 600, 300);
 }
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'sXyAbsenceController.do?upload', "sXyAbsenceList");
}

//导出
function ExportXls() {
	JeecgExcelExport("sXyAbsenceController.do?exportXls","sXyAbsenceList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("sXyAbsenceController.do?exportXlsByT","sXyAbsenceList");
}

//导入
function ImportExcel() {
	openuploadwin('Excel导入', 'sXyAbsenceController.do?upload', "sXyAbsenceUploadExcelList");
}

//下载zip文件
function downFile(fileName) {
	var url = 'sXyAbsenceController.do?downFile&fileName='+fileName;
	window.location.href = url;
}