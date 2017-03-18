<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <title>享宇外出表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
/**
 * 提交表单之前做的非空检验
 */
function bSubmit() {
	var backDate = $.trim($("#backDate").val());
	var backRemarks = $.trim($("#backRemarks").val());
	
	if(backDate == ''){
		alertTip("请选择销假日期");
		return false;
	} else if(backRemarks == ''){
		alertTip("请输入销假原因");
		return false;
	}
	return true;
}
</script>
</head>

<body>
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" beforeSubmit="bSubmit();" action="sXyOutWorkController.do?dobackOutwork" tiptype="1" >
		<input id="id" name="id" type="hidden" value="${sXyOutWorkPage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								销假日期:
							</label>
						</td>
					<td class="value">
							   <input id="backDate" name="backDate" type="text" style="width: 150px"
					      						 class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">销假日期</label>
						</td>
					</tr>
		
					<tr>
						<td align="right">
							<label class="Validform_label">
								销假原因:
							</label>
						</td>
						<td class="value">
						  	 	<textarea id="backRemarks" style="width:475px; height: 200px;" class="inputxt" rows="6" name="backRemarks"></textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">销假原因</label>
						</td>
					</tr>
			</table>
</t:formvalid>
</body>
</html>