package com.bupt.service;

import com.bupt.dao.Exe_moduleMapper;
import com.bupt.dao.ModulesMapper;
import com.bupt.pojo.Exe_module;
import com.bupt.pojo.Modules;
import com.bupt.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ModuleService {
    @Autowired
    private ModulesMapper modulesMapper;

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
}
