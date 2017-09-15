package com.bupt.util;

import com.bupt.service.SocketScriptsHandleTest;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class RedisUtilTest {

    private static Logger logger = Logger.getLogger(RedisUtilTest.class);
    @Autowired
    RedisUtil redisUtil;
    @Test
    public void exists() throws Exception {
        logger.info(this.redisUtil.exists("IO","date"));
    }

    @Test
    public void get() throws Exception {
        logger.info(this.redisUtil.get("CPU","cpu"));
    }

    @Test
    public void set() throws Exception {
        this.redisUtil.set("IO","date","asddasd");
    }
    @Test
    public void getKeySize(){
        logger.info(this.redisUtil.getKeySize("IO"));
    }

    @Test
    public void getAllKey(){
        logger.info(this.redisUtil.getAllKeyValue("IO"));
    }


}