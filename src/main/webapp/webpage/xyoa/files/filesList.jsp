<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <%@include file="/context/mytags.jsp"%>

<t:base type="jquery,easyui,tools,DatePicker"></t:base> 
<div class="easyui-layout" fit="true">
<div region="center"  style="padding:0px;border:0px">
<t:datagrid queryMode="group"   fitColumns="true" name="fList" title="文件下载" 
actionUrl="systemController.do?documentList&staffid=${staffid}&typecode=files" idField="id" fit="true">
	<t:dgCol title="编号" field="id" hidden="true"></t:dgCol>	
	<t:dgCol title="类型" field="documenttype" replace="简历_1,基础信息表_2,身份证_3,学历证明_4,合同协议_5,资质证书_6,offer_7,其它_8" width="80" query="true" queryMode="single"></t:dgCol>
	<t:dgCol title="标题" field="documentTitle" width="120" queryMode="single" query="true"></t:dgCol>
	<t:dgCol title="创建时间" field="createdate"></t:dgCol>
	<t:dgCol title="类名" field="subclassname" hidden="true"></t:dgCol>
	<t:dgCol title="操作" field="opt"></t:dgCol>
	<t:dgDefOpt url="commonController.do?viewFile&fileid={id}&subclassname={subclassname}" title="下载"></t:dgDefOpt>
	<t:dgOpenOpt width="550" height="500" url="commonController.do?openViewFile&fileid={id}&subclassname={subclassname}" title="预览"></t:dgOpenOpt>
	<t:dgDelOpt url="systemController.do?delDocument&id={id}" title="删除"></t:dgDelOpt>
	<t:dgToolBar title="上传文件" icon="icon-add" funname="addfile" url="systemController.do?addFiles"></t:dgToolBar>
	<%-- <t:dgToolBar title="编辑" icon="icon-edit" funname="update" url="systemController.do?editFiles"></t:dgToolBar>
	 --%>
	   <button id="addButton" onclick="refresh();" hidden="true">sasssssghfgdfgdfgfd</button>  
</t:datagrid>
 

</div>
</div>
<div>
 
<input id="staffid" value="${staffid}" type="hidden"></div>

<script>

function addfile(title,addurl,gname,width,height) {
	gridname=gname;
	addurl=addurl+"&staffid="+$("#staffid").val();
	
	createwindow(title, addurl,655,300);
}
/**
 * 创建添加或编辑窗口
 * 
 * @param title
 * @param addurl
 * @param saveurl
 */
function createwindow(title, addurl,width,height) {
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
				saveObj();
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
				saveObj();
				return false;
		    },
		    cancelVal: '关闭',
		    cancel: true /*为true等价于function(){}*/
		});
	}
}
function saveObj() {	
	
	$('#btn_sub', iframe.document).click();	
	//	$('#sheduletime_Table').datagrid('reload');
	self.location.reload(); 
}

	

	
</script>
