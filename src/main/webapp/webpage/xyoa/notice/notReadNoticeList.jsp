<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
 <t:base type="jquery,easyui,tools"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="noticeList" title="common.notice" actionUrl="noticeController.do?datagrid3" idField="id" fit="true" sortName="createTime" sortOrder="desc">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="状态" field="isRead" width="40" replace="已读_1,未读_0"></t:dgCol>
   <t:dgCol title="标题" field="noticeTitle" width="120"></t:dgCol>
   <t:dgCol title="时间" field="createTime" formatter="yyyy-MM-dd hh:mm" width="80"></t:dgCol>
   <t:dgCol title="common.operation" field="opt" width="40"></t:dgCol>
   <t:dgFunOpt funname="doRead(id,isRead)" title="common.read"></t:dgFunOpt>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript" charset="utf-8">
  $('#noticeList').datagrid({   
	    rowStyler:function(index,row){   
	        if (row.isRead!=1){
	            return 'font-weight:bold !important;';   
	        }   
	    }
	});
  
  function doRead(id,isRead){
	 
	
	  if(isRead!=1){
		  var url = "noticeController.do?updateNoticeRead";
			$.ajax({
	    		url:url,
	    		type:"GET",
	    		dataType:"JSON",
	    		data:{
	    			noticeId:id
	    		},
	    		success:function(data){
	    			if(data.success){
	    				 
	    				 window.parent.document.getElementById('NoticeReadBut').click();
	    				
	    				$('#noticeList').datagrid({   
	    				    rowStyler:function(index,row){   
	    				        if (row.isRead!=1){
	    				            return 'font-weight:bold !important;';   
	    				        }else{
	    				        	return 'font-weight:normal;';
	    				        }
	    				    }   
	    				});
	    			}
	    		}
	    	});
	  }
	  var addurl = "noticeController.do?goDetail&id="+id;
		createdetailwindow("通知公告详情", addurl, 800, 500);
  }
  
  
  function refresh()
  {
	  
	  var url = "noticeController.do?getNoticeList";
		var roll = false;
		
		$.ajax({
  		url:url,
  		type:"GET",
  		dataType:"JSON",
  		async: false,
  		success:function(data){
  			if(data.success){
  				
  				var noticeList = data.attributes.noticeList;
  				var noticehtml = "";
  				if(noticeList.length > 0){
  					$("#noticenum").text(noticeList.length);
  					$("#noticenum2").text("您有"+noticeList.length+"未读公告");
  					$("#noticeTitle").show();
  					$("#notice").show();
  				}else{
  					
  					$("#noticeTitle").hide();
  					$("#notice").hide();
  				}
  			}
  		}
  	}); 
  }
 </script>