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
			},
			{
				field : 'MXACCEPT',
				title : '操作',
				templet: function(row){
					return "<a style=\"color:blue\" href=\"javascript:showOrderLocus("+row.MAXACCEPT+")\">轨迹查询</a>";
				}
			}
		] ],
		page : true,
		loading : true
	});
	
	//查询绑定
	$('#query_order_btn').click(function() {
		table.reload("order_grid_list", {where: {order_phone: $("#order_phone").val(), cust_name: $("#cust_name").val()}});
	});
	
	//轨迹查询
	window.showOrderLocus=function(maxaccept){
		$.ajax({
			url: webpath + "/order/getOrderLocus.action",
			type: "post",
			dataType: "json", 
			data: {maxaccept: maxaccept},
			success: function(data){
				var resultCode = data.resultCode;
				var resultData = data.resultData;
				
				var html = [];
				html.push("<ul class=\"layui-timeline\">");
				for(var ix=0; ix<resultData.length; ix++){
					html.push("<li class=\"layui-timeline-item\">");
					html.push("  <i class=\"layui-icon layui-timeline-axis\">&#xe63f;</i>");
					html.push("  <div class=\"layui-timeline-content layui-text\">");
					html.push("  	<h3 class=\"layui-timeline-title\">8月18日</h3>");
					html.push("  	<p>");
					html.push("			layui 2.0 的一切准备工作似乎都已到位。发布之弦，一触即发。不枉近百个日日夜夜与之为伴。因小而大，因弱而强。无论它能走多远，抑或如何支撑？至少我曾倾注全心，无怨无悔");
					html.push("  	</p>");
					html.push("  </div>");
					html.push("</li>");
				}
				html.push("</ul>");
				$("#locus_order_div").html(html.join(""));
				debugger;
				dialogIndex = layer.open({
					type : 1,
					title : '工单轨迹',
					content : $('#locus_order_div'),
					area : [ '500px', '300px' ]
				});
			}
		});
	}
	
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

