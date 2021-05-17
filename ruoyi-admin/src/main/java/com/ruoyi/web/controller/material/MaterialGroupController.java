package com.ruoyi.web.controller.material;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.material.domain.MaterialGroup;
import com.ruoyi.material.service.IMaterialGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 素材分组Controller
 * 
 * @author Weiye
 * @date 2021-03-24
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/material/group" )
public class MaterialGroupController extends BaseController {

    private final IMaterialGroupService iMaterialGroupService;

    /**
     * 查询素材分组列表
     */
    @PreAuthorize("@ss.hasPermi('material:group:list')")
    @GetMapping("/list")
    public TableDataInfo list(MaterialGroup materialGroup) {
        startPage();
        List<MaterialGroup> list = iMaterialGroupService.queryList(materialGroup);
        return getDataTable(list);
    }

    /**
     * 导出素材分组列表
     */
    @PreAuthorize("@ss.hasPermi('material:group:export')" )
    @Log(title = "素材分组" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(MaterialGroup materialGroup) {
        List<MaterialGroup> list = iMaterialGroupService.queryList(materialGroup);
        ExcelUtil<MaterialGroup> util = new ExcelUtil<MaterialGroup>(MaterialGroup.class);
        return util.exportExcel(list, "group" );
    }

    /**
     * 获取素材分组详细信息
     */
    @PreAuthorize("@ss.hasPermi('material:group:query')" )
    @GetMapping(value = "/{materialGroupId}" )
    public AjaxResult getInfo(@PathVariable("materialGroupId" ) Integer materialGroupId) {
        return AjaxResult.success(iMaterialGroupService.getById(materialGroupId));
    }

    /**
     * 新增素材分组
     */
    @PreAuthorize("@ss.hasPermi('material:group:add')" )
    @Log(title = "素材分组" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MaterialGroup materialGroup) {
        materialGroup.setParentId(-1);
        return toAjax(iMaterialGroupService.save(materialGroup) ? 1 : 0);
    }

    /**
     * 修改素材分组
     */
    @PreAuthorize("@ss.hasPermi('material:group:edit')" )
    @Log(title = "素材分组" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MaterialGroup materialGroup) {
        return toAjax(iMaterialGroupService.updateById(materialGroup) ? 1 : 0);
    }

    /**
     * 删除素材分组
     */
    @PreAuthorize("@ss.hasPermi('material:group:remove')" )
    @Log(title = "素材分组" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{materialGroupIds}" )
    public AjaxResult remove(@PathVariable Integer[] materialGroupIds) {
        return toAjax(iMaterialGroupService.removeByIds(Arrays.asList(materialGroupIds)) ? 1 : 0);
    }
}
