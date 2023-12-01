package com.zsj.system.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/12/1.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicRequestBody implements Serializable {
    private Integer isSelf;
    private String fileName;
}

