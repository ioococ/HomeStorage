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
  import contents from "../components/BtDownloadTable.vue"
  import sideList from "../components/sideList.vue"
  export default {
    components: {
      contents,
      sideList,
      bannerBar
    },
    data() {
      return {}
    },
    mounted() {
      
    },
    methods: {
     
    }
  }
</script>

<style>
</style>
