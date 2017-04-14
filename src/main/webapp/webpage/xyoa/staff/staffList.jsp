<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding: 0px; border: 0px">
		<t:datagrid name="staffList" checkbox="false" fitColumns="true"
			onDblClick="onDbClick_table_row" title="在职员工表"
			actionUrl="staffController.do?datagrid" idField="id" fit="true"
			queryMode="group">
			<t:dgCol title="主键" field="id" hidden="true" queryMode="single"
				width="120"></t:dgCol>
			<t:dgCol title="员工编号" field="sttaffId" query="true"
				queryMode="single" width="85"></t:dgCol>
			<t:dgCol title="归属公司" field="companyId" replace="${replacecompany}"
				query="true" queryMode="single" dictionary="company" width="170"></t:dgCol>
			<t:dgCol title="部门" field="deptId" replace="${replacedepart}"
				query="true" queryMode="single" dictionary="dept_id" width="120"></t:dgCol>
			<t:dgCol title="岗位" field="jobNo" queryMode="single"
				replace="${replacejob}" width="120"></t:dgCol>
			<t:dgCol title="姓名" field="name" query="true" queryMode="single"
				width="120"></t:dgCol>
			<t:dgCol title="性别" field="sex" queryMode="single" query="true"
				dictionary="sex" width="120"></t:dgCol>
			<t:dgCol title="状态" field="state" query="true" queryMode="single"
				dictionary="state" width="100"></t:dgCol>
			<t:dgCol title="工作年限" field="workYear" queryMode="single" width="77"></t:dgCol>
			<t:dgCol title="入本单位日期" field="goXyDate" formatter="yyyy-MM-dd"
				queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="正式转正日期" field="positiveDate" formatter="yyyy-MM-dd"
				queryMode="single" width="120"></t:dgCol>
			<t:dgCol title="劳动合同开始" field="contractStartDate"
				formatter="yyyy-MM-dd" queryMode="single" width="110"></t:dgCol>
			<t:dgCol title="劳动合同结束" field="contractEndDate"
				formatter="yyyy-MM-dd" queryMode="single" width="110"></t:dgCol>
			<%-- <t:dgCol title="备注" field="remarks" queryMode="single" width="120"></t:dgCol> --%>
			<t:dgCol title="操作" field="opt" width="150"></t:dgCol>
			<%-- <t:dgDelOpt title="删除" url="staffController.do?doDel&id={id}" /> --%>
			
			<% 
			if("true".equals(request.getAttribute("isQueryAll").toString())) {%>
			<t:dgFunOpt title="编辑" funname="updatestaff(id,name)" />
			<t:dgFunOpt title="查看" funname="detailstaff(id,name)" />
			
			<t:dgFunOpt title="离职" funname="changestate(id)" />
			
			<t:dgToolBar title="录入" icon="icon-add"
				url="staffController.do?goAdd" width="720" height="500"
				funname="addstaff"></t:dgToolBar>
			<t:dgToolBar title="编辑" icon="icon-edit"
				url="staffController.do?goUpdate" funname="update"></t:dgToolBar>
			<t:dgToolBar title="查看" icon="icon-search"
				url="staffController.do?goUpdate" width="720" height="500"
				funname="detail"></t:dgToolBar>
			<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
			<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
			<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
			<t:dgToolBar title="入职登记表下载" icon="icon-putout" funname="downObj"></t:dgToolBar>
			<%}else{ %>
			<t:dgFunOpt title="查看" funname="detailstaff(id,name)" />
			
			<t:dgToolBar title="查看" icon="icon-search"
				url="staffController.do?goUpdate" width="720" height="500"
				funname="detail"></t:dgToolBar>
			<t:dgToolBar title="入职登记表下载" icon="icon-putout" funname="downObj"></t:dgToolBar>
			
			<%} %>
		</t:datagrid>
	</div>
</div>

<script type="text/javascript">
	function onDbClick_table_row(rowIndex, rowData) {
		detailstaff(rowData.id, rowData.name);
	}

	/* 
	 function elecrecord(id,name) {			   
	 url="${pageContext.request.contextPath}/systemController.do?files&staffid="+id;	
	 var name="电子档案管理: "+name;
	 // var iTop = (window.screen.availHeight-30-400)/2 ; //获得窗口的垂直位置;
	 //   var iLeft = (window.screen.availWidth-10-680)/2; //获得窗口的水平位置;
	 //   window.open(url,"电子档案","menubar=no,titlebar=no,toolbar=no,location=no,status=no,width=680,height=400,left="+iLeft+",top="+iTop);
	 openwindow(name,url,680,400);	
	 } */
	function update(title, url, id, width, height, isRestful) {
		gridname = id;
		title = "基本信息编辑";
		var rowsData = $('#' + id).datagrid('getSelections');
		if (!rowsData || rowsData.length == 0) {
			tip('请选择编辑项目');
			return;
		}
		if (rowsData.length > 1) {
			tip('请选择一条记录再编辑');
			return;
		}
		if (isRestful != 'undefined' && isRestful) {
			url += '/' + rowsData[0].id;
		} else {
			url += '&id=' + rowsData[0].id;
			title = rowsData[0].name + ": 基本信息编辑";
		}
		createwindow(title, url, 720, 500);
		//createdetailwindow(title,url,width,height);
	}

	$(document).ready(
			function() {
				//给时间控件加上样式
				$("#staffListtb").find("input[name='graduationDate']").attr(
						"class", "Wdate").click(function() {
					WdatePicker({
						dateFmt : 'yyyy-MM-dd'
					});
				});
				$("#staffListtb").find("input[name='goXyDate']").attr("class",
						"Wdate").click(function() {
					WdatePicker({
						dateFmt : 'yyyy-MM-dd'
					});
				});
				$("#staffListtb").find("input[name='trialStartData']").attr(
						"class", "Wdate").click(function() {
					WdatePicker({
						dateFmt : 'yyyy-MM-dd'
					});
				});
				$("#staffListtb").find("input[name='trialEndData']").attr(
						"class", "Wdate").click(function() {
					WdatePicker({
						dateFmt : 'yyyy-MM-dd'
					});
				});
				$("#staffListtb").find("input[name='positiveDate']").attr(
						"class", "Wdate").click(function() {
					WdatePicker({
						dateFmt : 'yyyy-MM-dd'
					});
				});
				$("#staffListtb").find("input[name='contractStartDate']").attr(
						"class", "Wdate").click(function() {
					WdatePicker({
						dateFmt : 'yyyy-MM-dd'
					});
				});
				$("#staffListtb").find("input[name='contractEndDate']").attr(
						"class", "Wdate").click(function() {
					WdatePicker({
						dateFmt : 'yyyy-MM-dd'
					});
				});
				$("#staffListtb").find("input[name='birthday']").attr("class",
						"Wdate").click(function() {
					WdatePicker({
						dateFmt : 'yyyy-MM-dd'
					});
				});
				$("#staffListtb").find("input[name='createTime']").attr(
						"class", "Wdate").click(function() {
					WdatePicker({
						dateFmt : 'yyyy-MM-dd'
					});
				});
				$("#staffListtb").find("input[name='upudateTime']").attr(
						"class", "Wdate").click(function() {
					WdatePicker({
						dateFmt : 'yyyy-MM-dd'
					});
				});
			});

	//导入
	function ImportXls() {
		openuploadwin('Excel导入', 'staffController.do?upload', "staffList");
	}

	//导出
	function ExportXls() {
		JeecgExcelExport("staffController.do?exportXls", "staffList");
	}
	//导出员工登记表
	function downObj(title, url, id, width, height, isRestful) {
		gridname = id;
		url = "staffController.do?doadWordDoc";
		var rowsData = $('#' + id).datagrid('getSelections');
		if (!rowsData || rowsData.length == 0) {
			tip('请选择导出登记表员工');
			return;
		}
		if (rowsData.length > 1) {
			tip('请选择一条记录再导出登记表');
			return;
		}
		if (isRestful != 'undefined' && isRestful) {
			url += '/' + rowsData[0].id;
		} else {
			url += '&id=' + rowsData[0].id;
		}
		try {
			var elemIF = document.createElement("iframe");
			elemIF.src = url;
			elemIF.style.display = "none";
			document.body.appendChild(elemIF);
		} catch (e) {

		}
	}
	//模板下载
	function ExportXlsByT() {
		JeecgExcelExport("staffController.do?exportXlsByT", "staffList");
	}

	function detailstaff(id, name) {
		url = "${pageContext.request.contextPath}/staffController.do?goUpdate&load=detail&id="
				+ id;
		var name = name + ": 基本信息查看";
		createdetailwindow(name, url, 720, 500);
		//   openwindow(name,url,700,600);	
	}
	function updatestaff(id, name) {
		url = "${pageContext.request.contextPath}/staffController.do?goUpdate&id="
				+ id;
		var name = name + ": 基本信息编辑";
		createwindow(name, url, 720, 500);
		//   openwindow(name,url,700,600);	
	}
	function changestate(id) {
		url = "${pageContext.request.contextPath}/staffController.do?gochangestate&id="
				+ id;

		openwindow("员工离职", url, 370, 360);
	}

	function addstaff(title, addurl, gname, width, height) {
		gridname = gname;
		createwindow(title, addurl, width, height);
	}
	/**
	 * 创建添加或编辑窗口
	 * 
	 * @param title
	 * @param addurl
	 * @param saveurl
	 */
	function createwindow(title, addurl, width, height) {
		width = width ? width : 700;
		height = height ? height : 400;
		if (width == "100%" || height == "100%") {
			width = window.top.document.body.offsetWidth;
			height = window.top.document.body.offsetHeight - 100;
		}
		//--author：JueYue---------date：20140427---------for：弹出bug修改,设置了zindex()函数
		if (typeof (windowapi) == 'undefined') {
			$.dialog({
				content : 'url:' + addurl,
				lock : true,
				zIndex : getzIndex(),
				width : width,
				height : height,
				title : title,
				opacity : 0.3,
				cache : false,
				ok : function() {
					iframe = this.iframe.contentWindow;
					saveObj();
					return false;
				},
				cancelVal : '关闭',
				cancel : true
			/*为true等价于function(){}*/
			});
		} else {
			W.$.dialog({
				content : 'url:' + addurl,
				lock : true,
				width : width,
				zIndex : getzIndex(),
				height : height,
				parent : windowapi,
				title : title,
				opacity : 0.3,
				cache : false,
				ok : function() {
					iframe = this.iframe.contentWindow;
					saveObj();
					return false;
				},
				cancelVal : '关闭',
				cancel : true
			/*为true等价于function(){}*/
			});
		}
	}
	function saveObj() {
		var flag = check_null();
		if (flag != false)
			$('#btn_sub', iframe.document).click();
	}

	/**表单验证*/
	function check_null() {
		if ($('#companyId', iframe.document).val() == "") {
			alertTip("请选择归属公司！");
			return false;
		}
		if ($.trim($('#deptId', iframe.document).val()) == "") {
			alertTip("请选择部门！");
			return false;
		}
		if ($.trim($("input[name='roleid']", iframe.document).val()) == "") {
			alertTip("请选择角色！");
			return false;
		}
		if ($.trim($("input[name='name']", iframe.document).val()) == "") {
			alertTip("姓名不能为空！");
			return false;
		}
		if ($("select[name='state']", iframe.document).val() == "") {
			alertTip("请选择状态！");
			return false;
		}
		if ($("#isCheck", iframe.document).val() == "true") {
			var checkid = $("#checkId", iframe.document).val();
			if (checkid == "") {
				alertTip("请输入考勤id！");
				return false;
			}
		}
		if ($("#checkId", iframe.document).val() != "") {
			var checkid = $("#checkId", iframe.document).val();
			var v = checkid;
			var pattern = /^\d{1,11}$/;
			flag = pattern.test(v);
			if (!flag) {
				alertTip("考勤ID为1—11位整数！");
				return false;
			}
		}
		if ($("#checkidflag", iframe.document).val() == "1") {
			alertTip("考勤id已存在请重新输入！");
			return false;
		}
		if ($("#graduationDate", iframe.document).val() == "") {
			alertTip("请填写毕业时间！");
			return false;
		}
		if ($("#state", iframe.document).val() == "1") {
			var positiveDate = $("#positiveDate", iframe.document).val();
			if (positiveDate == "") {
				alertTip("请输入正式转正时间！");
				return false;
			}
		}
		if ($("#trialStartData", iframe.document).val() == "") {
			alertTip("请输入试用期开始时间！");
			return false;
		}
		if ($("#state", iframe.document).val() == "0") {
			var trialEndData = $("#trialEndData", iframe.document).val();
			if (trialEndData == "") {
				alertTip("请输入试用期结束时间！");
				return false;
			}
		}
		if ($("#trialStartData", iframe.document).val() != ""
				&& $("#trialEndData", iframe.document).val() != "") {
			var beginDate = $("#trialStartData", iframe.document).val();
			var endDate = $("#trialEndData", iframe.document).val();
			var d1 = new Date(beginDate.replace(/\-/g, "\/"));
			var d2 = new Date(endDate.replace(/\-/g, "\/"));
			if (d1 > d2) {
				alertTip("试用期开始时间不能大于结束时间！");
				return false;
			}
		}

		if ($("#contractStartDate", iframe.document).val() != ""
				&& $("#contractEndDate", iframe.document).val() != "") {
			var beginDate = $("#contractStartDate", iframe.document).val();
			var endDate = $("#contractEndDate", iframe.document).val();
			var d1 = new Date(beginDate.replace(/\-/g, "\/"));
			var d2 = new Date(endDate.replace(/\-/g, "\/"));
			if (d1 > d2) {
				alertTip("劳动合同开始时间不能大于结束时间！");
				return false;
			}
		}
		
		
		if ($('#offWorkCount', iframe.document).val() != "") {
			var v = $('#offWorkCount', iframe.document).val();
			//var pattern = new RegExp("^(0|[1-9][0-9]*)$");
			var pattern = new RegExp("^0(\\.\\d*)?$|^[1-9]\\d*(\\.\\d*)?$");
			flag = pattern.test(v);
			if (!flag) {
				alertTip("可调休天数有误！");
				return false;
			}
		}
		if ($('#leaveCount', iframe.document).val() != "") {
			var v = $('#leaveCount', iframe.document).val();
			//var pattern = new RegExp("^(0|[1-9][0-9]*)$");
			var pattern = new RegExp("^0(\\.\\d*)?$|^[1-9]\\d*(\\.\\d*)?$");
			flag = pattern.test(v);
			if (!flag) {
				alertTip("年假天数有误！");
				return false;
			}
		}
		if ($('#mobile', iframe.document).val() != "") {
			var v = $('#mobile', iframe.document).val();
			var pattern = new RegExp("^1\\d{10}$");
			flag = pattern.test(v);
			if (!flag) {
				alertTip("您的手机号码填写有误！");
				return false;
			}
		}
		if ($('#email', iframe.document).val() != "") {
			var v = $.trim($('#email', iframe.document).val());
			$('#email', iframe.document).val(v);
			var pattern = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;

			flag = pattern.test(v);
			if (!flag) {
				alertTip("您的email号码格式有误！");
				return false;
			}
		}
		if ($('#certNo', iframe.document).val() == "") {
			alertTip("请填写身份证号码！");
			return false;
		} else {
			var v = $.trim($('#certNo', iframe.document).val());
			$('#certNo', iframe.document).val(v);
			var pattern = /(^\d{15}$)|(^\d{17}([0-9]|X|x)$)/;

			flag = pattern.test(v);
			if (!flag) {
				alertTip("身份证号码格式有误，15或18位数！");
				return false;
			}
		}
		
		if ($('#age', iframe.document).val() != "") {
			var v = $('#age', iframe.document).val();
			var pattern = new RegExp("^[1-9][0-9]*$");

			flag = pattern.test(v);
			if (!flag) {
				alertTip("年龄为正整数！");
				return false;
			}
		}
		return true;
	}

	/**
	 * 创建添加或编辑窗口
	 * 
	 * @param title
	 * @param addurl
	 * @param saveurl
	 */

	function openwindow(title, addurl, width, height) {
		width = width ? width : 700;
		height = height ? height : 400;
		if (width == "100%" || height == "100%") {
			width = window.top.document.body.offsetWidth;
			height = window.top.document.body.offsetHeight - 100;
		}
		//--author：JueYue---------date：20140427---------for：弹出bug修改,设置了zindex()函数
		if (typeof (windowapi) == 'undefined') {
			$.dialog({
				content : 'url:' + addurl,
				lock : true,
				zIndex : getzIndex(),
				width : width,
				height : height,
				title : title,
				opacity : 0.3,
				cache : false,
				ok : function() {
					iframe = this.iframe.contentWindow;
					save();
					return false;
				},
				cancelVal : '关闭',
				cancel : true
			/*为true等价于function(){}*/
			});
		} else {
			W.$.dialog({
				content : 'url:' + addurl,
				lock : true,
				width : width,
				zIndex : getzIndex(),
				height : height,
				parent : windowapi,
				title : title,
				opacity : 0.3,
				cache : false,
				ok : function() {
					iframe = this.iframe.contentWindow;
					save();
					return false;
				},
				cancelVal : '关闭',
				cancel : true
			/*为true等价于function(){}*/
			});
		}
	}
	function save() {
		if (changstate_checkNull())
			$('#btn_sub', iframe.document).click();
	}
	function changstate_checkNull() {
		if ($('#abdicateDate', iframe.document).val() == "") {
			alertTip("请填写离职时间！");
			return false;
		} else
			return true;
	}
	function addTab(title, url, id, width, height, isRestful) {
		var rowsData = $('#' + id).datagrid('getSelections');
		if (!rowsData || rowsData.length == 0) {
			tip('请选择编辑项目');
			return;
		}
		if (rowsData.length > 1) {
			tip('请选择一条记录再编辑');
			return;
		}
		if (isRestful != 'undefined' && isRestful) {
			url += '/' + rowsData[0].id;
		} else {
			url += '&id=' + rowsData[0].id;
		}
		//createwindow(title,url,width,height);
		window.parent.addTabb(url, '111', title);

	};
</script>