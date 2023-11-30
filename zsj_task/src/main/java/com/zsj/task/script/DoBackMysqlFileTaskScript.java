package com.zsj.task.script;

import com.zsj.task.util.ScriptUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;


/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/24.
 * 备份mysql的数据库文件
 */
@Component
public class DoBackMysqlFileTaskScript {

    private static int count = 1;


    /**
     * 每三十分钟执行一次
     * 定时保存数据库的所有信息
     */
    //    @Scheduled(cron = "* 0/30 * * * ? *")
    @Scheduled(fixedDelayString = "${time.fixedDelay}")
    public void backMysql() throws Exception {
//        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("shell/backMysql4Docker.sh");
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("shell/test.sh");
        List<String> script = ScriptUtil.getScript(stream);

        ProcessBuilder pb = new ProcessBuilder();
        for (String s : script) {
            Runtime.getRuntime().exec(s);
        }
        System.out.println("success");
    }

}
