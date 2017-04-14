<%--
  Created by IntelliJ IDEA.
  User: wangkun
  Date: 2016/4/23
  Time: 10:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/context/mytags.jsp"%>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">

    <title><t:mutiLang langKey="jeect.platform"/></title>

    <meta name="keywords" content="JEECG,后台bootstrap框架,会员中心主题,后台HTML,响应式后台">
    <meta name="description" content="JEECG是一个完全响应式，基于Bootstrap3最新版本开发的扁平化主题，她采用了主流的左右两栏式布局，使用了Html5+CSS3等现代技术">

    <link rel="shortcut icon" href="images/favicon.ico">
    <link href="plug-in-ui/hplus/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="plug-in-ui/hplus/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
    <link rel="stylesheet" href="plug-in/ace/assets/css/font-awesome.min.css" />
    <!--[if IE 7]>
    <link rel="stylesheet" href="plug-in/ace/assets/css/font-awesome-ie7.min.css" />
    <![endif]-->
    <!-- Sweet Alert -->
    <link href="plug-in-ui/hplus/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link href="plug-in-ui/hplus/css/animate.css" rel="stylesheet">
    <link href="plug-in-ui/hplus/css/style.css?v=4.1.0" rel="stylesheet">
</head>

<body class="fixed-sidebar full-height-layout gray-bg" style="overflow:hidden">
<div id="wrapper">
    <!--左侧导航开始-->
    <nav class="navbar-default navbar-static-side" role="navigation" style="z-index: 1991;">
        <div class="nav-close"><i class="fa fa-times-circle"></i>
        </div>
        <div class="sidebar-collapse">
            <ul class="nav" id="side-menu">
                <li class="nav-header">
                    <div class="dropdown profile-element">
                        <span><img alt="image" class="img-circle" src="plug-in/login/images/jeecg-aceplus.png" /></span>
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear">
                               <span class="block m-t-xs"><strong class="font-bold">协同办公系统</strong></span>
                               </span>
                        </a>
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear">
                               <span class="block m-t-xs"><strong class="font-bold">${realname}</strong></span>                               
                               <span class="text-muted text-xs block">${roleName }<b class="caret"></b></span>
                                </span>
                        </a>
                        <ul class="dropdown-menu animated fadeInRight m-t-xs">
                            <li>
                                <a href="javascript:add('<t:mutiLang langKey="common.change.password"/>','userController.do?changepassword','',550,200)">
                                    <t:mutiLang langKey="common.change.password"/>
                                </a>
                            </li>
                            <li><a href="javascript:createwindow('<t:mutiLang langKey="common.profile"/>','userController.do?userinfo',550,220)"><t:mutiLang langKey="common.profile"/></a></li>
                           <%--  <li><a href="javascript:openwindow('<t:mutiLang langKey="common.ssms.getSysInfos"/>','tSSmsController.do?getSysInfos')"><t:mutiLang langKey="common.ssms.getSysInfos"/></a></li>
                            <li><a href="javascript:add('<t:mutiLang langKey="common.change.style"/>','userController.do?changestyle','',550,250)"><t:mutiLang langKey="common.my.style"/></a></li>--%> 
                            <li><a href="javascript:clearLocalstorage()"><t:mutiLang langKey="common.clear.localstorage"/></a></li>                          
                            <li class="divider"></li>
                            <li><a href="javascript:logout()">注销</a></li>
                        </ul>
                    </div>
                    <div class="logo-element">OA
                    </div>
                </li>

                <t:menu style="hplus" menuFun="${menuMap}"></t:menu>

            </ul>
        </div>
    </nav>
    <!--左侧导航结束-->
    <!--右侧部分开始-->
    <div id="page-wrapper" class="gray-bg dashbard-1">
        <div class="row border-bottom">
            <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
                <div class="navbar-header" style="height: 60px;"><a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>
                    <form role="search" class="navbar-form-custom" method="post" action="search_results.html">
                        <div class="form-group">
                            <input type="text" placeholder="欢迎使用本系统……" class="form-control" name="top-search" id="top-search">
                        </div>
                    </form>
                </div>
                <ul class="nav navbar-top-links navbar-right">
                    <li class="dropdown">
                        <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                           公告 <i class="fa fa-envelope"></i> <span class="label label-warning" id="noticenum">0</span>
                        </a>
                        <ul class="dropdown-menu dropdown-alerts">
                            <li>
                                <a class="" href="javascript:goNotReadNotice();">
                                    <div>
                                        <i class="fa fa-envelope fa-fw"></i> <div id="noticenum2">您有0条未读消息</div>
                                        <%--<span class="pull-right text-muted small">4分钟前</span>--%>
                                    </div>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <div class="text-center link-block">
                                    <a class="" href="javascript:goAllNotice();">
                                        <i class="fa fa-envelope"></i> <strong> 查看所有消息</strong>
                                    </a>
                                    <button id="NoticeReadBut" onclick="queryNoReadeNotice();" hidden="true" >sasssss</button>
                                </div>
                            </li>
                        </ul>
                    </li>
                   <%--  <li class="dropdown">
                        <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                            <i class="fa fa-bell"></i> <span class="label label-primary">0</span>
                        </a>
                        <ul class="dropdown-menu dropdown-alerts">
                            <li>
                                <a>
                                    <div>
                                        <i class="fa fa-envelope fa-fw"></i> 您有0条未读消息
                                        <span class="pull-right text-muted small">4分钟前</span>
                                    </div>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <div class="text-center link-block">
                                    <a class="" href="javascript:goAllMessage();">
                                        <strong>查看所有 </strong>
                                        <i class="fa fa-angle-right"></i>
                                    </a>
                                </div>
                            </li>
                        </ul>
                    </li> --%>
                    <li class="dropdown hidden-xs">
                        <a class="right-sidebar-toggle" aria-expanded="false">
                            <i class="fa fa-tasks"></i> 主题
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
        <div class="row content-tabs">
            <button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i>
            </button>
            <nav class="page-tabs J_menuTabs">
                <div class="page-tabs-content">
                    <a href="javascript:;" class="active J_menuTab" data-id="loginController.do?hplushome">首页</a>                
                </div>
            <button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i>
            </button>
            <div class="btn-group roll-nav roll-right">
                <button class="dropdown J_tabClose" data-toggle="dropdown">关闭操作<span class="caret"></span>

                </button>
                <ul role="menu" class="dropdown-menu dropdown-menu-right">
                    <li class="J_tabShowActive"><a>定位当前选项卡</a>
                    </li>
                    <li class="divider"></li>
                    <li class="J_tabCloseAll"><a>关闭全部选项卡</a>
                    </li>
                    <li class="J_tabCloseOther"><a>关闭其他选项卡</a>
                    </li>
                </ul>
            </div>
                    <a href="javascript:logout()" class="roll-nav roll-right J_tabExit"><i class="fa fa fa-sign-out"></i> 退出</a>
        </nav>
        </div>
        <div class="row J_mainContent" id="content-main">
             <iframe class="J_iframe"  id="iframe0" name="iframe0" width="100%" height="100%" src="loginController.do?hplushome" frameborder="0" data-id="loginController.do?hplushome" seamless></iframe>
         	
         </div>
        <div class="footer">
            <div class="pull-right"><a href="http://math.cuit.edu.cn/" target="_blank">成都信息工程大学 ©2017 CUIT-SXXY</a>
            </div>
        </div>
    </div>
    <!--右侧部分结束-->
    <!--右侧边栏开始-->
    <div id="right-sidebar">
        <div class="sidebar-container">

            <ul class="nav nav-tabs navs-3">

                <li class="active">
                    <a data-toggle="tab" href="#tab-1">
                        <i class="fa fa-gear"></i> 主题
                    </a>
                </li>
                <!-- <li class=""><a data-toggle="tab" href="#tab-2">
                    通知
                </a>
                </li>
                <li><a data-toggle="tab" href="#tab-3">
                    项目进度
                </a>
                </li> -->
            </ul>

            <div class="tab-content">
                <div id="tab-1" class="tab-pane active">
                    <div class="sidebar-title">
                        <h3> <i class="fa fa-comments-o"></i> 主题设置</h3>
                        <small><i class="fa fa-tim"></i> 你可以从这里选择和预览主题的布局和样式，这些设置会被保存在本地，下次打开的时候会直接应用这些设置。</small>
                    </div>
                    <div class="skin-setttings">
                        <div class="title">主题设置</div>
                        <div class="setings-item">
                            <span>收起左侧菜单</span>
                            <div class="switch">
                                <div class="onoffswitch">
                                    <input type="checkbox" name="collapsemenu" class="onoffswitch-checkbox" id="collapsemenu">
                                    <label class="onoffswitch-label" for="collapsemenu">
                                        <span class="onoffswitch-inner"></span>
                                        <span class="onoffswitch-switch"></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="setings-item">
                            <span>固定顶部</span>

                            <div class="switch">
                                <div class="onoffswitch">
                                    <input type="checkbox" name="fixednavbar" class="onoffswitch-checkbox" id="fixednavbar">
                                    <label class="onoffswitch-label" for="fixednavbar">
                                        <span class="onoffswitch-inner"></span>
                                        <span class="onoffswitch-switch"></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="setings-item">
                                <span>
                        固定宽度
                    </span>

                            <div class="switch">
                                <div class="onoffswitch">
                                    <input type="checkbox" name="boxedlayout" class="onoffswitch-checkbox" id="boxedlayout">
                                    <label class="onoffswitch-label" for="boxedlayout">
                                        <span class="onoffswitch-inner"></span>
                                        <span class="onoffswitch-switch"></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="title">皮肤选择</div>
                        <div class="setings-item default-skin nb">
                                <span class="skin-name ">
                         <a href="#" class="s-skin-0">
                             默认皮肤
                         </a>
                    </span>
                        </div>
                        <div class="setings-item blue-skin nb">
                                <span class="skin-name ">
                        <a href="#" class="s-skin-1">
                            蓝色主题
                        </a>
                    </span>
                        </div>
                        <div class="setings-item yellow-skin nb">
                                <span class="skin-name ">
                        <a href="#" class="s-skin-3">
                            黄色/紫色主题
                        </a>
                    </span>
                        </div>
                    </div>
                </div>
              
            </div>

        </div>
    </div>
   
</div>

<!-- 全局js -->
<script src="plug-in-ui/hplus/js/jquery.min.js?v=2.1.4"></script>
<script src="plug-in-ui/hplus/js/bootstrap.min.js?v=3.3.6"></script>
<script src="plug-in-ui/hplus/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="plug-in-ui/hplus/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="plug-in-ui/hplus/js/plugins/layer/layer.min.js"></script>

<!-- 自定义js -->
<script src="plug-in-ui/hplus/js/hplus.js?v=4.1.0"></script>
<script type="text/javascript" src="plug-in-ui/hplus/js/contabs.js"></script>
<t:base type="tools"></t:base>
<!-- 第三方插件 -->
<script src="plug-in-ui/hplus/js/plugins/pace/pace.min.js"></script>
<!-- Sweet alert -->
<script src="plug-in-ui/hplus/js/plugins/sweetalert/sweetalert.min.js"></script>
<script src="plug-in/jquery-plugs/storage/jquery.storageapi.min.js"></script>
<!-- 弹出TAB -->
<script type="text/javascript" src="plug-in/hplus/hplus-tab.js"></script>
<script>
    function logout(){
        /*bootbox.confirm("<t:mutiLang langKey="common.exit.confirm"/>", function(result) {
            if(result)
                location.href="loginController.do?logout";
        });*/
        /*swal({
            title: "您确定要注销吗？",
            text: "注销后需要重新登录！",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            closeOnConfirm: false,
            closeOnCancel: true
        },
        function (isConfirm) {
            if (isConfirm) {
                //swal("注销成功！", "您已经成功注销。", "success");
                location.href="loginController.do?logout";
            } else {
                return false;
            }
        });*/
        layer.confirm('您确定要注销吗？', {
            btn: ['确定','取消'], //按钮
            shade: false //不显示遮罩
        }, function(){
            location.href="loginController.do?logout";
        }, function(){
            return;
        });
    }
    function clearLocalstorage(){
        var storage=$.localStorage;
        if(!storage)
            storage=$.cookieStorage;
        storage.removeAll();
        //bootbox.alert( "浏览器缓存清除成功!");
        layer.msg("浏览器缓存清除成功!");
    }
    function goAllNotice(){
        var addurl = "noticeController.do?noticeList";
        createdetailwindow("公告", addurl, 600, 400);
    }
    function goNotReadNotice(){
        var addurl = "noticeController.do?notReadNoticeList";
        createdetailwindow("公告", addurl, 600, 400);
    }
    function goAllMessage(){
        var addurl = "tSSmsController.do?getSysInfos";
        createdetailwindow("消息", addurl, 600, 400);
    }
    
    
  /*   $(document).ready(function(){
    	
	});
     */
    
    window.onload=myOnload;
    
    function myOnload()
    {
    	/**
    	//转正提醒
    	$.post("staffController.do?getTrialEndData",{async: false},function(data){  
            if(data!=null&&data.length>0){  
            	$("#iframe0").contents().find("#trialend").show();
          	   for(var i=0;i<data.length;i++){
   		       	var staffname = data[i].name;
   		        var staffenddate=data[i].certNo;
   		    	var staffdept = data[i].deptId;   	
   		       	//添加到单位名称的下拉菜单中
   		     	var tr= "<tr><td><font color='#676A6C'>"+(i+1)+". </font></td><td>"+staffname+"</td><td align='center'>"+staffdept+"</td><td>"+staffenddate+"</td></tr>";
   		       
   		     $("#iframe0").contents().find("#staffendtrial").append(tr); 
   	      	 }
            }
        },"json");*/
    	

    	
    	//审批提醒
    	$.post("ajaxactiviti.do?getApprove",{async: false},function(data){  	       	  
          if(data!=null){    
        	  	var flag=false;
            	  if(data.absenceSum>0){
            		  var li="<li><a href=javascript:addTab('sXyAbsenceController.do?listCheck','11','请假审批')>请假审批：等待您的批准 <font color='red' size='4' >("+data.absenceSum+")</font> 条，请查看</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li);  
                	  flag=true;
            	  }
            	  if(data.businesstripSum>0){
            		  var li="<li><a href=javascript:addTab('sXyBusinessTripController.do?listCheck','15','出差审批')>出差审批：等待您的批准 <font color='red' size='4' >("+data.businesstripSum+")</font> 条，请查看</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li); 
                	  flag=true;
            	  }
            	  if(data.compensateleaveSum>0){
            		  var li="<li><a href=javascript:addTab('sXyCompensateLeaveController.do?listCheck','13','调休审批')>调休审批：等待您的批准 <font color='red' size='4' >("+data.compensateleaveSum+")</font> 条，请查看</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li);  
                	  flag=true;
            	  }
            	  if(data.outworkSum>0){
            		  var li="<li><a href=javascript:addTab('sXyOutWorkController.do?listCheck','14','外出审批') >外出审批：等待您的批准 <font color='red' size='4' >("+data.outworkSum+")</font> 条，请查看</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li);  
                	  flag=true;
            	  }
            	  if(data.workovertimeSum>0){
            		  var li="<li><a href=javascript:addTab('sXyWorkOvertimeController.do?listCheck','12','加班审批')>加班审批：等待您的批准 <font color='red' size='4' >("+data.workovertimeSum+")</font> 条，请查看</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li);  
                	  flag=true;
            	  }
            	  if(data.checkApplySum>0){
            		  var li="<li><a href=javascript:addTab('sXyCheckApplyController.do?listCheck','7','考勤异常审批')>考勤异常审批：等待您的批准 <font color='red' size='4' >("+data.checkApplySum+")</font> 条，请查看</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li);  
                	  flag=true;
            	  }
            	  
            	  
            	  if(data.hrAbsenceSum>0){
            		  var li="<li><a href=javascript:addTab('sXyAbsenceController.do?listHr','21','请假备案')>请假备案：等待您的批准 <font color='red' size='4' >("+data.hrAbsenceSum+")</font> 条</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li);  
                	  flag=true;
            	  }
            	  if(data.hrBusinesstripSum>0){
            		  var li="<li><a href=javascript:addTab('sXyBusinessTripController.do?listHr','25','出差备案')>出差备案：等待您的批准 <font color='red' size='4' >("+data.hrBusinesstripSum+")</font> 条</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li);  
                	  flag=true;
            	  }
            	  if(data.hrCompensateleaveSum>0){
            		  var li="<li><a href=javascript:addTab('sXyCompensateLeaveController.do?listHr','23','调休备案')>调休备案：等待您的批准 <font color='red' size='4' >("+data.hrCompensateleaveSum+")</font> 条</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li); 
                	  flag=true;
            	  }
            	  if(data.hrOutworkSum>0){
            		  var li="<li><a href=javascript:addTab('sXyOutWorkController.do?listHr','24','外出备案')>外出备案：等待您的批准 <font color='red' size='4' >("+data.hrOutworkSum+")</font> 条</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li);  
                	  flag=true;
            	  }
            	  if(data.hrWorkovertimeSum>0){
            		  var li="<li><a href=javascript:addTab('sXyWorkOvertimeController.do?listHr','22','加班备案')>加班备案：等待您的批准 <font color='red' size='4' >("+data.hrWorkovertimeSum+")</font> 条</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li);  
                	  flag=true;
            	  }
            	  if(data.hrCheckApplySum>0){
            		  var li="<li><a href=javascript:addTab('sXyCheckApplyController.do?listHr','7','考勤异常备案')>考勤异常备案：等待您的批准 <font color='red' size='4' >("+data.hrCheckApplySum+")</font> 条</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li);  
                	  flag=true;
            	  } 
            	  
            	  
            	  if(data.selfAbsenceSum>0){
            		  var li="<li><a href=javascript:addTab('sXyAbsenceController.do?list','4','请假管理')>请假管理操作 <font color='red' size='4' >("+data.selfAbsenceSum+")</font> 条，请查看</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li);  
                	  flag=true;
            	  }
            	  if(data.selfBusinesstripSum>0){
            		  var li="<li><a href=javascript:addTab('sXyBusinessTripController.do?list','6','出差管理')>出差管理操作 <font color='red' size='4' >("+data.selfBusinesstripSum+")</font> 条，请查看</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li);  
                	  flag=true;
            	  }
            	  if(data.selfCompensateleaveSum>0){
            		  var li="<li><a href=javascript:addTab('sXyCompensateLeaveController.do?list','7','调休管理')>调休管理操作 <font color='red' size='4' >("+data.selfCompensateleaveSum+")</font> 条，请查看</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li); 
                	  flag=true;
            	  }
            	  if(data.selfOutworkSum>0){
            		  var li="<li><a href=javascript:addTab('sXyOutWorkController.do?list','9','外出管理')>外出管理操作 <font color='red' size='4' >("+data.selfOutworkSum+")</font> 条，请查看</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li);  
                	  flag=true;
            	  }
            	  if(data.selfWorkovertimeSum>0){
            		  var li="<li><a href=javascript:addTab('sXyWorkOvertimeController.do?list','5','加班管理')>加班管理操作 <font color='red' size='4' >("+data.selfWorkovertimeSum+")</font> 条，请查看</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li);  
                	  flag=true;
            	  }
            	  if(data.selfCheckApplySum>0){
            		  var li="<li><a href=javascript:addTab('sXyCheckApplyController.do?list','8','考勤异常管理')>考勤异常管理操作 <font color='red' size='4' >("+data.selfCheckApplySum+")</font> 条，请查看</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li);  
                	  flag=true;
            	  } 
            	  
            	  
            	  if(data.exceptionSum>0){
            		
            		  var li="<li><a >打卡记录：您有异常打卡 <font color='red' size='4' >("+data.exceptionSum+")</font> 条</a></li><br>";
                	  $("#iframe0").contents().find("#approve").append(li);  
                	  flag=true;
            	  }
     	       if(flag)
     	    	   {
  	    	  		$("#iframe0").contents().find("#approveid").show();
     	    	   }
          }
      },"json");
     //   	var s= $("#iframe0").contents().find("#approve").val(); 
        	
        
    	
    	//公告初始化
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
    
    function queryNoReadeNotice(){
    	var url = "noticeController.do?getNoticeList";
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
    					$("#noticeTitle").show();
    					$("#notice").show();
    				}else{
    					
    					$("#noticeTitle").hide();
    					$("#notice").hide();
    				}
    				$("#noticenum").text(noticeList.length);
					$("#noticenum2").text("您有"+noticeList.length+"未读公告");
    			}
    		}
    	});
    }
    
    
    
    
    function addTabb(dataUrl,dataIndex,menuName) {
        
         flag = true;
        if (dataUrl == undefined || $.trim(dataUrl).length == 0)return false;

        // 选项卡菜单已存在
        $('.J_menuTab').each(function () {
            if ($(this).data('id') == dataUrl) {
                if (!$(this).hasClass('active')) {
                    $(this).addClass('active').siblings('.J_menuTab').removeClass('active');           
                    // 显示tab对应的内容区
                    $('.J_mainContent .J_iframe').each(function () {
                        if ($(this).data('id') == dataUrl) {
                            $(this).show().siblings('.J_iframe').hide();
                            return false;
                        }
                    });
                }
                flag = false;
                return false;
            }
        });

        // 选项卡菜单不存在
        if (flag) {
            var str = '<a href="javascript:;" class="active J_menuTab" data-id="' + dataUrl + '">' + menuName + ' <i class="fa fa-times-circle"></i></a>';
            $('.J_menuTab').removeClass('active');

            // 添加选项卡对应的iframe
            var str1 = '<iframe class="J_iframe" name="iframe' + dataIndex + '" width="100%" height="100%" src="' + dataUrl + '" frameborder="0" data-id="' + dataUrl + '" seamless></iframe>';
            $('.J_mainContent').find('iframe.J_iframe').hide().parents('.J_mainContent').append(str1);

            //显示loading提示
//            var loading = layer.load();
    //
//            $('.J_mainContent iframe:visible').load(function () {
//                //iframe加载完成后隐藏loading提示
//                layer.close(loading);
//            });
            // 添加选项卡
            $('.J_menuTabs .page-tabs-content').append(str);
          //  scrollToTab($('.J_menuTab.active'));
        }
        return false;
    };
    
    
    
  
    	

</script>
</body>

</html>
