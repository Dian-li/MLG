package com.bupt.dao;

import com.bupt.pojo.User;
import org.apache.ibatis.binding.MapperMethod;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int authentication(String l_name,String password);

    List<Map<String,Object>> selectUsers(Map paramMap);

    Object login(Map paramMap);
}