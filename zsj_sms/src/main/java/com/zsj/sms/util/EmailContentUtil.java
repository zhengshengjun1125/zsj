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


    public static String createLoginUserEmailContent(String code) {
        return "<div class=\"content\" style=\" text-align: center; margin: 0;padding: 0;\">\n" +
                "        <img src=\"https://image-zsj.oss-cn-beijing.aliyuncs.com/logo.svg\" alt=\"欢迎呀~\">\n" +
                "        <h1 class=\"title\"><span style=\"color: blue; font-size: 25px;font: bold;\">ZSJ</span>博客管理系统</h1>\n" +
                "        <h2>您的邮件验证码是<span style=\"color: red;\">" + code + "</span> </h2>\n" +
                "    </div>";
    }

    public static String createConsumptionEmailContent(String name, Double price, String time) {
        return " <div style=\" text-align: center; margin: 0;padding: 0;\\\">\n" +
                "        <img src=\"https://image-zsj.oss-cn-beijing.aliyuncs.com/logo.svg\" alt=\"欢迎呀~\">\n" +
                "        <h1 class=\"title\\\"><span style=\"color: blue; font-size: 25px;font: bold;\">ZSJ</span>博客管理系统</h1>\n" +
                "        <h2> <span style=\"color: red;\">" + name + "</span> 用户,您在 <span style=\"color: rgb(15, 187, 187);\">" + time + "</span> 的时候消费了 <span style=\"color: khaki;\">" + price + "</span> Z币!</h2>\n" +
                "    </div>";
    }

    public static String createRechargeEmailContent(String name, Double price, String time) {
        return " <div style=\" text-align: center; margin: 0;padding: 0;\\\">\n" +
                "        <img src=\"https://image-zsj.oss-cn-beijing.aliyuncs.com/logo.svg\" alt=\"欢迎呀~\">\n" +
                "        <h1 class=\"title\\\"><span style=\"color: blue; font-size: 25px;font: bold;\">ZSJ</span>博客管理系统</h1>\n" +
                "        <h2> <span style=\"color: red;\">" + name + "</span> 用户,您在 <span style=\"color: rgb(15, 187, 187);\">" + time
                + "</span> 的时候 <span style=\"color: red;\">成功充值</span> 了 <span style=\"color: khaki;\">" + price + "</span>的Z币\n" +
                "        </h2>\n" +
                "        <h2> 祝您使用愉快~ </h2>\n" +
                "        <img src=\"https://request-oss-zsj.oss-cn-beijing.aliyuncs.com/zsj/2023-11-30/image/d440dc192735.jpg\" alt=\"欢迎呀~\">\n" +
                "    </div>";
    }

    public static String createInsufficientBalanceEmailContent(){
        return "  <div style=\" text-align: center; margin: 0;padding: 0;\\\">\n" +
                "        <img src=\"https://image-zsj.oss-cn-beijing.aliyuncs.com/logo.svg\" alt=\"欢迎呀~\">\n" +
                "        <h1 class=\"title\\\"><span style=\"color: blue; font-size: 25px;font: bold;\">ZSJ</span>博客管理系统</h1>\n" +
                "        <h1 style=\"color: red;text-align: center;font-size: 60px;\">您的余额不足,请及时充值</h1>\n" +
                "    </div>";
    }

}
