package com.bupt.service;

import com.bupt.Enum.Address;
import com.bupt.Enum.ScriptType;
import com.bupt.Enum.TaskStatus;
import com.bupt.dao.TestcaseMapper;
import com.bupt.pojo.TestConf;
import com.bupt.pojo.Testcase;
import com.bupt.util.DateUtil;
import com.bupt.util.FileUtil;
import com.bupt.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class TestcaseService {
    @Autowired
    private TestcaseMapper testcaseMapper;

    /**
     * query testcase
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> getTestcase(Map<String,String[]> paramMap){
        Set set = new HashSet();
        set.add("NAME");
        set.add("DATE");
        set.add("STATUS");
        set.add("SCRIPTNAME");
        set.add("PARAMFILENAME");
        set.add("S_INDEX");
        set.add("MAX_NUM");
        Map map = MapUtil.ConvertMap(paramMap,set);
        MapUtil.setDefault(map,"S_INDEX",0);
        MapUtil.setDefault(map,"MAX_NUM",10);//default
        MapUtil.changeToInt(map,"S_INDEX");
        MapUtil.changeToInt(map,"MAX_NUM");
        return this.testcaseMapper.selectTestcase(map);
    }

    /**
     * add a new testcase
     * @param username
     * @param testcasename
     * @param scriptname
     * @param paramfile
     * @param remark
     * @throws Exception
     */
    public void addTestcase(String username,String testcasename,String scriptname,String paramfile,String remark,String protocol,TestConf testConf)throws Exception{
        String testcaseDir = Address.getUserTestcaseAddress(username, testcasename);
        FileUtil.createDir(testcaseDir);//make testcase dir
        String scriptSource = Address.getUserNotModulesScripts(username)+scriptname;
        String scriptDest = testcaseDir+"/"+scriptname;
        String paramfileSource = Address.getUserParamFile(username)+paramfile;
        String paramfileDest = testcaseDir+"/"+paramfile;
        if(scriptname!=null && !scriptname.equals("")){
            FileUtil.copyFileUsingFileStreams(scriptSource,scriptDest);
        }
        if(paramfile!=null && !paramfile.equals("")){
            FileUtil.copyFileUsingFileStreams(paramfileSource,paramfileDest);
        }
        Testcase testcase = new Testcase();
        testcase.setName(testcasename);
        testcase.setDate(DateUtil.getNowTime());
        testcase.setScriptname(scriptname);
        testcase.setScriptpath(scriptDest);
        testcase.setParamfilename(paramfile);
        testcase.setParamfilepath(paramfileDest);
        testcase.setRemark(remark);
        testcase.setStatus(TaskStatus.READY.getStatus());
        testcase.setStatdatapath(Address.getUserData(username,testcasename));
        //testcase.setIp(ip);
        //testcase.setPort(port);
        testcase.setProtocol(protocol);
        testcase.setUserid(1);
        String path = Address.getUserTestConfAddress(username,testConf.getConfName());
        this.WriteProperties(path,testConf);//写入配置文件，名称与测试用例名称相同
        testcase.setTestenvpath(path);
        this.testcaseMapper.insertSelective(testcase);

    }

    public Map<String,Object> getTestcaseInfoByName(String testcasename)throws Exception{
        Testcase testcase = this.testcaseMapper.selectTestcaseByName(testcasename);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("DATE",testcase.getDate());
        map.put("TESTCASENAME",testcase.getName());
        map.put("PARAMFILENAME",testcase.getParamfilename());
        map.put("PROTOCOL",testcase.getProtocol());
        map.put("REMARK",testcase.getRemark());
        map.put("SCRIPTNAME",testcase.getScriptname());
        String path = testcase.getTestenvpath();
        TestConf testConf = this.ReadProperties(path);
        map.put("initPressure",testConf.getInitPressure());
        map.put("virtualUser",testConf.getVirtualUser());
        map.put("mode",testConf.getMode());
        map.put("pressureMode",testConf.getPressureMode());
        map.put("step",testConf.getStep());
        map.put("successThreshold",testConf.getSuccessThreshold());
        map.put("targetPressure",testConf.getTargetPressure());
        map.put("testTime",testConf.getTestTime());
        map.put("timeDelayThreshold",testConf.getTimeDelayThreshold());
        map.put("timeInterval",testConf.getTimeInterval());
        return map;

    }

    private void WriteProperties(String path,TestConf testConf)throws FileNotFoundException,IOException{
        Properties pro = new Properties();
        //String path = Address.getUserTestConfAddress(username,testConf.getConfName());
        FileUtil.createFile(path);//新建文件
        FileOutputStream oFile = new FileOutputStream( path);
        pro.setProperty("initPressure",testConf.getInitPressure());
        pro.setProperty("virtualUser",testConf.getVirtualUser());
        pro.setProperty("mode",testConf.getMode());
        pro.setProperty("pressureMode",testConf.getPressureMode());
        pro.setProperty("step",testConf.getStep());
        pro.setProperty("successThreshold",testConf.getSuccessThreshold());
        pro.setProperty("targetPressure",testConf.getTargetPressure());
        pro.setProperty("testTime",testConf.getTestTime());
        pro.setProperty("timeDelayThreshold",testConf.getTimeDelayThreshold());
        pro.setProperty("timeInterval",testConf.getTimeInterval());
        pro.store(oFile, "Comment");
        oFile.close();
    }

    private TestConf ReadProperties(String path)throws FileNotFoundException,IOException{
        Properties prop = new Properties();
        InputStream in = new BufferedInputStream(new FileInputStream(path));
        prop.load(in);     ///加载属性列表
        TestConf testConf = new TestConf();
        testConf.setInitPressure(prop.getProperty("initPressure"));
        testConf.setVirtualUser(prop.getProperty("virtualUser"));
        testConf.setMode(prop.getProperty("mode"));
        testConf.setPressureMode(prop.getProperty("pressureMode"));
        testConf.setStep(prop.getProperty("step"));
        testConf.setSuccessThreshold(prop.getProperty("successThreshold"));
        testConf.setTargetPressure(prop.getProperty("targetPressure"));
        testConf.setTestTime(prop.getProperty("testTime"));
        testConf.setTimeDelayThreshold(prop.getProperty("timeDelayThreshold"));
        testConf.setTimeInterval(prop.getProperty("timeInterval"));

        return testConf;

    }

    /**
     * delete testcase name
     * @param testcasename
     */
    public void deleteTestcase(String username,String testcasename){
        String testcaseDir = Address.getUserTestcaseAddress(username, testcasename);
        FileUtil.deleteDirectory(testcaseDir);
        this.testcaseMapper.deleteByNAME(testcasename);
    }

    public int updateStatus(String status,String testcasename){
        return this.testcaseMapper.updateTestcaseStatus(status, testcasename);
    }


    public String selectDataPathByName(String name){
        return this.testcaseMapper.selectStatDataPathByName(name);
    }
}
