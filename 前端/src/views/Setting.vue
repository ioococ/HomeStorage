<template>
  <div>
      <bannerBar></bannerBar>
      <el-container>
        <sideList></sideList>
        <contents></contents>
      </el-container>
  </div>
</template>

<script>
  import bannerBar from "../components/banner_bar.vue"
  import contents from "../components/SettingContent.vue"
  import sideList from "../components/sideList.vue"
  export default {
    components:{
      contents,
      sideList,
      bannerBar
    },
    data(){
      return{
        
      }
    },
    mounted() {
     
    }
  }
</script>

<style type="text/css">
/*
	找到html标签、body标签，和挂载的标签
	都给他们统一设置样式
*/
  html,body,#app,.el-container{
        /*设置内部填充为0，几个布局元素之间没有间距*/
        padding: 0px;
         /*外部间距也是如此设置*/
        margin: 0px;
        /*统一设置高度为100%*/
        height: 100%;
    }

    .status_bar{

    }
</style>

