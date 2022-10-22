package com.xiaxinyu.kob.botrunningsystem.service.impl.utils;

import com.xiaxinyu.kob.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.function.Supplier;

@Component
public class Consumer extends Thread{

    private Bot bot;
    private static RestTemplate restTemplate;

    private final static String receiveBotMoveUrl = "http://127.0.0.1:3000/pk/receive/bot/move";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        Consumer.restTemplate = restTemplate;
    }

    public void startTimeout(long timeout,Bot bot){
        this.bot = bot;
        this.start(); //开始执行run方法里面的函数

        try {
            this.join(timeout); //最多等待timeout秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            this.interrupt(); //中断当前线程
        }
    }

    private String addUid(String code,String uid){ //在code中的Bot类名后添加uid
        int k = code.indexOf(" implements java.util.function.Supplier<Integer>");
        return code.substring(0,k) + uid + code.substring(k);

    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0,8);
//        BotInterface botInterface = Reflect.compile(
//              "com.xiaxinyu.kob.botrunningsystem.utils.Bot" + uid,
//                addUid(bot.getBotCode(),uid)
//
//        ).create().get();

        Supplier<Integer> botInterface = Reflect.compile(
      "com.xiaxinyu.kob.botrunningsystem.utils.Bot" + uid,
        addUid(bot.getBotCode(),uid)
        ).create().get();


        File file = new File("input.txt");
        try(PrintWriter fout = new PrintWriter(file)) {
            fout.println(bot.getInput());
            fout.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("开始执行bot代码");
//        Integer direction = botInterface.nextMove(bot.getInput());
        Integer direction = botInterface.get();
        System.out.println("move-direction: " + bot.getUserId() + " " + direction);

        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",bot.getUserId().toString());
        data.add("direction",direction.toString());

        restTemplate.postForObject(receiveBotMoveUrl,data,String.class);
    }
}
