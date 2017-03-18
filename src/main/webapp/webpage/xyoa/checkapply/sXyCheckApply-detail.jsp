<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>享宇考勤申请表</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script type="text/javascript">
	//编写自定义JS代码
	function onDbclick_table_row(rowIndex, rowData) {
	 	createdetailwindow("查看详情", "sXyCheckinoutController.do?goUpdate&load=detail&id="+rowData.id);
 	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center'" style="padding: 0px; border: 0px; overflow: auto;">
		<t:formvalid formid="formobj" dialog="true" usePlugin="password"
			layout="table" tiptype="1">
			<input id="id" name="id" type="hidden"
				value="${sXyCheckApplyPage.id }">
			<table style="width: 800px;" cellpadding="0" cellspacing="1"
				class="formtable">
				<tr>
					<td align="right"><label class="Validform_label"> 申请人:
					</label></td>
					<td class="value"><input id="tsUser" name="tsUser" type="text"
						style="width: 150px" class="inputxt"
						value='${sXyCheckApplyPage.tsUser.realName }'> <span
						class="Validform_checktip"></span> <label class="Validform_label"
						style="display: none;">申请人</label></td>
					<td align="right"><label class="Validform_label">
							所属部门: </label></td>
					<td class="value"><input id="tsDept" name="tsDept" type="text"
						style="width: 150px" class="inputxt"
						value='${sXyCheckApplyPage.tsDept.departname }'> <span
						class="Validform_checktip"></span> <label class="Validform_label"
						style="display: none;">所属部门</label></td>
				</tr>


				<tr>
					<td align="right"><label class="Validform_label">
							申请人编号: </label></td>
					<td class="value"><input id="applySttaffId"
						name="applySttaffId" type="text" style="width: 150px"
						class="inputxt" value='${sXyCheckApplyPage.applySttaffId}'>
						<span class="Validform_checktip"></span> <label
						class="Validform_label" style="display: none;">申请人编号</label></td>
					<td align="right"><label class="Validform_label">
							考勤类型: </label></td>
					<td class="value"><t:dictSelect field="checkType" type="list"
							typeGroupCode="applycheckType"
							defaultVal="${sXyCheckApplyPage.checkType}" hasLabel="false"
							title="考勤类型"></t:dictSelect> <span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">考勤类型</label>
					</td>
				</tr>


				<tr>
					<td align="right"><label class="Validform_label">
							申请日期: </label></td>
					<td class="value"><input id="applyDate" name="applyDate"
						type="text" style="width: 150px" class="Wdate"
						onClick="WdatePicker()"
						value='<fmt:formatDate value='${sXyCheckApplyPage.applyDate}' type="date" pattern="yyyy-MM-dd"/>'>
						<span class="Validform_checktip"></span> <label
						class="Validform_label" style="display: none;">申请日期</label></td>
					<td align="right"><label class="Validform_label">
							流程状态: </label></td>
					<td class="value"><c:choose>
							<c:when test="${sXyCheckApplyPage.flowState == '0'}">
								<input id="flowState" name="flowState" type="text"
									style="width: 150px" class="inputxt" value='流程未启动'>
							</c:when>
							<c:when test="${sXyCheckApplyPage.flowState == '1'}">
								<input id="flowState" name="flowState" type="text"
									style="width: 150px" class="inputxt" value='异常待审批'>
							</c:when>
							<c:when test="${sXyCheckApplyPage.flowState == '2'}">
								<input id="flowState" name="flowState" type="text"
									style="width: 150px" class="inputxt" value='异常审批中'>
							</c:when>
							<c:when test="${sXyCheckApplyPage.flowState == '3'}">
								<input id="flowState" name="flowState" type="text"
									style="width: 150px" class="inputxt" value='异常申请被否决'>
							</c:when>
							<c:when test="${sXyCheckApplyPage.flowState == '4'}">
								<input id="flowState" name="flowState" type="text"
									style="width: 150px" class="inputxt" value='已完成'>
							</c:when>
							<c:when test="${sXyCheckApplyPage.flowState == '5'}">
								<input id="flowState" name="flowState" type="text"
									style="width: 150px" class="inputxt" value='已撤销'>
							</c:when>
							<c:when test="${sXyCheckApplyPage.flowState == '6'}">
								<input id="flowState" name="flowState" type="text"
									style="width: 150px" class="inputxt" value='已取消'>
							</c:when>
						</c:choose> <span class="Validform_checktip"></span> <label
						class="Validform_label" style="display: none;">流程状态</label></td>
				</tr>


				<tr>
					<td align="right"><label class="Validform_label">
							考勤异常原因: </label></td>
					<td class="value" colspan="3"><textarea id="remarks"
							style="width: 775px; height: 100px;" class="inputxt" rows="6"
							name="remarks">${sXyCheckApplyPage.remarks}</textarea> <span
						class="Validform_checktip"></span> <label class="Validform_label"
						style="display: none;">考勤异常原因</label></td>
				</tr>


				<c:if test="${sXyCheckApplyPage.flowState != '0' }">
					<tr>
						<td align="right"><label class="Validform_label">
								审批意见: </label></td>
						<td class="value" colspan="3"><c:forEach
								items="${commentBeansPage }" var="commentBean">
								<table>
									<tr>
										<td align="right" style="width: 60px;"><c:out
												value="${commentBean.getRealName() }" />：</td>
										<td class="value" style="width: 80px;"><font color="red">（${commentBean.getIsAgree() }）</font></td>
										<td class="value"><c:out
												value="${commentBean.getFullMessage() }" /></td>
									</tr>
								</table>
							</c:forEach> <span class="Validform_checktip"></span> <label
							class="Validform_label" style="display: none;">审批意见</label></td>
					</tr>
				</c:if>

			</table>
		</t:formvalid>
		<t:datagrid name="sXyCheckApplyListDetail" checkbox="true"
			fitColumns="true" title="考勤异常列表"
			actionUrl="sXyCheckApplyController.do?datagridDetail&checkIds=${sXyCheckApplyPage.checkIds }"
			idField="id" fit="true" queryMode="group" sortName="checkDate"
			sortOrder="desc" onDblClick="onDbclick_table_row">
			<t:dgCol title="主键" field="id" hidden="true" queryMode="group"
				width="120"></t:dgCol>
			<t:dgCol title="姓名" field="staffId" hidden="true" queryMode="single"
				width="120"></t:dgCol>
			<t:dgCol title="姓名" field="name" align="center" queryMode="single"
				width="100"></t:dgCol>
			<t:dgCol title="部门" field="deptId" hidden="true" queryMode="single"
				width="100" dictionary="company"></t:dgCol>
			<t:dgCol title="考勤日期" field="checkDate" align="center"
				formatter="yyyy-MM-dd" queryMode="group" width="100"></t:dgCol>
			<t:dgCol title="上班时间" field="workTime" align="center"
				queryMode="group" width="100"></t:dgCol>
			<t:dgCol title="下班时间" field="offWorkTime" align="center"
				queryMode="group" width="100"></t:dgCol>
			<t:dgCol title="考勤状态" field="isCheckTrue" align="center"
				queryMode="single" dictionary="company" width="100"
				replace="正常_00,异常_01"></t:dgCol>
			<t:dgCol title="异常分钟数" field="exceptionMinute" align="center"
				queryMode="group" width="100"></t:dgCol>
			<t:dgCol title="异常原因" field="exceptionRemarks" hidden="true"
				queryMode="group" width="100"></t:dgCol>
			<t:dgCol title="考勤类型" field="checkType" hidden="true"
				queryMode="group" width="100"></t:dgCol>
			<t:dgCol title="考勤原因" field="checkRemarks" hidden="true"
				queryMode="group" width="100"></t:dgCol>
			<t:dgCol title="申请人ID" field="applyId" hidden="true"
				queryMode="group" width="100"></t:dgCol>
			<t:dgCol title="日期类型" field="dateType" align="center"
				queryMode="single" width="100" dictionary="company"
				replace="工作日_B,休息日_H,节假日_W"></t:dgCol>
			<t:dgCol title="工作时长" field="workHour" align="center"
				queryMode="group" width="100"></t:dgCol>
			<t:dgCol title="修改时间" field="uTime" hidden="true"
				formatter="yyyy-MM-dd hh:mm:ss" queryMode="group" width="150"></t:dgCol>
			<t:dgCol title="修改人" field="uUser" hidden="true" queryMode="group"
				width="100"></t:dgCol>
			<t:dgCol title="考勤系统员工ID" field="userId" hidden="true"
				queryMode="group" width="100"></t:dgCol>
			<t:dgCol title="流程状态" field="flowState" hidden="true"
				queryMode="group" width="100"></t:dgCol>
			<t:dgCol title="流程实例ID" field="flowInstId" hidden="true"
				queryMode="group" width="120"></t:dgCol>
			<t:dgToolBar title="查看详情" icon="icon-search"
				url="sXyCheckinoutController.do?goUpdate" funname="detail"></t:dgToolBar>
		</t:datagrid>
	</div>
</body>
</html>