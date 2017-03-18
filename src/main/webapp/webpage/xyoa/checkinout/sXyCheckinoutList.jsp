<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
<div region="center" style="padding:0px;border:0px">
<div id="tempSearchColums" style="display: none;">
    <div name="searchColums">
    	<span style="display:-moz-inline-box;display:inline-block;">
			<span style="vertical-align:middle;display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;" title="所属部门">
				所属部门：
			</span>
		<input readonly="readonly" type="text" id="departname" name="departname" style="width: 200px" onclick="openDepartmentSelect()"  />
		<input id="orgId" name="orgId" type="hidden" />
		</span>
    </div>
</div>
  <t:datagrid name="sXyCheckinoutList" checkbox="false" fitColumns="false" title="考勤统计表" actionUrl="sXyCheckinoutController.do?datagrid"
  				    idField="id" fit="true" queryMode="group" sortName="checkDate" sortOrder="desc" 
  				    onDblClick="onDbClick_table_row">
   <t:dgCol title="id"  field="id"  hidden="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="姓名"  field="staffId"  hidden="true" query="fllse"  queryMode="single"  width="120"  ></t:dgCol>
   <t:dgCol title="姓名"  field="name"  align="center" query="true"  queryMode="single"  width="100"  ></t:dgCol>
   <t:dgCol title="部门"  field="deptId"  replace="${deptname}" align="center" queryMode="single"  width="100"></t:dgCol>
   <t:dgCol title="考勤日期"  field="checkDate" formatter="yyyy-MM-dd" align="center" query="true" queryMode="group"  width="100" ></t:dgCol>
   <t:dgCol title="上班时间"  field="workTime"  align="center"  queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="下班时间"  field="offWorkTime"  align="center"  queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="考勤状态"  field="isCheckTrue"  align="center" query="true" queryMode="single" width="100"  replace="正常_00,异常_01"></t:dgCol>
   <t:dgCol title="异常分钟数"  field="exceptionMinute"  align="center"  queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="异常原因"  field="exceptionRemarks" hidden="true"   queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="考勤类型"  field="checkType"   hidden="true" queryMode="group"  width="100" ></t:dgCol>
   <t:dgCol title="考勤原因"  field="checkRemarks" hidden="true"   queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="申请人ID"  field="applyId" hidden="true"  queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="日期类型"  field="dateType" align="center" query="true" queryMode="single"   width="100" replace="工作日_B,休息日_W,节假日_H" ></t:dgCol>
   <t:dgCol title="加班时长"  field="workHour" align="center"  queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="修改时间"  field="uTime" hidden="true" align="center" formatter="yyyy-MM-dd hh:mm:ss"   queryMode="group"  width="150"></t:dgCol>
   <t:dgCol title="修改人"  field="uUser"  hidden="true" align="center" queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="考勤系统员工ID"  field="userId" hidden="true"  queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="流程状态"  field="flowState" hidden="true" queryMode="group"  width="100"></t:dgCol>
   <t:dgCol title="流程实例ID"  field="flowInstId"  hidden="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" align="center" width="100"></t:dgCol>
   <t:dgFunOpt title="查看考勤流水"  funname="elecrecord(userId,checkDate)" />
   <t:dgToolBar title="编辑" icon="icon-edit" url="sXyCheckinoutController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="查看详情" icon="icon-search" url="sXyCheckinoutController.do?goUpdate" funname="detail"></t:dgToolBar>
   <t:dgToolBar title="导出考勤记录" icon="icon-putout" funname="importChecklist"></t:dgToolBar>
   <t:dgToolBar title="导出考勤异常统计信息" icon="icon-putout" url="sXyCheckinoutController.do?goPutOutXls" funname="goPutOutXls"></t:dgToolBar>
   <t:dgToolBar title="导出考勤汇总" icon="icon-putout" url="sXyCheckinoutController.do?goPutOutXls" funname="impCheckSumXls"></t:dgToolBar>
  
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
   //给时间控件加上样式
    $("#sXyCheckinoutListtb").find("input[name='checkDate_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
    $("#sXyCheckinoutListtb").find("input[name='checkDate_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
    $("#sXyCheckinoutListtb").find("div[name='searchColums']").find("form#sXyCheckinoutListForm").append($("#tempSearchColums div[name='searchColums']").html());
	$("#tempSearchColums").html('');
 });
 
 function onDbClick_table_row(rowIndex,rowData) {
	 createdetailwindow("查看详情",'${pageContext.request.contextPath}/sXyCheckinoutController.do?goUpdate&load=detail&id='+rowData.id);
 }
 
 function importChecklist() {      
       url="${pageContext.request.contextPath}/sXyCheckinoutController.do?goImportChecklist"; 
       var name="考勤记录导出筛选";
    
      openwindow(name,url,360,190); 
 }  
 
//导入
function ImportXls() {
 openuploadwin('Excel导入', 'sXyCheckinoutController.do?upload', "sXyCheckinoutList");
}
//导出
function ExportXls() {
 JeecgExcelExport("sXyCheckinoutController.do?doImportChecklist","sXyCheckinoutList");
}
//模板下载
function ExportXlsByT() {
 JeecgExcelExport("sXyCheckinoutController.do?exportXlsByT","sXyCheckinoutList");
}

function elecrecord(userId,checkDate) {
      url="${pageContext.request.contextPath}/sXyCheckinoutController.do?pCheckList&userId="+userId+"&checkDate="+checkDate; 
      var name="考勤流水列表";
      createwindow(name,url,680,400);
}
/**
 * 导出考勤异常信息
 */
function goPutOutXls(title,addurl,gname,width,height) {
 gridname=gname;
 title = '选择导出条件';
 createwindowputout(title, addurl,450,200,"00");
}
/**
 * 导出考勤异常信息
 */
function impCheckSumXls(title,addurl,gname,width,height) {
 gridname=gname;
 title = '选择导出条件';
 createwindowputout(title, addurl,450,200,"01");
}

/**
 * 创建添加或编辑窗口
 * 
 * @param title
 * @param addurl
 * @param saveurl
 */
function openwindow(title, addurl,width,height) {
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
       return save();
     
    // return false;
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
    return save();
      
   //  return false;
      },
      cancelVal: '关闭',
      cancel: true /*为true等价于function(){}*/
  });
 }
}
function save() {
 var flag=check_null(); 
 if(flag!=false)
// $('#btn_sub', iframe.document).click();
 { 
  var companyId=$("#companyId",iframe.document).val();
  var deptId=$("#deptId",iframe.document).val();
  var name=$("#name",iframe.document).val();
  var startDate=$("#startDate",iframe.document).val();
  var endDate=$("#endDate",iframe.document).val();
 JeecgExcelExport("sXyCheckinoutController.do?doImportChecklist&companyId="+companyId+"&deptId="+deptId+"&name="+name+"&startDate="+startDate+"&endDate="+endDate,"sXyCheckinoutList"); 
 }
 return flag;
}
function check_null()
{
 if($("#startDate",iframe.document).val()==""  && $("#endDate",iframe.document).val()=="")
 {
 alert("请填写日期范围!");
 return false;
 }
 if($("#startDate",iframe.document).val()==""  && $("#endDate",iframe.document).val()!="")
  {
  alert("请填写开始范围日期!");
  return false;
  }
 if($("#startDate",iframe.document).val()!=""  && $("#endDate",iframe.document).val()=="")
 {
 alert("请填写结束范围日期!");
 return false;
 }
 if($("#startDate",iframe.document).val()!=""  && $("#endDate",iframe.document).val()!="")
 {
     var beginDate=$("#startDate",iframe.document).val();      
  //   var day= beginDate.substring(8,10);    
     var startday=Number(beginDate.substring(8,10)); 
     var startmonth=Number(beginDate.substring(5,7));
  
     var endDate=$("#endDate",iframe.document).val(); 
     var endday=Number(endDate.substring(8,10));
     var endmonth=Number(endDate.substring(5,7));
  
    
     var d1 = new Date(beginDate.replace(/\-/g, "\/"));  
     var d2 = new Date(endDate.replace(/\-/g, "\/"));  
      if(d1>d2)  
     {  
      alert("时间范围开始日期不能大于结束日期！");  
      return false;  
     } 
      
    if((endmonth-startmonth)!=1){
     alert("日期查询范围为当月21号 到 次月 20号！");
    return false;
    }
  if(startday<21){
      alert("开始日期为应该大于此月20号！");
      return false;
     }
  if(endday>20){
      alert("结束日期应该小于次月21号！");
      return false;
 }
      
 return true;    
 } 
}

function createwindowputout(title, addurl,width,height,type) {
 width = width?width:700;
 height = height?height:400;
 if(width=="100%" || height=="100%"){
  width = window.top.document.body.offsetWidth;
  height =window.top.document.body.offsetHeight-100;
 }
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
       return mySave(type);
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
       return mySave(type);
      },
      cancelVal: '关闭',
      cancel: true /*为true等价于function(){}*/
  });
 }
}
function mySave(type) {
    var orgId = $.trim($("#orgId", iframe.document).val());
    var startDate = $.trim($("#startDate", iframe.document).val());
    var endDate = $.trim($("#endDate", iframe.document).val());
    var flag = checkData(startDate, endDate);
    if (flag) {
	    if("00"== type){
	        var url = 'sXyCheckinoutController.do?doPutOutXls';
	        var params = "&orgId=" + orgId + "&startDate=" + startDate + "&endDate=" + endDate;
	        window.location.href = url+ encodeURI(params);
	        tip("考勤异常统计表导出成功");
	    }
	    if("01"==type){
	        var url = 'sXyCheckinoutController.do?impCheckSumXls';
	        var params = "&orgId=" + orgId + "&startDate=" + startDate + "&endDate=" + endDate;
	        window.location.href = url+ encodeURI(params);
	        tip("考勤汇总表导出成功");
	    }
    }
    return flag;
}

function checkData(startDate, endDate) {
	if (startDate == '') {
		alertTip("请输入考勤开始日期");
		return false;
	} else if (endDate == '') {
		alertTip("请输入考勤结束日期");
		return false;
	}
	var sT = new Date(Date.parse(startDate.replace(/-/g, "/")));
	var eT = new Date(Date.parse(endDate.replace(/-/g, "/")));
	if (sT > eT) {
		alertTip("考勤开始日期不能大于结束日期");
		return false;
	}
	return true;
}
 </script>
