var dialogIndex;
var deptTree;
layui.config({
	base : webpath + "/common/ui/layui/lay/mymodules/"
}).use([ 'jquery', 'table', 'eleTree', 'code' ], function() {
	var $ = layui.jquery;
	var eleTree = layui.eleTree;
	var form = layui.form;
	var layer=layui.layer;

	var treeData = loadDeptTree();

	deptTree = eleTree.render({
		elem : '.dept_elem',
		data : treeData,
		showCheckbox : true,
		defaultExpandAll : true,
		checkStrictly: true,
		checkOnClickNode: true,
		expandOnClickNode: false
	});
	
	//部门添加事件
	$("#add_dept_btn").on("click",function() {
		var node = deptTree.getChecked();
		if(node.length < 1){
			layer.msg('请选择父部门后添加！',{icon:0});
			return;
		}
		if(node.length > 1){
			layer.msg('只能选择一个父部门！',{icon:0});
			return;
		}
		
		//部门添加弹窗
		dialogIndex = layer.open({
		  type: 1,
		  title: "部门添加",
		  skin: 'layui-layer-rim', //加上边框
		  area: ['450px', '250px'], //宽高
		  content: $("#add_dept_div").html(),
		  success: function(layero, index){
			  
		  }
		});
		
		//父部门编码赋值
		form.val("add_dept_form", {
			  "add_dept_pid": node[0].id
		});
		
		//部门添加提交
		form.on('submit(add_dept_form_sub)', function(data){
			  $.ajax({  
				  url:webpath + "/dept/addDept.action",       
				  method:'post',       
				  data:data.field,  
				  dataType: "json",
				  success:function(data1){
					  layer.close(dialogIndex);
					  var resultCode = data1.resultCode;
					  if(resultCode == "0000"){
						  layer.msg('添加成功！');
						  deptTree = deptTree.reload({data: loadDeptTree()});
					  }else{
						  layer.alert('添加失败，请重新操作！', {icon: 2});
					  }
				  }           
			  });         
              
        });
		
	});
	
	//部门修改事件
	$("#edit_dept_btn").on("click",function() {
		var node = deptTree.getChecked();
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
		  title: "部门修改",
		  skin: 'layui-layer-rim', //加上边框
		  area: ['450px', '250px'], //宽高
		  content: $("#edit_dept_div").html(),
		  success: function(layero, index){
			  
		  }
		});
		
		//父部门编码赋值
		form.val("edit_dept_form", {
			  "edit_dept_id": node[0].id,
			  "edit_dept_name": node[0].label
		});
		
		//部门修改提交
		form.on('submit(edit_dept_form_sub)', function(data){
			  $.ajax({  
				  url:webpath + "/dept/modifyDept.action",       
				  method:'post',       
				  data:data.field,  
				  dataType: "json",
				  success:function(data1){
					  layer.close(dialogIndex);
					  var resultCode = data1.resultCode;
					  if(resultCode == "0000"){
						  layer.msg('修改成功！');
						  deptTree = deptTree.reload({data: loadDeptTree()});
					  }else{
						  layer.alert('修改失败，请重新操作！', {icon: 2});
					  }
				  }           
			  });         
              
        });
	});
	
	//部门删除事件
	$("#del_dept_btn").on("click",function() {
		var node = deptTree.getChecked();
		
		if(node.length < 1){
			layer.msg('未选择任何数据！',{icon:0});
			return;
		}
		
		layer.msg('注意！删除父部门的同时，子部门也会被删除！确定删除？', {time: 0, btn: ['是', '否'],shade: [0.5, '#f5f5f5'],scrollbar: false, 
			yes: function(index){
				layer.close(index);
				
				//删除用户信息
				delDept(node);
			}
		}) ;
	});
});

//弹窗关闭
function closeDialog(){
	layer.close(dialogIndex);
}

//部门删除
function delDept(node){
	var ids = "";
	for(var ix=0; ix<node.length; ix++){
		ids = node[ix].id + "," + ids;
	}
	
	$.ajax({
		url : webpath + "/dept/delDept.action",
		type : "post",
		dataType : "json",
		data : {
			ids : ids
		},
		success : function(data) {
			var resultCode = data.resultCode;
			
			if(resultCode == "0000"){
				layer.msg('删除成功！');
				deptTree = deptTree.reload({data: loadDeptTree()});
			}else{
				layer.msg('删除失败！', {icon: 5});
			}
			
		}
	});
}

/**
 * 部门列表加载
 */
function loadDeptTree(){
	var tree;
	$.ajax({
		url: webpath + "/dept/deptList.action",
		type: "post",
		async: false,
		dataType: "json",
		success: function(data){
			tree = data;
		}
	});
	return tree;
}