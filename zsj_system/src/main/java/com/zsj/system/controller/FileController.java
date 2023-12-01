package com.zsj.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.reflect.TypeToken;
import com.zsj.common.utils.*;
import com.zsj.common.vo.EmailVoProperties;
import com.zsj.system.entity.FileEntity;
import com.zsj.system.entity.UserEntity;
import com.zsj.system.param.Location;
import com.zsj.system.service.UserService;
import com.zsj.system.util.QRCodeUtil;
import com.zsj.system.util.WaterMarkUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.zsj.system.service.FileService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-12 15:55:51
 */
@RestController
@RequestMapping("system/file")
@Slf4j
public class FileController {
    private static final String DEDUCT_LOCK_NAME = "DEDUCT-CUT-LOCK";

    @Autowired
    private FileService fileService;

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    @Lazy
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    @Lazy
    private RabbitTemplate rabbitTemplate;


    @Autowired
    private RedissonClient redissonClient;

    @PostMapping("/list/{cur}/{size}")
    public R list(@RequestHeader("system_api_Authorize_name") String name,
                  @PathVariable("cur") Integer cur,
                  @PathVariable("size") Integer size,
                  @NotNull @RequestBody FileEntity entity) {
        //带参的不缓存 直接查库
        if (!fileEntityParamIsEmptyQ(entity)) {
            Page<FileEntity> list = fileService.pageConditionUser(name, cur, size, entity);
            return R.ok().put("data", list);
        }
        //不带参 缓存一下
        ValueOperations<String, String> ops =
                stringRedisTemplate.opsForValue();
        String json = ops.get("fileList/" + name + cur + size);
        if (ObjectUtil.isNullOrEmpty(json)) {
            //缓存中没有
            Page<FileEntity> entityPage;
            if (name.equals("zsj")) {
                entityPage = fileService.page(new Page<>(cur, size));
            } else {
                entityPage = fileService.page(new Page<>(cur, size),
                        new QueryWrapper<FileEntity>().eq("Affiliation", name));
            }
            ops.set("fileList/" + name + cur + size, GsonUtil.gson.toJson(entityPage), 1, TimeUnit.HOURS);
            return R.ok().put("data", entityPage);
        }
        //缓存命中
        return R.ok().put("data", GsonUtil.gson.fromJson(json, new TypeToken<Page<FileEntity>>() {
        }.getType()));
    }

    @GetMapping("/qrcode/check")
    @Transactional
    public String check(@RequestParam("key") String key, @RequestParam("user") String user) {
        String obj = stringRedisTemplate.opsForValue().get(key);
        if (!ObjectUtil.isNullOrEmpty(obj)) {
            //给用户添加10000的Z币
            double add = 10000d;
            UserEntity one = userService.getOne(new QueryWrapper<UserEntity>().eq("username", user));
            if (!ObjectUtil.objectIsNull(one)) {
                boolean update = userService.update(new UpdateWrapper<UserEntity>().eq("username", one.getUsername()).set("balance", one.getBalance() + add));
                if (update) {
                    EmailVoProperties emailVoProperties = new EmailVoProperties(one.getEmail(), EmailVoProperties.RECHARGE, add, one.getUsername(), BaseUtil.currentTime());
                    rabbitTemplate.convertAndSend(GlobalValueToExchange.EMAIL_EXCHANGE, GlobalValueToExchange.EMAIL_QUEUE, emailVoProperties, new CorrelationData(UUID.randomUUID().toString()));
                } else log.error("出现了错误,充值邮件错误");
            }
            //删除redis中的key
            stringRedisTemplate.delete(key);
            String token = stringRedisTemplate.opsForValue().get(user);
            //这里我们把token删了
            if (!ObjectUtil.isNullOrEmpty(token)) stringRedisTemplate.delete(token);
            return "<div><h1 style=\"color: red;text-align: center;font-size: 60px;\">充值成功</h1></div>";
        }
        return "<div><h1 style=\"color: red;text-align: center;font-size: 60px;\">充值失败</h1></div>";
    }

    @GetMapping("/qrcode")
    public void getQrCode(@RequestHeader("system_api_Authorize_name") String user,
                          HttpServletResponse response) {
        String name = Encrypt.encrypt_uuid_6();
        try {
            String key = Encrypt.encrypt_uuid_12();
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            response.setCharacterEncoding("utf-8");
            String filename = URLEncoder.encode(name, "UTF-8").replaceAll(" ", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + filename + ".jpg");
            //todo  这里上线后 要写成线上地址
            QRCodeUtil.encode("http://localhost:88/api/system/file/qrcode/check?key=" + key + "&user=" + user, QRCodeUtil.DEFAULT_LOGO_URL, response, filename + ".jpg", true);
            //生成了二维码之后
            stringRedisTemplate.opsForValue().set(key, key + filename, 1, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 图片压缩
     */
    @Transactional
    @PostMapping(value = "/compress")
    public void fileCompress(@RequestHeader("system_api_Authorize_name") String user,
                             @NotNull @RequestBody MultipartFile file,
                             HttpServletResponse response) {
        boolean deduct = deduct(user, (double) file.getSize() / 1024);
        if (deduct) {
            try {
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                response.setCharacterEncoding("utf-8");
                String filename = URLEncoder.encode("加工图片", "UTF-8").replaceAll(" ", "%20");
                response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + filename + ".jpg");
                byte[] bytes = file.getBytes();
                log.info("force{}", bytes.length);
                Thumbnails.of(new ByteArrayInputStream(bytes))
                        .scale(1f) //图片大小（长宽）压缩比例 从0-1，1表示原图
                        .outputQuality(0.7f) //图片质量压缩比例 从0-1，越接近1质量越好
                        .toOutputStream(response.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 图片加水印
     * @param user 操作用户
     * @param file 文件对象
     * @param response 响应体
     * @param location 水印位置
     * @param text 水印文本
     * @param r 三原色之一
     * @param g 三原色之一
     * @param b 三原色之一
     * @param a 透明度 越小越透明
     */
    @Transactional
    @PostMapping(value = "/watermark")
    public void fileWatermark(@RequestHeader("system_api_Authorize_name") String user,
                              @NotNull MultipartFile file,
                              HttpServletResponse response,
                              @NotNull @RequestParam("location") String location,
                              @NotNull @RequestParam("text") String text,
                              @NotNull @RequestParam("r") Integer r,
                              @NotNull @RequestParam("g") Integer g,
                              @NotNull @RequestParam("b") Integer b,
                              @NotNull @RequestParam("a") Double a) {
        boolean deduct = deduct(user, (double) file.getSize() / 1024);
        if (deduct) {
            //进行判断
            if (location.equals("")) location = Location.MID;
            if (text.equals("")) text = "ZSJ_BLOG";
            try {
                Color self = new Color(r, g, b, (int) (a * 255));
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                response.setCharacterEncoding("utf-8");
                String filename = URLEncoder.encode("水印图片", "UTF-8").replaceAll(" ", "%20");
                response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + filename + ".jpg");
                WaterMarkUtil.waterMark(file.getInputStream(), self, text, location, response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * 在操作用户的余额的时候 进行了分布式的事务控制 <br></br>
     * 在需要操作用户的余额的时候 进行业务锁的操作 <br></br>
     * 每个锁是根据用户的当前账号名称+deduct来命令的<br></br>
     *
     * @param name 用户名称
     * @param z    金额
     * @return 操作成功或者失败
     */
    public boolean deduct(String name, double z) {
        UserEntity one = userService.getOne(new QueryWrapper<UserEntity>().eq("username", name));
        if (ObjectUtil.objectIsNotNull(one)) {
            //拿一下当前用户的余额 如果不足以支付就直接返回false就行
            if (one.getBalance() >= z) {
                RLock d_lock = redissonClient.getLock(name + DEDUCT_LOCK_NAME);
                boolean update;
                //这里加锁  这里应该锁的是当前用户的账户锁 其它锁不要动  业务执行应该是在10秒之内
                d_lock.lock();//阻塞锁
                try {
                    update = userService.update(new UpdateWrapper<UserEntity>().eq("username", name).set("balance", one.getBalance() - z));
                    if (update) {
                        EmailVoProperties emailVoProperties = new EmailVoProperties(one.getEmail(), EmailVoProperties.CONSUMPTION, z, one.getUsername(), BaseUtil.currentTime());
                        rabbitTemplate.convertAndSend(GlobalValueToExchange.EMAIL_EXCHANGE, GlobalValueToExchange.EMAIL_QUEUE, emailVoProperties, new CorrelationData(UUID.randomUUID().toString()));
                    }
                } finally {
                    //进行解锁操作
                    d_lock.unlock();
                }
                return update;
            } else {
                //发送邮件提醒余额不足
                EmailVoProperties emailVoProperties =
                        new EmailVoProperties(EmailVoProperties.INSUFFICIENT_BALANCE, one.getEmail());
                rabbitTemplate.convertAndSend(GlobalValueToExchange.EMAIL_EXCHANGE, GlobalValueToExchange.EMAIL_QUEUE, emailVoProperties, new CorrelationData(UUID.randomUUID().toString()));
            }
        }
        return false;
    }

    /**
     * 传参鉴定 看所有查询条件是否为一个空串
     * 或整个实体为null
     *
     * @param entity 条件实体
     * @return 是 否
     */
    private static boolean fileEntityParamIsEmptyQ(FileEntity entity) {
        return !ObjectUtil.objectIsNotNull(entity)
                || (ObjectUtil.isNullOrEmpty(entity.getFileName())
                && ObjectUtil.isNullOrEmpty(entity.getType())
                && ObjectUtil.isNullOrEmpty(entity.getAffiliation())
                && ObjectUtil.isNullOrEmpty(entity.getFileSuffix()));
    }

}
