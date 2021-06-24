package com.ruoyi.file.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringUtils;
import com.ruoyi.common.constant.SystemConfigConstants;
import com.ruoyi.common.enums.SystemConfigKeyEnum;
import com.ruoyi.common.exception.file.FileException;
import com.ruoyi.file.entity.FileUploader;
import com.ruoyi.file.mapper.FileUploaderMapper;
import com.ruoyi.file.service.IQiNiuService;
import com.ruoyi.file.utils.FileUploadUtil;
import enums.FileUploadTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
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
public class QiNiuServiceImpl implements IQiNiuService {

    private String accessKey;
    private String secretKey;
    private String bucket;
    private String domainPrefix;

    @Autowired
    private FileUploaderMapper fileUploaderMapper;

    @Override
    public void upload(InputStream file, FileUploader fileUploader, Map<String, String> configMap) {
        this.init(configMap);
        File dest = null;
        try {
            // 设置临时文件路径
            String uploadType = fileUploader.getUploadType();
            String rootPath = StrUtil.isEmpty(uploadType) ? FileUploadTypeEnum.COMMON.toString() + "/" :
                    uploadType.endsWith("/") ? uploadType : uploadType + "/";
            rootPath  = rootPath + FileUploadUtil.createNewFileName(fileUploader.getOriginalFileName());
            Date startTime = new Date();
            //Zone.zone0:华东
            //Zone.zone1:华北
            //Zone.zone2:华南
            //Zone.zoneNa0:北美
            //Zone.zoneAs0:东南亚
            Configuration cfg = new Configuration(Region.autoRegion());
            UploadManager uploadManager = new UploadManager(cfg);
            Auth auth = Auth.create(this.accessKey, this.secretKey);
            String upToken = auth.uploadToken(this.bucket);
            Response response = uploadManager.put(file, rootPath, upToken, null, null);

            //解析上传成功的结果
            DefaultPutRet putRet = JSONUtil.toBean(response.bodyString(), DefaultPutRet.class);
            fileUploader.setStorageType(SystemConfigConstants.SYS_FILE_PRIORITY_QINIU);
            fileUploader.setUploadStartTime(startTime);
            fileUploader.setUploadEndTime(new Date());
            fileUploader.setFilePath(putRet.key);
            fileUploader.setFileHash(putRet.hash);
            fileUploader.setFullFilePath((this.domainPrefix.endsWith("/") ?
                    this.domainPrefix : this.domainPrefix + "/") + putRet.key);
            // 上传文件信息到数据库
            fileUploaderMapper.insert(fileUploader);
        } catch (QiniuException ex) {
            throw new RuntimeException("七牛云文件上传失败");
        }

    }

    @Override
    public boolean remove(FileUploader uploader, Map<String, String> configMap) {
        this.init(configMap);
        try {
            Auth auth = Auth.create(this.accessKey, this.secretKey);
            Configuration config = new Configuration(Region.autoRegion());
            BucketManager bucketManager = new BucketManager(auth, config);
            Response re = bucketManager.delete(this.bucket, uploader.getFilePath());
            return re.isOK();
        }catch (QiniuException e){
            Response r = e.response;
            throw new FileException("七牛删除文件发生异常：" + r.toString(), null);
        }
    }

    @Override
    public void downloadFile(List<FileUploader> uploaders, HttpServletResponse response){
        String localFilePath = null;
        try {
            localFilePath = System.getProperty("user.dir");
            if (StrUtil.isEmpty(localFilePath)){
                log.error("method: downloadFile line: 【116行】 七牛云下载>>>未查询到存储路径");
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
            log.error("method: downloadFile line: 【134行】 七牛云下载文件失败  发生的异常是",e);
        }finally {
            if (StrUtil.isNotEmpty(localFilePath)){
                // 删除临时下载文件夹及文件
                FileUtil.del(localFilePath+"/temp");
            }
        }
    }


    private void init(Map<String, String> configMap) {
        // 获取七牛云的系统配置并校验值
        if (Objects.isNull(configMap)){
            throw new FileException("配置信息为空，文件上传功能暂时不可用！",null);
        }
        this.domainPrefix = configMap.get(SystemConfigKeyEnum.QINIU_DOMAIN_PREFIX.getKey());
        this.accessKey = configMap.get(SystemConfigKeyEnum.QINIU_ACCESS_KEY.getKey());
        this.secretKey = configMap.get(SystemConfigKeyEnum.QINIU_SECRET_KEY.getKey());
        this.bucket = configMap.get(SystemConfigKeyEnum.QINIU_BUCKET_PATH.getKey());
        if (StringUtils.isNullOrEmpty(this.domainPrefix) ||
                StringUtils.isNullOrEmpty(this.accessKey) ||
                StringUtils.isNullOrEmpty(this.secretKey)||
                StringUtils.isNullOrEmpty(this.bucket)) {
            throw new FileException("尚未配置七牛云，文件上传功能暂时不可用！",null);
        }
    }
}
