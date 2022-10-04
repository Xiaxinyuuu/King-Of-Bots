<template>
  <NavBar />
  <router-view />
</template>

<script>
import NavBar from '@/components/NavBar.vue'
// import $ from 'jquery'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"

export default {
  components: {
    NavBar
  },
  setup(){
    
    // $.ajax({
    //   url:"http://localhost:3000/user/account/info",
    //   type:"get",
    //   headers:{
    //     Authorization:"Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJlNzcxNTRiOTRhMGY0ZDk4OTYwNGMyOTRmNWYwMDgyYyIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY2NDg2MDAzNSwiZXhwIjoxNjY2MDY5NjM1fQ.l1hqcuD0fE8-Jcc0c70NtPUcWxImlIcXjjadywzX4bw"
    //   },
    //   success(resp){
    //     console.log(resp);
    //   },
    //   error(resp){
    //     console.log(resp);
    //   }
    // });
    // $.ajax({
    //   url:"http://localhost:3000/user/account/register",
    //   type:"post",
    //   data:{
    //     username: "yxx1",
    //     password: "1231",
    //     confirmedPassword: "1231"
    //   },
    //   success(resp){
    //     console.log(resp);
    //   },
    //   error(resp){
    //     console.log(resp);
    //   }
    // });
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
