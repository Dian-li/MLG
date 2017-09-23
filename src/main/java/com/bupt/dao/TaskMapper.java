package com.bupt.dao;

import com.bupt.pojo.Task;

import java.util.List;
import java.util.Map;

public interface TaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Task record);

    int insertSelective(Task record);

    Task selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Task record);

    int updateByPrimaryKey(Task record);

    int updateByTaskName(Map paramMap);

    List<Map<String,Object>> selectTasks(String status);//根据状态取status

}