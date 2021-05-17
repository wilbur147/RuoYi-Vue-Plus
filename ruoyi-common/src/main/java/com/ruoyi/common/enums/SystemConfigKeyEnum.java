package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 系统配置key枚举类
 * @author Wilbur
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SystemConfigKeyEnum {

    /**
     *  ①②③④⑤⑥⑦⑧
     *  系统配置前缀
     */
    SYS_CONFIG_PREFIX("sys.config.","系统配置"),
    /**
     *  ①  文件存储
     */
    FILE_PRIORITY_SHOW("filePriorityShow","文件优先选择显示"),
    IS_UPLOAD_LOCAL("uploadLocal","是否文件上传本地"),

    QINIU_DOMAIN_PREFIX("qiNiuDomainPrefix","七牛云文件域名"),
    QINIU_ACCESS_KEY("qiNiuAccessKey","七牛云公钥"),
    QINIU_SECRET_KEY("qiNiuSecretKey","七牛云私钥"),
    QINIU_BUCKET_PATH("qiNiuBucket","七牛云上传空间"),
    IS_UPLOAD_QINIU("uploadQiNiu","文件上传七牛"),

    ALIYUN_BUCKET_NAME("aliyunBucketName","阿里云Bucket名称"),
    ALIYUN_ENDPOINT("aliyunEndpoint","阿里云地域节点（EndPoint）"),
    ALIYUN_FILE_URL("aliyunFileUrl","阿里云Bucket域名"),
    ALIYUN_ACCESS_KEY("aliyunAccessKey","阿里云AccessKey"),
    ALIYUN_ACCESS_KEY_SECRET("aliyunAccessKeySecret","阿里云AccessKeySecret"),
    IS_UPLOAD_OSS("uploadAliYunOss","文件上传阿里云OSS"),

//
//    /**
//     * 站点简介
//     */
//    SITE_DESC("siteDesc"),
//    /**
//     * 主域名
//     */
//    DOMAIN("domain"),
//    /**
//     * 博客地址
//     */
//    SITE_URL("siteUrl"),
//    /**
//     * 站点图标
//     */
//    SITE_FAVICON("siteFavicon"),
//    /**
//     * 资源文件域名
//     */
//    STATIC_WEB_SITE("staticWebSite"),
//    /**
//     * CMS后管系统地址
//     */
//    CMS_URL("cmsUrl"),
//
//    /**
//     * 网站Title
//     */
//    SITE_NAME("siteName"),
//    /**
//     * 网站首页的Description
//     */
//    HOME_DESC("homeDesc"),
//    /**
//     * 网站首页的Keywords
//     */
//    HOME_KEYWORDS("homeKeywords"),
//    /**
//     * 百度推送Token
//     */
//    BAIDU_PUSH_TOKEN("baiduPushToken"),
//
//    /**
//     * 百度站长平台的Cookie
//     */
//    BAIDU_PUSH_COOKIE("baiduPushCookie"),



//
//
//    /**
//     * 允许匿名评论
//     */
//    ANONYMOUS("anonymous"),
//    /**
//     * 开启留言板评论
//     */
//    COMMENT("comment"),
//    /**
//     * 占位符，当没输入内容时显示该值
//     */
//    EDITOR_PLACEHOLDER("editorPlaceholder"),
//    /**
//     * 评论框右下角显示的内容
//     */
//    EDITOR_ALERT("editorAlert"),
//
//    /**
//     * 站长名称
//     */
//    AUTHOR_NAME("authorName"),
//    /**
//     * 站长邮箱
//     */
//    AUTHOR_EMAIL("authorEmail"),
//    /**
//     * 微信二维码
//     */
//    WX_CODE("wxCode"),
//    /**
//     * QQ
//     */
//    QQ("qq"),
//    /**
//     * weibo
//     */
//    WEIBO("weibo"),
//    /**
//     * github
//     */
//    GITHUB("github"),
//
//    /**
//     * 微信赞赏码
//     */
//    WX_PRAISE_CODE("wxPraiseCode"),
//    /**
//     * 支付宝赞赏码
//     */
//    ZFB_PRAISECODE("zfbPraiseCode"),
//
//    /**
//     * 百度Api的AK
//     */
//    BAIDU_API_AK("baiduApiAk"),
//    /**
//     * 是否显示维护通知
//     */
//    MAINTENANCE("maintenance"),
//    /**
//     * 维护通知的日期
//     */
//    MAINTENANCE_DATE("maintenanceDate"),
//    /**
//     * 维护通知大约需要的时间
//     */
//    MAINTENANCE_TIME("maintenanceTime"),
//
//    /**
//     * 系统最后一次更新的日期
//     */
//    UPDATE_TIME("updateTime"),
//
//    /**
//     * 文章编辑器
//     */
//    ARTICLE_EDITOR("articleEditor"),
//
//    /**
//     * 网站安装时间，默认为执行init_data.sql的时间
//     */
//    INSTALLDATE("installdate"),
//
//    /**
//     * 当切换浏览器tab时，在原tab上的标题。比如https://docs.zhyd.me上的“麻溜儿回来~~~”
//     */
//    DYNAMIC_TITLE("dynamicTitle"),
    ;

    private String key;
    private String value;

    public static String getValueByKey(String key) {
        SystemConfigKeyEnum[] configKeyEnums = values();
        for (SystemConfigKeyEnum configKeyEnum : configKeyEnums) {
            if (configKeyEnum.getKey().equals(key)) {
                return configKeyEnum.getValue();
            }
        }
        return null;
    }

    public static String getPrefixSysConfigKey(String key){
        return SystemConfigKeyEnum.SYS_CONFIG_PREFIX.getKey() + key;
    }
}
