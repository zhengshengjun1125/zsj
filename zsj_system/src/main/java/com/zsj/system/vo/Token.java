package com.zsj.system.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/10/11.
 */
@Data
public class Token {

    private String token;

    private Date date;

    private String timestamp;

    public Token() {
    }

    public Token(String token) {
        this.token = token;
        this.date = new Date(System.currentTimeMillis());
        this.timestamp = String.valueOf(System.currentTimeMillis());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
