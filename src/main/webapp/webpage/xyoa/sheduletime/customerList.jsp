<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
	<table id="sheduletime_Table"></table>  			
	<script type="text/javascript">
	var Address = [{ "value": "01", "text": "早班" }, { "value": "02", "text": "晚班" }, { "value": "03", "text": "休息" }, { "value": "04", "text": "加班" }];
	 
	 
	$(document).ready(function(){  
		GetTable();  
});  
	function GetTable() {
    var editRow = undefined;
 
    $("#sheduletime_Table").datagrid({
       // height: 300,
     //   width: 450,
        title: '',
 //       collapsible: true,
        singleSelect: true,
        url: 'xyWorkScheduleController.do?getSchedulejson&sheduletimeId=${sheduletimeId}',
       
        columns: [[{field:'staffid',hidden:true,title:'员工id'},
		{field:'staffname', width:90,align:'center',rowspan:2,title:'姓名'},
		{field:'mon',width:80,formatter: function(value){return format(value);}
		,editor:{type:'combobox', options:{data:Address, valueField:'value', textField:'text'}},title:'${oneweek.mon}'},
		{field:'tue',width:80,formatter: function(value){return format(value);}
		,editor:{type:'combobox', options:{data:Address, valueField:'value', textField:'text'}},title:'${oneweek.tue}'},
		{field:'wed',width:80,formatter: function(value){return format(value);}
		,editor:{type:'combobox', options:{data:Address, valueField:'value', textField:'text'}},title:'${oneweek.wed}'},
		{field:'fri',width:80,formatter: function(value){return format(value);}
		,editor:{type:'combobox', options:{data:Address, valueField:'value', textField:'text'}},title:'${oneweek.thu}'},
		{field:'thu',width:80,formatter: function(value){return format(value);}
		,editor:{type:'combobox', options:{data:Address, valueField:'value', textField:'text'}},title:'${oneweek.fri}'},
		{field:'sat',width:80,formatter: function(value){return format(value);}
		,editor:{type:'combobox', options:{data:Address, valueField:'value', textField:'text'}},title:'${oneweek.sat}'},
		{field:'sun',width:80,formatter: function(value){return format(value);}
		,editor:{type:'combobox', options:{data:Address, valueField:'value', textField:'text'}},title:'${oneweek.sun}'},
		{field:'starttime',hidden:true,title:'开始时间'},
		{field:'endtime',hidden:true,title:'结束时间'}
	],
	[
       {title:'星期一'},{title:'星期二'},{title:'星期三'},
       {title:'星期四'},{title:'星期五'},{title:'星期六'},{title:'星期天'}
	]
	],
        toolbar: [  '-', {
            text: '修改', iconCls: 'icon-edit', handler: function () {
               var row = $("#sheduletime_Table").datagrid('getSelected');
                if (row != null) {
                    if (editRow != undefined) {
                        $("#sheduletime_Table").datagrid('endEdit', editRow);
                    }
 
                    if (editRow == undefined) {
                        var index = $("#sheduletime_Table").datagrid('getRowIndex', row);
                        $("#sheduletime_Table").datagrid('beginEdit', index);
                        editRow = index;
                        $("#sheduletime_Table").datagrid('unselectAll');
                    }
                } else {
 
                }
            }
        },'-', {
            text: '保存', iconCls: 'icon-save', handler: function () {
                $("#sheduletime_Table").datagrid('endEdit', editRow);
 
                //如果调用acceptChanges(),使用getChanges()则获取不到编辑和新增的数据。
 
                //使用JSON序列化datarow对象，发送到后台。
                var rows = $("#sheduletime_Table").datagrid('getChanges');
				if(rows!=null&&rows!=""){
                var rowstr = JSON.stringify(rows);
               $.post("xyWorkScheduleController.do?updateSchedule", {"rowstr":rowstr,"sheduletimeId":"${sheduletimeId}"}, function (data) {
					$("#sheduletime_Table").datagrid('acceptChanges');
					$.messager.show({
						title:'提示信息',
						msg:'排班修改成功！',
						timeout:5000,
						showType:'slide'
					});

	             },"json");}
				else{
					$.messager.show({
						title:'提示信息',
						msg:'请修改排班！',
						timeout:5000,
						showType:'slide'
					});
				}
 
            }
        }, '-', {
            text: '重置', iconCls: 'icon-redo', handler: function () {
                editRow = undefined;
                $("#sheduletime_Table").datagrid('rejectChanges');
                $("#sheduletime_Table").datagrid('unselectAll');
            }
        }],
        onAfterEdit: function (rowIndex, rowData, changes) {
            editRow = undefined;
        },
        onDblClickRow: function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#sheduletime_Table").datagrid('endEdit', editRow);
            }
 
            if (editRow == undefined) {
                $("#sheduletime_Table").datagrid('beginEdit', rowIndex);
                editRow = rowIndex;
            }
        },
        onClickRow: function (rowIndex, rowData) {
            if (editRow != undefined) {
                $("#sheduletime_Table").datagrid('endEdit', editRow);
 
            }
 
        }
 
    });
} 
	function format(value)
	{
		if("01"==value)return "早班";
		if("02"==value)return "晚班";
		if("03"==value)return "休息";
		if("04"==value)return "加班";
	}
      </Script>
	