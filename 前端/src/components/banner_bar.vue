<template>
  <div>


    <div class="status_bar">

      <el-menu :router="true" :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
        <el-menu-item index="/storage">我的网盘</el-menu-item>
        <el-submenu index="2">
          <template slot="title">插件</template>
          <el-menu-item v-for="item in items" :index="'http://'+item.ip+':'+item.port+'/'+item.mapping">
            {{item.pluginName}}
          </el-menu-item>
        </el-submenu>
        <el-menu-item index="/storage/BtDownload">离线下载</el-menu-item>
        <el-menu-item index="/storage/FileVersionManger">版本控制</el-menu-item>
        <el-menu-item index="/storage/Schedule">定时任务</el-menu-item>
        <el-menu-item index="/storage/Admin">设置</el-menu-item>
        <el-submenu style="float: right;" index="User">
          <template slot="title">{{userName}}</template>
          <el-menu-item v-if="isLogin" index="/User/logout">注销</el-menu-item>
          <el-menu-item v-else index="/User/Login">登陆</el-menu-item>
        </el-submenu>
      </el-menu>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        userName: '',
        rememberme: '',
        today: new Date().toLocaleDateString(),
        activeIndex:window.location.pathname,
        items: [],
        isLogin:false
      }
    },
    mounted() {
      this.getCookie();
      this.get();
    },
    methods: {
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      },
      get() {
        this.$axios.get('/api/v1/plugins/plugins').then(responese => {
          this.items = responese.data.result;
        })
      },
      getCookie: function() {
        if (document.cookie.length > 0) {
          var arr = document.cookie.split('; '); //这里显示的格式需要切割一下自己可输出看下
          for (var i = 0; i < arr.length; i++) {
            var arr2 = arr[i].split('='); //再次切割
            if (arr2[0] == 'nickName') {
              this.userName = (arr2[1]); //cookie中的userName
              this.isLogin=true;
              return ;
            }
          }
        }
        else{
          this.userName="未登录"
        }
      }
    }
  }
</script>

<style>
  .status_bar {
    display: inline-block;
    width: 100%;
  }

  .status_bar i {
    margin: 0.3125rem;
  }
</style>
