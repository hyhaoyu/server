package com.haoyu.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ImgFileWorker {

    public static String saveImg(MultipartFile imgFile, String folderName,String id) throws IOException {

        //定义文件保存的本地路径
        String localPath = "D:\\myProjects\\graduation_project_images\\" + folderName +"\\" + id +"\\";
        if(!imgFile.isEmpty()){
            //定义 文件名
            String filename;
            //生成uuid作为文件名称
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            //获得文件类型（可以判断如果不是图片，禁止上传）
            String contentType = imgFile.getContentType();
            //获得文件后缀名
            String suffixName = contentType.substring(contentType.indexOf("/")+1);
            if(!suffixName.equals("jpeg") && !suffixName.equals("png") && !suffixName.equals("gif")){
                throw new RuntimeException("图片上传类型错误");
            }
            //得到 文件名
            filename = uuid+"." + suffixName;
            //判断文件夹是否存在
            File dir=new File(localPath);
            if(!dir.exists()){
                dir.mkdirs();
            }
            //文件保存路径
            imgFile.transferTo(new File(dir, filename));
            return folderName + "/" + id + "/" + filename;
        }
        else {
            return null;
        }

    }

}
