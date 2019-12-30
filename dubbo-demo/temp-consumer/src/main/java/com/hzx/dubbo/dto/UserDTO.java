package com.hzx.dubbo.dto;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 15:06 2019/12/28
 */
@Getter
@Setter
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserDTO implements Serializable {
    private String id;
    private String name;
    private int age;
    private Date time = new Date();
}
