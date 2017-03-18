/**
 * 选择机构
 */
function openDepartmentSelect(flag) {
	$.dialog.setting.zIndex = getzIndex();
	var url;
	if(flag) {
		url = 'departController.do?tsDeptSelect&flag=' + flag;
	} else {
		url = 'departController.do?tsDeptSelect';
	}
	$.dialog({
			content: 'url:'+url, 
			zIndex: getzIndex(), 
			title: '组织机构列表', 
			lock: true, 
			width: '400px', 
			height: '350px',
			opacity: 0.4,
			button: [{
			        	 name: '确定', 
			        	 callback: callbackDepartmentSelect, 
			        	 focus: true
			          },{
			        	 name: '取消', 
			        	 callback: function (){}
			          }]
			}).zindex();
}

function callbackDepartmentSelect() {
	var iframe = this.iframe.contentWindow; 
	var treeObj = iframe.$.fn.zTree.getZTreeObj("departSelect"); 
	var nodes = treeObj.getCheckedNodes(true); 
	if(nodes.length>0){
		var id = nodes[0].id;
		var name = nodes[0].name;
		$('#departname').val(name); 
		$('#departname').blur(); 
		$('#orgId').val(id);
	}
}