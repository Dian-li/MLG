package com.bupt.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bupt.Enum.ResultEnum;
import com.bupt.pojo.Result;
import com.bupt.pojo.User;
import com.bupt.service.ResultService;
import com.bupt.service.UserService;
import com.bupt.util.MapUtil;
import com.bupt.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Max;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dian on 2017/8/3.
 */
@Component
@Path("/userMana")
public class UserRest {
    @Autowired
    private UserService userService;
    @Autowired
    private ResultService resultService;
    @GET
    @Path("/listUser")
    @Produces("application/json")
    public Response userGet(@Context HttpServletRequest httpServletRequest){

        Map<String,String[]> paramMap = httpServletRequest.getParameterMap();


        JSONObject resultJson = new JSONObject();
        Result result = new Result();
        try{
            List<Map<String,Object>> map = this.userService.selectUsers(paramMap);
            List<String> columns = MapUtil.getColumns(map);
            resultJson.put("USER",map);
            resultJson.put("COLUMNS",columns);
            result = ResultService.Success();

        }catch(Exception e){
            result.setErrText(e.getMessage());
            result.setErrCode(String.valueOf(e.hashCode()));
        }
        resultJson.put("RET_INFO",result);

        return ResponseUtil.SupportCORS(JSON.toJSONString(resultJson));
    }
    @POST
    @Path("/addUser")
    @Produces("application/json")
    public Response userAdd(@FormParam("l_name") String username,@FormParam("password")String password){
        System.out.println(username);
        System.out.println(password);
        Result result = new Result();
        if(this.userService.addUser(username,password)){
            result.setIndex("0");
            result.setErrCode("0");
            result.setErrText("success_utf8");
        }else{
            result.setIndex("0");
            result.setErrCode("1");
            result.setErrText("插入错误");
        }
        return ResponseUtil.SupportCORS(result);
    }
    @PUT
    @Path("/userModify")
    @Produces("application/json")
    public void userModify(){

    }

    @DELETE
    @Path("/userDelete")
    @Produces("application/json")
    public void userDelete(){

    }

    @POST
    @Path("userLogin")
    @Produces("application/json")
    public Response userLogin(@FormParam("L_NAME")String username,@FormParam("PASSWORD")String password){
        Result result = new Result();
        if(username==null || password==null){
            result = ResultEnum.LOGINERROR.getResult();
            return ResponseUtil.SupportCORS(result);
        }

        try{
            Object id = this.userService.login(username,password);
            if(id==null){//出错
                result = ResultEnum.LOGINERROR.getResult();
            }else{
                result = ResultEnum.SUCCESS.getResult();

            }
        }catch (Exception e){
            result = this.resultService.Error(String.valueOf(e.hashCode()),e.getMessage());
        }
        return ResponseUtil.SupportCORS(result);
    }
}
