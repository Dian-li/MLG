package com.bupt.service;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class DataCollectionServiceTest {
    private static Logger logger = Logger.getLogger(DataCollectionServiceTest.class);
    @Autowired
    DataCollectionService dataCollectionService;



    @Test
    public void start() {
        try{
            this.dataCollectionService.startCollect("dian","test");
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }


    //@After
    public void stop(){
        try{
            Thread.sleep(3000);
            this.dataCollectionService.stopCollect();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}