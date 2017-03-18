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
  <t:datagrid name="sXyCheckApplyListHr" checkbox="false" fitColumns="true" title="享宇考勤异常备案" actionUrl="sXyCheckApplyController.do?datagridHr"
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
   <t:dgCol title="流程状态"  field="flowState"  hidden="true" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程实例ID"  field="flowInstId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="审批状态"  field="isApprove" align="center" replace="待审批_00, 已审批_01" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" align="center" width="100"></t:dgCol>
   <t:dgFunOpt funname="goHrAgreeCheck(id)" title="批准" exp="isApprove#eq#00"/>
   <t:dgFunOpt funname="goHrNotAgreeCheck(id)" title="否决" exp="isApprove#eq#00"/>
   <t:dgToolBar title="查看详情" icon="icon-search" url="sXyCheckApplyController.do?goDetail" width="900" height="500" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
		//给时间控件加上样式
			$("#sXyCheckApplyListHrtb").find("input[name='applyDate_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyCheckApplyListHrtb").find("input[name='applyDate_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
			$("#sXyCheckApplyListHrtb").find("div[name='searchColums']").find("form#sXyCheckApplyListHrForm").append($("#tempSearchColums div[name='searchColums']").html());
		    $("#tempSearchColums").html('');
});
//批准
 function goHrAgreeCheck(id) {
	 var url = 'sXyCheckApplyController.do?goHrAgreeCheck&id='+id;
	 createwindow("批准原因", url, 600, 250);
 }
 //否决
 function goHrNotAgreeCheck(id) {
	 var url = 'sXyCheckApplyController.do?goHrNotAgreeCheck&id='+id;
	 createwindow("否决原因", url, 600, 250);
 }
 
 function onDbclick_table_row(rowIndex, rowData) {
	 createdetailwindow("查看详情", "sXyCheckApplyController.do?goDetail&load=detail&id="+rowData.id, 900, 500);
 }
 </script>