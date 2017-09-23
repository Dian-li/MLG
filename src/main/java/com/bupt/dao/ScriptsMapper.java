package com.bupt.dao;

import com.bupt.pojo.Scripts;

import java.util.List;
import java.util.Map;

public interface ScriptsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Scripts record);

    int insertSelective(Scripts record);

    Scripts selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Scripts record);

    int updateByPrimaryKey(Scripts record);

    int deleteByScript(Scripts scripts);

    List<Map<String,Object>> selectScripts(Map paramMap);

    String selectPathByName(String scriptname);

    int selectScriptTimeByName(String scriptname);
}