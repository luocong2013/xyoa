<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="xyWorkScheduleList" checkbox="true" fitColumns="false" title="xy_work_schedule" actionUrl="xyWorkScheduleController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="ID"  field="id"  hidden="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="工作日期"  field="scheduleDay"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="排班类型"  field="scheduleType"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="员工id"  field="staffId"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="员工uuid"  field="userId"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="部门ID"  field="deptId"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="创建人"  field="cUser"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="更新人"  field="uUser"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="创建时间"  field="cTime" formatter="yyyy-MM-dd"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="更新时间"  field="uTime" formatter="yyyy-MM-dd"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="xyWorkScheduleController.do?doDel&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="xyWorkScheduleController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="xyWorkScheduleController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="xyWorkScheduleController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="xyWorkScheduleController.do?goUpdate" funname="detail"></t:dgToolBar>
   <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
   <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/xyoa/workschedule/xyWorkScheduleList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 		//给时间控件加上样式
 			$("#xyWorkScheduleListtb").find("input[name='cTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#xyWorkScheduleListtb").find("input[name='cTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#xyWorkScheduleListtb").find("input[name='uTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#xyWorkScheduleListtb").find("input[name='uTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 
   
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'xyWorkScheduleController.do?upload', "xyWorkScheduleList");
}

//导出
function ExportXls() {
	JeecgExcelExport("xyWorkScheduleController.do?exportXls","xyWorkScheduleList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("xyWorkScheduleController.do?exportXlsByT","xyWorkScheduleList");
}
 </script>