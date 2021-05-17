import {
  listResources,
  getResources,
  delResources,
  addResources,
  updateResources,
  downloadResources
} from "@/api/material/resources";
import { listGroup} from "@/api/material/group";
import { delFile} from "@/api/file/file";
import FileUpload from '@/components/FileUpload-V1';

export default {
  name: "Resources",
  components: {
    FileUpload,
  },
  data() {
    return {
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
      // 素材资源数据
      resourcesList: [],
      // 素材分组数据
      groupList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 资源类型字典
      resourceTypeOptions: [],
      // 是否有效字典
      isVoidOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 8,
        materialGroupId: undefined,
        resourceName: undefined,
        resourceType: undefined,
        isVoid: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        filePath: [{
          required: true,
          message: "文件资源不能为空",
          trigger: "change"
        }],
        resourceName: [{
          required: true,
          message: "资源名称不能为空",
          trigger: "change"
        }],
      },
      // ===================================================================================

      // 资源类型选中值
      resourceTypeActiveName: undefined,
      resourceTypeLable: '图片',
      // 分组选中值
      groupActiveName: undefined,
      // 文件类型格式
      fileType: "IMG",
      // 文件大小 默认2
      fileSize: 2,
      // 组别ID
      materialGroupId: undefined,
      // 文件内容预览框显示
      dialogFileVisible: false,
      dialogFileUrl: undefined,
    };
  },
  created() {
    this.initData();
  },
  methods: {
    /** 初始化值 */
    initData() {
      this.getDicts("resource_type_dict").then(response => {
        const data = response.data;
        if (data) {
          this.resourceTypeOptions = data;
          this.resourceTypeActiveName = data[0].dictValue;
          this.getGroupList();
        }
      });
      this.getDicts("sys_yes_no").then(response => {
        this.isVoidOptions = response.data;
      });
    },
    /* 查询分组列表*/
    getGroupList(){
      listGroup().then(response => {
        this.groupList = response.rows;
        if (this.groupList && this.groupList.length > 0) {
          this.materialGroupId = String(this.groupList[0].materialGroupId);
          this.getRescourcesList();
        }
      });
    },
    /** 查询素材资源列表 */
    getRescourcesList() {
      this.loading = this.$loading({lock: true,text: "加载中",background: "rgba(0, 0, 0, 0.7)",});
      this.queryParams.resourceType = this.resourceTypeActiveName;
      this.queryParams.materialGroupId = this.materialGroupId;
      listResources(this.queryParams).then(response => {
        this.resourcesList = response.rows;
        this.resourcesList.map(data=>data.isVoid=='Y'?true:false)
        this.total = response.total;
        this.loading.close();
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
        materialResourcesId: undefined,
        fileUniqueId: undefined,
        resourceName: undefined,
        resourceType: undefined,
        summary: undefined,
        isVoid: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        remark: undefined,
        filePath: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getRescourcesList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加素材资源";
    },
    /** 修改按钮操作 */
    handleUpdate(e) {
      this.reset();
      const materialResourcesId = e.materialResourcesId;
      getResources(materialResourcesId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改素材资源";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.resourceType = this.resourceTypeActiveName;
          this.form.materialGroupId = this.materialGroupId;
          if (this.form.materialResourcesId != null) {
            updateResources(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getRescourcesList();
            });
          } else {
            addResources(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getRescourcesList();
            });
          }
        }
      });
    },
    /* 选择*/
    handleChecked(e){
      // 保存ids
      const index = this.ids.indexOf(e.materialResourcesId);
      if (index === -1) {
        // 不存在,则添加
        this.ids.push(e.materialResourcesId)
      } else {
        // 存在,则删除
        this.ids.splice(index, 1)
      }
      this.single = this.ids.length !== 1
      this.multiple = !this.ids.length
    },
    /* 预览文件内容*/
    handlePreview(url){
        this.dialogFileVisible = true;
        this.dialogFileUrl = url;
    },
    /* 赋值剪贴板*/
    handleCopyUrl(text){
      this.copyText(text);
      this.$notify({title: '成功',message: '复制链接到剪切板',type: 'success', offset: 150});
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const that = this;
      const materialResourcesIds = row.materialResourcesId || this.ids;
      this.$confirm('是否确认删除素材资源编号为"' + materialResourcesIds + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delResources(materialResourcesIds);
      }).then(function() {
        // 如果不需要删除文件本身存储，注释即可
        const fileUniqueIds = that.resourcesList.map(data=>data.fileUniqueId)
        return delFile(fileUniqueIds);
      }).then(() => {
        this.getRescourcesList();
        this.msgSuccess("删除成功");
      })
    },
    /** 下载文件操作 */
    handleDownload() {
      const queryParams = this.ids ? this.ids.join(",") : "";
      this.$confirm('是否确认下载所有素材资源数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return downloadResources(queryParams);
      }).then(response => {
         this.downloadFile(response.msg);
      })
    },
    /* 更改有效状态*/
    handleIsVoid(e,materialResourcesId){
      if (materialResourcesId) {
        const upForm = {
          materialResourcesId: materialResourcesId,
          isVoid: e
        }
        updateResources(upForm).then(response => {
          if (response && response.code == 200) {
            this.msgSuccess("修改状态成功");
            this.getRescourcesList();
          }
        });
      }
    },
    /* 资源类型切换*/
    resourceTypeClick(e) {
      this.resourceTypeActiveName = e.name;
      this.switchResourceTypeName();
      this.handleQuery();
    },
    /* 分组类别切换*/
    materialGroupClick(e) {
      this.materialGroupId = e.name;
      this.switchResourceTypeName();
      this.handleQuery();
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
    /* 根据资源类型*/
    switchResourceTypeName(){
      this.resourcesList = [];
      this.total = 0;
      switch(this.resourceTypeActiveName){
        case 'GRAPHIC':
        case 'IMG':
        this.resourceTypeLable = '图片';
        this.fileType = "IMG";
        this.fileSize = 2;
        break;
        case 'VIDEO':
        this.resourceTypeLable = '视频';
        this.fileType = "VIDEO";
        this.fileSize = 10;
        break;
        case 'AUDIO':
        this.resourceTypeLable = '音频';
        this.fileType = "AUDIO";
        this.fileSize = 5;
        break;
        default:
      }
    },
  }
};
