package com.houke.community;

import com.houke.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        for(int i=0;i<10;i++)
        mailClient.sendMail("1160377992@qq.com", "阿欣", "一个迷人的混蛋");
    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "迷人的小混蛋");

        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);

        mailClient.sendMail("1160377992@qq.com", "HTML", content);
    }
}
