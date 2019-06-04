function senWordsAn(){
	$.ajax({
		url: webpath + "/senwords/getWordsCloud.action",
		type: 'post',
		dataType: "json",
		success: function(data){
			var resultCode = data.resultCode;
			if(resultCode == "0000"){
				var resultData = data.resultData;
				initWordsCloud(resultData);
			}
		}
	});
}

function initWordsCloud(data){
	Highcharts.chart('sen_words_an', {
		series: [{
			type: 'wordcloud',
			data: [{name:"推销", weight:2}, {name:"购买", weight:2}, {name:"收费", weight:2}, {name:"费用", weight:2}]
		}],
		title: {
			text: ''
		},
		credits: {  
            enabled: false     //不显示LOGO 
        }
	});
}