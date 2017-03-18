<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<title>考勤记录导出</title>
</head>
<body>
<t:formvalid formid="formobj" dialog="true" usePlugin="password"
		layout="table" action=" " tiptype="1">
		<input id="id" name="id" type="hidden" value="${staffPage.id }">
		<table style="width:360px;" cellpadding="0" cellspacing="1"
			class="formtable">
			<tr>
				<td align="right"><label class="Validform_label"> 公&#12288&#12288司:
				</label></td>
				<td class="value"> <select id="companyId" name="companyId" style="width: 260px" >
						<option value="">---请选择---</option>
						<c:forEach items="${TSDeparts}" var="list">
							<option value="${list.id}">${list.departname}</option>
						</c:forEach>
				</select>   <span class="Validform_checktip"> </span> <label
					class="Validform_label" style="display: none;">公司</label></td>
					
			</tr>
			<tr>
					<td align="right"><label class="Validform_label"> 部&#12288&#12288门: </label>
				</td>
				<td class="value">
				 <input id="departname"  type="text" readonly="readonly" class="inputxt"  style="width: 203px" value="请先选择公司！">
                <input id="deptId" name="deptId"  type="hidden"   >
                <a href="#" class="easyui-linkbutton" plain="true" icon="icon-search" id="departSearch" onclick="openDepartmentSelect()">选择</a>
                 <span class="Validform_checktip"> </span>          
				 <label class="Validform_label"
					style="display: none;">部门</label></td>
				
			</tr>
			<tr>
					<td align="right"><label class="Validform_label"> 名&#12288&#12288字: </label>
				</td>
				<td class="value">
				 <input id="name" name="name" type="text"  class="inputxt"  style="width: 253px">
                  <span class="Validform_checktip"> </span>          
				 <label class="Validform_label"
					style="display: none;">名字</label></td>
				
			</tr>
			 <tr>
					<td align="right"><label class="Validform_label"> 日&#12288&#12288期: </label>
				</td>
				<td class="value">
				<input id="startDate"  
					name="startDate" type="text" style="width: 115px"
					class="Wdate" onClick="WdatePicker()"> ~ <input id="endDate"  
					name="endDate" type="text" style="width: 115px"
					class="Wdate" onClick="WdatePicker()">
				  <span class="Validform_checktip"> <font color="red">*</font></span>          
				 <label class="Validform_label"
					style="display: none;">日期</label></td>
				
			</tr>		

		</table>
	</t:formvalid>

</body>


<script type="text/javascript">
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
	
</script>
</html>