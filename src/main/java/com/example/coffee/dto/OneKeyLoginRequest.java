package com.example.coffee.dto;

import lombok.Data;

@Data
public class OneKeyLoginRequest {
    private String code;
    private String openId;
    private String nickName;
    private String avatarUrl;
    private String encryptedData;
    private String iv;
}