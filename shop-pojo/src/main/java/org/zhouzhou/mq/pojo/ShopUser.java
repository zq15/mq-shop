package org.zhouzhou.mq.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ShopUser implements Serializable {
    private Long userId;

    private String userName;

    private String userPassword;

    private String userMobile;

    private Integer userScore;

    private Date userRegTime;

    private BigDecimal userMoney;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile == null ? null : userMobile.trim();
    }

    public Integer getUserScore() {
        return userScore;
    }

    public void setUserScore(Integer userScore) {
        this.userScore = userScore;
    }

    public Date getUserRegTime() {
        return userRegTime;
    }

    public void setUserRegTime(Date userRegTime) {
        this.userRegTime = userRegTime;
    }

    public BigDecimal getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(BigDecimal userMoney) {
        this.userMoney = userMoney;
    }
}