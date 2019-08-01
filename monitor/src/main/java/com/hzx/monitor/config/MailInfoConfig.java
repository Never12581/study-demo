package com.hzx.monitor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 20:44 2019/8/1
 */
@Configuration
public class MailInfoConfig {

    @Value("${monitor.from.mail.username}")
    private String fromMail ;
    @Value("${monitor.to.mail.username}")
    private String toMail ;

    public String getFromMail() {
        return fromMail;
    }

    public MailInfoConfig setFromMail(String fromMail) {
        this.fromMail = fromMail;
        return this;
    }

    public String getToMail() {
        return toMail;
    }

    public MailInfoConfig setToMail(String toMail) {
        this.toMail = toMail;
        return this;
    }
}
