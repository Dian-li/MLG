package com.bupt.service;

import com.bupt.client.Httpclient;
import com.bupt.dao.Exe_moduleMapper;
import com.bupt.dao.ModulesMapper;
import com.bupt.pojo.Exe_module;
import com.bupt.pojo.Modules;
import com.bupt.pojo.Result;
import com.bupt.util.FileUtil;
import com.bupt.util.MapUtil;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ModuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleService.class);

    @Autowired
    private ModulesMapper modulesMapper;
    @Autowired
    private Httpclient httpclient;

    public void addModule(String address,int parallel){

        Modules modules = new Modules();
        modules.setAddress(address);
        modules.setParallel(parallel);
        modules.setStatus("Ready");
        this.modulesMapper.insertSelective(modules);
    }

    public List<Map<String,Object>> getModules(Map<String,String[]> paramMap){
        Set set = new HashSet();
        set.add("ADDRESS");
        set.add("STATUS");
        set.add("PARALLEL");
        Map map = MapUtil.ConvertMap(paramMap,set);
        MapUtil.setDefault(map,"S_INDEX",0);
        MapUtil.setDefault(map,"MAX_NUM",10);//default
        MapUtil.changeToInt(map,"S_INDEX");
        MapUtil.changeToInt(map,"MAX_NUM");
        return this.modulesMapper.selectModules(map);

    }

    public int deleteModule(String address){
        return this.modulesMapper.deleteByAddress(address);
    }

    public Result testModule(String host){
        Result result = new Result();
        try{
            HttpResponse response = httpclient.postParam(host,null);
            result.setErrCode(String.valueOf(response.getStatusLine().getStatusCode()));
            result.setErrText(FileUtil.inputStreamToString(response.getEntity().getContent()));
            result.setIndex("0");
        }catch (Exception e){
            result.setErrCode("404");
            result.setIndex("-1");
            result.setErrText("post request fail:"+e.getMessage());
            LOGGER.error("post request fail:{}",e.getMessage());
        }
        return result;
    }
}
