package com.hzx.transactional.controller;

import com.hzx.transactional.service.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource
    FileService fileService ;

    @GetMapping
    public String test() {
        fileService.insertFile();
        return "测试结束";
    }

}
