package com.bupt.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bupt.Enum.Address;
import com.bupt.Enum.ResultEnum;
import com.bupt.Enum.TaskStatus;
import com.bupt.client.Httpclient;
import com.bupt.pojo.Result;
import com.bupt.service.DataCollectionService;
import com.bupt.service.TaskService;
import com.bupt.service.TestcaseService;
import com.bupt.util.DateUtil;
import com.bupt.util.RedisUtil;
import com.bupt.util.ResponseUtil;
import com.sun.jersey.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Component
@Path("/test")
public class TestRest {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private DataCollectionService dataCollectionService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TestcaseService testcaseService;

    @GET
    @Path("/dataCollection")
    @Produces("application/json")
    public Response getRedisData(@QueryParam("TESTCASENAME") String testcaseName) {
        JSONObject resultJson = new JSONObject();
        Result result = new Result();
        try {
            Map<Object, Object> cpuMap = this.redisUtil.getAllKeyValue(testcaseName + "_CPU");
            Map<Object, Object> ioMap = this.redisUtil.getAllKeyValue(testcaseName + "_IO");
            Map<Object, Object> netMap = this.redisUtil.getAllKeyValue(testcaseName + "_NET");
            Map<Object, Object> memMap = this.redisUtil.getAllKeyValue(testcaseName + "_MEM");
            JSONObject cpuObject = new JSONObject();
            JSONObject ioObject = new JSONObject();
            JSONObject netObject = new JSONObject();
            JSONObject memObject = new JSONObject();
            JSONObject delayObject = new JSONObject();
            if (cpuMap.size() != 0) {
                String time = cpuMap.get("time").toString();
                String date = cpuMap.get("date").toString();
                String cpu = cpuMap.get("cpu").toString();
                cpuObject.put("DATE", date);
                cpuObject.put("TIME", time);
                cpuObject.put("CPU", cpu);
            }
            if (ioMap.size() != 0) {
                float read = 0;
                float write = 0;
                ioObject.put("READ", ioMap.get("sda_read"));
                ioObject.put("WRITE", ioMap.get("sda_write"));
                ioObject.put("TIME", ioMap.get("time"));
                ioObject.put("DATE", ioMap.get("date"));
            }

            if (netMap.size() != 0) {
                float send = 0;
                float recv = 0;
                for (Map.Entry entry : netMap.entrySet()) {
                    String key = entry.getKey().toString();
                    if (key != null && key.indexOf("send") != -1) {
                        float value = Float.valueOf(entry.getValue().toString());
                        send += value;
                    }
                    if (key != null && key.indexOf("recv") != -1) {
                        float value = Float.valueOf(entry.getValue().toString());
                        recv += value;
                    }
                }
                netObject.put("SEND", send);
                netObject.put("RECV", recv);
                netObject.put("TIME", ioMap.get("time"));
                netObject.put("DATE", ioMap.get("date"));
            }
            if (memMap.size() != 0) {
                memObject.put("DATE", memMap.get("date"));
                memObject.put("TIME", memMap.get("time"));
                memObject.put("MEM", memMap.get("mem"));
                memObject.put("VMEM", memMap.get("vmem"));
            }
            String avg_delay = this.redisUtil.getStringKey("avg_delay");
            String max_delay = this.redisUtil.getStringKey("max_delay");
            String min_delay = this.redisUtil.getStringKey("min_delay");
            if (avg_delay != null && avg_delay.length() != 0)
                delayObject.put("avg_delay", avg_delay);
            if (max_delay != null && max_delay.length() != 0)
                delayObject.put("max_delay", max_delay);
            if (min_delay != null && min_delay.length() != 0)
                delayObject.put("min_delay", min_delay);
            resultJson.put("CPU", cpuObject);
            resultJson.put("IO", ioObject);
            resultJson.put("MEM", memObject);
            resultJson.put("NET", netObject);
            resultJson.put("INDICATOR", delayObject);
            result = ResultEnum.SUCCESS.getResult();
        } catch (Exception e) {
            result.setErrText(e.getMessage());
            result.setIndex("-1");
            result.setErrCode(String.valueOf(e.hashCode()));
        }
        resultJson.put("RET_INFO", result);
        return ResponseUtil.SupportCORS(resultJson.toJSONString());
    }

    @POST
    @Path("/testBegin")
    @Produces("application/json")
    public Response startTest(@FormParam("TESTCASENAME") String testcaseName,
                              @FormParam("PROTOCOL") String protocol,
                              @FormParam("REMARK") String remark,
                              @FormParam("SCRIPTNAME") String scriptName,
                              @FormParam("PARAMFILENAME") String paramfileName,
                              @FormParam("CONCURRENCE") Integer concurrentMode,
                              @FormParam("TIMEDELAY") Integer timeDelay,
                              @FormParam("THRESHOLD") Integer threshold,
                              @FormParam("PRESSUREMODE") Integer pressureMode,
                              @FormParam("TPS") Integer tps,
                              @FormParam("STEP") Integer step,
                              @FormParam("DURATION") Integer duration,
                              @FormParam("TARGET") Integer targetTps,
                              @FormParam("IP[]") List<String> ipList,
                              @FormParam("RATIO[]") List<Float> ratioList) {
        Result result = new Result();
        try {
            //this.testcaseService.updateStatus(TaskStatus.RUNNING.getStatus(),testcaseName);
            this.dataCollectionService.startCollect("dian", testcaseName,
                    protocol, remark, scriptName, paramfileName, concurrentMode, timeDelay,
                    threshold, pressureMode, tps, step, duration, targetTps, ipList, ratioList);
            result = ResultEnum.SUCCESS.getResult();
        } catch (Exception e) {
            result.setErrCode(String.valueOf(e.hashCode()));
            result.setIndex("-1");
            result.setErrText(e.getMessage());
        }
        return ResponseUtil.SupportCORS(result);
    }

    @POST
    @Path("/testEnd")
    @Produces("application/json")
    public Response endTest(@FormParam("TESTCASENAME") String testcaseName) {
        Result result = new Result();
        try {
            this.dataCollectionService.stopCollect();
            this.testcaseService.updateStatus(TaskStatus.FINISH.getStatus(), testcaseName);
            this.redisUtil.deleteKey(testcaseName + "_CPU");
            this.redisUtil.deleteKey(testcaseName + "_IO");
            this.redisUtil.deleteKey(testcaseName + "_NET");
            this.redisUtil.deleteKey(testcaseName + "_MEM");
            this.redisUtil.deleteKey("avg_delay");
            this.redisUtil.deleteKey("min_delay");
            this.redisUtil.deleteKey("max_delay");
            result = ResultEnum.SUCCESS.getResult();
        } catch (Exception e) {
            result.setErrCode(String.valueOf(e.hashCode()));
            result.setIndex("-1");
            result.setErrText(e.getMessage());
        }
        return ResponseUtil.SupportCORS(result);
    }

    @GET
    @Path("listTasks")
    @Produces("application/json")
    public Response listTask(@QueryParam("STATUS") String status) {
        JSONObject resultJson = new JSONObject();
        Result result = new Result();
        List<Map<String, Object>> tasks = null;
        try {
            if (status.equals("Running"))
                tasks = this.taskService.listTasks(TaskStatus.RUNNING.getStatus());
            else if (status.equals("Finish"))
                tasks = this.taskService.listTasks(TaskStatus.FINISH.getStatus());
            result = ResultEnum.SUCCESS.getResult();
            resultJson.put("TASKS", tasks);

        } catch (Exception e) {
            result.setIndex("-1");
            result.setErrText(e.getMessage());
            result.setErrCode(String.valueOf(e.hashCode()));
        }
        resultJson.put("RET_INFO", result);
        return ResponseUtil.SupportCORS(JSON.toJSONString(resultJson));
    }

    @GET
    @Path("historyTasks")
    @Produces("application/json")
    public Response historyTest(@QueryParam("TESTCASENAME") String testcasename) {
        return null;
    }
}
