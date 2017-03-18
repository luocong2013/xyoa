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
  <t:datagrid name="sXyCheckinoutList3" checkbox="false" fitColumns="false" title="考勤统计表" actionUrl="sXyCheckinoutController.do?datagrid&staffId=${staffid}"
  					 idField="id" fit="true" queryMode="group" sortName="checkDate" sortOrder="desc">
   <t:dgCol title="id"  field="id"  hidden="true"  queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="姓名"  field="staffId"  hidden="true" query="false"  queryMode="single"  width="100"  ></t:dgCol>
   <t:dgCol title="姓名"  field="name"  align="center" query="true"  queryMode="single"  width="100"  ></t:dgCol>
   <t:dgCol title="部门"  field="deptId"  replace="${deptname}" align="center" query="false"  queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="考勤日期"  field="checkDate" align="center" formatter="yyyy-MM-dd"  query="true" queryMode="group"  width="100" ></t:dgCol>
   <t:dgCol title="上班时间"  field="workTime"  align="center"  queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="下班时间"  field="offWorkTime"  align="center"  queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="考勤状态"  field="isCheckTrue"  align="center" query="true" queryMode="single" width="100"  replace="正常_00,异常_01"></t:dgCol>
   <t:dgCol title="异常分钟数"  field="exceptionMinute"  align="center"  queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="异常原因"  field="exceptionRemarks" hidden="true"   queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="考勤类型"  field="checkType"   hidden="true" queryMode="group"  width="100" ></t:dgCol>
   <t:dgCol title="考勤原因"  field="checkRemarks" hidden="true"   queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="申请人ID"  field="applyId" hidden="true"  queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="日期类型"  field="dateType" align="center"  query="true" queryMode="single"   width="100" replace="工作日_B,休息日_W,节假日_H" ></t:dgCol>
   <t:dgCol title="加班时长"  field="workHour"  align="center"  queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="修改时间"  field="uTime" hidden="true" align="center" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="150"></t:dgCol>
   <t:dgCol title="修改人"  field="uUser" hidden="true" align="center" queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="考勤系统员工ID"  field="userId" hidden="true"  queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="流程状态"  field="flowState" hidden="true" queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="流程实例ID"  field="flowInstId"  hidden="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" align="center" width="100"></t:dgCol>
   <t:dgFunOpt title="查看考勤流水"  funname="elecrecord(userId,checkDate)" />
   <t:dgToolBar title="查看详情" icon="icon-search" url="sXyCheckinoutController.do?goUpdate" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
	   //给时间控件加上样式
	    $("#sXyCheckinoutList3tb").find("input[name='checkDate_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
	    $("#sXyCheckinoutList3tb").find("input[name='checkDate_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
	    $("#sXyCheckinoutList3tb").find("div[name='searchColums']").find("form#sXyCheckinoutList3Form").append($("#tempSearchColums div[name='searchColums']").html());
		$("#tempSearchColums").html('');
});
 
 function elecrecord(userId,checkDate) {
   url="${pageContext.request.contextPath}//sXyCheckinoutController.do?pCheckList&userId="+userId+"&checkDate="+checkDate;	
   var name="考勤流水列表";
   createwindow(name,url,680,400);
}
 </script>