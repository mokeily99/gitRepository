package com.yl.common.pojo.converPojo;

public class Cdr extends BaseConver{

	private String callfrom;
	
	private String callto;
	
	private String status;
	
	private String reason;
	
	private String type;
	
	private String calltime;

	private String answertime;
	
	private String hanguptime;
	
	private String talktime;
	
	private String record_path;
	
	private String download_path;

	public String getCallfrom() {
		return callfrom;
	}

	public void setCallfrom(String callfrom) {
		this.callfrom = callfrom;
	}

	public String getCallto() {
		return callto;
	}

	public void setCallto(String callto) {
		this.callto = callto;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCalltime() {
		return calltime;
	}

	public void setCalltime(String calltime) {
		this.calltime = calltime;
	}

	public String getAnswertime() {
		return answertime;
	}

	public void setAnswertime(String answertime) {
		this.answertime = answertime;
	}

	public String getHanguptime() {
		return hanguptime;
	}

	public void setHanguptime(String hanguptime) {
		this.hanguptime = hanguptime;
	}

	public String getTalktime() {
		return talktime;
	}

	public void setTalktime(String talktime) {
		this.talktime = talktime;
	}

	public String getRecord_path() {
		return record_path;
	}

	public void setRecord_path(String record_path) {
		this.record_path = record_path;
	}

	public String getDownload_path() {
		return download_path;
	}

	public void setDownload_path(String download_path) {
		this.download_path = download_path;
	}
}
