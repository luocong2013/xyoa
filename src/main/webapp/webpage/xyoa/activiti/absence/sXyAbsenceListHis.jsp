<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
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
  <t:datagrid name="sXyAbsenceListHis" checkbox="false" fitColumns="true" title="请假申请历史记录列表"  actionUrl="sXyAbsenceController.do?datagridHis" 
  					idField="id" fit="true" queryMode="group" sortName="createTime" sortOrder="desc" onDblClick="onDbclick_table_row">
    <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="80"></t:dgCol>
   <t:dgCol title="申请编号"  field="applyNo" query="true" align="center" queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="申请人"  field="tsUser.realName" query="true" align="center"  queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="所属部门"  field="tsDept.id" replace="${replacedepart}" align="center"  queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="休假类型"  field="absenceType" query="true" replace="事假_01, 病假_02, 生育假_03, 婚假_04, 陪产假_05, 产假_06, 哺乳假_07, 年假_08" align="center"   queryMode="single"  width="60"></t:dgCol>
   <t:dgCol title="申请请假开始时间"  field="startTime"  formatter="yyyy-MM-dd hh:mm:ss"  align="center"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="申请请假结束时间"  field="endTime"  formatter="yyyy-MM-dd hh:mm:ss"  align="center"   queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="请假结束工作时间"  field="workTime" formatter="yyyy-MM-dd hh:mm:ss" hidden="true" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="申请请假小时数"  field="applyAbsenceDay"  align="center"   queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="申请日期"  field="applyDate" formatter="yyyy-MM-dd hh:mm:ss"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="销假日期"  field="backDate" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="请假原因"  field="remarks"  hidden="true"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="工作备份安排"  field="transferWork"  hidden="true"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="销假原因"  field="backRemarks"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="真实请假小时数"  field="absenceDay"  hidden="true"  queryMode="single"  width="60"></t:dgCol>
   <t:dgCol title="创建时间"  field="createTime" formatter="yyyy-MM-dd hh:mm:ss"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人"  field="cUser"    queryMode="single" hidden="true" width="120"></t:dgCol>
   <t:dgCol title="更新时间"  field="uTime" formatter="yyyy-MM-dd hh:mm:ss"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人"  field="uUser" hidden="true"    queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程状态"  field="flowState" query="true" replace="流程未启动_0,申请未提交_1,申请审批中_2,申请被驳回_3,审批已完成_4,申请已取消_5"  align="center"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程实例ID"  field="flowInstId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgToolBar title="查看详情" icon="icon-search" url="sXyAbsenceController.do?goDetail" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
		//给时间控件加上样式
			$("#sXyAbsenceListHistb").find("input[name='startTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyAbsenceListHistb").find("input[name='startTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyAbsenceListHistb").find("input[name='endTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyAbsenceListHistb").find("input[name='endTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyAbsenceListHistb").find("div[name='searchColums']").find("form#sXyAbsenceListHisForm").append($("#tempSearchColums div[name='searchColums']").html());
 	        $("#tempSearchColums").html('');
});
 
 function onDbclick_table_row(rowIndex, rowData) {
	 createdetailwindow("查看详情", "sXyAbsenceController.do?goDetail&load=detail&id="+rowData.id);
 }
 </script>