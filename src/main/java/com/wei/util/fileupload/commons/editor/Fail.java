package com.wei.util.fileupload.commons.editor;

/**
 * @author weihaiyang
 * @since 2024/2/29 17:05 星期四
 */
public class Fail {
	private int errno;
	private String message;

	public int getErrno() {
		return errno;
	}

	public void setErrno(int errno) {
		this.errno = errno;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
