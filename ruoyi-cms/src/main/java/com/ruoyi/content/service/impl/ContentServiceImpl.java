package com.ruoyi.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.SetFilePath;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.content.bo.ConCategroyQueryBo;
import com.ruoyi.content.bo.ContentAddBo;
import com.ruoyi.content.bo.ContentEditBo;
import com.ruoyi.content.bo.ContentQueryBo;
import com.ruoyi.content.domain.Content;
import com.ruoyi.content.mapper.ContentMapper;
import com.ruoyi.content.service.IConCategroyService;
import com.ruoyi.content.service.IContentService;
import com.ruoyi.content.vo.ConCategroyVo;
import com.ruoyi.content.vo.ContentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 内容Service业务层处理
 *
 * @author ruoyi
 * @date 2021-05-12
 */
@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, Content> implements IContentService {

    @Autowired
    private IConCategroyService categroyService;

    @Override
    public ContentVo queryById(Long contentId){
        Content db = this.baseMapper.selectById(contentId);
        return BeanUtil.toBean(db, ContentVo.class);
    }

    @Override
    @SetFilePath(name = {"banner","icon"}, pathValue = {"bannerPath","iconPath"})
    public TableDataInfo<ContentVo> queryPageList(ContentQueryBo bo) {
        List<Long> categroyIds = new ArrayList<>();
        // 查询当前分类ID下所有子级分类ID
        if (Validator.isNotEmpty(bo.getCategroyId())){
            List<ConCategroyVo> categroyVos = categroyService.queryAllChildrenList(new ConCategroyQueryBo().setCategroyId(bo.getCategroyId()));
            categroyService.formatListChildrenNodeAsCategroyNode(categroyVos,categroyIds);
        }
        LambdaQueryWrapper<Content> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getConTopicId() != null, Content::getConTopicId, bo.getConTopicId());
        lqw.inSql(CollUtil.isNotEmpty(categroyIds), Content::getCategroyId, CollUtil.join(categroyIds,","));
        lqw.like(StrUtil.isNotBlank(bo.getContentName()), Content::getContentName, bo.getContentName());
        lqw.eq(Validator.isNotEmpty(bo.getStatus()), Content::getStatus, bo.getStatus());
        return PageUtils.buildDataInfo(this.pageVo(PageUtils.buildPagePlus(),lqw,ContentVo.class));
    }

    @Override
    @SetFilePath(name = {"banner","icon"}, pathValue = {"bannerPath","iconPath"})
    public List<ContentVo> queryList(ContentQueryBo bo) {
        LambdaQueryWrapper<Content> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getConTopicId() != null, Content::getConTopicId, bo.getConTopicId());
        lqw.like(StrUtil.isNotBlank(bo.getContentName()), Content::getContentName, bo.getContentName());
        lqw.eq(Validator.isNotEmpty(bo.getStatus()), Content::getStatus, bo.getStatus());
        return this.listVo(lqw, ContentVo.class);
    }

    @Override
    public Boolean insertByAddBo(ContentAddBo bo) {
        Content add = BeanUtil.toBean(bo, Content.class);
        validEntityBeforeSave(add);
        return this.save(add);
    }

    @Override
    public Boolean updateByEditBo(ContentEditBo bo) {
        Content update = BeanUtil.toBean(bo, Content.class);
        validEntityBeforeSave(update);
        return this.updateById(update);
    }

    @Override
    public Boolean updateStatusByEditBo(ContentEditBo bo) {
        Content update = BeanUtil.toBean(bo, Content.class);
        validEntityBeforeSave(update);
        return this.update(new LambdaUpdateWrapper<Content>()
                .set(Content::getStatus,update.getStatus())
        .eq(Content::getContentId,update.getContentId()));
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(Content entity){
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
