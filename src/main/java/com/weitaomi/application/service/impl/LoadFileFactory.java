package com.weitaomi.application.service.impl;

import java.io.File;

/**
 * Created by Administrator on 2016/10/25.
 */
public class LoadFileFactory {
    public static File getFile(String url){
       return new File(url);
    }
}
