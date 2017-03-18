<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="sXyStaffHomeMemberList" checkbox="true" fitColumns="false" title="员工家庭成员" actionUrl="sXyStaffHomeMemberController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="id"  field="id"  hidden="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="姓名"  field="name"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="关系"  field="relative"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="年龄"  field="age"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="工作单位"  field="workUnit"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="职务"  field="workDuty"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="电话"  field="tel"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="创建人"  field="createUser"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="创建时间"  field="createTime" formatter="yyyy-MM-dd"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="跟新人"  field="updateUser"    queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="跟新时间"  field="updateTime" formatter="yyyy-MM-dd"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="sXyStaffHomeMemberController.do?doDel&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="sXyStaffHomeMemberController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="sXyStaffHomeMemberController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="sXyStaffHomeMemberController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="sXyStaffHomeMemberController.do?goUpdate" funname="detail"></t:dgToolBar>
   <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
   <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/xyoa/staffhomemember/sXyStaffHomeMemberList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 		//给时间控件加上样式
 			$("#sXyStaffHomeMemberListtb").find("input[name='createTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyStaffHomeMemberListtb").find("input[name='createTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyStaffHomeMemberListtb").find("input[name='updateTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyStaffHomeMemberListtb").find("input[name='updateTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 
   
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'sXyStaffHomeMemberController.do?upload', "sXyStaffHomeMemberList");
}

//导出
function ExportXls() {
	JeecgExcelExport("sXyStaffHomeMemberController.do?exportXls","sXyStaffHomeMemberList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("sXyStaffHomeMemberController.do?exportXlsByT","sXyStaffHomeMemberList");
}
 </script>