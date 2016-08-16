package com.weitaomi.application.service.impl;

import com.weitaomi.systemconfig.exception.InfoException;
import com.weitaomi.systemconfig.fileManage.UpYun;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/8/16.
 */
public class BaseService {
    public boolean uploadImage(String imageUrl,String imageFiles){
        BASE64Decoder base64Decoder=new BASE64Decoder();
        UpYun upYun=new UpYun("weitaomi","weitaomi","Weitaomi@Woyun");
        if (imageFiles==null){
            throw new InfoException("上传图片为空，请重新上传");
        }
        boolean flag=false;
        byte[] imageFile = new byte[0];
        try {
            imageFile=  base64Decoder.decodeBuffer(imageFiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
        flag= upYun.writeFile(imageUrl,imageFile);
        return flag;
    }
}
