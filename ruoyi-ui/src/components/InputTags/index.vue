<template>
    <div class="tags_input">
      <el-tag
        class="tag-word"
        v-for="(tag,index) in list"
        :key="index"
        closable
        :disable-transitions="false"
        @close="handleClose(tag)">
        {{tag}}
      </el-tag>
      <el-input
          class="tag-new-input"
          type="text"
          v-if="inputVisible"
          v-model="inputText"
          ref="saveTagInput"
          size="small"
          :placeholder="placeholder"
          @keyup.enter.native="add"
          @blur="add">
      </el-input>
      <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>
    </div>
</template>
<script>
    export default {
        name:'InputTags',
        props: {
            value: String,
            // 是否去重
            listFilter: {
              type: Boolean,
              default: false
            },
            // 占位符
            placeholder: {
              type: String,
              default: ""
            }
        },
        data() {
            return {
                list: [],
                inputText: '',
                inputVisible: false
            }
        },
        watch:{
        },
        mounted() {
            this.setList(this.value)
            this.$emit('input', this.list.join(","))  // 发送给父级的tagslist
        },
        methods: {
            add() {
                let self = this
                if (self.inputText != '' || self.inputText != ' ') {
                    this.setList(self.inputText)
                    if (self.listFilter) {
                        let arr = self.list
                        let set = new Set(arr)
                        self.list = Array.from(set)
                    }
                  self.inputText = '';
                  self.$emit('input', self.list.join(","))
                }
                self.inputVisible = false;
            },
            handleClose(tag) {
                this.list.splice(this.list.indexOf(tag), 1);
                this.$emit('input', this.list.join(","))
            },
            showInput() {
              this.inputVisible = true;
              this.$nextTick(_ => {
                this.$refs.saveTagInput.$refs.input.focus();
              });
            },
            setList(val){
              if (val != undefined && val.trim() != '') {
                if (val.indexOf(',') != -1) {
                  let arr = val.split(",");
                  this.list = [...this.list,...arr];
                }else{
                  this.list.push(val);
                }
              }
            }

        }
    }
</script>

<style>
 .tags_input{
    background-color: #fff;
    background-image: none;
    border-radius: 4px;
    border: 1px solid #dcdfe6;
    box-sizing: border-box;
    color: #606266;
    display: inline-block;
    outline: none;
    padding: 0 10px 0 5px;
    transition: border-color .2s cubic-bezier(.645,.045,.355,1);
    width: 100%;
    height: auto;
  }
  .tag-word{
    margin-right: 4px;
  }
  .tag-new-input{
    width: 90px;
    margin-left: 4px;
    vertical-align: bottom;
  }
  .button-new-tag {
    margin-left: 4px;
    height: 30px;
    line-height: 28px;
    padding-top: 0;
    padding-bottom: 0;
  }
</style>
