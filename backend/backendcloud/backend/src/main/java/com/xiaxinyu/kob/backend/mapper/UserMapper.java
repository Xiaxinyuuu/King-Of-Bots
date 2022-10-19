package com.xiaxinyu.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaxinyu.kob.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
