package com.rlogin.domain.gjj.result.detail;

public class GjjDetailResult {

	private Data data;

	private Integer returnCode;

	@Override
	public String toString() {
		return "GjjDetailResult [data=" + data + ", returnCode=" + returnCode + ", getData()=" + getData() + ", getReturnCode()=" + getReturnCode()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Integer getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(Integer returnCode) {
		this.returnCode = returnCode;
	}

}
