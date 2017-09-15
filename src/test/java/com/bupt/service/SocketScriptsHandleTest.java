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
public class SocketScriptsHandleTest {
    private static Logger logger = Logger.getLogger(SocketScriptsHandleTest.class);
    @Autowired
    SocketScriptsHandle socketScriptsHandle;
    @Test
    public void socketHandle() throws Exception {
        String dirname="/home/dian/code/nethandle-master/nethandle/src/csdn.pcap";
        String output = "/home/dian/code/MLG/out";
        String input = "csdn.pcap";
        this.socketScriptsHandle.socketHandle(dirname,input,output);

    }

}