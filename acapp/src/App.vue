<template>
    <div class="game-body">
      <MenuView v-if="$store.state.router.router_name === 'menu'"/>
      <PkIndexViewVue v-else-if="$store.state.router.router_name === 'pk'"/>
      <RecordIndexViewVue v-else-if="$store.state.router.router_name === 'record'"/>
      <RecordContentViewVue v-else-if="$store.state.router.router_name === 'record_content'"/>
      <RanklistIndexViewVue v-else-if="$store.state.router.router_name === 'ranklist'"/>
      <UserBotIndexViewVue v-else-if="$store.state.router.router_name === 'user_bot'"/>
    </div>
</template>

<script>
import {useStore} from 'vuex'
import MenuView from './views/MenuView.vue'
import PkIndexViewVue from "./views/pk/PkIndexView.vue"
import RecordIndexViewVue from "./views/record/RecordIndexView.vue"
import RecordContentViewVue from "./views/record/RecordContentView.vue"
import RanklistIndexViewVue from "./views/ranklist/RanklistIndexView.vue"
import UserBotIndexViewVue from "./views/user/bot/UserBotIndexView.vue"
import $ from 'jquery'

export default {
  components: {
    MenuView,
    PkIndexViewVue,
    RecordIndexViewVue,
    RecordContentViewVue,
    RanklistIndexViewVue,
    UserBotIndexViewVue
  },
  setup() {
    const store = useStore();
    console.log("开始请求");
    $.ajax({
      url: "https://app3649.acapp.acwing.com.cn/api/user/account/acwing/acapp/apply_code",
      type: "get",
      success: resp => {
        console.log(resp);
        if(resp.result === "success"){
          console.log("请求成功");
          store.state.user.AcWingOS.api.oauth2.authorize(resp.appid, resp.redirect_uri, resp.scope, resp.state, resp => {

            console.log(resp);
            if(resp.result === "success"){
              const jwt_token = resp.jwt_token;
              console.log(jwt_token);
              store.commit("updateToken", jwt_token);
              store.dispatch("getinfo", {  
                  success() {
                      store.commit("updatePullingInfo", false);
                  },
                  error() {
                      store.commit("updatePullingInfo", false);
                  }
              })
            }else{
              // store.state.user.AcWingOS.api.window.close();
            }
          });
        }else{
          console.log("请求失败");
          store.state.user.AcWingOS.api.window.close();
        }
      }
    })

  //   const jwt_token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmZGU3Y2QyMzc0ZWI0NGEzYWNhMmZkYjU1MDVmNDUxNiIsInN1YiI6IjEzIiwiaXNzIjoic2ciLCJpYXQiOjE2NjY0NDEyNTksImV4cCI6MTY2NzY1MDg1OX0.VV6D-uOYwYy2p-lY-XIUkYT9fbbnj1r0LaOJ82AZc9o";
  //       if (jwt_token) {
  //           store.commit("updateToken", jwt_token);
  //           store.dispatch("getinfo", {  
  //               success() {
  //                   store.commit("updatePullingInfo", false);
  //               },
  //               error() {
  //                   store.commit("updatePullingInfo", false);
  //               }
  //           })
  //       } else {
  //           store.commit("updatePullingInfo", false);
  //       }
  }
}
</script>

<style scoped>
body {
  margin: 0;
}

div.game-body {
  background-image: url("@/assets/images/background.png");
  background-size: cover;
  width: 100%;
  height: 100%;
}

div.window{
  width: 100vw;
  height: 100vh;  
}
</style>
