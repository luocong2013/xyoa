/**
 * 提交表单之前做的非空检查
 */
function bSubmit() {
	var workType = $.trim($("#workType").val());
	var startT = $.trim($("#applyStartTime").val());
	var endT = $.trim($("#applyEndTime").val());
	var remarks = $.trim($("#remarks").val());
	
	if (workType == '') {
		alertTip("请选择加班类型");
		return false;
	} else if (startT == '') {
		alertTip("请选择加班开始时间");
		return false;
	} else if (endT == '') {
		alertTip("请选择加班结束时间");
		return false;
	} else if (remarks == '') {
		alertTip("请输入加班原因");
		return false;
	}
	var sT = new Date(Date.parse(startT.replace(/-/g, "/")));
	var eT = new Date(Date.parse(endT.replace(/-/g, "/")));
	if (sT > eT) {
		alertTip("加班开始时间不能大于加班结束时间");
		return false;
	}
	return true;
}
/**
 * 时间改变 异步获取时间差
 */
function timeChange() {
	var startT = $.trim($("#applyStartTime").val());
	var endT = $.trim($("#applyEndTime").val());
	if(startT != '' && endT != ''){
		  var sT = new Date(Date.parse(startT.replace(/-/g, "/")));
		  var eT = new Date(Date.parse(endT.replace(/-/g, "/")));
		  if(sT > eT) {
			  $("#applyWorkHour").val(null);
		  } else {
			  getApplyWorkHour("sXyWorkOvertimeController.do?getTwoDayDiffer&startT="+startT+"&endT="+endT);
		  }
	}
}
function getApplyWorkHour(url, data) {
	var paramsData = data;
	if(!paramsData){
		paramsData = new Object();
		if (url.indexOf("&") != -1) {
			var str = url.substr(url.indexOf("&")+1);
			url = url.substr(0,url.indexOf("&"));
			var strs = str.split("&");
			for(var i = 0; i < strs.length; i ++) {
				paramsData[strs[i].split("=")[0]]=(strs[i].split("=")[1]);
			}
		}      
	}
	$.ajax({
		async : true,
		cache : false,
		type : 'POST',
		data : paramsData,
		url : url,
		success : function(data) {
			var d = $.parseJSON(data);
			if (d.success) {
				$("#applyWorkHour").val(d.obj);
			}
		}
	});
}
  
/**
 * 改变加班类型所做的变化
 */
function change() {
	  var d = new Date();
	  var sTime = new Date(d.getFullYear(), d.getMonth(), d.getDate(), 20, 0, 0);
	  
	  var val = $("#workType").val();
	  if(val == '01' || val == '02') {
		  $("#applyStartTime").attr("readonly", false).attr("disabled", false);
		  $("#applyEndTime").attr("readonly", false).attr("disabled", false);
		  $("#remarks").attr("disabled", false);
		  $("#applyStartTime").val(null);
		  $("#applyEndTime").val(null);
		  $("#applyWorkHour").val(null);
	  } else if (val == '03') {
		  $("#applyStartTime").attr("readonly", true).attr("disabled", false);
		  $("#applyEndTime").attr("readonly", true).attr("disabled", false);
		  $("#remarks").attr("disabled", false);
		  var eTime = new Date(d.getFullYear(), d.getMonth(), d.getDate()+1, 0, 0, 0);
		  $("#applyStartTime").val(sTime.format("yyyy-MM-dd hh:mm:ss"));
		  $("#applyEndTime").val(eTime.format("yyyy-MM-dd hh:mm:ss"));
		  $("#applyWorkHour").val(4);
	  } else if (val == '04'){
		  $("#applyStartTime").attr("readonly", true).attr("disabled", false);
		  $("#applyEndTime").attr("readonly", true).attr("disabled", false);
		  $("#remarks").attr("disabled", false);
		  var eTime = new Date(d.getFullYear(), d.getMonth(), d.getDate()+1, 6, 0, 0);
		  $("#applyStartTime").val(sTime.format("yyyy-MM-dd hh:mm:ss"));
		  $("#applyEndTime").val(eTime.format("yyyy-MM-dd hh:mm:ss"));
		  $("#applyWorkHour").val(8);
	  } else {
		  $("#applyStartTime").attr("readonly", false).attr("disabled", true);
		  $("#applyEndTime").attr("readonly", false).attr("disabled", true);
		  $("#remarks").attr("disabled", true);
		  $("#applyStartTime").val(null);
		  $("#applyEndTime").val(null);
		  $("#applyWorkHour").val(null);
		  $("#remarks").val(null);
	  }
}
  
Date.prototype.format = function(format) {
	  var o = {
			  "M+" : this.getMonth() + 1,
			  "d+" : this.getDate(),
			  "h+" : this.getHours(),
			  "m+" : this.getMinutes(),
			  "s+" : this.getSeconds(),
	  }
	  if (/(y+)/.test(format)) {
		  format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length))
	  }
	  for (var k in o) {  
	        if (new RegExp("(" + k + ")").test(format)) {  
	            format = format.replace(RegExp.$1, RegExp.$1.length == 1  
	                            ? o[k]  
	                            : ("00" + o[k]).substr(("" + o[k]).length));  
	        }  
	    }  
	  return format;
}