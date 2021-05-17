package com.ruoyi.content.service;

import com.ruoyi.common.core.page.IServicePlus;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.content.bo.ContentAddBo;
import com.ruoyi.content.bo.ContentEditBo;
import com.ruoyi.content.bo.ContentQueryBo;
import com.ruoyi.content.domain.Content;
import com.ruoyi.content.vo.ContentVo;

import java.util.Collection;
import java.util.List;

/**
 * 内容Service接口
 *
 * @author ruoyi
 * @date 2021-05-12
 */
public interface IContentService extends IServicePlus<Content> {
	/**
	 * 查询单个
	 * @return
	 */
	ContentVo queryById(Long contentId);

	/**
	 * 分页查询列表
	 */
	TableDataInfo<ContentVo> queryPageList(ContentQueryBo bo);

	/**
	 * 分页查询列表
	 */
	List<ContentVo> queryList(ContentQueryBo bo);

	/**
	 * 根据新增业务对象插入内容
	 * @param bo 内容新增业务对象
	 * @return
	 */
	Boolean insertByAddBo(ContentAddBo bo);

	/**
	 * 根据编辑业务对象修改内容
	 * @param bo 内容编辑业务对象
	 * @return
	 */
	Boolean updateByEditBo(ContentEditBo bo);

	/**
	 * 根据编辑业务对象修改内容状体
	 * @param bo 内容编辑业务对象
	 * @return
	 */
	Boolean updateStatusByEditBo(ContentEditBo bo);

	/**
	 * 校验并删除数据
	 * @param ids 主键集合
	 * @param isValid 是否校验,true-删除前校验,false-不校验
	 * @return
	 */
	Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
