var dialogIndex;
layui.use(['form', 'layer', 'table' ], function() {
	var treeSelect = layui.treeSelect;
	var table = layui.table;
	form = layui.form;
	$ = layui.jquery;
	layer = layui.layer;
	
	var orderTable = table.render({
		id: "order_grid_list",
		elem : '#order_grid_list',
		url : webpath + "/order/getPendingOrder.action",
		method: "post",
		where: {order_phone: $("#order_phone").val(), cust_name: $("#cust_name").val()},
		cols : [ [
			{
				type : 'checkbox'
			},
			{
				field : 'CUST_NAME',
				title : '客户姓名'
			},
			{
				field : 'CONN_PHONE',
				title : '联系电话'
			},
			{
				field : 'CUST_ADR',
				title : '联系地址'
			},
			{
				field : 'MARK_CONTENT',
				title : '工单备注'
			},
			{
				field : 'CREATE_OPR_NAME',
				title : '创建人'
			},
			{
				field : 'CREATE_TIME',
				title : '创建时间'
			},
			{
				field : 'SEND_OPR_NAME',
				title : '派发至人员'
			},
			{
				field : 'CREATE_TIME',
				title : '派发时间'
			},
			{
				field : 'SEND_MARK',
				title : '派发备注'
			}
		] ],
		page : true,
		loading : true
	});
	
	//查询绑定
	$('#query_order_btn').click(function() {
		table.reload("order_grid_list", {where: {order_phone: $("#order_phone").val(), cust_name: $("#cust_name").val()}});
	});
	
	//转发
	$('#turn_order_btn').click(function() {
		var checkData = table.checkStatus("order_grid_list");
		if(checkData.data.length < 1){
			layer.msg('未选择任何数据！',{icon:0});
			return;
		}
		var delData = checkData.data;
		var ids = "";
		for(var ix=0; ix<delData.length; ix++){
			ids = delData[ix].MAXACCEPT + "," + ids;
		}
		
		$("#send_order_form")[0].reset();
		$("#send_order_ids").val(ids);
		//加载派发人员
		LayerSelect.initLayerSelect({
			dom : "order_send_opr",
			url : webpath + "/personnel/getSendPersonList.action",
			type : "post",
			dataType : "json",
			text : "USER_NAME",
			id : "MAXACCEPT"
		});
		
		dialogIndex = layer.open({
			type : 1,
			title : '工单转发',
			content : $('#send_order_div'),
			area : [ '500px', '300px' ]
		});

		//工单转发提交
		form.on('submit(send_order_form_sub)', function(editData) {
			$.ajax({
				url : webpath + "/order/sendOrder.action",
				method : 'post',
				data : editData.field,
				dataType : "json",
				success : function(data1) {
					layer.close(dialogIndex);
					var resultCode = data1.resultCode;
					if (resultCode == "0000") {
						layer.msg('修改成功！');
						orderTable = table.reload("order_grid_list");
					} else {
						layer.alert('修改失败，请重新操作！', {
							icon : 2
						});
					}
				}
			});

		});
	});
	
	//工单终结
	$('#over_order_btn').click(function() {
		var checkData = table.checkStatus("order_grid_list");
		if(checkData.data.length < 1){
			layer.msg('未选择任何数据！',{icon:0});
			return;
		}
		var delData = checkData.data;
		var ids = "";
		for(var ix=0; ix<delData.length; ix++){
			ids = delData[ix].MAXACCEPT + "," + ids;
		}
		
		$("#over_order_form")[0].reset();
		$("#over_order_ids").val(ids);
		
		dialogIndex = layer.open({
			type : 1,
			title : '工单终结',
			content : $('#over_order_div'),
			area : [ '500px', '240px' ]
		});

		//工单转发提交
		form.on('submit(over_order_form_sub)', function(editData) {
			$.ajax({
				url : webpath + "/order/overOrder.action",
				method : 'post',
				data : editData.field,
				dataType : "json",
				success : function(data1) {
					layer.close(dialogIndex);
					var resultCode = data1.resultCode;
					if (resultCode == "0000") {
						layer.msg('修改成功！');
						orderTable = table.reload("order_grid_list");
					} else {
						layer.alert('修改失败，请重新操作！', {
							icon : 2
						});
					}
				}
			});

		});
	});
});

//弹窗关闭
function closeDialog() {
	layer.close(dialogIndex);
}

