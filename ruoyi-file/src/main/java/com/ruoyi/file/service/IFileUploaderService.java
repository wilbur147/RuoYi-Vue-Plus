package com.ruoyi.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.file.entity.FileUploader;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 基础文件信息 服务类
 * </p>
 *
 * @author Ruoyi
 * @since 2021-03-17
 */
public interface IFileUploaderService extends IService<FileUploader> {

    /**
     * 单个文件上传
     *
     * @param multipartFile 待上传的文件
     * @param uploadType 文件上传类型，用来区分文件
     * @return
     */
    FileUploader uploadFile(MultipartFile multipartFile, String uploadType) throws Exception;

    /**
     * 下载文件
     *
     * @param uniqueIds 文件唯一码多个
     */
    void fileDownload(String uniqueIds, HttpServletResponse response);

    /**
     * 删除文件
     *
     * @param uniqueIds   文件唯一码集合
     */
    boolean removeFile(String uniqueIds) throws Exception;

    /**
     * 获取文件信息
     * @param uniqueId
     * @return
     */
    FileUploader getFileInfo(String uniqueId);

    /**
     * 获取文件信息--多个
     * @param uniqueIds
     * @return
     */
    Map<String,FileUploader> getFileInfoMap(String uniqueIds);
}
