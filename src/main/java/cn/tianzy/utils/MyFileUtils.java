package cn.tianzy.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class MyFileUtils {
    public static String getFileExtensionName(String fileName){
        int index = fileName.lastIndexOf(".");
        if(index>-1){
            return fileName.substring(index).toLowerCase();
        }
        return "";
    }

    public static void mkParentDir(String path){
        File gifFile = new File(path);
        if(!gifFile.getParentFile().exists()){
            try {
                FileUtils.forceMkdir(gifFile.getParentFile());
            } catch (IOException e) {
                throw new RuntimeException("", e);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(getFileExtensionName("dongdong.png"));
    }
}
