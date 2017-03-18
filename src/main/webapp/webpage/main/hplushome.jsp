<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>

<!DOCTYPE html>
<html>
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!--360浏览器优先以webkit内核解析-->


    <title>Jeecg 微云快速开发平台</title>

    <link rel="shortcut icon" href="images/favicon.ico">
    <link href="plug-in-ui/hplus/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="plug-in-ui/hplus/css/font-awesome.css?v=4.4.0" rel="stylesheet">

    <link href="plug-in-ui/hplus/css/animate.css" rel="stylesheet">
    <link href="plug-in-ui/hplus/css/style.css?v=4.1.0" rel="stylesheet">
<style>
table {
    *border-collapse: collapse; /* IE7 and lower */
    border-spacing: 0;
    width: 100%;  
	color:#3679B7;
	 
}
.zebra td, .zebra th {
    padding: 5px;
    border-bottom: 1px solid #f2f2f2;    
}
.zebra tbody tr:nth-child(even) {
    background: #EAEAEA;
    -webkit-box-shadow: 0 1px 0 rgba(255,255,255,.8) inset; 
    -moz-box-shadow:0 1px 0 rgba(255,255,255,.8) inset;  
    box-shadow: 0 1px 0 rgba(255,255,255,.8) inset;        
}
.zebra th {
    color:#89909B;
    text-align: center;
    text-shadow: 0 1px 0 rgba(255,255,255,.5); 
    border-bottom: 1px solid #ccc;
    background-color: #EAEAEA;
    background-image: -webkit-gradient(linear, left top, left bottom, from(#f5f5f5), to(#eee));
    background-image: -webkit-linear-gradient(top, #f5f5f5, #eee);
    background-image:    -moz-linear-gradient(top, #f5f5f5, #eee);
    background-image:     -ms-linear-gradient(top, #f5f5f5, #eee);
    background-image:      -o-linear-gradient(top, #f5f5f5, #eee); 
    background-image:         linear-gradient(top, #f5f5f5, #eee);
}
.zebra th:first-child {
    -moz-border-radius: 5px 0 0 0;
    -webkit-border-radius: 8px 0 0 0;
    border-radius: 5px 0 0 0;  
}
.zebra th:last-child {
    -moz-border-radius: 0 5px 0 0;
    -webkit-border-radius: 0 5px 0 0;
    border-radius: 0 5px 0 0;
}
.zebra th:only-child{
    -moz-border-radius: 5px 5px 0 0;
    -webkit-border-radius: 5px 8px 0 0;
    border-radius: 5px 5px 0 0;
}
.zebra tfoot td {
    border-bottom: 0;
    border-top: 1px solid #fff;
    background-color: #f1f1f1;  
}

.zebra tfoot td:first-child {
    -moz-border-radius: 0 0 0 6px;
    -webkit-border-radius: 0 0 0 6px;
    border-radius: 0 0 0 6px;
}

.zebra tfoot td:last-child {
    -moz-border-radius: 0 0 6px 0;
    -webkit-border-radius: 0 0 6px 0;
    border-radius: 0 0 6px 0;
}

.zebra tfoot td:only-child{
    -moz-border-radius: 0 0 6px 6px;
    -webkit-border-radius: 0 0 6px 6px
    border-radius: 0 0 6px 6px
}
  .zebra-content {
  background: #FCFEFF;
  padding: 7px;
   border-top:1px solid #E7EAEC;
  }
  
</style>

</head>


<body class="gray-bg">
   
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-sm-4">

            <div class="ibox float-e-margins" id="approveid" hidden="hidden">
            
                <div class="ibox-title">
                    <h5>待您审批 </h5>
                </div>
                <div class="ibox-content"  >
                   
                    <ol id="approve">
                                                    
                    </ol>
                </div>
            </div>
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>联系信息</h5>

                </div>
                <div class="ibox-content">
                    <p><i class="fa fa-send-o"></i> 官网：<a href="http://www.xyect.com/" target="_blank">http://www.xyect.com/</a>
                    </p>
                    <p><i class="fa fa-empire"></i> 地址：<a href="javascript:;">四川省成都市天仁路388号凯德天府10楼</a>
                    </p>
                    <p><i class="fa fa-hacker-news"></i>联系电话：<a href="javascript:;">400 604 0955</a>
                    </p>
                    <p><i class="fa fa-credit-card"></i> 邮箱：<a href="javascript:;" class="邮箱">xyhr@xyebank.com</a>
                    </p> 
                </div>
            </div>  
        </div>
        <div class="col-sm-4">
        
        
        
        <div id="trialend" class="ibox float-e-margins" hidden="hidden">
                <div class="ibox-title">
                    <h5>转正信息提醒</h5>

                </div>
                <div class="zebra-content">
		    
		<table class="zebra" id="staffendtrial">
		    <thead>
		    <tr>
		    	<th width="15">#</th><th width="80">姓名</th> <th>所在部门</th><th width="80">试用结束</th>		
		    </tr>
		    </thead>
		    <tfoot>
		    <tr>
		        <td>&nbsp;</td><td></td><td></td><td></td>
		    </tr>
		    </tfoot> 
			<!-- <tr  >
		    	 <td>&nbsp;</td><td></td><td></td>		
		    </tr> -->
		</table>
                </div>
            </div> 
        
            <!-- <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>转正提醒</h5>
                </div>
              
                
                <div class="ibox-content no-padding">
                    <div class="panel-body">
                        <div class="panel-group" id="version">
                             <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h5 class="panel-title">
                                        <a data-toggle="collapse" data-parent="#version" href="#v33">更多内容...</a><code class="pull-right"></code>
                                    </h5>
                                </div>
                                <div id="v33" class="panel-collapse collapse">
                                    <div class="panel-body">
                                        <ol>
                                            <li>更多版本，请详见论坛：www.jeecg.org</li>
                                        </ol>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div> -->
        </div> 
         <div class="col-sm-4">
            <div class="ibox float-e-margins" hidden="hidden">
                <div class="ibox-title">
                    <h5>适用范围</h5>
                                 </div>
               <!--  <div class="ibox-content">
                    <p>为什么选择jeecg？</p>
                    <ol>
                        <li>采用主流框架，容易上手，学习成本低；</li>
                        <li>强大代码生成器和在线配置能力，提高开发效率，大大缩短项目周期；</li>
                        <li>开源免费，万人活跃社区；</li>
                        <li>封装基础用户权限，报表等功能模块，无需再次开发；</li>
                        <li>采用SpringMVC + Hibernate + Minidao+ Easyui+Jquery+Boostrap等基础架构</li>
                        <li>支持多浏览器，多数据库；</li>
                        <li>支持移动开发，可以完美兼容电脑、手机、pad等多平台；</li>
                        <li>……</li>
                    </ol>
                    <hr>
                    <div class="alert alert-warning">JEECG智能开发平台，可以应用在任何J2EE项目的开发中，尤其适合企业信息管理系统（MIS）、内部办公系统（OA）、企业资源计划系统（ERP） 、客户关系管理系统（CRM）等，其半智能手工Merge的开发方式，可以显著提高开发效率60%以上，极大降低开发成本。
                    </div>
                </div> -->
            </div>
        </div> 
    </div>  
</div>

<!-- 全局js -->
<script src="plug-in-ui/hplus/js/jquery.min.js?v=2.1.4"></script>
<script src="plug-in-ui/hplus/js/bootstrap.min.js?v=3.3.6"></script>
<script src="plug-in-ui/hplus/js/plugins/layer/layer.min.js"></script>

<!-- 自定义js -->
<script type="text/javascript">

 function addTab(dataUrl,dataIndex,menuName) {   
    window.parent.addTabb(dataUrl,dataIndex,menuName);
    
};

 
</script>
</body>

</html>
