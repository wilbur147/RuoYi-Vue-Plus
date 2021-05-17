package com.ruoyi.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.ruoyi.common.annotation.SetFilePath;
import com.ruoyi.content.bo.ConTopicAddBo;
import com.ruoyi.content.bo.ConTopicEditBo;
import com.ruoyi.content.bo.ConTopicQueryBo;
import com.ruoyi.content.domain.ConTopic;
import com.ruoyi.content.mapper.ConTopicMapper;
import com.ruoyi.content.service.IConTopicService;
import com.ruoyi.content.vo.ConTopicVo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 专题Service业务层处理
 *
 * @author ruoyi
 * @date 2021-05-12
 */
@Service
public class ConTopicServiceImpl extends ServiceImpl<ConTopicMapper, ConTopic> implements IConTopicService {

    @Override
    public ConTopicVo queryById(Long conTopicId){
        ConTopic db = this.baseMapper.selectById(conTopicId);
        return BeanUtil.toBean(db, ConTopicVo.class);
    }

    @Override
    @SetFilePath(name = {"icon"})
    public List<ConTopicVo> queryList(ConTopicQueryBo bo) {
        LambdaQueryWrapper<ConTopic> lqw = Wrappers.lambdaQuery();
        lqw.like(StrUtil.isNotBlank(bo.getTopicName()), ConTopic::getTopicName, bo.getTopicName());
        return entity2Vo(this.list(lqw));
    }

    /**
    * 实体类转化成视图对象
    *
    * @param collection 实体类集合
    * @return
    */
    private List<ConTopicVo> entity2Vo(Collection<ConTopic> collection) {
        List<ConTopicVo> voList = collection.stream()
                .map(any -> BeanUtil.toBean(any, ConTopicVo.class))
                .collect(Collectors.toList());
        if (collection instanceof Page) {
            Page<ConTopic> page = (Page<ConTopic>)collection;
            Page<ConTopicVo> pageVo = new Page<>();
            BeanUtil.copyProperties(page,pageVo);
            pageVo.addAll(voList);
            voList = pageVo;
        }
        return voList;
    }

    @Override
    public Boolean insertByAddBo(ConTopicAddBo bo) {
        ConTopic add = BeanUtil.toBean(bo, ConTopic.class);
        validEntityBeforeSave(add);
        return this.save(add);
    }

    @Override
    public Boolean updateByEditBo(ConTopicEditBo bo) {
        ConTopic update = BeanUtil.toBean(bo, ConTopic.class);
        validEntityBeforeSave(update);
        return this.updateById(update);
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(ConTopic entity){
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
