package org.ljdp.support.web.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : jianqingqiu
 * create at:  2020-04-20  15:57
 * @description: 简单判断文件类型
 */
public class FileTypeUtil {

    public final static Map<String, String> FILE_TYPE_ALL = new HashMap<String, String>();
    public final static Map<String, String> FILE_TYPE_IMAGE = new HashMap<String, String>();
    public final static Map<String, String> FILE_TYPE_VEDIO = new HashMap<String, String>();
    public final static Map<String, String> FILE_TYPE_AUDIO = new HashMap<String, String>();

    static{
        //初始化文件类型信息
        //常用图片格式
        FILE_TYPE_IMAGE.put("jpg","image/jpeg");
        FILE_TYPE_IMAGE.put("jpeg","image/jpeg");
        FILE_TYPE_IMAGE.put("bmp","image/bmp");
        FILE_TYPE_IMAGE.put("png","image/png");
        FILE_TYPE_IMAGE.put("gif","image/gif");

        //常用视频格式
        FILE_TYPE_VEDIO.put("mp4","video/mpeg4");
        FILE_TYPE_VEDIO.put("avi","video/avi");
        FILE_TYPE_VEDIO.put("mpeg","video/mpg");

        //常用音频格式
        FILE_TYPE_AUDIO.put("mp3","audio/mp3");


        FILE_TYPE_ALL.put("jpg","image/jpeg");
        FILE_TYPE_ALL.put("jpeg","image/jpeg");
        FILE_TYPE_ALL.put("bmp","image/bmp");
        FILE_TYPE_ALL.put("png","image/png");
        FILE_TYPE_ALL.put("mp4","video/mpeg4");
        FILE_TYPE_ALL.put("avi","video/avi");
        FILE_TYPE_ALL.put("mpeg","video/mpg");
        FILE_TYPE_ALL.put("mp3","audio/mp3");
        FILE_TYPE_ALL.put("pdf","application/pdf");
        FILE_TYPE_ALL.put("xls","application/vnd.ms-excel");
        FILE_TYPE_ALL.put("xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        FILE_TYPE_ALL.put("doc","application/vnd.ms-excel");
        FILE_TYPE_ALL.put("docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    }


}
