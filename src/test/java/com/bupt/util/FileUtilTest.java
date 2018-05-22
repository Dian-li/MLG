package com.bupt.util;

import com.bupt.Enum.Address;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class FileUtilTest {
    private static Logger logger = Logger.getLogger(FileUtilTest.class);
    //@Test
    public void getTotalLines() throws Exception {
        String filename = Address.getUserData("dian","mytest")+"/cpu.txt";
        File file  = new File(filename);
        //logger.info(FileUtil.getTotalLines(file));
    }

    @Test
    public void getLineString()throws IOException{
        String filename = Address.getUserData("dian","mytest")+"/cpu.txt";
        File file  = new File(filename);
        //logger.info(FileUtil.readAppointedLineNumber(file,1,4));
    }

}