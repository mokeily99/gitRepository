var dialogIndex;
var ticketTree;
layui.config({
	base : webpath + "/common/ui/layui/lay/mymodules/"
}).use([ 'jquery', 'table', 'eleTree', 'code' ], function() {
	var $ = layui.jquery;
	var eleTree = layui.eleTree;
	var form = layui.form;
	var layer=layui.layer;

	var treeData = loadticketTree();

	ticketTree = eleTree.render({
		elem : '.ticket_elem',
		data : treeData,
		showCheckbox : true,
		defaultExpandAll : true,
		checkStrictly: true,
		checkOnClickNode: true,
		expandOnClickNode: false
	});
	
	//添加事件
	$("#add_ticket_btn").on("click",function() {
		var node = ticketTree.getChecked();
		//添加弹窗
		dialogIndex = layer.open({
		  type: 1,
		  title: "添加",
		  skin: 'layui-layer-rim', //加上边框
		  area: ['450px', '250px'], //宽高
		  content: $("#add_ticket_div").html(),
		  success: function(layero, index){
		  }
		});
		//编码赋值
		form.val("add_ticket_form", {
			  "add_ticket_pid": ""
		});
		//添加提交
		form.on('submit(add_ticket_form_sub)', function(data){
			  $.ajax({  
				  url:webpath + "/ticket/addTicket.action",       
				  method:'post',       
				  data:data.field,  
				  dataType: "json",
				  success:function(data1){
					  layer.close(dialogIndex);
					  var resultCode = data1.resultCode;
					  if(resultCode == "0000"){
						  layer.msg('添加成功！');
						  ticketTree = ticketTree.reload({data: loadticketTree()});
					  }else{
						  layer.alert('添加失败，请重新操作！', {icon: 2});
					  }
				  }           
			  });         
        });
	});
	//修改事件
	$("#edit_ticket_btn").on("click",function() {
		var node = ticketTree.getChecked();
		if(node.length < 1){
			layer.msg('未选择任何数据！',{icon:0});
			return;
		}
		if(node.length > 1){
			layer.msg('每次只能修改一个信息！',{icon:0});
			return;
		}
		//修改添加弹窗
		dialogIndex = layer.open({
		  type: 1,
		  title: "修改",
		  skin: 'layui-layer-rim', //加上边框
		  area: ['450px', '250px'], //宽高
		  content: $("#edit_ticket_div").html(),
		  success: function(layero, index){
		  }
		});
		//编码赋值
		form.val("edit_ticket_form", {
			  "edit_ticket_id": node[0].id,
			  "edit_ticket_name": node[0].label
		});
		
		//修改提交
		form.on('submit(edit_ticket_form_sub)', function(data){
			  $.ajax({  
				  url:webpath + "/ticket/modifyTicket.action",       
				  method:'post',       
				  data:data.field,  
				  dataType: "json",
				  success:function(data1){
					  layer.close(dialogIndex);
					  var resultCode = data1.resultCode;
					  if(resultCode == "0000"){
						  layer.msg('修改成功！');
						  ticketTree = ticketTree.reload({data: loadticketTree()});
					  }else{
						  layer.alert('修改失败，请重新操作！', {icon: 2});
					  }
				  }           
			  });         
        });
	});
	//查询事件
	$("#query_ticket_btn").on("click",function() {
		form.on('submit(query_ticket_btn_sub)', function(data){
			ticketTree = ticketTree.reload({data: loadticketTree()});	
		 });
	});
	
	//删除事件
	$("#del_ticket_btn").on("click",function() {
		var node = ticketTree.getChecked();
		
		if(node.length < 1){
			layer.msg('未选择任何数据！',{icon:0});
			return;
		}
		layer.msg('确定删除？', {time: 0, btn: ['是', '否'],shade: [0.5, '#f5f5f5'],scrollbar: false, 
			yes: function(index){
				layer.close(index);
				//删除用户信息
				delticket(node);
			}
		}) ;
	});
});

//弹窗关闭
function closeDialog(){
	layer.close(dialogIndex);
}

//删除
function delticket(node){
	var ids = "";
	for(var ix=0; ix<node.length; ix++){
		ids = node[ix].id + "," + ids;
	}
	
	$.ajax({
		url : webpath + "/ticket/delTicket.action",
		type : "post",
		dataType : "json",
		data : {
			ids : ids
		},
		success : function(data) {
			var resultCode = data.resultCode;
			
			if(resultCode == "0000"){
				layer.msg('删除成功！');
				ticketTree = ticketTree.reload({data: loadticketTree()});
			}else{
				layer.msg('删除失败！', {icon: 5});
			}
			
		}
	});
}

/**
 * 列表加载
 */
function loadticketTree(){
	var tree;
	var inp = document.getElementById("query_ticket_name");
	var ticket_name =  inp.value ;
	//去掉两边空格
	var t = trim(ticket_name);
//	layer.msg("ticket_name---"+t+"----"+ticket_name);
	if(null == t  || '' == t){
		layer.msg("1");
		//初始化加载
		$.ajax({
			url: webpath + "/ticket/tickeTypetList.action",
			type: "post",
			async: false,
			dataType: "json",
			success: function(data){
				tree = data;
			}
		});
	}else{
		layer.msg("2");
		//查询条件加载
		$.ajax({
			  url:webpath + "/ticket/queryTicket.action",       
			  method:'post',       
			  data:{
				  "ticket_name" : ticket_name
			  },  
			  dataType: "json",
			success: function(data){
				//	layer.msg("1111_________"+data);
					tree = data;	
			}
		});
	}
	return tree;
}
function trim(str){ //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
/**
 * 查询加载
 * @param data1
 * @returns
 */
/*function loadticketTree1(){
	var tree;
	var input = document.getElementById("query_ticket_name");
	var ticket_name =  input.value ;
	$.ajax({
		  url:webpath + "/ticket/queryTicket.action",       
		  method:'post',       
		  data:{
			  "ticket_name" : ticket_name
		  },  
		  dataType: "json",
		success: function(data){
			if(null != data  || '' != data){
			//	layer.msg("1111_________"+data);
				
				tree = data;	
			}
		}
	});

	return tree;
}*/