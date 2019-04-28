var dialogIndex;
layui.use(['form', 'layer', 'table' ], function() {
	var treeSelect = layui.treeSelect;
	var table = layui.table;
	form = layui.form;
	$ = layui.jquery;
	layer = layui.layer;
	
	var blackTable = table.render({
		id: "black_grid_list",
		elem : '#black_grid_list',
		url : webpath + "/sms/blackList.action",
		method: "post",
		where: {blackPhone: $("#black_phone").val()},
		cols : [ [
			{
				type : 'checkbox'
			},
			{
				field : 'BLACK_PHONE',
				title : '手机号码'
			},
			{
				field : 'BLACK_REASON',
				title : '短信内容'
			},
			{
				field : 'CREATE_OPR_NAME',
				title : '修改人'
			},
			{
				field : 'UPDATE_TIME',
				title : '修改时间'
			},
			{
				field : 'ABLE_FLAG',
				title : '状态',
				templet: function(row){
					var flag = row.ABLE_FLAG;
					var status = "";
					if(flag == "10101"){
						status = "<input type=\"checkbox\" value=\""+row.MAXACCEPT+"\" checked=\"\" name=\"open\" lay-skin=\"switch\" lay-filter=\"able_switch\" lay-text=\"生效|失效\">";
					}else{
						status = "<input type=\"checkbox\" value=\""+row.MAXACCEPT+"\" name=\"close\" lay-skin=\"switch\" lay-filter=\"able_switch\" lay-text=\"生效|失效\">";
					}
			        return status;
				}
			}
		] ],
		page : true,
		loading : true
	});
	
	//查询绑定
	$('#query_black_btn').click(function() {
		table.reload("black_grid_list", {where: {blackPhone: $("#black_phone").val()}});
	});
	
	//监听指定开关
	form.on('switch(able_switch)', function(data){
		var maxaccept = data.value;
		var ableFlag = this.checked;
		if(ableFlag){
			ableFlag = "10101";
		}else{
			ableFlag = "10102";
		}
		$.ajax({
			url: webpath + "/sms/changeBlackStatus.action",
			type: "post",
			data: {maxaccept: maxaccept, ableFlag: ableFlag},
			dataType: "json",
			success: function(data){
				var resultCode = data.resultCode;
				if(resultCode == "0000"){
					layer.msg('修改成功！');
				}else{
					layer.alert('修改失败，请重新操作！', {
						icon : 2
					});
				}
				blackTable = table.reload("black_grid_list");
			}
		});
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
						blackTable = table.reload("black_grid_list");
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
		var checkData = table.checkStatus("black_grid_list");
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
						blackTable = table.reload("black_grid_list");
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
		var checkData = table.checkStatus("black_grid_list");
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
						blackTable = table.reload("black_grid_list");
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

