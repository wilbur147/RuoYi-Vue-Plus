package com.ruoyi.file.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.file.service.IFileUploaderService;
import com.ruoyi.file.service.ILocalFileService;
import com.ruoyi.common.constant.SystemConfigConstants;
import com.ruoyi.common.enums.SystemConfigKeyEnum;
import com.ruoyi.file.entity.FileUploader;
import com.ruoyi.file.mapper.FileUploaderMapper;
import com.ruoyi.file.service.IAliOssService;
import com.ruoyi.file.service.IQiNiuService;
import com.ruoyi.file.utils.FileValidUtil;
import com.ruoyi.file.utils.MimeTypeUtils;
import com.ruoyi.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 基础文件信息 服务实现类
 * </p>
 *
 * @author Ruoyi
 * @since 2021-03-17
 */
@Service
public class FileUploaderServiceImpl extends ServiceImpl<FileUploaderMapper, FileUploader> implements IFileUploaderService {


    @Autowired
    private ILocalFileService localFileService;
    @Autowired
    private IQiNiuService qiNiuService;
    @Autowired
    private IAliOssService aliOssService;
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private FileUploaderMapper fileUploaderMapper;

    @Override
    public FileUploader uploadFile(MultipartFile file, String uploadType) throws Exception{
        // 验证文件
        FileValidUtil.assertAllowed(file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);

        // 获取配置
        Map<String, String> configMap = configService.getAll(SystemConfigKeyEnum.SYS_CONFIG_PREFIX.getKey());
        String isUpdateLocal = configMap.get(SystemConfigKeyEnum.IS_UPLOAD_LOCAL.getKey());
        String isUpLoadQiniu = configMap.get(SystemConfigKeyEnum.IS_UPLOAD_QINIU.getKey());
        String isUpLoadOss = configMap.get(SystemConfigKeyEnum.IS_UPLOAD_OSS.getKey());
        FileUploader fileUploader = new FileUploader()
                .setUniqueId(IdUtil.objectId())
                .setUploadType(uploadType)
                .setFileSize(file.getSize())
                .setOriginalFileName(file.getOriginalFilename())
                .setSuffix(FileUtil.getSuffix(file.getOriginalFilename()))
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        // 判断本地是否上传
        if (SystemConfigConstants.SYS_YES.equals(isUpdateLocal)){
            localFileService.upload(file.getInputStream(), fileUploader);
        }
        // 判断七牛是否上传
        if (SystemConfigConstants.SYS_YES.equals(isUpLoadQiniu)){
            qiNiuService.upload(file.getInputStream(), fileUploader, configMap);
        }
        // 判断阿里云OSS是否上传
        if (SystemConfigConstants.SYS_YES.equals(isUpLoadOss)){
            aliOssService.upload(file.getInputStream(), fileUploader, configMap);
        }
        return fileUploader;
    }

    @Override
    public void fileDownload(String uniqueIds, HttpServletResponse response) {
        try
        {
            // 获取文件优先展示
            String priorityShow = configService.selectConfigByKey(SystemConfigKeyEnum.getPrefixSysConfigKey(SystemConfigKeyEnum.FILE_PRIORITY_SHOW.getKey()));
            if (StrUtil.isEmpty(priorityShow)){
                log.error("method: fileDownload line: 【92行】：文件优先展示查询错误");
                return;
            }
            List<FileUploader> uploaders = fileUploaderMapper.selectPropertyByUniqueIds(Arrays.asList(uniqueIds.split(",")), priorityShow);
            if (CollectionUtil.isNotEmpty(uploaders)){
                switch (priorityShow){
                    case SystemConfigConstants.SYS_FILE_PRIORITY_LOCAL:
                        // 下载本地
                       localFileService.downloadFile(uploaders, response);
                        break;
                    case SystemConfigConstants.SYS_FILE_PRIORITY_QINIU:
                        // 下载七牛
                        qiNiuService.downloadFile(uploaders, response);
                        break;
                    case SystemConfigConstants.SYS_FILE_PRIORITY_OSS:
                        // 下载阿里OSS
                        aliOssService.downloadFile(uploaders, response);
                        break;
                    default:
                }
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }

    }

    @Override
    public boolean removeFile(String uniqueIds) throws Exception {
        // 查询当前uniqueId对应的文件信息
        List<FileUploader> uploaderList = fileUploaderMapper.selectPropertyByUniqueIds(Arrays.asList(uniqueIds.split(",")),null);
        if (CollectionUtil.isEmpty(uploaderList)){
            return false;
        }
        fileUploaderMapper.deleteBatchIds(uploaderList.stream().map(i->i.getFileId()).collect(Collectors.toList()));
        uploaderList.forEach(this::delete);
        return true;
    }

    @Override
    public FileUploader getFileInfo(String uniqueId) {
        // 查询系统配置，优先显示
        String priorityShow = configService.selectConfigByKey(SystemConfigKeyEnum.getPrefixSysConfigKey(SystemConfigKeyEnum.FILE_PRIORITY_SHOW.getKey()));
        if (StrUtil.isEmpty(priorityShow)){
            return null;
        }
        return fileUploaderMapper.selectAllByUniqueIdAndStorageType(uniqueId, priorityShow);
    }

    @Override
    public Map<String, FileUploader> getFileInfoMap(String uniqueIds) {
        // 查询系统配置，优先显示
        String priorityShow = configService.selectConfigByKey(SystemConfigKeyEnum.getPrefixSysConfigKey(SystemConfigKeyEnum.FILE_PRIORITY_SHOW.getKey()));
        List<FileUploader> uploaderList = fileUploaderMapper.selectPropertyByUniqueIds(Arrays.asList(uniqueIds.split(",")), priorityShow);
        if (CollectionUtil.isNotEmpty(uploaderList)) {
            Map<String, FileUploader> map = new HashMap<>(16);
            for (String uniqueId : uniqueIds.split(",")) {
                for (FileUploader uploader : uploaderList) {
                    if (uniqueId.equals(uploader.getUniqueId())) {
                        map.put(uniqueId,uploader);
                        break;
                    }
                }
            }
            return map;
        }
        return null;
    }


    private void delete(FileUploader uploader){
        // 判断本地是否有文件可删除
        if (SystemConfigConstants.SYS_FILE_PRIORITY_LOCAL.equals(uploader.getStorageType())){
            localFileService.remove(uploader);
        }
        // 判断七牛是否有文件可删除
        if (SystemConfigConstants.SYS_FILE_PRIORITY_QINIU.equals(uploader.getStorageType())){
            qiNiuService.remove(uploader,configService.getAll(SystemConfigKeyEnum.SYS_CONFIG_PREFIX.getKey()));
        }
        // 判断阿里云OSS是否有文件可删除
        if (SystemConfigConstants.SYS_FILE_PRIORITY_OSS.equals(uploader.getStorageType())){
            aliOssService.remove(uploader,configService.getAll(SystemConfigKeyEnum.SYS_CONFIG_PREFIX.getKey()));
        }
    }

}
