<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="pCheckinoutList"  fitColumns="false" title="" actionUrl="pCheckinoutController.do?datagrid&userId=${userId}&checkDate=${checkDate}" idField="id" fit="true" queryMode="group">
   <t:dgCol title="id"  field="id"  hidden="true" queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="考勤系统员工ID号"  field="userid"    hidden="false" queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="打卡时间"  field="checktime" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="200"></t:dgCol>
   <t:dgCol title="打卡类型"  field="checktype"     queryMode="group"  width="200" replace="签到_I,签退_O"></t:dgCol>
   <t:dgCol title="验证方式"  field="verifycode"  hidden="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="设备ID"  field="sensorid"   hidden="true"  queryMode="group"  width="120"></t:dgCol>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/xyoa/pcheck/pCheckinoutList.js"></script>		
 <script type="text/javascript">

 </script>