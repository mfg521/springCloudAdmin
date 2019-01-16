package com.mfg.auth.admin.biz;

import com.mfg.auth.client.jwt.UserAuthUtil;
import com.ace.cache.annotation.Cache;
import com.ace.cache.annotation.CacheClear;
import com.mfg.auth.admin.entity.User;
import com.mfg.auth.admin.mapper.UserMapper;
import com.mfg.auth.common.biz.BaseBiz;
import com.mfg.auth.common.constant.UserConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserBiz extends BaseBiz<UserMapper,User> {

    //加入新用户
    @Override
    public void insertSelective(User entity){
        //将密码进行一次加密
        String password=new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(entity.getPassword());
        entity.setPassword(password);
        super.insertSelective(entity);
    }

    @Override
    @CacheClear(pre = "user{1.username}")
    public void updateSelectiveById(User entity){
        super.updateSelectiveById(entity);
    }

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    @Cache(key="user{1}")
    public User getUserByUsername(String username){
        User user = new User();
        user.setUsername(username);
        return mapper.selectOne(user);
    }

}
