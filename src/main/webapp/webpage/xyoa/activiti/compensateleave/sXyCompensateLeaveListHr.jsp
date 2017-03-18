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
		<span style="display:-moz-inline-box;display:inline-block;">
			<span style="vertical-align:middle;display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;" title="审批状态">
				审批状态：
			</span>
			<select name="spflag">
				<option value="">---请选择---</option>
				<option value="00">待审批</option>
				<option value="01">已审批</option>
			</select>
		</span>
    </div>
</div>
  <t:datagrid name="sXyCompensateLeaveListHr" checkbox="false" fitColumns="true" title="人事部审批享宇调休申请" actionUrl="sXyCompensateLeaveController.do?datagridHr" 
  					idField="id" fit="true" queryMode="group" sortName="createTime" sortOrder="desc" onDblClick="onDbclick_table_row">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="申请编号"  field="applyNo" query="true" align="center" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="申请人"  field="tsUser.realName"  align="center"  query="true" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属部门"  field="tsDept.id" replace="${replacedepart}"  align="center" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="申请人ID"  field="applySttaffId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="调休类别"  field="leaveType" query="true" replace="加班调休_01, 其他调休_99" align="center"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="申请调休开始时间"  field="leaveStartTime" query="true" align="center" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="140"></t:dgCol>
   <t:dgCol title="申请调休结束时间"  field="leaveEndTime" query="true" align="center" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="140"></t:dgCol>
   <t:dgCol title="申请调休时长"  field="applyLeaveHour"  align="center"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="调休开始时间"  field="startTime" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="调休结束时间"  field="endTime" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single" dictionary="bpm_status" width="120"></t:dgCol>
   <t:dgCol title="真实调休时长"  field="leaveHour"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="申请日期"  field="applyDate" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="调休原因"  field="remarks"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="工作备份安排"  field="transferWork"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="销假日期"  field="backDate" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="销假原因"  field="backRemarks"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建时间"  field="createTime" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人"  field="cUser"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新时间"  field="uTime" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人"  field="uUser"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程状态"  field="flowState"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程实例ID"  field="flowInstId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="审批状态"  field="isApprove" align="center" replace="待审批_00, 已审批_01" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" align="center" width="100"></t:dgCol>
   <t:dgFunOpt funname="goHrAgreeLeave(id)" title="批准" exp="isApprove#eq#00"></t:dgFunOpt>
   <t:dgFunOpt funname="goHrNotAgreeLeave(id)" title="否决" exp="isApprove#eq#00"></t:dgFunOpt>
   <t:dgToolBar title="查看详情" icon="icon-search" url="sXyCompensateLeaveController.do?goDetail" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
		//给时间控件加上样式
			$("#sXyCompensateLeaveListHrtb").find("input[name='leaveStartTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyCompensateLeaveListHrtb").find("input[name='leaveStartTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyCompensateLeaveListHrtb").find("input[name='leaveEndTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyCompensateLeaveListHrtb").find("input[name='leaveEndTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyCompensateLeaveListHrtb").find("div[name='searchColums']").find("form#sXyCompensateLeaveListHrForm").append($("#tempSearchColums div[name='searchColums']").html());
 	        $("#tempSearchColums").html('');
});
//HR批准
 function goHrAgreeLeave(id) {
	 var url = 'sXyCompensateLeaveController.do?goHrAgreeLeave&id='+id;
	 createwindow("批准原因", url, 600, 250);
 }
 
 //HR否决
 function goHrNotAgreeLeave(id) {
	 var url = 'sXyCompensateLeaveController.do?goHrNotAgreeLeave&id='+id;
	 createwindow("否决原因", url, 600, 250);
 }
 
 function onDbclick_table_row(rowIndex, rowData) {
	 createdetailwindow("查看详情", "sXyCompensateLeaveController.do?goDetail&load=detail&id="+rowData.id);
 }
 </script>