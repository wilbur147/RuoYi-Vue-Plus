<template>
  <div class="app-container">
    <el-tabs type="border-card" @tab-click="handleClick" v-model="activeName">
      <el-tab-pane name="one"  v-hasPermi="['system:globalConfig:query']">
        <span slot="label"><i class="el-icon-edit"></i> 文件存储</span>
        <el-form style="margin-left: 20px;" label-position="left" label-width="180px"  :model="form" :rules="rules" ref="form">

          <aside>
            可以选择多种存储类型，通过开关选择文件显示方式<br />
          </aside>

          <el-form-item label="文件显示优先级">
            <el-radio v-for="item in filePriorityDictList" :key="item.dictCode" v-model="form.filePriorityShow" :label="item.dictValue"
               border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>

          <el-form-item label="存储类型">
            <el-radio-group v-model="storageType" size="medium">
              <el-radio-button v-for="(item, index) in filePriorityDictList" :key="item.dictCode" :label="item.dictValue"
                :disabled="item.disabled">{{item.dictLabel}}</el-radio-button>
            </el-radio-group>
          </el-form-item>

          <div v-if="storageType==0">
            <el-form-item label="文件上传本地">
              <el-radio v-for="item in yesNoDictList" :key="item.dictCode" v-model="form.uploadLocal" :label="item.dictValue"
                border size="medium">{{item.dictLabel}}</el-radio>
            </el-form-item>
          </div>

          <div v-if="storageType==1">
            <el-form-item label="七牛云文件域名" prop="qiNiuDomainPrefix">
              <el-input v-model="form.qiNiuDomainPrefix" clearable placeholder="请输入文件服务器域名，如：http://file.***.***/" auto-complete="new-password" style="width: 400px"></el-input>
            </el-form-item>
            <el-form-item label="七牛云公钥">
              <el-input v-model="form.qiNiuAccessKey" clearable placeholder="请输入AccessKey" auto-complete="new-password" style="width: 400px"></el-input>
            </el-form-item>
            <el-form-item label="七牛云私钥">
              <el-input type="password" v-model="form.qiNiuSecretKey" clearable placeholder="请输入SecretKey" auto-complete="new-password" style="width: 400px"></el-input>
            </el-form-item>
            <el-form-item label="上传空间">
              <el-input v-model="form.qiNiuBucket" clearable placeholder="请输入七牛云的空间名称,如: lakers" style="width: 400px"></el-input>
            </el-form-item>
            <el-form-item label="文件上传七牛云">
              <el-radio v-for="item in yesNoDictList" :key="item.dictCode" v-model="form.uploadQiNiu" :label="item.dictValue"
                border size="medium">{{item.dictLabel}}</el-radio>
            </el-form-item>
          </div>

          <div v-if="storageType==2">
            <el-form-item label="Bucket 名称">
              <el-input v-model="form.aliyunBucketName" clearable placeholder="请输入Bucket名称" auto-complete="new-password" style="width: 400px"></el-input>
            </el-form-item>
            <el-form-item label="地域节点（EndPoint）">
              <el-input v-model="form.aliyunEndpoint" clearable placeholder="请输入EndPoint,如:https://oss-cn-hangzhou.aliyuncs.com" auto-complete="new-password" style="width: 400px"></el-input>
            </el-form-item>
            <el-form-item label="Bucket 域名">
              <el-input v-model="form.aliyunFileUrl" clearable placeholder="默认为bucket + endPoint,若使用自定义域名,请修改" auto-complete="new-password" style="width: 400px"></el-input>
            </el-form-item>
            <el-form-item label="Access Key">
              <el-input type="password" v-model="form.aliyunAccessKey" clearable placeholder="请输入AccessKey" style="width: 400px"></el-input>
            </el-form-item>
            <el-form-item label="Access Key Secret">
              <el-input type="password" v-model="form.aliyunAccessKeySecret" clearable placeholder="请输入AccessKeySecret" style="width: 400px"></el-input>
            </el-form-item>
            <el-form-item label="文件上传阿里云OSS">
              <el-radio v-for="item in yesNoDictList" :key="item.dictCode" v-model="form.uploadAliYunOss" :label="item.dictValue"
                border size="medium">{{item.dictLabel}}</el-radio>
            </el-form-item>
          </div>

          <el-form-item>
            <el-button type="primary" @click="submitForm()" v-hasPermi="['system:globalConfig:edit']">保 存</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>


      <el-tab-pane name="two" v-hasPermi="['system:globalConfig:query']">
        <span slot="label"><i class="el-icon-edit"></i> 邮箱配置</span>
        <el-form style="margin-left: 20px;" label-position="left" label-width="80px"  :model="form" :rules="rules" ref="form">

          <aside>
            邮箱配置主要用于配置网站消息的接收<br />
            例如：友链申请、网站评论、网站反馈等，可以在系统配置处选择是否开启邮件通知<br />
          </aside>

          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="密码" prop="newPwd1">
            <el-input type="password" v-model="form.emailPassword" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="用户名" prop="newPwd2">
            <el-input v-model="form.emailUserName" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="SMTP地址" prop="newPwd2">
            <el-input v-model="form.smtpAddress" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="SMTP端口" prop="newPwd2">
            <el-input v-model="form.smtpPort" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm()" v-hasPermi="['system:globalConfig:edit']">保 存</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <!-- <el-tab-pane name="seven" v-permission="'/systemConfig/cleanRedisByKey'">
        <span slot="label"><i class="el-icon-edit"></i> Redis管理</span>
        <el-form style="margin-left: 20px;" label-position="left" label-width="120px">

          <aside>
            Redis管理主要用于清空一些缓存数据<br />
            用户首次部署时，可以使用清空全部，把Redis中的缓存一键清空<br />
          </aside>

          <el-form-item label="全部">
            <el-row>
              <el-col :span="6">
                <el-button type="danger" @click="cleanRedis('ALL')">清空全部</el-button>
              </el-col>
            </el-row>
          </el-form-item>

          <el-form-item label="博客相关">
            <el-row>
              <el-col :span="3">
                <el-button type="primary" @click="cleanRedis('BLOG_CLICK')">清空点击量</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="success" @click="cleanRedis('BLOG_PRAISE')">清空点赞量</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="info" @click="cleanRedis('BLOG_LEVEL')">清空推荐博客</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="warning" @click="cleanRedis('HOT_BLOG')">清空热门博客</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="danger" @click="cleanRedis('NEW_BLOG')">清空最新博客</el-button>
              </el-col>
            </el-row>
          </el-form-item>

          <el-form-item label="分类和归档相关">
            <el-row>
              <el-col :span="3">
                <el-button type="primary" @click="cleanRedis('MONTH_SET')">清空分类日期</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="success" @click="cleanRedis('BLOG_SORT_BY_MONTH')">清空分类数据</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="info" @click="cleanRedis('BLOG_SORT_CLICK')">清空分类点击量</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="warning" @click="cleanRedis('TAG_CLICK')">清空标签点击量</el-button>
              </el-col>
            </el-row>
          </el-form-item>


          <el-form-item label="系统相关">
            <el-row>
              <el-col :span="3">
                <el-button type="primary" @click="cleanRedis('REDIS_DICT_TYPE')">清空字典</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="success" @click="cleanRedis('ADMIN_VISIT_MENU')">清空角色访问菜单</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="info" @click="cleanRedis('userToken')">清空用户Token</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="warning" @click="cleanRedis('REQUEST_LIMIT')">清空接口请求限制</el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </el-tab-pane> -->

      <!-- <el-tab-pane label="仪表盘通知" name="third">
        <span slot="label"><i class="el-icon-edit"></i> 仪表盘通知</span>
        <div class="editor-container">
          <CKEditor ref="editor" v-if="form.editorModel == '0'" :content="form.dashboardNotification" :height="500"></CKEditor>
          <MarkdownEditor ref="editor" v-if="form.editorModel == '1'" :height="660"></MarkdownEditor>
        </div>
        <div style="margin-top: 5px; margin-left: 10px;">
          <el-button type="primary" @click="submitForm()" v-permission="'/system/editMe'">保 存</el-button>
        </div>
      </el-tab-pane> -->

    </el-tabs>

  </div>
</template>

<script>
  import { getSystemConfig, editSystemConfig } from "@/api/system/allocation";
  import { selectTypeList } from "@/api/system/dict/data"

  // import CKEditor from "@/components/CKEditor";
  // import MarkdownEditor from "@/components/MarkdownEditor";

  export default {
    data() {
      return {
        form: {
            filePriorityShow: 0
        },
        storageType: 0,
        activeName: "one",
        yesNoDictList: [], //是、否 字典
        openDictList: [], // 正常、通用  字典
        filePriorityDictList: [], //图片显示优先级字典
        loadingInstance: null, // loading对象
        rules: {
          qiNiuDomainPrefix: [{
            pattern: /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/,
            message: '请输入正确的域名'
          }, ],
          aliyunEndpoint: [{
            pattern: /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/,
            message: '请输入格式EndPoint'
          }, ],
          email: [{
            pattern: /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/,
            message: '请输入正确的邮箱'
          }, ],
        }
      };
    },
    watch: {

    },
    // components: {
    //   CKEditor,
    //   MarkdownEditor
    // },
    created() {
      // 获取字典
      this.getDictList()
      // 获取系统配置
      this.getSystemConfigList()
    },
    methods: {
      /**
       * 字典查询
       */
      getDictList() {

        var dictTypeList = ['sys_yes_no', 'sys_file_priority', 'sys_normal_disable'
        ]
        selectTypeList(dictTypeList).then(response => {
          if (response.code == 200) {
            var dictMap = response.data;
            this.yesNoDictList = dictMap.sys_yes_no
            this.openDictList = dictMap.sys_normal_disable
            this.filePriorityDictList = dictMap.sys_file_priority
          }
        });


      },
      handleClick(tab, event) {
        //设置富文本内容
        if (this.form.dashboardNotification) {
          this.$refs["editor"].setData(this.form.dashboardNotification);
        }
      },
      getSystemConfigList() {
        getSystemConfig({"configKeywords": "sys.config"}).then(response => {
          if (response.code == 200) {
            if (response.data) {
              this.form = response.data;
            }
          }
        });
      },
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            //获取文本编辑器中的内容
            // this.form.dashboardNotification = this.$refs["editor"].getData();
            editSystemConfig(this.form).then(res => {
              if (res.code = 200) {
                this.msgSuccess(res.msg);
              } else {
                 this.msgError(res.msg);
              }
            });
          }
        });
      },

    }
  };
</script>

<style lang="scss">
  aside {
    background: #eef1f6;
    padding: 8px 24px;
    margin-bottom: 20px;
    border-radius: 2px;
    display: block;
    line-height: 32px;
    font-size: 16px;
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Fira Sans", "Droid Sans", "Helvetica Neue", sans-serif;
    color: #2c3e50;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;

    a {
      color: #337ab7;
      cursor: pointer;

      &:hover {
        color: rgb(32, 160, 255);
      }
    }
  }
</style>
