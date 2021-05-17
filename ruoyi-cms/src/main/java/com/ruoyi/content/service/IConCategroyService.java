package com.ruoyi.content.service;

import com.ruoyi.content.domain.ConCategroy;
import com.ruoyi.content.vo.ConCategroyVo;
import com.ruoyi.content.bo.ConCategroyQueryBo;
import com.ruoyi.content.bo.ConCategroyAddBo;
import com.ruoyi.content.bo.ConCategroyEditBo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * 分类Service接口
 *
 * @author ruoyi
 * @date 2021-05-12
 */
public interface IConCategroyService extends IService<ConCategroy> {
	/**
	 * 查询单个
	 * @return
	 */
	ConCategroyVo queryById(Long conCategroyId);

	/**
	 * 查询列表
	 */
	List<ConCategroyVo> queryList(ConCategroyQueryBo bo);

	/**
	 * 查询列表及所有子级
	 */
	List<ConCategroyVo> queryAllChildrenList(ConCategroyQueryBo bo);

	/**
	 * 重新组合分类层级ID，把所有子级放到一个平级list中
	 */
	void formatListChildrenNodeAsCategroyNode(List<ConCategroyVo> categroyVoList, List<Long> categroyIds);

	/**
	 * 根据新增业务对象插入分类
	 * @param bo 分类新增业务对象
	 * @return
	 */
	Boolean insertByAddBo(ConCategroyAddBo bo);

	/**
	 * 根据编辑业务对象修改分类
	 * @param bo 分类编辑业务对象
	 * @return
	 */
	Boolean updateByEditBo(ConCategroyEditBo bo);

	/**
	 * 校验并删除数据
	 * @param ids 主键集合
	 * @param isValid 是否校验,true-删除前校验,false-不校验
	 * @return
	 */
	Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
