import { listBlog, getBlog, delBlog, addBlog, updateBlog, exportBlog } from "@/api/blog/blog";
import { listSort } from "@/api/blog/sort";
import { listTag } from "@/api/blog/tag";


import FileUpload from '@/components/FileUpload-V1';
import Tinymce from '@/components/Tinymce';
import TuiEditor from '@/components/TuiEditor';

export default {
  name: "Blog",
  components: {
    FileUpload,
    Tinymce,
    TuiEditor
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedRyBlogTag: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 博客数据
      blogList: [],
      // 博客分类数据
      sortList: [],
      // 博客标签数据
      tagList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 状态字典
      statusOptions: [],
      // 是否原创字典
      isOriginalOptions: [],
      // 是否发布字典
      isPublishOptions: [],
      // 是否评论字典
      openCommentOptions: [],
      // 类型字典
      typeOptions: [],
      // 编辑器类型字典
      editorTypeOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        blogTagId: undefined,
        blogSortId: undefined,
        status: undefined,
        isOriginal: undefined,
        isPublish: undefined,
        openComment: undefined,
      },
      // 表单参数
      form: {},
      // 文件类型格式
      fileType: "IMG",
      // 文件大小 默认2
      fileSize: 2,
      // 文本显示
      contentDisabled: false,
      // 表单校验
      rules: {
        title: [
          { required: true, message: "标题不能为空", trigger: "blur" },
          { max: 200, message: "请输入标题最大长度不超过200位", trigger: "blur" }
        ],
        blogSortId: [
          { required: true, message: "分类不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "blur" }
        ],
        sort: [
          { required: true, message: "排序不能为空", trigger: "blur" }
        ],
        isPublish: [
         {required: true, message: '发布不能为空', trigger: 'blur'},
        ],
        isOriginal: [
          {required: true, message: '原创不能为空', trigger: 'blur'},
        ],
        openComment: [
          {required: true, message: '评论不能为空', trigger: 'blur'},
        ],
        type: [
          { required: true, message: "类型不能为空", trigger: "blur" }
        ],
        editorType: [
          { required: true, message: "编辑器类型不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "内容不能为空", trigger: "blur" }
        ],
        outsideLink: [
          {required: true, message: '外链地址不能为空', trigger: 'blur'},
          {pattern:  /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/, message: '请输入有效的URL'},
        ],
      }
    };
  },
  created() {
    this.initData();
    this.getList();
    var dictTypeList = ['sys_normal_disable','original_status', 'publish_status', 'open_comment_status', 'article_type', 'editor_type'
    ]
    const that = this;
    this.selectTypeList(dictTypeList).then(response => {
      if (response.code == 200) {
        var dictMap = response.data;
        // 状态
        that.statusOptions = dictMap.sys_normal_disable
        // 原创
        that.isOriginalOptions = dictMap.original_status
        // 发布
        that.isPublishOptions = dictMap.publish_status
        // 评论
        that.openCommentOptions = dictMap.open_comment_status
        // 类型
        that.typeOptions = dictMap.article_type
        // 编辑器类型
        that.editorTypeOptions = dictMap.editor_type
      }
    });
  },
  watch: {
    open: function(val){
      this.contentDisabled = !val;
    }
  },
  methods: {
    /* 初始化数据*/
    initData() {
      // 分类列表
      listSort({status: '0'}).then(response => {
        this.sortList = response.rows;
      });
      // 标签列表
      listTag({status: '0'}).then(response => {
        this.tagList = response.rows;
      });
    },
    /** 查询博客文章列表 */
    getList() {
      this.loading = true;
      listBlog(this.queryParams).then(response => {
        this.blogList = response.rows;
        this.blogList.map(data=>data.status=String(data.status))
        this.total = response.total;
        this.loading = false;
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
        blogId: undefined,
        title: undefined,
        summary: undefined,
        content: undefined,
        blogTagId: undefined,
        blogSortId: undefined,
        clickCount: undefined,
        fileUniqueId: undefined,
        status: 0,
        author: undefined,
        articlesPart: undefined,
        isOriginal: "Y",
        isPublish: "Y",
        sort: 0,
        openComment: "Y",
        type: 0,
        outsideLink: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        filePath: undefined,
        editorType: 0
      };
      this.ryBlogTagList = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.blogId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加博客文章";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const blogId = row.blogId || this.ids
      getBlog(blogId).then(response => {
        this.form = response.data;
        if (this.form.blogTagId) {
          this.form.blogTagId = this.form.blogTagId.split(",").map(e=> parseInt(e));
        }
        this.form.blogSortId = parseInt(this.form.blogSortId)
        this.open = true;
        this.title = "修改博客文章";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.blogTagId = this.form.blogTagId.join(",");
          if (this.form.blogId != null) {
            updateBlog(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBlog(this.form).then(response => {
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
      const blogIds = row.blogId || this.ids;
      this.$confirm('是否确认删除博客文章编号为"' + blogIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delBlog(blogIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有博客文章数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportBlog(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },
    /* 文件上传*/
    fileUpload(res){
      if (res) {
        this.form.fileUniqueId = res.uniqueId;
        this.form.filePath = res.fullFilePath
      }else{
        this.form.fileUniqueId = undefined;
        this.form.filePath = undefined;
      }
    },
    /** 状态修改 */
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$confirm('确认要"' + text + '""' + row.title + '"吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          const paramsForm = {blogId: row.blogId,status: row.status}
          return updateBlog(paramsForm);
        }).then(() => {
          this.msgSuccess(text + "成功");
        }).catch(function() {
          row.status = row.status === "0" ? "1" : "0";
        });
    },
  }
};
