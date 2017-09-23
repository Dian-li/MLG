package com.bupt.service;

import com.bupt.Enum.Address;
import com.bupt.Enum.TaskStatus;
import com.bupt.dao.TaskMapper;
import com.bupt.pojo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    @Autowired
    private TaskMapper taskMapper;

    /**
     * 增加task
     * @param testcaseName
     * @param userid
     * @param username
     * @param time
     */
    public void addTask(String testcaseName,int userid,String username,String time){

        Task task = new Task();
        task.setTestcasename(testcaseName);
        task.setConfigurepath(Address.getUserTestConfAddress(username, testcaseName));
        task.setStatus(TaskStatus.RUNNING.getStatus());
        task.setBegintime(time);
        task.setUserid(userid);
        this.taskMapper.insertSelective(task);//存储task

    }

    public void updateTaskStop(String testcaseName){
        Map haspMap = new HashMap();
        haspMap.put("status",TaskStatus.FINISH.getStatus());
        haspMap.put("testcasename",testcaseName);
        this.taskMapper.updateByTaskName(haspMap);
    }

    /**
     * 根据状态查询task
     * @param status
     * @return
     */
    public List<Map<String,Object>>  listTasks(String status){
        return this.taskMapper.selectTasks(status);
    }

    /**
     * search the path of dataCollection file by testcase name
     * @param testcase
     * @return
     */
//    public String searchDataPath(String testcase){
//
//    }


}
