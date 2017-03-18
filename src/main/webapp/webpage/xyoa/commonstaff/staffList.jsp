<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 0px; border: 0px">
		<t:datagrid name="staffList"  fitColumns="true"
			title="享宇员工表" actionUrl="commonstaffController.do?datagrid" idField="id"
			fit="true" queryMode="group">
			<t:dgCol title="主键" field="id" hidden="true" queryMode="single"
				width="120"></t:dgCol>
			<t:dgCol title="归属公司" field="companyId" replace="${replacecompany}" query="true"
				queryMode="single" dictionary="company" width="150"></t:dgCol>
			<t:dgCol title="部门" field="deptId"  replace="${replacedepart}" query="true" queryMode="single"
				dictionary="dept_id" width="100"></t:dgCol>
			<t:dgCol title="姓名" field="name" query="true" queryMode="single"
				width="100"></t:dgCol>
			<t:dgCol title="性别" field="sex"  queryMode="single"
				dictionary="sex" width="40"></t:dgCol>			
			<t:dgCol title="手机" field="mobile" queryMode="single" width="100"></t:dgCol>
			<t:dgCol title="email" field="email" queryMode="single" width="130"></t:dgCol>			
			<t:dgCol title="操作" field="opt" width="50"></t:dgCol>
			<t:dgFunOpt title="编辑"  funname="updatestaff(id,name)" exp="id#eq#${staff.id}"></t:dgFunOpt>	
			<t:dgFunOpt title="查看"  funname="detailstaff(id,name)" exp="id#eq#${staff.id}"></t:dgFunOpt>
					
		</t:datagrid>
	</div>
</div>
<<script type="text/javascript">

function updatestaff(id,name) {			   
		 url="${pageContext.request.contextPath}/commonstaffController.do?goUpdate&id="+id;			 
		 var name=name +": 基本信息编辑";
		 createwindow(name,url,700,500);
	 //   openwindow(name,url,700,600);	
}
function detailstaff(id,name) {			   
		 url="${pageContext.request.contextPath}/commonstaffController.do?goUpdate&load=detail&id="+id;			 
	  	   var name=name +": 基本信息查看";
	   createdetailwindow(name,url,700,500);
	 //   openwindow(name,url,700,600);	
}
function showstaff(id) {			   
	   url="${pageContext.request.contextPath}/commonstaffController.do?goUpdate&load=detail&id="+id;			 
	   createdetailwindow("查看个人信息",url,616,480);	
}
</script>
