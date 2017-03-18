<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>工作经历</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="staffWorkExperienceController.do?doAdd" tiptype="1" >
					<input id="staffId" name="staffId"  type="hidden" value="${staffId}" >
					<input id="id" name="id" type="hidden" value="${staffWorkExperiencePage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				
				<tr>
					<td align="right">
						<label class="Validform_label">
							公司名称:
						</label>
					</td>
					<td class="value">
					     	 <input id="companyName" name="companyName" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">公司名称</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							公司规模:
						</label>
					</td>
					<td class="value">
					     <select name="companyCount" id="companyCount" style="width: 155px" >			
							<option value="1">少于50人</option>        
							<option value="2">50-150人</option>							
							<option value="3">150-500人</option> 
							<option value="4">500-1000人</option>        
							<option value="5">1000-5000人</option>							
							<option value="6">5000-10000人</option> 		
							<option value="7">10000人以上</option> 						
													 
			       </select>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">公司规模</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							开始时间:
						</label>
					</td>
					<td class="value">
					     	 <input id="startDate" class="Wdate" onClick="WdatePicker()" name="startDate" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">开始时间</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							离职时间:
						</label>
					</td>
					<td class="value">
					     	 <input id="endDate" name="endDate" class="Wdate" onClick="WdatePicker()" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">结束时间</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							离职原因:
						</label>
					</td>
					<td class="value">
					     	 <input id="leaveReason"  name="leaveReason" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">离职原因</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							薪资（k）:
						</label>
					</td>
					<td class="value">
					     	 <input id="salary" name="salary" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">薪资</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							职位:
						</label>
					</td>
					<td class="value">
					     	 <input id="position" name="position" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">职位</label>
						</td>
						<td align="right">
						<label class="Validform_label">
							工作类型:
						</label>
					</td>
					<td class="value">
					     
							<select name="workType" id="workType" style="width: 155px" >			
							<option value="A">全职</option>        
							<option value="B">兼职</option>							
							<option value="C">实习</option> 							
													 
			       </select>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">工作类型</label>
						</td>
					
					
					</tr>
					<tr>
					<td align="right">
						<label class="Validform_label">
							证明人:
						</label>
					</td>
					<td class="value">
					     	 <input id="certifyName" name="certifyName" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">证明人</label>
						</td>
					<td align="right">
						<label class="Validform_label">
							证明人电话:
						</label>
					</td>
					<td class="value">
					     	 <input id="tel" name="tel" type="text" style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">证明人电话</label>
						</td>
					</tr>
					
				<tr>
				<td align="right"><label class="Validform_label">有保密协议与<br>竞业限制: </label>
				</td>
				<td class="value"> <t:dictSelect field="haveProtocol" type="list"
						typeGroupCode="haveProtocol" hasLabel="false"
						title="保密协议"></t:dictSelect><span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">有保密协议与竞业限制</label></td>
				
				<td align="right"><label class="Validform_label">有未尽发的<br>法律事宜: </label>
				</td>
				<td class="value"><t:dictSelect field="haveLaw" type="list"
						typeGroupCode="haveLaw"  hasLabel="false"
						title="法律事宜"></t:dictSelect> <span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">有未尽发的法律事宜</label></td>
			</tr>
			<tr>
					<td align="right">
						<label class="Validform_label">
							如果有未尽发的法律<br>事宜，具体是:
						</label>
					</td>
					<td class="value" colspan='3'>
					<textarea id="specificLaw" name="specificLaw"  
					  class="inputxt" cols="3" rows="1"  style="width: 460px;height: 50px;"></textarea>
					     	
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">工作内容</label>
						</td>
					</tr>
				
					<tr>
					<td align="right">
						<label class="Validform_label">
							工作内容:
						</label>
					</td>
					<td class="value" colspan='3'>
					<textarea id="workContent" name="workContent"  
					  class="inputxt" cols="3" rows="1"  style="width: 460px;height: 50px;"></textarea>
					     	
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">工作内容</label>
						</td>
					</tr>
				
				
			</table>
		</t:formvalid>
 </body>
