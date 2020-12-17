<template>
  <div style="height: 100%; width: 100%;">
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1"><i class="el-icon-edit"></i></el-menu-item>
    </el-menu>
    <el-table ref="singleTable" :data="data" highlight-current-row @current-change="handleCurrentChange" style="height: 100%;">
    <el-table-column property="bittorrent.info.name" label="名称">
    </el-table-column>
    <el-table-column property="completedLength" label="进度" width="120px">
      <template slot-scope="scope">
        <el-progress :text-inside="true" :stroke-width="18" :percentage="test(scope.row.completedLength,scope.row.totalLength)"></el-progress>
      </template>
    </el-table-column>
    <el-table-column property="downloadSpeed" label="下载速度" width="120px">
    </el-table-column>
    <el-table-column width="30">
      <template slot-scope="scope">
        <i class="el-icon-arrow-right"></i>
      </template>
    </el-table-column>
  </el-table>
  </div>

</template>


<script>
  export default {
    data() {
      return {
        data: []
      }
    },
    mounted() {

       const timer = setInterval(()=>{
          this.get();
        },2500)
        this.$once('hook:beforeDestroy', ()=>{
          clearInterval(timer)
        })
    },
    methods: {
      setCurrent(row) {
        this.$refs.singleTable.setCurrentRow(row);
      },
      handleCurrentChange(val) {
        this.currentRow = val;
      },
      get() {
        this.$axios.get('/api/disk/aria2DownloadList').then(responese => {
          var arr=Array();
          arr.push(JSON.parse(responese.data.result))
          this.data=arr;
        })
      },
      test(v1,v2){
        var num1 = parseInt(v1);
        var num2=parseInt(v2);
        return parseInt((num1/num2).toFixed(2));
      }
    }
  }
</script>
