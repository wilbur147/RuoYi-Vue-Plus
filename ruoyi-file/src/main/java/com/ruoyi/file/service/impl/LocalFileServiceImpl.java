package com.ruoyi.file.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.ruoyi.file.service.ILocalFileService;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.SystemConfigConstants;
import com.ruoyi.common.exception.file.FileException;
import com.ruoyi.common.utils.ServerUtil;
import com.ruoyi.file.entity.FileUploader;
import com.ruoyi.file.mapper.FileUploaderMapper;
import com.ruoyi.file.utils.FileUploadUtil;
import enums.FileUploadTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 基础文件信息 服务实现类
 * </p>
 *
 * @author Ruoyi
 * @since 2021-03-17
 */
@Slf4j
@Service
public class LocalFileServiceImpl implements ILocalFileService {

    private String localFilePath;

    @Autowired
    private ServerUtil serverUtil;
    @Autowired
    private FileUploaderMapper fileUploaderMapper;

    @Override
    public void upload(InputStream is, FileUploader fileUploader) {
        this.init();
        Date startTime = new Date();
        String uploadType = fileUploader.getUploadType();
        uploadType = StrUtil.isEmpty(uploadType) ? FileUploadTypeEnum.COMMON.toString() + "/" :
                uploadType.endsWith("/") ? uploadType : uploadType + "/";
        String realPath = uploadType + FileUploadUtil.createNewFileName(fileUploader.getOriginalFileName());
        String rootPath = this.localFilePath + realPath;
        FileUploadUtil.checkFilePath(rootPath);
        try (InputStream uploadIs = FileUploadUtil.clone(is);
             InputStream fileHashIs = FileUploadUtil.clone(is);
             FileOutputStream fos = new FileOutputStream(rootPath)) {
            FileCopyUtils.copy(uploadIs, fos);
            fileUploader.setStorageType(SystemConfigConstants.SYS_FILE_PRIORITY_LOCAL);
            fileUploader.setUploadStartTime(startTime);
            fileUploader.setUploadEndTime(new Date());
            fileUploader.setFilePath(realPath);
            fileUploader.setFileHash(DigestUtils.md5DigestAsHex(fileHashIs));
            fileUploader.setFullFilePath(serverUtil.getUrl() + realPath);
            // 上传文件信息到数据库
            fileUploaderMapper.insert(fileUploader);
        } catch (Exception e) {
            log.error("method: upload line: 【60行】发生的异常是",e);
            throw new RuntimeException("本地文件上传失败");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean remove(FileUploader uploader) {
        try {
            File file = new File(RuoYiConfig.getProfile() + "/" + uploader.getFilePath());
            if (!file.exists()) {
                throw new FileException("本地删除文件失败：文件不存在[" + uploader.getFilePath() + "]",null);
            }
            return file.delete();
        }catch (Exception e){
            throw new FileException("本地删除文件失败：" + e.getMessage(),null);
        }
    }

    @Override
    public void downloadFile(List<FileUploader> uploaders, HttpServletResponse response){
        try
        {
            String localFilePath = RuoYiConfig.getProfile();
            if (StrUtil.isEmpty(localFilePath)){
                log.error("method: fileDownload line: 【101行】：未查询到本地存储路径 localFilePath");
                return;
            }
            String tempPath = localFilePath + "/temp/download/" + System.currentTimeMillis();
            String filePath,tempFile;
            // 将所有相关文件复制到临时下载目录中去
            for (FileUploader uploader : uploaders) {
                filePath = (localFilePath.endsWith("/") ? localFilePath : localFilePath + "/") + uploader.getFilePath();
                tempFile = tempPath + "/" + FileUtil.getName(filePath);
                FileUtil.copy(filePath,tempFile,true);
            }
            // 将临时下载目录下的文件压缩成zip
            String zipPath = tempPath+".zip";
            ZipUtil.zip(tempPath);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUploadUtil.setAttachmentResponseHeader(response, FileUtil.getName(zipPath));
            FileUploadUtil.writeBytes(zipPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("本地文件下载失败", e);
        }finally {
            // 删除临时下载文件夹及文件
            FileUtil.del(RuoYiConfig.getProfile()+"/temp");
        }

    }

    private void init() {
        // 获取本地系统配置并校验值
        if (Objects.isNull(RuoYiConfig.getProfile())){
            throw new FileException("尚未配置本地路径，文件上传功能暂时不可用！",null);
        }
        this.localFilePath = RuoYiConfig.getProfile().endsWith("/") ? RuoYiConfig.getProfile() : RuoYiConfig.getProfile() + "/";
    }
}
