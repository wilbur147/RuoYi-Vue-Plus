<template>
  <div class="tinymce-box">
    <editor
      v-model="myValue"
      :init="init"
      :disabled="disabled"
      @click="onClick">
    </editor>
  </div>
</template>

<script>
//引入文件上传服务器的接口
import { uploadFile } from "@/api/file/file";
import tinymce from 'tinymce/tinymce'
import Editor from '@tinymce/tinymce-vue'
import 'tinymce/themes/silver'
//引入工具栏图标
import 'tinymce/icons/default'

// 编辑器插件plugins
// 更多插件参考：https://www.tiny.cloud/docs/plugins/

import 'tinymce/plugins/lists'// 列表插件
import 'tinymce/plugins/link'
import 'tinymce/plugins/wordcount'
import 'tinymce/plugins/image'// 插入上传图片插件
import 'tinymce/plugins/media'// 插入视频插件
import 'tinymce/plugins/table'// 插入表格插件
import 'tinymce/plugins/code'
import 'tinymce/plugins/preview'
import 'tinymce/plugins/fullscreen'


export default {
  components: {Editor},
  name: 'Tinymce',
  props: {
    // 默认的富文本内容
    value: {
      type: String,
      default: ''
    },
    // 禁用
    disabled: {
      type: Boolean,
      default: false
    },
    uploadType: {
      type: String,
      default: ''
    },
    videoSuffix: {
      type: Array,
      default: ()=>['rm','rmvb','wmv','avi','mpg','mpeg','mp4']
    },
    videoSize: {
      type: Number,
      default: 10
    },
    plugins: {
      type: [String, Array],
      default: 'link lists image code table media preview fullscreen wordcount '
    },
    toolbar: {
      type: [String, Array],
      default: 'bold italic underline strikethrough | undo redo | forecolor backcolor | formatselect | fontselect  fontsizeselect | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent blockquote | link unlink code table image media | removeformat | fullscreen preview'
    }
  },
  data () {
    return {
      myValue: this.value,
      resVideo:'',    //上传视频的url
      mediaUploaded:false,//有没有上传完成
      init: {
        language_url: "/tinymce/langs/zh_CN.js", // 语言包的路径
        language: 'zh_CN', // 语言
        skin_url: "/tinymce/skins/ui/oxide", // skin路径
        height: 600, // 编辑器高度
        convert_urls: false,
        statusbar: true, // 底部的状态栏
        branding: false, // 去水印
        menubar: false, // 隐藏最上方menu
        plugins: this.plugins, // （指定需加载的插件）
        toolbar: this.toolbar, // （自定义工具栏）
        content_style: "p {margin: 5px 0;}",
        fontsize_formats: "12px 14px 16px 18px 24px 36px 48px 56px 72px",
        font_formats:
          "微软雅黑=Microsoft YaHei,Helvetica Neue,PingFang SC,sans-serif;苹果苹方=PingFang SC,Microsoft YaHei,sans-serif;宋体=simsun,serif;仿宋体=FangSong,serif;黑体=SimHei,sans-serif;Arial=arial,helvetica,sans-serif;Arial Black=arial black,avant garde;Book Antiqua=book antiqua,palatino;",
        file_picker_types: 'media',
        images_upload_handler: (blobInfo, success, failure) => {
          this.handleImageAdded(blobInfo, success, failure)
        },
        //be used to add custom file picker to those dialogs that have it.
        file_picker_callback: (callback, value, meta) => {
           this.filePicker(callback, value, meta)
        },
        //自定义逻辑替换 Tinymce 的默认媒体嵌入逻辑
        media_url_resolver: function (data, resolve) {
            try {
                let videoUri = encodeURI(data.url);
                let embedHtml = `<p>
                    <span
                        class="mce-object mce-object-video"
                        data-mce-selected="1"
                        data-mce-object="video"
                        data-mce-p-width="35%"
                        data-mce-p-height="auto"
                        data-mce-p-controls="controls"
                        data-mce-p-controlslist="nodownload"
                        data-mce-p-allowfullscreen="true"
                        data-mce-p-src=${videoUri} >
                        <video src=${data.url} width="35%" height="auto" controls="controls" controlslist="nodownload">
                        </video>
                    </span>
                </p>
                <p style="text-align: left;"></p>`;
                resolve({ html: embedHtml });
            } catch (e) {
                resolve({ html: "" });
            }
        }

      }
    }
  },
  mounted () {
    tinymce.init({})
  },
  methods: {
    // 添加相关的事件，可用的事件参照文档=> https://github.com/tinymce/tinymce-vue => All available events
    // 需要什么事件可以自己增加
    onClick (e) {
      this.$emit('onClick', e, tinymce)
    },
    filePicker(callback, value, meta){
      if (meta.filetype == 'media') {
            let input = document.createElement('input');//创建一个隐藏的input
            input.setAttribute('type', 'file');
            let that = this;
            input.onchange = async function(){
                let file = this.files[0];//选取第一个文件
                const mediaUploaded = await that.uploadVideo(file); // 上传视频拿到url
                if(mediaUploaded){
                        callback(that.resVideo) //将url显示在弹框输入框中
                      }else{
                    setTimeout(()=>{
                        //设置几秒后再去取数据
                        callback(this.resVideo)
                    },5000)
                }
            }
            //触发点击
            input.click();
        }
    },
    // 插入视频的方法
    async uploadVideo(file){
      const boo = await this.validVideo(file);
      return new Promise((resolve,reject)=>{
        if (!boo) reject(false);
        const formdate = new FormData()
        formdate.set('file',file)
        formdate.set('uploadType',this.uploadType)
        uploadFile(formdate).then(response =>{
          if (response.code == 200) {
            this.resVideo = response.data.fullFilePath
            this.mediaUploaded = true
            resolve(true)
          }
        })
      })
    },
    // 插入图片的方法
    handleImageAdded(blobInfo, success, failure) {
      let file = blobInfo.blob()
      const isLt8M = file.size / 1024 / 1024 < 5
      if (!isLt8M) {
        this.$message.error('图片大小不能超过 5MB!')
        return
      }
     const formdate = new FormData()
     formdate.set('file',blobInfo.blob())
     formdate.set('uploadType',this.uploadType)
     uploadFile(formdate).then(response =>{
      if (response.code == 200) {
            let url = response.data.fullFilePath
            success(url)
          } else {
            failure(response.message)
            this.$message.error(response.message)
          }
     })
    },
    async validVideo(file){
      return new Promise((resolve,reject)=>{
        // 校检文件类型
        if (this.videoSuffix) {
          let fileExtension = "";
          if (file.name.lastIndexOf(".") > -1) {
            fileExtension = file.name.slice(file.name.lastIndexOf(".") + 1);
          }
          const isTypeOk = this.videoSuffix.some((type) => {
            if (file.type.indexOf(type) > -1) return true;
            if (fileExtension && fileExtension.indexOf(type) > -1) return true;
            return false;
          });
          if (!isTypeOk) {
            this.$message.error(`视频格式不正确, 请上传${this.videoSuffix.join("/")}格式视频!`);
            return reject(false);
          }
        }
        // 校检文件大小
        if (this.videoSize) {
          const isLt = file.size / 1024 / 1024 < this.videoSize;
          if (!isLt) {
            this.$message.error(`上传视频大小不能超过 ${this.videoSize} MB!`);
            return reject(false);
          }
        }
        return resolve(true);
      })
    }
  },
  watch: {
    value (newValue) {
      this.myValue = newValue
    },
    myValue (newValue) {
      this.$emit('input', newValue)
    }
  }
}
</script>

<style>
    /* 解决富文本弹窗在*/
    .tox-tinymce-aux {
      z-index: 5000 !important;
    }
</style>
