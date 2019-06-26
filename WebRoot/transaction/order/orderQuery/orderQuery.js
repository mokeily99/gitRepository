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
		url : webpath + "/order/orderListAll.action",
		method: "post",
		where: {connPhone: $("#conn_phone").val(), custName: $("#cust_name").val(), orderStatus: $("#order_status").val()},
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
				field : 'CREATE_TIME',
				title : '创建时间'
			},
			{
				field : 'CREATE_OPR_NAME',
				title : '派发人'
			},
			{
				field : 'SEND_OPR_NAME',
				title : '当前处理人'
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
				field : 'OVER_FLAG',
				title : '工单状态',
				templet: function(row){
					if(row.OVER_FLAG == "1"){
						return "已完结";
					}else{
						return "未完结";
					}
				}
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
		table.reload("order_grid_list", {where: {connPhone: $("#conn_phone").val(), custName: $("#cust_name").val(), orderStatus: $("#order_status").val()}});
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
					if(resultData[ix].OVER_FLAG == "1"){
						html.push("  <i class=\"layui-icon layui-timeline-axis\">&#xe673;</i>");
					}else{
						html.push("  <i class=\"layui-icon layui-timeline-axis\">&#xe63f;</i>");
					}
					html.push("  <div class=\"layui-timeline-content layui-text\">");
					html.push("  	<h3 class=\"layui-timeline-title\" style=\"font-size: 16px;\">" +resultData[ix].SEND_TIME+ "&nbsp;&nbsp;&nbsp;" +resultData[ix].CREATE_OPR_NAME+ "</h3>");
					html.push("  	<p style=\"color:cadetblue;\">");
					html.push("			" +(resultData[ix].SEND_MARK==undefined?'':resultData[ix].SEND_MARK)+ "");
					html.push("  	</p>");
					html.push("  </div>");
					html.push("</li>");
				}
				html.push("</ul>");
				$("#locus_order_div").html(html.join(""));
				
				dialogIndex = layer.open({
					type : 1,
					title : '工单轨迹',
					content : $('#locus_order_div'),
					area : [ '500px', '300px' ]
				});
			}
		});
	}
	
	//黑名单添加
	$('#add_black_btn').click(function() {
		dialogIndex = layer.open({
			type : 1,
			title : '黑名单新增',
			content : $('#add_black_div'),
			area : [ '500px', '300px' ]
		});

		//黑名单添加提交
		form.on('submit(add_black_form_sub)', function(data) {

			$.ajax({
				url : webpath + "/sms/addBlackList.action",
				method : 'post',
				data : data.field,
				dataType : "json",
				success : function(data1) {
					layer.close(dialogIndex);
					var resultCode = data1.resultCode;
					if (resultCode == "0000") {
						layer.msg('添加成功！');
						orderTable = table.reload("order_grid_list");
					} else {
						layer.alert('添加失败，请重新操作！', {
							icon : 2
						});
					}
				}
			});

		});
	});
	
	//黑名单修改
	$('#edit_black_btn').click(function() {
		var checkData = table.checkStatus("order_grid_list");
		if(checkData.data.length < 1){
			layer.msg('未选择任何数据！',{icon:0});
			return;
		}
		
		if(checkData.data.length > 1){
			layer.msg('每次只能修改一条信息！',{icon:0});
			return;
		}
		
		var editeData = checkData.data[0];
		//修改赋值 
		form.val("edit_black_form", {
			  "edit_black_id": editeData.MAXACCEPT,
			  "edit_black_phone": editeData.BLACK_PHONE,
			  "edit_black_reason": editeData.BLACK_REASON
		});
		
		dialogIndex = layer.open({
			type : 1,
			title : '黑名单修改',
			content : $('#edit_black_div'),
			area : [ '500px', '300px' ]
		});

		//黑名单修改提交
		form.on('submit(edit_black_form_sub)', function(editData) {
			$.ajax({
				url : webpath + "/sms/editBlackList.action",
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
	
	//删除绑定
	$('#del_black_btn').click(function() {
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
		
		layer.msg('确定删除选择的数据？', {time: 0, btn: ['是', '否'],shade: [0.5, '#f5f5f5'],scrollbar: false, 
			yes: function(index){
				layer.close(index);
				
				$.ajax({
					url : webpath + "/sms/delBlackList.action",
					type : "post",
					dataType : "json",
					data : {
						ids : ids
					},
					success : function(data) {
						var resultCode = data.resultCode;
						if(resultCode == "0000"){
							layer.msg("删除成功！");
						}else{
							layer.msg('删除失败！', {icon: 5});
						}
						orderTable = table.reload("order_grid_list");
					}
				});
			}
		}) ;
	});
});

//弹窗关闭
function closeDialog() {
	layer.close(dialogIndex);
}

