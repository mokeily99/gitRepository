var $,tab,skyconsWeather,dialogIndex;
layui.config({
	base : "js/"
}).use(['bodyTab','form','element','layer','jquery'],function(){
	var form = layui.form,
		layer = layui.layer,
		element = layui.element;
		$ = layui.jquery;
		tab = layui.bodyTab();

	//锁屏
	function lockPage(){
		layer.open({
			title : false,
			type : 1,
			content : $("#lock-box"),
			closeBtn : 0,
			shade : 0.9
		})
	}
	$(".lockcms").on("click",function(){
		window.sessionStorage.setItem("lockcms",true);
		lockPage();
	})
	// 判断是否显示锁屏
	if(window.sessionStorage.getItem("lockcms") == "true"){
		lockPage();
	}
	// 解锁
	$("#unlock").on("click",function(){
		if($(this).siblings(".admin-header-lock-input").val() == ''){
			layer.msg("请输入解锁密码！");
		}else{
			if($(this).siblings(".admin-header-lock-input").val() == "123456"){
				window.sessionStorage.setItem("lockcms",false);
				$(this).siblings(".admin-header-lock-input").val('');
				layer.closeAll("page");
			}else{
				layer.msg("密码错误，请重新输入！");
			}
		}
	});
	$(document).on('keydown', function() {
		if(event.keyCode == 13) {
			$("#unlock").click();
		}
	});

	//手机设备的简单适配
	var treeMobile = $('.site-tree-mobile'),
		shadeMobile = $('.site-mobile-shade')

	treeMobile.on('click', function(){
		$('body').addClass('site-mobile');
	});

	shadeMobile.on('click', function(){
		$('body').removeClass('site-mobile');
	});

	// 添加新窗口
	$(".layui-nav .layui-nav-item a").on("click",function(){
		addTab($(this));
		$(this).parent("li").siblings().removeClass("layui-nav-itemed");
	})

	//公告层
	function showNotice(){
		layer.open({
	        type: 1,
	        title: "系统公告", //不显示标题栏
	        closeBtn: false,
	        area: '310px',
	        shade: 0.8,
	        id: 'LAY_layuipro', //设定一个id，防止重复弹出
	        btn: ['关闭'],
	        moveType: 1, //拖拽模式，0或者1
	        content: '<div style="padding:15px 20px; text-align:justify; line-height: 22px; text-indent:2em;border-bottom:1px solid #e2e2e2;"><p>系统短信提醒<br/>系统短信提醒<br/>系统短信提醒<br/>系统短信提醒<br/>系统短信提醒<br/>系统短信提醒<br/>系统短信提醒<br/>系统短信提醒<br/>系统短信提醒<br/>系统短信提醒<br/>系统短信提醒<br/>系统短信提醒<br/>系统短信提醒<br/>系统短信提醒<br/>系统短信提醒<br/>系统短信提醒系统短信提醒系统短信提醒系统短信提醒<br/>系统短信提醒系统短信提醒<br/>系统短信提醒系统短信提醒系统短信提醒系统短信提醒</p></div>',
	        success: function(layero){
				var btn = layero.find('.layui-layer-btn');
				btn.css('text-align', 'center');
				btn.on("click",function(){
					window.sessionStorage.setItem("showNotice","true");
				})
				if($(window).width() > 432){  //如果页面宽度不足以显示顶部“系统公告”按钮，则不提示
					btn.on("click",function(){
						layer.tips('系统公告躲在了这里', '#showNotice', {
							tips: 3
						});
					})
				}
	        }
	    });
	}
	//判断是否处于锁屏状态(如果关闭以后则未关闭浏览器之前不再显示)
	if(window.sessionStorage.getItem("lockcms") != "true" && window.sessionStorage.getItem("showNotice") != "true"){
		showNotice();
	}
	$(".showNotice").on("click",function(){
		showNotice();
	})

	//刷新后还原打开的窗口
	if(window.sessionStorage.getItem("menu") != null){
		menu = JSON.parse(window.sessionStorage.getItem("menu"));
		curmenu = window.sessionStorage.getItem("curmenu");
		var openTitle = '';
		for(var i=0;i<menu.length;i++){
			openTitle = '';
			if(menu[i].icon.split("-")[0] == 'icon'){
				openTitle += '<i class="iconfont '+menu[i].icon+'"></i>';
			}else{
				openTitle += '<i class="layui-icon">'+menu[i].icon+'</i>';
			}
			openTitle += '<cite>'+menu[i].title+'</cite>';
			openTitle += '<i class="layui-icon layui-unselect layui-tab-close" data-id="'+menu[i].layId+'">&#x1006;</i>';
			element.tabAdd("bodyTab",{
				title : openTitle,
		        content :"<iframe src='"+menu[i].href+"' data-id='"+menu[i].layId+"'></frame>",
		        id : menu[i].layId
			})
			//定位到刷新前的窗口
			if(curmenu != "undefined"){
				if(curmenu == '' || curmenu == "null"){  //定位到后台首页
					element.tabChange("bodyTab",'');
				}else if(JSON.parse(curmenu).title == menu[i].title){  //定位到刷新前的页面
					element.tabChange("bodyTab",menu[i].layId);
				}
			}else{
				element.tabChange("bodyTab",menu[menu.length-1].layId);
			}
		}
	}

	/******************************弹屏模块BEGIN******************************************/
	//加载派发人员
	LayerSelect.initLayerSelect({
		dom : "order_send_opr",
		url : webpath + "/personnel/getSendPersonList.action",
		type : "post",
		dataType : "json",
		text : "USER_NAME",
		id : "MAXACCEPT"
	});
	form.render();
	
	//弹窗方法
	window.bombScreen=function(phone){
		$("#show_screen_form")[0].reset();

		$("#call_phone").html(phone);
		
		form.val("show_screen_form", {
			"conn_phone": phone
		});
		dialogIndex = layer.open({
			title : '来电弹窗',
			type : 1,
			skin : 'layui-layer-demo', // 样式类名
			anim : 2,
			area : [ '700px', '500px' ],
			shadeClose : false, // 开启遮罩关闭
			content : $("#show_screen_div")
		});
		
		form.on('select(is_send)', function(data){
			if(data.value == "1"){
				$("#order_send_opr_div").show();
			}else{
				$("#order_send_opr_div").hide();
			}
		});
		
		form.render();
	}
	
	//表单提交
	form.on('submit(order_submit_btn)', function(data) {
		
		$.ajax({
			url : webpath + "/order/saveOrder.action",
			method : 'post',
			data : data.field,
			dataType : "json",
			success : function(data1) {
				var resultCode = data1.resultCode;
				if (resultCode == "0000") {
					layer.close(dialogIndex);
					layer.msg('工单创建成功！');
				} else {
					layer.alert('工单创建失败！', {
						icon : 2
					});
				}
			}
		});

	});
	
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
	/******************************弹屏模块END  ******************************************/
})

//打开新窗口
function addTab(_this){
	tab.tabAdd(_this);
}

//捐赠弹窗
function donation(){
	layer.tab({
		area : ['260px', '367px'],
		tab : [{
			title : "微信",
			content : "<div style='padding:30px;overflow:hidden;background:#d2d0d0;'><img src='images/wechat.jpg'></div>"
		},{
			title : "支付宝",
			content : "<div style='padding:30px;overflow:hidden;background:#d2d0d0;'><img src='images/alipay.jpg'></div>"
		}]
	})
}

//弹窗关闭
function closeDialog() {
	layer.close(dialogIndex);
}
