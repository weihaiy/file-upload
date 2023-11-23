package com.wei.util.fileupload.commons;

public class Result {
    private Boolean success;

    private String message;

    /**
     * 构造返回结果
     * @param success "true" or "false"
     * @param message 返回的消息
     */
    public Result(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Result() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
