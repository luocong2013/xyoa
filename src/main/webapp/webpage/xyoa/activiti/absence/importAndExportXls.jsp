<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script src = "webpage/xyoa/activiti/absence/js/sXyAbsenceList.js"></script>
<div class="easyui-layout" fit="true">
<div region="center" style="padding:0px;border:0px">
  <t:datagrid name="sXyAbsenceUploadExcelList" title="导入Excel解析并导出" actionUrl="sXyAbsenceController.do?datagridUploadExcel" pagination="false" 
  					queryMode="group" autoLoadData="true" idField="id">
  	<t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="80"></t:dgCol>
  	<t:dgCol title="文件名称" field="fileName" align="center" queryMode="single"  width="120"/>
  	<t:dgCol title="操作" field="opt" width="200" align="center" ></t:dgCol>
  	<t:dgFunOpt funname="downFile(id)" title="下载" />
	<t:dgToolBar title="导入Excel" icon="icon-put" funname="ImportExcel" />
</t:datagrid>
</div>
</div>