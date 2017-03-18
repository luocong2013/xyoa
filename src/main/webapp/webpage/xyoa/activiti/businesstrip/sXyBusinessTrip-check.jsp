<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <title>享宇出差表</title>
  <t:base type="jquery,easyui,tools"></t:base>
  <script type="text/javascript">
//编写自定义JS代码
function bSubmit() {
	var passReason = $.trim($("#passReason").val());
	if (passReason == '') {
		alertTip("请输入批准原因");
		return false;
	}
	return true;
}
  </script>
</head>

<body>
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" beforeSubmit="bSubmit();" action="sXyBusinessTripController.do?doAgreeTrip" tiptype="1" >
		<input id="id" name="id" type="hidden" value="${sXyBusinessTripPage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
		
					<tr>
						<td align="right">
							<label class="Validform_label">
								审批人：
							</label>
						</td>
						<td class="value">
							<select name="nextApprover">
			          			<c:forEach items="${tsUsersPage }" var="tsUsers">
			          				<option value="${tsUsers.userName }">${tsUsers.realName }</option>
			          			</c:forEach>
			          		</select>
			          		<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">审批人</label>
						</td>
					</tr>
					
					
					<tr>
						<td align="right">
							<label class="Validform_label">
								批准原因：
							</label>
						</td>
						<td class="value">
								<textarea id="passReason" style="width:475px; height: 200px;" class="inputxt" rows="6" name="passReason"></textarea>
			          		<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">批准原因</label>
						</td>
					</tr>
			</table>
</t:formvalid>
</body>
</html>