$(function(){
	loadPage();
	
	//自定义表格高度
	//auto_table_H();
	//人员新增表单验证
	//add_validator_form();
	//人员修改表单验证
	//mod_validator_form();
	
});

var H = "";
function auto_table_H(){
	var windowH = $(window).height();
	var topH = $(".page-header").height();
	H = windowH - topH - 200;
}

function loadPage() {
	LayerGrid.initLayerGrid({
		dom: "ticket_table_list",
		url: webpath + "/ticket/ticketList.action",
		type: "post",
		dataType: "json",
		queryParams: {},
		page: true,
		pageSize: 10, 
		columns: [
			{ field: 'MAXACCEPT', title: '序列', visible: false},
			{ field: 'CODE_NAME', title: '票类名称'},
			{ field: 'CODE_ID', title: '票类编码'}
		]
	});
	
}

//增加票类
function add_ticket_before(){
	alert("123456");
	//触发事件
	/*var active = {
	    offset: function(othis){
	    var type = othis.data('type'),text = othis.text();
	  
	    layer.open({
	        type: 1
	        ,offset: type //具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
	        ,id: 'layerDemo'+type //防止重复弹出
	        ,content: '<div style="padding: 20px 100px;">'+ text +'</div>'
	        ,btn: '关闭全部'
	        ,btnAlign: 'c' //按钮居中
	        ,shade: 0 //不显示遮罩
	        ,yes: function(){
	            layer.closeAll();
	        }
	      });
	    }
	};
	  
	$('#layerDemo .layui-btn').on('click', function(){
	$('#add_ticket').on('click', function(){
	    var othis = $(this), method = othis.data('method');
	    active[method] ? active[method].call(this, othis) : '';
	});*/
}

//查询票类
function loadTicketData(){
	alert("查询票类");
}



/*function reload_table_data(){
	$("#dept_table_list").treegrid("reload");
}


//添加部门前
function add_ticket_before(){
	var checkData = $("#dept_table_list").datagrid("getChecked");
	if (checkData.length != 1) {
		layer.msg('请选择一个父部门后再添加！', {icon: 5});
		return;
	}
	
	$('#add_dept_modal').modal();
	
	//赋值
	$("#add_parent_code").val(checkData[0].MAXACCEPT);
	//添加弹窗组件初始化
    init_component();
}

function init_component(){
	//部门类型下拉菜单
	init_dept_type_combox();
}

function init_dept_type_combox(){
	$.ajax({
		url: webpath + "/code/DEPT_TYPE.action",
        dataType: 'json',
        type: "post",
        async: false,
        success: function (data) {
        	if(data != null){
        		if(data.length > 0){
        			_initSelect("add_dept_type", data, "CODE_NAME", "CODE_ID", "", 569);
        		}
        	}
        } 
	});
	
}

//添加部门
function add_dept(){
	//定义一个校验器
	var bootstrapValidator = $("#add_dept_form").data('bootstrapValidator');
	//执行校验
	bootstrapValidator.validate();
	
	if(bootstrapValidator.isValid()){
		$("#add_dept_form").form("submit", {
			success : function(data) {
				data = eval("(" + data + ")");
				var resultCode = data.resultCode;
				if(resultCode == "0000"){
					layer.msg("添加成功！");
				}else{
					layer.msg('添加失败！', {icon: 5});
				}
				
				//关闭弹窗
				$('#add_dept_modal').modal('hide');
				
				//刷新表格
				reload_table_data();
			}
		});
	}
}

function add_validator_form(){
	$("#add_dept_form").bootstrapValidator({
		 live: 'disabled',//验证时机，enabled是内容有变化就验证（默认），disabled和submitted是提交再验证
         feedbackIcons: {//根据验证结果显示的各种图标
             valid: 'glyphicon glyphicon-ok',
             invalid: 'glyphicon glyphicon-remove',
             validating: 'glyphicon glyphicon-refresh'
         },
        fields: {
        	add_dept_name: {
                message: '部门名称验证失败',
                validators: {
                    notEmpty: {
                        message: '部门名称不能为空'
                    }
                }
            }
        }
    });
}

function mod_validator_form(){
	$("#mod_dept_form").bootstrapValidator({
		 live: 'disabled',//验证时机，enabled是内容有变化就验证（默认），disabled和submitted是提交再验证
         feedbackIcons: {//根据验证结果显示的各种图标
             valid: 'glyphicon glyphicon-ok',
             invalid: 'glyphicon glyphicon-remove',
             validating: 'glyphicon glyphicon-refresh'
         },
        fields: {
        	mod_dept_name: {
                message: '部门名称验证失败',
                validators: {
                    notEmpty: {
                        message: '部门名称不能为空'
                    }
                }
            }
        }
    });
}

function mod_ticket_before() {
	var checkData = $("#dept_table_list").datagrid("getChecked");
	if (checkData.length < 1) {
		layer.msg('请选择要修改的部门！', {icon: 5});
		return;
	}
	
	$('#mod_dept_modal').modal();
	
	//赋值
	$("#mod_maxaccept").val(checkData[0].MAXACCEPT);
	$("#mod_dept_name").val(checkData[0].DEPT_NAME);
	$("#mod_dept_des").val(checkData[0].DEPT_DES);
	
	
	//添加弹窗组件初始化
    init_mode_component(checkData[0].DEPT_TYPE);
}

function init_mode_component(deptType){
	init_mod_role_combox(deptType);
}

function init_mod_role_combox(deptType){
	$.ajax({
		url: webpath + "/code/DEPT_TYPE.action",
        dataType: 'json',
        type: "post",
        async: false,
        success: function (data) {
        	if(data != null){
        		if(data.length > 0){
        			_initSelect("mod_dept_type", data, "CODE_NAME", "CODE_ID", deptType, 569);
        		}
        	}
        } 
	});
}

//修改部门
function mod_personnel_sub(){
	//定义一个校验器
	var bootstrapValidator = $("#mod_dept_form").data('bootstrapValidator');
	//执行校验
	bootstrapValidator.validate();
	
	if(bootstrapValidator.isValid()){
		$("#mod_dept_form").form("submit", {
			success : function(data) {
				data = eval("(" + data + ")");
				var resultCode = data.resultCode;
				if(resultCode == "0000"){
					layer.msg("修改成功！");
				}else{
					layer.msg('修改失败！', {icon: 5});
				}
				
				//关闭弹窗
				$('#mod_dept_modal').modal('hide');
				
				//刷新表格
				reload_table_data();
			}
		});
	}
}

//删除确认
function del_ticket_confim(){
	var checkData = $("#dept_table_list").datagrid("getChecked");
	if(checkData.length < 1){
		layer.msg('请至少选择一条要删除的记录！', {icon: 5});
		return;
	}
	
	layer.msg('确定删除？', {time: 0, btn: ['是', '否'],shade: [0.5, '#f5f5f5'],scrollbar: false, 
		yes: function(index){
			layer.close(index);
			
			//删除用户信息
			del_dept(checkData);
		}
	}) ;
}

//删除用户信息
function del_dept(checkData){
	
	var ids = "";
	for ( var ix = 0; ix < checkData.length; ix++) {
		ids = checkData[ix].MAXACCEPT + "," + ids;
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
				reload_table_data();
			}else{
				layer.msg('删除失败！', {icon: 5});
			}
			
		}
	});
}*/