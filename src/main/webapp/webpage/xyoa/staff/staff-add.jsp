<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>享宇员工表</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script type="text/javascript">
	//编写自定义JS代码
</script>
</head>
<body>
	<t:formvalid formid="formobj" dialog="true" usePlugin="password"
		layout="table" action="staffController.do?doAdd" tiptype="1">
		<input id="id" name="id" type="hidden" value="${staffPage.id }">
		<table style="width: 680px;" cellpadding="0" cellspacing="1"
			class="formtable">
			<tr>
				<td align="right"><label class="Validform_label"> 归属公司:
				</label></td>
				<td class="value"><select id="companyId" name="companyId">
						<option value="">---请选择---</option>
						<c:forEach items="${TSDeparts}" var="list">
							<option value="${list.id}">${list.departname}</option>
						</c:forEach>
				</select> <span class="Validform_checktip"><font color="red">*</font></span>
					<label class="Validform_label" style="display: none;">归属公司</label></td>
				<td align="right"><label class="Validform_label"> 部门: </label>
				</td>
				<td class="value"><input id="departname" type="text"
					readonly="readonly" class="inputxt" value="请先选择公司！"> <input
					id="deptId" name="deptId" type="hidden"> <a href="#"
					class="easyui-linkbutton" plain="true" icon="icon-search"
					id="departSearch" onclick="openDepartmentSelect()">选择</a> <!--   <a href="#" class="easyui-linkbutton" plain="true" icon="icon-redo" id="departRedo" onclick="callbackClean()">清空</a>
             --> <span class="Validform_checktip"><font
						color="red">*</font></span> <label class="Validform_label"
					style="display: none;">部门</label></td>

			</tr>
			<tr>
				<td align="right"><label class="Validform_label"> 岗位: </label>
				</td>
				<td class="value"><select name="jobNo" id="jobNo"></select> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">岗位</label></td>
				<td align="right"><label class="Validform_label"> <t:mutiLang
							langKey="common.role" />:
				</label></td>
				<td class="value" nowrap><input name="roleid" name="roleid"
					type="hidden" value="${id}" id="roleid"> <input
					name="roleName" class="inputxt" value="${roleName }" id="roleName"
					readonly="readonly" /> <a href="#" class="easyui-linkbutton"
					plain="true" icon="icon-search" id="roleidSearch"
					onclick="openroleidSelect()">选择</a> <%--  <t:choose hiddenName="roleid" hiddenid="id" url="userController.do?roles" name="roleList"
                          icon="icon-search" title="common.role.list" textname="roleName" isclear="false" isInit="true"></t:choose> --%>
					<span class="Validform_checktip"> <font color="red">*</font></span>
				</td>


			</tr>
			<tr>
				<td align="right"><label class="Validform_label"> 姓名: </label>
				</td>
				<td class="value"><input id="name" name="name" type="text"
					style="width: 150px" class="inputxt"> <span
					class="Validform_checktip"><font color="red">*</font></span> <label
					class="Validform_label" style="display: none;">姓名</label></td>
				<td align="right"><label class="Validform_label"> 性别: </label>
				</td>
				<td class="value"><t:dictSelect field="sex" type="list"
						typeGroupCode="sex" defaultVal="${staffPage.sex}" hasLabel="false"
						title="性别"></t:dictSelect> <span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">性别</label></td>
			</tr>
			<tr>
				<td align="right"><label class="Validform_label"> 政治面貌:
				</label></td>
				<td class="value"><t:dictSelect field="politicsStatus"
						type="list" typeGroupCode="politicsStatus"
						defaultVal="${staffPage.politicsStatus}" hasLabel="false"
						title="是否统招"></t:dictSelect><span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">政治面貌</label></td>
				<td align="right"><label class="Validform_label"> 是否统招:
				</label></td>
				<td class="value"><t:dictSelect field="fullEducation"
						type="list" typeGroupCode="full_Education"
						defaultVal="${staffPage.fullEducation}" hasLabel="false"
						title="是否统招"></t:dictSelect> <span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">是否统招</label></td>
			</tr>
			<tr>

				<td align="right"><label class="Validform_label"> 民族: </label>
				</td>
				<td class="value"><t:dictSelect field="nation" type="list"
						typeGroupCode="nation" defaultVal="${staffPage.nation}"
						hasLabel="false" title="民族"></t:dictSelect> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">民族</label></td>
				<td align="right"><label class="Validform_label"> 状态: </label>
				</td>
				<td class="value"><select name="state" id="state"
					onchange="showrstate(this)">
						<option value="2">试用期</option>
						<option value="1">已转正</option>
						<option value="0">离职</option>
						<option value="4">兼职</option>
						<option value="3">其它</option>
				</select> <span class="Validform_checktip"><font color="red">*</font></span>
					<label class="Validform_label" style="display: none;">状态</label></td>
			</tr>
			<tr>
				<td align="right"><label class="Validform_label"> 是否考勤:
				</label></td>
				<td class="value"><select id="isCheck" name="isCheck"
					onchange="showisCheck()">
						<option value="true">是</option>
						<option value="false">否</option>
				</select> <span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">是否考勤</label></td>
				<td align="right"><label class="Validform_label"> 考勤ID:
				</label></td>
				<td class="value"><input id="checkId" name="checkId"
					onblur="getcheck()" type="text" style="width: 150px"
					class="inputxt"><input id="checkidflag" type="hidden">
					<span class="Validform_checktip"> <font id="checkIdcheck"
						color="red">*</font></span> <label class="Validform_label"
					style="display: none;">考勤ID</label></td>
			</tr>
			<tr>
				<td align="right"><label class="Validform_label"> 最高学历:
				</label></td>
				<td class="value"><t:dictSelect field="maxDegree" type="list"
						typeGroupCode="max_degree" defaultVal="${staffPage.maxDegree}"
						hasLabel="false" title="最高学历"></t:dictSelect> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">最高学历</label></td>
				<td align="right"><label class="Validform_label">
						有无留学经历: </label></td>
				<td class="value"><t:dictSelect field="isStudyAbroad"
						type="list" typeGroupCode="is_study_abroad"
						defaultVal="${staffPage.isStudyAbroad}" hasLabel="false"
						title="有无留学经历"></t:dictSelect> <span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">留学经历</label></td>
			</tr>
			<tr>

				<td align="right"><label class="Validform_label"> 毕业院校:
				</label></td>
				<td class="value"><input id="fromSchool" name="fromSchool"
					type="text" style="width: 150px" class="inputxt"> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">毕业院校</label></td>

				<td align="right"><label class="Validform_label"> 专业: </label>
				</td>
				<td class="value"><input id="major" name="major" type="text"
					style="width: 150px" class="inputxt"> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">专业</label></td>
			</tr>




			<tr>

				<td align="right"><label class="Validform_label"> 毕业时间:
				</label></td>
				<td class="value"><input id="graduationDate"
					name="graduationDate" type="text" style="width: 150px"
					class="Wdate" onClick="WdatePicker()"> <span
					class="Validform_checktip"><font color="red">*</font> </span> <label
					class="Validform_label" style="display: none;">毕业时间</label></td>


				<td align="right"><label class="Validform_label">
						正式转正日期: </label></td>
				<td class="value"><input id="positiveDate" name="positiveDate"
					type="text" style="width: 150px" class="Wdate"
					onClick="WdatePicker()"> <span class="Validform_checktip"><font
						id="stateusercheck" color="red">*</font></span> <label
					class="Validform_label" style="display: none;">正式转正日期</label></td>

			</tr>

			<tr>
				<td align="right"><label class="Validform_label">
						试用期开始时间: </label></td>
				<td class="value"><input id="trialStartData"
					name="trialStartData" type="text" style="width: 150px"
					onchange="getSiling();" class="Wdate" onClick="WdatePicker()">
					<span class="Validform_checktip"><font color="red">*</font></span>
					<label class="Validform_label" style="display: none;">试用期开始时间</label></td>
				<td align="right"><label class="Validform_label">
						试用期结束时间: </label></td>
				<td class="value"><input id="trialEndData" name="trialEndData"
					type="text" style="width: 150px" class="Wdate"
					onClick="WdatePicker()"> <span class="Validform_checktip"><font
						id="statecheck" color="red">*</font> </span> <label
					class="Validform_label" style="display: none;">试用期结束时间</label></td>
			</tr>
			<tr>

				<td align="right"><label class="Validform_label">
						劳动合同开始时间: </label></td>
				<td class="value"><input id="contractStartDate"
					name="contractStartDate" type="text" style="width: 150px"
					class="Wdate" onClick="WdatePicker()"> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">劳动合同开始时间</label></td>
				<td align="right"><label class="Validform_label">
						劳动合同结束时间: </label></td>
				<td class="value"><input id="contractEndDate"
					name="contractEndDate" type="text" style="width: 150px"
					class="Wdate" onClick="WdatePicker()"> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">劳动合同结束时间</label></td>
			</tr>
			<tr>
				<td align="right"><label class="Validform_label">
						可调休天数: </label></td>
				<td class="value"><input id="offWorkCount" name="offWorkCount"
					type="text" style="width: 150px" class="inputxt"> <span
					class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">可调休天数</label></td>
				<td align="right"><label class="Validform_label">
						年假可用天数: </label></td>
				<td class="value"><input id="leaveCount" name="leaveCount"
					type="text" style="width: 150px" class="inputxt"> <span
					class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">年假可用天数</label></td>
			</tr>
			<tr>


				<td align="right"><label class="Validform_label"> 手机: </label>
				</td>
				<td class="value"><input id="mobile" name="mobile" type="text"
					style="width: 150px" class="inputxt"> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">手机</label></td>
				<td align="right"><label class="Validform_label">
						email: </label></td>
				<td class="value"><input id="email" name="email" type="text"
					style="width: 150px" class="inputxt"> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">email</label></td>

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
				<td class="value"><input id="certNo" name="certNo" type="text"
					onblur="getAge();" style="width: 150px" class="inputxt"> <span
					class="Validform_checktip"><font color="red">*</font></span> <label
					class="Validform_label" style="display: none;">身份证号码</label></td>
			</tr>
			<tr>
				<td align="right"><label class="Validform_label"> 出身日期:
				</label></td>
				<td class="value"><input id="birthday" name="birthday"
					type="text" style="width: 150px" class="Wdate"
					onClick="WdatePicker()"> <span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">出身日期</label></td>
				<td align="right"><label class="Validform_label"> 年龄: </label></td>
				<td class="value"><input id="age" name="age" type="text"
					style="width: 150px" class="inputxt"> <span
					class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">年龄</label></td>
			</tr>
			<tr>
				<td align="right"><label class="Validform_label">
						户籍所在地: </label></td>
				<td class="value"><input id="registerAddr" name="registerAddr"
					type="text" style="width: 150px" class="inputxt"> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">户籍所在地</label></td>
				<td align="right"><label class="Validform_label"> 户籍类型:
				</label></td>
				<td class="value"><t:dictSelect field="registerType"
						type="list" typeGroupCode="register_t"
						defaultVal="${staffPage.registerType}" hasLabel="false"
						title="户籍类型"></t:dictSelect> <span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">户籍类型</label>
				</td>
			</tr>
			<tr>
				<td align="right"><label class="Validform_label"> 家庭住址:
				</label></td>
				<td class="value" colspan='3'><input id="addr" name="addr"
					type="text" style="width: 460px" class="inputxt"> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">家庭住址</label></td>

			</tr>
			<tr>
				<td align="right"><label class="Validform_label">
						紧急联系人: </label></td>
				<td class="value"><input id="linkmanName" name="linkmanName"
					type="text" style="width: 150px" class="inputxt"> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">紧急联系人</label></td>
				<td align="right"><label class="Validform_label">
						紧急联系电话: </label></td>
				<td class="value"><input id="linkmanTel" name="linkmanTel"
					type="text" style="width: 150px" class="inputxt"> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">紧急联系电话</label></td>

			</tr>
			<tr>
				<td align="right"><label class="Validform_label">
						紧急联系人关系: </label></td>
				<td class="value"><input id="linkmanRelative"
					name="linkmanRelative" type="text" style="width: 150px"
					class="inputxt"> <span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">紧急联系人关系</label></td>
				<td align="right"><label class="Validform_label">
						司龄（月）: </label></td>
				<td class="value"><input id="siling" name="siling"
					readonly="readonly" type="text" style="width: 150px"
					class="inputxt"> <span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">司龄</label></td>

			</tr>
			<tr>
				<td align="right"><label class="Validform_label"> 社保账号:
				</label></td>
				<td class="value"><input id="socialSecurityNo"
					name="socialSecurityNo" type="text" style="width: 150px"
					class="inputxt"> <span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">社保账号</label></td>
				<td align="right"><label class="Validform_label">
						公积金账号: </label></td>
				<td class="value"><input id="fundNo" name="fundNo" type="text"
					style="width: 150px" class="inputxt"> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">公积金账号</label></td>

			</tr>

			<tr>
				<td align="right"><label class="Validform_label"> 是否伤残:
				</label></td>
				<td class="value"><t:dictSelect field="isDistressed"
						type="list" typeGroupCode="isDistressed"
						defaultVal="${staffPage.isDistressed}" hasLabel="false"
						title="是否伤残"></t:dictSelect><span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">是否伤残</label>
				</td>
				<td align="right"><label class="Validform_label"> 伤残等级:
				</label></td>
				<td class="value"><t:dictSelect field="distressedLevel"
						type="list" typeGroupCode="distressedLevel"
						defaultVal="${staffPage.distressedLevel}" hasLabel="false"
						title="伤残等级"></t:dictSelect> <span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">伤残等级</label></td>

			</tr>
			<tr>
				<td align="right"><label class="Validform_label">
						是否有传染病: </label></td>
				<td class="value"><t:dictSelect field="isSick" type="list"
						typeGroupCode="isSick" defaultVal="${staffPage.isSick}"
						hasLabel="false" title="是否有传染病"></t:dictSelect><span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">是否有传染病</label></td>
				<td align="right"><label class="Validform_label">
						如果有，是何传染病: </label></td>
				<td class="value"><input id="sick" name="sick" type="text"
					value="${staffPage.sick}" style="width: 150px" class="inputxt">
					<span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">如果有，是何传染病</label></td>

			</tr>
			<tr>
				<td align="right"><label class="Validform_label">
						是否曾犯罪: </label></td>
				<td class="value"><t:dictSelect field="isCommit" type="list"
						typeGroupCode="isCommit" defaultVal="${staffPage.isCommit}"
						hasLabel="false" title="是否曾犯罪"></t:dictSelect> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">是否曾犯罪</label></td>
				<td align="right"><label class="Validform_label">
						如果有，犯何罪: </label></td>
				<td class="value"><input id="commit" name="commit" type="text"
					value="${staffPage.commit}" style="width: 150px" class="inputxt">
					<span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">如果有，犯何罪</label></td>

			</tr>

			<tr>
				<td align="right"><label class="Validform_label"> 招聘来源:
				</label></td>
				<td class="value"><select name="staffSource" id="staffSource"
					onchange="showreffers(this)">

						<option value="0">智联招聘</option>
						<option value="1">拉勾网</option>
						<option value="2">前程无忧</option>
						<option value="3">猎聘网</option>
						<option value="4">Boss直聘</option>
						<option value="15">内部推荐</option>
						<option value="6">其他</option>
				</select> <span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">招聘来源</label></td>
				<td align="right"><label class="Validform_label"> 推荐人:
				</label></td>
				<td class="value"><input id="referencename" type="text"
					readonly="readonly" class="inputxt"
					style="width: 150px; display: none;"> <input
					id="referenceId" name="referenceId" type="hidden" value="0">
					<a href="#" class="easyui-linkbutton" plain="true"
					icon="icon-search" id="referenceIdSearch" style="display: none;"
					onclick="openreferenceIdSelect()">选择</a> <!-- <a href="#" class="easyui-linkbutton" plain="true" icon="icon-redo" id="referenceIdRedo" style="display: none;"  onclick="referenceIdcallbackClean()">清空</a>
                 --> <span class="Validform_checktip"> <font
						id="referenceIdcheck" color="red">*</font></span> <label
					class="Validform_label" style="display: none;">推荐人</label></td>
			</tr>
			<tr>
				<td align="right"><label class="Validform_label"> 简历链接:
				</label></td>
				<td class="value"><input id="cvUrl" name="cvUrl" type="text"
					style="width: 150px" class="inputxt"> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">简历链接</label></td>
				<td align="right"><label class="Validform_label">
						是否外包人员: </label></td>
				<td class="value"><select name="isOutsource" id="isOutsource">

						<option value="A">是</option>
						<option value="B">否</option>

				</select> <span class="Validform_checktip"></span> <label
					class="Validform_label" style="display: none;">是否外包人员</label></td>
			</tr>
			<tr>

				<td align="right"><label class="Validform_label"> 备注: </label>
				</td>

				<td class="value" colspan='3'><textarea id="remarks"
						name="remarks" class="inputxt" cols="3" rows="1"
						style="width: 464px; height: 50px;"></textarea> <span
					class="Validform_checktip"></span> <label class="Validform_label"
					style="display: none;">备注</label></td>
				<!-- <input id="remarks" name="remarks"
					type="text" style="width: 150px" class="inputxt"> -->
			</tr>

		</table>
	</t:formvalid>
</body>


<script type="text/javascript">
	//司龄控制
	function getSiling() {

		if ($('#trialStartData').val() == "") {
			alertTip("请填写试用期开始日期！");
			return false;
		} else {

			var trialStartData = $('#trialStartData').val();
			//身份证内容得到年龄和出身日期
			$.post("staffController.do?getSiling", {
				"trialStartData" : trialStartData
			}, function(data) {
				//先删除单位名称的下拉菜单，但是请选择要留下

				if (data != null) {

					$("#siling").val(data);

				}

			}, "json");
		}

	}

	//ajax的二级联动，使用选择的所属单位，查询该所属单位下对应的单位名称列表
	function findJctUnit(o) {
		//  所属单位的文本内容
		var jct = $(o).find("option:selected").val();
		alertTip($('#deptId').val());
		$.post("staffController.do?getDeparts", {
			"id" : jct
		}, function(data) {
			//先删除单位名称的下拉菜单，但是请选择要留下
			$("#deptId option").remove();
			if (data != null && data.length > 0) {
				for (var i = 0; i < data.length; i++) {
					var ddlCode = data[i].id;

					var ddlName = data[i].departname;

					//添加到单位名称的下拉菜单中
					var $option = $("<option></option>");
					$option.attr("value", ddlCode);
					$option.text(ddlName);
					$("#deptId").append($option);
				}
			}
		}, "json");

	}
	//推荐人的显示控制
	function showreffers(o) {
		var jct = $(o).find("option:selected").val();
		if (jct == "15") {
			$("#referencename").show();
			$("#referencename").attr("datatype", true);
			$("#referencename").attr("datatype", "*");
			$("#referenceIdSearch").show();
			$("#referenceIdRedo").show();
			$("#referenceIdcheck").show();
		} else {
			$("#referencename").hide();
			$("#referencename").removeAttr("datatype");
			;
			$("#referenceIdSearch").hide();
			$("#referenceIdRedo").hide();
			$("#referenceIdcheck").hide();
			$("#referenceId").val("0");
		}

	}
	$(function() {
		$("#referenceIdcheck").hide();
	});

	//选择角色部分js
	function openroleidSelect() {
		$.dialog.setting.zIndex = getzIndex();
		var ids = $('#roleid').val();
		//	var deptId = $("#companyId").val();

		$.dialog({
			content : 'url:userController.do?roles&ids=' + ids,
			zIndex : 3999,
			title : '角色列表',
			lock : true,
			width : '450px',
			height : '350px',
			opacity : 0.4,
			button : [ {
				name : '<t:mutiLang langKey="common.confirm"/>',
				callback : callbackroleidSelect,
				focus : true
			}, {
				name : '<t:mutiLang langKey="common.cancel"/>',
				callback : function() {
				}
			} ]
		}).zindex();
	}

	function callbackroleidSelect() {
		var iframe = this.iframe.contentWindow;
		var s = iframe.$('#roleList').datagrid('getSelections');
		;
		if (s.length > 0) {
			var roleName = "";
			var roleid = "";
			for (var i = 0; i < s.length; i++) {
				roleName += s[i].roleName + ",";
				roleid += s[i].id + ",";

			}

			$('#roleName').val(roleName.substring(0, roleName.length - 1));
			$('#roleName').blur();
			$('#roleid').val(roleid.substring(0, roleid.length - 1));
		} else {
			$('#roleName').val("");
			$('#roleid').val("");
		}
	}
	//选择推荐人部分js
	function openreferenceIdSelect() {
		$.dialog.setting.zIndex = getzIndex();

		//	var deptId = $("#companyId").val();

		$.dialog({
			content : 'url:staffController.do?listforReference',
			zIndex : 3999,
			title : '享宇员工列表',
			lock : true,
			width : '450px',
			height : '350px',
			opacity : 0.4,
			button : [ {
				name : '<t:mutiLang langKey="common.confirm"/>',
				callback : callbackreferenceIdSelect,
				focus : true
			}, {
				name : '<t:mutiLang langKey="common.cancel"/>',
				callback : function() {
				}
			} ]
		}).zindex();
	}

	function callbackreferenceIdSelect() {

		var iframe = this.iframe.contentWindow;
		var s = iframe.$('#staffList').datagrid('getSelected');
		;

		$('#referencename').val(s['name']);
		$('#referencename').blur();
		$('#referenceId').val(s['sttaffId']);

	}

	function referenceIdcallbackClean() {
		$('#referencename').val('');
		$('#referenceId').val('');
	}

	//ajax的二级联动，使用选择的所属部门，查询该所属单位下对应的岗位名称列表
	function findUnit(departid) {
		//所属单位的文本内容
		$.post("deptJobController.do?getDeptJobs", {
			"departid" : departid
		}, function(data) {
			//先删除单位名称的下拉菜单，但是请选择要留下
			$("#jobNo option").remove();
			if (data != null && data.length > 0) {
				for (var i = 0; i < data.length; i++) {
					var ddlCode = data[i].id;

					var ddlName = data[i].jobName;

					//添加到单位名称的下拉菜单中
					var $option = $("<option></option>");
					$option.attr("value", ddlCode);
					$option.text(ddlName);
					$("#jobNo").append($option);
				}
			}
		}, "json");

	}

	//通过所选公司，选择部门部分js
	function openDepartmentSelect() {
		$.dialog.setting.zIndex = getzIndex();

		//var deptId = $("#companyId").val();// 动态获取公司
		var deptId = "402882a0563022c7015630271d440001";//公司写死
		$
				.dialog(
						{
							content : 'url:departController.do?deptjobDepartSelect&deptId='
									+ deptId + ',',
							zIndex : 3999,
							title : '组织机构列表',
							lock : true,
							width : '400px',
							height : '350px',
							opacity : 0.4,
							button : [
									{
										name : '<t:mutiLang langKey="common.confirm"/>',
										callback : callbackDepartmentSelect,
										focus : true
									},
									{
										name : '<t:mutiLang langKey="common.cancel"/>',
										callback : function() {
										}
									} ]
						}).zindex();
	}

	function callbackDepartmentSelect() {
		var iframe = this.iframe.contentWindow;
		var treeObj = iframe.$.fn.zTree.getZTreeObj("departSelect");
		var nodes = treeObj.getCheckedNodes(true);
		if (nodes.length > 0) {
			var ids = '', names = '';
			for (i = 0; i < nodes.length; i++) {
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

	function callbackClean() {
		$('#departname').val('');
		$('#deptId').val('');
	}

	function setOrgIds() {
	}
	$(function() {
		$("#departname").prev().hide();
	});

	//实习结束时间 、正式转正必选控制
	function showrstate(o) {
		var jct = $(o).find("option:selected").val();

		if (jct == "0") {
			$("#statecheck").show();
		} else {
			$("#statecheck").hide();
		}
		if (jct == "1") {
			$("#stateusercheck").show();
		} else {
			$("#stateusercheck").hide();
		}
	}
	//考勤Id的显示控制
	function showisCheck() {
		var jct = $("#isCheck").val();
		if (jct == "true") {
			$("#checkIdcheck").show();
		} else {
			$("#checkIdcheck").hide();
		}
	}
	//验证考勤Id是否唯一
	function getcheck() {
		var checkid = $('#checkId').val();
		if (checkid != "") {
			var v = checkid;
			var pattern = /^\d{1,11}$/;
			flag = pattern.test(v);
			if (!flag) {
				alertTip("考勤ID为1—11位整数！");
				return false;
			}

			$.post("staffController.do?getcheck", {
				"checkid" : checkid
			}, function(data) {
				if (data.checkId != null) {
					alertTip("考勤ID：" + checkid + "已经存在！")
					$("#checkidflag").val("1");
				} else
					$("#checkidflag").val("0");
			}, "json");
		}
	}

	//年龄与出身日期控制
	function getAge() {
		if ($('#certNo').val() == "") {
			alertTip("请填写身份证号码！");
			return false;
		} else {
			var v = $.trim($('#certNo').val());
			$('#certNo').val(v);
			var pattern = /(^\d{15}$)|(^\d{17}([0-9]|X|x)$)/;

			flag = pattern.test(v);
			if (!flag) {
				alertTip("身份证号码格式有误，15或18位数！");
				return false;
			}
		}
		var certno = $('#certNo').val();
		//身份证内容得到年龄和出身日期
		$.post("staffController.do?getAge", {
			"certno" : certno
		}, function(data) {
			//先删除单位名称的下拉菜单，但是请选择要留下
			if (data != null) {
				//alert(data.age+"c"+data.birthday);
				$("#age").val(data.age);
				$("#birthday").val(data.name);
			}

		}, "json");

	}

	//通用弹出式文件上传
	function commonUpload(callback) {
		$.dialog({
			content : "url:systemController.do?commonUpload",
			lock : true,
			title : "文件上传",
			zIndex : 2100,
			width : 700,
			height : 200,
			parent : windowapi,
			cache : false,
			ok : function() {
				var iframe = this.iframe.contentWindow;
				iframe.uploadCallback(callback);
				return true;
			},
			cancelVal : '关闭',
			cancel : function() {
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
</script>