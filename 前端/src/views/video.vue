<template>
  <div v-title :data-title="selfName">
    <video :src="src" controls="controls"></video>
  </div>

</template>

<script>
  export default {
    data() {
      return {
        src: '',
        selfName: this.$route.query.file.substring(this.$route.query.file.lastIndexOf("/") + 1),
        parentName: this.$route.query.file.substring(0, this.$route.query.file.lastIndexOf("/") + 1),
      }
    },
    mounted() {
      this.get()
    },
    methods: {
      get() {
        this.$axios.get('/api/disk/getFile', {
          params: {
            pathName: encodeURIComponent(this.parentName),
            fileName: encodeURIComponent(this.selfName)
          }
        }).then(responese => {
          console.log("/api" + responese.data.result.path);
          this.src = "/api" + responese.data.result.path;
        })
      },
      dlna() {

      }
    }
  }
</script>

<style>
</style>
