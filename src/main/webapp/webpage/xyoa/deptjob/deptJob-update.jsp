<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>岗位表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="deptJobController.do?doUpdate" tiptype="1" >
					<input id="id" name="id" type="hidden" value="${deptJobPage.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								部门ID:
							</label>
						</td>
						<td class="value">
						     	
					
					 <input id="departname"  type="text" readonly="readonly" class="inputxt" datatype="*" value="${departname}">
                    <input id="deptId" name="deptId"  type="hidden" value="${deptJobPage.deptId }">
                     <a href="#" class="easyui-linkbutton" plain="true" icon="icon-search" id="departSearch" onclick="openDepartmentSelect()">选择</a>
                     <a href="#" class="easyui-linkbutton" plain="true" icon="icon-redo" id="departRedo" onclick="callbackClean()">清空</a>
                
					<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">部门ID</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								岗位名称:
							</label>
						</td>
						<td class="value">
						     	 <input id="jobName" name="jobName" type="text" style="width: 150px" class="inputxt"  value='${deptJobPage.jobName}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">岗位名称</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								创建日期:
							</label>
						</td>
						<td class="value">
									  <input id="createTime" name="createTime"  type="text" disabled="true" style="width: 150px"  class="Wdate" onClick="WdatePicker()" value='<fmt:formatDate value='${deptJobPage.createTime}' type="date" pattern="yyyy-MM-dd"/>'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">创建日期</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								创建人:
							</label>
						</td>
						<td class="value">
						     	 <input id="createUser" name="createUser" type="text" disabled="true" style="width: 150px" class="inputxt"  value='${deptJobPage.createUser}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">创建人</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								更新日期:
							</label>
						</td>
						<td class="value">
									  <input id="updateTime" name="updateTime" type="text" disabled="true" style="width: 150px"  class="Wdate" onClick="WdatePicker()" value='<fmt:formatDate value='${deptJobPage.updateTime}' type="date" pattern="yyyy-MM-dd"/>'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">更新日期</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								更新人:
							</label>
						</td>
						<td class="value">
						     	 <input id="updateUser" name="updateUser" type="text" disabled="true" style="width: 150px" class="inputxt"  value='${deptJobPage.updateUser}'>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">更新人</label>
						</td>
					</tr>
			</table>
		</t:formvalid>
 </body>
  <script type="text/javascript">
  //$(document).ready(function(){
//	  $("#deptId").find("option[value='${deptJobPage.deptId}']").attr("selected",true);
	//}); 
  
  

  //选择部门部分js
	function openDepartmentSelect() {
			$.dialog.setting.zIndex = getzIndex(); 
			var orgIds = $("#deptId").val();			
			$.dialog({content: 'url:departController.do?departSelect&orgIds='+orgIds+',', zIndex: 3999, title: '组织机构列表', lock: true, width: '400px', height: '350px', opacity: 0.4, button: [
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
  
