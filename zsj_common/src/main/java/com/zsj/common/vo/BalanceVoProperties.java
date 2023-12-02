package com.zsj.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author <a href="https://gitee.com/zhengshengjun">zsj</a>
 * @date 2023/12/2.
 */
@Data
@AllArgsConstructor
public class BalanceVoProperties implements Serializable {

    private double deduct;

    private String user;

    public BalanceVoProperties() {
    }
}
