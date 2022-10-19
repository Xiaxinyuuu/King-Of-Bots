package com.xiaxinyu.kob.backend.service.impl.user.account;

import com.xiaxinyu.kob.backend.pojo.User;
import com.xiaxinyu.kob.backend.service.impl.utils.UserDetailsImpl;
import com.xiaxinyu.kob.backend.service.user.account.LoginService;
import com.xiaxinyu.kob.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username,password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken); //如果登录失败会自动处理
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();

        String jwt = JwtUtil.createJWT(user.getId().toString());

        Map<String,String> res = new HashMap<>();
        res.put("error_message","success");
        res.put("token",jwt);
        return res;
    }
}
