<template>
    <div ref="parent" class="gamemap">
        <canvas ref="canvas"></canvas>
    </div>
</template>

<script>
import { GameMap } from "@/assets/scripts/GameMap"
import { ref, onMounted } from 'vue'

export default {
    setup() {
        let parent = ref(null);
        let canvas = ref(null);

        onMounted(() => { //组件挂载完之后需要执行的操作 
            new GameMap(canvas.value.getContext('2d'), parent.value);
        })

        return {
            parent,
            canvas
        }
    }
}
</script>

<style scoped>
div.gamemap {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center; /*水平居中*/
    align-items: center;  /*竖直居中*/
}
</style>