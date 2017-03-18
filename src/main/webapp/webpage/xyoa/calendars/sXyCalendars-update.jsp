<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>节假日明细表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="sXyCalendarsController.do?doUpdate" tiptype="1" >
					<input id="id" name="id" type="hidden" value="${sXyCalendarsPage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
					
					<tr>
						<td align="right">
							<label class="Validform_label">
								日期类型:
							</label>
						</td>
						<td class="value">
								
							<select name="calendartype">
								   <option style="width:200px" value="B">工作日</option>
								   <option style="width:200px" value="W">双休日</option>
								   <option style="width:200px" value="H">节假日</option>
  							</select>
							 
							 <span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">日期类型</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								节假日:
							</label>
						</td>
						<td class="value">
						     	 <input id="remarks" name="remarks" type="text" style="width: 150px" class="inputxt"  value='${sXyCalendarsPage.remarks}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">备注</label>
						</td>
					</tr>
					
			</table>
		</t:formvalid>
 </body>