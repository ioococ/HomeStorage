<template>
  <div style="margin-top: 5px;">
    <!-- 此处是面包屑 -->

    <div style="float: left;">

      <el-breadcrumb style="padding: 5px;" separator="/">
        <el-breadcrumb-item :to="{ path: '/storage' }">全部文件</el-breadcrumb-item>
        <el-breadcrumb-item v-for="(item,index) in list" :to="{path:'/storage?parentName='+pathList[index]+''}">{{item}}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <el-dialog title="" :visible.sync="perentDialog" width="30%" center>
      <p>{{fileName}}</p>
      <el-progress :percentage="perent" :format="format"></el-progress>
    </el-dialog>

    <div style="margin-bottom: 10px;float: right;margin-right: 1.25rem;display: -webkit-box;">

      <!--      <el-button @click="upload">上传</el-button>
 -->
      <div>
        <el-upload style="margin-right: 10px;" :with-credentials="true" :data="form" name="upload" ref="upload" action="/api/disk/uploadFile"
          :on-change="upload" :auto-upload="false">
          <el-button slot="trigger" size="small" type="primary">上传文件</el-button>
        </el-upload>
      </div>

      <!-- <div class="createFolder"> -->
      <el-button @click="createFolder" type="primary" size="small">创建文件夹</el-button>


    </div>
  </div>

</template>
<script>
  import SparkMD5 from 'spark-md5'

  export default {
    inject: ['reload'],
    data() {
      return {

        userName: '',
        parentName: this.$route.query.parentName,
        uploadDialog: false,
        backButton: true,
        perent: 0,
        form: {
          parentName: ''
        },
        magnet: {
          dialog: false
        },
        activceList: [],
        stoppedList: [],
        list: [],
        pathList: [],
        imgUrl: require("../assets/logo_1.png"),
        perentDialog: false,
        fileName: '',
      }
    },
    computed: {
      // 这里定义上传文件时携带的参数，即表单数据
      upData: function() {
        return {
          body: this.form
        }
      }
    },
    mounted() {
      if (this.parentName == null || this.parentName == "undefined") {
        this.parentName = "/";
      }
      this.list = this.parentName.split("/").filter(item => item != '');
      this.pathList[0] = "/" + this.list[0] + "/";
      for (var i = 1; i < this.list.length; i++) {
        this.pathList[i] = this.pathList[i - 1] + this.list[i] + "/";
      }
    },
    methods: {

      upload(event) {
        console.log(event.raw);
        var file = event.raw;
        var spark = new SparkMD5.ArrayBuffer();
        var fileReader = new FileReader();
        var vue = this;
        //获取文件二进制数据
        fileReader.readAsArrayBuffer(file)
        //异步执行函数
        fileReader.onload = function(e) {
          spark.append(e.target.result);
          var md5 = spark.end();
          vue.uploadMd5(md5, file, file.name, vue);
        }

      },

      uploadMd5(md5, file, name, vue) {
        let form = new FormData();
        form.append("md5", md5);
        form.append("name", name);
        form.append("path", this.parentName);
        //查后端有没有这个文件
        this.$axios.post('/api/upload/checkMd5', form).then(responese => {
          if (!responese.data.result) {
            vue.upload_(file);
          } else {
            vue.perentDialog = true;
            vue.perent = 100;
          }
          //如果返回true就是新增成功就无需继续 如果false就是没有这个文件需要继续上传
        }).catch(responese=>{
           this.$notify.error({
                    title: '错误',
                    message: '上传出错'
                  });
        });
      },

      upload_(file) {
        var pieceSize = 5;
        const chunkSize = pieceSize * 1024 * 1024 // 5MB一片
        const chunkCount = Math.ceil(file.size / chunkSize) // 总片数

        for (var i = 0; i < chunkCount; i++) {
          const {
            chunk
          } = this.chunkFile(file, i, chunkSize)
          this.uploadChunk({
            chunk,
            name: file.name,
            currentChunk: i,
            chunkCount
          })
        }
      },

      chunkFile(file, currentChunk, chunkSize) {
        let start = currentChunk * chunkSize
        let end = Math.min(file.size, start + chunkSize)
        let chunk = file.slice(start, end)
        return {
          start,
          end,
          chunk
        }
      },

      uploadChunk(chunkInfo) {
        let form = new FormData();
        form.append("upload", chunkInfo.chunk);
        form.append("name", chunkInfo.name);
        form.append("current", chunkInfo.currentChunk);
        form.append('size', chunkInfo.chunkCount)
        this.perentDialog = true
        this.$axios.post('/api/upload/uploadSlice', form).then(responese => {
          if (responese.data.result.code) {
            this.perent = (chunkInfo.currentChunk / chunkInfo.chunkCount) * 100;
          }
          if (chunkInfo.currentChunk >= chunkInfo.chunkCount - 1) {
            let mergeInfo = new FormData();
            mergeInfo.append("name", chunkInfo.name)
            mergeInfo.append("size", chunkInfo.chunkCount)
            mergeInfo.append("path", this.parentName)

            this.$axios.post('/api/upload/merge', mergeInfo).then(responese => {
              if (responese.data.result == true) {
                this.$refs.upload.clearFiles()
                this.perentDialog = false;
                this.$notify.success({
                         title: '成功',
                         message: '上传成功'
                       });
              }
            });
          }
        }).catch(responese=>{
           this.$notify.error({
                    title: '错误',
                    message: '上传出错'
                  });
        });
      },

      format(percentage) {
        return "";
      },
      progress(event, file, fileList) {
        this.perent = file.percentage;
      },
      createFolder() {
        this.$prompt('请输入文件夹名', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /^((?!\\|\/|:|\*|\?|<|>|\||'|%).){1,8}$/,
          inputErrorMessage: '文件夹名格式不正确'
        }).then(({
          value
        }) => {
          var name;
          name = this.parentName;
          this.$axios.post('/api/disk/uploadFolder', {
            selfName: value,
            parentName: name
          }).then(responese => {
            console.log(responese)
            if (responese.data.code === 20000) {
              this.reload();
            }
          });
        });
      }



    }
  }
</script>

<style>
  .header button {
    text-align: left;
  }

  el-upload-list el-upload-list--text {
    position: fixed;
    right: 20px;
    bottom: 20px;
  }
</style>
