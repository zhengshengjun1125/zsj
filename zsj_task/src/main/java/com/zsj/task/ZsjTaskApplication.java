package com.zsj.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 用来做定时任务的
 * 比如 TODO
 * 推送生日祝福邮件的功能
 * 动态保存数据库的定时任务
 * 定时使用cannal同步数据等等
 * .......
 */
@SpringBootApplication
@EnableScheduling
public class ZsjTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZsjTaskApplication.class, args);
    }

}
