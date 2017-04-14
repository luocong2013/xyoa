<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script src = "webpage/xyoa/activiti/businesstrip/js/sXyBusinessTripList.js"></script>
<div class="easyui-layout" fit="true">
<div region="center" style="padding:0px;border:0px">
  <t:datagrid name="sXyBusinessTripList" checkbox="false" fitColumns="true" title="我的出差申请列表" actionUrl="sXyBusinessTripController.do?datagrid" 
  					idField="id" fit="true" queryMode="group" sortName="createTime" sortOrder="desc" onDblClick="onDbclick_table_row">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="申请编号"  field="applyNo" query="true" align="center" queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="申请人"  field="tsUser.realName"  align="center" query="true"  queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="所属部门"  field="tsDept.id" replace="${replacedepart}"  align="center" queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="申请人ID"  field="applySttaffId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="申请出差开始时间"  field="tripStartTime" align="center" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="申请出差结束时间"  field="tripEndTime"  align="center" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="申请出差时长"  field="applyTripHour"  align="center"  queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="出差开始时间"  field="startTime" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="出差结束时间"  field="endTime" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="真实出差时长"  field="tripHour"  hidden="true"  queryMode="single" dictionary="bpm_status" width="120"></t:dgCol>
   <t:dgCol title="申请日期"  field="applyDate" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="出差原因"  field="remarks"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="销假日期"  field="backDate"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="销假原因"  field="backRemarks"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建时间"  field="createTime" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人"  field="cUser"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新时间"  field="uTime" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人"  field="uUser"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程状态"  field="flowState"  align="center" replace="流程未启动_0,申请未提交_1,申请审批中_2,申请被驳回_3,审批已完成_4,申请已取消_5"  query="true" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程实例ID"  field="flowInstId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" align="center" width="200"></t:dgCol>
   <t:dgFunOpt funname="goUpdate(id)" title="修改" exp="flowState#eq#0"/>
   <t:dgFunOpt funname="goUpdate(id)" title="修改" exp="flowState#eq#1"/>
   <t:dgFunOpt funname="goUpdate(id)" title="修改" exp="flowState#eq#3"/>
   <t:dgDelOpt title="删除" url="sXyBusinessTripController.do?doDel&id={id}" exp="flowState#eq#0"/>
   <t:dgFunOpt funname="startFlow(id)" title="启动流程" exp="flowState#eq#0"/>
   <t:dgFunOpt funname="goSubmitTrip(id)" title="提交申请" exp="flowState#eq#1"/>
   <t:dgFunOpt funname="cancelTrip(id)" title="取消申请" exp="flowState#eq#1"/>
   <t:dgFunOpt funname="lookFlowImage(flowInstId)" title="流程图片" exp="flowState#eq#2" />
   <t:dgFunOpt funname="goSubmitTrip(id)" title="提交申请" exp="flowState#eq#3"/>
   <t:dgFunOpt funname="cancelTrip(id)" title="取消申请" exp="flowState#eq#3"/>
   <t:dgToolBar title="出差申请" icon="icon-add" url="sXyBusinessTripController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="查看详情" icon="icon-search" url="sXyBusinessTripController.do?goDetail" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script>
 function onDbclick_table_row(rowIndex, rowData) {
	 createdetailwindow("查看详情", "sXyBusinessTripController.do?goDetail&load=detail&id="+rowData.id);
 }
 </script>