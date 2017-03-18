<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>xy_work_schedule</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="xyWorkScheduleController.do?doAdd" tiptype="1" >
					<input id="id" name="id" type="hidden" value="${xyWorkSchedulePage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							工作日期:
						</label>
					</td>
					<td class="value">
					     	 <input id="scheduleDay" name="scheduleDay" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">工作日期</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							排班类型:
						</label>
					</td>
					<td class="value">
					     	 <input id="scheduleType" name="scheduleType" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">排班类型</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							员工id:
						</label>
					</td>
					<td class="value">
					     	 <input id="staffId" name="staffId" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">员工id</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							员工uuid:
						</label>
					</td>
					<td class="value">
					     	 <input id="userId" name="userId" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">员工uuid</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							部门ID:
						</label>
					</td>
					<td class="value">
					     	 <input id="deptId" name="deptId" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">部门ID</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							创建人:
						</label>
					</td>
					<td class="value">
					     	 <input id="cUser" name="cUser" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">创建人</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							更新人:
						</label>
					</td>
					<td class="value">
					     	 <input id="uUser" name="uUser" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">更新人</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							创建时间:
						</label>
					</td>
					<td class="value">
							   <input id="cTime" name="cTime" type="text" style="width: 150px" 
					      						class="Wdate" onClick="WdatePicker()"
>    
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">创建时间</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							更新时间:
						</label>
					</td>
					<td class="value">
							   <input id="uTime" name="uTime" type="text" style="width: 150px" 
					      						class="Wdate" onClick="WdatePicker()"
>    
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">更新时间</label>
						</td>
				</tr>
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/xyoa/workschedule/xyWorkSchedule.js"></script>		
