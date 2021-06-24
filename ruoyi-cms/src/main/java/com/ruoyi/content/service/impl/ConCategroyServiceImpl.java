package com.ruoyi.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.SetFilePath;
import com.ruoyi.common.core.mybatisplus.core.ServicePlusImpl;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.content.bo.ConCategroyAddBo;
import com.ruoyi.content.bo.ConCategroyEditBo;
import com.ruoyi.content.bo.ConCategroyQueryBo;
import com.ruoyi.content.domain.ConCategroy;
import com.ruoyi.content.mapper.ConCategroyMapper;
import com.ruoyi.content.service.IConCategroyService;
import com.ruoyi.content.vo.ConCategroyVo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 分类Service业务层处理
 *
 * @author ruoyi
 * @date 2021-05-12
 */
@Service
public class ConCategroyServiceImpl extends ServicePlusImpl<ConCategroyMapper, ConCategroy> implements IConCategroyService {

    @Override
    public ConCategroyVo queryById(Long conCategroyId){
        ConCategroy db = this.baseMapper.selectById(conCategroyId);
        return BeanUtil.toBean(db, ConCategroyVo.class);
    }

    @Override
    @SetFilePath(name = {"icon"})
    public TableDataInfo<ConCategroyVo> queryPageList(ConCategroyQueryBo bo) {
        LambdaQueryWrapper<ConCategroy> lqw = Wrappers.lambdaQuery();
        lqw.like(StrUtil.isNotBlank(bo.getCategroyName()), ConCategroy::getCategroyName, bo.getCategroyName());
        return PageUtils.buildDataInfo(this.pageVo(PageUtils.buildPagePlus(),lqw,ConCategroyVo.class));
    }

    @Override
    @SetFilePath(name = {"icon"})
    public List<ConCategroyVo> queryList(ConCategroyQueryBo bo) {
        LambdaQueryWrapper<ConCategroy> lqw = Wrappers.lambdaQuery();
        lqw.like(StrUtil.isNotBlank(bo.getCategroyName()), ConCategroy::getCategroyName, bo.getCategroyName());
        return this.listVo(lqw, ConCategroyVo.class);
    }

    @Override
    public List<ConCategroyVo> queryAllChildrenList(ConCategroyQueryBo bo) {
        return this.baseMapper.selectAll(bo.getCategroyName(),bo.getCategroyId());
    }

    @Override
    public void formatListChildrenNodeAsCategroyNode(List<ConCategroyVo> categroyVoList, List<Long> categroyIds) {
        Iterator<ConCategroyVo> iterator = categroyVoList.iterator();
        while (iterator.hasNext()){
            ConCategroyVo categroyVo = iterator.next();
            categroyIds.add(categroyVo.getCategroyId());
            List<ConCategroyVo> children = categroyVo.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                formatListChildrenNodeAsCategroyNode(children, categroyIds);
                categroyVo.setChildren(null);
            }
        }
    }

    @Override
    public Boolean insertByAddBo(ConCategroyAddBo bo) {
        ConCategroy add = BeanUtil.toBean(bo, ConCategroy.class);
        validEntityBeforeSave(add);
        return this.save(add);
    }

    @Override
    public Boolean updateByEditBo(ConCategroyEditBo bo) {
        ConCategroy update = BeanUtil.toBean(bo, ConCategroy.class);
        validEntityBeforeSave(update);
        return this.updateById(update);
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(ConCategroy entity){
        //TODO 做一些数据校验,如唯一约束
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return this.removeByIds(ids);
    }
}
