package com.example.springboot.common;

public class ApiResult<T> {
    private int code;
    private String message;
    private T data;

    public ApiResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功响应
    public static <T> ApiResult<T> ok(T data) {
        return new ApiResult<>(200, "success", data);
    }

    public static ApiResult<String> ok() {
        return new ApiResult<>(200, "success", "操作成功");
    }

    // 失败响应
    public static <T> ApiResult<T> fail(String message) {
        return new ApiResult<>(400, message, null);
    }

    // getter和setter
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}