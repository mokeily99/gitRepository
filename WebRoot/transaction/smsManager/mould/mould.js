var dialogIndex;
layui.use(['form', 'layer', 'table' ], function() {
	var treeSelect = layui.treeSelect;
	var table = layui.table;
	form = layui.form;
	$ = layui.jquery;
	layer = layui.layer;
	
	var mouldTable = table.render({
		id: "mould_grid_list",
		elem : '#mould_grid_list',
		url : webpath + "/sms/mouldList.action",
		method: "post",
		where: {mouldTitle: $("#mould_title").val()},
		cols : [ [
			{
				type : 'checkbox'
			},
			{
				field : 'MOULD_TITLE',
				title : '模板标题'
			},
			{
				field : 'MOULD_TYPE',
				title : '模板类型',
				templet: function(row){
					var type = row.MOULD_TYPE;
					var obj = getCodeName("MOULD_TYPE", type);
					return obj[0].CODE_NAME;
				}
			},
			{
				field : 'MOULD_CONTENT',
				title : '模板内容'
			},
			{
				field : 'CREATE_OPR_NAME',
				title : '修改人'
			},
			{
				field : 'CREATE_TIME',
				title : '修改时间'
			}
			
		] ],
		page : true,
		loading : true
	});
	
	//查询绑定
	$('#query_mould_btn').click(function() {
		table.reload("mould_grid_list", {where: {mouldTitle: $("#mould_title").val()}});
	});
	
	//模板添加
	$('#add_mould_btn').click(function() {
		dialogIndex = layer.open({
			type : 1,
			title : '模板新增',
			content : $('#add_mould_div'),
			area : [ '500px', '300px' ]
		});
		
		LayerSelect.initLayerSelect({
			dom : "add_mould_type",
			url : webpath + "/code/getCommonCode.action",
			type : "post",
			dataType : "json",
			queryParams: {codeKey: "MOULD_TYPE"},
			text : "CODE_NAME",
			id : "CODE_ID",
			defaultText: "请选择"
		});
		form.render();
		//模板添加提交
		form.on('submit(add_mould_form_sub)', function(data) {
			//判断是否可添加自动触发模板
			var mouldType = $("#add_mould_type").val();
			if(mouldType == "10401"){
				var tableList = layui.table.cache.mould_grid_list;
				for(var ix=0; ix<tableList.length; ix++){
					var obj = tableList[ix];
					if(obj.MOULD_TYPE == "10401"){
						layer.alert('已存在自动触发模板，不能再添加自动触发模板！', {
							icon : 2
						});
						return;
					}
				}
			}
			
			$.ajax({
				url : webpath + "/sms/addMould.action",
				method : 'post',
				data : data.field,
				dataType : "json",
				success : function(data1) {
					layer.close(dialogIndex);
					var resultCode = data1.resultCode;
					if (resultCode == "0000") {
						layer.msg('添加成功！');
						mouldTable = table.reload("mould_grid_list");
					}else if (resultCode == "0001") {
						layer.msg(data1.resultMsg);
						mouldTable = table.reload("mould_grid_list");
					} else {
						layer.alert('添加失败，请重新操作！', {
							icon : 2
						});
					}
				}
			});

		});
	});
	
	//模板修改
	$('#edit_mould_btn').click(function() {
		var checkData = table.checkStatus("mould_grid_list");
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
		form.val("edit_mould_form", {
			  "edit_mould_id": editeData.MAXACCEPT,
			  "edit_mould_title": editeData.MOULD_TITLE,
			  "edit_mould_content": editeData.MOULD_CONTENT
		});
		
		dialogIndex = layer.open({
			type : 1,
			title : '模板修改',
			content : $('#edit_mould_div'),
			area : [ '500px', '300px' ]
		});
		
		LayerSelect.initLayerSelect({
			dom : "edit_mould_type",
			url : webpath + "/code/getCommonCode.action",
			type : "post",
			dataType : "json",
			queryParams: {codeKey: "MOULD_TYPE"},
			text : "CODE_NAME",
			id : "CODE_ID",
			selectedID: editeData.MOULD_TYPE
		});
		form.render();

		//模板修改提交
		form.on('submit(edit_mould_form_sub)', function(editData) {
			$.ajax({
				url : webpath + "/sms/editMould.action",
				method : 'post',
				data : editData.field,
				dataType : "json",
				success : function(data1) {
					layer.close(dialogIndex);
					var resultCode = data1.resultCode;
					if (resultCode == "0000") {
						layer.msg('修改成功！');
						blackTable = table.reload("mould_grid_list");
					} else if (resultCode == "0001") {
						layer.msg(data1.resultMsg);
						mouldTable = table.reload("mould_grid_list");
					} else {
						layer.alert('修改失败，请重新操作！', {
							icon : 2
						});
					}
				}
			});

		});
	});
	
	//删除模板
	$('#del_mould_btn').click(function() {
		var checkData = table.checkStatus("mould_grid_list");
		if(checkData.data.length < 1){
			layer.msg('未选择任何数据！',{icon:0});
			return;
		}
		var delData = checkData.data;
		var ids = "";
		for(var ix=0; ix<delData.length; ix++){
			if(delData[ix].MOULD_TYPE == "10401"){
				layer.msg('自动触发模板不能删除！',{icon:0});
				return;
			}
			ids = delData[ix].MAXACCEPT + "," + ids;
		}
		
		layer.msg('确定删除选择的数据？', {time: 0, btn: ['是', '否'],shade: [0.5, '#f5f5f5'],scrollbar: false, 
			yes: function(index){
				layer.close(index);
				
				$.ajax({
					url : webpath + "/sms/delMould.action",
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
						mouldTable = table.reload("mould_grid_list");
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

