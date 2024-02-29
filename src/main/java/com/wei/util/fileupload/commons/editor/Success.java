package com.wei.util.fileupload.commons.editor;

/**
 * @author weihaiyang
 * @since 2024/2/29 17:04 星期四
 */
public class Success {
	private int errno;
	private SuccessMsg data;

	public int getErrno() {
		return errno;
	}

	public void setErrno(int errno) {
		this.errno = errno;
	}

	public SuccessMsg getData() {
		return data;
	}

	public void setData(SuccessMsg data) {
		this.data = data;
	}
}
