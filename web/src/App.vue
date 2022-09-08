<template>
  <NavBar />
  <router-view />
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"

export default {
  components: {
    NavBar
  }
}

</script>

<style>
body {
  /* @定义为src目录 */
  background: url("@/assets/images/background.png") no-repeat;
  background-attachment: fixed;
  background-size: cover;

}
</style>
