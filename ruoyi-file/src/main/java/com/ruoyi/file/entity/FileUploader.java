package com.ruoyi.file.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 基础文件信息
 * </p>
 *
 * @author Weiye
 * @since 2021-03-17
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class FileUploader extends Model<FileUploader> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "file_id", type = IdType.AUTO)
    private Integer fileId;

    private String uniqueId;

    private String storageType;

    private String uploadType;

    private String originalFileName;

    private Long fileSize;

    private String suffix;

    private String filePath;

    private String fullFilePath;

    private String fileHash;

    private Date uploadStartTime;

    private Date uploadEndTime;

    private Date createTime;

    private Date updateTime;

    @Override
	public Serializable pkVal() {
        return this.fileId;
    }

}
