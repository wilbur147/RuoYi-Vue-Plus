package com.ruoyi.file.utils;

import cn.hutool.core.io.FileUtil;
import com.ruoyi.common.exception.file.FileNameLengthLimitExceededException;
import com.ruoyi.common.exception.file.FileSizeLimitExceededException;
import com.ruoyi.common.exception.file.InvalidExtensionException;
import com.ruoyi.common.utils.file.FileTypeUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Ruoyi
 * @Description
 * @create 2021-03-18 14:55
 **/
public class FileValidUtil {

    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;


    /**
     * 文件大小长度校验
     *
     * @param file 上传的文件
     * @return
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws InvalidExtensionException
     */
    public static final void assertAllowed(MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, InvalidExtensionException, FileNameLengthLimitExceededException
    {

        // 验证文件名称长度
        String fileName = file.getOriginalFilename();
        if (fileName.length() > FileValidUtil.DEFAULT_FILE_NAME_LENGTH)
        {
            throw new FileNameLengthLimitExceededException(FileValidUtil.DEFAULT_FILE_NAME_LENGTH);
        }

        // 验证文件大小
        long size = file.getSize();
        if (FileValidUtil.DEFAULT_MAX_SIZE != -1 && size > FileValidUtil.DEFAULT_MAX_SIZE)
        {
            throw new FileSizeLimitExceededException(FileValidUtil.DEFAULT_MAX_SIZE / 1024 / 1024);
        }

        // 验证文件类型是否符合系统上传要求
        String fileSuffix = FileUtil.getSuffix(fileName);
        if (allowedExtension != null && !FileValidUtil.isAllowedExtension(fileSuffix, allowedExtension))
        {
            throw new InvalidExtensionException(allowedExtension, fileSuffix,fileName);
        }

    }



    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    public static final boolean isAllowedExtension(String extension, String[] allowedExtension)
    {
        for (String str : allowedExtension)
        {
            if (extension.indexOf(str) != -1)
            {
                return true;
            }
        }
        return false;
    }


    /**
     * 检查文件是否可下载
     *
     * @param resource 需要下载的文件
     * @return true 正常 false 非法
     */
    public static boolean checkAllowDownload(String resource)
    {
        // 禁止目录上跳级别
        if (StringUtils.contains(resource, ".."))
        {
            return false;
        }

        // 检查允许下载的文件规则
        if (ArrayUtils.contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, FileTypeUtils.getFileType(resource)))
        {
            return true;
        }

        // 不在允许下载的文件规则
        return false;
    }

}
