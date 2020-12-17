<template>
    <el-container>
      <el-header style="height: 40px;margin: 0 0 0px 0;">
        <headers></headers>
      </el-header>
      <el-main style="padding-left: 10px;padding-top: 0rem;">
        <el-table :data="tableData" fit>
          <el-table-column type="selection" width="55">
          </el-table-column>
          <el-table-column label="文件名" min-width="100%">
            <template slot-scope="scope" @mousemove="">
              <img :src="chooseIcon(scope.row)" style="vertical-align: middle;" />
              <a style="color: #000000;margin-left: 5px;" @click="getDetails(scope.row)">{{scope.row.selfName}}</a>
              <el-dropdown size='mini' style="float: right;">

                <el-link :underline="false" icon="el-icon-more"></el-link>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item>
                    <el-button type="text" v-if="checkExt(scope.row)" size="mini" @click="download(scope.$index, scope.row)">原始文件</el-button>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-button type="text" size="mini" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-button type="text" v-if="checkDLNA(scope.row.selfName)" size="mini" @click="DLNA(scope.row.fid)">投屏</el-button>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-button size="mini" v-if="scope.row.isDir==0" type="text" @click="FileVersion( scope.row)">版本控制</el-button>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </el-table-column>

          <el-table-column prop="size" label="大小" align="center" width="200px">
            <template slot-scope="scope">
              {{bytesToSize(scope.row)}}
            </template>
          </el-table-column>
          <el-table-column label="创建日期">
            <template slot="header" slot-scope="scope">
              <el-input @keyup.enter.native="s(search)" v-model="search" size="mini" placeholder="输入关键字搜索" />
            </template>
            <template slot-scope="scope" filter-method="filter()">
              <span value="">{{parse(scope.row.createDate)}}</span>
            </template>
          </el-table-column>
        </el-table>

        <el-dialog customClass="pdfDialog" size="full" :title="office_.fileName" :visible.sync="office_.officeDialog"
          :before-close="handleClose">
          <embed :src="office_.filePath" type="application/pdf" width="1000px" height="1000px" internalinstanceid="81" />
          <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="office_.officeDialog=false">确 定</el-button>
          </span>
        </el-dialog>
        <el-image-viewer v-if="showViewer" :on-close="close" :url-list="[pics]" />

        <Player v-if="musicPlayer" style="position: fixed;right: 50px;bottom: 3.125rem;" float autoplay mini :music="music"></Player>
        </el-drawer>
      </el-main>
    </el-container>
</template>

<script>
  import headers from "../components/header.vue"
  import footers from "../components/footer.vue"
  import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
  import Player from 'vue-aplayer'
  export default {
    inject: ['reload'],
    components: {
      headers,
      footers,
      Player,
      ElImageViewer
    },
    data() {
      return {
        tableData: [],
        parentName: '/',
        path: "",
        userName: '',
        dialogVisible: false,
        name: "All",
        FolderName: '',
        search: '',
        showViewer: false,
        pics: '',
        musicPlayer: false,
        office_: {
          officeDialog: false,
          officeFileName: '',
          officePath: ''
        },
        music: {
          title: '',
          src: '',
          pic: require("../assets/test.gif")
        }
      }
    },
    mounted() {
      if (typeof this.$route.query.parentName != "undefined") {
        this.parentName = this.$route.query.parentName
      }
      this.getList(this.path, this.parentName);
    },
    watch: {
      '$route'(to, from) {
        this.$router.go(0);
      }
    },
    methods: {
      checkExt(row) {
        if (row.isDir == 0) {
          return false;
        } else if (this.checkType(row.selfName) === 'video') {
          return false;
        }
        return true;
      },
      close() {
        this.showViewer = false
      },
      insertFresh() {
        this.reload();
      },
      checkDLNA(str) {
        return this.checkType(str) == "video";
      },
      chooseIcon(row) {
        if (row.isDir == 1) {
          return require("../assets/icon/Folder.png");
        } else {
          var type = this.checkType(row.selfName);
          switch (type) {
            case "torrent":
              return require("../assets/icon/BT.png");
              break;
            case "picture":
              return require("../assets/icon/Picture.png");
              break;
            case "music":
              return require("../assets/icon/Music.png");
              break;
            case "video":
              return require("../assets/icon/Video.png");
              break;
            case "office":
              return require("../assets/icon/Text.png");
              break;
            case "pdf":
              return require("../assets/icon/PDF.png");
              break;
          }
        }
        return require("../assets/icon/Other.png");
      },
      parse(value) {
        return new Date(value);
      },
      bytesToSize(row) {
        if (row.isDir == 1) {
          return "-";
        } else {
          var bytes = row.size;
          //KB MB GB
          if (bytes === 0) return '0 B';
          var k = 1024, // or 1024
            sizes = ['KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
            i = Math.floor(Math.log(bytes) / Math.log(k));

          return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
        }

      },
      s(keyword) {
        this.$axios.get('/api/disk/searchFiles', {
          params: {
            keyword: keyword
          },
        }).then(responese => {
          this.tableData = [];
          this.tableData = responese.data.result;
        });
      },


      checkType(str) {
        switch (str.substring(str.lastIndexOf(".")).toLowerCase()) {
          case ".jpg":
            return "picture";
          case ".png":
            return "picture";
            break;
          case ".gif":
            return "picture";
            break;
          case ".jpeg":
            return "picture";
            break;
          case ".mp4":
            return "video";
            break;
          case ".mkv":
            return "video";
          case ".avi":
            return "video";
          case ".pdf":
            return "pdf";
          case ".doc":
            return "MicrosoftOffice";
          case ".docx":
            return "MicrosoftOffice";
          case ".torrent":
            return "torrent";
          case ".mp3":
            return "music";
            break;
        }
      },

      FileVersion(row) {
        this.$axios.post('/api/v1/FilesVersion/insertFilesVersionControl', {
          fid: row.fid + ''
        }).then(responese => {
          if (responese.data.code == 20000) {

            this.$notify({
              title: '成功',
              message: '添加成功!',
              type: 'success'
            });
          }
        });
      },

      DLNA(fid) {
        this.$prompt('设备名', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({
          value
        }) => {
          this.$axios.get('/api/Files/Share/DLNA', {
            params: {
              fid: fid,
              device: encodeURIComponent(value)
            }
          }).then(responese => {
            console.log(responese);
          })
        });
      },
      handleDelete(index, row) {
        this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios.post('/api/disk/delete', {
            fid: row.fid
          }).then(responese => {
            if (responese.data.code == 20000) {
              this.$notify({
                title: '成功',
                message: '删除成功!',
                type: 'success'
              });
              this.reload();
              this.insertFresh();
            }
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      CreateFolder() {
        var name;
        if (this.parentName == "/" && this.parentName == this.path) {
          name = "/";
        } else {
          name = this.parentName + this.path + "/"
        }
        this.$axios.post('/api/disk/createFolder', {
          selfName: this.FolderName,
          parentName: name
        }).then(responese => {
          if (responese.data.code === 20000) {
            this.dialogVisible = false
            this.insertFresh();
          }
        });
      },
      download(index, row) {
        this.getFile(row, function(ex) {
          window.open(ex, '_blank')
        });
      },
      Music(row, vue) {
        this.getFile(row, function(ex) {
          vue.musicPlayer = true;
          vue.music.title = row.selfName;
          vue.music.src = ex;
        })
      },
      video(row, vue) {

        let routeData = this.$router.resolve({
          path: '/video',
          query: {
            file: row.parentName + row.selfName
          }
        });
        window.open(routeData.href, '_blank');
      },
      getDetails(row) {
        if (row.isDir == 1) {
          var path = "";
          if (row.parentName == '/') {
            path = "/" + row.selfName + "/";
          } else {
            path = row.parentName + row.selfName + "/"
          }
          this.$router.push({
            path: '/storage',
            query: {
              parentName: path
            }
          });
        } else {
          var vue = this
          var str = row.selfName;
          switch (this.checkType(str)) {
            case "video":
              vue.video(row, vue);
              break;
            case "picture":
              vue.picture(row, vue);
              break;
            case "MicroSoftOffice":
              vue.office(row, vue);
              break;
            case "pdf":
              vue.office(row, vue);
              break;
            case "torrent":
              vue.torrent(row, vue);
              break;
            case "music":
              vue.Music(row, vue);
          }
        }
      },

      torrent(row, vue) {
        var msg = "确定要离线 " + row.selfName + "种子的内容吗";
        if (confirm(msg) == true) {
          this.aria2Torrent(row, function() {
            this.$notify({
              title: '成功',
              message: '添加成功!',
              type: 'success'
            });
          });
        }
        this.insertFresh();
      },

      picture(row, vue) {
        this.getFile(row, function(ex) {
          vue.showViewer = true;
          vue.pics = ex
        });
      },

      office(row, vue) {
        this.getFile(row, function(ex) {
          vue.office_.officeDialog = true;
          vue.office_.fileName = row.selfName
          vue.office_.filePath = ex
        });
      },
      aria2Torrent(row, callback) {
        this.$axios.get('/api/disk/aria2Torrent', {
          params: {
            fid: row.fid
          }
        }).then(responese => {
          if (responese.data.code == 20000) {
            callback();
          }
        });
      },
      getFile(row, testfuntion) {
        this.$axios.get('/api/disk/getFile', {
          params: {
            pathName: encodeURIComponent(row.parentName),
            fileName: encodeURIComponent(row.selfName)
          }
        }).then(responese1 => {
          if (responese1.data.code == 20000) {
            this.$axios.get("/api/"+responese1.data.result.path).then(resolve => {
              testfuntion("/api/"+responese1.data.result.path);
            }, reject => {
              this.open8(row.selfName + '还没准备好', 'error');
            });
          }
        });
      },

      open8(message, type) {
        this.$message({
          showClose: true,
          message: message,
          type: type
        });
      },

      getList(path, parentName) {
        this.$axios.get('/api/disk/fileList', {
          params: {
            parentName: parentName
          }
        }).then(responese => {
          if (responese.data.code == 20000) {
            this.tableData = responese.data.result;
          }

        });
      },
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      }
    }
  }
</script>

<style>
  a {
    text-decoration: none;
    color: black;
  }

  .el-upload {
    text-align: left !important;
  }

  .pdfDialog {
    width: 1200px;
    height: 1300px;
  }
</style>
