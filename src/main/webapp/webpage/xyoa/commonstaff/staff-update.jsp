<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>享宇员工表</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<style type="text/css">
.button { 
display: inline-block; 
zoom: 1; /* zoom and *display = ie7 hack for display:inline-block */ 
*display: inline; 
vertical-align: baseline; 
margin: 0 2px; 
outline: none; 
cursor: pointer; 
text-align: center; 
text-decoration: none; 
font: 14px/100% Arial, Helvetica, sans-serif; 
padding: .5em 2em .55em; 
text-shadow: 0 1px 1px rgba(0,0,0,.3); 
-webkit-border-radius: .5em; 
-moz-border-radius: .5em; 
border-radius: .5em; 
-webkit-box-shadow: 0 1px 2px rgba(0,0,0,.2); 
-moz-box-shadow: 0 1px 2px rgba(0,0,0,.2); 
box-shadow: 0 1px 2px rgba(0,0,0,.2); 
} 
.button:hover { 
text-decoration: none; 
} 
.button:active { 
position: relative; 
top: 1px; 
} 
.bigrounded { 
-webkit-border-radius: 2em; 
-moz-border-radius: 2em; 
border-radius: 2em; 
} 
.medium { 
font-size: 12px; 
padding: .4em 1.5em .42em; 
} 

.blue { 
color: #d9eef7; 
border: solid 1px #0076a3; 
background: #0095cd; 
background: -webkit-gradient(linear, left top, left bottom, from(#00adee), to(#0078a5)); 
background: -moz-linear-gradient(top, #00adee, #0078a5); 
filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#00adee', endColorstr='#0078a5'); 
} 
.blue:hover { 
background: #007ead; 
background: -webkit-gradient(linear, left top, left bottom, from(#0095cc), to(#00678e)); 
background: -moz-linear-gradient(top, #0095cc, #00678e); 
filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#0095cc', endColorstr='#00678e'); 
} 
.blue:active { 
color: #80bed6; 
background: -webkit-gradient(linear, left top, left bottom, from(#0078a5), to(#00adee)); 
background: -moz-linear-gradient(top, #0078a5, #00adee); 
filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#0078a5', endColorstr='#00adee'); 
} 
</style>
<script type="text/javascript">
  //编写自定义JS代码
  </script>
</head>
<body >
<input id="loadtype"  type="hidden" value="${loadtype}">
<div class="easyui-tabs" id="stafftabs" style="width:692px;height:auto;margin:auto" >
		<div title="个人信息" style="padding:10px;">
				
	<t:formvalid formid="formobj" dialog="true" usePlugin="password"
		layout="table" action="commonstaffController.do?doUpdate" tiptype="1">
		<input id="id" name="id" type="hidden" value="${staffPage.id }">
		<table style="width: 680px;margin:auto" cellpadding="0" cellspacing="1"
			class="formtable">
			<tr>
				<td align="right"><label class="Validform_label"> 归属公司:
				</label></td>
				<td class="value"><select id="companyId" name="companyId" disabled="true">						
						<c:forEach items="${TSDeparts}" var="list">
							<option value="${list.id}">${list.departname}</option>
						</c:forEach>
				</select> <span class="Validform_checktip"><font color="red">*</font></span> <label
					class="Validform_label" style="display: none;">归属公司</label></td>
				<td align="right"><label class="Validform_label"> 部门: </label>
				</td>
				<td class="value"> <input id="departname"  type="text" disabled="true" readonly="readonly" class="inputxt"  value="${departname}">
                <input id="deptId" name="deptId"  type="hidden" disabled="true" value='${staffPage.deptId}' >
              <!--    <a href="#" class="easyui-linkbutton" plain="true" icon="icon-search" id="departSearch" onclick="openDepartmentSelect()">选择</a> -->
             <!--   <a href="#" class="easyui-linkbutton" plain="true" icon="icon-redo" id="departRedo" onclick="callbackClean()">清空</a>
                --> 
					<span class="Validform_checktip"><font color="red">*</font></span> <label
					class="Validform_label" style="display: none;">部门</label></td>
			</tr>
			<tr>
			<td align="right"><label class="Validform_label"> 岗位: </label>
				</td>
				<td class="value">
						<select name="jobNo" id="jobNo"  disabled="true">					
						<c:forEach items="${deptjobs}" var="list">
							<option value="${list.id}">${list.jobName}</option>
						</c:forEach>
				</select> 
						 <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">岗位</label></td>
				<td align="right"><label class="Validform_label"> <t:mutiLang langKey="common.role"/>: </label></td>
				<td class="value" nowrap>
                <input name="roleid" name="roleid"  type="hidden" disabled="true" value="${id}" id="roleid">
                <input name="roleName" class="inputxt" disabled="true" value="${roleName }" id="roleName" readonly="readonly"   />
              <!-- <a href="#" class="easyui-linkbutton" plain="true" icon="icon-search" id="roleidSearch"  onclick="openroleidSelect()">选择</a>
             -->
             <%-- <t:choose hiddenName="roleid" hiddenid="id" url="userController.do?roles" name="roleList"
                          icon="icon-search" title="common.role.list" textname="roleName" isclear="false" isInit="true"></t:choose>
                 --%><span class="Validform_checktip"> <font color="red">*</font></span>
            	</td>
				
			</tr>
			<tr>
			<td align="right"><label class="Validform_label"> 员工编号:
				</label></td>
				<td class="value"><input id="sttaffId" name="sttaffId" disabled="disabled" disabled="true"
					type="text" style="width: 150px;"class="inputxt" 
					value='${staffPage.sttaffId}'><span
					class="Validform_checktip"></span> <label class="Validform_label" style="display: none;">员工编号</label></td>
				
				<td align="right"><label class="Validform_label"> 状态: </label>
				</td>
				<td class="value"><select name="state" id="state" onchange="showrstate(this)" disabled="true">
						
							<option value="2">试用期</option>        
							<option value="1">已转正</option>							
									
							<option value="4">兼职</option>
							<option value="3">其它</option>	
							<option value="5">外包</option>	
							<option value="6">实习</option>									 
			       </select><span
					class="Validform_checktip"><font color="red">*</font></span> <label class="Validform_label"
					style="display: none;">状态</label></td>
				
			</tr>
			<tr>
				<td align="right"><label class="Validform_label"> 姓名: </label>
				</td>
				<td class="value"><input id="name" name="name" type="text"  
					style="width: 150px" class="inputxt" value='${staffPage.name}' disabled="true">
					<span class="Validform_checktip"><font color="red">*</font></span> <label
					class="Validform_label" style="display: none;">姓名</label></td>
				<td align="right"><label class="Validform_label"> 性别: </label>
				</td>
				<td class="value"><t:dictSelect field="sex" type="list"
						typeGroupCode="sex" defaultVal="${staffPage.sex}" hasLabel="false"
						title="性别"></t:dictSelect> <span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">性别</label></td>
			</tr>
		
			<tr>	
				<td align="right"><label class="Validform_label"> 民族: </label>
				</td>
				<td class="value"><t:dictSelect field="nation" type="list"
						typeGroupCode="nation" defaultVal="${staffPage.nation}"
						hasLabel="false" title="民族"></t:dictSelect> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">民族</label></td>					
			<td align="right"><label class="Validform_label">是否外包人员:
				</label></td>
				<td class="value"><select name="isOutsource" id="isOutsource" disabled="true" >
						
							<option value="A">是</option>         
							<option value="B">否</option>
							
				       </select> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">是否外包人员</label></td>
			
			</tr>
			 
			<tr>
					<td align="right"><label class="Validform_label"> 是否考勤:
				</label></td>
				<td class="value">				
					<select id="isCheck" name="isCheck"    onchange="showisCheck(this)"  disabled="true">	
							<option value="false">否</option>			
							<option value="true">是</option>
														
				       </select>
					<span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">是否考勤</label></td>
				<td align="right"><label class="Validform_label"> 考勤ID:
				</label></td>
				<td class="value"><input id="checkId" name="checkId" value='${staffPage.checkId}' disabled="true" onblur="getcheck()"
					type="text" style="width: 150px" class="inputxt"><input id="checkidflag"   type="hidden"  >  <span
					class="Validform_checktip"> <font id="checkIdcheck" color="red">*</font></span> <label class="Validform_label"
					style="display: none;">考勤ID</label></td>
			</tr>
			<tr>
				<td align="right"><label class="Validform_label"> 最高学历:
				</label></td>
				<td class="value">
				<select name="maxDegree" id="maxDegree" disabled="true">
							<option value="0">本科</option>   
							<option value="1">专科</option>  
							<option value="2">高中</option> 
							<option value="3">硕士</option>
							<option value="4">博士</option>
							<option value="5">本科在读</option>   
							<option value="6">硕士研究生在读</option>   
							<option value="7">博士研究生在读</option>   
							<option value="8">其它</option>   							           								 
			       </select>
				<span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">最高学历</label></td>
				<td align="right"><label class="Validform_label"> 有无留学经历:
				</label></td>
				<td class="value">
				<select name="isStudyAbroad" id="isStudyAbroad" disabled="true">
							<option value="1">有</option>   
							<option value="2">无</option>  
													           								 
			       </select>
				 <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">留学经历</label></td>
			</tr>
			<tr>
				
				<td align="right"><label class="Validform_label"> 毕业院校:
				</label></td>
				<td class="value"><input id="fromSchool" name="fromSchool" disabled="true"
					type="text" style="width: 150px" class="inputxt"
					value='${staffPage.fromSchool}'> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">毕业院校</label></td>
					
					<td align="right"><label class="Validform_label"> 专业: </label>
				</td>
				<td class="value"><input id="major" name="major" type="text" disabled="true"
					style="width: 150px" class="inputxt" value='${staffPage.major}'>
					<span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">专业</label></td>
			</tr>
			<tr>
				
				<td align="right"><label class="Validform_label"> 毕业时间:
				</label></td>
				<td class="value"><input id="graduationDate"   disabled="true"
					name="graduationDate" type="text" style="width: 150px" 
					class="Wdate" onClick="WdatePicker()"
					value='<fmt:formatDate value='${staffPage.graduationDate}' type="date" pattern="yyyy-MM-dd"/>'>
					<span class="Validform_checktip"><font color="red">*</font></span> <label
					class="Validform_label" style="display: none;">毕业时间</label></td>
					
					<td align="right"><label class="Validform_label"> 工作年限:
				</label></td>
				<td class="value"><input id="workYear" name="workYear" disabled="true"
					type="text" style="width: 150px" class="inputxt"
					value='${staffPage.workYear}'> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">工作年限</label></td>
					
			</tr>
			
			<tr>
				
				<td align="right"><label class="Validform_label">
						入本单位日期: </label></td>
				<td class="value"><input id="goXyDate" name="goXyDate"   disabled="true"
					type="text" style="width: 150px" class="Wdate"
					onClick="WdatePicker()"
					value='<fmt:formatDate value='${staffPage.goXyDate}' type="date" pattern="yyyy-MM-dd"/>'>
					<span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">入本单位日期</label></td>
					<td align="right"><label class="Validform_label">
						正式转正日期: </label></td>
				<td class="value"><input id="positiveDate" name="positiveDate"    disabled="true"
					type="text" style="width: 150px" class="Wdate"
					onClick="WdatePicker()"
					value='<fmt:formatDate value='${staffPage.positiveDate}' type="date" pattern="yyyy-MM-dd"/>'>
					<span class="Validform_checktip"><font id="stateusercheck" color="red">*</font> </span> <label
					class="Validform_label" style="display: none;">正式转正日期</label></td>
			</tr>
			<tr>
				<td align="right"><label class="Validform_label">
						试用期开始时间: </label></td>
				<td class="value"><input id="trialStartData"   disabled="true"
					name="trialStartData" type="text" style="width: 150px"
					class="Wdate" onClick="WdatePicker()"
					value='<fmt:formatDate value='${staffPage.trialStartData}' type="date" pattern="yyyy-MM-dd"/>'>
					<span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">试用期开始时间</label></td>
				<td align="right"><label class="Validform_label">
						试用期结束时间: </label></td>
				<td class="value"><input id="trialEndData" name="trialEndData"  disabled="true"
					type="text" style="width: 150px" class="Wdate"
					onClick="WdatePicker()"
					value='<fmt:formatDate value='${staffPage.trialEndData}' type="date" pattern="yyyy-MM-dd"/>'>
					<span class="Validform_checktip"><font id="statecheck" color="red">*</font> </span> <label
					class="Validform_label" style="display: none;">试用期结束时间</label></td>
			</tr>
			<tr>
				
				<td align="right"><label class="Validform_label">
						劳动合同开始时间: </label></td>
				<td class="value"><input id="contractStartDate"  disabled="true"
					name="contractStartDate" type="text" style="width: 150px"
					class="Wdate" onClick="WdatePicker()"
					value='<fmt:formatDate value='${staffPage.contractStartDate}' type="date" pattern="yyyy-MM-dd"/>'>
					<span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">劳动合同开始时间</label></td>
						<td align="right"><label class="Validform_label">
						劳动合同结束时间: </label></td>
				<td class="value"><input id="contractEndDate"  disabled="true"
					name="contractEndDate" type="text" style="width: 150px"
					class="Wdate" onClick="WdatePicker()"
					value='<fmt:formatDate value='${staffPage.contractEndDate}' type="date" pattern="yyyy-MM-dd"/>'>
					<span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">劳动合同结束时间</label></td>
			</tr>
			<tr>
			<td align="right"><label class="Validform_label">
						可调休天数: </label></td>
				<td class="value"><input id="offWorkCount" name="offWorkCount"  disabled="true"
					type="text" style="width: 150px" class="inputxt"
					value='${staffPage.offWorkCount}'> <span
					class="Validform_checktip"> <font  color="red">*</font></span> <label class="Validform_label"
					style="display: none;">可调休天数</label></td>
					
				<td align="right"><label class="Validform_label">
						年假可用天数: </label></td>
				<td class="value"><input id="leaveCount" name="leaveCount" disabled="true"
					type="text" style="width: 150px" class="inputxt"
					value='${staffPage.leaveCount}'> <span
					class="Validform_checktip"> <font  color="red">*</font></span> <label class="Validform_label"
					style="display: none;">年假可用天数</label></td>
			</tr>
			<tr>
				
				<td align="right"><label class="Validform_label"> 手机: </label>
				</td>
				<td class="value"><input id="mobile" name="mobile" type="text"
					style="width: 150px" class="inputxt" value='${staffPage.mobile}'>
					<span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">手机</label></td>
					<td align="right"><label class="Validform_label">
						email: </label></td>
				<td class="value"><input id="email" name="email" type="text"
					style="width: 150px" class="inputxt" value='${staffPage.email}'>
					<span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">email</label></td>
			</tr>
			
			<tr>
				<td align="right"><label class="Validform_label"> 婚姻状况:
				</label></td>
				<td class="value"><t:dictSelect field="marryState" type="list"
						typeGroupCode="marry_sta" defaultVal="${staffPage.marryState}"
						hasLabel="false" title="婚姻状况"></t:dictSelect> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">婚姻状况</label></td>
				<td align="right"><label class="Validform_label">
						身份证号码: </label></td>
				<td class="value"><input id="certNo" name="certNo" type="text" disabled="true"  onblur="getAge();"
					style="width: 150px" class="inputxt" value='${staffPage.certNo}'> 
					<span class="Validform_checktip"> <font  color="red">*</font></span> <label
					class="Validform_label" style="display: none;">身份证号码</label></td>
			</tr>
			<tr>
			<td align="right"><label class="Validform_label"> 出生日期:
				</label></td>
				<td class="value"><input id="birthday" name="birthday"
					type="text" style="width: 150px" class="Wdate"
					onClick="WdatePicker()"
					value='<fmt:formatDate value='${staffPage.birthday}' type="date" pattern="yyyy-MM-dd"/>'>
					<span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">出生日期</label></td>
			 <td align="right"><label class="Validform_label"> 年龄: </label>
				</td>
				<td class="value"><input id="age" name="age" type="text"
					style="width: 150px" class="inputxt" value='${staffPage.age}'>
					<span class="Validform_checktip"><font  color="red">*</font></span> <label
					class="Validform_label" style="display: none;">年龄</label></td>
			
					 
			</tr>
			<tr>
			<td align="right"><label class="Validform_label"> 户籍类型:
				</label></td>
				<td class="value"><t:dictSelect field="registerType"
						type="list" typeGroupCode="register_t"
						defaultVal="${staffPage.registerType}" hasLabel="false"
						title="户籍类型"></t:dictSelect> <span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">户籍类型</label>
				</td>
				<td align="right"><label class="Validform_label">
						户籍所在地: </label></td>
				<td class="value"><input id="registerAddr" name="registerAddr"
					type="text" style="width: 150px" class="inputxt"
					value='${staffPage.registerAddr}'> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">户籍所在地</label></td>
				
			</tr>
			<tr>
				<td align="right"><label class="Validform_label"> 家庭住址:
				</label></td>
				<td class="value"   ><input id="addr" name="addr" type="text" 
					style="width: 150px" class="inputxt" value='${staffPage.addr}'>
					<span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">家庭住址</label></td>
	 			
					<td align="right"><label class="Validform_label"> 简历链接:
				</label></td>
				<td class="value"><input id="cvUrl" name="cvUrl" type="text" disabled="true"
					style="width: 150px" class="inputxt" value='${staffPage.cvUrl}'>
					<span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">简历链接</label></td>
		
			</tr>
			
			<tr>
			<td align="right"><label class="Validform_label"> 紧急联系人:
				</label></td>
				<td class="value"><input id="linkmanName" name="linkmanName"
					type="text" style="width: 150px" class="inputxt"
					value='${staffPage.linkmanName}'> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">紧急联系人</label></td>
				<td align="right"><label class="Validform_label">
						紧急联系电话: </label></td>
				<td class="value"><input id="linkmanTel" name="linkmanTel"
					type="text" style="width: 150px" class="inputxt"
					value='${staffPage.linkmanTel}'> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">紧急联系电话</label></td>
				
			</tr>
			<tr>
			<td align="right"><label class="Validform_label"> 紧急联系人关系:
				</label></td>
				<td class="value"><input id="linkmanRelative" name="linkmanRelative"
					type="text" style="width: 150px" class="inputxt" value='${staffPage.linkmanRelative}'> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">紧急联系人关系</label></td>
				<td align="right"><label class="Validform_label">
						司龄（月）: </label></td>
				<td class="value"><input id="siling" name="siling"  disabled="true"
					type="text" style="width: 150px" class="inputxt" value='${staffPage.siling}'> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">司龄</label></td>
				
			</tr>
			<tr>
				
				<td align="right"><label class="Validform_label"> 社保账号:
				</label></td>
				<td class="value"><input id="socialSecurityNo"
					name="socialSecurityNo" type="text" style="width: 150px"
					class="inputxt" value='${staffPage.socialSecurityNo}'> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">社保账号</label></td>
					<td align="right"><label class="Validform_label">
						公积金账号: </label></td>
				<td class="value"><input id="fundNo" name="fundNo" type="text"
					style="width: 150px" class="inputxt" value='${staffPage.fundNo}'>
					<span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">公积金账号</label></td>
			</tr>
				
			<tr>
			<td align="right"><label class="Validform_label"> 是否伤残:
				</label></td>
				<td class="value"><t:dictSelect field="isDistressed" readonly="readonly"
						type="list" typeGroupCode="isDistressed"
						defaultVal="${staffPage.isDistressed}" hasLabel="false"
						title="是否伤残"></t:dictSelect><span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">是否伤残</label>
				</td>
				<td align="right"><label class="Validform_label">
						伤残等级: </label></td>
				<td class="value"><t:dictSelect field="distressedLevel" readonly="readonly"
						type="list" typeGroupCode="distressedLevel"
						defaultVal="${staffPage.distressedLevel}" hasLabel="false"
						title="伤残等级"></t:dictSelect> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">伤残等级</label></td>
				
			</tr>	
			<tr>
			<td align="right"><label class="Validform_label"> 是否有传染病:
				</label></td>
				<td class="value"><t:dictSelect field="isSick" readonly="readonly"
						type="list" typeGroupCode="isSick"
						defaultVal="${staffPage.isSick}" hasLabel="false"
						title="是否有传染病"></t:dictSelect><span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">是否有传染病</label>
				</td>
				<td align="right"><label class="Validform_label">
						如果有，是何传染病: </label></td>
				<td class="value"><input id="sick" name="sick" disabled="true" type="text" value="${staffPage.sick}"
					style="width: 150px" class="inputxt"> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">如果有，是何传染病</label></td>
				
			</tr>	
			<tr>
			<td align="right"><label class="Validform_label"> 是否曾犯罪:
				</label></td>
				<td class="value"><t:dictSelect field="isCommit" readonly="readonly"
						type="list" typeGroupCode="isCommit"
						defaultVal="${staffPage.isCommit}" hasLabel="false"
						title="是否曾犯罪"></t:dictSelect> <span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">是否曾犯罪</label>
				</td>
				<td align="right"><label class="Validform_label">
						如果有，犯何罪: </label></td>
				<td class="value"><input id="commit" name="commit" disabled="true"  type="text" value="${staffPage.commit}"
					style="width: 150px" class="inputxt"> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">如果有，犯何罪</label></td>
				
			</tr>	
					
			<tr>
				<td align="right"><label class="Validform_label"> 招聘来源:
				</label></td>
				<td class="value"><select name="staffSource" id="staffSource" disabled="true" onchange="showreffers(this)">
						
							<option value="0">智联招聘</option>
							<option value="1">拉勾网</option>
							<option value="2">前程无忧</option>
							<option value="3">猎聘网</option>
							<option value="4">Boss直聘</option>							
							<option value="15">内部推荐</option>
							<option value="6">其他</option>
				       </select> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">招聘来源</label></td>
				<td align="right"><label class="Validform_label"> 推荐人:
				</label></td>
				<td class="value">
				
				 <input id="referencename"  disabled="true" type="text" readonly="readonly" class="inputxt" style="width: 150px;display: none;" value='${staffPage.referenceId}'>
                <input id="referenceId"  disabled="true" name="referenceId"  type="hidden"  value='${staffPage.referenceId}' >
             <a href="#" class="easyui-linkbutton" plain="true" icon="icon-search" id="referenceIdSearch" style="display: none;" onclick="openreferenceIdSelect()">选择</a>
                 <!-- <a href="#" class="easyui-linkbutton" plain="true" icon="icon-redo" id="referenceIdRedo" style="display: none;"  onclick="referenceIdcallbackClean()">清空</a>
 -->
					<span class="Validform_checktip"> <font id="referenceIdcheck" color="red">*</font></span> <label
					class="Validform_label" style="display: none;">推荐人</label></td>
			</tr>
			
			<tr>
				<td align="right"><label class="Validform_label"> 备注: </label>
				</td>
				<td class="value" colspan='3'><textarea id="remarks" name="remarks" disabled="true" 
					  class="inputxt" cols="3" rows="1"  style="width: 460px;height: 50px;">${staffPage.remarks}</textarea> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">备注</label></td>
			</tr>
			<!-- 
			<tr>
				
				<td class="value" colspan='4' style=" text-align: center;"><input type="submit"  class="button blue medium" value="&nbsp;&nbsp;提 &nbsp;交 &nbsp;&nbsp;"></td>
			</tr> -->
		</table>
		
	</t:formvalid>
	
		</div>
		<div title="教育经历" style="padding:10px">
	<table id="education_Table"></table>
	</div>
		<div title="电子档案" style="padding:10px">
		<!-- <iframe class="J_iframe" name="iframe10" width="100%" height="100%" src="webpage/xyoa/files/filesListTab.jsp" frameborder="0" data-id="webpage/xyoa/files/filesListTab.jsp" seamless></iframe>
	 -->	
	<div>
			
			&#8195;&#8195;类型: 
			<t:dictSelect id="documenttype" field="documenttype" type="list"
						typeGroupCode="documenttype" defaultVal="${doc.documenttype}" hasLabel="false"
						title="上传文件类型"></t:dictSelect>
			
			&#8195;&#8195;标题: 
			<input name="documentTitle" id="documentTitle" type="text" style="width: 140px;">&#8195;&#8195;&#8195;
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="filesSelect()">查询</a>
		</div>
	 <table id="sheduletime_Table"></table>  			
	
	</div>
<div title="工作经历" style="padding:10px">
	<table id="exprience_Table"></table>
	</div>
<div title="家庭成员" style="padding:10px">
	<table id="home_member_Table"></table>
	</div>
<div title="培训经历" style="padding:10px">
	<table id="train_Table"></table>
	</div>

</div>	
</body>
 <script type="text/javascript">
//删除教育经历调用函数
 function delEducationObj(id) {
 	gridname=name;
 	url="staffEducationController.do?doDel&id="+id;
 	createdialog('删除确认 ', '确定删除该文件吗 ?', url,name);
 	
 }
//删除调用函数
 function delObj(id) {
 	gridname=name;
 	url="systemController.do?delDocument&id="+id;
 	createdialog('删除确认 ', '确定删除该文件吗 ?', url,name);
 	
 }
//修改调用函数
 function updateObj(id) {
	 addurl="staffEducationController.do?goUpdate&staffId=${staffPage.sttaffId}&id="+id;
 	addeducation("修改经历",addurl,510,320);
 	
 	
 }
//查看调用函数
 function detailObj(id) {
 	gridname=name;
 	 addurl="staffEducationController.do?goUpdate&load=detail&staffId=${staffPage.sttaffId}&id="+id;
 	createdetailwindow("查看经历",addurl,510,320);
 	
 }
//删除工作经验调用函数
 function delExprienceObj(id) {
 	gridname=name;
 	url="staffWorkExperienceController.do?doDel&id="+id;
 	createdialog('删除确认 ', '确定删除该文件吗 ?', url,name);
 	
 }
//修改工作经验调用函数
 function updateExprienceObj(id) {
	 addurl="staffWorkExperienceController.do?goUpdate&staffId=${staffPage.sttaffId}&id="+id;
 	addeducation("修改经历",addurl,610,470);
 	
 	
 }
//查看工作经验调用函数
 function detailExprienceObj(id) {
 	gridname=name;
 	 addurl="staffWorkExperienceController.do?goUpdate&load=detail&staffId=${staffPage.sttaffId}&id="+id;
 	createdetailwindow("查看经历",addurl,610,470);
 	
 }
 //下载调用函数
 function downObj(id,subclassname) {
	 
 	gridname=name;
 	url="commonController.do?viewFile&fileid="+id+"&subclassname="+subclassname;
 	 try{ 
         var elemIF = document.createElement("iframe");   
         elemIF.src = url;   
         elemIF.style.display = "none";   
         document.body.appendChild(elemIF);   
     }catch(e){ 

     } 	
 }

 function doSubmit(url,name,data) {
		gridname=name;
		//--author：JueYue ---------date：20140227---------for：把URL转换成POST参数防止URL参数超出范围的问题
		var paramsData = data;
		if(!paramsData){
			paramsData = new Object();
			if (url.indexOf("&") != -1) {
				var str = url.substr(url.indexOf("&")+1);
				url = url.substr(0,url.indexOf("&"));
				var strs = str.split("&");
				for(var i = 0; i < strs.length; i ++) {
					paramsData[strs[i].split("=")[0]]=(strs[i].split("=")[1]);
				}
			}      
		}
		//--author：JueYue ---------date：20140227---------for：把URL转换成POST参数防止URL参数超出范围的问题
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			data : paramsData,
			url : url,// 请求的action路径
			error : function() {// 请求失败处理函数
			},
			success : function(data) {
				var d = $.parseJSON(data);
				if (d.success) {
					var msg = d.msg;
					tip(msg);
					$('#sheduletime_Table').datagrid('reload'); 
					$('#education_Table').datagrid('reload'); 
					$('#exprience_Table').datagrid('reload'); 
					$('#train_Table').datagrid('reload');
					$('#home_member_Table').datagrid('reload');
				}
			}
		});
		
		
	}
 //文件查询
 function filesSelect()
 {
	var documenttype =$("#documenttype").val();
	var documentTitle= $("#documentTitle").val();
	 GetTable('systemController.do?documentTabList&staffid=${staffPage.id }&typecode=files&field=id,documenttype,documentTitle,createdate,subclassname,&documenttype='+documenttype+'&documentTitle='+documentTitle);  
 }
	$(document).ready(function(){  
		
		GetTable('systemController.do?documentTabList&isNotDel=1&staffid=${staffPage.id }&typecode=files&field=id,documenttype,documentTitle,createdate,subclassname,');  
		GetedcationTable('staffEducationController.do?educationdatagrid&isNotDel=1&staffId=${staffPage.sttaffId }&field=id,staffId,educationType,major,schoolName,startDate,endDate,certifyName,tel,');
		GetexperienceTable('staffWorkExperienceController.do?experiencedatagrid&isNotDel=1&staffId=${staffPage.sttaffId }&field=id,staffId,workType,startDate,endDate,companyName,companyCount,position,workContent,certifyName,tel,');
		GethomememberTable('sXyStaffHomeMemberController.do?homedatagrid&isNotDel=1&staffId=${staffPage.sttaffId }&field=id,name,relative,age,workUnit,workDuty,tel,');
		GettrainTable('sXyStaffTrainController.do?traindatagrid&isNotDel=1&staffId=${staffPage.sttaffId }&field=id,name,relative,age,workUnit,workDuty,tel,');
		
	}); 
	//初始化家庭成员列表
	function GethomememberTable(url) {
    var editRow = undefined;
    $("#home_member_Table").datagrid({
       // height: 300,
     //   width: 450,
        title: '',
 //       collapsible: true,
 		rownumbers: true,
        singleSelect: true,
        pagination:true,
		pageSize:10,
        url: url,
       
        columns:[[{field:'id',hidden:true,title:'id'},
		
        {field:'name',title:'姓名',width:90}
        ,
		{field:'relative',title:'关系',width:80}      
        ,
        {field:'age',width:30 ,title:'年龄'}
        ,
		{field:'workUnit',title:'工作单位',width:110}
        ,
        {field:'workDuty',title:'职务',width:90}
        ,
        {field:'tel',title:'联系电话',width:81}
       
        /*,
        {field:'tel',title:'证明人电话',width:90} */
        ,
		{field:'update',title:'操作',width:137,colspan:3,align:'center'},
       
		]]
	,
        toolbar: [  '-', {
            text: '添加成员', iconCls: 'icon-save', handler: function () {
            	addurl="sXyStaffHomeMemberController.do?goAdd&staffId=${staffPage.sttaffId}";
            	addeducation("添加家庭成员",addurl,510,280);
				}
 
            
        }],
        onAfterEdit: function (rowIndex, rowData, changes) {
            editRow = undefined;
        },
        onDblClickRow: function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#home_member_Table").datagrid('endEdit', editRow);
            }
 
            if (editRow == undefined) {
                $("#home_member_Table").datagrid('beginEdit', rowIndex);
                editRow = rowIndex;
            }
        },
        onClickRow: function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#home_member_Table").datagrid('endEdit', editRow);
 
            }
 
        }
 
    });
} 
	//初始化培训经历列表
	function GettrainTable(url) {
    var editRow = undefined;
    $("#train_Table").datagrid({
       // height: 300,
     //   width: 450,
        title: '',
 //       collapsible: true,
 		rownumbers: true,
        singleSelect: true,
        pagination:true,
		pageSize:10,
        url: url,
       
        columns:[[{field:'id',hidden:true,title:'id'},
		
        {field:'className',title:'课程名称',width:125}
        ,
		{field:'trainOrganization',title:'培训机构',width:125}      
        ,
        {field:'startTime',width:90 ,title:'时间'}
        ,
        {field:'certificate',width:125 ,title:'所获证书'}
        ,
		{field:'update',title:'操作',width:137,colspan:3,align:'center'},
       
		]]
	,
        toolbar: [  '-', {
            text: '添加经历', iconCls: 'icon-save', handler: function () {
            	addurl="sXyStaffTrainController.do?goAdd&staffId=${staffPage.sttaffId}";
            	addeducation("添加培训经历",addurl,510,200);
				}
 
            
        }],
        onAfterEdit: function (rowIndex, rowData, changes) {
            editRow = undefined;
        },
        onDblClickRow: function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#train_Table").datagrid('endEdit', editRow);
            }
 
            if (editRow == undefined) {
                $("#train_Table").datagrid('beginEdit', rowIndex);
                editRow = rowIndex;
            }
        },
        onClickRow: function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#train_Table").datagrid('endEdit', editRow);
 
            }
 
        }
 
    });
}   
	//初始化工作经历列表
	function GetexperienceTable(url) {
    var editRow = undefined;
    $("#exprience_Table").datagrid({
       // height: 300,
     //   width: 450,
        title: '',
 //       collapsible: true,
 		rownumbers: true,
        singleSelect: true,
        pagination:true,
		pageSize:10,
        url: url,
       
        columns:[[{field:'id',hidden:true,title:'id'},
		
        {field:'companyName',title:'公司名称',width:125}
        ,
		{field:'companyCount',title:'公司规模',width:80,formatter: function(value){return formatepCompanyCount(value);}}      
        ,
        {field:'position',width:90 ,title:'职位'}
        ,
		{field:'startDate',title:'开始日期',width:70}
        ,
        {field:'endDate',title:'离职日期',width:70}
        ,
        {field:'workType',title:'类型',width:40,formatter: function(value){return formateptype(value);}}
       
        /*,
        {field:'tel',title:'证明人电话',width:90} */
        ,
		{field:'update',title:'操作',width:137,colspan:3,align:'center'},
       
		]]
	,
        toolbar: [  '-', {
            text: '添加经历', iconCls: 'icon-save', handler: function () {
            	addurl="staffWorkExperienceController.do?goAdd&staffId=${staffPage.sttaffId}";
            	addeducation("添加工作经历",addurl,610,470);
				}
 
            
        }],
        onAfterEdit: function (rowIndex, rowData, changes) {
            editRow = undefined;
        },
        onDblClickRow: function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#exprience_Table").datagrid('endEdit', editRow);
            }
 
            if (editRow == undefined) {
                $("#exprience_Table").datagrid('beginEdit', rowIndex);
                editRow = rowIndex;
            }
        },
        onClickRow: function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#exprience_Table").datagrid('endEdit', editRow);
 
            }
 
        }
 
    });
} 
	//初始化教育经历列表
	function GetedcationTable(url) {
    var editRow = undefined;
    $("#education_Table").datagrid({
       // height: 300,
     //   width: 450,
        title: '',
 //       collapsible: true,
 		rownumbers: true,
        singleSelect: true,
        pagination:true,
		pageSize:10,
        url: url,
       
        columns:[[{field:'id',hidden:true,title:'id'},
		
        {field:'schoolName',title:'学校',width:125}
        ,
		{field:'major',title:'专业',width:125}      
        ,
        {field:'educationType',width:90 ,title:'学历',formatter: function(value){return formatedu(value);}}
        ,
		{field:'startDate',title:'开始日期',width:70}
        ,
        {field:'endDate',title:'结束日期',width:70}
        /* ,
        {field:'certifyName',title:'证明人',width:60}
        ,
        {field:'tel',title:'证明人电话',width:90} */
        ,
		{field:'update',title:'操作',width:137,colspan:3,align:'center'},
       
		]]
	,
        toolbar: [  '-', {
            text: '添加经历', iconCls: 'icon-save', handler: function () {
            	addurl="staffEducationController.do?goAdd&staffId=${staffPage.sttaffId}";
            	addeducation("添加经历",addurl,510,320);
				}
 
            
        }],
        onAfterEdit: function (rowIndex, rowData, changes) {
            editRow = undefined;
        },
        onDblClickRow: function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#education_Table").datagrid('endEdit', editRow);
            }
 
            if (editRow == undefined) {
                $("#education_Table").datagrid('beginEdit', rowIndex);
                editRow = rowIndex;
            }
        },
        onClickRow: function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#education_Table").datagrid('endEdit', editRow);
 
            }
 
        }
 
    });
} 
	//初始化文件列表
	function GetTable(url) {
    var editRow = undefined;
    $("#sheduletime_Table").datagrid({
       // height: 300,
     //   width: 450,
        title: '',
 //       collapsible: true,
 		rownumbers: true,
        singleSelect: true,
        pagination:true,
		pageSize:10,
        url: url,
       
        columns:[[{field:'id',hidden:true,title:'id'},
		{field:'documenttype',width:120 ,title:'类型',formatter: function(value){return format(value);}}
        ,
		{field:'documentTitle',title:'标题',width:240}
        ,
		{field:'businessKey',title:'创建日期',width:125}
        ,
		{field:'note',title:'操作',width:130,align:'center'}
        
		
	]]
	,
        toolbar: [  '-', {
            text: '上传文件', iconCls: 'icon-save', handler: function () {
            	addurl="systemController.do?addFiles&staffid=${staffPage.id }";
        		addfilecreatewindow("文件上传", addurl,655,300);
				}
 
            
        }],
        onAfterEdit: function (rowIndex, rowData, changes) {
            editRow = undefined;
        },
        onDblClickRow: function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#sheduletime_Table").datagrid('endEdit', editRow);
            }
 
            if (editRow == undefined) {
                $("#sheduletime_Table").datagrid('beginEdit', rowIndex);
                editRow = rowIndex;
            }
        },
        onClickRow: function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#sheduletime_Table").datagrid('endEdit', editRow);
 
            }
 
        }
 
    });
} 
	function format(value)
	{
		
		if("1"==value)return "简历";
		if("2"==value)return "基础信息表";
		if("3"==value)return "身份证";
		if("4"==value)return "学历证明";
		if("5"==value)return "合同协议";
		if("6"==value)return "资质证书";
		if("7"==value)return "offer";
		if("8"==value)return "其它";
	}
	function formatedu(value)
	{
		if("0"==value)return "本科";
		if("1"==value)return "专科";
		if("2"==value)return "高中";
		if("3"==value)return "硕士";
		if("4"==value)return "博士";
		if("5"==value)return "本科在读";
		if("6"==value)return "硕士研究生在读";
		if("7"==value)return "博士研究生在读";
		if("8"==value)return "其它";
	}
	function formateptype(value)
	{
		if("A"==value)return "全职";
		if("B"==value)return "兼职";
		if("C"==value)return "实习";
		
	}
	function formatepCompanyCount(value)
	{
		
		if("1"==value)return "少于50人";
		if("2"==value)return "50-150人";
		if("3"==value)return "150-500人";
		if("4"==value)return "500-1000人";
		if("5"==value)return "1000-5000人";
		if("6"==value)return "5000-10000人";
		if("7"==value)return "10000人以上";
		
	}
	//上传文件弹出框
	function addfilecreatewindow(title, addurl,width,height) {
		width = width?width:700;
		height = height?height:400;
		if(width=="100%" || height=="100%"){
			width = window.top.document.body.offsetWidth;
			height =window.top.document.body.offsetHeight-100;
		}
	    //--author：JueYue---------date：20140427---------for：弹出bug修改,设置了zindex()函数
		if(typeof(windowapi) == 'undefined'){
			$.dialog({
				content: 'url:'+addurl,
				lock : true,
				zIndex: getzIndex(),
				width:width,
				height:height,
				title:title,
				opacity : 0.3,
				cache:false,
			    ok: function(){
			    	iframe = this.iframe.contentWindow;
					addObj();
					return false;
			    },
			    cancelVal: '关闭',
			    cancel: true /*为true等价于function(){}*/
			});
		}else{
			W.$.dialog({
				content: 'url:'+addurl,
				lock : true,
				width:width,
				zIndex:getzIndex(),
				height:height,
				parent:windowapi,
				title:title,
				opacity : 0.3,
				cache:false,
			    ok: function(){
			    	iframe = this.iframe.contentWindow;
			    	$('#btn_sub', iframe.document).click();	
					setTimeout(s,1500);	
					
					return false;
					
			    },
			    cancelVal: '关闭',
			    cancel: true /*为true等价于function(){}*/
			});
		}
	}
	
	//延迟1.5秒刷新
	function s()
	{
		
		$('#sheduletime_Table').datagrid('reload');
	}
	

	//添加教育经历弹出框
	function addeducation(title, addurl,width,height) {
		width = width?width:700;
		height = height?height:400;
		if(width=="100%" || height=="100%"){
			width = window.top.document.body.offsetWidth;
			height =window.top.document.body.offsetHeight-100;
		}
	    //--author：JueYue---------date：20140427---------for：弹出bug修改,设置了zindex()函数
		if(typeof(windowapi) == 'undefined'){
			$.dialog({
				content: 'url:'+addurl,
				lock : true,
				zIndex: getzIndex(),
				width:width,
				height:height,
				title:title,
				opacity : 0.3,
				cache:false,
			    ok: function(){
			    	iframe = this.iframe.contentWindow;
					addObj();
					return false;
			    },
			    cancelVal: '关闭',
			    cancel: true /*为true等价于function(){}*/
			});
		}else{
			W.$.dialog({
				content: 'url:'+addurl,
				lock : true,
				width:width,
				zIndex:getzIndex(),
				height:height,
				parent:windowapi,
				title:title,
				opacity : 0.3,
				cache:false,
				 ok: function(){
				    	
				    	iframe = this.iframe.contentWindow;
				    	if($('#age', iframe.document).val()!=null)
				    		{
				    		if($('#age', iframe.document).val()=="")
				    		{
				    			alert("请填写年龄！");			 
				    			return false;
				    		}else
				    			{
				    			var	v	=	$('#age', iframe.document).val();
				    			var	pattern	=/^[1-9]\d*$/;
				    			
				    			flag = pattern.test(v);
				    			if(!flag){
				    				alert("年龄为正整数！");				 
				    				return false;
				    			}else{   			
							    	$('#btn_sub', iframe.document).click();	
									setTimeout(edu,800);						
									return true;
				    				} 
				    			}			    					    		
				    	}else{			    		
				    	$('#btn_sub', iframe.document).click();	
						setTimeout(edu,800);						
						return true;
				    	}
				    },
			    cancelVal: '关闭',
			    cancel: true /*为true等价于function(){}*/
			});
		}
	}
	function edu()
	{
		iframe.close();
		$.messager.show({
			title:'提示信息',
			msg:'经历操作成功！',
			timeout:5000,
			showType:'slide'
		});
		$('#education_Table').datagrid('reload');
		$('#exprience_Table').datagrid('reload');
		$('#train_Table').datagrid('reload');
		$('#home_member_Table').datagrid('reload');
	}
    </Script>
	
<script type="text/javascript">
var checkidstart;
$(document).ready(function(){
$("#companyId").find("option[value='${staffPage.companyId}']").attr("selected",true);
$("#jobNo").find("option[value='${staffPage.jobNo}']").attr("selected",true);
$("#staffSource").find("option[value='${staffPage.staffSource}']").attr("selected",true);
$("#isCheck").find("option[value='${staffPage.isCheck}']").attr("selected",true);
$("#state").find("option[value='${staffPage.state}']").attr("selected",true);
$("#maxDegree").find("option[value='${staffPage.maxDegree}']").attr("selected",true);
$("#isStudyAbroad").find("option[value='${staffPage.maxDegree}']").attr("selected",true);
checkidstart= $("#checkId").val();
if($("#isCheck").val()==false)
	{	 
	 $("#checkId").removeAttr("datatype");		   
	 $("#checkId").hide();
	}
$("#isOutsource").find("option[value='${staffPage.isOutsource}']").attr("selected",true);
if( '${staffPage.staffSource}'=="15"){
	  $("#referencename").show();
	  $("#referenceIdSearch").show();
	  $("#referenceIdRedo").show();
}
});

//实习结束时间 、正式转正必选控制
function  showrstate(o)
{
	  var jct = $(o).find("option:selected").val();	
	  
	  if( jct=="2"){				  
		  $("#statecheck").show();				  				 				 
	  }
	  else{		  				  
		  $("#statecheck").hide();				 
	  }	
	  if( jct=="1"){				  
		  $("#stateusercheck").show();				  				 				 
	  }
	  else{		  				  
		  $("#stateusercheck").hide();				 
	  }	
}

//考勤Id的显示控制
function  showisCheck(o)
{
	  var jct = $(o).find("option:selected").val();	
	  var checkid=$("#checkId").val();
	  if( jct=="true"){				  
		  $("#checkIdcheck").show();				  				 				 
	  }
	  else{		  				  
		  $("#checkIdcheck").hide();				 
	  }			  
}
//验证考勤Id是否唯一
function getcheck()
{
	
	 var checkid=$('#checkId').val();
	 if(checkid!="")
		{
			var	v	=checkid;
			var	pattern	=/^\d{1,11}$/;
			
			flag = pattern.test(v);
			if(!flag){
				alert("考勤ID为1—11位整数！");				 
				return false;
			} 
			if(checkid!=checkidstart){
			//身份证内容得到年龄和出身日期
		 	$.post("staffController.do?getcheck",{"checkid":checkid},function(data){
		     	    //先删除单位名称的下拉菜单，但是请选择要留下
		     	      	   
		          if(data.checkId!=null ){      	 
		          alert("考勤ID："+checkid+"已经存在！")				     
		    	 $("#checkidflag").val("1");
		          
		     	       }
		          else  $("#checkidflag").val("0");
		          
		      },"json");
		   }else $("#checkidflag").val("0");
		}
 }

//推荐人的显示控制
function  showreffers(o)
{
	  var jct = $(o).find("option:selected").val();	
	  if( jct=="15"){
		  $("#referencename").show();
		  $("#referencename").attr("datatype",true); 
		  $("#referencename").attr("datatype","*");
		  $("#referenceIdSearch").show();
		  $("#referenceIdRedo").show();
		  $("#referenceIdcheck").show();
	  }
	  else{
		  $("#referencename").hide();
		  $("#referencename").removeAttr("datatype"); ;
		  $("#referenceIdSearch").hide();
		  $("#referenceIdRedo").hide();
		  $("#referenceIdcheck").hide();
		  $("#referenceId").val("0");
	  }
	  
}
$(function(){
	  $("#referenceIdcheck").hide();
	});
	

//ajax的二级联动，使用选择的所属部门，查询该所属单位下对应的岗位名称列表
function findUnit(departid){
	
	//所属单位的文本内容
	$.post("deptJobController.do?getDeptJobs",{"departid":departid},function(data){
   	    //先删除单位名称的下拉菜单，但是请选择要留下
   	    $("#jobNo option").remove();     	   
        if(data!=null&&data.length>0){      	 
            for(var i=0;i<data.length;i++){
   		       	var ddlCode = data[i].id;
   		       
   		       	var ddlName = data[i].jobName;
   		 	
   		       	//添加到单位名称的下拉菜单中
   		     	var $option = $("<option></option>");
   		       	$option.attr("value",ddlCode);
   		       	$option.text(ddlName);
   		      	$("#jobNo").append($option);
   	       }
        }
    },"json");
	
}

//选择部门部分js
function openDepartmentSelect() {
	
	
		$.dialog.setting.zIndex = getzIndex(); 
		
		var deptId = $("#companyId").val();
		
		$.dialog({content: 'url:departController.do?deptjobDepartSelect&deptId='+deptId+',', zIndex: 3999, title: '组织机构列表', lock: true, width: '400px', height: '350px', opacity: 0.4, button: [
		   {name: '<t:mutiLang langKey="common.confirm"/>', callback: callbackDepartmentSelect, focus: true},
		   {name: '<t:mutiLang langKey="common.cancel"/>', callback: function (){}}
	   ]}).zindex();
	}
		
	function callbackDepartmentSelect() {
		  var iframe = this.iframe.contentWindow;
		  var treeObj = iframe.$.fn.zTree.getZTreeObj("departSelect");
		  var nodes = treeObj.getCheckedNodes(true);
		  if(nodes.length>0){
		  var ids='',names='';
		  for(i=0;i<nodes.length;i++){
		     var node = nodes[i];
		     ids += node.id;
		    names += node.name;
		 }
		 $('#departname').val(names);
		 $('#departname').blur();		
		 $('#deptId').val(ids);	
		 findUnit(ids);
		 
		}
	}
	
	function callbackClean(){
		$('#departname').val('');
		 $('#deptId').val('');	
	}
	
	function setOrgIds() {}
	$(function(){
		$("#departname").prev().hide();
	});
	
	//选择角色部分js
	function openroleidSelect() {
			$.dialog.setting.zIndex = getzIndex(); 
			var ids=$('#roleid').val();
		//	var deptId = $("#companyId").val();
			
			$.dialog({content: 'url:userController.do?roles&ids='+ids, zIndex: 3999, title: '角色列表', lock: true, width: '450px', height: '350px', opacity: 0.4, button: [
			   {name: '<t:mutiLang langKey="common.confirm"/>', callback: callbackroleidSelect, focus: true},
			   {name: '<t:mutiLang langKey="common.cancel"/>', callback: function (){}}
		   ]}).zindex();
		}
			
		function callbackroleidSelect() {			
				 var iframe = this.iframe.contentWindow;
				 var s= iframe.$('#roleList').datagrid('getSelections');                    ;
				 if(s.length>0) 
					 {
					 var roleName="";
					 var roleid="";					
					 for(var i=0;i<s.length;i++)
						 {
						 roleName+=s[i].roleName+",";
						 roleid+=s[i].id+",";
						
						 }
					
					 $('#roleName').val(roleName.substring(0,roleName.length-1));
					 $('#roleName').blur();		
					 $('#roleid').val(roleid.substring(0,roleid.length-1));	
					 }
				 else {
					 $('#roleName').val("");			
					 $('#roleid').val("");	
				 }
		}
	//选择推荐人部分js
	function openreferenceIdSelect() {
			$.dialog.setting.zIndex = getzIndex(); 
			
		//	var deptId = $("#companyId").val();
			
			$.dialog({content: 'url:staffController.do?listforReference', zIndex: 3999, title: '享宇员工列表', lock: true, width: '450px', height: '350px', opacity: 0.4, button: [
			   {name: '<t:mutiLang langKey="common.confirm"/>', callback: callbackreferenceIdSelect, focus: true},
			   {name: '<t:mutiLang langKey="common.cancel"/>', callback: function (){}}
		   ]}).zindex();
		}
			
		function callbackreferenceIdSelect() {
				 var iframe = this.iframe.contentWindow;
				 var s= iframe.$('#staffList').datagrid('getSelected');                    ;
				  
				 $('#referencename').val(s['name']);
				 $('#referencename').blur();		
				 $('#referenceId').val(s['sttaffId']);	
		}		
		function referenceIdcallbackClean(){
			$('#referencename').val('');
			 $('#referenceId').val('');	
		}
		
		
		

		//通用弹出式文件上传
		function commonUpload(callback){
		    $.dialog({
		           content: "url:systemController.do?commonUpload",
		           lock : true,
		           title:"文件上传",
		           zIndex:2100,
		           width:700,
		           height: 200,
		           parent:windowapi,
		           cache:false,
		       ok: function(){
		               var iframe = this.iframe.contentWindow;
		               iframe.uploadCallback(callback);
		                   return true;
		       },
		       cancelVal: '关闭',
		       cancel: function(){
		       } 
		   });
		}
		function browseImages(inputId, Img) {// 图片管理器，可多个上传共用
				var finder = new CKFinder();
				finder.selectActionFunction = function(fileUrl, data) {//设置文件被选中时的函数 
					$("#" + Img).attr("src", fileUrl);
					$("#" + inputId).attr("value", fileUrl);
				};
				finder.resourceType = 'Images';// 指定ckfinder只为图片进行管理
				finder.selectActionData = inputId; //接收地址的input ID
				finder.removePlugins = 'help';// 移除帮助(只有英文)
				finder.defaultLanguage = 'zh-cn';
				finder.popup();
			}
		function browseFiles(inputId, file) {// 文件管理器，可多个上传共用
			var finder = new CKFinder();
			finder.selectActionFunction = function(fileUrl, data) {//设置文件被选中时的函数 
				$("#" + file).attr("href", fileUrl);
				$("#" + inputId).attr("value", fileUrl);
				decode(fileUrl, file);
			};
			finder.resourceType = 'Files';// 指定ckfinder只为文件进行管理
			finder.selectActionData = inputId; //接收地址的input ID
			finder.removePlugins = 'help';// 移除帮助(只有英文)
			finder.defaultLanguage = 'zh-cn';
			finder.popup();
		}
		function decode(value, id) {//value传入值,id接受值
			var last = value.lastIndexOf("/");
			var filename = value.substring(last + 1, value.length);
			$("#" + id).text(decodeURIComponent(filename));
		}
		
		 //年龄与出身日期控制
		 function  getAge(){
	 
			 if($('#certNo').val()=="")
				{
					alert("请填写身份证号码！");			 
					return false;
				}else
					{
					 
					var	v	=	$('#certNo').val();
					var	pattern	=/(^\d{15}$)|(^\d{17}([0-9]|X|x)$)/;
					
					flag = pattern.test(v);
					if(!flag){
						alert(" 身份证号码格式有误，15或18位数！");				 
						return false;
					} 
					}
			 var certno	=	$('#certNo').val();
			//身份证内容得到年龄和出身日期
			 	$.post("staffController.do?getAge",{"certno":certno},function(data){
			     	    //先删除单位名称的下拉菜单，但是请选择要留下
			     	    $("#jobNo option").remove();     	   
			          if(data!=null ){      	 
			             //alert(data.age+"c"+data.birthday);
			     $("#age").val(data.age);
			     $("#birthday").val(data.name);
			     	       }
			          
			      },"json");
			  	 
		 }
</script>