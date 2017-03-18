<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
	<table id="sheduletime_Table"></table>  			
	<script type="text/javascript">
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
        url: 'systemController.do?documentList&staffid=${staffid}&typecode=files',
       
        columns: [{field:'id',hidden:true,title:'id'},
		{field:'documenttype',title:'类型',width:80 ,formatter: function(value){return format(value);}}
        , 
		{field:'documentTitle',title:'标题',width:120}
        ,
		{field:'createdate',title:'类型',width:80}
		
	]
	
	,
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
            text: '撤销', iconCls: 'icon-redo', handler: function () {
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
		
		if("1"==value)return "简历";
		if("2"==value)return "基础信息表";
		if("3"==value)return "身份证";
		if("4"==value)return "学历证明";
		if("5"==value)return "合同协议";
		if("6"==value)return "资质证书";
		if("7"==value)return "offer";
		if("8"==value)return "其它";
	}
      </Script>
	