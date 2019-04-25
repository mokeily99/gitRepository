var dialogIndex;
var table;
var roleTable;

var eleTree;
var roleTree;
layui.config({
	base : webpath + "/common/ui/layui/lay/mymodules/"
}).use([ 'form', 'layer', 'table' , 'eleTree'], function() {
	table = layui.table;
	form = layui.form;
	$ = layui.jquery;
	layer = layui.layer;
	
	eleTree = layui.eleTree;

	roleTable = table.render({
		id : "role_grid_list",
		elem : '#role_grid_list',
		url : webpath + "/role/roleList.action",
		method : "post",
		cols : [ [
			{
				type : 'radio'
			},
			{
				field : 'ROLE_NAME',
				title : '角色名称'
			},
			{
				field : 'ROLE_DES',
				title : '描述'
			}
		] ],
		page : true,
		loading : true,
		parseData: function(res){ //res 即为原始返回的数据
		    res.data[0].LAY_CHECKED=true;
		    return {
		      "code": res.code, //解析接口状态
		      "msg": res.msg, //解析提示文本
		      "count": res.count, //解析数据长度
		      "data": res.data //解析数据列表
		    };
		},
		done: function(res, curr, count){
			var checkData = table.checkStatus("role_grid_list");

			roleTree = eleTree.render({
				elem : '.menu_elem',
				data : load_menu_data(checkData.data[0].MAXACCEPT),
				showCheckbox : true,
				defaultExpandAll : true,
				checkOnClickNode : true,
				expandOnClickNode : false
			});
		}
	});

	//监听行单击事件（单击事件为：rowDouble）
	table.on('row(role_grid_list)', function(obj) {
		var data = obj.data;
		roleTree = roleTree.reload({data: load_menu_data(data.MAXACCEPT)});
	});
	
	
	//角色类型下拉
	LayerSelect.initLayerSelect({
		dom : "add_role_type",
		url : webpath + "/code/getCommonCode.action?codeKey=ROLE_LEVEL",
		type : "post",
		dataType : "json",
		text : "CODE_NAME",
		id : "CODE_ID",
		defaultText: "请选择"
	});
	
	form.render();
	//角色添加
	$('#add_role_btn').click(function() {
		dialogIndex = layer.open({
			type : 1,
			title : '角色新增',
			content : $('#add_role_div'),
			area : [ '500px', '300px' ]
		});

		//角色添加提交
		form.on('submit(add_role_form_sub)', function(data) {
			
			$.ajax({
				url : webpath + "/role/addRole.action",
				method : 'post',
				data : data.field,
				dataType : "json",
				success : function(data1) {
					layer.close(dialogIndex);
					var resultCode = data1.resultCode;
					if (resultCode == "0000") {
						layer.msg('添加成功！');
						//加载表格
						roleTable = table.reload("role_grid_list");
						form.render();
					} else {
						layer.alert('添加失败，请重新操作！', {
							icon : 2
						});
					}
				}
			});

		});
	});
	
	
	//角色编辑
	$('#edit_role_btn').click(function() {
		var checkData = table.checkStatus("role_grid_list");
		
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
		form.val("edit_role_form", {
			  "edit_role_id": editeData.MAXACCEPT,
			  "edit_role_name": editeData.ROLE_NAME,
			  "edit_role_des": editeData.ROLE_DES
		});
		//修改角色类型下拉
		LayerSelect.initLayerSelect({
			dom : "edit_role_type",
			url : webpath + "/code/getCommonCode.action?codeKey=ROLE_LEVEL",
			type : "post",
			dataType : "json",
			text : "CODE_NAME",
			id : "CODE_ID",
			selectedID: checkData.data[0].ROLE_LEVEL
		});
		
		form.render();
		dialogIndex = layer.open({
			type : 1,
			title : '角色编辑',
			content : $('#edit_role_div'),
			area : [ '500px', '300px' ]
		});

		//角色添加提交
		form.on('submit(edit_role_form_sub)', function(data) {
			
			$.ajax({
				url : webpath + "/role/modRole.action",
				method : 'post',
				data : data.field,
				dataType : "json",
				success : function(data1) {
					layer.close(dialogIndex);
					var resultCode = data1.resultCode;
					if (resultCode == "0000") {
						layer.msg('添加成功！');
						//加载表格
						roleTable = table.reload("role_grid_list");
						form.render();
					} else {
						layer.alert('添加失败，请重新操作！', {
							icon : 2
						});
					}
				}
			});

		});
	});
	
	//删除绑定
	$('#del_role_btn').click(function() {
		var checkData = table.checkStatus("role_grid_list");
		if(checkData.data.length < 1){
			layer.msg('未选择任何数据！',{icon:0});
			return;
		}
		var editeData = checkData.data;
		var ids = "";
		for(var ix=0; ix<editeData.length; ix++){
			ids = editeData[ix].MAXACCEPT + "," + ids;
		}
		
		layer.msg('确定删除选择的数据？', {time: 0, btn: ['是', '否'],shade: [0.5, '#f5f5f5'],scrollbar: false, 
			yes: function(index){
				layer.close(index);
				
				$.ajax({
					url : webpath + "/role/delRoleMenus.action",
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
						roleTable = table.reload("role_grid_list");
					}
				});
			}
		}) ;
	});
});

//加载菜单数据
function load_menu_data(maxaccept) {
	var treeData = "";
	$.ajax({
		url : webpath + "/role/roleMenu.action?maxaccept=" + maxaccept,
		type : "post",
		async: false,
		dataType : "json",
		success : function(data) {
			if (data.length > 0) {
				treeData = data;
			}
		}
	});
	return treeData;
}

//角色菜单保存
function save_role_menu(){
	var checkData = table.checkStatus("role_grid_list");
	if(checkData.data.length < 1){
		layer.msg('未选择任何数据！',{icon:0});
		return;
	}
	var checkedTree = roleTree.getChecked();
	
	var ids = "";
	for(var ix=0; ix<checkedTree.length; ix++){
		ids = checkedTree[ix].id + "," + ids;
	}
	$.ajax({
		url : webpath + "/role/saveRoleMenus.action",
		type : "post",
		dataType : "json",
		data : {
			ids : ids,
			roleID: checkData.data[0].MAXACCEPT
		},
		success : function(data) {
			var resultCode = data.resultCode;
			
			if(resultCode == "0000"){
				layer.msg('保存成功！');
				roleTree = roleTree.reload({data: load_menu_data(checkData.data[0].MAXACCEPT)});
			}else{
				layer.msg('保存失败！', {icon: 5});
			}
			
		}
	});
}

//弹窗关闭
function closeDialog(){
	layer.close(dialogIndex);
}


$(document).on("click", ".layui-table-body table.layui-table tbody tr", function() {
	var index = $(this).attr('data-index');
	var tableBox = $(this).parents('.layui-table-box');
	//存在固定列
	if (tableBox.find(".layui-table-fixed.layui-table-fixed-l").length > 0) {
		tableDiv = tableBox.find(".layui-table-fixed.layui-table-fixed-l");
	} else {
		tableDiv = tableBox.find(".layui-table-body.layui-table-main");
	}
	//选中单击行
	var checkCell = tableDiv.find("tr[data-index=" + index + "]").find("td div.laytable-cell-radio div.layui-form-radio I");
	if (checkCell.length > 0) {
		checkCell.click();
	}
});
$(document).on("click", "td div.laytable-cell-radio div.layui-form-radio", function (e) {
    e.stopPropagation();
});