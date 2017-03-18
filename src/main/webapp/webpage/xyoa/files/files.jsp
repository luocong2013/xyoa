<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>文件列表</title>
<t:base type="jquery,easyui,tools"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
 
<t:formvalid formid="formobj" layout="div" dialog="true"  beforeSubmit="upload">
	<fieldset class="step">
	<div class="form">	
	 
		<label class="Validform_label"> 文件类型: </label> 
		<t:dictSelect id="documenttype" field="documenttype" type="list"
						typeGroupCode="documenttype" defaultVal="${doc.documenttype}" hasLabel="false"
						title="上传文件类型"></t:dictSelect><span>   </span>
		<label class="Validform_label"> 文件标题: </label> 
		<input name="documentTitle" id="documentTitle" datatype="s2-50" value="${doc.documentTitle}" type="text">				
	</div>
	<div class="form">
	
	<t:upload name="fiels" buttonText="上传文件" uploader="systemController.do?saveFiles&fileKey=${doc.id}&staffid=${staffid}" extend="*.jpg;*,jpeg;*.png;*.gif;*.bmp;*.ico;*.tif;*.doc;*.docx;*.txt;*.ppt;*.xls;*.xlsx;*.html;*.htm " id="file_upload" formData="documentTitle,documenttype"  ></t:upload>
		
	</div>
	<div class="form" id="filediv" style="height: 50px"></div>
	</fieldset>
</t:formvalid>
</body>
<script type="text/javascript">

 
 /* function refresh(){
	//$(window.opener.document).find("#addButton").click();
	 
	window.parent.document.getElementById("addButton").click();
	alert("hello");
	 
	 
}  */
</script>
</html>
