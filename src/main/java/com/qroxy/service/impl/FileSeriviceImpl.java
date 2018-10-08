package com.qroxy.service.impl;

import com.google.common.collect.Lists;
import com.qroxy.service.IFileSerivice;
import com.qroxy.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @desc：文件处理serviceImpl
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/7-2:29 PM
 */
@Service("iFileSerivice")
public class FileSeriviceImpl implements IFileSerivice {
    private Logger logger = LoggerFactory.getLogger(FileSeriviceImpl.class);

    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        //扩展名
        //abc.jpg,+1获取到.jpg往后移一位成jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("开始上传文件，上传文件名：{},上传路径：{},新文件名：{}", fileName, path, uploadFileName);
        File fileDir = new File(path);
//        如果不存在目录
        if (!fileDir.exists()) {
//            先赋予文件夹权限
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);
        try {
            file.transferTo(targetFile);
//            文件已经上传成功
            // 将targetFile上传到ftp文件服务器上
            FTPUtil.uploadFile(Lists.<File>newArrayList(targetFile));
            // 上传完后，删除upload下面的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("文件上传异常", e);
            return null;
        }
        return targetFile.getName();


    }


}
