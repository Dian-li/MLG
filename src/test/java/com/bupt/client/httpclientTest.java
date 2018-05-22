package com.bupt.client;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class httpclientTest {
    @Autowired
    private Httpclient Httpclient;
    private static Logger logger = Logger.getLogger(httpclientTest.class);
    //@Test
    public void postParam() throws Exception {
        Map<String,String> map = new HashMap<String, String>();
        map.put("tps","2");
        map.put("duration","6");
        map.put("scriptname","temp.txt");
        this.Httpclient.postParam("http://0.0.0.0:13388/ptf/upload_params",map);
        //this.httpclient.postParam("http://127.0.0.1:8080/MLG/userMana/myTest", map);
    }

    //@Test
    public void postFile()throws Exception{
        Map<String,String> map = new HashMap<String, String>();
        map.put("tps","2");
        map.put("duration","6");
        map.put("scriptname","temp.txt");
        this.Httpclient.postParam("http://0.0.0.0:13388/ptf/upload_params",map);
        String filename = "/home/dian/temp.txt";
        this.Httpclient.postFile("http://0.0.0.0:13388/ptf/upload_file",filename);
    }

    @Test
    public void postReq()throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("tps","2");
        map.put("duration","6");
        map.put("scriptname","temp.txt");
        map.put("protocol","HTTP");
        String filename = "/home/dian/temp.txt";
        this.Httpclient.postRequest("http://0.0.0.0:13388/ptf/new_test",filename,map);
    }

}