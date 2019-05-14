var dialogIndex;
layui.use(['form', 'layer', 'table' ], function() {
	var treeSelect = layui.treeSelect;
	var table = layui.table;
	form = layui.form;
	$ = layui.jquery;
	layer = layui.layer;
	
	//加载发送结果
	LayerSelect.initLayerSelect({
		dom : "send_flag",
		url : webpath + "/code/getCommonCode.action",
		type : "post",
		queryParams: {codeKey: "SEND_FLAG"},
		dataType : "json",
		text : "CODE_NAME",
		id : "CODE_ID",
		defaultText : "请选择"
	});
	form.render();
	
	var blackTable = table.render({
		id: "sms_grid_list",
		elem : '#sms_grid_list',
		url : webpath + "/sms/smsHis.action",
		method: "post",
		where: {smsPhone: $("#sms_phone").val(), sendFlag: $("#send_flag").val()},
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
				field : 'YY_SEND_TIME',
				title : '预约发送时间'
			},
			{
				field : 'SJ_SEND_TIME',
				title : '实际发送时间'
			},{
				field : 'CREACREATE_OPR_NAME',
				title : '创建人'
			},
			{
				field : 'CREATE_TIME',
				title : '创建时间'
			},
			{
				field : 'SEND_FLAG',
				title : '发送结果',
				templet: function(row){
					var sendFlag = row.SEND_FLAG;
					var obj = getCodeName("SEND_FLAG", sendFlag);
					return obj[0].CODE_NAME;
				}
			},
			{
				field : 'SEND_MARK',
				title : '描述'
			}
		] ],
		page : true,
		loading : true
	});
	
	//查询绑定
	$('#query_sms_btn').click(function() {
		table.reload("sms_grid_list", {where: {smsPhone: $("#sms_phone").val(), sendFlag: $("#send_flag").val()}});
	});
	
});

//弹窗关闭
function closeDialog() {
	layer.close(dialogIndex);
}

