package com.ruoyi.file.controller;


import com.ruoyi.file.service.IFileUploaderService;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 基础文件信息 前端控制器
 * </p>
 *
 * @author Weiye
 * @since 2021-03-17
 */
@RestController
@RequestMapping("/fileUploader")
public class FileUploaderController {

    @Autowired
    private IFileUploaderService fileUploaderService;

    /**
     * 上传文件
     * @param file
     * @param uploadType 文件作用类型
     * @return
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file,
                                 @RequestParam(value = "uploadType",required = false) String uploadType)
    {
        try {
            return AjaxResult.success("上传文件成功",fileUploaderService.uploadFile(file, uploadType));
        }catch (Exception e){
            return AjaxResult.error("上传文件失败",e.getMessage());
        }
    }

    /**
     * 下载文件
     *
     * @param uniqueIds 文件唯一码多个
     */
    @GetMapping("/download")
    public void fileDownload(String uniqueIds, HttpServletResponse response)
    {
        fileUploaderService.fileDownload(uniqueIds,response);
    }

    /**
     * 删除文件
     * @param uniqueIds  文件唯一码集合
     * @return
     */
    @DeleteMapping("/{uniqueIds}")
    public AjaxResult removeFile(@PathVariable("uniqueIds") String uniqueIds){
        try {
            return fileUploaderService.removeFile(uniqueIds) ? AjaxResult.success("删除文件成功")
                    : AjaxResult.error("删除文件失败");
        }catch (Exception e){
            return AjaxResult.error("删除文件失败",e.getMessage());
        }
    }


    /**
     * 查询文件信息
     * @param uniqueId 文件唯一码
     * @return
     */
    @GetMapping("/getFileInfo/{uniqueId}")
    public AjaxResult getFileInfo(@PathVariable("uniqueId") String uniqueId)
    {
        return AjaxResult.success("获取文件信息成功",fileUploaderService.getFileInfo(uniqueId));
    }

}
