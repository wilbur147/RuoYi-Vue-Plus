package com.ruoyi.content.service;

import com.ruoyi.common.core.page.IServicePlus;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.content.bo.ConTopicAddBo;
import com.ruoyi.content.bo.ConTopicEditBo;
import com.ruoyi.content.bo.ConTopicQueryBo;
import com.ruoyi.content.domain.ConTopic;
import com.ruoyi.content.vo.ConTopicVo;

import java.util.Collection;
import java.util.List;

/**
 * 专题Service接口
 *
 * @author ruoyi
 * @date 2021-05-12
 */
public interface IConTopicService extends IServicePlus<ConTopic> {
	/**
	 * 查询单个
	 * @return
	 */
	ConTopicVo queryById(Long conTopicId);

	/**
	 * 分页查询列表
	 */
	TableDataInfo<ConTopicVo> queryPageList(ConTopicQueryBo bo);


	/**
	 * 查询列表
	 */
	List<ConTopicVo> queryList(ConTopicQueryBo bo);

	/**
	 * 根据新增业务对象插入专题
	 * @param bo 专题新增业务对象
	 * @return
	 */
	Boolean insertByAddBo(ConTopicAddBo bo);

	/**
	 * 根据编辑业务对象修改专题
	 * @param bo 专题编辑业务对象
	 * @return
	 */
	Boolean updateByEditBo(ConTopicEditBo bo);

	/**
	 * 校验并删除数据
	 * @param ids 主键集合
	 * @param isValid 是否校验,true-删除前校验,false-不校验
	 * @return
	 */
	Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
