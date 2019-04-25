$(function() {
	//加载组件
	initCom();

	//加载列表
	//loadPersonnelData();

});
var dialogIndex;
layui.use([ 'treeSelect', 'form', 'layer', 'table' ], function() {
	var treeSelect = layui.treeSelect;
	var table = layui.table;
	form = layui.form;
	$ = layui.jquery;
	layer = layui.layer;
	
	var personnelTable = table.render({
		id: "personnel_grid_list",
		elem : '#personnel_grid_list',
		url : webpath + "/personnel/personnelList.action",
		method: "post",
		cols : [ [
			{
				type : 'checkbox'
			},
			/*{ field: 'MAXACCEPT', title: '序列', hide: true},*/
			{
				field : 'USER_ACCOUNT',
				title : '账号'
			},
			{
				field : 'USER_NAME',
				title : '姓名'
			},
			{
				field : 'PHONE',
				title : '电话'
			},
			{
				field : 'ADDRESS',
				title : '地址'
			},
			{
				field : 'ROLE_NAME',
				title : '角色'
			},
			{
				field : 'DEPT_NAME',
				title : '部门',
			}
		] ],
		page : true,
		loading : true
	});
	
	//查询绑定
	$('#query_personnel_btn').click(function() {
		table.reload("personnel_grid_list", {where: {userName: $("#user_name").val(), roleCode: $("#role_level").val()}});
	});


	treeSelect.render({
		// 选择器
		elem : '#add_personnel_dept',
		// 数据
		data : webpath + '/dept/deptList.action',
		// 异步加载方式：get/post，默认get
		type : 'get',
		style : {
			folder : { // 父节点图标
				enable : false // 是否开启：true/false
			},
			line : { // 连接线
				enable : false // 是否开启：true/false
			}
		},
		// 点击回调
		click : function(d) {
			console.log(d);
		},
	});

	LayerSelect.initLayerSelect({
		dom : "add_personnel_role",
		url : webpath + "/role/comRoleList.action",
		type : "post",
		dataType : "json",
		text : "ROLE_NAME",
		id : "MAXACCEPT",
		defaultText : "请选择"
	});

	form.render();
	//人员添加
	$('#add_personnel_btn').click(function() {
		dialogIndex = layer.open({
			type : 1,
			title : '人员新增',
			content : $('#add_personnel_div'),
			area : [ '500px', '500px' ]
		});

		//部门添加提交
		form.on('submit(add_personnel_form_sub)', function(data) {

			$.ajax({
				url : webpath + "/personnel/addPersonnel.action",
				method : 'post',
				data : data.field,
				dataType : "json",
				success : function(data1) {
					layer.close(dialogIndex);
					var resultCode = data1.resultCode;
					if (resultCode == "0000") {
						layer.msg('添加成功！');
						personnelTable = table.reload("personnel_grid_list");
					} else {
						layer.alert('添加失败，请重新操作！', {
							icon : 2
						});
					}
				}
			});

		});
	});

	//加载修改人员树
	renderTree(treeSelect, "edit_personnel_dept", "");
	//人员修改
	$('#edit_personnel_btn').click(function() {
		var checkData = table.checkStatus("personnel_grid_list");
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
		form.val("edit_personnel_form", {
			  "edit_personnel_id": editeData.MAXACCEPT,
			  "edit_personnel_account": editeData.USER_ACCOUNT,
			  "edit_personnel_pwd": editeData.USER_PWD,
			  "edit_personnel_name": editeData.USER_NAME,
			  "edit_personnel_phone": editeData.PHONE,
			  "edit_personnel_address": editeData.ADDRESS,
		});
		
		//部门树默认选中
		treeSelect.checkNode("edit_personnel_dept", editeData.DEPT_CODE);
		
		
		
		dialogIndex = layer.open({
			type : 1,
			title : '人员修改',
			content : $('#edit_personnel_div'),
			area : [ '500px', '500px' ]
		});
		
		//角色下拉默认选中
		LayerSelect.initLayerSelect({
			dom : "edit_personnel_role",
			url : webpath + "/role/comRoleList.action",
			type : "post",
			dataType : "json",
			text : "ROLE_NAME",
			id : "MAXACCEPT",
			selectedID: editeData.USER_ROLE
		});
		
		form.render();

		//部门修改提交
		form.on('submit(edit_personnel_form_sub)', function(editData) {
			$.ajax({
				url : webpath + "/personnel/modifyPersonnel.action",
				method : 'post',
				data : editData.field,
				dataType : "json",
				success : function(data1) {
					layer.close(dialogIndex);
					var resultCode = data1.resultCode;
					if (resultCode == "0000") {
						layer.msg('修改成功！');
						table.reload("personnel_grid_list");
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
	$('#del_personnel_btn').click(function() {
		var checkData = table.checkStatus("personnel_grid_list");
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
					url : webpath + "/personnel/delPersonnel.action",
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
						table.reload("personnel_grid_list");
					}
				});
			}
		}) ;
	});
});

function initCom() {
	//查询区域部门下拉菜单
	LayerSelect.initLayerSelect({
		dom : "role_level",
		url : webpath + "/role/comRoleList.action",
		type : "post",
		dataType : "json",
		text : "ROLE_NAME",
		id : "MAXACCEPT",
		defaultText : "请选择"
	});
}
var editTree;
function renderTree(treeSelect, ele){
	editTree = treeSelect.render({
		// 选择器
		elem : '#'+ele,
		// 数据
		data : webpath + '/dept/deptList.action',
		// 异步加载方式：get/post，默认get
		type : 'get',
		style : {
			folder : { // 父节点图标
				enable : false // 是否开启：true/false
			},
			line : { // 连接线
				enable : false // 是否开启：true/false
			}
		},
		// 点击回调
		click : function(d) {
			console.log(d);
		}
	});
}

//弹窗关闭
function closeDialog() {
	layer.close(dialogIndex);
}