/**
 * 提交表单之前做的非空检查
 */
function bSubmit() {
	var checkType = $.trim($("#checkType").val());
	var remarks = $.trim($("#remarks").val());
	
	if (checkType == '') {
		alertTip("请选择考勤类型");
		return false;
	} else if (remarks == '') {
		alertTip("请输入考勤异常原因");
		return false;
	}
	var ids = [];
	var rows = $("#sXyCheckApplyListAdd").datagrid('getSelections');
	if (rows.length > 0) {
		for ( var i = 0; i < rows.length; i++) {
			 ids.push(rows[i].id);
		}
		$("#checkIds").val(ids.join(','));
		return true;
	} else {
		alertTip("请选择需要进行考勤异常申请的数据");
		return false;
	}
}