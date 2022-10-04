package com.xiaxinyu.kob.backend.service.impl.account;

import com.xiaxinyu.kob.backend.pojo.User;
import com.xiaxinyu.kob.backend.service.impl.utils.UserDetailsImpl;
import com.xiaxinyu.kob.backend.service.user.account.InfoService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {
    @Override
    public Map<String, String> getInfo() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();
        Map<String,String> res = new HashMap<>();
        res.put("error_message","success");
        res.put("id",user.getId().toString());
        res.put("user_name",user.getPassword());
        res.put("photo",user.getPhoto());
        return res;
    }
}
