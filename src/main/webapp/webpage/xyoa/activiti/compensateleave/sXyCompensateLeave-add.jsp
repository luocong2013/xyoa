<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>调休表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script src = "webpage/xyoa/activiti/compensateleave/js/sXyCompensateLeave-add.js"></script>
  <script type="text/javascript">
  //编写自定义JS代码
  
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" beforeSubmit="bSubmit();" action="sXyCompensateLeaveController.do?doAdd" tiptype="1" >
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
							调休类别:
						</label>
					</td>
					<td class="value" colspan="3">
							  <t:dictSelect field="leaveType" type="list" id="leaveType"
									typeGroupCode="leavetype"  hasLabel="false"  title="调休类别"></t:dictSelect>     
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">调休类别</label>
						</td>
					</tr>
					
					
				<tr>
					<td align="right">
						<label class="Validform_label">
							申请调休开始时间:
						</label>
					</td>
					<td class="value">
							   <input id="leaveStartTime" name="leaveStartTime" type="text" style="width: 150px"
					      						 class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" onchange="timeChange();">
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请调休开始时间</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							申请调休结束时间:
						</label>
					</td>
					<td class="value">
							   <input id="leaveEndTime" name="leaveEndTime" type="text" style="width: 150px"
					      						 class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" onchange="timeChange();">
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请调休结束时间</label>
						</td>
					</tr>
					
			<tr>
				<td align="right">
					<label class="Validform_label">
						申请调休时长:
					</label>
				</td>
				<td class="value" colspan="3">
						     <input id="applyLeaveHour" name="applyLeaveHour" type="text" style="width: 150px" class="inputxt" readonly="readonly">
						     <font color="red">（h）</font>
						<span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">申请调休时长</label>
						</td>
				  </tr>
					
				<tr>
					<td align="right">
						<label class="Validform_label">
							调休原因:
						</label>
					</td>
					<td class="value" colspan="3">
						  	 <textarea style="width:545px; height: 150px;" class="inputxt" rows="6" id="remarks" name="remarks"></textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">调休原因</label>
						</td>
					</tr>
					
					
				<tr>
					<td align="right">
						<label class="Validform_label">
							工作备份安排:
						</label>
					</td>
					<td class="value" colspan="3">
						  	 <textarea style="width:545px; height: 150px;" class="inputxt" rows="6" id="transferWork" name="transferWork"></textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">工作备份安排</label>
						</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
</html>