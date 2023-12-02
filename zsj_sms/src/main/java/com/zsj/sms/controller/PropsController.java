package com.zsj.sms.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zsj.common.utils.Encrypt;
import com.zsj.common.utils.ObjectUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zsj.sms.entity.PropsEntity;
import com.zsj.sms.service.PropsService;
import com.zsj.common.utils.PageUtils;
import com.zsj.common.utils.R;


/**
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-12-02 14:52:48
 */
@RestController
@RequestMapping("sms/props")
public class PropsController {
    @Autowired
    private PropsService propsService;


    @GetMapping("/self")
    public R getSelfConfig(@NotNull @RequestHeader("system_api_Authorize_name") String register) {
        PropsEntity one = propsService.getOne(new QueryWrapper<PropsEntity>().eq("belong_user", register));
        if (ObjectUtil.objectIsNull(one)) return R.error("请您到设置中配置您的邮箱密钥");
        return R.ok().put("data", one);
    }

    /**
     * 保存
     */
    @PostMapping("/disposition")
    public R save(@NotNull @RequestHeader("system_api_Authorize_name") String register,
                  @NotNull @RequestBody PropsEntity props) {
        if (checkIsLegitimate(props)) {
            QueryWrapper<PropsEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("belong_user", register);
            PropsEntity one = propsService.getOne(queryWrapper);
            if (ObjectUtil.objectIsNull(one)) {//不存在
                props.setId(Encrypt.encrypt_uuid_32_low());
                props.setCreateTime(new Date(System.currentTimeMillis()));
                props.setUpdateTime(new Date(System.currentTimeMillis()));
                props.setVersion(1L);
                props.setBelongUser(register);
                boolean save = propsService.save(props);
                if (save) {
                    //TODO 推送消息邮件
                    return R.ok("应用成功");
                }
            } else {
                UpdateWrapper<PropsEntity> wrapper = new UpdateWrapper<>();
                wrapper.set("host", props.getHost());
                wrapper.set("username", props.getUsername());
                wrapper.set("SMTP", props.getSmtp());
                wrapper.set("version", one.getVersion() + 1);
                wrapper.set("update_time", new Date(System.currentTimeMillis()));
                wrapper.eq("belong_user", register);
                boolean update = propsService.update(wrapper);
                if (update) {
                    //TODO 推送消息邮件
//                String email = one.getUsername();
                    return R.ok("应用成功");
                }
            }
        }
        return R.error("应用失败,检查您的格式");
    }

    private boolean checkIsLegitimate(PropsEntity props) {
        //检测严谨格式 TODO
        return ObjectUtil.isNotNullOrEmpty(props.getHost()) &&
                ObjectUtil.isNotNullOrEmpty(props.getUsername()) &&
                ObjectUtil.isNotNullOrEmpty(props.getSmtp());
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = propsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id) {
        PropsEntity props = propsService.getById(id);

        return R.ok().put("props", props);
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PropsEntity props) {
        propsService.updateById(props);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody String[] ids) {
        propsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
