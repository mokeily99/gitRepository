function loadUserNumAn(){
	//获取横轴
	var dateList = getDateList(10);
	
	$.ajax({
		url: webpath + "/personnel/getUserNum.action",
		type: 'post',
		dataType: "json",
		data: {dateList: dateList},
		success: function(data){
			var resultCode = data.resultCode;
			if(resultCode == "0000"){
				var resultData = data.resultData;
				initUserNumChart(dateList, resultData);
			}
		}
	});
}

function initUserNumChart(categories, data){
	var chart = Highcharts.chart('user_num_an',{
		title: {
			text: ''
		},
		xAxis: {
			categories: categories,
			crosshair: true
		},
		yAxis: {
			min: 0,
			title: {
				text: '总人数(个)'
			}
		},
		tooltip: {
			headerFormat: '<b>{series.name}</b><br />',
			pointFormat: '总数{point.y}'
		},
		series: [{
			name: "人员",
			data: data
		}],
		credits: {  
            enabled: false     //不显示LOGO 
        }
	});
}