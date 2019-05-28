package com.example.ff.a523;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by lenovo on 2018/4/22.
 */

public class MyFilter implements FilenameFilter {
    private String type;
    public MyFilter(String type){ //构造函数
        this.type = type;
    }
    @Override //实现FilenameFilter接口accept()方法
    public boolean accept(File dir, String name) { //dir当前目录, name文件名
        return name.endsWith(type); //返回true的文件则合格
    }
}