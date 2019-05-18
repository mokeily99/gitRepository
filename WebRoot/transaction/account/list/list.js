var dialogIndex;
layui.use(['form', 'layer', 'table', 'upload', 'laydate'], function() {
	var treeSelect = layui.treeSelect;
	var table = layui.table;
	form = layui.form;
	$ = layui.jquery;
	layer = layui.layer;
	var upload = layui.upload;
	var laydate = layui.laydate;
	
	var accountTable = table.render({
		id: "account_grid_list",
		elem : '#account_grid_list',
		url : webpath + "/account/accountList.action",
		method: "post",
		where: {dept_name: $("#dept_name").val()},
		cols : [ [
			{
				type : 'checkbox'
			},
			{
				field : 'DEPT_NAME',
				title : '部门账户'
			},
			{
				field : 'ACC_BALANCE',
				title : '账户余额(元)'
			},
			{
				field : 'CREATE_TIME',
				title : '账户创建时间'
			},
			{
				field : 'ABLE_FLAG',
				title : '账户状态',
				templet: function(row){
					var ableFlag = row.ABLE_FLAG;
					var obj = getCodeName("ABLE_FLAG", ableFlag);
					return obj[0].CODE_NAME;
				}
			}
		] ],
		page : true,
		loading : true
	});
	
	//查询绑定
	$('#query_account_btn').click(function() {
		table.reload("account_grid_list", {where: {dept_name: $("#dept_name").val()}});
	});
	
	//账户充值
	$('#add_fee_btn').click(function() {
		var checkData = table.checkStatus("account_grid_list");
		if(checkData.data.length < 1){
			layer.msg('未选择任何数据！',{icon:0});
			return;
		}
		
		if(checkData.data.length > 1){
			layer.msg('每次只能操作一条信息！',{icon:0});
			return;
		}
		
		var accData = checkData.data[0];
		
		dialogIndex = layer.open({
			type : 1,
			title : '设置账户金额',
			content : $('#add_fee_div'),
			area : [ '500px', '240px' ]
		});
		
		//赋值
		form.val("add_fee_form", {
			  "add_dept_name": accData.DEPT_NAME,
			  "add_balance_fee": accData.ACC_BALANCE,
		});

		//账户充值提交
		form.on('submit(add_fee_form_sub)', function(data) {

			$.ajax({
				url : webpath + "/account/setAccountBalance.action",
				method : 'post',
				data : data.field,
				dataType : "json",
				data: {maxaccept: accData.MAXACCEPT, balance: $("#add_balance_fee").val()},
				success : function(data1) {
					layer.close(dialogIndex);
					var resultCode = data1.resultCode;
					if (resultCode == "0000") {
						layer.msg('操作成功！');
						accountTable = table.reload("account_grid_list");
					} else {
						layer.alert('操作失败，请重新操作！', {
							icon : 2
						});
					}
				}
			});

		});
	});
	
	//表单校验
  	form.verify({
  		fee: function(value, item) { //value：表单的值、item：表单的DOM对象
  			if(value != ""){
  				var three = /^(([1-9]{1}\d*)|(0{1}))(\.\d{3})$/;
  				if (!three.test(value)) {
  					return '金额格式错误！样例：123.000';
  				}
  			}
  		}
  	});
});

//弹窗关闭
function closeDialog() {
	layer.close(dialogIndex);
}