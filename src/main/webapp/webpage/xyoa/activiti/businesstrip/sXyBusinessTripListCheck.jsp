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
  <t:datagrid name="sXyBusinessTripListCheck" checkbox="false" fitColumns="true" title="出差申请审批列表" actionUrl="sXyBusinessTripController.do?datagridCheck"
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
   <t:dgCol title="流程状态"  field="flowState" hidden="true" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程实例ID"  field="flowInstId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="批准"  field="isAgree"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="备案"  field="isHrAgree"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="驳回"  field="isNotAgree"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" align="center" width="150"></t:dgCol>
   <t:dgFunOpt funname="goAgreeTrip(id)" title="批准" exp="isAgree#eq#01"></t:dgFunOpt>
   <t:dgFunOpt funname="hrAgreeTrip(id)" title="备案" exp="isHrAgree#eq#01"></t:dgFunOpt>
   <t:dgFunOpt funname="goNotAgreeTrip(id)" title="驳回" exp="isNotAgree#eq#01"></t:dgFunOpt>
   <t:dgToolBar title="查看详情" icon="icon-search" url="sXyBusinessTripController.do?goDetail" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
		//给时间控件加上样式
			$("#sXyBusinessTripListChecktb").find("input[name='tripStartTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyBusinessTripListChecktb").find("input[name='tripStartTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyBusinessTripListChecktb").find("input[name='tripEndTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyBusinessTripListChecktb").find("input[name='tripEndTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyBusinessTripListChecktb").find("div[name='searchColums']").find("form#sXyBusinessTripListCheckForm").append($("#tempSearchColums div[name='searchColums']").html());
 	        $("#tempSearchColums").html('');
});
 //批准申请
 function goAgreeTrip(id) {
	 var url = 'sXyBusinessTripController.do?goAgreeTrip&id='+id;
	 createwindow("选择下一审批人", url, 600, 300);
 }
 
//人事备案
 function hrAgreeTrip(id) {
	 var url = 'sXyBusinessTripController.do?hrAgreeTrip&id='+id;
	 var content = "确定备案？";
	 var gridname = 'sXyBusinessTripListCheck';
	 confirm(url, content, gridname);
 }
 
 //驳回申请
 function goNotAgreeTrip(id) {
	 var url = 'sXyBusinessTripController.do?goNotAgreeTrip&id='+id;
	 createwindow("驳回原因", url, 600, 250);
 }
 
 function onDbclick_table_row(rowIndex, rowData) {
	 createdetailwindow("查看详情", "sXyBusinessTripController.do?goDetail&load=detail&id="+rowData.id);
 }
 </script>