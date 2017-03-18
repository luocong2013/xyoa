<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script src = "webpage/xyoa/checkapply/js/sXyCheckApplyList.js"></script>
<div class="easyui-layout" fit="true">
<div region="center" style="padding:0px;border:0px">
<div id="tempSearchColums" style="display: none;">
    <div name="searchColums">
    	<span style="display:-moz-inline-box;display:inline-block;">
			<span style="vertical-align:middle;display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;" title="所属部门">
				所属部门：
			</span>
		<input readonly="readonly" type="text" id="departname" name="departname" style="width: 200px" onclick="openDepartmentSelect()"  />
		<input id="orgId" name="orgId" type="hidden" />
		</span>
    </div>
</div>
  <t:datagrid name="sXyCheckApplyList" checkbox="false" fitColumns="true" title="享宇考勤异常申请表" actionUrl="sXyCheckApplyController.do?datagrid" 
                   idField="id" fit="true" queryMode="group" sortName="createTime" sortOrder="desc" onDblClick="onDbclick_table_row">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="申请编号"  field="applyNo" query="true" align="center" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="申请人"  field="tsUser.realName" query="true" align="center"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属部门"  field="tsDept.id" replace="${replacedepart}" align="center"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="异常考勤IDs"  field="checkIds"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="申请人编号"  field="applySttaffId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="考勤类型"  field="checkType" query="true" align="center" dictionary="applycheckType"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="申请日期"  field="applyDate" query="true" align="center" formatter="yyyy-MM-dd"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="考勤异常原因"  field="remarks"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建时间"  field="createTime" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人"  field="cUser"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新时间"  field="uTime" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single" width="120"></t:dgCol>
   <t:dgCol title="更新人"  field="uUser"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程状态"  field="flowState" align="center" replace="流程未启动_0,异常待审批_1,异常审批中_2,异常申请被否决_3,已完成_4,已撤销_5,已取消_6"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程实例ID"  field="flowInstId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" align="center" width="200"></t:dgCol>
   <t:dgFunOpt funname="editor(id)" title="修改"  exp="flowState#eq#0"/>
   <t:dgFunOpt funname="editor(id)" title="修改"  exp="flowState#eq#3"/>
   <t:dgFunOpt title="提交申请" funname="goSubmitCheck(id)" exp="flowState#eq#0"/>
   <t:dgDelOpt title="删除" url="sXyCheckApplyController.do?doDel&id={id}" exp="flowState#eq#0"/>
   <t:dgFunOpt funname="delCheck(id)" title="撤销申请" exp="flowState#eq#1"/>
   <t:dgFunOpt funname="lookFlowImage(flowInstId)" title="流程图片" exp="flowState#eq#1"/>
   <t:dgFunOpt funname="lookFlowImage(flowInstId)" title="流程图片" exp="flowState#eq#2"/>
   <t:dgFunOpt funname="reSubmitCheck(id)" title="重新申请" exp="flowState#eq#3"/>
   <t:dgFunOpt funname="cancelCheck(id)" title="取消申请" exp="flowState#eq#3"/>
   <t:dgToolBar title="考勤异常申请" icon="icon-add" url="sXyCheckApplyController.do?goAdd" width="900" height="500" funname="add"></t:dgToolBar>
   <t:dgToolBar title="查看详情" icon="icon-search" url="sXyCheckApplyController.do?goDetail"  width="900" height="500" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script>
 function onDbclick_table_row(rowIndex, rowData) {
	 createdetailwindow("查看详情", "sXyCheckApplyController.do?goDetail&load=detail&id="+rowData.id, 900, 500);
 }
 </script>