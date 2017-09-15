package com.bupt.service;

import com.bupt.Enum.Address;
import com.bupt.Enum.ScriptType;
import com.bupt.dao.TestcaseMapper;
import com.bupt.pojo.Testcase;
import com.bupt.util.DateUtil;
import com.bupt.util.FileUtil;
import com.bupt.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public void addTestcase(String username,String testcasename,String scriptname,String paramfile,String remark,String ip,String port,String protocol)throws Exception{
        String testcaseDir = Address.getUserTestcaseAddress(username, testcasename);
        FileUtil.createDir(testcaseDir);//make testcase dir
        String scriptSource = Address.getUserTestAddress(username)+scriptname;
        String scriptDest = testcaseDir+"/"+scriptname;
        String paramfileSource = Address.getUserParamFile(username)+paramfile;
        String paramfileDest = testcaseDir+"/"+paramfile;
        FileUtil.copyFileUsingFileStreams(scriptSource,scriptDest);
        FileUtil.copyFileUsingFileStreams(paramfileSource,paramfileDest);
        Testcase testcase = new Testcase();
        testcase.setName(testcasename);
        testcase.setDate(DateUtil.getNowTime());
        testcase.setScriptname(scriptname);
        testcase.setScriptpath(scriptDest);
        testcase.setParamfilename(paramfile);
        testcase.setParamfilepath(paramfileDest);
        testcase.setRemark(remark);
        testcase.setIp(ip);
        testcase.setPort(port);
        testcase.setProtocol(protocol);
        testcase.setUserid(1);
        this.testcaseMapper.insertSelective(testcase);
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
}
