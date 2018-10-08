package com.qroxy.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @create by 林镇权
 * *
 * @QQ：1114031075 *
 * @时间: 2018/10/7-2:28 PM
 */
public interface IFileSerivice {
    public String upload(MultipartFile file, String path);
}
