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
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="sXyCheckinoutController.do?doAdd" tiptype="1" >
					<input id="id" name="id" type="hidden" value="${sXyCheckinoutPage.id }">
					<input id="staffId" name="staffId" type="hidden" value="${sXyCheckinoutPage.staffId }">
					<input id="deptId" name="deptId" type="hidden" value="${sXyCheckinoutPage.deptId }">
					<input id="applyId" name="applyId" type="hidden" value="${sXyCheckinoutPage.applyId }">
					<input id="cUser" name="cUser" type="hidden" value="${sXyCheckinoutPage.cUser }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							考勤日期:
						</label>
					</td>
					<td class="value">
							   <input id="checkDate" name="checkDate" type="text" style="width: 150px" 
					      						class="Wdate" onClick="WdatePicker()"
>    
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">考勤日期</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							上班时间:
						</label>
					</td>
					<td class="value">
					     	 <input id="workTime" name="workTime" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">上班时间</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							下班时间:
						</label>
					</td>
					<td class="value">
					     	 <input id="offWorkTime" name="offWorkTime" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">下班时间</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							异常时间:
						</label>
					</td>
					<td class="value">
					     	 <input id="exceptionMinute" name="exceptionMinute" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">异常时间</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							异常原因:
						</label>
					</td>
					<td class="value">
					     	 <input id="exceptionRemarks" name="exceptionRemarks" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">异常原因</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							考勤状态:
						</label>
					</td>
					<td class="value">
					     	 <input id="isCheckTrue" name="isCheckTrue" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">考勤状态</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							考勤类型:
						</label>
					</td>
					<td class="value">
					     	 <input id="checkType" name="checkType" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">考勤类型</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							考勤原因:
						</label>
					</td>
					<td class="value">
					     	 <input id="checkRemarks" name="checkRemarks" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">考勤原因</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							日期类型:
						</label>
					</td>
					<td class="value">
					     	 <input id="dateType" name="dateType" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">日期类型</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							加班时长:
						</label>
					</td>
					<td class="value">
					     	 <input id="workHour" name="workHour" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">加班时长</label>
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
				<td align="right">
					<label class="Validform_label">
					</label>
				</td>
				<td class="value">
				</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/xyoa/checkinout/sXyCheckinout.js"></script>		
