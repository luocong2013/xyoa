<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>享宇出差表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script src = "webpage/xyoa/activiti/businesstrip/js/sXyBusinessTrip-add.js"></script>
  <script type="text/javascript">
//编写自定义JS代码

  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" beforeSubmit="bSubmit();" action="sXyBusinessTripController.do?doAdd" tiptype="1">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
		
				<tr>
					<td align="right">
						<label class="Validform_label">
							申请人:
						</label>
					</td>
					<td class="value">
								<input id="tsUser" name="tsUser" type="text" style="width: 150px" class="inputxt" value='${tsUserPage.realName}' disabled="disabled">
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请人</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							所属部门:
						</label>
					</td>
					<td class="value">
					     	 <input id="tsDept" name="tsDept" type="text" style="width: 150px" class="inputxt" value="${tsDepartPage.departname }" disabled="disabled">
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">所属部门</label>
						</td>
					</tr>
		
				<tr>
					<td align="right">
						<label class="Validform_label">
							申请出差开始时间:
						</label>
					</td>
					<td class="value">
							   <input id="tripStartTime" name="tripStartTime" type="text" style="width: 150px"
					      						 class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" onchange="timeChange();">
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请出差开始时间</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							申请出差结束时间:
						</label>
					</td>
					<td class="value">
							   <input id="tripEndTime" name="tripEndTime" type="text" style="width: 150px"
					      						 class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" onchange="timeChange();">
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请出差结束时间</label>
						</td>
					</tr>
				
				  <tr>
						<td align="right">
							<label class="Validform_label">
								申请出差时长:
							</label>
						</td>
						<td class="value" colspan="3">
						     	 <input id="applyTripHour" name="applyTripHour" type="text" style="width: 150px" class="inputxt"  readonly="readonly">
						     	 <font color="red">（h）</font>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请出差时长</label>
						</td>
					</tr>
					
				<tr>
					<td align="right">
						<label class="Validform_label">
							出差原因:
						</label>
					</td>
					<td class="value" colspan="3">
						  	 <textarea style="width:545px; height: 200px;" class="inputxt" rows="6" id="remarks" name="remarks"></textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">出差原因</label>
						</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
  </html>		