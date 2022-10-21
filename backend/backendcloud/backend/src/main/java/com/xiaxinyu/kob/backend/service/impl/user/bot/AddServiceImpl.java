package com.xiaxinyu.kob.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaxinyu.kob.backend.mapper.BotMapper;
import com.xiaxinyu.kob.backend.pojo.Bot;
import com.xiaxinyu.kob.backend.pojo.User;
import com.xiaxinyu.kob.backend.service.impl.utils.UserDetailsImpl;
import com.xiaxinyu.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String,String> res = new HashMap<>();
        if(title == null || title.length() == 0){
            res.put("error_message","标题不能为空");
            return res;
        }
        if(title.length() > 100){
            res.put("error_message","标题长度不能大于100");
            return res;
        }

        if(description == null || description.length() == 0){
            description = "这个用户很懒，什么也没留下~";
        }

        if(description.length() > 300){
            res.put("error_message","Bot描述的长度不能大于300");
            return res;
        }
        if(content == null || content.length() == 0){
            res.put("error_message","代码不能为空");
            return res;
        }
        if(content.length() > 10000){
            res.put("error_message","代码长度不能超过10000");
            return res;
        }

        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        if(botMapper.selectCount(null) >= 10){
            res.put("error_message","每个用户最多创建10个Bot!");
            return res;
        }

        Date now = new Date();
        Bot bot = new Bot(null, user.getId(), title,description,content,now,now);
        botMapper.insert(bot);
        res.put("error_message","success");
        return res;
    }
}
