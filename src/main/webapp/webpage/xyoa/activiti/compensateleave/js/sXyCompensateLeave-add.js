/**
 * 提交表单之前做的非空检查
 */
function bSubmit() {
	var leaveType = $.trim($("#leaveType").val());
	var startT = $.trim($("#leaveStartTime").val());
	var endT = $.trim($("#leaveEndTime").val());
	var remarks = $.trim($("#remarks").val());
	var transferWork = $.trim($("#transferWork").val());
	
	if (leaveType == '') {
		alertTip("请选择调休类别");
		return false;
	} else if (startT == '') {
		alertTip("请选择调休开始时间");
		return false;
	} else if (endT == '') {
		alertTip("请选择调休结束时间");
		return false;
	} else if (remarks == '') {
		alertTip("请输入调休原因");
		return false;
	} else if (transferWork == '') {
		alertTip("请输入工作备份安排");
		return false;
	}
	var sT = new Date(Date.parse(startT.replace(/-/g, "/")));
	var eT = new Date(Date.parse(endT.replace(/-/g, "/")));
	if (sT > eT) {
		alertTip("调休开始时间不能大于调休结束时间");
		return false;
	}
	return true;
}
/**
 * 时间改变 异步获取时间差
 */
function timeChange() {
	var startT = $.trim($("#leaveStartTime").val());
	var endT = $.trim($("#leaveEndTime").val());
	if(startT != '' && endT != ''){
		  var sT = new Date(Date.parse(startT.replace(/-/g, "/")));
		  var eT = new Date(Date.parse(endT.replace(/-/g, "/")));
		  if(sT > eT) {
			  $("#applyLeaveHour").val(null);
		  } else {
			  getApplyLeaveHour("sXyAbsenceController.do?getTwoDayDiffer&startT="+startT+"&endT="+endT);
		  }
	}
}
function getApplyLeaveHour(url, data) {
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
				$("#applyLeaveHour").val(d.obj);
			}
		}
	});
}