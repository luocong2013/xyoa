<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>员工家庭成员</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="sXyStaffHomeMemberController.do?doUpdate" tiptype="1" >
					<input id="id" name="id" type="hidden" value="${sXyStaffHomeMemberPage.id }">
		<table style="width: 505px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								姓名:
							</label>
						</td>
						<td class="value">
						     	 <input id="name" name="name" type="text" style="width: 150px" class="inputxt"  value='${sXyStaffHomeMemberPage.name}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">姓名</label>
						</td>
					<tr>
						<td align="right">
							<label class="Validform_label">
								关系:
							</label>
						</td>
						<td class="value">
						     	 <input id="relative" name="relative" type="text" style="width: 150px" class="inputxt"  value='${sXyStaffHomeMemberPage.relative}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">关系</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								年龄:
							</label>
						</td>
						<td class="value">
						     	 <input id="age" name="age" type="text" style="width: 150px" class="inputxt"    value='${sXyStaffHomeMemberPage.age}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">年龄</label>
						</td>
					<tr>
						<td align="right">
							<label class="Validform_label">
								工作单位:
							</label>
						</td>
						<td class="value">
						     	 <input id="workUnit" name="workUnit" type="text" style="width: 150px" class="inputxt"  value='${sXyStaffHomeMemberPage.workUnit}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">工作单位</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								职务:
							</label>
						</td>
						<td class="value">
						     	 <input id="workDuty" name="workDuty" type="text" style="width: 150px" class="inputxt"  value='${sXyStaffHomeMemberPage.workDuty}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">职务</label>
						</td>
					<tr>
						<td align="right">
							<label class="Validform_label">
								电话:
							</label>
						</td>
						<td class="value">
						     	 <input id="tel" name="tel" type="text" style="width: 150px" class="inputxt"  value='${sXyStaffHomeMemberPage.tel}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">电话</label>
						</td>
					</tr>
					
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/xyoa/staffhomemember/sXyStaffHomeMember.js"></script>		
