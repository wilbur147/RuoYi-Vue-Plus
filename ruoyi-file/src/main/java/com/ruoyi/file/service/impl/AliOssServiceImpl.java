package com.ruoyi.file.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.HttpUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import com.qiniu.util.StringUtils;
import com.ruoyi.common.constant.SystemConfigConstants;
import com.ruoyi.common.enums.SystemConfigKeyEnum;
import com.ruoyi.common.exception.file.FileException;
import com.ruoyi.file.entity.FileUploader;
import com.ruoyi.file.mapper.FileUploaderMapper;
import com.ruoyi.file.service.IAliOssService;
import com.ruoyi.file.utils.FileUploadUtil;
import enums.FileUploadTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
public class AliOssServiceImpl implements IAliOssService {

    private OSSClient client;
    private String accessKey;
    private String accessKeySecret;
    private String bucketName;
    private String endpoint;
    private String fileUrl;

    @Autowired
    private FileUploaderMapper fileUploaderMapper;

    @Override
    public void upload(InputStream file, FileUploader fileUploader, Map<String, String> configMap) {
        this.init(configMap);
        try {
            // 设置临时文件路径
            String uploadType = fileUploader.getUploadType();
            String rootPath = StrUtil.isEmpty(uploadType) ? FileUploadTypeEnum.COMMON.toString() + "/" :
                    uploadType.endsWith("/") ? uploadType : uploadType + "/";
            rootPath  = rootPath + FileUploadUtil.createNewFileName(fileUploader.getOriginalFileName());
            Date startTime = new Date();

            // 上传
            if (!this.client.doesBucketExist(this.bucketName)) {
                throw new RuntimeException("[阿里云OSS] 无法上传文件！Bucket不存在：" + this.bucketName);
            }
            // 上传文件。
            PutObjectResult result = this.client.putObject(bucketName, rootPath, file);
            //设置返回的url有效期为10年
            Date expiration = new Date(System.currentTimeMillis() + 10 * 365 * 24 * 60 * 60 * 1000);
            URL url = this.client.generatePresignedUrl(bucketName, rootPath, expiration);
            //解析上传成功的结果
            fileUploader.setStorageType(SystemConfigConstants.SYS_FILE_PRIORITY_OSS);
            fileUploader.setUploadStartTime(startTime);
            fileUploader.setUploadEndTime(new Date());
            fileUploader.setFilePath(rootPath);
            fileUploader.setFileHash(result.getETag());
            fileUploader.setFullFilePath(url.toString());
            // 上传文件信息到数据库
            fileUploaderMapper.insert(fileUploader);
        } catch (Exception ex) {
            throw new RuntimeException("七牛云文件上传失败");
        } finally {
            this.client.shutdown();
        }

    }

    @Override
    public void remove(FileUploader uploader, Map<String, String> configMap) {
        this.init(configMap);
        try {
            boolean exists = this.client.doesBucketExist(this.bucketName);
            if (!exists) {
                throw new FileException("[阿里云OSS] 文件删除失败！Bucket不存在：" + this.bucketName, null);
            }
            if (!this.client.doesObjectExist(bucketName, uploader.getFilePath())) {
                throw new FileException("[阿里云OSS] 文件删除失败！文件不存在：" + this.bucketName + "/" + uploader.getFilePath(), null);
            }
            this.client.deleteObject(bucketName, uploader.getFilePath());
        } catch (Exception e){
            throw new FileException("[阿里云OSS] 文件删除失败！", null);
        } finally {
            this.client.shutdown();
        }
    }

    @Override
    public void downloadFile(List<FileUploader> uploaders, HttpServletResponse response){
        String localFilePath = null;
        try {
            localFilePath = System.getProperty("user.dir");
            if (StrUtil.isEmpty(localFilePath)){
                log.error("method: downloadFile line: 【114行】阿里云OSS下载>>>未查询到存储路径");
                return;
            }
            String tempPath = localFilePath + "/temp/download/" + System.currentTimeMillis();
            // 将所有相关文件复制到临时下载目录中去
            String temlFileName;
            for (FileUploader uploader : uploaders) {
                temlFileName = tempPath + "/" + IdUtil.simpleUUID() + uploader.getSuffix();
                HttpUtil.downloadFile(uploader.getFullFilePath(), FileUtil.file(temlFileName));
            }
            // 将临时下载目录下的文件压缩成zip
            String zipPath = tempPath+".zip";
            ZipUtil.zip(tempPath);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUploadUtil.setAttachmentResponseHeader(response, FileUtil.getName(zipPath));
            FileUploadUtil.writeBytes(zipPath, response.getOutputStream());
        }catch (Exception e){
            log.error("method: downloadFile line: 【134行】 阿里云OSS下载文件失败  发生的异常是",e);
        }finally {
            if (StrUtil.isNotEmpty(localFilePath)){
                // 删除临时下载文件夹及文件
                FileUtil.del(localFilePath+"/temp");
            }
        }
    }


    private void init(Map<String, String> configMap) {
        // 获取阿里云OSS的系统配置并校验值
        if (Objects.isNull(configMap)){
            throw new FileException("配置信息为空，阿里云OSS文件上传功能暂时不可用！",null);
        }
        this.bucketName = configMap.get(SystemConfigKeyEnum.ALIYUN_BUCKET_NAME.getKey());
        this.endpoint = configMap.get(SystemConfigKeyEnum.ALIYUN_ENDPOINT.getKey());
        this.fileUrl = configMap.get(SystemConfigKeyEnum.ALIYUN_FILE_URL.getKey());
        this.accessKey = configMap.get(SystemConfigKeyEnum.ALIYUN_ACCESS_KEY.getKey());
        this.accessKeySecret = configMap.get(SystemConfigKeyEnum.ALIYUN_ACCESS_KEY_SECRET.getKey());
        if (StringUtils.isNullOrEmpty(this.bucketName) ||
                StringUtils.isNullOrEmpty(this.endpoint) ||
                StringUtils.isNullOrEmpty(this.fileUrl)||
                StringUtils.isNullOrEmpty(this.accessKey)||
                StringUtils.isNullOrEmpty(this.accessKeySecret)) {
            throw new FileException("尚未配置七牛云，文件上传功能暂时不可用！",null);
        }
        this.client = new OSSClient(endpoint, accessKey, accessKeySecret);
    }
}
