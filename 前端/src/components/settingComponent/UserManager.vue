<template>
  <div>
    <el-menu class="el-menu-demo" mode="horizontal">
      <el-menu-item index="1">
        <el-button icon="el-icon-plus" size="mini" round="" @click="show">增加</el-button>
      </el-menu-item>
    </el-menu>
    <el-table :data="users">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column label="序列号">
        <template slot-scope="scope">
          <a style="color: #000000;">{{scope.row.uid}}</a>
        </template>
      </el-table-column>
      <el-table-column label="邮箱">
        <template slot-scope="scope">
          <a style="color: #000000;">{{scope.row.email}}</a>
        </template>
      </el-table-column>
      <el-table-column label="角色名">
        <template slot-scope="scope">
          <a style="color: #000000;">{{scope.row.roleName}}</a>
        </template>
      </el-table-column>
      <el-table-column label="用户名">
        <template slot-scope="scope">
          <a style="color: #000000;">{{scope.row.name}}</a>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot="header" slot-scope="scope">
          <el-input v-model="search" size="mini" placeholder="输入关键字搜索" />
        </template>
        <template slot-scope="scope">
          <el-dropdown size='mini'><i class="el-icon-more"></i>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button type="text" @click="showRoles(scope.row)" size="mini">调整角色</el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="修改角色" :visible.sync="alter">
      <el-form>
        <el-checkbox v-for="p in hasRole" v-model="role_" :label="p.rid" border>{{p.name}}</el-checkbox>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateUserRole()">修改</el-button>
        <el-button @click="alter = false">取消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="新增用户" :visible.sync="addDialog">
      <el-form :model="form">
        <el-form-item label="邮箱">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickName"></el-input>
        </el-form-item>
        <el-form-item label="">
          <el-select v-model="form.rid" placeholder="角色">
            <el-option v-for="role in roles" :label='role.name' :value="role.rid"></el-option>
          </el-select>
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
        users: [],
        search: '',
        addDialog: false,
        alter: false,
        form: {
          email: '',
          rid: '',
          password: '',
          nickName: ''
        },
        user_roles: {
          id: '',
          rids: []
        },
        roles: [],
        allRole: [],
        hasRole: [],
        role_: []
      }
    },
    mounted() {
      this.get();
    },
    methods: {
      updateUserRole(row) {
        this.user_roles.rids = this.role_;
        console.log(this.user_roles);
        this.$axios.post('/api/v1/admin/UpdateUserRoles', this.user_roles).then(responese => {
          if (responese.data.code == 20000 && responese.data.result == true) {
            this.$notify({
              title: '修改成功',
              message: '修改成功',
              type: 'success'
            });
            this.alter = false;
            this.get();
          }
        })
      },
      get() {
        this.$axios.get('/api/v1/admin/users').then(responese => {
          this.users = responese.data.result;
        })
      },
      getRoles() {
        this.$axios.get('/api/v1/admin/getRoles').then(responese => {
          this.roles = responese.data.result;
        })
      },
      hasRoles(row) {
        this.user_roles.id = row.uid;
        this.$axios.get('/api/v1/admin/getRoles').then(responese => {
          this.hasRole = responese.data.result;
          this.$axios.get('/api/v1/admin/hasRole', {
            params: {
              id: row.uid
            }
          }).then(responese1 => {
            for (var i = 0; i < responese1.data.result.length; i++) {
              this.role_.push(responese1.data.result[i].rid);
            }
            console.log(this.role_)
          })
        })
      },
      show() {
        this.addDialog = true;
        this.getRoles();
      },
      showRoles(row) {
        this.alter = true;
        this.hasRoles(row);
      },
      add() {
        this.$axios.post('/api/v1/admin/addUser', this.form).then(responese => {
          var msg = '';
          if (responese.data.result == true) {
            msg = "添加成功"
          } else {
            msg = "添加失败"
          }
          this.$message({
            message: msg,
            type: 'success'
          });
        });
      },
      update() {

      },
      searchUser() {

      },
      Userdetail() {

      }
    }
  }
</script>

<style>
</style>
