package com.hzx.monitor.service;

import com.hzx.monitor.config.MailInfoConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 20:53 2019/8/1
 */
@Service
public class MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private MailInfoConfig mailInfoConfig;

    public void sendMail(String subject, String context) {
        sendMail(mailInfoConfig.getFromMail(), subject, context, mailInfoConfig.getToMail());
    }

    public void sendMail(String fromMail, String subject, String context, String... toMail) {
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toMail);
            helper.setFrom(fromMail);
            helper.setSubject(subject);
            helper.setText(context);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            logger.error("邮件发送error!" + e);
            logger.error("fromMail:{} \n subject:{} \n context:{} \n toMail:{}", fromMail, subject, context, toMail);
        }
    }

}
