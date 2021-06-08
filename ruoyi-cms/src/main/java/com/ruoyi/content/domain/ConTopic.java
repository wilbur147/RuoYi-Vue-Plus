package com.ruoyi.content.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 专题对象 ry_con_topic
 *
 * @author ruoyi
 * @date 2021-05-12
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ry_con_topic")
public class ConTopic extends BaseEntity {

private static final long serialVersionUID=1L;


    /** 内容专题主键 */
    @TableId(value = "con_topic_id")
    private Long conTopicId;

    /** 专题名称 */
    private String topicName;

    /** 专题图标 */
    private String icon;

    /** 显示顺序 */
    private Integer orderNum;

    /** 描述 */
    private String description;

}
