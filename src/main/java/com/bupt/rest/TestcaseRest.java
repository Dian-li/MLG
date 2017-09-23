package com.bupt.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bupt.Enum.ResultEnum;
import com.bupt.pojo.Result;
import com.bupt.pojo.TestConf;
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
            resultJson.put("TESTCASES",map);
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
        //String testMechineIp = formParams.get("testmechineip").get(0);
        //String port = formParams.get("port").get(0);
        String testfile = formParams.get("testfile").get(0);
        String paramfile = formParams.get("paramfile").get(0);

        String mode = formParams.get("mode").get(0);//同步异步
        String timedelay = formParams.get("timedelay").get(0);
        String successThreshold = formParams.get("successThreshold").get(0);
        String pressureMode = formParams.get("pressureMode").get(0);
        String initPressure = formParams.get("initPressure").get(0);//初始压力
        String virtualUsers = formParams.get("virtualUsers").get(0);//并发用户
        String timeInterval = formParams.get("timeInterval").get(0);//时间间隔
        String step = formParams.get("step").get(0);
        String testTime = formParams.get("testTime").get(0);
        String targetPressure = formParams.get("targetPressure").get(0);//目标压力

        TestConf testConf = new TestConf();
        testConf.setMode(mode);
        testConf.setTimeDelayThreshold(timedelay);
        testConf.setPressureMode(pressureMode);
        testConf.setInitPressure(initPressure);
        testConf.setStep(step);
        testConf.setSuccessThreshold(successThreshold);
        testConf.setTimeInterval(timeInterval);
        testConf.setTestTime(testTime);
        testConf.setTargetPressure(targetPressure);
        testConf.setConfName(testcaseName);
        testConf.setVirtualUser(virtualUsers);

        Result result = new Result();
        try{
            this.testcaseService.addTestcase("dian",testcaseName,testfile,paramfile,remark,protocol,testConf);
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

    /**
     * select testcase info by the testcase name
     * @param name
     * @return
     */
    @GET
    @Path("getTestcaseInfo")
    @Produces("application/json")
    public Response getTestcaseInfo(@QueryParam("TESTCASENAME")String name){
        JSONObject resultObj = new JSONObject();
        Result result = new Result();
        try{
            Map<String,Object> map= this.testcaseService.getTestcaseInfoByName(name);
            result = ResultEnum.SUCCESS.getResult();
            resultObj.put("TESTCASE_INFO",map);

        }catch(Exception e){
            result.setErrText(e.getMessage());
            result.setErrCode(String.valueOf(e.hashCode()));
        }
        resultObj.put("RET_INFO",result);
        return ResponseUtil.SupportCORS(JSON.toJSONString(resultObj));
    }



}
