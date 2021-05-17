package com.ruoyi.file.service;

import com.ruoyi.file.entity.FileUploader;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * 本地文件服务类
 *
 * @author Ruoyi
 * @since 2021-03-17
 */
public interface ILocalFileService {

    /**
     /**
     * 上传文件
     *
     * @param file       待上传的文件流
     * @param fileUploader 文件实例类
     */
    void upload(InputStream file, FileUploader fileUploader);

    /**
     * 删除文件
     *
     * @param uploader   文件信息实例
     */
    boolean remove(FileUploader uploader);

    /**
     * 下载本地文件
     * @param uploaders
     * @param response
     */
    void downloadFile(List<FileUploader> uploaders, HttpServletResponse response);
}
