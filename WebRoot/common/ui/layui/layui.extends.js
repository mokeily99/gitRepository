var LayerGrid = {
		
		pageLoadFlag:true,
		
		initLayerGrid:function (gridObj, pageIndex){
		//清空列表
		$("#"+gridObj.dom+"_laygrid").html("");
		
		//组装分页参数
		if(gridObj.page){
			if(isEmpty(gridObj.pageSize)){
				gridObj.pageSize = 10;
			}
			if(isEmpty(pageIndex)){
				pageIndex = 1;
			}
			
			if(gridObj.queryParams == undefined){
				gridObj.queryParams = {pageSize: gridObj.pageSize, pageIndex: pageIndex};
			}else{
				gridObj.queryParams.pageSize = gridObj.pageSize;
				gridObj.queryParams.pageIndex = pageIndex;
			}
		}
		$.ajax({
			url: gridObj.url,
			type: gridObj.type==undefined?"post":gridObj.type,
			dataType: gridObj.dataType==undefined?"json":gridObj.dataType,
			data: gridObj.queryParams,
			success: function(data){
				
				var resultCode = data.resultCode;
				if(resultCode == "0000"){
					var resultData = data.resultData;
					LayerGrid.initLayerGridData(gridObj, data);
					
					//调用回调
					if(gridObj.success != undefined){
						gridObj.success(data);
					}
				}else{
					LayerGrid.initLayerGridData(gridObj, data);
				}
			}
		});
	},
	initLayerGridData:function (gridObj, data){
		var fieldArr = gridObj.columns;
		var resultData = data.resultData;
		var resultRow = data.resultRow;
		var gridHtml = [];
		var domDiv = gridObj.dom+"_laygrid";
		$("#"+gridObj.dom).append("<div id=\"" +domDiv+ "\" style=\"height:100%;width:100%;overflow:auto;\"></div>");
		
		gridHtml.push("<table class=\"layui-table\" style=\"width:100%;;margin: 0 auto;\">");
		gridHtml.push("<colgroup>");
		$.each(fieldArr,function(index,obj){
			if(obj.visible != false){
				gridHtml.push("<col>");
			}
		});
		gridHtml.push("</colgroup>");
		
		gridHtml.push("<thead>");
		gridHtml.push("<tr>");
		$.each(fieldArr,function(index,obj){
			if(obj.visible != false){
				gridHtml.push("<th style=\"text-align:center;\">" +obj.title+ "</th>");
			}
		});
		gridHtml.push("<tr>");
		gridHtml.push("</thead>");
		if(!isEmpty(resultRow) && resultRow > 0){
			
			gridHtml.push("<tbody>");
			$.each(resultData,function(indexX,objX){
				gridHtml.push("<tr>");
				$.each(fieldArr,function(indexY,objY){
					var value = objX[objY.field];
					if(objY.visible != false){
						if(!isEmpty(value)){
							if(isEmpty(objY.formatter)){
								gridHtml.push("<td>" +value+ "</td>");
							}else{
								gridHtml.push("<td>" +objY.formatter(value, objX, indexX)+ "</td>");
							}
						}else{
							gridHtml.push("<td></td>");
						}
					}
				});
				gridHtml.push("</tr>");
			});
			gridHtml.push("</tbody>");
			gridHtml.push("</table>");
			
			
			//分页
			if(LayerGrid.pageLoadFlag){
				LayerGrid.laypage(gridObj, data);
				//分页加载标识
				LayerGrid.pageLoadFlag = false;
			}
		}
		$("#"+domDiv).html(gridHtml.join(""));
	},
	laypage:function(gridObj, data){
		if(gridObj.page != true){
			return;
		}
		
		$("#"+gridObj.dom).append("<div id=\"" +gridObj.dom+ "_laypage_div\" style=\"text-align:center;\"></div>");
		
		var row = data.resultRow;
		layui.use('laypage', function(){
			var laypage = layui.laypage;
			  
			//执行一个laypage实例
		  	laypage.render({
		    elem: gridObj.dom+'_laypage_div' //注意，这里的 test1 是 ID，不用加 # 号
			    ,count: row //数据总数，从服务端得到
			    ,limit: gridObj.pageSize
			    ,jump: function(obj, first){
			        console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
			        console.log(obj.limit); //得到每页显示的条数
			        if(!first){
			        	LayerGrid.initLayerGrid(gridObj, obj.curr)
			        }
			    }
		  	});
		});
	}
}

var LayerSelect = {
	initLayerSelect:function(selectObj){
		$.ajax({
			url: selectObj.url,
			type: selectObj.type==undefined?"post":selectObj.type,
			dataType: selectObj.dataType==undefined?"json":selectObj.dataType,
			async:false,
			data: selectObj.queryParams,
			success: function(data){
				LayerSelect.initLayerSelectData(selectObj, data);
			}
		});
	}, 
	initLayerSelectData:function(selectObj, data){
		var selectHtml = [];
		
		var defaultText = selectObj.defaultText;
		if(defaultText != undefined){
			selectHtml.push("<option value=\"\">" +defaultText+ "</option>")
		}
		
		$.each(data,function(index,value){
			var selectedID = selectObj.selectedID;
			if(selectedID != undefined){
				if(selectedID == value[selectObj.id]){
					selectHtml.push("<option value=\"" +value[selectObj.id]+ "\" selected=\"selected\">" +value[selectObj.text]+ "</option>")
				}else{
					selectHtml.push("<option value=\"" +value[selectObj.id]+ "\">" +value[selectObj.text]+ "</option>")
				}
			}else{
				selectHtml.push("<option value=\"" +value[selectObj.id]+ "\">" +value[selectObj.text]+ "</option>")
			}
		});
		debugger;
		$("#"+selectObj.dom).html(selectHtml.join(""));
	}
}