package com.zsj.system.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zsj.system.entity.BlogHomeHpEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/1.
 */
@Data
@Setter
@Getter
public class BlogHomeImgVo {

    private static final long serialVersionUID = 1L;

    public BlogHomeImgVo(BlogHomeHpEntity entity, String username) {
        this.username = username;
        this.id = entity.getId();
        this.createTime = entity.getCreateTime();
        this.photo = entity.getPhoto();
        this.belongUserId = entity.getBelongUserId();
    }

    public BlogHomeImgVo(Long id, String photo, Long belongUserId, Date createTime, String username) {
        this.id = id;
        this.photo = photo;
        this.belongUserId = belongUserId;
        this.username = username;
        this.createTime = createTime;
    }

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 博客首页图片
     */
    private String photo;
    /**
     * 归属用户id
     */
    private Long belongUserId;
    /**
     * 归属用户名称
     */
    private String username;
    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}

