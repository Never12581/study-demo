package com.hzx.dubbo.service;

import com.hzx.dubbo.dto.UserDTO;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 15:08 2019/12/28
 */
public interface UserProvider {

    UserDTO getUser(String userId);
}
