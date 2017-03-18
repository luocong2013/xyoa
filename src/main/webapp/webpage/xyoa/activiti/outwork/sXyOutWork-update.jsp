<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>享宇外出表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script src = "webpage/xyoa/activiti/outwork/js/sXyOutWork-add.js"></script>
  <script type="text/javascript">
  //编写自定义JS代码
  
  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" beforeSubmit="bSubmit();" action="sXyOutWorkController.do?doUpdate" tiptype="1" >
					<input id="id" name="id" type="hidden" value="${sXyOutWorkPage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
		
					<tr>
						<td align="right">
							<label class="Validform_label">
								申请人:
							</label>
						</td>
						<td class="value">
								<input id="tsUser" name="tsUser" type="text" style="width: 150px" class="inputxt" value='${sXyOutWorkPage.tsUser.realName }' disabled="disabled">
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请人</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								所属部门:
							</label>
						</td>
						<td class="value">
					     	 <input id="tsDept" name="tsDept" type="text" style="width: 150px" class="inputxt" value="${sXyOutWorkPage.tsDept.departname }" disabled="disabled">
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">所属部门</label>
						</td>
					</tr>
		
		
					<tr>
						<td align="right">
							<label class="Validform_label">
								申请外出开始时间:
							</label>
						</td>
						<td class="value">
									  <input id="outStartTime" name="outStartTime" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" 
									  onchange="timeChange();" value='<fmt:formatDate value='${sXyOutWorkPage.outStartTime}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/>'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请外出开始时间</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								申请外出结束时间:
							</label>
						</td>
						<td class="value">
									  <input id="outEndTime" name="outEndTime" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
									  onchange="timeChange();" value='<fmt:formatDate value='${sXyOutWorkPage.outEndTime}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/>'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请外出结束时间</label>
						</td>
					</tr>
					
					<tr>
						<td align="right">
							<label class="Validform_label">
								申请外出时长:
							</label>
						</td>
						<td class="value" colspan="3">
						     	 <input id="applyOutHour" name="applyOutHour" type="text" style="width: 150px" class="inputxt" value='${sXyOutWorkPage.applyOutHour}' readonly="readonly">
						     	 <font color="red">（h）</font>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请外出时长</label>
						</td>
					</tr>
					
					
					<tr>
						<td align="right">
							<label class="Validform_label">
								外出原因:
							</label>
						</td>
						<td class="value" colspan="3">
						  	 	<textarea id="remarks" style="width:545px; height: 200px;" class="inputxt" rows="6" name="remarks">${sXyOutWorkPage.remarks}</textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">外出原因</label>
						</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
</html>