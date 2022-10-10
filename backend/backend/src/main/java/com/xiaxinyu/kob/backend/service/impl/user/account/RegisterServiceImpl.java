package com.xiaxinyu.kob.backend.service.impl.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaxinyu.kob.backend.mapper.UserMapper;
import com.xiaxinyu.kob.backend.pojo.User;
import com.xiaxinyu.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String,String> res = new HashMap<>();
        if(username == null){
            res.put("error_message","用户名不能为空");
        }else if(password == null || confirmedPassword == null){
            res.put("error_message","密码不能为空");
        }
        username = username.trim();
        if(username.length() == 0) {
            res.put("error_message", "用户名不能为空");
            return res;
        }else if(username.length() > 100){
            res.put("error_message", "用户名长度不能大于100");
            return res;
        }else if(password.length() > 100 || confirmedPassword.length() > 100){
            res.put("error_message", "密码长度不能大于100");
            return res;
        }else if(password.length() == 0 || confirmedPassword.length() == 0){
            res.put("error_message", "密码不能为空");
            return res;
        }

        if(! password.equals(confirmedPassword)){
            res.put("error_message","两次输入的密码不一致");
            return res;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("username",username);
        List<User> users = userMapper.selectList(queryWrapper);
        if(! users.isEmpty()){
            res.put("error_message","用户名已存在");
            return res;
        }

        String encodedPassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/66949_lg_99f458b217.png";
        User user = new User();
        user.setPassword(encodedPassword);
        user.setUsername(username);
        user.setPhoto(photo);
        userMapper.insert(user);
        res.put("error_message","success");
        return res;
    }
}
