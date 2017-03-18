/**
 * 提交表单之前做的非空检查
 */
function bSubmit() {
	var absenceType = $.trim($("#absenceType").val());
	var startT = $.trim($("#startTime").val());
	var endT = $.trim($("#endTime").val());
	var workT = $.trim($("#workTime").val());
	var remarks = $.trim($("#remarks").val());
	var transferWork = $.trim($("#transferWork").val());
	
	if(absenceType == ''){
		alertTip("请选择休假类型");
		return false;
	} else if(startT == ''){
		alertTip("请选择请假开始时间");
		return false;
	} else if (endT == ''){
		alertTip("请选择请假结束时间");
		return false;
	} else if (workT == ''){
		alertTip("请选择请假结束工作时间");
		return false;
	} else if (remarks == ''){
		alertTip("请输入请假原因");
		return false;
	} else if (transferWork == ''){
		alertTip("请输入工作备份安排");
		return false;
	}
	var sT = new Date(Date.parse(startT.replace(/-/g, "/")));
	var eT = new Date(Date.parse(endT.replace(/-/g, "/")));
	var wT = new Date(Date.parse(workT.replace(/-/g, "/")));
	if(sT > eT){
		 alertTip("请假开始时间不能大于请假结束时间");
		 return false;
	}
	if(eT > wT){
		alertTip("请假结束时间不能大于请假结束工作时间");
		return false;
	}
	return true;
}
/**
 * 时间改变 异步获取时间差
 */
function timeChange() {
	  var startT = $.trim($("#startTime").val());
	  var endT = $.trim($("#endTime").val());
	  if(startT != '' && endT != ''){
		  var sT = new Date(Date.parse(startT.replace(/-/g, "/")));
		  var eT = new Date(Date.parse(endT.replace(/-/g, "/")));
		  if(sT > eT) {
			  $("#applyAbsenceDay").val(null);
		  } else {
			  getApplyAbsenceDay("sXyAbsenceController.do?getTwoDayDiffer&startT="+startT+"&endT="+endT);
		  }
	  }
}
function getApplyAbsenceDay(url, data) {
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
				$("#applyAbsenceDay").val(d.obj);
			}
		}
	});
}