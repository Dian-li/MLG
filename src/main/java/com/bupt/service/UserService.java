package com.bupt.service;

import com.bupt.Enum.Address;
import com.bupt.dao.UserMapper;
import com.bupt.pojo.User;
import com.bupt.util.FileUtil;
import com.bupt.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Dian on 2017/8/3.
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 鉴权
     * @param l_name
     * @param password
     * @return
     */
    public boolean authentication(String l_name,String password){
        if(this.userMapper.authentication(l_name,password)==0){//未查到
            return false;
        }else{
            return true;
        }
    }

    /**
     * 增加用户
     * @param username
     * @param password
     * @return
     */
    public boolean addUser(String username,String password){
        User user = new User();
        user.setlName(username);
        user.setPassword(password);
        if(this.userMapper.insertSelective(user)>0){
            FileUtil.createDir(Address.ROOT.getAddress()+username+Address.ORIGINAL.getAddress());//创建用户目录
            FileUtil.createDir(Address.ROOT.getAddress()+username+Address.TEST.getAddress());
            return true;
        }else{
            return false;
        }
    }

    /**
     * 查询用户
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> selectUsers(Map<String,String[]> paramMap){

        Set set = new HashSet();
        set.add("ROLE");
        set.add("DEPT");
        set.add("PHONE");
        set.add("POST");
        set.add("EMAIL");
        set.add("S_INDEX");
        set.add("MAX_NUM");
        Map map = MapUtil.ConvertMap(paramMap,set);
        MapUtil.setDefault(map,"S_INDEX",0);
        MapUtil.setDefault(map,"MAX_NUM",0);
        MapUtil.changeToInt(map,"S_INDEX");
        MapUtil.changeToInt(map,"MAX_NUM");

        return this.userMapper.selectUsers(map);
    }

    public Object login(String username,String password){
        Map map = new HashMap();
        map.put("lName",username);
        map.put("password",password);
        return  this.userMapper.login(map);
    }
}
