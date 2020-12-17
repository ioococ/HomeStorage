<template>
  <div>
    <el-table :data="patternList">
      <el-table-column label="分区路径"  prop="PatternPath"></el-table-column>
      <el-table-column label="分区名"  prop="PatternName"></el-table-column>
      <el-table-column label="容量"  prop="total"></el-table-column>
      <el-table-column label="已使用"  prop="used"></el-table-column>
      <el-table-column label="空闲"  prop="free"></el-table-column>
      <el-table-column label="百分比"  prop="bfb"></el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        patternList: [],
      }
    },
    mounted() {
      this.get();
    },
    methods: {
      get() {
        this.$axios.get('/api/v1/admin/PatternData').then(responese => {
           this.patternList = (responese.data.result);
          console.log(responese);
        });

      },
      add() {

      },
      detail() {

      }
    }
  }
</script>

<style>
</style>
