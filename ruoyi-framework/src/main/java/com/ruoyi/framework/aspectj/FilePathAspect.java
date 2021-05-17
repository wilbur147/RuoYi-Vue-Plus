package com.ruoyi.framework.aspectj;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ruoyi.common.annotation.SetFilePath;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.file.entity.FileUploader;
import com.ruoyi.file.service.IFileUploaderService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 登录控制的切面 将信息存入到threadLocal,方便进行创建人和修改人的数据存储
 *
 * @author tangyq
 * @date 2020-06-22
 */
@Component
@Aspect
@Slf4j
public class FilePathAspect {

  public final String DATA = "data";
  public final String ROWS = "rows";

  @Autowired
  private IFileUploaderService fileUploaderService;

  // 配置注解入口
  @Pointcut("@annotation(com.ruoyi.common.annotation.SetFilePath)")
  public void pointCut()
  {
  }

  /**
   * 处理完请求后执行
   *
   * @param joinPoint 切点
   */
  @AfterReturning(pointcut = "pointCut()", returning = "jsonResult")
  public void doAfterReturning(JoinPoint joinPoint, Object jsonResult)
  {
    try {
      // 获得注解
      SetFilePath annotation = getAnnotation(joinPoint);
      if (annotation == null){
        return;
      }
      // 获取注解属性值
      String[] fileParamName = ObjectUtil.isNotEmpty(annotation.name()) ? annotation.name() : new String[]{"fileUniqueId"};
      String[] fileParamPath = ObjectUtil.isNotEmpty(annotation.pathValue()) ? annotation.pathValue() : new String[]{"filePath"};
      // 获取返回值
      if (ObjectUtil.isEmpty(jsonResult))
      {
        return;
      }
      if (jsonResult instanceof AjaxResult){
        jsonResult = BeanUtil.getFieldValue(jsonResult, DATA);
      }

      if (jsonResult instanceof TableDataInfo){
        jsonResult = BeanUtil.getFieldValue(jsonResult, ROWS);
      }

      if (jsonResult instanceof List){
        this.setFilePath((List<Object>) jsonResult,fileParamName,fileParamPath);
      }else{
        this.setFilePath(Arrays.asList(jsonResult),fileParamName,fileParamPath);
      }
    }catch (Exception e){
      log.error("method: doAfterReturning 《 AOP切点赋值文件路径 》 line: 【70行】发生的异常是：{}",e);
    }
  }

  /**
   * 是否存在注解，如果存在就获取
   */
  private SetFilePath getAnnotation(JoinPoint joinPoint) throws Exception
  {
    Signature signature = joinPoint.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    Method method = methodSignature.getMethod();

    if (method != null)
    {
      return method.getAnnotation(SetFilePath.class);
    }
    return null;
  }

  private void setFilePath(List<Object> objects,String[] fileParamName,String[] fileParamPath)
  {
    int j = 0;
    for (String paramName : fileParamName) {
      Set<String> collect = objects.stream().map(i -> ObjectUtil.isNotEmpty(BeanUtil.getFieldValue(i, paramName)) ?
              BeanUtil.getFieldValue(i, paramName).toString() : null).collect(Collectors.toSet());
      Map<String, FileUploader> infoMap = fileUploaderService.getFileInfoMap(CollUtil.join(collect, ","));
      if (Objects.nonNull(infoMap)) {
        for (Object obj : objects) {
          Object uniqueId = BeanUtil.getFieldValue(obj, paramName);
          if (ObjectUtil.isEmpty(uniqueId)) continue;
          FileUploader uploader = infoMap.get(uniqueId.toString());
          if (ObjectUtil.isNotEmpty(uploader)){
            BeanUtil.setFieldValue(obj,fileParamPath[j],uploader.getFullFilePath());
          }
        }
      }
      j++;
    }
  }
}
