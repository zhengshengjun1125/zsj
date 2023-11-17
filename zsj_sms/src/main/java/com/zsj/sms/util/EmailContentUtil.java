package com.zsj.sms.util;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/17.
 */
public class EmailContentUtil {

    //构造用户注册实体信息
    public static String createRegisterUserEmailContent(String recipient, String username, String roleName) {
        return "<div class=\"content\" style=\" text-align: center; margin: 0;padding: 0;\">\n" +
                "        <img src=\"https://image-zsj.oss-cn-beijing.aliyuncs.com/logo.svg\" alt=\"欢迎呀~\">\n" +
                "        <h1 class=\"title\"><span style=\"color: blue; font-size: 25px;font: bold;\">ZSJ</span>博客管理系统</h1>\n" +
                "        <h3>欢迎 <span style=\"color: red;\">" + username + ":" + roleName + "(" + recipient + ")" + "</span> 用户注册成功 祝您有个良好的体验</h3>\n" +
                "        <img src=\"https://image-zsj.oss-cn-beijing.aliyuncs.com/backgroud.jpg\" alt=\"close eye\">\n" +
                "        <h2>已经为你生成默认密码<span style=\"color: red;\">123456</span> 请进入系统后自己修改想要的密码</h2>\n" +
                "    </div>";
    }
}
