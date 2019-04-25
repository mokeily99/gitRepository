var dialogIndex;
var menuTree;
layui.config({
	base : webpath + "/common/ui/layui/lay/mymodules/"
}).use([ 'jquery', 'table', 'eleTree', 'code' ], function() {
	var $ = layui.jquery;
	var eleTree = layui.eleTree;
	var form = layui.form;
	var layer=layui.layer;

	var treeData = loadMenuTree();

	menuTree = eleTree.render({
		elem : '#menu_tree',
		data : treeData,
		showCheckbox : true,
		defaultExpandAll : true,
		checkStrictly: true,
		checkOnClickNode: true,
		expandOnClickNode: false
	});
	
	//菜单添加事件
	$("#add_menu_btn").on("click",function() {
		var node = menuTree.getChecked();
		if(node.length > 1){
			layer.msg('只能选择一个父菜单！',{icon:0});
			return;
		}
		
		if(node.length == 1){
			//父部门编码赋值
			form.val("add_menu_form", {
				  "add_menu_pid": node[0].id
			});
		}else{
			//父部门编码赋值
			form.val("add_menu_form", {
				  "add_menu_pid": ""
			});
		}
		
		//菜单添加弹窗
		dialogIndex = layer.open({
		  type: 1,
		  title: "菜单添加",
		  skin: 'layui-layer-rim', //加上边框
		  area: ['450px', '250px'], //宽高
		  content: $("#add_menu_div").html(),
		  success: function(layero, index){
			  
		  }
		});
		
		//菜单添加提交
		form.on('submit(add_menu_form_sub)', function(data){
			  $.ajax({
				  url:webpath + "/menu/addMenu.action",       
				  method:'post',       
				  data:data.field,  
				  dataType: "json",
				  success:function(data1){
					  layer.close(dialogIndex);
					  var resultCode = data1.resultCode;
					  if(resultCode == "0000"){
						  layer.msg('添加成功！');
						  menuTree = menuTree.reload({data: loadMenuTree()});
					  }else{
						  layer.alert('添加失败，请重新操作！', {icon: 2});
					  }
				  }           
			  });         
              
        });
		
	});
	
	//菜单修改事件
	$("#edit_menu_btn").on("click",function() {
		var node = menuTree.getChecked();
		if(node.length < 1){
			layer.msg('未选择任何数据！',{icon:0});
			return;
		}
		if(node.length > 1){
			layer.msg('每次只能修改一个部门信息！',{icon:0});
			return;
		}
		
		//修改添加弹窗
		dialogIndex = layer.open({
		  type: 1,
		  title: "菜单修改",
		  skin: 'layui-layer-rim', //加上边框
		  area: ['450px', '250px'], //宽高
		  content: $("#edit_menu_div").html(),
		  success: function(layero, index){
			var menuInfo = loadMenuInfo(node[0].id);
			var menuUrl = (isEmpty(menuInfo))?"":menuInfo[0].menuUrl;
			//菜单编码赋值
			form.val("edit_menu_form", {
				  "edit_menu_id": node[0].id,
				  "edit_menu_name": node[0].label,
				  "edit_menu_adr": menuUrl
			});
		  }
		});
		
		//部门修改提交
		form.on('submit(edit_menu_form_sub)', function(data){
			  $.ajax({  
				  url:webpath + "/menu/modMenu.action",       
				  method:'post',       
				  data:data.field,  
				  dataType: "json",
				  success:function(data1){
					  layer.close(dialogIndex);
					  var resultCode = data1.resultCode;
					  if(resultCode == "0000"){
						  layer.msg('修改成功！');
						  menuTree = menuTree.reload({data: loadMenuTree()});
					  }else{
						  layer.alert('修改失败，请重新操作！', {icon: 2});
					  }
				  }           
			  });         
              
        });
	});
	
	//菜单删除事件
	$("#del_menu_btn").on("click",function() {
		var node = menuTree.getChecked();
		
		if(node.length < 1){
			layer.msg('未选择任何数据！',{icon:0});
			return;
		}
		
		layer.msg('注意！删除父部门的同时，子部门也会被删除！确定删除？', {time: 0, btn: ['是', '否'],shade: [0.5, '#f5f5f5'],scrollbar: false, 
			yes: function(index){
				layer.close(index);
				
				//删除用户信息
				delMenu(node);
			}
		}) ;
	});
});

//弹窗关闭
function closeDialog(){
	layer.close(dialogIndex);
}

//角色删除
function delMenu(node){
	var ids = "";
	for(var ix=0; ix<node.length; ix++){
		ids = node[ix].id + "," + ids;
	}
	
	$.ajax({
		url : webpath + "/menu/deleteMenu.action",
		type : "post",
		dataType : "json",
		data : {
			ids : ids
		},
		success : function(data) {
			var resultCode = data.resultCode;
			
			if(resultCode == "0000"){
				layer.msg('删除成功！');
				menuTree = menuTree.reload({data: loadMenuTree()});
			}else{
				layer.msg('删除失败！', {icon: 5});
			}
			
		}
	});
}

/**
 * 菜单列表加载
 */
function loadMenuTree(){
	var tree;
	$.ajax({
		url: webpath + "/menu/menuList.action",
		type: "post",
		async: false,
		dataType: "json",
		success: function(data){
			tree = data;
		}
	});
	return tree;
}
/**
 * 获取菜单信息
 */
function loadMenuInfo(maxaccept){
	var menuInfo;
	$.ajax({
		url: webpath + "/menu/showMenuInfo.action",
		type: "post",
		data: {menuID: maxaccept},
		async: false,
		dataType: "json",
		success: function(data){
			if(!isEmpty(data)){
				menuInfo = data;
			}
		}
	});
	return menuInfo;
}