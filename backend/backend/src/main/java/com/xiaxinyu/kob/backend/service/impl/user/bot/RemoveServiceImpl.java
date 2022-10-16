package com.xiaxinyu.kob.backend.service.impl.user.bot;

import com.xiaxinyu.kob.backend.mapper.BotMapper;
import com.xiaxinyu.kob.backend.pojo.Bot;
import com.xiaxinyu.kob.backend.pojo.User;
import com.xiaxinyu.kob.backend.service.impl.utils.UserDetailsImpl;
import com.xiaxinyu.kob.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveServiceImpl implements RemoveService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> remove(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        int bot_id = Integer.parseInt(data.get("bot_id"));
        Bot bot = botMapper.selectById(bot_id);
        Map<String,String> res = new HashMap<>();
        if(bot == null){
            res.put("error_message","Bot不存在或已被删除");
            return res;
        }
        System.out.println(111);
        if(!bot.getUserId().equals(user.getId())){
            res.put("error_message","没有权限删除该Bot");
            return res;
        }
        System.out.println(222);
        botMapper.deleteById(bot_id);
        res.put("error_message","success");
        return res;
    }
}
