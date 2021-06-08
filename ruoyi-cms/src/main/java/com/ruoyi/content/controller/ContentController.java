package com.ruoyi.content.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.SetFilePath;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.content.bo.ContentAddBo;
import com.ruoyi.content.bo.ContentEditBo;
import com.ruoyi.content.bo.ContentQueryBo;
import com.ruoyi.content.service.IContentService;
import com.ruoyi.content.vo.ContentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 内容Controller
 *
 * @author ruoyi
 * @date 2021-05-12
 */
@Api(value = "内容控制器", tags = {"内容管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/content/content")
public class ContentController extends BaseController {

    private final IContentService iContentService;

    /**
     * 查询内容列表
     */
    @ApiOperation("查询内容列表")
    @PreAuthorize("@ss.hasPermi('content:content:list')")
    @GetMapping("/list")
    public TableDataInfo<ContentVo> list(ContentQueryBo bo) {
        return iContentService.queryPageList(bo);
    }

    /**
     * 导出内容列表
     */
    @ApiOperation("导出内容列表")
    @PreAuthorize("@ss.hasPermi('content:content:export')")
    @Log(title = "内容", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult<ContentVo> export(ContentQueryBo bo) {
        List<ContentVo> list = iContentService.queryList(bo);
        ExcelUtil<ContentVo> util = new ExcelUtil<ContentVo>(ContentVo.class);
        return util.exportExcel(list, "内容");
    }

    /**
     * 获取内容详细信息
     */
    @ApiOperation("获取内容详细信息")
    @PreAuthorize("@ss.hasPermi('content:content:query')")
    @SetFilePath(name = {"banner","icon"}, pathValue = {"bannerPath","iconPath"})
    @GetMapping("/{contentId}")
    public AjaxResult<ContentVo> getInfo(@PathVariable("contentId" ) Long contentId) {
        return AjaxResult.success(iContentService.queryById(contentId));
    }

    /**
     * 新增内容
     */
    @ApiOperation("新增内容")
    @PreAuthorize("@ss.hasPermi('content:content:add')")
    @Log(title = "内容", businessType = BusinessType.INSERT)
    @PostMapping()
    public AjaxResult<Void> add(@RequestBody ContentAddBo bo) {
        return toAjax(iContentService.insertByAddBo(bo) ? 1 : 0);
    }

    /**
     * 修改内容
     */
    @ApiOperation("修改内容")
    @PreAuthorize("@ss.hasPermi('content:content:edit')")
    @Log(title = "内容", businessType = BusinessType.UPDATE)
    @PutMapping()
    public AjaxResult<Void> edit(@RequestBody ContentEditBo bo) {
        return toAjax(iContentService.updateByEditBo(bo) ? 1 : 0);
    }

    /**
     * 修改状态
     */
    @ApiOperation("修改内容")
    @PreAuthorize("@ss.hasPermi('content:content:edit')")
    @Log(title = "内容", businessType = BusinessType.UPDATE)
    @PutMapping("/editStatus")
    public AjaxResult<Void> editStatus(@RequestBody ContentEditBo bo) {
        return toAjax(iContentService.updateStatusByEditBo(bo) ? 1 : 0);
    }

    /**
     * 删除内容
     */
    @ApiOperation("删除内容")
    @PreAuthorize("@ss.hasPermi('content:content:remove')")
    @Log(title = "内容" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{contentIds}")
    public AjaxResult<Void> remove(@PathVariable Long[] contentIds) {
        return toAjax(iContentService.deleteWithValidByIds(Arrays.asList(contentIds), true) ? 1 : 0);
    }
}
