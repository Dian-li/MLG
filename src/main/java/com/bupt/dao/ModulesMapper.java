package com.bupt.dao;

import com.bupt.pojo.Modules;

import java.util.List;
import java.util.Map;

public interface ModulesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Modules record);

    int insertSelective(Modules record);

    Modules selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Modules record);

    int updateByPrimaryKey(Modules record);

    List<Map<String,Object>> selectModules(Map paramMap);

    int deleteByAddress(String address);//根据地址删除模块
}