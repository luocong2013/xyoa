<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>员工培训经历</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="sXyStaffTrainController.do?doUpdate" tiptype="1" >
					<input id="id" name="id" type="hidden" value="${sXyStaffTrainPage.id }">
		<table style="width: 505px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								课程名称:
							</label>
						</td>
						<td class="value">
						     	 <input id="className" name="className" type="text" style="width: 150px" class="inputxt"  value='${sXyStaffTrainPage.className}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">课程名称</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								培训机构:
							</label>
						</td>
						<td class="value">
						     	 <input id="trainOrganization" name="trainOrganization" type="text" style="width: 150px" class="inputxt"  value='${sXyStaffTrainPage.trainOrganization}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">培训组织</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								时间:
							</label>
						</td>
						<td class="value">
						     	 <input id="startTime" name="startTime" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker()" value='${sXyStaffTrainPage.startTime}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">时间</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								所获证书:
							</label>
						</td>
						<td class="value">
						     	 <input id="certificate" name="certificate" type="text" style="width: 150px" class="inputxt"  value='${sXyStaffTrainPage.certificate}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">所获证书</label>
						</td>
					</tr>
					
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/xyoa/stafftrain/sXyStaffTrain.js"></script>		
