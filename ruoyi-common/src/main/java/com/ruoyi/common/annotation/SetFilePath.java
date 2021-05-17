package com.ruoyi.common.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解为有文件唯一码的类赋值文件路径
 * 
 * @author weiye
 *
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SetFilePath
{
    /**
     *  name和 pathValue 索引要对称 例如： name[0] icon  那么 pathValue[0] 就是对应的 iconPath
     */
    public String[] name() default {};

    /**
     * name数组有两个及以上必须要设置 pathValue 不然会报数组越界错误
     * @return
     */
    public String[] pathValue() default {};

}
