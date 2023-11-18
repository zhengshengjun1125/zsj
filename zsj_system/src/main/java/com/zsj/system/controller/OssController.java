package com.zsj.system.controller;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectRequest;
import com.zsj.common.utils.*;
import com.zsj.system.entity.FileEntity;
import com.zsj.system.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import com.zsj.system.entity.OssEntity;
import com.zsj.system.service.OssService;
import org.springframework.web.multipart.MultipartFile;


/**
 * 文件上传
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-10-05 20:07:09
 */
@RestController
@RequestMapping("system/oss")
@Slf4j
public class OssController {


    @Autowired
    private OssService ossService;

    @Autowired
    @Lazy
    private FileService fileService;

    @Autowired
    @Lazy
    private StringRedisTemplate stringRedisTemplate;

    //调用oss资源上传文件
    @Autowired
    @Lazy
    OSS ossClient;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucket;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;

    @Value("${spring.cloud.alicloud.secret-key}")
    private String secret;


    /**
     * 单一文件上传
     *
     * @param requestU 请求人
     * @param file     文件对象
     */
    @PostMapping("/uploadOssFileSingle")
    public R uploadOssFileSingle(@RequestHeader("system_api_Authorize_name") String requestU,
                                 @NotNull MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        long size = file.getSize();//文件大小 图片不可以超过
        log.info("上传文件名为:{} 文件大小字节为:{}", originalFilename, size);
        assert originalFilename != null;
        String suffix = FileGlobalHelper.getSuffix(originalFilename);
        Map<String, String> filter_map = FileGlobalHelper.fileTypeMap();
        String type = filter_map.get(suffix) == null ? "other" : filter_map.get(suffix);
        String objectName = requestU + '/';
        switch (type) {
            case "视频":
                if (size > 52428800) {
                    return R.error("你不可以上传超过50M的视频文件");
                }
                objectName += BaseUtil.year_month_day() + "/video/";
                break;
            case "图片":
                if (size > 3145728) {
                    return R.error("你不可以上传超过3M的图片文件");
                }
                objectName += BaseUtil.year_month_day() + "/image/";
                break;
            case "音乐":
                if (size > 20971520) {
                    return R.error("你不可以上传超过20M的音乐文件");
                }
                objectName += BaseUtil.year_month_day() + "/music/";
                break;
            default:
                if (size > 10485760) {
                    return R.error("文件上传未知类型时,默认大小为10M");
                }
                objectName += BaseUtil.year_month_day() + "/common/";
                break;
        }
        //之前的文件名称是生成了12位随机数字
        //保证文件名绝对不可能重复 或者重复率小于0.000000000001%
        String realName = Encrypt.encrypt_uuid_6() + BaseUtil.randomNumber(6) + '.' + suffix;
        objectName += realName;
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, secret);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, objectName, file.getInputStream());
        ossClient.putObject(putObjectRequest);
        String url = "https://" + bucket + '.' + endpoint + '/' + objectName;
        log.info("上传文件路径为{}", url);
        new Thread(() -> {
            //进行文件保存
            fileService.save(new FileEntity(originalFilename, suffix, requestU,
                    new Date(System.currentTimeMillis()), url, realName, size));
        }).start();
        //进行redis中所有文件key的删除

        new Thread(() -> {
            Set<String> keys = stringRedisTemplate.keys("fileList/*");
            assert keys != null;
            stringRedisTemplate.delete(keys);
        }).start();

        return R.ok("上传成功").put("url", url);
    }



    @GetMapping("/policyToPhoto")
    public R policyToPhoto() {
        // 填写Host地址，格式为https://bucketname.endpoint。
        String host = "https://" + bucket + "." + endpoint;
        // 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dir = format + "/photo/";
        return R.ok().put("data", initMap(dir, host));
    }


    @GetMapping("/policyToVideo")
    public R policyToVideo() {
        // 填写Host地址，格式为https://bucketname.endpoint。
        String host = "https://" + bucket + "." + endpoint;
        // 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
        String format = new SimpleDateFormat("yyyy:MM:hh").format(new Date());
        String dir = format + "/video";
        return R.ok().put("data", initMap(dir, host));
    }

    Map<String, String> initMap(String dir, String host) {
        Map<String, String> respMap = null;
        try {
            long expireTime = 6000;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);
            respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return respMap;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = ossService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        OssEntity oss = ossService.getById(id);

        return R.ok().put("oss", oss);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody OssEntity oss) {
        ossService.save(oss);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody OssEntity oss) {
        ossService.updateById(oss);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        ossService.removeByIds(Arrays.asList(ids));

        return R.ok();

    }


}
