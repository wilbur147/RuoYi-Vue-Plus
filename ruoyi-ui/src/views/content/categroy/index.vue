<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="分类名称" prop="categroyName">
        <el-input
          v-model="queryParams.categroyName"
          placeholder="请输入分类名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
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
          v-hasPermi="['content:categroy:add']"
        >新增</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="categroyList"
      row-key="categroyId"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column label="名称" align="left"  prop="categroyName" />
      <el-table-column label="图标"  align="center">
        <template slot-scope="scope">
          <el-image
            :src="scope.row.filePath"
            style="width: 130px;height: 70px;"
          />
        </template>
       </el-table-column>
      <el-table-column label="排序" width="60" align="center" prop="orderNum" />
      <el-table-column label="创建时间" align="center" prop="createTime"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['content:categroy:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['system:menu:add']"
          >新增</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['content:categroy:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改分类对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级菜单">
              <treeselect
                v-model="form.parentId"
                :options="treeOptions"
                :normalizer="normalizer"
                :show-count="true"
                placeholder="选择上级菜单"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="类别名称" prop="categroyName">
              <el-input v-model="form.categroyName" placeholder="请输入类别名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="类别图标" prop="icon">
              <!-- <el-input v-model="form.icon" placeholder="请输入类别图标" /> -->
              <file-upload ref="fileUpload" :value="form.filePath" :params="{uploadType:'CONTENT'}" :fileType="fileType" :fileSize="fileSize" @input='fileUpload'></file-upload >
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <el-input v-model="form.description" :rows="5" type="textarea"  placeholder="请输入描述" />
            </el-form-item>
          </el-col>
         <el-col :span="24">
             <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" :rows="5" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCategroy, getCategroy, delCategroy, addCategroy, updateCategroy, exportCategroy } from "@/api/content/categroy";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import FileUpload from '@/components/FileUpload-V1';
export default {
  name: "Categroy",
  components: {
    FileUpload,Treeselect },
  data() {
    return {
      // 分类树选项
      treeOptions: [],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 分类表格数据
      categroyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 文件类型格式
      fileType: "IMG",
      // 文件大小 默认2
      fileSize: 2,
      // 查询参数
      queryParams: {
        categroyName: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        categroyName: [
          { required: true, message: "类别名称不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询分类列表 */
    getList() {
      this.loading = true;
      listCategroy(this.queryParams).then(response => {
        this.categroyList = this.handleTree(response.rows, "categroyId");
        this.loading = false;
      });
    },
     /** 转换菜单数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.categroyId,
        label: node.categroyName,
        children: node.children
      };
    },
    /** 查询菜单下拉树结构 */
    getTreeselect() {
      listCategroy().then(response => {
        this.treeOptions = [];
        const menu = { categroyId: 0, categroyName: '主类目', children: [] };
        menu.children = this.handleTree(response.rows, "categroyId");
        this.treeOptions.push(menu);
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        categroyId: null,
        parentId: 0,
        categroyName: null,
        icon: null,
        orderNum: 0,
        description: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        filePath: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.categroyId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      this.getTreeselect();
      if (row != null && row.categroyId) {
        this.form.parentId = row.categroyId;
      } else {
        this.form.parentId = 0;
      }
      this.open = true;
      this.title = "添加分类";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      getCategroy(row.categroyId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改分类";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.categroyId != null) {
            updateCategroy(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCategroy(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const categroyIds = row.categroyId || this.ids;
      this.$confirm('是否确认删除"' + row.categroyName + '"?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delCategroy(categroyIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /* 文件上传*/
    fileUpload(res){
      if (res) {
        this.form.icon = res.uniqueId;
        this.form.filePath = res.fullFilePath
      }else{
        this.form.icon = undefined;
        this.form.filePath = undefined;
      }
    },
  }
};
</script>
