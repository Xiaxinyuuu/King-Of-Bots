package com.xiaxinyu.kob.botrunningsystem.service.impl;

import com.xiaxinyu.kob.botrunningsystem.service.BotRunningService;
import com.xiaxinyu.kob.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

@Service
public class BotRunningServiceImpl implements BotRunningService {

    public final static BotPool botPool = new BotPool();
    @Override
    public String addBot(Integer userId, String botCode, String input) {
        System.out.println("add bot : " + userId + " " + botCode + " " + input);
        botPool.addBot(userId,botCode,input);
        return "add bot success";
    }
}
