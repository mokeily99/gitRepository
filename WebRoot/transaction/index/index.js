$(function() {
	//加载菜单
	loadMenu();
	
	mod_pwd_form();
	
	//菜单点击
	J_iframe
    $(".J_menuItem").on('click',function(){
        var url = $(this).attr('href');
        $("#J_iframe").attr('src',url);
        return false;
    });
    
});

function loadMenu() {
	$.ajax({
		url : webpath+"/menu/loadMenu.action",
		type : "post",
		dataType : "json",
		async: false,
		success : function(data) {
			var pMenu = data[0].pMenu;
			var sMenu = data[0].sMenu;
			
			var menuHtml = [];
			for(var ix=0; ix<pMenu.length; ix++){
				menuHtml.push("<li>");
				menuHtml.push("    <a href=\"#\">");
				menuHtml.push("        <i class=\"fa fa-desktop\"></i>");
				menuHtml.push("        <span class=\"nav-label\">" +pMenu[ix].menuName+ "</span>");
				menuHtml.push("        <span class=\"fa arrow\"></span>");
				menuHtml.push("    </a>");
				menuHtml.push("    <ul class=\"nav nav-second-level\">");
				
				for(var iy=0; iy<sMenu.length; iy++){
					if(sMenu[iy].pid == pMenu[ix].maxaccept){
						menuHtml.push("        <li>");
						menuHtml.push("           <a class=\"J_menuItem\" href=\"" + webpath + sMenu[iy].menuUrl+ "\">" +sMenu[iy].menuName+ "</a>");
						menuHtml.push("        </li>");
					}
				}
				menuHtml.push("    </ul>");
				menuHtml.push("</li>");
				menuHtml.push("<li class=\"line dk\"></li>");
			}
			$("#side-menu").append(menuHtml.join(""));
			
		}
	});
}


//退出系统
function cancel(){
	$.ajax({
		url : webpath + "/login/cancel.action",
		type : "post",
		dataType : "json",
		success : function(data) {
			parent.window.location.assign(webpath+"/login.jsp");
		}
	});
}

//修改密码弹窗
function edit(){
	$('#edit_pwd_modal').modal("show");
}

//修改密码
function editPwd(){
	//定义一个校验器
	var bootstrapValidator = $("#edit_pwd_form").data('bootstrapValidator');
	
	var oldPw = $("#old_pwd").val();
	var newPw = $("#new_pwd").val();
	var newPwOk = $("#new_pwd_ok").val();
	//执行校验
	bootstrapValidator.validate();
	if(bootstrapValidator.isValid()){
		/*$("#edit_pwd_form").form("submit", {
			success : function(data) {
				data = eval("(" + data + ")");
				var resultCode = data.resultCode;
				if(resultCode == "0000"){
					layer.msg("修改成功！");
					//关闭弹窗
					$('#edit_pwd_modal').modal('hide');
					window.location.reload();
				}else if(resultCode == "8888"){
					layer.msg('原始密码有误！', {icon: 5});
				}else if(resultCode == "7777"){
					layer.msg('新密码与确认密码不一致！', {icon: 5});
				}else{
					layer.msg('修改失败！', {icon: 5});
				}
				
				//刷新表格
				loadMenu();
			}
		});*/
		
		$.ajax({
			url : webpath + "/login/editPWD.action",
			type : "post",
			dataType : "json",
			data : {
				oldPw : oldPw,
				newPw : newPw,
				newPwOk : newPwOk
			},
			success : function(data) {
				var resultCode = data.resultCode;
				if(resultCode == "0000"){
					layer.msg("修改成功！");
					//关闭弹窗
					$('#edit_pwd_modal').modal('hide');
					//window.location.reload();
					$("#old_pwd").val("");
					$("#new_pwd").val("");
					$("#new_pwd_ok").val("");
				}else if(resultCode == "8888"){
					layer.msg('原始密码有误！', {icon: 5});
				}else if(resultCode == "7777"){
					layer.msg('新密码与确认密码不一致！', {icon: 5});
				}else{
					layer.msg('修改失败！', {icon: 5});
				}
			}
		});
	}
}

function mod_pwd_form(){
	$("#edit_pwd_form").bootstrapValidator({
		 live: 'disabled',//验证时机，enabled是内容有变化就验证（默认），disabled和submitted是提交再验证
         feedbackIcons: {//根据验证结果显示的各种图标
             valid: 'glyphicon glyphicon-ok',
             invalid: 'glyphicon glyphicon-remove',
             validating: 'glyphicon glyphicon-refresh'
         },
         fields:{
        	 old_pwd:{
        		 message:'原始密码不正确',
                 validators: {
                     notEmpty: {
                         message: '密码不能为空'
                     },
                 }
        	 },
        	 new_pwd:{
                 validators: {
                     notEmpty: {
                         message: '新密码不能为空'
                     },
                     identical: {
                         field: 'new_pwd', //需要进行比较的input name值
                         message: '两次密码不一致'
                     }
                 }
        	 },
        	 new_pwd_ok:{
                 validators: {
                     notEmpty: {
                         message: '新密码不能为空'
                     },
                     identical: {
                         field: 'new_pwd_ok', //需要进行比较的input name值
                         message: '两次密码不一致'
                     }
                 }
        	 }
         }
	});
}