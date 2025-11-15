package com.example.coffee.common;

import lombok.Data;

@Data
public class Result {
    private Integer code;
    private String message;
    private Object data;

    public static Result success(String message, Object data) {
        Result result = new Result();
        result.setCode(0);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static Result success(Object data) {
        return success("成功", data);
    }

    public static Result error(String message) {
        Result result = new Result();
        result.setCode(1);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
}