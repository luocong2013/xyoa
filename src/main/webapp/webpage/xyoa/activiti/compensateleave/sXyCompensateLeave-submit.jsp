<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <title>调休表</title>
  <t:base type="jquery,easyui,tools"></t:base>
</head>

<body>
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="sXyCompensateLeaveController.do?doSubmitLeave" tiptype="1" >
		<input id="id" name="id" type="hidden" value="${sXyCompensateLeavePage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								审批人：
							</label>
						</td>
						<td class="value">
							<select name="feaaApprover">
			          			<c:forEach items="${tsUsersPage }" var="tsUsers">
			          				<option value="${tsUsers.userName }">${tsUsers.realName }</option>
			          			</c:forEach>
			          		</select>
			          		<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">审批人</label>
						</td>
					</tr>
			</table>
</t:formvalid>
</body>
</html>