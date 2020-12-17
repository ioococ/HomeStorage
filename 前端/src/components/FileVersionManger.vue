<template>
  <div style="width: 100%;margin-bottom: 20px;">
    <el-table :data="tableData" style="width: 100%;margin-bottom: 20px;" row-key="groupId" border default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column prop="groupName" label="版本名称">
      </el-table-column>
      <el-table-column prop="version" label="版本号" sortable width="180">
      </el-table-column>
      <el-table-column prop="updateTime" label="修改日期">
      </el-table-column>
      <el-table-column prop="updateTime" label="">
        <template slot-scope="scope">
          <el-dropdown size='mini' style="margin-left: 10px;">
            <i class="el-icon-more"></i>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button v-if="scope.row.children!=null" @click="add(scope.row)" size="mini" type="text">添加</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button size="mini" type="text">原始文件</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button size="mini" @click="timeline(scope.row)" type="text">历史详情</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button size="mini" @click="del(scope.row.groupId)" type="text">删除</el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <div>
      <el-dialog title="上传" :visible.sync="uploadDialog">
        <el-form>
          <el-form-item label="文件组名称">
            <el-input type="text" v-model="form.groupName" readonly=""></el-input>
          </el-form-item>
          <el-form-item label="版本描述">
            <el-input type="text" v-model="form.desc_"></el-input>
          </el-form-item>
          <el-form-item label="版本号">
            <el-input type="text" v-model="form.version"></el-input>
          </el-form-item>
          <el-form-item label="上传文件">
            <el-upload class="upload-demo" :with-credentials="true" :data="form" name="upload" ref="upload" action="/api/v1/FilesVersion/updateVersion"
              :on-preview="handlePreview" :auto-upload="false">
              <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            </el-upload>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="uploadDialog = false">取 消</el-button>
          <el-button type="primary" @click="upload">确 定</el-button>
        </span>
      </el-dialog>


      <el-dialog title="历史详情" :visible.sync="timelineDialog">
        <el-timeline>
          <el-timeline-item  v-for="activity in timeline_" :timestamp="activity.updateTime" >
            <el-card>
              <h4>{{activity.version}}</h4>
              <p>{{activity.desc_}}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-dialog>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        tableData: [],
        timelineDialog: false,
        form: {
          groupName: '',
          desc_: '',
          version: ''
        },

        timeline_: [],
        uploadDialog: false
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
      this.$axios.get('/api/v1/FilesVersion/FileVersionControlList').then(responese => {
        this.tableData = responese.data.result;
      });
    },
    methods: {
      timeline(row) {
        this.timelineDialog = true;
        this.$axios.get("/api/v1/FilesVersion/FileVersion", {
          params: {
            GroupName: row.groupName
          },
        }).then(responese => {
          this.timeline_ = responese.data.result;
        });
      },
      add(row) {
        this.uploadDialog = true;
        this.form.groupName = row.groupName;
      },
      del(groupId) {
        console.log(groupId);
        this.$axios.post('/api/v1/FilesVersion/delete', {
          groupId: groupId
        }).then(responese => {
          console.log(responese);
        });
      },
      originFile() {

      },
      handlePreview() {

      },
      upload() {
        this.$refs.upload.submit();
      }
    }
  }
</script>
