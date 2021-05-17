package com.ruoyi.content.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * 分类视图对象 mall_package
 *
 * @author ruoyi
 * @date 2021-05-12
 */
@Data
@ApiModel("分类视图对象")
// 解决spring序列化mybatis的懒加载异常
@JsonIgnoreProperties(value = {"handler"})
public class ConCategroyVo {
	private static final long serialVersionUID = 1L;

	/** 内容分类ID */
	@ApiModelProperty("内容分类ID")
	private Long categroyId;

	/** 父菜单ID */
	@Excel(name = "父菜单ID")
	@ApiModelProperty("父菜单ID")
	private Long parentId;
	/** 类别名称 */
	@Excel(name = "类别名称")
	@ApiModelProperty("类别名称")
	private String categroyName;
	/** 类别图标 */
	@Excel(name = "类别图标")
	@ApiModelProperty("类别图标")
	private String icon;
	private String filePath;
	/** 显示顺序 */
	@Excel(name = "显示顺序")
	@ApiModelProperty("显示顺序")
	private Integer orderNum;
	/** 创建时间 */
	@Excel(name = "创建时间" , width = 30, dateFormat = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty("创建时间")
	private Date createTime;
	/** 子级节点 */
	@Excel(name = "子级节点")
	@ApiModelProperty("子级节点")
	private List<ConCategroyVo> children;

}
