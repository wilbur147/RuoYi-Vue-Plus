<template>
  <div>
    <div id="tuiEditor"></div>
    <div class="text-secondary mt-2 p-2 d-block mb-2">
      <i class="el-icon-info"></i> 你可以在编辑器底部切换为markdown模式，编辑器也支持托放上传图片。
    </div>
  </div>
</template>

<script>
  import 'codemirror/lib/codemirror.css'
  import '@toast-ui/editor/dist/toastui-editor.css'
  import Editor from '@toast-ui/editor'
  // 导入中文语言包
  import '@toast-ui/editor/dist/i18n/zh-cn';
  // 引入语法高亮插件
  import 'highlight.js/styles/github.css';
  import codeSyntaxHighlight from '@toast-ui/editor-plugin-code-syntax-highlight';
  import hljs from 'highlight.js';
  // 引入字体颜色
  import 'tui-color-picker/dist/tui-color-picker.css';
  import colorSyntax from '@toast-ui/editor-plugin-color-syntax';
  // 引入表格合并
  import tableMergedCell from '@toast-ui/editor-plugin-table-merged-cell';
  //引入文件上传服务器的接口
  import { uploadFile } from "@/api/file/file";

  export default {
    props: {
      uploadType: {
        type: String,
        default: ''
      },
      //编辑器高度
      height: {
        type: String,
        default: '600px'
      },
      //显示方式
      previewStyle: {
        type: String,
        default: 'vertical'
      },
      initialEditType: {
        type: String,
        default: 'wysiwyg'
      }
    },
    data() {
      return {
        editor: null
      }
    },
    mounted() {
      this.initEditor()
      this.fullScreenEvent()
    },
    methods: {
      initEditor() {
        const that = this
        const editor = new Editor({
          el: document.querySelector('#tuiEditor'),
          previewStyle: this.previewStyle,
          initialValue: that.$attrs.value,
          initialEditType: this.initialEditType,
          plugins: [[codeSyntaxHighlight, { hljs }],colorSyntax,tableMergedCell],
          height: this.height,
          language: 'zh-CN',
          placeholder: '',
          events: {
            //监听编辑器输入
            change: function() {
              that.$emit('input', editor.getMarkdown())
            }
          },
          hooks: {
            async addImageBlobHook(blob, callback) {
              // 上传图片
              const formdate = new FormData()
              formdate.set('file',blob)
              formdate.set('uploadType',that.uploadType)
              const response = await uploadFile(formdate);
              if (response.code == 200) {
                //更改编辑器内容
                callback(response.data.fullFilePath, blob.name)
              }
              return false
            }
          },
          toolbarItems: this.toolbar()
        })
        this.editor = editor
      },
      //添加工具条按钮
      createButton(className) {
        const button = document.createElement('button')
        button.className = className
        button.innerHTML = `<i class="el-icon-full-screen" style="color:#666;font-weight: 1000;font-size: 14px;"></i>`
        return button
      },
      //添加全屏按钮事件
      fullScreenEvent() {
        const toolbar = this.editor.getUI().getToolbar()
        const cm = this.editor.mdEditor.cm
        //设置按钮点击事件
        this.editor.eventManager.addEventType('fullscreen')
        this.editor.eventManager.listen('fullscreen', () => {
          this.editor.previewStyle = 'vertical'
          //保存点击状态
          cm.setOption('fullScreen', !cm.getOption('fullScreen'))
          let ui = document.querySelector('.tui-editor-defaultUI')
          if (cm.getOption('fullScreen')) {
            ui.classList.add('fullScreen')
          } else {
            ui.classList.remove('fullScreen')
          }
        })
      },
      //自定义工具条
      toolbar() {
        return [{
            type: 'button',
            options: {
              el: this.createButton('last'),
              name: 'fullscreen',
              tooltip: 'fullscreen',
              event: 'fullscreen'
            }
          },
          'heading',
          'bold',
          'italic',
          'strike',
          'divider',
          'hr',
          'quote',
          'divider',
          'ul',
          'ol',
          'task',
          'indent',
          'outdent',
          'divider',
          'table',
          'image',
          'link',
          'divider',
          'code',
          'codeblock'
        ]
      }
    }
  }
</script>
<style lang="scss">
  // 事件按钮需要使用类所以不能加scoped
  .fullScreen {
    position: fixed !important;
    z-index: 999;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: #fff;
  }


</style>
