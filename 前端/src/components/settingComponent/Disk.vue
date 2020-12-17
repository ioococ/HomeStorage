<template>
  <div>
    <div style="margin-bottom: 50px;">
      <el-table :data="DiskList">
        <el-table-column label="序号" prop="id">

        </el-table-column>
        <el-table-column label="文件夹名" prop="folderName">

        </el-table-column>
        <el-table-column label="映射路径" prop="customName">

        </el-table-column>
        <el-table-column label="映射名称" prop="deviceName">
            <template slot="header" slot-scope="scope">
                <el-button size="mini" @click="addDialog=true">新增</el-button>
            </template>
        </el-table-column>
      </el-table>

      <el-dialog customClass="pdfDialog" size="full" title="添加" :visible.sync="addDialog">
          <el-form ref="form" :model="DiskForm">
            <el-form-item label="文件夹名">
              <el-input v-model="DiskForm.folderName"></el-input>
            </el-form-item>
            <el-form-item label="设备名称">
              <el-input v-model="DiskForm.deviceName"></el-input>
            </el-form-item>
            <el-form-item label="相对访问路径">
              <el-input v-model="DiskForm.customName"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="add">添加</el-button>
              <el-button>取消</el-button>
            </el-form-item>
          </el-form>
        </span>
      </el-dialog>
    </div>
    <!-- <div>
      <el-form ref="form" :model="DiskForm">
        <el-form-item label="文件夹名">
          <el-input v-model="DiskForm.folderName"></el-input>
        </el-form-item>
        <el-form-item label="设备名称">
          <el-input v-model="DiskForm.deviceName"></el-input>
        </el-form-item>
        <el-form-item label="相对访问路径">
          <el-input v-model="DiskForm.customName"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="add">添加</el-button>
          <el-button>取消</el-button>
        </el-form-item>
      </el-form>
    </div> -->
  </div>
</template>

<script>
  export default {
    data() {
      return {
        DiskList: [],
        DiskForm: {
          folderName: '',
          deviceName: '',
          customName: ''
        },
        addDialog:false
      }
    },
    mounted() {
      this.get();
    },
    methods: {
      get() {
        this.$axios.get('/api/v1/admin/hard_Devices').then(responese => {
          this.DiskList = (responese.data.result);
        });

      },
      add() {
        this.$axios.post('/api/v1/admin/insertHardDevices', this.DiskForm).then(responese => {
          console.log(responese.data.result);
        });
      },
      detail() {

      }
    }
  }
</script>

<style>
</style>
