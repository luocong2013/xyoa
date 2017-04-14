<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
<div region="center" style="padding:0px;border:0px">
  <t:datagrid name="deployList" title="流程定义信息列表" actionUrl="deployController.do?datagrid" pagination="false" 
  					queryMode="group" autoLoadData="true" idField="id">
  	<t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="80"></t:dgCol>
  	<t:dgCol title="流程定义名称" field="name" align="center" queryMode="single"  width="120"/>
  	<t:dgCol title="流程定义KEY" field="key" align="center" queryMode="single"  width="120"/>
  	<t:dgCol title="流程定义版本" field="version" align="center" queryMode="single"  width="120"/>
  	<t:dgCol title="部署对象ID" field="deploymentId" align="center" queryMode="single"  width="120"/>
  	<t:dgCol title="操作" field="opt" width="200" align="center" ></t:dgCol>
  	<t:dgFunOpt funname="viewImage(id)" title="查看流程图" />
  	<t:dgDelOpt title="common.delete" url="deployController.do?del&deploymentId={deploymentId}" /> 
	<t:dgToolBar title="上传流程部署文件" icon="icon-put" funname="ImportZip" />
</t:datagrid>
</div>
</div>
<script type="text/javascript">
//导入
function ImportZip() {
	openuploadwin('上传流程部署文件', 'deployController.do?upload', "deployList");
}
//查看流程图片
function viewImage(id) {
	var url = 'deployController.do?viewImage&pdid='+id;
	createdetailwindow("流程图片",url,1000,500);
}
</script>