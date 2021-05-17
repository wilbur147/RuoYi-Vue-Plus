package com.ruoyi.web.controller.blog;

import com.ruoyi.blog.domain.BlogSort;
import com.ruoyi.blog.service.IBlogSortService;
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
 * 博客分类Controller
 * 
 * @author Weiye
 * @date 2021-04-13
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/blog/sort" )
public class BlogSortController extends BaseController {

    private final IBlogSortService iBlogSortService;

    /**
     * 查询博客分类列表
     */
    @PreAuthorize("@ss.hasPermi('blog:sort:list')")
    @GetMapping("/list")
    public TableDataInfo list(BlogSort blogSort) {
        return iBlogSortService.queryPageList(blogSort);
    }

    /**
     * 导出博客分类列表
     */
    @PreAuthorize("@ss.hasPermi('blog:sort:export')" )
    @Log(title = "博客分类" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(BlogSort blogSort) {
        List<BlogSort> list = iBlogSortService.queryList(blogSort);
        ExcelUtil<BlogSort> util = new ExcelUtil<BlogSort>(BlogSort.class);
        return util.exportExcel(list, "sort" );
    }

    /**
     * 获取博客分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('blog:sort:query')" )
    @GetMapping(value = "/{blogSortId}" )
    public AjaxResult getInfo(@PathVariable("blogSortId" ) Long blogSortId) {
        return AjaxResult.success(iBlogSortService.getById(blogSortId));
    }

    /**
     * 新增博客分类
     */
    @PreAuthorize("@ss.hasPermi('blog:sort:add')" )
    @Log(title = "博客分类" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BlogSort blogSort) {
        return toAjax(iBlogSortService.save(blogSort) ? 1 : 0);
    }

    /**
     * 修改博客分类
     */
    @PreAuthorize("@ss.hasPermi('blog:sort:edit')" )
    @Log(title = "博客分类" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BlogSort blogSort) {
        return toAjax(iBlogSortService.updateById(blogSort) ? 1 : 0);
    }

    /**
     * 删除博客分类
     */
    @PreAuthorize("@ss.hasPermi('blog:sort:remove')" )
    @Log(title = "博客分类" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{blogSortIds}" )
    public AjaxResult remove(@PathVariable Long[] blogSortIds) {
        return toAjax(iBlogSortService.removeByIds(Arrays.asList(blogSortIds)) ? 1 : 0);
    }


    /**
     * 置顶分类
     */
    @PreAuthorize("@ss.hasPermi('blog:sort:edit')" )
    @Log(title = "博客分类" , businessType = BusinessType.UPDATE)
    @PutMapping("/clickTop" )
    public AjaxResult clickTop(@RequestBody BlogSort blogSort) {
        return toAjax(iBlogSortService.clickTop(blogSort) ? 1 : 0);
    }
}
