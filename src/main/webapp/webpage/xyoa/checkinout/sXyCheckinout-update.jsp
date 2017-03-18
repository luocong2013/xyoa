<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>考勤统计表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="sXyCheckinoutController.do?doUpdate" tiptype="1" >
					<input id="id" name="id" type="hidden" value="${sXyCheckinoutPage.id }">
					<input id="staffId" name="staffId" type="hidden" value="${sXyCheckinoutPage.staffId }">
					<input id="deptId" name="deptId" type="hidden" value="${sXyCheckinoutPage.deptId }">
					<input id="applyId" name="applyId" type="hidden" value="${sXyCheckinoutPage.applyId }">
					<%-- <input id="cUser" name="cUser" type="hidden" value="${sXyCheckinoutPage.cUser }"> --%>
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								考勤人员:
							</label>
						</td>
						<td class="value">
									  <input id="name" name="name" readonly="readonly" type="text" style="width: 150px" class="inputxt"  value='${sXyCheckinoutPage.name}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">考勤人员</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								归属部门:
							</label>
						</td>
						<td class="value">
							<input id="departname" datatype="*" readonly="readonly" type="text" style="width: 150px"  value='${departname}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">归属部门</label>
						</td>
						
					</tr>
					
					<tr>
					<td align="right">
						<label class="Validform_label">
								日期类型:
							</label>
						</td>
						<td class="value">
							<t:dictSelect readonly="readonly"  field="dateType" type="list" 
													typeGroupCode="dateType" defaultVal="${sXyCheckinoutPage.dateType}" hasLabel="false"  title="日期类型"></t:dictSelect> 
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">日期类型</label>
						</td>
						
					<td align="right">
							<label class="Validform_label">
								考勤日期:
							</label>
						</td>
						<td class="value">
							<input id="checkDate" name="checkDate" readonly="readonly" type="text" style="width: 150px"  value='${sXyCheckinoutPage.checkDate}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">考勤日期</label>
						</td>
						
					
					</tr>
					<tr>
					<td align="right">
							<label class="Validform_label">
								上班时间:
							</label>
						</td>
						<td class="value">
						     	 <input id="workTime" name="workTime" readonly="readonly" type="text" style="width: 150px" class="inputxt"  value='${sXyCheckinoutPage.workTime}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">上班时间</label>
						</td>
						
						<td align="right">
							<label class="Validform_label">
								下班时间:
							</label>
						</td>
						<td class="value">
						     	 <input id="offWorkTime" name="offWorkTime" readonly="readonly" type="text" style="width: 150px" class="inputxt"  value='${sXyCheckinoutPage.offWorkTime}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">下班时间</label>
						</td>
						
					</tr>
					<tr>
				
					<td align="right">
							<label class="Validform_label">
								异常分钟数:
							</label>
						</td>
						<td class="value">
						     	 <input id="exceptionMinute" name="exceptionMinute" readonly="readonly" type="text" style="width: 150px" class="inputxt"  value='${sXyCheckinoutPage.exceptionMinute}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">异常分钟数</label>
						</td>
							
						
						<td align="right">
							<label class="Validform_label">
								加班时长:
							</label>
						</td>
						<td class="value">
						     	 <input id="workHour" name="workHour" readonly="readonly" type="text" style="width: 150px" class="inputxt"  value='${sXyCheckinoutPage.workHour}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">加班时长</label>
						</td>
					
					</tr>
				
					
					<tr>
					<td align="right">
					<label class="Validform_label">
								考勤是否异常:
							</label>
						</td>
						<td class="value">
						     	<t:dictSelect  field="isCheckTrue" type="list" 
													typeGroupCode="isCheckTrue" defaultVal="${sXyCheckinoutPage.isCheckTrue}" hasLabel="false"  title="考勤类型"></t:dictSelect> 
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">考勤是否异常</label>
						</td>
						
						<td align="right">
							<label class="Validform_label">
								考勤类型:
							</label>
						</td>
						<td class="value">
						
						<t:dictSelect  field="checkType" type="list" 
													typeGroupCode="checkType" defaultVal="${sXyCheckinoutPage.checkType}" hasLabel="false"  title="考勤类型"></t:dictSelect> 
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">考勤类型</label>
						</td>
						
					</tr>
					
					
					<tr >
					<td align="right">
							<label class="Validform_label">
								考勤原因:
							</label>
						</td>
						<td class="value" colspan="3">
						<textarea style="width:100%; height: 150px;" class="inputxt" rows="3" id="checkRemarks" name="checkRemarks" 
						     	 >${sXyCheckinoutPage.checkRemarks}</textarea>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">考勤原因</label>
						</td>
											
					</tr>
					
					
					<%-- <tr>
						<td align="right">
							<label class="Validform_label">
								创建时间:
							</label>
						</td>
						<td class="value">
									  <input id="cTime" name="cTime" type="text" style="width: 150px"  class="Wdate" onClick="WdatePicker()" value='<fmt:formatDate value='${sXyCheckinoutPage.cTime}' type="date" pattern="yyyy-MM-dd"/>'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">创建时间</label>
						</td> 
				<td align="right">
					<label class="Validform_label">
					</label>
				</td>
				<td class="value">
				</td>
					</tr>--%>
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/xyoa/checkinout/sXyCheckinout.js"></script>		
