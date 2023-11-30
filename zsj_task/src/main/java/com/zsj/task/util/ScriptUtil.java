package com.zsj.task.util;


import org.springframework.context.annotation.Configuration;
import java.io.*;
import java.rmi.activation.UnknownObjectException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/24.
 */
@Configuration
public class ScriptUtil {


    //读取sh文件中的备份脚本
    public static List<String> getScript(Object path) {
        if (null == path) throw new NullPointerException("the path is empty");
        List<String> dos = new ArrayList<>();
        FileReader fileReader = null;
        BufferedReader reader = null;
        InputStreamReader inputStreamReader = null;
        try {
            if (path instanceof String) {
                fileReader = new FileReader((String) path);
                reader = new BufferedReader(fileReader);
            } else if (path instanceof InputStream) {
                inputStreamReader = new InputStreamReader((InputStream) path);
                reader = new BufferedReader(inputStreamReader);
            } else throw new UnknownObjectException("This is not a supported type");
            String line = "";
            while ((line = reader.readLine()) != null) {
                dos.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException("file must save in resources dist");
        }
        return dos;
    }
}
