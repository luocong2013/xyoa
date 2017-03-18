<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>排班</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="xySheduleTimeController.do?doAdd" tiptype="1" >
					<input id="id" name="id" type="hidden" value="${xySheduleTimePage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
		<br>
				<tr>
					<td align="right">
						<label class="Validform_label">
							录入年:
						</label>
					</td>
					<td class="value">
					     	 <input id="timename" name="timename" type="text" style="width: 150px" class="inputxt" datatype="n4-5" >
							<span class="Validform_checktip">按年份录入，格式如：2016</span>
							<label class="Validform_label" style="display: none;">时间</label>
						</td>
				</tr>
				<!-- <tr>
					<td align="right">
						<label class="Validform_label">
							类型:
						</label>
					</td>
					<td class="value">
					     	 <input id="type" name="type" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">类型</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							父时间:
						</label>
					</td>
					<td class="value">
					     	 <input id="parentid" name="XySheduleTimeEntity.id" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">父时间</label>
						</td>
				</tr> -->
			</table>
		</t:formvalid>
 </body>
		
