package com.qroxy.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @desc：ftp工具类
 * @author: Qroxy
 * @QQ：1114031075
 * @时间: 2018/10/7-2:53 PM
 */
public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");


    //构造方法


    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    //写开放出去的静态方法
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);
        logger.info("开始连接ftp服务器");

        boolean result = ftpUtil.uploadFile("img", fileList);

        logger.info("开始连接ftp服务器,结束上传,上传结果{}");
        return result;
    }

    /**
     * @desc:remotePath,ftp服务器在liunx中是一个文件夹，上传到远程服务器需要填写路径(文件上传）
     * @author:Qroxy
     * @date:2018/10/7 3:11 PM
     * @param:[remotePath, fileList]
     * @type:boolean
     */
    public boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fileInputStream = null;
        //连接ftp服务器
        if (connectServer(this.ip, this.port, this.user, this.pwd)) {

            try {
                //是否切换到指定文件夹
                ftpClient.changeWorkingDirectory(remotePath);
//                设置文件缓存区大小
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
//                把文件类型设置为二进制的文件类型，防止乱码
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //ftp被动模式
                ftpClient.enterLocalPassiveMode();
                //开始上传
                for (File fileItem : fileList) {
                    fileInputStream = new FileInputStream(fileItem);
//                    存储文件
                    ftpClient.storeFile(fileItem.getName(), fileInputStream);


                }
            } catch (IOException e) {
                logger.error("切换文件夹失败", e);
                uploaded = false;
            } finally {
                fileInputStream.close();
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }

    /**
     * @desc:封装连接ftp服务器
     * @author:Qroxy
     * @date:2018/10/7 3:08 PM
     * @param:[ip, port, user, pwd]
     * @type:boolean
     */
    private boolean connectServer(String ip, int port, String user, String pwd) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            logger.error("连接服务器异常", e);
            e.printStackTrace();
        }
        return isSuccess;
    }

    private String ip;
    private int port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    private String user;
    private String pwd;
    private FTPClient ftpClient;


}
