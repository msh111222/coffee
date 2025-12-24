package com.example.coffee.dto;

public class UserProfileResponse {
  private Long userId;
  private String nickname;
  private String birthday; // yyyy-MM-dd or null
  private String phoneNumber;
  private String shippingAddress;

  public Long getUserId() { return userId; }
  public void setUserId(Long userId) { this.userId = userId; }

  public String getNickname() { return nickname; }
  public void setNickname(String nickname) { this.nickname = nickname; }

  public String getBirthday() { return birthday; }
  public void setBirthday(String birthday) { this.birthday = birthday; }

  public String getPhoneNumber() { return phoneNumber; }
  public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

  public String getShippingAddress() { return shippingAddress; }
  public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
}