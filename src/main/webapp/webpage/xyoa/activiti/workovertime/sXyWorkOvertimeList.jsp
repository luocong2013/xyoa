<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script src = "webpage/xyoa/activiti/workovertime/js/sXyWorkOvertimeList.js"></script>
<div class="easyui-layout" fit="true">
<div region="center" style="padding:0px;border:0px">
  <t:datagrid name="sXyWorkOvertimeList" checkbox="false" fitColumns="true" sortOrder="desc"  title="我的加班申请列表" actionUrl="sXyWorkOvertimeController.do?datagrid" 
  					idField="id" fit="true" queryMode="group" sortName="createTime" onDblClick="onDbclick_table_row">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="申请编号"  field="applyNo" query="true" align="center" queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="申请人"  field="tsUser.realName"  align="center" query="true"  queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="所属部门"  field="tsDept.id" replace="${replacedepart}"  align="center" queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="加班类型"  field="workType" query="true" replace="休息日加班_01, 节假日加班_02, 凌晨加班_03, 通宵加班_04" align="center" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="申请加班开始时间"  field="applyStartTime"  align="center" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="申请加班结束时间"  field="applyEndTime"  align="center" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="申请加班时长"  field="applyWorkHour"  align="center"  queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="加班开始时间"  field="startTime" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="加班结束时间"  field="endTime" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="真实加班时长"  field="workHour"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="可调休时长"  field="onWorkHour"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="申请日期"  field="applyDate" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="加班原因"  field="remarks"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建时间"  field="createTime" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人"  field="cUser"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新时间"  field="uTime" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人"  field="uUser"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程状态"  field="flowState" align="center" query="true" replace="流程未启动_0,申请未提交_1,申请审批中_2,申请被驳回_3,审批已完成_4,申请已取消_5"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程实例ID"  field="flowInstId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" align="center" width="200"></t:dgCol>
   <t:dgFunOpt funname="editor(id)" title="修改" exp="flowState#eq#0"/>
   <t:dgFunOpt funname="editor(id)" title="修改" exp="flowState#eq#1"/>
   <t:dgFunOpt funname="editor(id)" title="修改" exp="flowState#eq#3"/>
   <t:dgDelOpt title="删除" url="sXyWorkOvertimeController.do?doDel&id={id}" exp="flowState#eq#0"/>
   <t:dgFunOpt funname="startFlow(id)" title="启动流程" exp="flowState#eq#0"/>
   <t:dgFunOpt funname="goSubmitOvertime(id)" title="提交申请" exp="flowState#eq#1"/>
   <t:dgFunOpt funname="cancelOvertime(id)" title="取消申请" exp="flowState#eq#1"/>
   <t:dgFunOpt funname="lookFlowImage(flowInstId)" title="流程图片" exp="flowState#eq#2" />
   <t:dgFunOpt funname="goSubmitOvertime(id)" title="提交申请" exp="flowState#eq#3"/>
   <t:dgFunOpt funname="cancelOvertime(id)" title="取消申请" exp="flowState#eq#3"/>
   <t:dgToolBar title="加班申请" icon="icon-add" url="sXyWorkOvertimeController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="查看详情" icon="icon-search" url="sXyWorkOvertimeController.do?goDetail" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script>
 function onDbclick_table_row(rowIndex, rowData) {
	 createdetailwindow("查看详情", "sXyWorkOvertimeController.do?goDetail&load=detail&id="+rowData.id);
 }
 </script>