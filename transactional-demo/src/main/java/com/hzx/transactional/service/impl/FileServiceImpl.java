package com.hzx.transactional.service.impl;

import com.hzx.transactional.constants.Constants;
import com.hzx.transactional.service.FileService;
import com.hzx.transactional.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private UserService userService ;

    @Override
//    @Transactional(propagation= Propagation.SUPPORTS)
    public void insertFile() {
        String random = UUID.randomUUID().toString();
        jdbcTemplate.update(Constants.INSERT_FILE, "name" + random.substring(0, 12), "ext" + random.substring(12, 24), "addr" + random.substring(24));
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT TRX_ID FROM INFORMATION_SCHEMA.INNODB_TRX WHERE TRX_MYSQL_THREAD_ID = CONNECTION_ID( );");
        System.out.println("files --->" + TransactionSynchronizationManager.getCurrentTransactionName());
        System.out.println("files ===> "+maps);

        userService.insertUser();

    }
}
