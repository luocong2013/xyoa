<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<t:datagrid name="alluserList" title="common.operation" actionUrl="userController.do?datagrid"  
    fit="true" fitColumns="true" idField="id" queryMode="group" sortName="id" sortOrder="asc">
	<t:dgCol title="common.id" field="id" hidden="true"></t:dgCol>
	<t:dgCol title="员工编号" sortable="false" field="userName" query="true" align="center" width="60"></t:dgCol>
	<t:dgCol title="员工姓名" field="realName" query="true" align="center" width="60"></t:dgCol>
	<t:dgCol title="手机号码" field="mobilePhone" align="center" width="60"></t:dgCol>
	<t:dgCol title="常用邮箱" field="email" align="center" width="60"></t:dgCol>
	<t:dgCol title="common.department" sortable="false" field="userOrgList.tsDepart.departname" query="false" align="center" width="60"></t:dgCol>
	<t:dgCol title="common.role" field="userKey" align="center" width="60" ></t:dgCol>
	<t:dgCol title="common.createby" field="createBy" hidden="true"></t:dgCol>
	<t:dgCol title="common.createtime" field="createDate" formatter="yyyy-MM-dd" hidden="true"></t:dgCol>
	<t:dgCol title="common.updateby" field="updateBy" hidden="true"></t:dgCol>
	<t:dgCol title="common.updatetime" field="updateDate" formatter="yyyy-MM-dd" hidden="true"></t:dgCol>
	<t:dgCol title="common.status" sortable="true" field="status" replace="common.active_1,common.inactive_0,super.admin_-1"  align="center" width="60"></t:dgCol>
</t:datagrid>