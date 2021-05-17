import { listContent, getContent, delContent, addContent, updateContent, exportContent, editContentStatus } from "@/api/content/content";
import { listCategroy } from "@/api/content/categroy";
import { listTopic } from "@/api/content/topic";


import InputTags from "@/components/InputTags";
import Tinymce from '@/components/Tinymce';
import FileUpload from '@/components/FileUpload-V1';
import TreeRender from '@/components/TreeRender';
export default {
  name: "Content",
  components: {
    Tinymce,
    FileUpload,
    InputTags,
    TreeRender
  },
  data() {
    return {
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
      // 内容表格数据
      contentList: [],
      // 内容专题数据
      topicList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 状态字典
      statusOptions: [],
      // 推荐字典
      isrecommendOptions: [],
      // 文件类型格式
      fileType: "IMG",
      // 文件大小 默认2
      fileSize: 2,
      // 文本显示
      contentDisabled: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        conTopicId: undefined,
        categroyId: undefined,
        contentName: undefined,
        status: undefined,
      },
      sortProps: {
        label: 'categroyName',
        value: 'categroyId',
        expandTrigger: 'hover',
        checkStrictly: true
      },
      /* 左边树形开始*/
      maxexpandId: null, //新增节点开始id
      non_maxexpandId: null, //新增节点开始id(不更改)
      isLoadingTree: false, //是否加载节点树
      setTree: [], //节点树数据
      defaultProps: {
        children: 'children',
        label: 'categroyName'
      },
      defaultExpandKeys: [], //默认展开节点列表
      defaultCheckedKey: 1, //默认选中节点
      /* 左边树形结束*/

      // 表单参数
      form: {},
      // 表单校验
      rules: {
        categroyId: [
          { required: true, message: "分类不能为空", trigger: "change" }
        ],
        conTopicId: [
          { required: true, message: "专题不能为空", trigger: "change" }
        ],
        contentName: [
          { required: true, message: "标题名称不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.initData();
    var dictTypeList = ['sys_normal_disable','recommend_status' ]
    const that = this;
    this.selectTypeList(dictTypeList).then(response => {
      if (response.code == 200) {
        var dictMap = response.data;
        // 状态
        that.statusOptions = dictMap.sys_normal_disable
        // 推荐
        that.isrecommendOptions = dictMap.recommend_status
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
      this.getCategroyList();
      // 专题列表
      listTopic({}).then(response => {
        this.topicList = response.rows;
      });
    },
    /** 查询内容列表 */
    getList() {
      this.queryParams.categroyId = this.defaultCheckedKey;
      this.loading = true;
      listContent(this.queryParams).then(response => {
        this.contentList = response.rows;
        this.contentList.map(data=>{
          if (data.keywords && data.keywords.indexOf(',') != -1) {
            data.keywords = data.keywords.split(",")
          }else{
            data.keywords = Array(data.keywords);
          }
        });
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
        contentId: undefined,
        categroyId: undefined,
        conTopicId: undefined,
        contentName: undefined,
        icon: undefined,
        banner: undefined,
        keywords: undefined,
        status: 0,
        orderNum: 0,
        recommend: 'Y',
        content: undefined,
        expand: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        remark: undefined
      };
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
      this.ids = selection.map(item => item.contentId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加内容";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const contentId = row.contentId || this.ids
      getContent(contentId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改内容";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.contentId != null) {
            updateContent(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addContent(this.form).then(response => {
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
      const contentIds = row.contentId || this.ids;
      this.$confirm('是否确认删除内容编号为"' + contentIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delContent(contentIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有内容数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportContent(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },
    /* banner上传*/
    bannerUpload(res){
      if (res) {
        this.form.banner = res.uniqueId;
        this.form.bannerPath = res.fullFilePath
      }else{
        this.form.banner = undefined;
        this.form.bannerPath = undefined;
      }
    },
    /* icon上传*/
    iconUpload(res){
      if (res) {
        this.form.icon = res.uniqueId;
        this.form.iconPath = res.fullFilePath
      }else{
        this.form.iconPath = undefined;
        this.form.iconPath = undefined;
      }
    },
    /* form分类选择*/
    sortHandleChange(value){
      if (value) {
        this.form.categroyId = value[value.length-1];
      }
    },
    /** 状态修改 */
    handleStatusChange(row) {
      let text = row.status === 0 ? "启用" : "停用";
      this.$confirm('确认要"' + text + '""' + row.contentName + '"吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          const paramsForm = {contentId: row.contentId,status: row.status}
          return editContentStatus(paramsForm);
        }).then(() => {
          this.msgSuccess(text + "成功");
        }).catch(function() {
          row.status = row.status === 0 ? 1 : 0;
        });
    },
    /* 左边树形开始*/

    /** 查询分类列表 */
    getCategroyList() {
      const that = this;
      listCategroy({}).then(response => {
        const resArr = response.rows;
        if (resArr) {
          this.maxexpandId = resArr[resArr.length - 1].categroyId;
          this.non_maxexpandId = resArr[resArr.length - 1].categroyId;
        }
        this.setTree = this.handleTree(resArr, "categroyId");
        this.loading = false;
        that.initExpand();
      });
    },
    initExpand() {
      this.setTree.map((a, i) => {
        if (i == 0) {
          this.defaultCheckedKey = a.categroyId;
        }
        this.defaultExpandKeys.push(a.categroyId);
      });
      this.isLoadingTree = true;
      this.deptTree();
    },
    /* 默认选中一个节点*/
    deptTree() {
      if (this.defaultCheckedKey) {
        this.$nextTick(() => {
          document.querySelector(".is-current").firstChild.click();
        });
      }
    },
    /* 点击节点*/
    handleNodeClick(d, n, s) {
      // 放弃编辑状态
      d.isEdit = false;
      // 查询对应分类的右侧内容
      this.defaultCheckedKey = d.categroyId;
      this.getList();
    },
    renderContent(h, {
      node,
      data,
      store
    }) { //加载节点
      let that = this;
      return h(TreeRender, {
        props: {
          DATA: data,
          NODE: node,
          STORE: store,
          maxexpandId: that.non_maxexpandId
        },
        on: {
          nodeAdd: ((s, d, n) => that.addTree(s, d, n)),
          nodeEdit: ((s, d, n) => that.editTree(s, d, n)),
          nodeDel: ((s, d, n) => that.deleteTree(s, d, n))
        }
      });
    },
    /* 添加顶级节点*/
    addTreeTop() {
      this.setTree.push({
        id: ++this.maxexpandId,
        name: '新增节点',
        pid: '',
        isEdit: false,
        children: []
      })
    },
    /* 增加节点*/
    addTree(s, d, n) {
      console.log(s, d, n)
      if (n.level >= 6) {
        this.$message.error("最多只支持五级！")
        return false;
      }
      //添加数据
      d.children.push({
        id: ++this.maxexpandId,
        name: '新增节点',
        pid: d.id,
        isEdit: false,
        children: []
      });
      //展开节点
      if (!n.expanded) {
        n.expanded = true;
      }
    },
    editTree(s, d, n) { //编辑节点
      console.log(s, d, n)
    },
    deleteTree(s, d, n) { //删除节点
      console.log(s, d, n)
      let that = this;
      //有子级不删除
      if (d.children && d.children.length !== 0) {
        this.$message.error("此节点有子级，不可删除！")
        return false;
      } else {
        //新增节点直接删除，否则要询问是否删除
        let delNode = () => {
          let list = n.parent.data.children || n.parent.data, //节点同级数据
            _index = 99999; //要删除的index
          /*if(!n.parent.data.children){//删除顶级节点，无children
            list = n.parent.data
          }*/
          list.map((c, i) => {
            if (d.id == c.id) {
              _index = i;
            }
          })
          let k = list.splice(_index, 1);
          //console.log(_index,k)
          this.$message.success("删除成功！")
        }
        let isDel = () => {
          that.$confirm("是否删除此节点？", "提示", {
            confirmButtonText: "确认",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            delNode()
          }).catch(() => {
            return false;
          })
        }
        //判断是否新增
        d.id > this.non_maxexpandId ? delNode() : isDel()

      }
    },

    /* 左边树形结束*/
  }
};
