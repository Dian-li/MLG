package com.bupt.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class UserServiceTest {
    @Autowired
    UserService userService;
    private static Logger logger = Logger.getLogger(UserServiceTest.class);
    @Test
    public void login() throws Exception {
        logger.info(this.userService.login("dian","1234"));
    }

}