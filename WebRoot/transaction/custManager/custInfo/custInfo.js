var dialogIndex;
layui.use(['form', 'layer', 'table', 'upload'], function() {
	var treeSelect = layui.treeSelect;
	var table = layui.table;
	form = layui.form;
	$ = layui.jquery;
	layer = layui.layer;
	var upload = layui.upload;
	
	//超级管理员不显示客户类型
	if(userLevel == "10202"){
		$("#cust_type_div").show();
		//加载客户类型
		LayerSelect.initLayerSelect({
			dom : "cust_type",
			url : webpath + "/code/getCustType.action",
			type : "post",
			queryParams: {codeKey: "CUST_TYPE"},
			dataType : "json",
			text : "CODE_NAME",
			id : "CODE_ID",
			defaultText : "请选择"
		});
		form.render();
	}
	
	var custTable = table.render({
		id: "cust_grid_list",
		elem : '#cust_grid_list',
		url : webpath + "/cust/custList.action",
		method: "post",
		where: {custName: $("#cust_name").val(), custType: $("#cust_type").val()},
		cols : [ [
			{
				type : 'checkbox'
			},
			{
				field : 'CUST_NAME',
				title : '客户姓名'
			},
			{
				field : 'SEX',
				title : '性别',
				templet: function(row){
					var sex = row.SEX;
					var obj = getCodeName("SEX", sex);
					return obj[0].CODE_NAME;
				}
			},
			{
				field : 'CUST_AGE',
				title : '年龄'
			},
			{
				field : 'CONN_PHONE',
				title : '联系电话'
			},
			{
				field : 'CUST_ADDR',
				title : '地址'
			},
			{
				field : 'CUST_TYPE',
				title : '客户类型',
				templet: function(row){
					var custType = row.CUST_TYPE;
					var value = "";
					if(!isEmpty(custType)){
						var obj = getCustTypeCodeName("CUST_TYPE", custType);
						value = obj[0].CODE_NAME;
					}
					return value;
				}
			},
			{
				field : 'MARK',
				title : '备注信息'
			},
			{
				field : 'USER_NAME',
				title : '操作人'
			},
			{
				field : 'UPDATE_TIME',
				title : '操作时间'
			}
		] ],
		page : true,
		loading : true
	});
	
	//查询绑定
	$('#query_cust_btn').click(function() {
		table.reload("cust_grid_list", {where: {custName: $("#cust_name").val(), custType: $("#cust_type").val()}});
	});
	
	//性别下拉默认选中
	LayerSelect.initLayerSelect({
		dom : "add_cust_sex",
		url : webpath + "/code/getCommonCode.action",
		type : "post",
		dataType : "json",
		queryParams: {codeKey: "SEX"},
		text : "CODE_NAME",
		id : "CODE_ID",
		defaultText: "请选择"
	});
	LayerSelect.initLayerSelect({
		dom : "add_cust_type",
		url : webpath + "/code/getCustType.action",
		type : "post",
		dataType : "json",
		queryParams: {codeKey: "CUST_TYPE"},
		text : "CODE_NAME",
		id : "CODE_ID",
		defaultText: "请选择"
	});
	form.render();
	
	//单个客户添加
	$('#add_cust_btn').click(function() {
		dialogIndex = layer.open({
			type : 1,
			title : '单个客户录入',
			content : $('#add_cust_div'),
			area : [ '500px', '550px' ]
		});

		//客户添加提交
		form.on('submit(add_cust_form_sub)', function(data) {

			$.ajax({
				url : webpath + "/cust/addCustList.action",
				method : 'post',
				data : data.field,
				dataType : "json",
				success : function(data1) {
					layer.close(dialogIndex);
					var resultCode = data1.resultCode;
					if (resultCode == "0000") {
						layer.msg('添加成功！');
						custTable = table.reload("cust_grid_list");
					} else {
						layer.alert('添加失败，请重新操作！', {
							icon : 2
						});
					}
				}
			});

		});
	});
	
	//批量客户添加
	$('#bat_cust_btn').click(function() {
		dialogIndex = layer.open({
			type : 1,
			title : '批量客户录入',
			content : $('#bat_cust_div'),
			area : [ '500px', '420px' ]
		});
		
		//拖拽上传空间
		upload.render({
			elem : '#test10',
			url : webpath + "/cust/uploadUserFace.action",
			auto: false, 
			field: "custList",
			bindAction: '#bat_cust_form_sub',
			accept: "file",
			exts: "xlsx|xls",
			done : function(res) {
				layer.close(dialogIndex);
				var resultCode = res.resultCode;
				if (resultCode == "0000") {
					layer.msg('添加成功！');
					custTable = table.reload("cust_grid_list");
				} else {
					layer.alert('添加失败，请重新操作！', {
						icon : 2
					});
				}
			}
		});
	});
	
	//客户修改
	$('#edit_cust_btn').click(function() {
		var checkData = table.checkStatus("cust_grid_list");
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
		form.val("edit_cust_form", {
			  "edit_cust_id": editeData.CUST_ID,
			  "edit_cust_name": editeData.CUST_NAME,
			  "edit_cust_age": editeData.CUST_AGE,
			  "edit_conn_phone": editeData.CONN_PHONE,
			  "edit_cust_addr": editeData.CUST_ADDR,
			  "edit_cust_mark": editeData.MARK
		});
		//性别赋值
		LayerSelect.initLayerSelect({
			dom : "edit_cust_sex",
			url : webpath + "/code/getCommonCode.action",
			type : "post",
			dataType : "json",
			queryParams: {codeKey: "SEX"},
			text : "CODE_NAME",
			id : "CODE_ID",
			selectedID: editeData.SEX
		});
		//客户类型赋值
		LayerSelect.initLayerSelect({
			dom : "edit_cust_type",
			url : webpath + "/code/getCustType.action",
			type : "post",
			dataType : "json",
			queryParams: {codeKey: "CUST_TYPE"},
			text : "CODE_NAME",
			id : "CODE_ID",
			defaultText: "请选择",
			selectedID: editeData.CUST_TYPE
		});
		form.render();
		
		dialogIndex = layer.open({
			type : 1,
			title : '客户资料修改',
			content : $('#edit_cust_div'),
			area : [ '500px', '560px' ]
		});

		//客户资料修改提交
		form.on('submit(edit_cust_form_sub)', function(editData) {
			$.ajax({
				url : webpath + "/cust/editCustInfo.action",
				method : 'post',
				data : editData.field,
				dataType : "json",
				success : function(data1) {
					layer.close(dialogIndex);
					var resultCode = data1.resultCode;
					if (resultCode == "0000") {
						layer.msg('修改成功！');
						custTable = table.reload("cust_grid_list");
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
	$('#del_cust_btn').click(function() {
		var checkData = table.checkStatus("cust_grid_list");
		if(checkData.data.length < 1){
			layer.msg('未选择任何数据！',{icon:0});
			return;
		}
		var delData = checkData.data;
		var ids = "";
		for(var ix=0; ix<delData.length; ix++){
			ids = delData[ix].CUST_ID + "," + ids;
		}
		layer.msg('确定删除选择的数据？', {time: 0, btn: ['是', '否'],shade: [0.5, '#f5f5f5'],scrollbar: false, 
			yes: function(index){
				layer.close(index);
				
				$.ajax({
					url : webpath + "/cust/delCustInfo.action",
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
						custTable = table.reload("cust_grid_list");
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

