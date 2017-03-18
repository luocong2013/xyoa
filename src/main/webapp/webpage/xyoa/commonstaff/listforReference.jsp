<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
	<div id="staff" region="center" style="padding: 0px; border: 0px">
		<t:datagrid name="staffList"   checkbox="false" fitColumns="true"
			title="" actionUrl="staffController.do?datagrid" idField="id"
			fit="true" queryMode="group">
			<t:dgCol title="主键" field="id" hidden="true" queryMode="single"
				width="120"></t:dgCol>
			<t:dgCol title="归属公司" field="companyId" replace="${replace}" 
				queryMode="single" dictionary="company" width="120"></t:dgCol>
			<t:dgCol title="部门" field="deptId"  replace="${replace}"  queryMode="single"
				dictionary="dept_id" width="120"></t:dgCol>
			<t:dgCol title="岗位" field="jobNo"  queryMode="single"  replace="${replace}" width="120"></t:dgCol>	
			<t:dgCol title="姓名" field="name" query="true" queryMode="single"
				width="120"></t:dgCol>
			<t:dgCol title="性别" field="sex"  queryMode="single"
				dictionary="sex" width="120"></t:dgCol>	
			<t:dgCol title="员工编号" field="sttaffId" queryMode="single" hidden="true" width="120"></t:dgCol>
					
			<t:dgToolBar title="查看" icon="icon-search"
				url="staffController.do?goUpdate" funname="detail"></t:dgToolBar>
			</t:datagrid>
	</div>
</div>
<script type="text/javascript" >
/**
 * 更新事件打开窗口
 * @param title 编辑框标题
 * @param addurl//目标页面地址
 * @param id//主键字段
 */

function d(title,url, id,width,height) {
	var s=getstaffListSelected(companyId);
	alert(s);
	var rowsData = $('#'+id).datagrid('getSelections');
	if (!rowsData || rowsData.length == 0) {
		tip('请选择查看项目');
		return;
	}
	if (rowsData.length > 1) {
		tip('请选择一条记录再查看');
		return;
	}
    url += '&load=detail&id='+rowsData[0].id;
	createdetailwindow(title,url,width,height);
}
</script>