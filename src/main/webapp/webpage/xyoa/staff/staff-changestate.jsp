<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>享宇员工表</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script type="text/javascript">
  //编写自定义JS代码
  </script>
</head>
<body>
	<t:formvalid formid="formobj" dialog="true" usePlugin="password"
		layout="table" action="staffController.do?dochangestate" tiptype="1">
		<input id="staffid" name="staffid" type="hidden" value="${staff.id }">
		<input id="userid" name="userid" type="hidden" value="${user.id }">
		<table style="width: 350px;" cellpadding="0" cellspacing="1"
			class="formtable">
			<tr>
		
		<td align="right"><label class="Validform_label">部门: </label>
				</td>
				<td class="value" colspan='2'>
				
				<input  type="text" style="width: 205px" class="inputxt" value="${staff.deptId }"  disabled="true">
							
				  <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">部门</label></td>
				
			</tr>
			<tr>
		
		<td align="right"><label class="Validform_label">姓名: </label>
				</td>
				<td class="value" colspan='2'>
				
				<input  type="text" style="width: 205px" class="inputxt" value="${staff.name }" disabled="true">
							
				  <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">姓名</label></td>
				
			</tr>
			<tr>
		
		<td align="right"><label class="Validform_label">入本单位时间: </label>
				</td>
				<td class="value" colspan='2'>
				
				<input  type="text" style="width: 205px" class="Wdate" onClick="WdatePicker()" 
				value='<fmt:formatDate value='${staff.goXyDate }' type="date" pattern="yyyy-MM-dd"/>' disabled="true">				
			
							
				  <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">入本单位时间</label></td>
				
			</tr>
			<tr>
		
		<td align="right"><label class="Validform_label">劳动合同开始: </label>
				</td>
				<td class="value" colspan='2'>
				
				<input  type="text" style="width: 205px"  class="Wdate" onClick="WdatePicker()" 
				value='<fmt:formatDate value='${staff.contractStartDate }' type="date" pattern="yyyy-MM-dd"/>' disabled="true">
				
							
				  <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">劳动合同开始</label></td>
				
			</tr>
			<tr>
		
		<td align="right"><label class="Validform_label">劳动合同结束: </label>
				</td>
				<td class="value" colspan='2'>
				
				<input  type="text" style="width: 205px"  class="Wdate" onClick="WdatePicker()"
				value='<fmt:formatDate value='${staff.contractEndDate }' type="date" pattern="yyyy-MM-dd"/>' disabled="true">
							
				  <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">劳动合同结束</label></td>
				
			</tr>
			<tr>
		
		<td align="right"><label class="Validform_label">离职时间: </label>
				</td>
				<td class="value" colspan='2'>
				
				<input id="abdicateDate" class="Wdate" onClick="WdatePicker()" name="abdicateDate" type="text" style="width: 205px" class="inputxt" >
							
				  <span
					class="Validform_checktip"><font color="red">*</font></span> <label class="Validform_label"
					style="display: none;">离职时间</label></td>
				
			</tr>
			
<tr>
		
		<td align="right"><label class="Validform_label"> 离职原因: </label>
				</td>
				<td class="value" colspan='2'>
				<textarea id="abdicateRemarks" name="abdicateRemarks"  
					  class="inputxt" cols="2" rows="1"  style="width: 205px;height: 50px;"></textarea>
				   <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">离职原因</label></td>
				
			</tr>

		</table>
	</t:formvalid>
</body>

</html>