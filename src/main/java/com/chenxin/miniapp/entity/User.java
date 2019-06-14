package com.chenxin.miniapp.entity;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private String openId;

    private String nickName;

    private String gender;

    private String language;

    private String city;

    private String province;

    private String country;

    private String avatarUrl;

    private String unionId;

    private String appid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getAppid() {
        return appid;
    }

    @Override
    public String toString() {
        return "WxUserInfo{" +
                "id=" + id +
                ", openId=" + openId +
                ", nickName=" + nickName +
                ", gender=" + gender +
                ", language=" + language +
                ", city=" + city +
                ", province=" + province +
                ", country=" + country +
                ", avatarUrl=" + avatarUrl +
                ", unionId=" + unionId +
                ", appid=" + appid +
                "}";
    }
}
