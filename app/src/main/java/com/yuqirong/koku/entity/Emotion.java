package com.yuqirong.koku.entity;

import java.io.Serializable;

/**
 * 表情类
 */
public class Emotion implements Serializable {

	private static final long serialVersionUID = 4734938312469844000L;

	private String key;
	
	private byte[] data;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
}
