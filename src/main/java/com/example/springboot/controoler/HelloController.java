package com.example.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "服务运行正常";
    }

    @GetMapping("/test")
    public ApiResult test() {
        // 创建一个简单的返回对象
        return new ApiResult(200, "测试接口访问成功", LocalDateTime.now().toString());
    }

    // 内部类用于返回结果
    public static class ApiResult {
        private int code;
        private String message;
        private String data;

        public ApiResult(int code, String message, String data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        // getter方法
        public int getCode() { return code; }
        public String getMessage() { return message; }
        public String getData() { return data; }
    }
}