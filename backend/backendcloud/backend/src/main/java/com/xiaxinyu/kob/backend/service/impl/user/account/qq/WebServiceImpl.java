package com.xiaxinyu.kob.backend.service.impl.user.account.qq;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaxinyu.kob.backend.mapper.UserMapper;
import com.xiaxinyu.kob.backend.pojo.User;
import com.xiaxinyu.kob.backend.service.user.account.acwing.WebService;
import com.xiaxinyu.kob.backend.utils.JwtUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


@Service
public class WebServiceImpl implements WebService {

    private final static String appId = "你的appid";

    private final static String appKey = "你的appkey";
    private final static String redirectUri = "https://xiaxinyuxy.top/user/account/qq/receive_code";
    private final static String applyAccessTokenUrl = "https://graph.qq.com/oauth2.0/token";
    private final static String getUserInfoUrl = "https://graph.qq.com/user/get_user_info";

    private static final String getUserOpenIDUrl = "https://graph.qq.com/oauth2.0/me";
    private final static Random random = new Random();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public JSONObject applyCode() {
        JSONObject resp = new JSONObject();
        String encodeUrl = "";
        try {
            encodeUrl = URLEncoder.encode(redirectUri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            resp.put("result", "failed");
            return resp;
        }
        StringBuilder state = new StringBuilder();
        for (int i = 0; i < 10; i++)
            state.append((char) (random.nextInt(10) + '0'));
        resp.put("result", "success");
        redisTemplate.opsForValue().set(state.toString(), "true");
        redisTemplate.expire(state.toString(), Duration.ofMinutes(10)); //过期时间10分钟

        String applyCodeUrl = "https://graph.qq.com/oauth2.0/authorize?client_id=" + appId
                + "&redirect_uri=" + encodeUrl
                + "&response_type=code"
                + "&state=" + state;
        resp.put("apply_code_url", applyCodeUrl);
        return resp;
    }

    @Override
    public JSONObject receiveCode(String code, String state) {
        JSONObject resp = new JSONObject();
        resp.put("result", "failed");
        if (code == null || state == null) return resp;
        if (Boolean.FALSE.equals(redisTemplate.hasKey(state))) return resp;
        redisTemplate.delete(state);

        List<NameValuePair> nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
        nameValuePairs.add(new BasicNameValuePair("client_id", appId));
        nameValuePairs.add(new BasicNameValuePair("client_secret", appKey));
        nameValuePairs.add(new BasicNameValuePair("code", code));
        nameValuePairs.add(new BasicNameValuePair("redirect_uri", redirectUri));
        nameValuePairs.add(new BasicNameValuePair("fmt", "json"));
        String getString = HttpClientUtil.get(applyAccessTokenUrl, nameValuePairs);
        if (getString == null) return resp;
        JSONObject getResp = JSONObject.parseObject(getString);
        String accessToken = getResp.getString("access_token");
        System.out.println("access_token:" +  accessToken);
        nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("access_token", accessToken));
        nameValuePairs.add(new BasicNameValuePair("fmt", "json"));

        getString = HttpClientUtil.get(getUserOpenIDUrl, nameValuePairs);
        if (getString == null) return resp;
        getResp = JSONObject.parseObject(getString);
        String openid = getResp.getString("openid");
        System.out.println("open_id:" + openid);
        if (accessToken == null || openid == null) return resp;

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            User user = users.get(0);
            String jwt = JwtUtil.createJWT(user.getId().toString());
            System.out.println("生成jwt" + jwt);
            resp.put("result", "success");
            resp.put("jwt_token", jwt);
            return resp;
        }


        nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("access_token", accessToken));
        nameValuePairs.add(new BasicNameValuePair("oauth_consumer_key", appId));
        nameValuePairs.add(new BasicNameValuePair("openid", openid));

        getString = HttpClientUtil.get(getUserInfoUrl, nameValuePairs);
        if (getString == null) return resp;
        getResp = JSONObject.parseObject(getString);


        String username = getResp.getString("nickname");
        String photo = getResp.getString("figureurl_1");

        System.out.println("username : " + username);
        System.out.println("photo : " + photo);

        if (username == null || photo == null) return resp;

        for (int i = 0; i < 100; i++) {
            QueryWrapper<User> usernameQueryWrapper = new QueryWrapper<>();
            usernameQueryWrapper.eq("username", username);
            if (userMapper.selectList(usernameQueryWrapper).isEmpty()) break;
            username += (char) (random.nextInt(10) + '0');
            if (i == 99) return resp;
        }

        User user = new User(
                null,
                username,
                null,
                photo,
                1500,
                openid
        );
        userMapper.insert(user);
        String jwt = JwtUtil.createJWT(user.getId().toString());
        resp.put("result", "success");
        resp.put("jwt_token", jwt);
        return resp;
    }
}