package com.ruoyi.web.controller.blog;

import com.ruoyi.blog.domain.Blog;
import com.ruoyi.blog.service.IBlogService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.SetFilePath;
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
 * 博客系统Controller
 * 
 * @author Weiye
 * @date 2021-04-13
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/blog/blog" )
public class BlogController extends BaseController {

    private final IBlogService iBlogService;

    /**
     * 查询博客系统列表
     */
    @PreAuthorize("@ss.hasPermi('blog:blog:list')")
    @GetMapping("/list")
    public TableDataInfo<Blog> list(Blog blog) {
        return iBlogService.queryPageList(blog);
    }

    /**
     * 导出博客系统列表
     */
    @PreAuthorize("@ss.hasPermi('blog:blog:export')" )
    @Log(title = "博客系统" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(Blog blog) {
        List<Blog> list = iBlogService.queryList(blog);
        ExcelUtil<Blog> util = new ExcelUtil<Blog>(Blog.class);
        return util.exportExcel(list, "blog" );
    }

    /**
     * 获取博客系统详细信息
     */
    @PreAuthorize("@ss.hasPermi('blog:blog:query')" )
    @GetMapping(value = "/{blogId}" )
    @SetFilePath
    public AjaxResult getInfo(@PathVariable("blogId" ) Integer blogId) {
        return AjaxResult.success(iBlogService.getById(blogId));
    }

    /**
     * 新增博客系统
     */
    @PreAuthorize("@ss.hasPermi('blog:blog:add')" )
    @Log(title = "博客系统" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Blog blog) {
        return toAjax(iBlogService.save(blog) ? 1 : 0);
    }

    /**
     * 修改博客系统
     */
    @PreAuthorize("@ss.hasPermi('blog:blog:edit')" )
    @Log(title = "博客系统" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Blog blog) {
        return toAjax(iBlogService.updateById(blog) ? 1 : 0);
    }

    /**
     * 删除博客系统
     */
    @PreAuthorize("@ss.hasPermi('blog:blog:remove')" )
    @Log(title = "博客系统" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{blogIds}" )
    public AjaxResult remove(@PathVariable Integer[] blogIds) {
        return toAjax(iBlogService.removeByIds(Arrays.asList(blogIds)) ? 1 : 0);
    }
}
