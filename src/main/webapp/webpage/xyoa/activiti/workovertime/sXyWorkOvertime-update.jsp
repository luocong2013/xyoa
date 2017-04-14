<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>加班表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="webpage/xyoa/activiti/workovertime/js/sXyWorkOvertime-add.js"></script>
  <script type="text/javascript">
  //编写自定义JS代码
  function monload() {
	  var val = $("#workval").val();
	  //设置选中的值
	  $("#workType").val(val);
	  if(val == '03' || val == '04') {
	  	$("#applyStartTime").attr("readonly", true);
	    $("#applyEndTime").attr("readonly", true);
	  }
  }
  </script>
 </head>
 <body onload="monload()">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" beforeSubmit="bSubmit();" action="sXyWorkOvertimeController.do?doUpdate" tiptype="1" >
					<input id="id" name="id" type="hidden" value="${sXyWorkOvertimePage.id }">
					<input id="workval" type="hidden" value="${sXyWorkOvertimePage.workType }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
		
				<tr>
					<td align="right">
						<label class="Validform_label">
							申请人:
						</label>
					</td>
					<td class="value">
								<input id="tsUser" name="tsUser" type="text" style="width: 150px" class="inputxt" value='${sXyWorkOvertimePage.tsUser.realName }' disabled="disabled">
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请人</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							所属部门:
						</label>
					</td>
					<td class="value">
					     	 <input id="tsDept" name="tsDept" type="text" style="width: 150px" class="inputxt" value="${sXyWorkOvertimePage.tsDept.departname }" disabled="disabled">
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">所属部门</label>
						</td>
					</tr>
		
		
				<tr>
						<td align="right">
							<label class="Validform_label">
								加班类型:
							</label>
						</td>
						<td class="value" colspan="3">
							  <select id="workType" name="workType" onchange="change()">
							  	<option value="">---请选择---</option>
							  	<option value="01">休息日加班</option>
							  	<option value="02">节假日加班</option>
							  	<option value="03">凌晨加班</option>
							  	<option value="04">通宵加班</option>
							  </select>     
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">加班类型</label>
						</td>
					</tr>
		
		
				<tr>
					<td align="right">
						<label class="Validform_label">
							申请加班开始时间:
						</label>
					</td>
					<td class="value">
							   <input id="applyStartTime" name="applyStartTime" type="text" style="width: 150px" 
							   					class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" onchange="timeChange();"
							   					value='<fmt:formatDate value='${sXyWorkOvertimePage.applyStartTime}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/>'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请加班开始时间</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							申请加班结束时间:
						</label>
					</td>
					<td class="value">
							   <input id="applyEndTime" name="applyEndTime" type="text" style="width: 150px" 
					      						 class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" onchange="timeChange();"
					      						 value='<fmt:formatDate value='${sXyWorkOvertimePage.applyEndTime}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/>'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请加班结束时间</label>
						</td>
					</tr>
					
					
				<tr>
					<td align="right">
							<label class="Validform_label">
								申请加班时长:
							</label>
						</td>
						<td class="value" colspan="3">
						     	 <input id="applyWorkHour" name="applyWorkHour" type="text" style="width: 150px" class="inputxt" value='${sXyWorkOvertimePage.applyWorkHour}' readonly="readonly">
						     	 <font color="red">（h）</font>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请加班时长</label>
						</td>
					</tr>
					
					
				<tr>
					<td align="right">
						<label class="Validform_label">
							加班原因:
						</label>
					</td>
					<td class="value" colspan="3">
						  	 <textarea style="width:545px; height: 200px;" class="inputxt" rows="6" id="remarks" name="remarks">${sXyWorkOvertimePage.remarks}</textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">加班原因</label>
						</td>
					</tr>
					
			</table>
		</t:formvalid>
 </body>