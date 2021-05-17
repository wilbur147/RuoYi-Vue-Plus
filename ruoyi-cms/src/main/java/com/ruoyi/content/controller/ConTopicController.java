package com.ruoyi.content.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.SetFilePath;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.content.bo.ConTopicAddBo;
import com.ruoyi.content.bo.ConTopicEditBo;
import com.ruoyi.content.bo.ConTopicQueryBo;
import com.ruoyi.content.service.IConTopicService;
import com.ruoyi.content.vo.ConTopicVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 专题Controller
 * 
 * @author ruoyi
 * @date 2021-05-12
 */
@Api(value = "专题控制器", tags = {"专题管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/content/topic")
public class ConTopicController extends BaseController {

    private final IConTopicService iConTopicService;

    /**
     * 查询专题列表
     */
    @ApiOperation("查询专题列表")
    @PreAuthorize("@ss.hasPermi('content:topic:list')")
    @GetMapping("/list")
    public TableDataInfo<ConTopicVo> list(ConTopicQueryBo bo) {
        startPage();
        List<ConTopicVo> list = iConTopicService.queryList(bo);
        return getDataTable(list);
    }

    /**
     * 导出专题列表
     */
    @ApiOperation("导出专题列表")
    @PreAuthorize("@ss.hasPermi('content:topic:export')")
    @Log(title = "专题", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult<ConTopicVo> export(ConTopicQueryBo bo) {
        List<ConTopicVo> list = iConTopicService.queryList(bo);
        ExcelUtil<ConTopicVo> util = new ExcelUtil<ConTopicVo>(ConTopicVo.class);
        return util.exportExcel(list, "专题");
    }

    /**
     * 获取专题详细信息
     */
    @ApiOperation("获取专题详细信息")
    @PreAuthorize("@ss.hasPermi('content:topic:query')")
    @SetFilePath(name = {"icon"})
    @GetMapping("/{conTopicId}")
    public AjaxResult<ConTopicVo> getInfo(@PathVariable("conTopicId" ) Long conTopicId) {
        return AjaxResult.success(iConTopicService.queryById(conTopicId));
    }

    /**
     * 新增专题
     */
    @ApiOperation("新增专题")
    @PreAuthorize("@ss.hasPermi('content:topic:add')")
    @Log(title = "专题", businessType = BusinessType.INSERT)
    @PostMapping()
    public AjaxResult<Void> add(@RequestBody ConTopicAddBo bo) {
        return toAjax(iConTopicService.insertByAddBo(bo) ? 1 : 0);
    }

    /**
     * 修改专题
     */
    @ApiOperation("修改专题")
    @PreAuthorize("@ss.hasPermi('content:topic:edit')")
    @Log(title = "专题", businessType = BusinessType.UPDATE)
    @PutMapping()
    public AjaxResult<Void> edit(@RequestBody ConTopicEditBo bo) {
        return toAjax(iConTopicService.updateByEditBo(bo) ? 1 : 0);
    }

    /**
     * 删除专题
     */
    @ApiOperation("删除专题")
    @PreAuthorize("@ss.hasPermi('content:topic:remove')")
    @Log(title = "专题" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{conTopicIds}")
    public AjaxResult<Void> remove(@PathVariable Long[] conTopicIds) {
        return toAjax(iConTopicService.deleteWithValidByIds(Arrays.asList(conTopicIds), true) ? 1 : 0);
    }
}
