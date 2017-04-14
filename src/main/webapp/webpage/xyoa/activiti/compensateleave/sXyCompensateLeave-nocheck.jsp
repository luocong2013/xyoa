<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <title>调休表</title>
  <t:base type="jquery,easyui,tools"></t:base>
  <script type="text/javascript">
//编写自定义JS代码
function bSubmit() {
	var nopassReason = $.trim($("#nopassReason").val());
	if (nopassReason == '') {
		alertTip("请输入驳回原因");
		return false;
	}
	return true;
}
  </script>
</head>

<body>
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" beforeSubmit="bSubmit();" action="sXyCompensateLeaveController.do?doNotAgreeLeave" tiptype="1" >
		<input id="id" name="id" type="hidden" value="${sXyCompensateLeavePage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								驳回原因：
							</label>
						</td>
						<td class="value">
								<textarea id="nopassReason" style="width:475px; height: 200px;" class="inputxt" rows="6" name="nopassReason"></textarea>
			          		<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">驳回原因</label>
						</td>
					</tr>
			</table>
</t:formvalid>
</body>
</html>