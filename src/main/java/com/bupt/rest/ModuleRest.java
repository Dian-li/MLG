package com.bupt.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bupt.Enum.ResultEnum;
import com.bupt.pojo.Result;
import com.bupt.service.ModuleService;
import com.bupt.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Component
@Path("/moduleMana")
public class ModuleRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleRest.class);

    @Autowired
    private ModuleService moduleService;

    @POST
    @Path("/addModule")
    @Produces("application/json")
    public Response addModule(@FormParam("ADDRESS") String address, @FormParam("PARALLEL") int parallel) {
        Result result = new Result();
        try {
            this.moduleService.addModule(address, parallel);
            result = ResultEnum.SUCCESS.getResult();

        } catch (Exception e) {
            result.setErrText(e.getMessage());
            result.setErrCode(String.valueOf(e.hashCode()));
        }
        return ResponseUtil.SupportCORS(result);
    }

    @GET
    @Path("/listModules")
    @Produces("application/json")
    public Response getModules(@Context HttpServletRequest httpServletRequest) {
        Map<String, String[]> paramMap = httpServletRequest.getParameterMap();

        JSONObject resultJson = new JSONObject();
        Result result = new Result();
        try {
            List<Map<String, Object>> map = this.moduleService.getModules(paramMap);
            resultJson.put("MODULES", map);
            result = ResultEnum.SUCCESS.getResult();

        } catch (Exception e) {
            result.setErrText(e.getMessage());
            result.setErrCode(String.valueOf(e.hashCode()));
        }
        resultJson.put("RET_INFO", result);

        return ResponseUtil.SupportCORS(JSON.toJSONString(resultJson));
    }

    @POST
    @Path("/deleteModule")
    @Produces("application/json")
    public Response deleteModules(@FormParam("ADDRESS") String address) {
        Result result = new Result();
        try {
            int deleteNum = this.moduleService.deleteModule(address);
            if (deleteNum > 0) {
                result = ResultEnum.SUCCESS.getResult();
            } else {
                result = ResultEnum.DELETEERROR.getResult();
            }


        } catch (Exception e) {
            result.setErrText(e.getMessage());
            result.setErrCode(String.valueOf(e.hashCode()));
        }
        return ResponseUtil.SupportCORS(result);
    }

    @POST
    @Path("/testModule")
    @Produces("application/json")
    public Response testModule(@FormParam("ADDRESS") String host) {
        host = "http://" + host;
        return ResponseUtil.SupportCORS(moduleService.testModule(host));
    }

}
