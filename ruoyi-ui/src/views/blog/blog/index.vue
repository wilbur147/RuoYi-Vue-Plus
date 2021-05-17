<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分类" prop="blogSortId">
        <el-select v-model="queryParams.blogSortId" filterable clearable placeholder="请选择分类" size="small"
        >
          <el-option
            v-for="sort in sortList"
            :key="sort.blogSortId"
            :label="sort.sortName"
            :value="sort.blogSortId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="标签" prop="blogTagId">
        <el-select v-model="queryParams.blogTagId" filterable clearable  placeholder="请选择标签" size="small"
        >
          <el-option
            v-for="tag in tagList"
            :key="tag.blogTagId"
            :label="tag.tagName"
            :value="tag.blogTagId"
          />
        </el-select>
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
      <el-form-item label="是否原创" prop="isOriginal">
        <el-select v-model="queryParams.isOriginal" placeholder="请选择是否原创" clearable size="small">
          <el-option
            v-for="dict in isOriginalOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否发布" prop="isPublish">
        <el-select v-model="queryParams.isPublish" placeholder="请选择是否发布" clearable size="small">
          <el-option
            v-for="dict in isPublishOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否评论" prop="openComment">
        <el-select v-model="queryParams.openComment" placeholder="请选择是否评论" clearable size="small">
          <el-option
            v-for="dict in openCommentOptions"
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
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['blog:blog:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['blog:blog:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['blog:blog:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['blog:blog:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="blogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="类型" align="center" prop="blogId" v-if="false"/>
      <el-table-column label="封面图" width="160" align="center">
        <template slot-scope="scope">
          <el-image
            :src="scope.row.filePath"
            style="width: 130px;height: 70px;"
          />
        </template>
       </el-table-column>
      <el-table-column label="标题" align="center" width="160" prop="title" />
      <el-table-column label="分类" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.blogSort.sortName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="标签" width="200" align="center" >
        <template slot-scope="scope">
          <template>
            <el-tag
              style="margin-left: 3px; font-size: inherit;"
              type="warning"
              v-if="item"
              :key="index"
              v-for="(item, index) in scope.row.blogTags"
            >{{item.tagName}}</el-tag>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="是否原创" align="center" prop="isOriginal">
        <template slot-scope="scope">
          <el-tag
          style="font-size: inherit;"
          v-for="item in isOriginalOptions"
          :key="item.dictValue"
          v-if="scope.row.isOriginal == item.dictValue">{{item.dictLabel}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="发布状态" align="center" prop="isPublish">
        <template slot-scope="scope">
          <el-tag
          style="font-size: inherit;"
          v-for="item in isPublishOptions"
          :key="item.dictValue"
          :type="scope.row.isPublish === 'Y' ?'success' : 'danger'"
          v-if="scope.row.isPublish == item.dictValue">{{item.dictLabel}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="点击数" align="center" prop="clickCount" />
      <el-table-column label="评论" align="center" prop="openComment">
        <template slot-scope="scope">
          <el-tag
          style="font-size: inherit;"
          v-for="item in openCommentOptions"
          :key="item.dictValue"
          :type="scope.row.openComment === 'Y' ?'info' : 'danger'"
          v-if="scope.row.openComment == item.dictValue">{{item.dictLabel}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="50">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="medium"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['blog:blog:edit']"
          >修改</el-button>
          <el-button
            size="medium"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['blog:blog:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改博客文章对话框 -->
    <el-dialog :title="title" :visible.sync="open" fullscreen>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="16">
            <el-form-item label="标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入标题" />
            </el-form-item>
            <el-form-item label="简介" prop="summary">
              <el-input v-model="form.summary" placeholder="请输入简介" />
            </el-form-item>
            <el-row :gutter="80">
              <el-col :span="10">
                <el-form-item label="分类" prop="blogSortId">
                  <el-select v-model="form.blogSortId" filterable clearable placeholder="请选择分类"
                  style="width: 100%;"
                  >
                    <el-option
                      v-for="sort in sortList"
                      :key="sort.blogSortId"
                      :label="sort.sortName"
                      :value="sort.blogSortId"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item label="标签" prop="blogTagId">
                  <el-select v-model="form.blogTagId" filterable multiple clearable  placeholder="请选择标签"
                  style="width: 100%;"
                  >
                    <el-option
                      v-for="tag in tagList"
                      :key="tag.blogTagId"
                      :label="tag.tagName"
                      :value="tag.blogTagId"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="80">
              <el-col :span="10">
                <el-form-item label="作者" prop="author">
                  <el-input v-model="form.author" placeholder="请输入作者" />
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item label="文章出处" prop="articlesPart">
                  <el-input v-model="form.articlesPart" placeholder="请输入文章出处" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
          <el-col :span="8">
            <el-form-item label="封面图">
              <file-upload ref="fileUpload" :value="form.filePath" :params="{uploadType:'BLOG'}" :fileType="fileType" :fileSize="fileSize" @input='fileUpload'></file-upload >
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="4">
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
          <el-col :span="4">
            <el-form-item label="是否发布" prop="isPublish">
              <el-radio-group v-model="form.isPublish">
                <el-radio border
                  v-for="dict in isPublishOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="是否评论" prop="openComment">
              <el-radio-group v-model="form.openComment">
                <el-radio border
                  v-for="dict in openCommentOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="是否原创" prop="isOriginal">
              <el-radio-group v-model="form.isOriginal">
                <el-radio border
                  v-for="dict in isOriginalOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="4">
            <el-form-item label="类型" prop="type">
              <el-radio-group v-model="form.type">
                <el-radio border=""
                  v-for="dict in typeOptions"
                  :key="dict.dictValue"
                  :label="parseInt(dict.dictValue)"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="编辑器" prop="editorType">
              <el-radio-group v-model="form.editorType">
                <el-radio border=""
                  v-for="dict in editorTypeOptions"
                  :key="dict.dictValue"
                  :label="parseInt(dict.dictValue)"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item v-if="form.type == 1" label="外链" prop="outsideLink">
          <el-input v-model="form.outsideLink" placeholder="请输入外链" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <Tinymce v-if="!contentDisabled&&form.editorType===0" v-model="form.content" :uploadType="'BLOG'" />
          <TuiEditor v-if="!contentDisabled&&form.editorType===1" v-model="form.content" :uploadType="'BLOG'" />
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
  import blog from "./blog.js";
  export default blog;
</script>

<style>
 @import url("./blog.css");
</style>
