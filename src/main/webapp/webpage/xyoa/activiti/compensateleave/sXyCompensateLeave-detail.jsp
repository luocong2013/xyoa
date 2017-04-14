<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>调休表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" tiptype="1" >
					<input id="id" name="id" type="hidden" value="${sXyCompensateLeavePage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
		
					<tr>
						<c:choose>
							<c:when test="${sXyCompensateLeavePage.flowState == '1' }">
								<td align="center" colspan="4" style="color: red; font-size: 20px; font-family: 华文楷体">申请未提交</td>
							</c:when>
							<c:when test="${sXyCompensateLeavePage.flowState == '2' }">
								<td align="center" colspan="4" style="color: red; font-size: 20px; font-family: 华文楷体">申请审批中</td>
							</c:when>
						</c:choose>
					</tr>
					
					
					<tr>
						<td align="right">
							<label class="Validform_label">
								申请人:
							</label>
						</td>
						<td class="value">
						     	 <input id="tsUserId" name="tsUserId" type="text" style="width: 150px" class="inputxt"  value='${sXyCompensateLeavePage.tsUser.realName }'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请人</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								所属部门:
							</label>
						</td>
						<td class="value">
						     	 <input id="deptId" name="deptId" type="text" style="width: 150px" class="inputxt"  value='${sXyCompensateLeavePage.tsDept.departname }'>
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
									<t:dictSelect field="leaveType" type="list"
										typeGroupCode="leavetype" defaultVal="${sXyCompensateLeavePage.leaveType}" hasLabel="false"  title="调休类别"></t:dictSelect>     
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
									  <input id="leaveStartTime" name="leaveStartTime" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" 
									  value='<fmt:formatDate value='${sXyCompensateLeavePage.leaveStartTime}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/>'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请调休开始时间</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								申请调休结束时间:
							</label>
						</td>
						<td class="value">
									  <input id="leaveEndTime" name="leaveEndTime" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" 
									  value='<fmt:formatDate value='${sXyCompensateLeavePage.leaveEndTime}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/>'>
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
						<td class="value">
						     	 <input id="applyLeaveHour" name="applyLeaveHour" type="text" style="width: 150px" class="inputxt"  value='${sXyCompensateLeavePage.applyLeaveHour}'>
						     	 <font color="red">（h）</font>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请调休时长</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								调休开始时间:
							</label>
						</td>
						<td class="value">
									  <input id="startTime" name="startTime" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
									  value='<fmt:formatDate value='${sXyCompensateLeavePage.startTime}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/>'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">调休开始时间</label>
						</td>
					</tr>
					
					
					<c:if test="${sXyCompensateLeavePage.flowState != '0'  }">
					<tr>
						<td align="right">
							<label class="Validform_label">
								申请日期:
							</label>
						</td>
						<td class="value" colspan="3">
									  <input id="applyDate" name="applyDate" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" 
									  value='<fmt:formatDate value='${sXyCompensateLeavePage.applyDate}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/>'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请日期</label>
						</td>
					</tr>
					</c:if>
					
					
					<tr>
						<td align="right">
							<label class="Validform_label">
								调休原因:
							</label>
						</td>
						<td class="value" colspan="3">
						  	 	<textarea id="remarks" style="width:545px; height: 150px;" class="inputxt" rows="6" name="remarks">${sXyCompensateLeavePage.remarks}</textarea>
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
						  	 	<textarea id="transferWork" style="width:540px; height: 150px;" class="inputxt" rows="6" name="transferWork">${sXyCompensateLeavePage.transferWork}</textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">工作备份安排</label>
						</td>
					</tr>
					
					
					<tr>
						<td align="right">
							<label class="Validform_label">
								流程状态:
							</label>
						</td>
						<td class="value" colspan="3">
									<c:choose>
									<c:when test="${sXyCompensateLeavePage.flowState == '0' }">
										<input id="flowState" name="flowState" type="text" style="width: 150px" class="inputxt"   value='流程未启动'>
									</c:when>
									<c:when test="${sXyCompensateLeavePage.flowState == '1' }">
										<input id="flowState" name="flowState" type="text" style="width: 150px" class="inputxt"   value='申请未提交'>
									</c:when>
									<c:when test="${sXyCompensateLeavePage.flowState == '2' }">
										<input id="flowState" name="flowState" type="text" style="width: 150px" class="inputxt"   value='申请审批中'>
									</c:when>
									<c:when test="${sXyCompensateLeavePage.flowState == '3' }">
										<input id="flowState" name="flowState" type="text" style="width: 150px" class="inputxt"   value='申请被驳回'>
									</c:when>
									<c:when test="${sXyCompensateLeavePage.flowState == '4' }">
										<input id="flowState" name="flowState" type="text" style="width: 150px" class="inputxt"   value='审批已完成'>
									</c:when>
									<c:when test="${sXyCompensateLeavePage.flowState == '5' }">
										<input id="flowState" name="flowState" type="text" style="width: 150px" class="inputxt"   value='申请已取消'>
									</c:when>
								</c:choose>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">流程状态</label>
						</td>
					</tr>
					
					
					<c:if test="${sXyCompensateLeavePage.flowState != '0'  }">
					<tr>
						<td align="right">
							<label class="Validform_label">
								审批意见:
							</label>
						</td>
						<td class="value" colspan="3">
								<c:forEach items="${commentBeansPage }" var="commentBean">
									<table>
										<tr>
											<td align="right" style="width: 60px;"><c:out value="${commentBean.getRealName() }"/>：</td>
											<td class="value" style="width: 80px;"><font color="red">（${commentBean.getIsAgree() }）</font></td>
											<td class="value"><c:out value="${commentBean.getFullMessage() }"/></td>
										</tr>
									</table>
								</c:forEach>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">审批意见</label>
						</td>
					</tr>
					</c:if>
			</table>
		</t:formvalid>
 </body>
</html>