package com.zsj.Websocket.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zsj.Websocket.service.RelationMessageService;




/**
 * 
 *
 * @author zsj
 * @email zsjemail666@163.com
 * @date 2023-11-08 19:03:54
 */
@RestController
@RequestMapping("system/relationmessage")
public class RelationMessageController {
    @Autowired
    private RelationMessageService relationMessageService;




}
