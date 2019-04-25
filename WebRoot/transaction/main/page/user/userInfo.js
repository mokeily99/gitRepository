layui.use(['form', 'layer', 'upload'], function() {
	form = layui.form;
	$ = layui.jquery;
	layer = layui.layer;
	var upload = layui.upload;
	
	upload.render({
		elem : '#head_upload_input',
		url : webpath + "/personnel/uploadUserFace.action",
		method : "post",
		field: "user_face",
		exts : 'jpg|png|gif',
		done : function(data) {
			var resultCode = data.resultCode;
			if (resultCode == "0000") {
				$("#userFace").attr("src", "/facePic/" + data.resultData);
			} else {
				$("#userFace").attr("src", webpath + "/transaction/main/images/face.png");
			}
		}
	});
	
	//判断是否修改过头像，如果修改过则显示修改后的头像，否则显示默认头像
    if(photoHead != "null" && photoHead != "" && photoHead != undefined){
    	$("#userFace").attr("src","/facePic/" + photoHead);
    }else{
    	$("#userFace").attr("src",webpath + "/transaction/main/images/face.png");
    }
    
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
	
    //修改提交
  	form.on('submit(change_user)', function(data){
		  $.ajax({  
			  url:webpath + "/personnel/changeUserInfo.action",       
			  method:'post',       
			  data:data.field,  
			  dataType: "json",
			  success:function(data1){
				  var resultCode = data1.resultCode;
				  if(resultCode == "0000"){
					  layer.msg('修改成功！');
				  }else{
					  layer.alert('修改失败，请重新操作！', {icon: 2});
				  }
			  }           
		  });         
        
  });
});