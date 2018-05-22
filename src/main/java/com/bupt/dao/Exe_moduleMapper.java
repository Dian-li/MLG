package com.bupt.dao;

import com.bupt.pojo.Exe_module;

public interface Exe_moduleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Exe_module record);

    int insertSelective(Exe_module record);

    Exe_module selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Exe_module record);

    int updateByPrimaryKey(Exe_module record);
}