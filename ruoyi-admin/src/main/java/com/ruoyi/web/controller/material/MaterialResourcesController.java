package com.ruoyi.web.controller.material;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.SetFilePath;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.material.domain.MaterialResources;
import com.ruoyi.material.service.IMaterialResourcesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 素材资源Controller
 * 
 * @author Weiye
 * @date 2021-03-24
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/material/resources" )
public class MaterialResourcesController extends BaseController {

    private final IMaterialResourcesService iMaterialResourcesService;

    /**
     * 查询素材资源列表
     */
    @PreAuthorize("@ss.hasPermi('material:resources:list')")
    @GetMapping("/list")
    public TableDataInfo list(MaterialResources materialResources) {
        return iMaterialResourcesService.queryPageList(materialResources);
    }

    /**
     * 删除素材资源
     *
     */
    @PreAuthorize("@ss.hasPermi('material:resources:remove')" )
    @Log(title = "素材资源" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{materialResourcesIds}" )
    public AjaxResult remove(@PathVariable Integer[] materialResourcesIds) {
        return toAjax(iMaterialResourcesService.removeByIds(Arrays.asList(materialResourcesIds)) ? 1 : 0);
    }

    /**
     * 获取素材资源详细信息
     */
    @PreAuthorize("@ss.hasPermi('material:resources:query')" )
    @GetMapping(value = "/{materialResourcesId}" )
    @SetFilePath
    public AjaxResult getInfo(@PathVariable("materialResourcesId" ) Integer materialResourcesId) {
        return AjaxResult.success(iMaterialResourcesService.getById(materialResourcesId));
    }

    /**
     * 下载文件
     */
    @PreAuthorize("@ss.hasPermi('material:resources:download')" )
    @GetMapping(value = "/download/{materialResourcesIds}" )
    public AjaxResult downloadResources(@PathVariable("materialResourcesIds" ) String materialResourcesIds) {
        List<MaterialResources> materialResources = iMaterialResourcesService.listByIds(Arrays.asList(materialResourcesIds.split(",")));
        if (CollectionUtil.isNotEmpty(materialResources)){
            Set<String> collect = materialResources.stream().map(i -> i.getFileUniqueId()).collect(Collectors.toSet());
            return AjaxResult.success(CollUtil.join(collect,","));
        }
        return AjaxResult.error();
    }

    /**
     * 新增素材资源
     */
    @PreAuthorize("@ss.hasPermi('material:resources:add')" )
    @Log(title = "素材资源" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MaterialResources materialResources) {
        return toAjax(iMaterialResourcesService.save(materialResources) ? 1 : 0);
    }

    /**
     * 修改素材资源
     */
    @PreAuthorize("@ss.hasPermi('material:resources:edit')" )
    @Log(title = "素材资源" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MaterialResources materialResources) {
        return toAjax(iMaterialResourcesService.updateById(materialResources) ? 1 : 0);
    }
}
