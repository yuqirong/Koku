package com.yuqirong.koku.module.model.entity;

import java.io.Serializable;

/**
 * 表情类
 */
public class Emotion implements Serializable {

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
