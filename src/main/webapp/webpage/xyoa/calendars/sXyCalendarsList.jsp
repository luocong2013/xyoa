<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="sXyCalendarsList" checkbox="true" fitColumns="true" title="节假日明细表" actionUrl="sXyCalendarsController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="日期"  field="calendarday" formatter="yyyy-MM-dd"  query="true" queryMode="single"  width="80"></t:dgCol>
   <t:dgCol title="日期类型"  field="calendartype"  replace="工作日_B,双休日_W,节假日_H" query="true" queryMode="single"  width="60"></t:dgCol>
   <t:dgCol title="节假日"  field="remarks"   query="true" queryMode="single"  width="200"></t:dgCol>
   <t:dgCol title="创建时间"  field="ctime" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新时间"  field="utime" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人"  field="cuser"     queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="更新人"  field="uuser"    queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="80"></t:dgCol>  
   <t:dgFunOpt title="更改日期类型"  funname="uponedaytype(id)" />
   <t:dgToolBar title="初始化日历" icon="icon-add" url="sXyCalendarsController.do?goAdd" funname="init"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="sXyCalendarsController.do?goUpdate" funname="upclendar"></t:dgToolBar>
   <t:dgToolBar title="批量更改" icon="icon-put"  url="sXyCalendarsController.do?GoSetAllDayType" funname="SetBDayType"></t:dgToolBar>
   
  </t:datagrid>
  
 
  </div>
 </div>
 
 <script type="text/javascript">
 $(document).ready(function(){
 		//给时间控件加上样式
 			$("#sXyCalendarsListtb").find("input[name='calendarday']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyCalendarsListtb").find("input[name='ctime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 			$("#sXyCalendarsListtb").find("input[name='utime']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 
//批量设置日期类型
 function SetBDayType(title,url,gname,width,height) {

	 gridname=gname;
	    var ids = [];
	    var rows = $("#"+gname).datagrid('getSelections');
	    if (rows.length > 0) {
	    	$.dialog.setting.zIndex = getzIndex(true);
	    	
	    	$.messager.confirm('确认','您确认批量设置日期类型吗？', function(r) {
			   if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
						 
					}
					url += '&ids='+ids.join(',');						
					createwindow(title,url,616,160);
				}
			});
		} else {
			tip("请选择需要设置的数据！");
		}
 }
 

// 批量设置日期类型
function SetAllDayType(title,url,gname,daytype) {
	gridname=gname;
    var ids = [];
    var rows = $("#"+gname).datagrid('getSelections');
    if (rows.length > 0) {
    	$.dialog.setting.zIndex = getzIndex(true);
    	
    	$.messager.confirm('确认','您确认批量设置日期类型吗？', function(r) {
		   if (r) {
				for ( var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				$.ajax({
					url : url,
					type : 'post',
					data : {
						ids : ids.join(','),
						dayType : daytype
					},
					cache : false,
					success : function(data) {
						var d = $.parseJSON(data);
						if (d.success) {
							var msg = d.msg;
							tip(msg);
							reloadTable();
							$("#"+gname).datagrid('unselectAll');
							ids='';
						}
					}
				});
			}
		});
	} else {
		tip("请选择需要设置的数据！");
	}
}


	
/**
 * 初始化事件打开窗口
 * @param title 编辑框标题
 * @param addurl//目标页面地址
 */
function init(title,addurl,gname,width,height) {
	gridname=gname;
	$.messager.confirm('确认','初始化日期将会删除原有日期设置，确定初始化？', function(r) {
		   if (r) {
			   createwindow(title, addurl,616,160);
			}
		});
	
}

/**
 * 单个更改日期类型
 * @param title 编辑框标题
 * @param addurl//目标页面地址
 */
function uponedaytype(id) {
	$.messager.confirm('确认','更改日期类型？', function(r) {
		   if (r) {			   
			   url="${pageContext.request.contextPath}/sXyCalendarsController.do?goUpdate&id="+id;			 
			   createwindow("更改日期类型",url,616,130);
			}
		});
	
}

/**
 * 更新事件打开窗口
 * @param title 编辑框标题
 * @param addurl//目标页面地址
 * @param id//主键字段
 */
function upclendar(title,url, id,isRestful) {
	gridname=id;
	var rowsData = $('#'+id).datagrid('getSelections');
	if (!rowsData || rowsData.length==0) {
		tip('请选择编辑项目');
		return;
	}
	if (rowsData.length>1) {
		tip('请选择一条记录再编辑');
		return;
	}
	if(isRestful!='undefined'&&isRestful){
		url += '/'+rowsData[0].id;
	}else{
		url += '&id='+rowsData[0].id;
	}
	createwindow(title,url,616,140);
}
 </script>