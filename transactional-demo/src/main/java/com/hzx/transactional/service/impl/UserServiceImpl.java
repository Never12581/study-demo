package com.hzx.transactional.service.impl;

import com.hzx.transactional.constants.Constants;
import com.hzx.transactional.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
//    @Transactional
    public void insertUser() {
        String random = UUID.randomUUID().toString();
        jdbcTemplate.update(Constants.INSERT_USER, "name" + random.substring(0, 12), 12);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT TRX_ID FROM INFORMATION_SCHEMA.INNODB_TRX WHERE TRX_MYSQL_THREAD_ID = CONNECTION_ID( );");
        System.out.println("User" + maps);
    }
}
