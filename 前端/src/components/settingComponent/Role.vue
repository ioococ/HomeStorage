<template>
  <div>
    <el-menu class="el-menu-demo" mode="horizontal">
      <el-menu-item index="1">
        <el-button icon="el-icon-plus" size="mini" round="">增加</el-button>
      </el-menu-item>
    </el-menu>
    <el-table :data="Roles">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column label="id">
        <template slot-scope="scope">
          <a style="color: #000000;" @click="getDetails(scope.row)">{{scope.row.rid}}</a>
        </template>
      </el-table-column>
      <el-table-column label="角色名">
        <template slot-scope="scope">
          <a style="color: #000000;" @click="getDetails(scope.row)">{{scope.row.name}}</a>
        </template>
      </el-table-column>

      <el-table-column label="状态">
        <template slot-scope="scope">
          <a style="color: #000000;" @click="getDetails(scope.row)">{{scope.row.status}}</a>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="getDetails(scope)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="角色权限" :visible.sync="addDialog">
      <el-form>
        <el-checkbox v-for="p in permission" v-model="permissions" :label="p.pid" border>{{p.name}}</el-checkbox>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateRolePermission()">修改</el-button>
        <el-button @click="addDialog = false">取消</el-button>
      </div>
    </el-dialog>

  </div>

</template>

<script>
  export default {
    data() {
      return {
        Roles: [],
        addDialog: false,
        permission: [],
        permissions: [],
        role_permission: {
          id: '',
          permissions: []
        },
        form: {

        }
      }
    },
    mounted() {
      this.get();
    },
    methods: {
      get() {
        this.$axios.get('/api/v1/admin/getRoles').then(responese => {
          this.Roles = responese.data.result;
        })
      },
      updateRolePermission() {
        this.role_permission.permissions = this.permissions;
        this.$axios.post("/api/v1/admin/UpdatePermission", this.role_permission).then(responese => {
          if (responese.data.code == 20000 && responese.data.result == true) {
            this.$notify({
              title: '修改成功',
              message: '修改成功',
              type: 'success'
            });
            this.addDialog = false;
          }
        });
      },
      Add() {
        this.$axios.post('', Form).then(responese => {
          console.log(responese);
        });
      },
      getDetails(scope) {
        this.addDialog = true;
        this.role_permission.id = scope.row.rid;
        this.$axios.get("/api/v1/admin/getPermission").then(responese => {
          this.permission = responese.data.result;
          this.$axios.get("/api/v1/admin/getRolePermission", {
            params: {
              id: scope.row.rid
            }
          }).then(responese1 => {
            for (var i = 0; i < responese1.data.result.length; i++) {
              this.permissions.push(responese1.data.result[i].pid)
            }
          });
        });
      }
    }
  }
</script>

<style>
</style>
