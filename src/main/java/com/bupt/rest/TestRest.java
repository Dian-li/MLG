package com.bupt.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bupt.Enum.ResultEnum;
import com.bupt.pojo.Result;
import com.bupt.service.DataCollectionService;
import com.bupt.util.RedisUtil;
import com.bupt.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Map;
@Component
@Path("/test")
public class TestRest {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private DataCollectionService dataCollectionService;

    @GET
    @Path("/dataCollection")
    @Produces("application/json")
    public Response getRedisData() {
        JSONObject resultJson = new JSONObject();
        Result result = new Result();
        try{
            Map<Object,Object> cpuMap= this.redisUtil.getAllKeyValue("CPU");
            Map<Object,Object> ioMap= this.redisUtil.getAllKeyValue("IO");
            Map<Object,Object> netMap = this.redisUtil.getAllKeyValue("NET");
            Map<Object,Object> memMap = this.redisUtil.getAllKeyValue("MEM");
            JSONObject cpuObject = new JSONObject();
            JSONObject ioObject = new JSONObject();
            JSONObject netObject = new JSONObject();
            JSONObject memObject = new JSONObject();
            if(cpuMap.size()!=0){
                String time = cpuMap.get("time").toString();
                String date = cpuMap.get("date").toString();
                String cpu = cpuMap.get("cpu").toString();
                cpuObject.put("DATE",date);
                cpuObject.put("TIME",time);
                cpuObject.put("CPU",cpu);
            }
            if(ioMap.size()!=0){
                float read = 0;
                float write = 0;
                ioObject.put("READ",ioMap.get("sda_read"));
                ioObject.put("WRITE",ioMap.get("sda_write"));
                ioObject.put("TIME",ioMap.get("time"));
                ioObject.put("DATE",ioMap.get("date"));
            }

            if(netMap.size()!=0){
                float send = 0;
                float recv = 0;
                for(Map.Entry entry:netMap.entrySet()){
                    String key = entry.getKey().toString();
                    if(key!=null &&key.indexOf("send")!=-1){
                        float value = Float.valueOf(entry.getValue().toString());
                        send+=value;
                    }
                    if(key!=null && key.indexOf("recv")!=-1){
                        float value = Float.valueOf(entry.getValue().toString());
                        recv+=value;
                    }
                }
                netObject.put("SEND",send);
                netObject.put("RECV",recv);
                netObject.put("TIME",ioMap.get("time"));
                netObject.put("DATE",ioMap.get("date"));
            }
            if(memMap.size()!=0){
                memObject.put("DATE",memMap.get("date"));
                memObject.put("TIME",memMap.get("time"));
                memObject.put("MEM",memMap.get("mem"));
                memObject.put("VMEM",memMap.get("vmem"));
            }
            resultJson.put("CPU",cpuObject);
            resultJson.put("IO",ioObject);
            resultJson.put("MEM",memObject);
            resultJson.put("NET",netObject);
            result = ResultEnum.SUCCESS.getResult();
        }catch(Exception e){
            result.setErrText(e.getMessage());
            result.setIndex("-1");
            result.setErrCode(String.valueOf(e.hashCode()));
        }
        resultJson.put("RET_INFO",result);
        return ResponseUtil.SupportCORS(resultJson.toJSONString());
    }

    @GET
    @Path("/testBegin")
    @Produces("application/json")
    public Response startTest(){
        Result result = new Result();
        try{
            this.dataCollectionService.startCollect();
            result = ResultEnum.SUCCESS.getResult();
        }catch (Exception e){
            result.setErrCode(String.valueOf(e.hashCode()));
            result.setIndex("-1");
            result.setErrText(e.getMessage());
        }
        return ResponseUtil.SupportCORS(result);
    }

    @GET
    @Path("/testEnd")
    @Produces("application/json")
    public Response endTest(){
        Result result = new Result();
        try{
            this.dataCollectionService.stopCollect();
            result = ResultEnum.SUCCESS.getResult();
        }catch (Exception e){
            result.setErrCode(String.valueOf(e.hashCode()));
            result.setIndex("-1");
            result.setErrText(e.getMessage());
        }
        return ResponseUtil.SupportCORS(result);
    }
}
