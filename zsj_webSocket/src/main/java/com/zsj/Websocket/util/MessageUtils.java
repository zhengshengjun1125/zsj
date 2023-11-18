package com.zsj.Websocket.util;

import com.zsj.Websocket.entity.RelationMessageEntity;
import com.zsj.common.utils.GsonUtil;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/8.
 */
public class MessageUtils {
    public static String getMessage(String fromUser, Object message) {
        RelationMessageEntity relationMessageEntity = new RelationMessageEntity();
        relationMessageEntity.setFromuser(fromUser);
        relationMessageEntity.setMessage((String) message);
        return GsonUtil.gson.toJson(relationMessageEntity);
    }

}
