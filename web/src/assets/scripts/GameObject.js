const GAME_OBJECT = [];

//使用export时，在其他文件中导入时需要使用{},使用default关键字修饰export时则不用使用{}，default在一个文件中只能有一个
export class GameObject {
    constructor() {
        GAME_OBJECT.push(this);
        this.timedelta = 0; //当前帧距离上一帧的时间间隔
        this.has_called_start = false; //标记是否执行过start函数
    }

    start() { //只执行一次

    }

    update() { //除了第一帧之外，每一帧执行一次

    }

    on_destory() {  //删除之前执行

    }

    destopy() {
        this.on_destory();
        for (let i in GAME_OBJECT) {
            const obj = GAME_OBJECT[i];
            if (obj == this) {
                GAME_OBJECT.splice(i);
                break;
            }
        }
    }
}

let last_timestamp; //上一次执行的时刻
const step = timestamp => {
    for (let obj of GAME_OBJECT) {
        if (!obj.has_called_start) {
            obj.has_called_start = true;
            obj.start();
        } else {
            obj.timedelta = timestamp - last_timestamp;
            obj.update();
        }
    }
    last_timestamp = timestamp;
    requestAnimationFrame(step);
}

requestAnimationFrame(step);