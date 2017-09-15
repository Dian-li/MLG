package com.bupt.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class httpScriptsHandleTest {
    private static Logger logger = Logger.getLogger(httpScriptsHandleTest.class);
    @Autowired
    HttpScriptsHandle httpScriptsHandle;
    @Test
    public void httpHandle() throws Exception {
        //System.load("/home/dian/code/MLG/src/main/jnalibs/libhttpScript.so");
        String dirname="/home/dian/code/httpHandle/csdn2.saz";
        String output = "/home/dian/code/MLG/out";
        String input = "csdn2.saz";
        this.httpScriptsHandle.httpHandle(dirname,input,output);
    }

}