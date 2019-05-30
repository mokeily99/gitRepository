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
		url : webpath + "/order/getPageUnsendOrderList.action",
		method: "post",
		where: {order_phone: $("#order_phone").val(), cust_name: $("#cust_name").val(), send_flag: "0"},
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
			}
		] ],
		page : true,
		loading : true
	});
	
	//查询绑定
	$('#query_order_btn').click(function() {
		table.reload("order_grid_list", {where: {order_phone: $("#order_phone").val(), cust_name: $("#cust_name").val(), send_flag: "0"}});
	});
	
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
	
	//工单修改
	$('#edit_order_btn').click(function() {
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
		form.val("edit_order_form", {
			  "edit_order_id": editeData.MAXACCEPT,
			  "edit_cust_name": editeData.CUST_NAME,
			  "edit_conn_phone": editeData.CONN_PHONE,
			  "edit_conn_adr": editeData.CUST_ADR,
			  "edit_mark_content": editeData.MARK_CONTENT
		});
		
		dialogIndex = layer.open({
			type : 1,
			title : '工单编辑',
			content : $('#edit_order_div'),
			area : [ '500px', '400px' ]
		});

		//工单修改提交
		form.on('submit(edit_order_form_sub)', function(editData) {
			$.ajax({
				url : webpath + "/order/editOrderList.action",
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
	
	//工单派发
	$('#send_order_btn').click(function() {
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
		form.render();
		
		dialogIndex = layer.open({
			type : 1,
			title : '工单派发',
			content : $('#send_order_div'),
			area : [ '500px', '300px' ]
		});

		//工单派发提交
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
	
	//表单校验
  	form.verify({
  		connPhone : function(value, item) { //value：表单的值、item：表单的DOM对象
  			if(value != ""){
  				var tel1= /^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/;
  				var tel2= /^((0\d{2,3}\d{7,8})|(1[3584]\d{9}))$/;
  				var phone=/^1[34578]\d{9}$/;
  				if (!tel1.test(value) && !tel2.test(value) && !phone.test(value)) {
  					return '联系电话格式错误！';
  				}
  			}
  		}
  	});
});

//弹窗关闭
function closeDialog() {
	layer.close(dialogIndex);
}

