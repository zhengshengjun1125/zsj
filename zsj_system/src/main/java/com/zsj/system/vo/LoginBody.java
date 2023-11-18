package com.zsj.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/10/11.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginBody {

    private String email;

    private String username;

    private String password;

    private String code;

    private String key;


    @Override
    public String toString() {
        return "LoginBody{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", code='" + code + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
