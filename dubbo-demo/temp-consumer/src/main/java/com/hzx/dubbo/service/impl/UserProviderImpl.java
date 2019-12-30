package com.hzx.dubbo.service.impl;

import com.hzx.dubbo.dto.UserDTO;
import com.hzx.dubbo.service.UserProvider;
import org.apache.dubbo.config.annotation.Service;

import java.util.Date;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 15:08 2019/12/28
 */
@Service
public class UserProviderImpl implements UserProvider {
    @Override
    public UserDTO getUser(String userId) {
        return new UserDTO(userId,"最牛逼之人",25,new Date()) ;
    }
}
