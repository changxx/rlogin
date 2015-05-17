package com.rlogin.domain;

/**
 * 公积金账户状态
 * 
 * @author changxx
 *
 */
public enum GjjAccStatus {

	NORMAL(0, "正常"), SEALED(1, "封存"), EMPTY_ACCOUNT(3, "空账"), CANCEL(9, "销户");

	private int code;

	private String name;

	private GjjAccStatus(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
