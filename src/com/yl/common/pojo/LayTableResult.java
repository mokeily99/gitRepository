package com.yl.common.pojo;

import java.io.Serializable;

public class LayTableResult<T> implements Serializable {

	private static final long serialVersionUID = 1994426824918852082L;
	private  Integer code;
	private String msg;
    private Integer count;
    private T data;
    
    public LayTableResult() {
    	setCode(0);
    	setMsg("success");
    	setCount(0);
    	setData(null);
    }
    
    public LayTableResult(Integer code, String msg, Integer count, T data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

   
}
