package com.yl.common.pojo.converPojo;

public class Callfailed extends BaseConver{
	
	private String originator;
	
	private String cause;
	
	private Callertrunk callertrunk;
	
	private Calledtrunk calledtrunk;
	
	private Callerext callerext;
	
	private Calledext calledext;

	public String getOriginator() {
		return originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public Callertrunk getCallertrunk() {
		return callertrunk;
	}

	public void setCallertrunk(Callertrunk callertrunk) {
		this.callertrunk = callertrunk;
	}

	public Calledtrunk getCalledtrunk() {
		return calledtrunk;
	}

	public void setCalledtrunk(Calledtrunk calledtrunk) {
		this.calledtrunk = calledtrunk;
	}

	public Callerext getCallerext() {
		return callerext;
	}

	public void setCallerext(Callerext callerext) {
		this.callerext = callerext;
	}

	public Calledext getCalledext() {
		return calledext;
	}

	public void setCalledext(Calledext calledext) {
		this.calledext = calledext;
	}
}
