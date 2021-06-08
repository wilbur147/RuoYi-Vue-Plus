package com.ruoyi.content.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.SetFilePath;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.content.bo.ConCategroyAddBo;
import com.ruoyi.content.bo.ConCategroyEditBo;
import com.ruoyi.content.bo.ConCategroyQueryBo;
import com.ruoyi.content.service.IConCategroyService;
import com.ruoyi.content.vo.ConCategroyVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 分类Controller
 *
 * @author ruoyi
 * @date 2021-05-12
 */
@Api(value = "分类控制器", tags = {"分类管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/content/categroy")
public class ConCategroyController extends BaseController {

    private final IConCategroyService iConCategroyService;

    /**
     * 查询分类列表
     */
    @ApiOperation("查询分类列表")
    @PreAuthorize("@ss.hasPermi('content:categroy:list')")
    @GetMapping("/list")
    public AjaxResult<List<ConCategroyVo>> list(ConCategroyQueryBo bo) {
        return AjaxResult.success(iConCategroyService.queryList(bo));
    }

    /**
     * 导出分类列表
     */
    @ApiOperation("导出分类列表")
    @PreAuthorize("@ss.hasPermi('content:categroy:export')")
    @Log(title = "分类", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult<ConCategroyVo> export(ConCategroyQueryBo bo) {
        List<ConCategroyVo> list = iConCategroyService.queryList(bo);
        ExcelUtil<ConCategroyVo> util = new ExcelUtil<ConCategroyVo>(ConCategroyVo.class);
        return util.exportExcel(list, "分类");
    }

    /**
     * 获取分类详细信息
     */
    @ApiOperation("获取分类详细信息")
    @PreAuthorize("@ss.hasPermi('content:categroy:query')")
    @SetFilePath(name = {"icon"})
    @GetMapping("/{conCategroyId}")
    public AjaxResult<ConCategroyVo> getInfo(@PathVariable("conCategroyId" ) Long conCategroyId) {
        return AjaxResult.success(iConCategroyService.queryById(conCategroyId));
    }

    /**
     * 新增分类
     */
    @ApiOperation("新增分类")
    @PreAuthorize("@ss.hasPermi('content:categroy:add')")
    @Log(title = "分类", businessType = BusinessType.INSERT)
    @PostMapping()
    public AjaxResult<Void> add(@RequestBody ConCategroyAddBo bo) {
        return toAjax(iConCategroyService.insertByAddBo(bo) ? 1 : 0);
    }

    /**
     * 修改分类
     */
    @ApiOperation("修改分类")
    @PreAuthorize("@ss.hasPermi('content:categroy:edit')")
    @Log(title = "分类", businessType = BusinessType.UPDATE)
    @PutMapping()
    public AjaxResult<Void> edit(@RequestBody ConCategroyEditBo bo) {
        return toAjax(iConCategroyService.updateByEditBo(bo) ? 1 : 0);
    }

    /**
     * 删除分类
     */
    @ApiOperation("删除分类")
    @PreAuthorize("@ss.hasPermi('content:categroy:remove')")
    @Log(title = "分类" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{conCategroyIds}")
    public AjaxResult<Void> remove(@PathVariable Long[] conCategroyIds) {
        return toAjax(iConCategroyService.deleteWithValidByIds(Arrays.asList(conCategroyIds), true) ? 1 : 0);
    }
}
