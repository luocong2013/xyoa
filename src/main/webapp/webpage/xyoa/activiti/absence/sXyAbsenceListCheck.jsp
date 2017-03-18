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
  <t:datagrid name="sXyAbsenceListCheck" checkbox="false" fitColumns="true" title="享宇请假审批表"  actionUrl="sXyAbsenceController.do?datagridCheck" 
  				  idField="id" fit="true" queryMode="group" sortName="createTime" sortOrder="desc" onDblClick="onDbclick_table_row">
    <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="80"></t:dgCol>
   <t:dgCol title="申请编号"  field="applyNo" query="true" align="center" queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="申请人"  field="tsUser.realName" query="true" align="center"  queryMode="single"  width="70"></t:dgCol>
   <t:dgCol title="所属部门"  field="tsDept.id" replace="${replacedepart}" align="center"  queryMode="single"  width="70"></t:dgCol>
   <t:dgCol title="休假类型"  field="absenceType" query="true" replace="事假_01, 病假_02, 生育假_03, 婚假_04, 陪产假_05, 产假_06, 哺乳假_07, 年假_08" align="center"   queryMode="single"  width="70"></t:dgCol>
   <t:dgCol title="申请请假开始时间"  field="startTime" query="true" formatter="yyyy-MM-dd hh:mm:ss"  align="center"  queryMode="group"  width="140"></t:dgCol>
   <t:dgCol title="申请请假结束时间"  field="endTime" query="true" formatter="yyyy-MM-dd hh:mm:ss"  align="center"   queryMode="group"  width="140"></t:dgCol>
   <t:dgCol title="请假结束工作时间"  field="workTime" formatter="yyyy-MM-dd hh:mm:ss" align="center" queryMode="single"  width="140"></t:dgCol>
   <t:dgCol title="申请请假小时数"  field="applyAbsenceDay"  align="center"   queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="申请日期"  field="applyDate" formatter="yyyy-MM-dd hh:mm:ss"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="销假日期"  field="backDate" hidden="true" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="请假原因"  field="remarks"  hidden="true"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="工作备份安排"  field="transferWork"  hidden="true"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="销假原因"  field="backRemarks"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="真实请假小时数"  field="absenceDay"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建时间"  field="createTime" formatter="yyyy-MM-dd hh:mm:ss"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人"  field="cUser"    queryMode="single" hidden="true" width="120"></t:dgCol>
   <t:dgCol title="更新时间"  field="uTime" formatter="yyyy-MM-dd hh:mm:ss"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人"  field="uUser" hidden="true"    queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程状态"  field="flowState" hidden="true" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程实例ID"  field="flowInstId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="审批状态"  field="isApprove" align="center" replace="待审批_00, 已审批_01" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="150" align="center"></t:dgCol>
   <t:dgFunOpt funname="goAgreeAbsence(id)" title="批准" exp="isApprove#eq#00" />
   <t:dgFunOpt funname="goNotAgreeAbsence(id)" title="否决"  exp="isApprove#eq#00"/>
   <t:dgToolBar title="查看详情" icon="icon-search" url="sXyAbsenceController.do?goDetail" funname="detail"/>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
		//给时间控件加上样式
		var datagrid = $("#sXyAbsenceListChecktb");
		datagrid.find("input[name='startTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		datagrid.find("input[name='startTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		datagrid.find("input[name='endTime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		datagrid.find("input[name='endTime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		datagrid.find("div[name='searchColums']").find("form#sXyAbsenceListCheckForm").append($("#tempSearchColums div[name='searchColums']").html());
 	    $("#tempSearchColums").html('');
});
 //批准
 function goAgreeAbsence(id) {
	 var url = 'sXyAbsenceController.do?goAgreeAbsence&id='+id;
	 createwindow("选择下一审批人", url, 600, 300);
 }
 
 //否决
 function goNotAgreeAbsence(id) {
	 var url = 'sXyAbsenceController.do?goNotAgreeAbsence&id='+id;
	 createwindow("否决原因", url, 600, 250);
 }
 
 function onDbclick_table_row(rowIndex, rowData) {
	 createdetailwindow("查看详情", "sXyAbsenceController.do?goDetail&load=detail&id="+rowData.id);
 }
 </script>