package com.bupt.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bupt.Enum.ResultEnum;
import com.bupt.pojo.Result;
import com.bupt.service.ResultService;
import com.bupt.service.TestcaseService;
import com.bupt.util.MapUtil;
import com.bupt.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Component
@Path("/testcaseMana")
public class TestcaseRest {
    @Autowired
    private TestcaseService testcaseService;
    @GET
    @Path("/listTestcase")
    @Produces("application/json")
    public Response getTestcase(@Context HttpServletRequest httpServletRequest){
        Map<String,String[]> paramMap = httpServletRequest.getParameterMap();

        JSONObject resultJson = new JSONObject();
        Result result = new Result();
        try{
            List<Map<String,Object>> map = this.testcaseService.getTestcase(paramMap);
            List<String> columns;
            if(map.size()>0){
                columns = MapUtil.getColumns(map);
            }else{
                columns=null;
            }
            resultJson.put("TESTCASES",map);
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
    @Path("/addTestcase")
    @Produces("application/json")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addTestcase(MultivaluedMap<String, String> formParams){
        String testcaseName = formParams.get("testname").get(0);
        String protocol = formParams.get("protocol").get(0);
        String remark = formParams.get("remark").get(0);
        String testMechineIp = formParams.get("testmechineip").get(0);
        String port = formParams.get("port").get(0);
        String testfile = formParams.get("testfile").get(0);
        String paramfile = formParams.get("paramfile").get(0);
        String mode = formParams.get("mode").get(0);//同步异步
        String timedelay = formParams.get("timedelay").get(0);
        String successThreshold = formParams.get("successThreshold").get(0);
        String pressureMode = formParams.get("pressureMode").get(0);
        String initPressure = formParams.get("initPressure").get(0);//初始压力
        String timeInterval = formParams.get("timeInterval").get(0);//时间间隔
        String step = formParams.get("step").get(0);
        String testTime = formParams.get("testTime").get(0);
        String targetPressure = formParams.get("targetPressure").get(0);//目标压力
        String threadPoolsize = formParams.get("threadPoolsize").get(0);//线程池大小
        String testUsername = formParams.get("testUsername").get(0);
        String testPassword = formParams.get("testPassword").get(0);
        String dataTime = formParams.get("dataTime").get(0);//采集间隔

        Result result = new Result();
        try{
            this.testcaseService.addTestcase("dian",testcaseName,testcaseName,testcaseName,remark,testMechineIp,port,protocol);
            result = ResultEnum.SUCCESS.getResult();

        }catch(Exception e){
            result.setErrText(e.getMessage());
            result.setErrCode(String.valueOf(e.hashCode()));
        }
        return ResponseUtil.SupportCORS(result);
    }
    @POST
    @Path("/deleteTestcase")
    @Produces("application/json")
    public Response deleteTestcase(@FormParam("TESTCASENAME")String testcasename){
        Result result = new Result();
        try{
            this.testcaseService.deleteTestcase("dian",testcasename);
            result = ResultEnum.SUCCESS.getResult();

        }catch(Exception e){
            result.setErrText(e.getMessage());
            result.setErrCode(String.valueOf(e.hashCode()));
        }
        return ResponseUtil.SupportCORS(result);
    }
}
