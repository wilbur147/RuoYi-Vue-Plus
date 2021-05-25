<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="专题" prop="conTopicId">
        <el-select v-model="queryParams.conTopicId" filterable clearable  placeholder="请选择专题" size="small">
          <el-option
            v-for="topic in topicList"
            :key="topic.conTopicId"
            :label="topic.topicName"
            :value="topic.conTopicId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="标题名称" prop="contentName">
        <el-input
          v-model="queryParams.contentName"
          placeholder="请输入标题名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['content:content:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['content:content:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['content:content:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['content:content:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-row>
      <el-col :span="4">
        <div class="expand">
          <div>
            <!-- <el-button @click="handleAddTop">添加顶级节点</el-button> -->
            <el-tree ref="expandMenuList" class="expand-tree" v-if="isLoadingTree" :data="setTree" node-key="categroyId"
              :current-node-key="defaultCheckedKey" highlight-current :props="defaultProps" :expand-on-click-node="false"
              :render-content="renderContent" :default-expanded-keys="defaultExpandKeys" @node-click="handleNodeClick"></el-tree>
          </div>
        </div>
      </el-col>
      <el-col :span="20">
        <el-table v-loading="loading" :data="contentList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="内容主键ID" align="center" prop="contentId" v-if="false"/>
          <el-table-column label="标题名称" align="center" prop="contentName" />
          <el-table-column label="封面图" width="160" align="center" prop="banner" >
            <template slot-scope="scope">
              <el-image
                :src="scope.row.bannerPath"
                style="width: 130px;height: 70px;"
              />
            </template>
           </el-table-column>
          <el-table-column label="图标" width="160" align="center" prop="icon" >
            <template slot-scope="scope">
              <el-image
                :src="scope.row.iconPath"
                style="width: 130px;height: 70px;"
              />
            </template>
           </el-table-column>
          <el-table-column label="关键字" align="center" prop="keywords" >
            <template slot-scope="scope">
              <template>
                <el-tag
                  style="margin-left: 3px; font-size: inherit;"
                  type="warning"
                  v-if="item"
                  :key="index"
                  v-for="(item, index) in scope.row.keywords"
                >{{item}}</el-tag>
              </template>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" width="50">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                :active-value="0"
                :inactive-value="1"
                @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="显示顺序" align="center" prop="orderNum" />
          <el-table-column label="是否推荐" align="center" prop="recommend">
            <template slot-scope="scope">
              <el-tag
              style="font-size: inherit;"
              v-for="item in isrecommendOptions"
              :key="item.dictValue"
              :type="scope.row.recommend === 'Y' ?'success' : 'info'"
              v-if="scope.row.recommend == item.dictValue">{{item.dictLabel}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['content:content:edit']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['content:content:remove']"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
          @pagination="getList" />
      </el-col>
    </el-row>
    <!-- 添加或修改内容对话框 -->
    <el-dialog :title="title" :visible.sync="open" fullscreen>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="16">
            <el-row :gutter="80">
              <el-col :span="20">
                <el-form-item label="标题" prop="contentName">
                  <el-input v-model="form.contentName" placeholder="请输入标题" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="80">
              <el-col :span="10">
                <el-form-item label="分类" prop="categroyId">
                  <el-cascader
                      style="width: 100%;"
                      v-model="form.categroyId"
                      filterable
                      :options="setTree"
                      :props="sortProps"
                      @change="sortHandleChange"></el-cascader>
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item label="专题" prop="conTopicId">
                  <el-select v-model="form.conTopicId" filterable clearable  placeholder="请选择专题"
                  style="width: 100%;"
                  >
                    <el-option
                      v-for="topic in topicList"
                      :key="topic.conTopicId"
                      :label="topic.topicName"
                      :value="topic.conTopicId"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="80">
              <el-col :span="10">
                <el-form-item label="关键字" prop="keywords">
                 <input-tags
                    v-if="!contentDisabled"
                    v-model="form.keywords"
                    :listFilter="true"
                    :placeholder="'按enter键创建标签'"
                    ></input-tags>
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item label="排序字段" prop="orderNum">
                  <el-input-number v-model="form.orderNum" controls-position="right" style="width: 100%;" :min="0" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="80">
              <el-col :span="10">
                <el-form-item label="状态" prop="status">
                  <el-radio-group v-model="form.status">
                    <el-radio border
                      v-for="dict in statusOptions"
                      :key="dict.dictValue"
                      :label="parseInt(dict.dictValue)"
                    >{{dict.dictLabel}}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item label="是否推荐" prop="recommend">
                  <el-radio-group v-model="form.recommend">
                    <el-radio border
                      v-for="dict in isrecommendOptions"
                      :key="dict.dictValue"
                      :label="dict.dictValue"
                    >{{dict.dictLabel}}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
          <el-col :span="8">
            <el-form-item label="封面图">
              <file-upload ref="bannerUpload" :value="form.bannerPath" :params="{uploadType:'CONTENT'}" :fileType="fileType" :fileSize="fileSize" @input='bannerUpload'></file-upload >
            </el-form-item>
            <el-form-item label="图标">
              <file-upload ref="iconUpload" :value="form.iconPath" :params="{uploadType:'CONTENT'}" :fileType="fileType" :fileSize="fileSize" @input='iconUpload'></file-upload >
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="内容">
          <Tinymce v-if="!contentDisabled" v-model="form.content" :uploadType="'CONTENT'" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :autosize="{ minRows: 5}" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import content from "./content.js";
  export default content;
</script>

<style>
 @import url("./content.css");
</style>
