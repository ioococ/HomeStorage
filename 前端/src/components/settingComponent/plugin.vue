<template>
  <div><el-menu class="el-menu-demo" mode="horizontal">
      <el-menu-item index="1">
        <el-button icon="el-icon-plus" size="mini" round="" @click="addDialog=true">增加</el-button>
      </el-menu-item>
    </el-menu>
    <el-table :data="plugins">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column label="id">
        <template slot-scope="scope">
          <a style="color: #000000;" @click="getDetails(scope.row)">{{scope.row.id}}</a>
        </template>
      </el-table-column>
      <el-table-column label="角色名">
        <template slot-scope="scope">
          <a style="color: #000000;" @click="getDetails(scope.row)">{{scope.row.pluginName}}</a>
        </template>
      </el-table-column>

      <el-table-column label="状态">
        <template slot-scope="scope">
          <a style="color: #000000;" @click="getDetails(scope.row)">{{scope.row.status}}</a>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template slot="header" slot-scope="scope">
          <el-input v-model="search" size="mini" placeholder="输入关键字搜索" />
        </template>
        <template slot-scope="scope">
          <!-- <el-button
                size="mini"
                @click="handleEdit(scope.$index, scope.row)">编辑</el-button> -->
          <el-dropdown size='mini'>
            <el-button round>
              更多操作<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button type="text" size="mini">详情</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button type="text" size="mini">停用</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button type="text" size="mini">修改</el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="新增插件" :visible.sync="addDialog">
      <el-form :model="form">
        <el-form-item label="ip">
          <el-input v-model="form.ip"></el-input>
        </el-form-item>
        <el-form-item label="端口">
          <el-input v-model="form.port"></el-input>
        </el-form-item>
        <el-form-item label="首页映射">
          <el-input v-model="form.mapping"></el-input>
        </el-form-item>
        <el-form-item label="插件名称">
          <el-input v-model="form.pluginName"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialog = false">取 消</el-button>
        <el-button type="primary" @click="add">确 定</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
  export default {
    data() {
      return {
        plugins: [],
        search: '',
        addDialog: false,
        form: {
          ip: '',
          port: '',
          mapping: '',
          pluginName: ''
        }
      }
    },
    mounted() {
      this.getPlugins();
    },
    methods: {
      getPlugins() {
        this.$axios.get('/api/v1/plugins/plugins').then(responese => {
          this.plugins = responese.data.result;
        })
      },
      doSearch(search) {

      },
      ban(row) {

      },
      update() {

      },
      add() {
        this.$axios.post('', this.Form).then(responese => {
          console.log(responese);
        })
      }
    }
  }
</script>

<style>
</style>
