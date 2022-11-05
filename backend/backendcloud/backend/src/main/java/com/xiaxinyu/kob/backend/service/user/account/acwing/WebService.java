package com.xiaxinyu.kob.backend.service.user.account.acwing;

import com.alibaba.fastjson.JSONObject;

public interface WebService {
    JSONObject applyCode();
    JSONObject receiveCode(String code,String stage);
}