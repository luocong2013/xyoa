<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>


  <t:datagrid name="tSNoticeDeptList" checkbox="true" fitColumns="false" title="按部门授权" actionUrl="tSNoticeDeptController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="通知id"  field="noticeId"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="部门"  field="deptId" replace="${replacedepart}" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="tSNoticeDeptController.do?doDel&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="tSNoticeDeptController.do?doAdd" funname="addDepart"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="tSNoticeDeptController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
   </t:datagrid>
 <input type="hidden" id="pNoticeId" value="${noticeId}" />
 <script src = "webpage/xyoa/notice/tSNoticeDeptList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 		//给时间控件加上样式
 });

	function addDepart(title,url, id){
		$.dialog({
			width:600,
			height:400,
	        id: 'LHG1976D',
	        title: "选择通知公告授权部门",
	        max: false,
	        min: false,
	        resize: false,
	        content: 'url:departController.do?departSelect',
	        lock:true,
	        ok: function(){
	        	
		    	var noticeId = $("#pNoticeId").val();	    	
		    	var iframe = this.iframe.contentWindow;
				  var treeObj = iframe.$.fn.zTree.getZTreeObj("departSelect");
				  var nodes = treeObj.getCheckedNodes(true);
				  if(nodes.length>0){
				  var deptId='';
				  for(i=0;i<nodes.length;i++){
				     var node = nodes[i];
				     deptId += node.id;			    
				 }	
				}
				  if(deptId==""){
			    		return false;
			    	}else{
						url += '&deptId='+deptId;
						url += '&noticeId='+noticeId;
						doAjax(url);
			    	}	
		    },
	        close: function(){
	        }
	    });
}
	
	function doAjax(url) {
		
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			url : url,// 请求的action路径
			error : function() {// 请求失败处理函数
			},
			success : function(data) {
				var d = $.parseJSON(data);
				if (d.success) {
					tip(d.msg);
					$('#tSNoticeDeptList').datagrid('reload',{});
				}		
			}
		});
	}

 </script>