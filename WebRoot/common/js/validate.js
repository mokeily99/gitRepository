function validate(dom, type){
	
	//手机号码
	if(type == "mobile"){
        var reg=/^[1][3,4,5,7,8,9][0-9]{9}$/;
        if(!reg.test($("#"+dom).val())){
        	$("#"+dom).css("border", "solid red 1px");
        	return true;
        }else{
        	$("#"+dom).css("border", "");
        	return false;
        }
	}
	//身份证号
	if(type == "IDCard"){
        var reg=/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        if(!reg.test($("#"+dom).val())){
        	$("#"+dom).css("border", "solid red 1px");
        	return true;
        }else{
        	$("#"+dom).css("border", "");
        	return false;
        }
	}
	//金额，一位小数
	if(type == "fee"){
        var reg=/^[0-9]+([.]{1}[0-9]+){0,1}$/;
        if(!reg.test($("#"+dom).val())){
        	$("#"+dom).css("border", "solid red 1px");
        	return true;
        }else{
        	$("#"+dom).css("border", "");
        	return false;
        }
	}
}