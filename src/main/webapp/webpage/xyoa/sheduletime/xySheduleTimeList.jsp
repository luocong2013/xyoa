<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div id="shedulelist" class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="xySheduleTimeList"  fitColumns="false" title="排班日期管理" actionUrl="xySheduleTimeController.do?sheduletimeid" idField="id" fit="true" treegrid="true"  pagination="false">
   <t:dgCol title="主键"  field="id"  hidden="true"  treefield="id" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="时间"  field="timename"    queryMode="single"  width="250" treefield="text"></t:dgCol>
  <t:dgCol title="操作" field="opt" width="90"></t:dgCol>
   <t:dgDelOpt title="删除" url="xySheduleTimeController.do?doDel&id={id}" exp="parentId#eq#" />
    <t:dgFunOpt funname="operationData(id)" title="客服部排班" exp="state#eq#open"></t:dgFunOpt>
   <t:dgToolBar title="按年录入" icon="icon-add" url="xySheduleTimeController.do?goAdd" funname="init"></t:dgToolBar>
   <%-- <t:dgToolBar title="编辑" icon="icon-edit" url="xySheduleTimeController.do?goUpdate" funname="update"></t:dgToolBar>
 --%>  </t:datagrid>
  </div>
 </div>
 <div data-options="region:'east',
	title:'客服部排班 ',
	collapsed:true,
	split:true,
	border:false,
	onExpand : function(){
		li_east = 1;
	},
	onCollapse : function() {
	    li_east = 0;
	}"
	style="width: 700px; overflow: hidden;">
<div class="easyui-panel" style="padding:0px;border:0px" fit="true" border="false" id="operationDetailpanel"></div>
</div>

<script type="text/javascript">
/**
 * 初始化事件打开窗口
 * @param title 编辑框标题
 * @param addurl//目标页面地址
 */
function init(title,addurl,gname,width,height) {
		gridname=gname;
		createwindow(title, addurl,610,100);
	}

$(function() {
	var li_east = 0;
});
//排班
function  operationData(sheduletimeId){
	
	if(li_east == 0){
	   $('#shedulelist').layout('expand','east'); 
	}
	$('#operationDetailpanel').panel("refresh", "xySheduleTimeController.do?goCustomer&sheduletimeId="+sheduletimeId);
}

</script>	
 