<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<t:datagrid name="deluserList" title="common.operation" actionUrl="userController.do?deldatagrid"  
    fit="true" fitColumns="true" idField="id" queryMode="group" sortName="id" sortOrder="desc">
	<t:dgCol title="common.id" field="id" hidden="true"></t:dgCol>
	<t:dgCol title="员工编号" sortable="false" field="userName" query="true" align="center" width="60"></t:dgCol>
	<t:dgCol title="员工姓名" field="realName" query="true" align="center" width="60"></t:dgCol>
	<t:dgCol title="手机号码" field="mobilePhone" align="center" width="60"></t:dgCol>
	<t:dgCol title="常用邮箱" field="email" align="center" width="60"></t:dgCol>
	<t:dgCol title="common.role" field="userKey" align="center" width="60" ></t:dgCol>
	<t:dgCol title="common.createby" field="createBy" hidden="true"></t:dgCol>
	<t:dgCol title="common.createtime" field="createDate" formatter="yyyy-MM-dd" hidden="true"></t:dgCol>
	<t:dgCol title="common.updateby" field="updateBy" hidden="true"></t:dgCol>
	<t:dgCol title="common.updatetime" field="updateDate" formatter="yyyy-MM-dd" hidden="true"></t:dgCol>
	<t:dgCol title="common.status" field="deleteFlag" replace="离职_1, 在职_0"  align="center" width="60"></t:dgCol>
	<t:dgCol title="common.operation" field="opt" width="100" align="center"></t:dgCol> 
 	<t:dgDelOpt title="common.delete" url="userController.do?del&id={id}&userName={userName}" /> 
 	<t:dgToolBar title="excelOutput" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
</t:datagrid>
<script type="text/javascript">
//导出
function ExportXls() {
	JeecgExcelExport("userController.do?exportXls&deleteFlag=1", "deluserList");
}
</script>