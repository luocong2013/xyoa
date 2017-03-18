/**
 * 提交表单之前做的非空检查
 */
function bSubmit() {
	var startT = $.trim($("#outStartTime").val());
	var endT = $.trim($("#outEndTime").val());
	var remarks = $.trim($("#remarks").val());
	
	if (startT == '') {
		alertTip("请选择外出开始时间");
		return false;
	} else if (endT == '') {
		alertTip("请选择外出结束时间");
		return false;
	} else if (remarks == '') {
		alertTip("请输入外出原因");
		return false;
	}
	var sT = new Date(Date.parse(startT.replace(/-/g, "/")));
	var eT = new Date(Date.parse(endT.replace(/-/g, "/")));
	if (sT > eT) {
		alertTip("外出开始时间不能大于外出结束时间");
		return false;
	}
	return true;
}
/**
 * 时间改变 异步获取时间差
 */
function timeChange() {
	var startT = $.trim($("#outStartTime").val());
	var endT = $.trim($("#outEndTime").val());
	if(startT != '' && endT != ''){
		  var sT = new Date(Date.parse(startT.replace(/-/g, "/")));
		  var eT = new Date(Date.parse(endT.replace(/-/g, "/")));
		  if(sT > eT) {
			  $("#applyOutHour").val(null);
		  } else {
			  getApplyOutHour("sXyAbsenceController.do?getTwoDayDiffer&startT="+startT+"&endT="+endT);
		  }
	}
}
function getApplyOutHour(url, data) {
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
				$("#applyOutHour").val(d.obj);
			}
		}
	});
}