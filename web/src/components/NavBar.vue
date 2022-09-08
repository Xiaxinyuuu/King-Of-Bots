<template>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <!-- 使用router-link标签时点击链接不再向服务器发送请求获取页面 -->
            <!-- <a class="navbar-brand" href="/">King Of Bots</a> -->
            <router-link class="navbar-brand" :to="{name:'home'}">King Of Bots</router-link>
            <div class="collapse navbar-collapse" id="navbarText">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <!-- 如果想在属性里使用表达式，需要在属性名前加： -->
                        <router-link :class="route_name == 'pk_index' ? 'nav-link active' : 'nav-link'"
                            :to="{name:'pk_index'}">对战</router-link>
                    </li>
                    <li class="nav-item">
                        <router-link :class="route_name == 'record_index' ? 'nav-link active' : 'nav-link'"
                            :to="{name:'record_index'}">对局列表</router-link>
                    </li>
                    <li class="nav-item">
                        <router-link :class="route_name == 'ranklist_index' ? 'nav-link active' : 'nav-link'"
                            :to="{name:'ranklist_index'}">排行榜</router-link>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                            data-bs-toggle="dropdown" aria-expanded="false">
                            xiaxinyu
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <router-link class="dropdown-item" :to="{name:'user_bot_index'}">我的Bot</router-link>
                            <li>
                                <hr class="dropdown-divider">
                            </li>
                            <li><a class="dropdown-item" href="#">退出</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</template>

<script>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
export default {
    setup() {
        const route = useRoute();
        let route_name = computed(() => route.name);
        return {
            route_name //这里返回的变量是可以在html代码中使用的
        }
    }
}
</script>

<!-- scoped关键字使该组件的css样式不会影响到其他组件 -->
<style scoped>
</style>

