function loadOrderTypeAn(){
	//获取横轴
	var dateList = getDateList(10);
	
	$.ajax({
		url: webpath + "/order/getOrderTypeCount.action",
		type: 'post',
		dataType: "json",
		data: {dateList: dateList},
		success: function(data){
			var resultCode = data.resultCode;
			if(resultCode == "0000"){
				var resultData = data.resultData;
				initOrderTypeChart(dateList, resultData);
			}
		}
	});
}

function initOrderTypeChart(categories, series){
	var chart = Highcharts.chart('order_type_num_an',{
		chart: {
			type: 'column'
		},
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
				text: '工单量(个)'
			}
		},
		tooltip: {
			// head + 每个 point + footer 拼接成完整的 table
			headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
			pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
			'<td style="padding:0"><b>{point.y:1f}</b></td></tr>',
			footerFormat: '</table>',
			shared: true,
			useHTML: true
		},
		plotOptions: {
			column: {
				borderWidth: 0
			}
		},
		series: series,
		credits: {  
            enabled: false     //不显示LOGO 
        }
	});
}