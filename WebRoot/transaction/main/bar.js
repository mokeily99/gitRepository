$(function(){
	//登录验证
	loginVal();
	//获取坐席闲忙
	getSeatFreeBusyStatus();
	
})

//登录验证
function loginVal(){
	$.ajax({
		url:webpath + "/seat/loginVal.action",
		type: "post",
		dataType: "json",
		success: function(data){
			var resultCode = data.resultCode;
			if(resultCode == "0000"){
				alert("API登录成功！");
			}
		}
	});
}

function getSeatFreeBusyStatus(){
	$.ajax({
		url:webpath + "/seat/getSeatFreeBusy.action",
		type: "post",
		dataType: "json",
		success: function(data){
			var resultCode = data.resultCode;
			if(resultCode == "0000"){
				var resultData = data.resultData;
				if(!isEmpty(resultData)){
					var status = resultData.STATUS;
					if(!isEmpty(status)){
						if(status == "1"){//闲
							$("#seat_free_busy_img").attr("src", webpath+"/common/img/free.png");
						}else{//忙
							$("#seat_free_busy_img").attr("src", webpath+"/common/img/busy.png");
						}
					}
				}
			}
		}
	});
}

//置忙
function setBusy(){
	$.ajax({
		url:webpath + "/seat/setBusyFree.action",
		type: "post",
		data: {status: "0"},
		dataType: "json",
		success: function(data){
			var resultCode = data.resultCode;
			if(resultCode == "0000"){
				$("#seat_free_busy_img").attr("src", webpath+"/common/img/busy.png");
			}
		}
	});
}

//置闲
function setFree(){
	$.ajax({
		url:webpath + "/seat/setBusyFree.action",
		type: "post",
		data: {status: "1"},
		dataType: "json",
		success: function(data){
			var resultCode = data.resultCode;
			if(resultCode == "0000"){
				$("#seat_free_busy_img").attr("src", webpath+"/common/img/free.png");
			}
		}
	});
}



function isEmpty(val){
	if(val == null || val == undefined || val == ""){
		return true;
	}else{
		return false;
	}
}