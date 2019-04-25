var dialogIndex;
layui.use(['form', 'layer', 'table' ], function() {
	var treeSelect = layui.treeSelect;
	var table = layui.table;
	form = layui.form;
	$ = layui.jquery;
	layer = layui.layer;
	
	var senwordsTable = table.render({
		id: "senwords_grid_list",
		elem : '#senwords_grid_list',
		url : webpath + "/senwords/senwordsList.action",
		method: "post",
		where: {senwords: $("#sen_words").val()},
		cols : [ [
			{
				type : 'checkbox'
			},
			{
				field : 'SENSITIVE_WORDS',
				title : '敏感词'
			},
			{
				field : 'CREATE_TIME',
				title : '创建时间'
			},
			{
				field : 'CREATE_OPR_NAME',
				title : '创建人'
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
	$('#query_senwords_btn').click(function() {
		table.reload("senwords_grid_list", {where: {senwords: $("#sen_words").val()}});
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
			url: webpath + "/senwords/changeSenwordsStatus.action",
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
				senwordsTable = table.reload("senwords_grid_list");
			}
		});
	});
	
	//敏感词添加
	$('#add_senwords_btn').click(function() {
		dialogIndex = layer.open({
			type : 1,
			title : '敏感词新增',
			content : $('#add_senwords_div'),
			area : [ '500px', '200px' ]
		});

		//部门添加提交
		form.on('submit(add_senwords_form_sub)', function(data) {

			$.ajax({
				url : webpath + "/senwords/addSenwords.action",
				method : 'post',
				data : data.field,
				dataType : "json",
				success : function(data1) {
					layer.close(dialogIndex);
					var resultCode = data1.resultCode;
					if (resultCode == "0000") {
						layer.msg('添加成功！');
						senwordsTable = table.reload("senwords_grid_list");
					} else {
						layer.alert('添加失败，请重新操作！', {
							icon : 2
						});
					}
				}
			});

		});
	});
	
	//敏感词修改
	$('#edit_senwords_btn').click(function() {
		var checkData = table.checkStatus("senwords_grid_list");
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
		form.val("edit_senwords_form", {
			  "edit_senwords_id": editeData.MAXACCEPT,
			  "edit_senwords": editeData.SENSITIVE_WORDS
		});
		
		dialogIndex = layer.open({
			type : 1,
			title : '敏感词修改',
			content : $('#edit_senwords_div'),
			area : [ '500px', '200px' ]
		});

		//敏感词修改提交
		form.on('submit(edit_senwords_form_sub)', function(editData) {
			$.ajax({
				url : webpath + "/senwords/editSenwords.action",
				method : 'post',
				data : editData.field,
				dataType : "json",
				success : function(data1) {
					layer.close(dialogIndex);
					var resultCode = data1.resultCode;
					if (resultCode == "0000") {
						layer.msg('修改成功！');
						senwordsTable = table.reload("senwords_grid_list");
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
	$('#del_senwords_btn').click(function() {
		var checkData = table.checkStatus("senwords_grid_list");
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
					url : webpath + "/senwords/delSenwords.action",
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
						senwordsTable = table.reload("senwords_grid_list");
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

