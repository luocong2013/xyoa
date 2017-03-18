<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>享宇出差表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" tiptype="1">
					<input id="id" name="id" type="hidden" value="${sXyBusinessTripPage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
		
					<tr>
						<c:choose>
							<c:when test="${sXyBusinessTripPage.flowState == '1' }">
								<td align="center" colspan="4" style="color: red; font-size: 20px; font-family: 华文楷体">出差待审批</td>
							</c:when>
							<c:when test="${sXyBusinessTripPage.flowState == '2' }">
								<td align="center" colspan="4" style="color: red; font-size: 20px; font-family: 华文楷体">出差审批中</td>
							</c:when>
							<c:when test="${sXyBusinessTripPage.flowState == '5' }">
								<td align="center" colspan="4" style="color: red; font-size: 20px; font-family: 华文楷体">销假审批中</td>
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
						     	 <input id="tsUserId" name="tsUserId" type="text" style="width: 150px" class="inputxt"  value='${sXyBusinessTripPage.tsUser.realName }'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请人</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								所属部门:
							</label>
						</td>
						<td class="value">
						     	 <input id="deptId" name="deptId" type="text" style="width: 150px" class="inputxt"  value='${sXyBusinessTripPage.tsDept.departname }'>
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
									  <input id="tripStartTime" name="tripStartTime" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" 
									  value='<fmt:formatDate value='${sXyBusinessTripPage.tripStartTime}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/>'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请出差开始时间</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								申请出差结束时间:
							</label>
						</td>
						<td class="value">
									  <input id="tripEndTime" name="tripEndTime" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" 
									  value='<fmt:formatDate value='${sXyBusinessTripPage.tripEndTime}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/>'>
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
						<td class="value">
						     	 <input id="applyTripHour" name="applyTripHour" type="text" style="width: 150px" class="inputxt"  value='${sXyBusinessTripPage.applyTripHour}'>
						     	 <font color="red">（h）</font>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请出差时长</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								出差开始时间:
							</label>
						</td>
						<td class="value">
									  <input id="startTime" name="startTime" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" 
									  value='<fmt:formatDate value='${sXyBusinessTripPage.startTime}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/>'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">出差开始时间</label>
						</td>
					</tr>

					
					<c:if test="${sXyBusinessTripPage.flowState == '5' || sXyBusinessTripPage.flowState == '7' }">
					<tr>
						<td align="right">
							<label class="Validform_label">
								出差结束时间:
							</label>
						</td>
						<td class="value">
									  <input id="endTime" name="endTime" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" 
									  value='<fmt:formatDate value='${sXyBusinessTripPage.endTime}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/>'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">出差结束时间</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								真实出差时长:
							</label>
						</td>
						<td class="value">
						     	 <input id="tripHour" name="tripHour" type="text" style="width: 150px" class="inputxt"  value='${sXyBusinessTripPage.tripHour}'>
						     	 <font color="red">（h）</font>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">真实出差时长</label>
						</td>
					</tr>
					</c:if>
					
					<c:if test="${sXyBusinessTripPage.flowState != '0' }">
					<tr>
						<td align="right">
							<label class="Validform_label">
								申请日期:
							</label>
						</td>
						<td class="value" colspan="3">
									  <input id="applyDate" name="applyDate" type="text" style="width: 150px" class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" 
									  value='<fmt:formatDate value='${sXyBusinessTripPage.applyDate}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/>'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">申请日期</label>
						</td>
					</tr>
					</c:if>
					
					<tr>
						<td align="right">
							<label class="Validform_label">
								出差原因:
							</label>
						</td>
						<td class="value" colspan="3">
						  	 	<textarea id="remarks" style="width:540px; height: 150px;" class="inputxt" rows="6" name="remarks">${sXyBusinessTripPage.remarks}</textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">出差原因</label>
						</td>
					</tr>
					
					<c:if test="${sXyBusinessTripPage.flowState == '5' || sXyBusinessTripPage.flowState == '7' }">
					<tr>
						<td align="right">
							<label class="Validform_label">
								销假原因:
							</label>
						</td>
						<td class="value" colspan="3">
						  	 	<textarea id="backRemarks" style="width:540px; height: 150px;" class="inputxt" rows="6" name="backRemarks">${sXyBusinessTripPage.backRemarks}</textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">销假原因</label>
						</td>
					</tr>
					</c:if>
					
					<tr>
						<td align="right">
							<label class="Validform_label">
								流程状态:
							</label>
						</td>
						<td class="value" colspan="3">
								<c:choose>
									<c:when test="${sXyBusinessTripPage.flowState == '0' }">
										<input id="flowState" name="flowState" type="text" style="width: 150px" class="inputxt"   value='流程未启动'>
									</c:when>
									<c:when test="${sXyBusinessTripPage.flowState == '1' }">
										<input id="flowState" name="flowState" type="text" style="width: 150px" class="inputxt"   value='出差待审批'>
									</c:when>
									<c:when test="${sXyBusinessTripPage.flowState == '2' }">
										<input id="flowState" name="flowState" type="text" style="width: 150px" class="inputxt"   value='出差审批中'>
									</c:when>
									<c:when test="${sXyBusinessTripPage.flowState == '3' }">
										<input id="flowState" name="flowState" type="text" style="width: 150px" class="inputxt"   value='出差申请被否决'>
									</c:when>
									<c:when test="${sXyBusinessTripPage.flowState == '4' }">
										<input id="flowState" name="flowState" type="text" style="width: 150px" class="inputxt"   value='出差审批通过'>
									</c:when>
									<c:when test="${sXyBusinessTripPage.flowState == '5' }">
										<input id="flowState" name="flowState" type="text" style="width: 150px" class="inputxt"   value='销假审批中'>
									</c:when>
									<c:when test="${sXyBusinessTripPage.flowState == '6' }">
										<input id="flowState" name="flowState" type="text" style="width: 150px" class="inputxt"   value='销假申请被否决'>
									</c:when>
									<c:when test="${sXyBusinessTripPage.flowState == '7' }">
										<input id="flowState" name="flowState" type="text" style="width: 150px" class="inputxt"   value='已完成'>
									</c:when>
									<c:when test="${sXyBusinessTripPage.flowState == '8' }">
										<input id="flowState" name="flowState" type="text" style="width: 150px" class="inputxt"   value='已撤销'>
									</c:when>
									<c:when test="${sXyBusinessTripPage.flowState == '9' }">
										<input id="flowState" name="flowState" type="text" style="width: 150px" class="inputxt"   value='已取消'>
									</c:when>
								</c:choose>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">流程状态</label>
						</td>
					</tr>
					
					<c:if test="${sXyBusinessTripPage.flowState != '0'  }">
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