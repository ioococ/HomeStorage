<template>

  <el-aside width="220px">
    <img :src="imgUrl" width="150px" />
    <el-menu :router="true" :default-active="name">
        <el-menu-item index="/storage">全部</el-menu-item>
        <el-menu-item index="Music">
            音乐
          </el-menu-item>
        <el-menu-item index="Video">
            视频
        </el-menu-item>
        <el-menu-item index="Document">
            文档
        </el-menu-item>
    </el-menu>
  </el-aside>
</template>

<script>
  export default {
    data() {
      return {
        imgUrl: require("../assets/logo_1.png"),
        name: window.location.pathname,
      }
    },
    mounted() {
    },
    methods: {
    }
  }
</script>

<style>
</style>
