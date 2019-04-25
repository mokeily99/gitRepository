layui.config({
	base : "js/"
}).use(['form','element','layer','jquery'],function(){
	var form = layui.form(),
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		element = layui.element(),
		$ = layui.jquery;

	$(".panel a").on("click",function(){
		window.parent.addTab($(this));
	})

	//动态获取文章总数和待审核文章数量,最新文章
	$.get("../json/newsList.json",
		function(data){
			var waitNews = [];
			$(".allNews span").text(data.length);  //文章总数
			for(var i=0;i<data.length;i++){
				var newsStr = data[i];
				if(newsStr["newsStatus"] == "待审核"){
					waitNews.push(newsStr);
				}
			}
			$(".waitNews span").text(waitNews.length);  //待审核文章
			//加载最新文章
			var hotNewsHtml = '';
			for(var i=0;i<5;i++){
				hotNewsHtml += '<tr>'
		    	+'<td align="left">'+data[i].newsName+'</td>'
		    	+'<td>'+data[i].newsTime+'</td>'
		    	+'</tr>';
			}
			$(".hot_news").html(hotNewsHtml);
		}
	)

	//图片总数
	$.get("../json/images.json",
		function(data){
			$(".imgAll span").text(data.length);
		}
	)

	//用户数
	$.get("../json/usersList.json",
		function(data){
			$(".userAll span").text(data.length);
		}
	)

	//新消息
	$.get("../json/message.json",
		function(data){
			$(".newMessage span").text(data.length);
		}
	)


	//数字格式化
	$(".panel span").each(function(){
		$(this).html($(this).text()>9999 ? ($(this).text()/10000).toFixed(2) + "<em>万</em>" : $(this).text());	
	})

	//系统基本参数
	if(window.sessionStorage.getItem("systemParameter")){
		var systemParameter = JSON.parse(window.sessionStorage.getItem("systemParameter"));
		fillParameter(systemParameter);
	}else{
		$.ajax({
			url : "../json/systemParameter.json",
			type : "get",
			dataType : "json",
			success : function(data){
				fillParameter(data);
			}
		})
	}

	//填充数据方法
 	function fillParameter(data){
 		//判断字段数据是否存在
 		function nullData(data){
 			if(data == '' || data == "undefined"){
 				return "未定义";
 			}else{
 				return data;
 			}
 		}
 		$(".version").text(nullData(data.version));      //当前版本
		$(".author").text(nullData(data.author));        //开发作者
		$(".homePage").text(nullData(data.homePage));    //网站首页
		$(".server").text(nullData(data.server));        //服务器环境
		$(".dataBase").text(nullData(data.dataBase));    //数据库版本
		$(".maxUpload").text(nullData(data.maxUpload));    //最大上传限制
		$(".userRights").text(nullData(data.userRights));//当前用户权限
 	}

})


//------------------------------------------------------------------------------------------
//根据查询条件获取有效值并绘制Highcharts
function getChart() {
	
	//加载nine_pie
	
	
	/*var beginTime = $("#begin_time_param").datebox('getValue');
	var endTime = $("#end_time_param").datebox('getValue');
	var orderChannel = "70";
	
	var managerId = kfManagerMaxaccept;
	var userId = "";
	
	var a = new Date(beginTime);
	var b = new Date(endTime);
	if(a.getTime() > b.getTime()){
		alert("结束时间不能早开始时间！！");
	}
	
	if((beginTime==null||beginTime=='') && (endTime==null || endTime=='')){
		alert("请填写查询时间！");
		getTime();
		var now = new Date();
		//格式化日，如果小于9，前面补0
		var day = ("0" + now.getDate()).slice(-2);
		//格式化月，如果小于9，前面补0
		var month = ("0" + (now.getMonth() + 1)).slice(-2);
		//拼装完整日期格式
		beginTime = now.getFullYear()+"-"+(month)+"-"+(day) ;
		endTime = getBeforeDate(30);
	}else if((endTime==null || endTime=='')){
		endTime = new Date().format().toString();
		$("#end_time_param").datebox('setValue',endTime);
	}else if((beginTime==null || beginTime=='')){
		var lastMonth = getAfterDate(endTime, 30);
		$("#begin_time_param").datebox('setValue',lastMonth);
		beginTime = lastMonth;
	}
	
	var datax = []; //x轴：时间 
	var datawy = [];//当前月份通话时间
	
	var dateArr = new Array();
	dateArr = getDayAll(beginTime, endTime);*/
	
	//调取绘制图表方法
	doctorCount();
	patientCount();
	converCount();
	show_online();
	show_online_type();
	
	/*getOrderCount(beginTime, endTime);
	getPhoneTime(beginTime, endTime, datax, datawy, dateArr, userId, managerId);
	getBusyCount(beginTime, endTime);
	getConverCount(beginTime, endTime, userId, managerId);*/
	//getOrderType(beginTime, endTime, userId, managerId, orderChannel);
}


/*function getOrderCount(beginTime, endTime){
	$.ajax({
		url : webpath + "/kfManager/getOrderCount.action?beginTime="+beginTime+"&endTime="+endTime,
		cache: false,//不保存缓存
		type : "POST",
		dataType : "json",
		success : function(data) {
			var nameList = data.xList;
			var yzs = data.yzsList;
			var ydwh = data.ydwhList;
			var ydhf = data.ydhfList;
			var ywc = data.ywcList;
			
			var ydlj = data.yljList;
			
			orderCount(nameList, yzs, ydwh, ydlj, ydhf, ywc);
		}
	});
}*/

//Highcharts线性图表
//医生数量
function doctorCount() {
	var chart = Highcharts.chart('doctorCount',{
		chart: {
			type: 'column'
		},
		title: {
			text: ''
		},
		credits : {
			enabled : false// 去除水印
		},
		xAxis: {
			categories: [
				'一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'
			],
			crosshair: true
		},
		yAxis: {
			min: 0,
			title: {
				text: '人数 (num)'
			}
		},
		tooltip: {
			// head + 每个 point + footer 拼接成完整的 table
			headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
			pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
			'<td style="padding:0"><b>{point.y:.1f} num</b></td></tr>',
			footerFormat: '</table>',
			shared: true,
			useHTML: true
		},
		plotOptions: {
			column: {
				borderWidth: 0
			}
		},
		series: [{
			name: '咨询',
			data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]
		}, {
			name: '缴费',
			data: [83.6, 78.8, 98.5, 93.4, 106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3]
		}, {
			name: '业务办理',
			data: [48.9, 38.8, 39.3, 41.4, 47.0, 48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2]
		}, {
			name: '其他',
			data: [42.4, 33.2, 34.5, 39.7, 52.6, 75.5, 57.4, 60.4, 47.6, 39.1, 46.8, 51.1]
		}]
	});
}


//获取数据并绘制病人数量图表
/*function getPhoneTime(beginTime, endTime, datax, datawy, dateArr, userId, managerId){
	var test1 = [];
	var b = 0.0;
	$.ajax({
		url : webpath + "/kfManager/getPhoneTime.action?beginTime="+beginTime+"&endTime="+endTime+"&userId="+userId+"&managerId="+managerId,
		cache: false,//不保存缓存
		type : "POST",
		dataType : "json",
		success : function(data) {
			datax = data.xList;
			datawy = data.yList;
			phoneTime(datax,datawy);
		}
	});
}*/

//病人数量
function patientCount() {
	var chart = Highcharts.chart('patientCount',{
		chart: {
			type: 'column'
		},
		title: {
			text: ''
		},
		credits : {
			enabled : false// 去除水印
		},
		xAxis: {
			categories: [
				'一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'
			],
			crosshair: true
		},
		yAxis: {
			min: 0,
			title: {
				text: '人数 (num)'
			}
		},
		tooltip: {
			// head + 每个 point + footer 拼接成完整的 table
			headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
			pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
			'<td style="padding:0"><b>{point.y:.1f} num</b></td></tr>',
			footerFormat: '</table>',
			shared: true,
			useHTML: true
		},
		plotOptions: {
			column: {
				borderWidth: 0
			}
		},
		series: [{
			name: '营销短信',
			data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]
		}, {
			name: '通知短信',
			data: [83.6, 78.8, 98.5, 93.4, 106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3]
		}, {
			name: '生日祝福',
			data: [48.9, 38.8, 39.3, 41.4, 47.0, 48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2]
		}, {
			name: '业务办理',
			data: [48.9, 38.8, 39.3, 41.4, 47.0, 48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2]
		}, {
			name: '其他',
			data: [48.9, 38.8, 39.3, 41.4, 47.0, 48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2]
		}]
	});
}

/*function getConverCount(beginTime, endTime, userId, managerId , datax, datay, dataywhf, datayywc, dateArr, orderChannel){
var test4 = [];
$.ajax({
	url : webpath + "/kfManager/getConverCount.action?beginTime="+beginTime+"&endTime="+endTime+"&userId="+userId+"&managerId="+managerId+"&orderChannel="+orderChannel,
	cache: false,//不保存缓存
	type : "POST",
	dataType : "json",
	success : function(data) {
		datax = data.xList;
		datacy = data.ycList;
		
		//通话总时长
		converCount(datax, datacy);
	}
});
}*/

//咨询解答情况
function converCount() {
	var chart = Highcharts.chart('question',{
		chart : {
			renderTo: 'container',
			type : 'line'
		},
		credits : {
			enabled : false
		// 去除水印
		},
		title : {
			text : ''
		},
		xAxis : {
			categories: [
				'一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'
			],
			crosshair: true
		},
		yAxis : {
			title : {
				text : '(次)'
			}
		},
		//设置滚动条    
	    scrollbar: {
	        enabled: true
	    },
		plotOptions : {
			line : {
				dataLabels : {
					// 开启数据标签
					enabled : true
				},
				// 关闭鼠标跟踪，对应的提示框、点击事件会失效
				enableMouseTracking : false
			}
		},
		series : [ {
			name: '通常通话',
			data: [43934, 52503, 57177, 69658, 97031, 119931, 137133, 154175]
		}, {
			name: '主动挂断',
			data: [24916, 24064, 29742, 89851, 32490, 30282, 98121, 40434]
		}, {
			name: '被动挂断',
			data: [61744, 57722, 86005, 39771, 20185, 114377, 32147, 39387]
		}, {
			name: '异常链接',
			data: [null, null, 7988, 12169, 15112, 22452, 34400, 34227]
		}, {
			name: '其他',
			data: [52908, 9948, 8105, 11248, 38989, 11816, 18274, 18111]
		}]
	});
}


//直播情况
/*function getBusyCount(beginTime, endTime){
	$.ajax({
		url : webpath + "/kfManager/getBusyCount.action?beginTime="+beginTime+"&endTime="+endTime,
		cache: false,//不保存缓存
		type : "POST",
		dataType : "json",
		success : function(data) {
			var datax = data.xList;
			var ybList = data.ybList;
			var yzList = data.yzList;
			show_online(datax, ybList, yzList);
		}
	});
}*/

//直播情况
function show_online() {
	Highcharts.chart('show_online', {
		chart: {
			plotBackgroundColor: null,
			plotBorderWidth: null,
			plotShadow: false,
			type: 'pie'
		},
		title: {
			text: '类型占比'
		},
		credits : {
			enabled : false// 去除水印
		},
		tooltip: {
			pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		},
		plotOptions: {
			pie: {
				allowPointSelect: true,
				cursor: 'pointer',
				dataLabels: {
					enabled: true,
					format: '<b>{point.name}</b>: {point.percentage:.1f} %',
					style: {
						color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
					}
				}
			}
		},
		series: [{
			name: 'Brands',
			colorByPoint: true,
			data: [{
				name: '普通客户',
				y: 61.41,
				sliced: true,
				selected: true
			}, {
				name: '特殊客户',
				y: 11.84
			}, {
				name: '集团客户',
				y: 10.85
			}, {
				name: '政企客户',
				y: 4.67
			}]
		}]
	});
}

//直播类型占比
function show_online_type() {
	Highcharts.chart('show_online_type', {
		chart: {
			plotBackgroundColor: null,
			plotBorderWidth: null,
			plotShadow: false,
			type: 'pie'
		},
		title: {
			text: '年龄占比'
		},
		credits : {
			enabled : false// 去除水印
		},
		tooltip: {
			pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		},
		plotOptions: {
			pie: {
				allowPointSelect: true,
				cursor: 'pointer',
				dataLabels: {
					enabled: true,
					format: '<b>{point.name}</b>: {point.percentage:.1f} %',
					style: {
						color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
					}
				}
			}
		},
		series: [{
			name: 'Brands',
			colorByPoint: true,
			data: [{
				name: '20以下',
				y: 10.41,
				sliced: true,
				selected: true
			}, {
				name: '20-30',
				y: 21.84
			}, {
				name: '30-40',
				y: 20.85
			}, {
				name: '40以上',
				y: 14.67
			}]
		}]
	});
}



