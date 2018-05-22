package com.bupt.dao;

import com.bupt.pojo.History_test;

public interface History_testMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(History_test record);

    int insertSelective(History_test record);

    History_test selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(History_test record);

    int updateByPrimaryKey(History_test record);
}