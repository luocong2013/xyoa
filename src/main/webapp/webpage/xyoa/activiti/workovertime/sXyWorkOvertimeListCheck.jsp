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
		<input readonly="readonly" type="text" id="departname" name="departname" style="width: 200px" onclick="openDepartmentSelect('check')"  />
		<input id="orgId" name="orgId" type="hidden" />
		</span>
    </div>
</div>
  <t:datagrid name="sXyWorkOvertimeListCheck" checkbox="false" fitColumns="true" title="加班申请审批列表" actionUrl="sXyWorkOvertimeController.do?datagridCheck" 
  					idField="id" fit="true" queryMode="group" sortName="createTime" sortOrder="desc" onDblClick="onDbclick_table_row">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="申请编号"  field="applyNo" query="true" align="center" queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="申请人"  field="tsUser.realName"  align="center" query="true"  queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="所属部门"  field="tsDept.id" replace="${replacedepart}" align="center" queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="加班类型"  field="workType" query="true" replace="休息日加班_01, 节假日加班_02, 凌晨加班_03, 通宵加班_04"  align="center" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="申请加班开始时间"  field="applyStartTime" align="center" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="120"></t:dgCol>
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
   <t:dgCol title="流程状态"  field="flowState"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程实例ID"  field="flowInstId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="批准"  field="isAgree"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="备案"  field="isHrAgree"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="驳回"  field="isNotAgree"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" align="center" width="100"></t:dgCol>
   <t:dgFunOpt funname="goAgreeOvertime(id)" title="批准" exp="isAgree#eq#01"/>
   <t:dgFunOpt funname="hrAgreeOvertime(id)" title="备案" exp="isHrAgree#eq#01"/>
   <t:dgFunOpt funname="goNotAgreeOvertime(id)" title="驳回" exp="isNotAgree#eq#01"/>
   <t:dgToolBar title="查看详情" icon="icon-search" url="sXyWorkOvertimeController.do?goDetail" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
		//给时间控件加上样式
			$("#sXyWorkOvertimeListChecktb").find("input[name='applyStartTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyWorkOvertimeListChecktb").find("input[name='applyStartTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyWorkOvertimeListChecktb").find("input[name='applyEndTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyWorkOvertimeListChecktb").find("input[name='applyEndTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyWorkOvertimeListChecktb").find("div[name='searchColums']").find("form#sXyWorkOvertimeListCheckForm").append($("#tempSearchColums div[name='searchColums']").html());
		    $("#tempSearchColums").html('');
});
 //批准
 function goAgreeOvertime(id) {
	 var url = 'sXyWorkOvertimeController.do?goAgreeOvertime&id='+id;
	 createwindow("选择下一审批人", url, 600, 300);
 }
 
//人事备案
 function hrAgreeOvertime(id) {
	 var url = 'sXyWorkOvertimeController.do?hrAgreeOvertime&id='+id;
	 var content = "确定备案？";
	 var gridname = 'sXyWorkOvertimeListCheck';
	 confirm(url, content, gridname);
 }
 
 //驳回
 function goNotAgreeOvertime(id) {
	 var url = 'sXyWorkOvertimeController.do?goNotAgreeOvertime&id='+id;
	 createwindow("驳回原因", url, 600, 250);
 }
 
 function onDbclick_table_row(rowIndex, rowData) {
	 createdetailwindow("查看详情", "sXyWorkOvertimeController.do?goDetail&load=detail&id="+rowData.id);
 }
 </script>