<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>考勤统计表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script type="text/javascript">
//编写自定义JS代码
</script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" tiptype="1" >
		<table style="width: 450px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							部门:
						</label>
					</td>
					<td class="value">
							<input readonly="readonly" type="text" id="departname" name="departname" class="inputxt" style="width: 200px"/>
							<input id="orgId" name="orgId" type="hidden" />
							<a href="#" class="easyui-linkbutton" plain="true" icon="icon-search"onclick="openDepartmentSelect()">选择</a>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">部门</label>
						</td>
					</tr>
					
					
				<tr>
					<td align="right">
						<label class="Validform_label">
							考勤日期:
						</label>
					</td>
					<td class="value">
							   <input id="startDate" name="startDate" type="text" style="width: 150px"
					      						class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"><span><font color="red">*</font></span> ~
					      		<input id="endDate" name="endDate" type="text" style="width: 150px"
					      						class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})">   
							<span class="Validform_checktip"><font color="red">*</font></span>
							<label class="Validform_label" style="display: none;">考勤日期</label>
						</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/xyoa/checkinout/sXyCheckinout.js"></script>		
