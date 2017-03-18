<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="sXyStaffTrainList" checkbox="true" fitColumns="false" title="员工培训经历" actionUrl="sXyStaffTrainController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="id"  field="id"  hidden="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="课程名称"  field="className"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="培训组织"  field="trainOrganization"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="时间"  field="startTime"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="所获证书"  field="certificate"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="创建人"  field="creatUser"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="创建时间"  field="createTime" formatter="yyyy-MM-dd"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="跟新人"  field="updateUser"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="跟新时间"  field="updateTime" formatter="yyyy-MM-dd"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="sXyStaffTrainController.do?doDel&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="sXyStaffTrainController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="sXyStaffTrainController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="sXyStaffTrainController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="sXyStaffTrainController.do?goUpdate" funname="detail"></t:dgToolBar>
   <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
   <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/xyoa/stafftrain/sXyStaffTrainList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 		//给时间控件加上样式
 			$("#sXyStaffTrainListtb").find("input[name='createTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyStaffTrainListtb").find("input[name='createTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyStaffTrainListtb").find("input[name='updateTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyStaffTrainListtb").find("input[name='updateTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 
   
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'sXyStaffTrainController.do?upload', "sXyStaffTrainList");
}

//导出
function ExportXls() {
	JeecgExcelExport("sXyStaffTrainController.do?exportXls","sXyStaffTrainList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("sXyStaffTrainController.do?exportXlsByT","sXyStaffTrainList");
}
 </script>