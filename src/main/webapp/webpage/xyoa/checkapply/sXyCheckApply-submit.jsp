<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>享宇考勤申请表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="sXyCheckApplyController.do?doSubmitCheck" tiptype="1" >
					<input id="id" name="id" type="hidden" value="${sXyCheckApplyPage.id }">
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