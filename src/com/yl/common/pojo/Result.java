package com.yl.common.pojo;
/**
 * 公共写出对象，用于异步向前台写出
 * @author Administrator
 *
 */
public class Result {
	
	public Result(){
		setResultCode("0000");
		setResultMsg("操作成功！");
		setResultData(null);
		setResultRow(0);
	}

	private String resultCode;
	
	private String resultMsg;
	
	private Object resultData;
	
	private Integer resultRow;

	public Integer getResultRow() {
		return resultRow;
	}

	public void setResultRow(Integer resultRow) {
		this.resultRow = resultRow;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public Object getResultData() {
		return resultData;
	}

	public void setResultData(Object resultData) {
		this.resultData = resultData;
	}
	
	public void newInit(){
		setResultCode("0000");
		setResultMsg("操作成功！");
		setResultData(null);
	}
}