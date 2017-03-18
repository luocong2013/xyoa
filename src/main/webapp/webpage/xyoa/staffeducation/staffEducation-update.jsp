<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>教育经历</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="staffEducationController.do?doUpdate" tiptype="1" >
					<input id="id" name="id" type="hidden" value="${staffEducationPage.id }">
					<input id="creatUser" name="creatUser" type="hidden" value="${staffEducationPage.creatUser }">
					<input id="creatTime" name="creatTime" type="hidden" value="${staffEducationPage.creatTime }">
					<input id="updateUser" name="updateUser" type="hidden" value="${staffEducationPage.updateUser }">
					<input id="updateTime" name="updateTime" type="hidden" value="${staffEducationPage.updateTime }">					
					<input id="staffId" name="staffId"  type="hidden" value="${staffId}" >
						
		<table style="width: 500px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							学校名称:
						</label>
					</td>
					<td class="value">
					     	 <input id="schoolName" name="schoolName" type="text" style="width: 150px" class="inputxt" value='${staffEducationPage.schoolName}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">学校名称</label>
						</td>
					</tr>
					<tr>
					<td align="right">
						<label class="Validform_label">
							专业:
						</label>
					</td>
					<td class="value">
					     	 <input id="major" name="major" type="text" style="width: 150px" class="inputxt"value="${staffEducationPage.major }" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">专业</label>
						</td>
						</tr>
					<tr>
					<td align="right">
						<label class="Validform_label">
							教育形式:
						</label>
					</td>
					<td class="value"><t:dictSelect field="educationWay" type="list"
						typeGroupCode="education_way" defaultVal="${staffEducationPage.educationWay}"
						hasLabel="false" title="教育形式"></t:dictSelect>					     	 
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">教育形式</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							学历:
						</label>
					</td>
					<td class="value"><t:dictSelect field="educationType" type="list"
						typeGroupCode="max_degree" defaultVal="${staffEducationPage.educationType}"
						hasLabel="false" title="学历"></t:dictSelect>					     	 
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">学历</label>
						</td>
					</tr>
				
				<tr>
					<td align="right">
						<label class="Validform_label">
							开始时间:
						</label>
					</td>
					<td class="value">
					     	 <input id="startDate" name="startDate"   style="width: 150px" class="Wdate" type="text" style="width: 150px"
					     	 onClick="WdatePicker()"
							value='${staffEducationPage.startDate}' 
					     	  >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">开始时间</label>
						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							结束时间:
						</label>
					</td>
					<td class="value">
					     	 <input id="endDate" name="endDate"  style="width: 150px" class="Wdate" type="text" style="width: 150px"
					     	  onClick="WdatePicker()" value='${staffEducationPage.endDate}'
							
					     	 
					     	 >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">结束时间</label>
						</td>
				
				<tr>
					<td align="right">
						<label class="Validform_label">
							证明人:
						</label>
					</td>
					<td class="value">
					     	 <input id="certifyName" name="certifyName" type="text" value='${staffEducationPage.certifyName}' style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">证明人</label>
						</td>
				<tr>
					<td align="right">
						<label class="Validform_label">
							证明人电话:
						</label>
					</td>
					<td class="value">
					     	 <input id="tel" name="tel" type="text" value='${staffEducationPage.tel}' style="width: 150px" class="inputxt" >
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">证明人电话</label>
						</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
		
