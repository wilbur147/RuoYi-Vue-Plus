<template>
  <div class="component-upload-image">
    <el-upload
      :action="uploadImgUrl"
      :on-success="handleUploadSuccess"
      :before-upload="handleBeforeUpload"
      :on-error="handleUploadError"
      name="file"
      :show-file-list="false"
      :headers="headers"
      :data="params"
      style="display: inline-block; vertical-align: top"
      drag
      multiple>
      <div v-if="!value">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      </div>
      <div v-else class="mask-div">
        <el-image v-if="'IMG' == fileType"
          :src="value"
          :style="`width:358px;`"
          fit="scale-down"
        />
        <video v-else-if="'VIDEO' == fileType"
               :src="value"
               class="avatar video-avatar"
               controls="controls"
               controlslist="nodownload"
               style="width: 358px;">
            您的浏览器不支持视频播放
        </video>
        <audio v-else-if="'AUDIO' == fileType"
        :src="value"
        style="width: 358px; margin-top: 18%;"
        controls preload loop controlsList="nodownload"
        />
        <div class="mask">
          <div class="actions">
            <span v-if="'IMG' == fileType" title="预览" @click.stop="dialogVisible = true">
              <i class="el-icon-zoom-in" />
            </span>
            <span title="移除" @click.stop="removeImage">
              <i class="el-icon-delete" />
            </span>
          </div>
        </div>
      </div>

    <!-- 上传提示 -->
    <div class="el-upload__tip" slot="tip" v-if="showTip">
      请上传
      <template v-if="fileSize"> 大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB</b> </template>
      <template v-if="fileType"> 格式为 <b style="color: #f56c6c">{{ fileSuffix.join("/") }}</b> </template>
      的文件
    </div>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible" title="预览" width="800" append-to-body>
      <img :src="value" style="display: block; max-width: 100%; margin: 0 auto;">
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth";
import { getFileInfo } from "@/api/file/file";
export default {
  data() {
    return {
      dialogVisible: false,
      fileSuffix: [],
      uploadImgUrl: process.env.VUE_APP_BASE_API + "/fileUploader/upload", // 上传的图片服务器地址
      headers: {
        Authorization: "Bearer " + getToken(),
      },
    };
  },
  props: {
    value: {
      type: String,
      default: "",
    },
    // 大小限制(MB)
    fileSize: {
      type: Number,
      default: 5,
    },
    // 文件类型 (图片 IMG  视频 VIDEO  音频 AUDIO)
    fileType: {
      type: String,
      default: "IMG",
    },
    // 调用接口的其它参数
    params: {
      type: Object,
      default: null,
    },
    // 是否显示提示
    isShowTip: {
      type: Boolean,
      default: true
    },
  },
  computed: {
    // 是否显示提示
    showTip() {
      return this.isShowTip && (this.fileType || this.fileSize);
    },
  },
  /* 子组件监听父组件值的变化*/
  watch: {
    fileType: {
      handler(newVal, oldVal) {
          this.suffixList()
      },
      deep: true
    }
  },
  methods: {
    removeImage() {
      this.$emit("input", "");
    },
    handleUploadSuccess(res) {
      if (res.code == 200) {
        getFileInfo(res.data.uniqueId).then(response => {
          this.$emit("input", response.data);
        });
      }else{
        this.$message.error('上传失败')
      }
       this.loading.close();
    },
    handleBeforeUpload(file) {
       // 校检文件类型
       if (this.fileSuffix) {
         let fileExtension = "";
         if (file.name.lastIndexOf(".") > -1) {
           fileExtension = file.name.slice(file.name.lastIndexOf(".") + 1);
         }
         const isTypeOk = this.fileSuffix.some((type) => {
           if (file.type.indexOf(type) > -1) return true;
           if (fileExtension && fileExtension.indexOf(type) > -1) return true;
           return false;
         });
         if (!isTypeOk) {
           this.$message.error(`文件格式不正确, 请上传${this.fileSuffix.join("/")}格式文件!`);
           return false;
         }
       }
       // 校检文件大小
       if (this.fileSize) {
         const isLt = file.size / 1024 / 1024 < this.fileSize;
         if (!isLt) {
           this.$message.error(`上传文件大小不能超过 ${this.fileSize} MB!`);
           return false;
         }
       }
      this.loading = this.$loading({
        lock: true,
        text: "上传中",
        background: "rgba(0, 0, 0, 0.7)",
      });
      return true;
    },
    handleUploadError() {
      this.$message.error('上传失败')
      this.loading.close();
    },
    suffixList() {
       switch(this.fileType){
         case 'IMG':
         this.fileSuffix =  ['jpeg','jpg','png','gif','bmp'];
         break;
         case 'VIDEO':
         this.fileSuffix =  ['rm','rmvb','wmv','avi','mpg','mpeg','mp4'];
         break;
         case 'AUDIO':
         this.fileSuffix =  ['wav','mp3','ogg'];
         break;
         default:
         this.fileSuffix =  ['jpg','png','jpeg']
       }
    },
  },
  created() {
    this.suffixList()
  },
};
</script>

<style scoped lang="scss">
.mask-div {
  position: relative;
  .mask {
    opacity: 0;
    position: absolute;
    top: 0;
    width: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    transition: all 0.3s;
  }
  &:hover .mask {
    opacity: 1;
  }
}
</style>
