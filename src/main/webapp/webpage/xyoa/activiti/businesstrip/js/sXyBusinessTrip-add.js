/**
 * 提交表单前的非空 检验
 */
function bSubmit() {
	var startT = $.trim($("#tripStartTime").val());
	var endT = $.trim($("#tripEndTime").val());
	var remarks = $.trim($("#remarks").val());
	
	if (startT == '') {
		alertTip("请选择出差开始时间");
		return false;
	} else if (endT == '') {
		alertTip("请选择出差结束时间");
		return false;
	} else if (remarks == '') {
		alertTip("请输入出差原因");
		return false;
	}
	var sT = new Date(Date.parse(startT.replace(/-/g, "/")));
	var eT = new Date(Date.parse(endT.replace(/-/g, "/")));
	if (sT > eT) {
		alertTip("出差开始时间不能大于出差结束时间");
		return false;
	}
	return true;
}

/**
 * 改变时间 计算两时间的差值
 */
function timeChange() {
	var startT = $.trim($("#tripStartTime").val());
	var endT = $.trim($("#tripEndTime").val());
	if(startT != '' && endT != ''){
		 var sT = new Date(Date.parse(startT.replace(/-/g, "/")));
		  var eT = new Date(Date.parse(endT.replace(/-/g, "/")));
		  if(sT > eT) {
			  $("#applyTripHour").val(null);
		  } else {
			  getApplyTripHour("sXyAbsenceController.do?getTwoDayDiffer&startT="+startT+"&endT="+endT);
		  }
	}
}
function getApplyTripHour(url, data) {
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
				$("#applyTripHour").val(d.obj);
			}
		}
	});
}