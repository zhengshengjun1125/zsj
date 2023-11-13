package com.zsj.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zsj.common.utils.FileGlobalHelper;
import com.zsj.common.utils.ObjectUtil;
import lombok.Data;

/**
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-12 15:55:51
 */
@Data
@TableName("sys_file")
public class FileEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private static String VIDEO_TYPE = "视频";

    private static String IMAGE_TYPE = "图片";

    private static String MUSIC_TYPE = "音乐";

    private static String OTHER_TYPE = "其它";
    /**
     * 文件id(雪花)
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 上传之后的文件名称
     */
    private String upFileName;
    /**
     * 文件后缀
     */
    private String fileSuffix;
    /**
     * 文件类型
     */
    private String type;
    /**
     * 文件所属
     */
    private String affiliation;
    /**
     * 文件路径
     */
    private String url;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 上传时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 是否删除
     */
    private Integer disappear;//1 yes  0 no

    public FileEntity() {
    }

    public FileEntity(String fileName, String fileSuffix, String affiliation, Date createTime, String url, String real,Long size) {
        this.fileName = fileName;
        this.fileSuffix = fileSuffix;
        this.affiliation = affiliation;
        this.createTime = createTime;
        this.upFileName = real;
        this.url = url;
        this.fileSize = size;
        String suffix = FileGlobalHelper.fileTypeMap().get(fileSuffix);
        if (ObjectUtil.objectIsNotNull(suffix)){
            if (suffix.equals(VIDEO_TYPE)) this.type = VIDEO_TYPE;
            if (suffix.equals(IMAGE_TYPE)) this.type = IMAGE_TYPE;
            if (suffix.equals(MUSIC_TYPE)) this.type = MUSIC_TYPE;
        } else this.type = OTHER_TYPE;
    }
}
