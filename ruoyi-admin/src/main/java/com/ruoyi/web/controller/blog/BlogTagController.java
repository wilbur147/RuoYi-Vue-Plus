package com.ruoyi.web.controller.blog;

import com.ruoyi.blog.domain.BlogTag;
import com.ruoyi.blog.service.IBlogTagService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 博客标签Controller
 * 
 * @author Weiye
 * @date 2021-04-13
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/blog/tag" )
public class BlogTagController extends BaseController {

    private final IBlogTagService iBlogTagService;

    /**
     * 查询博客标签列表
     */
    @PreAuthorize("@ss.hasPermi('blog:tag:list')")
    @GetMapping("/list")
    public TableDataInfo list(BlogTag blogTag) {
        startPage();
        List<BlogTag> list = iBlogTagService.queryList(blogTag);
        return getDataTable(list);
    }

    /**
     * 导出博客标签列表
     */
    @PreAuthorize("@ss.hasPermi('blog:tag:export')" )
    @Log(title = "博客标签" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(BlogTag blogTag) {
        List<BlogTag> list = iBlogTagService.queryList(blogTag);
        ExcelUtil<BlogTag> util = new ExcelUtil<BlogTag>(BlogTag.class);
        return util.exportExcel(list, "tag" );
    }

    /**
     * 获取博客标签详细信息
     */
    @PreAuthorize("@ss.hasPermi('blog:tag:query')" )
    @GetMapping(value = "/{blogTagId}" )
    public AjaxResult getInfo(@PathVariable("blogTagId" ) Long blogTagId) {
        return AjaxResult.success(iBlogTagService.getById(blogTagId));
    }

    /**
     * 新增博客标签
     */
    @PreAuthorize("@ss.hasPermi('blog:tag:add')" )
    @Log(title = "博客标签" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BlogTag blogTag) {
        return toAjax(iBlogTagService.save(blogTag) ? 1 : 0);
    }

    /**
     * 修改博客标签
     */
    @PreAuthorize("@ss.hasPermi('blog:tag:edit')" )
    @Log(title = "博客标签" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BlogTag blogTag) {
        return toAjax(iBlogTagService.updateById(blogTag) ? 1 : 0);
    }

    /**
     * 删除博客标签
     */
    @PreAuthorize("@ss.hasPermi('blog:tag:remove')" )
    @Log(title = "博客标签" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{blogTagIds}" )
    public AjaxResult remove(@PathVariable Long[] blogTagIds) {
        return toAjax(iBlogTagService.removeByIds(Arrays.asList(blogTagIds)) ? 1 : 0);
    }


    /**
     * 置顶标签
     */
    @PreAuthorize("@ss.hasPermi('blog:tag:edit')" )
    @Log(title = "博客标签" , businessType = BusinessType.UPDATE)
    @PutMapping("/clickTop" )
    public AjaxResult clickTop(@RequestBody BlogTag blogTag) {
        return toAjax(iBlogTagService.clickTop(blogTag) ? 1 : 0);
    }
}
