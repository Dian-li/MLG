package com.bupt.dao;

import com.bupt.pojo.Testcase;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TestcaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Testcase record);

    int insertSelective(Testcase record);

    Testcase selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Testcase record);

    int updateByPrimaryKey(Testcase record);

    List<Map<String,Object>> selectTestcase(Map paramMap);

    int deleteByNAME(String name);

    int updateTestcaseStatus(@Param("STATUS")String status,@Param("NAME")String testcaseName);

    Testcase selectTestcaseByName(String name);

    String selectStatDataPathByName(String name);
}