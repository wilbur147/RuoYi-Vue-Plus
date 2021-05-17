package com.ruoyi.file.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.file.entity.FileUploader;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 基础文件信息 Mapper 接口
 * </p>
 *
 * @author Ruoyi
 * @since 2021-03-17
 */
public interface FileUploaderMapper extends BaseMapper<FileUploader> {

    List<FileUploader> selectAllByUniqueId(@Param("uniqueId")String uniqueId);

    FileUploader selectAllByUniqueIdAndStorageType(@Param("uniqueId")String uniqueId,
                                                   @Param("storageType")String storageType);



    List<FileUploader> selectPropertyByUniqueIds(@Param("uniqueIds")List<String> uniqueIds,
                                                 @Param("priorityShow")String priorityShow);





}
