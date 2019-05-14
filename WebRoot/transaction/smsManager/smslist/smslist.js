var dialogIndex;
layui.use(['form', 'layer', 'table' ], function() {
	var treeSelect = layui.treeSelect;
	var table = layui.table;
	form = layui.form;
	$ = layui.jquery;
	layer = layui.layer;
	
	var blackTable = table.render({
		id: "sms_grid_list",
		elem : '#sms_grid_list',
		url : webpath + "/sms/smsList.action",
		method: "post",
		where: {smsPhone: $("#sms_phone").val()},
		cols : [ [
			{
				type : 'checkbox'
			},
			{
				field : 'CUST_NAME',
				title : '客户姓名'
			},
			{
				field : 'PHONE',
				title : '电话号码'
			},
			{
				field : 'SMS_CONTENT',
				title : '短信内容'
			},
			{
				field : 'SEND_TIME',
				title : '发送时间'
			},{
				field : 'CREACREATE_OPR_NAME',
				title : '创建人'
			},
			{
				field : 'CREATE_TIME',
				title : '创建时间'
			}
		] ],
		page : true,
		loading : true
	});
	
	//查询绑定
	$('#query_sms_btn').click(function() {
		table.reload("sms_grid_list", {where: {smsPhone: $("#sms_phone").val()}});
	});
	
});

//弹窗关闭
function closeDialog() {
	layer.close(dialogIndex);
}

