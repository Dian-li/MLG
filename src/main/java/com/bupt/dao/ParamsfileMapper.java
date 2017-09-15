package com.bupt.dao;

import com.bupt.pojo.Paramsfile;
import com.bupt.pojo.Scripts;

import java.util.List;
import java.util.Map;

public interface ParamsfileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Paramsfile record);

    int insertSelective(Paramsfile record);

    Paramsfile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Paramsfile record);

    int updateByPrimaryKey(Paramsfile record);

    List<Map<String,Object>> selectParamFile(Map paramMap);

    int deleteByParamFile(Paramsfile paramsfile);
}