<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="deptJobList" checkbox="true" fitColumns="true" title="岗位表" actionUrl="deptJobController.do?datagrid" 
  	idField="id" fit="true" queryMode="group" sortName="deptId">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="部门ID"  field="deptId"   replace="${replace}" queryMode="single" query="true"  width="120"></t:dgCol>
   
   <t:dgCol title="岗位名称"  field="jobName"   query="true" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建日期"  field="createTime" formatter="yyyy-MM-dd"  query="true" queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="创建人"  field="createUser"    queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新日期"  field="updateTime" formatter="yyyy-MM-dd"  query="true" queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="跟新人"  field="updateUser"    queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="deptJobController.do?doDel&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="deptJobController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="deptJobController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="deptJobController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="deptJobController.do?goUpdate" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
 		//给时间控件加上样式
 			$("#deptJobListtb").find("input[name='createTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#deptJobListtb").find("input[name='createTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#deptJobListtb").find("input[name='updateTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#deptJobListtb").find("input[name='updateTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 
   
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'deptJobController.do?upload', "deptJobList");
}

//导出
function ExportXls() {
	JeecgExcelExport("deptJobController.do?exportXls","deptJobList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("deptJobController.do?exportXlsByT","deptJobList");
}
 </script>