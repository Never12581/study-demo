package com.hzx.data.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class TutorialController {


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String createEmployee() {
        String s = "哇哈哈哈哈";
        log.info(s);
        return s;
    }


}
