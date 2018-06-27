package com.mmall.service;

import org.springframework.web.multipart.MultipartFile; /**
 * 描述:
 * IFileService
 *
 * @outhor Leo
 * @create 2018-06-24 下午 8:27
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}