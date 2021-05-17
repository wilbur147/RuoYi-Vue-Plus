<template>
  <div class="app-container">
    <h2 style="margin-left: 10;"> 素材资源 </h2>
    <!-- 资源类型 -->
    <el-tabs v-model="resourceTypeActiveName" @tab-click="resourceTypeClick">
      <el-tab-pane  v-for="dict in resourceTypeOptions"
        :key="dict.dictValue"
        :label="dict.dictLabel"
        :name="dict.dictValue">
      </el-tab-pane>

    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="资源名称" prop="resourceName">
        <el-input
          v-model="queryParams.resourceName"
          placeholder="请输入资源名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8" style="margin-bottom: 30px;">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="medium"
          @click="handleAdd"
          v-hasPermi="['material:resources:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="medium"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['material:resources:remove']"
        >删除选中</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="medium"
          :disabled="multiple"
          @click="handleDownload"
          v-hasPermi="['material:resources:download']"
        >下载选中</el-button>
      </el-col>
    </el-row>

    </el-tabs>

    <!-- 分组 -->
    <el-tabs tab-position="left" type="border-card" style="height: 800px; width: 100%;"
    v-model="materialGroupId"
    @tab-click="materialGroupClick">
      <el-tab-pane
            style="height: 800px; width: 100%;"
            v-for="(group, index) in groupList"
            :key="group.materialGroupId"
            :disabled="group.isVoid != `Y`"
            :name="String(group.materialGroupId)"
          >
            <div class="sortItem" slot="label" style="float:left">
              <i class="el-icon-picture"></i>
              {{group.groupName}}
            </div>

            <el-row style="display: flex;flex-wrap: wrap;">
              <el-col
                v-for="(resources, index) in resourcesList"
                :key="resources.materialResourcesId"
                style="margin: 15px;width: 23%;"
                :span="24"
              >
                <el-card
                  :body-style="{ padding: '0px', textAlign: 'center' }"
                  style="position: relative;"
                  shadow="always"
                >
                  <input class="checkbox" type="checkbox" :checked="ids.indexOf(resources.materialResourcesId)>=0" @click="handleChecked(resources)">
                  <el-switch v-model="resources.isVoid" class="void-switch"
                  v-hasPermi="['material:resources:edit']"
                  active-color="#13ce66"
                  inactive-color="#dcdfe6"
                  active-value="Y"
                  inactive-value="N"
                  @change="handleIsVoid($event,resources.materialResourcesId)"
                  > </el-switch>
                  <div class="resName" v-if="resources.resourceName"  @click="handleChecked(resources)">{{resources.resourceName}}</div>
                  <el-image
                    v-if="'IMG' == fileType"
                    :src="resources.filePath"
                    style="cursor:pointer;width: 100%;height: 200px;"
                    fit="scale-down"
                    @click="handlePreview(resources.filePath)"
                  />
                  <video v-else-if="'VIDEO' == fileType"
                         :src="resources.filePath"
                         class="avatar video-avatar"
                         controls="controls"
                         controlslist="nodownload"
                         style="width: 100%;height: 200px;">
                      您的浏览器不支持视频播放
                  </video>
                  <audio v-else-if="'AUDIO' == fileType"
                  :src="resources.filePath"
                  style="width: 100%;height: 100px;margin-bottom: 100px;"
                  controls preload loop controlsList="nodownload"
                  />
                  <div style="margin: 14px 0;">
                    <span class="update-time-span" v-if="resources.createTime">更新于：{{resources.createTime}}</span>
                    <el-button-group>
                      <el-tooltip class="item" effect="dark" :content="'复制'+resourceTypeLable+'地址'" placement="bottom-start" style="margin-right: 2px">
                        <el-button
                          size="mini"
                          icon="el-icon-copy-document"
                          @click="handleCopyUrl(resources.filePath)"
                        />
                      </el-tooltip>
                      <el-tooltip class="item" effect="dark" :content="'修改'+resourceTypeLable"
                      placement="bottom-start" style="margin-right: 2px"
                      v-hasPermi="['material:resources:edit']">
                        <el-button
                          type="warning"
                          size="mini"
                          icon="el-icon-edit"
                          @click="handleUpdate(resources)"
                        />
                      </el-tooltip>
                      <el-tooltip class="item" effect="dark" :content="'删除'+resourceTypeLable"
                      placement="bottom-start" style="margin-right: 2px"
                      v-hasPermi="['material:resources:remove']">
                        <el-button
                          type="danger"
                          size="mini"
                          icon="el-icon-delete"
                          @click="handleDelete(resources)"
                        />
                      </el-tooltip>

                    </el-button-group>
                  </div>
                </el-card>

              </el-col>
            </el-row>

            <!--分页-->
            <div class="block" v-if="total>0">
             <el-pagination
                @current-change="getRescourcesList"
                :current-page.sync="queryParams.pageNum"
                :page-size="queryParams.pageSize"
                layout="total, prev, pager, next, jumper"
                :total="total">
              </el-pagination>
            </div>
          </el-tab-pane>
    </el-tabs>



    <!-- 添加或修改素材资源对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item :label="'上传'+resourceTypeLable" prop="filePath">
          <file-upload ref="fileUpload" :value="form.filePath" :params="{uploadType:'MATERIAL'}" :fileType="fileType" :fileSize="fileSize" @input='fileUpload'></file-upload >
        </el-form-item>
        <el-form-item label="资源名称" prop="resourceName">
          <el-input v-model="form.resourceName" placeholder="请输入资源名称" />
        </el-form-item>
        <el-form-item label="摘要" prop="summary">
          <el-input v-model="form.summary" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--  文件内容预览框  -->
    <el-dialog :visible.sync="dialogFileVisible" title="预览" width="800" append-to-body>
      <img :src="dialogFileUrl" style="display: block; max-width: 100%; margin: 0 auto;">
    </el-dialog>
  </div>
</template>

<script>
  import resources from "./resources.js";
  export default resources;
</script>

<style>
 @import url("./resources.css");
</style>
