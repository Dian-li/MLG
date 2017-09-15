package com.bupt.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bupt.Enum.Address;
import com.bupt.Enum.ScriptType;
import com.bupt.pojo.Result;
import com.bupt.pojo.Scripts;
import com.bupt.service.ResultService;
import com.bupt.service.HttpScriptsHandle;
import com.bupt.service.ScriptsService;
import com.bupt.util.DateUtil;
import com.bupt.util.MapUtil;
import com.bupt.util.ResponseUtil;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Dian on 2017/8/7.
 */
@Component
@Path("/scriptsMana")
public class ScriptsRest {
    private static String serverPath = "scripts/";
    private static String filepath=".src/resources/csdn.saz";
    @Autowired
    private ScriptsService scriptsService;
    @Autowired
    private ResultService resultService;
    @Autowired
    private HttpScriptsHandle httpScriptsHandle;

    @POST
    @Path("/uploadTestScripts")
    @Produces("application/json")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    //@Consumes("application/octet-stream")
    public Response upload(@FormDataParam("file") InputStream fileInputStream,
                           @FormDataParam("file") FormDataContentDisposition contentDispositionHeader)throws IOException{
        String fileName = contentDispositionHeader.getFileName();
        String t=contentDispositionHeader.getName();
        //httpServletRequest.g
        System.out.println(fileName+" "+t);

        File file = new File(Address.ROOT.getAddress()+"dian"+Address.ORIGINAL.getAddress()+"/" + fileName);
        File parent = file.getParentFile();
        //判断目录是否存在，不在创建
        if(parent!=null&&!parent.exists()){
            parent.mkdirs();
        }
        file.createNewFile();
        byte[] fileContext = new byte[(int)file.length()];
        fileInputStream.read(fileContext);

        OutputStream outpuStream = new FileOutputStream(file);
        int read = 0;
        byte[] bytes = new byte[1024];
        while ((read = fileInputStream.read(bytes)) != -1) {
            outpuStream.write(bytes, 0, read);
        }

        outpuStream.flush();
        outpuStream.close();

        fileInputStream.close();
        Scripts scripts = new Scripts();
        scripts.setScriptname(fileName);
        scripts.setScriptdate(DateUtil.dateFormat(file.lastModified()));
        scripts.setScripttype(ScriptType.ORAGINAL.getType());
        scripts.setFilepath(file.getAbsolutePath());
        scripts.setUserid(1);
        Result result = new Result();
        try{
            int code = this.scriptsService.insertScript(scripts);
            if(code>0){
                result = this.resultService.Success();
            }
        }catch(Exception e){
            result = this.resultService.Error(String.valueOf(e.hashCode()),e.getMessage());
        }
        return ResponseUtil.SupportCORS(result);
        //return ResponseUtil.SupportCORS(scripts);
    }

    @GET
    @Path("/downloadscript")
    //@Consumes("application/json")
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON })
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@QueryParam("SCRIPTPATH")String filepath) {
        File file = new File(filepath);
        if (file.isFile() && file.exists() ){
            String mt = new MimetypesFileTypeMap().getContentType(file);
            String fileName = file.getName();

            return Response
                    .ok(file, mt)
                    .header("Content-disposition",
                            "attachment;filename=" + fileName)
                    .header("Cache-Control", "no-cache").build();

        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("下载失败，未找到该文件").build();
        }
    }
    @GET
    @Path("/listScripts")
    @Produces("application/json")
    public Response queryScript(@Context HttpServletRequest httpServletRequest){
        Map<String,String[]> paramMap = httpServletRequest.getParameterMap();
        JSONObject resultJson = new JSONObject();
        Result result = new Result();
        try{
            List<Map<String,Object>> map = this.scriptsService.selectScript(paramMap);
            List<String> columns;
            if(map.size()>0){
                columns = MapUtil.getColumns(map);
            }else{
                columns=null;
            }
            resultJson.put("SCRIPTS",map);
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
    @Path("exchangeScripts")
    @Produces("application/json")
    public Response exchangeScript(@FormParam("SCRIPTNAME")String scriptname,@FormParam("SCRIPTPATH")String path){
        //String scriptname = request.getParameter("SCRIPTNAME");
        //String path = request.getParameter("SCRIPTPATH");
        if(scriptname==null || path==null){
            return ResponseUtil.SupportCORS(ResultService.Error("1","no such file"));
        }
        Result result = this.scriptsService.handleScript(scriptname,path,"dian");
        return ResponseUtil.SupportCORS(result);

    }

    @POST
    @Path("deleteScripts")
    @Produces("application/json")
    public Response deleteScript(@FormParam("SCRIPTNAME")String scriptname,@FormParam("SCRIPTTYPE")String type,@FormParam("SCRIPTPATH")String path){
        if(scriptname==null || path==null){
            return ResponseUtil.SupportCORS(ResultService.Error("1","no such file"));
        }
        Result result = new Result();
        try{
            int code = this.scriptsService.deleteScript(scriptname,type,path);
            if(code>0){
                result = this.resultService.Success();
            }
        }catch(Exception e){
            result = this.resultService.Error(String.valueOf(e.hashCode()),e.getMessage());
        }
        return ResponseUtil.SupportCORS(result);

    }

    @POST
    @Path("editScripts")
    @Produces("application/json")
    public Response editScript(@FormParam("SCRIPTNAME")String scriptname,@FormParam("SCRIPTPATH")String path){
        if(scriptname==null || path==null){
            return ResponseUtil.SupportCORS(ResultService.Error("1","no such file"));
        }
        Result result = new Result();
        String context = "";
        try{
            context = this.scriptsService.getScriptContext(path);
            result = this.resultService.Success();
        }catch(Exception e){
            result.setErrCode(String.valueOf(e.hashCode()));
            result.setErrText(e.getMessage());
        }
        JSONObject object = new JSONObject();
        object.put("FILENAME",path);
        object.put("CONTEXT",context);
        object.put("RET_INFO",result);
        return ResponseUtil.SupportCORS(JSON.toJSONString(object));

    }
    @POST
    @Path("saveScripts")
    @Produces("application/json")
    public Response editScript(@FormParam("SCRIPTNAME")String scriptname,@FormParam("SCRIPTPATH")String path,@FormParam("SCRIPTCONTEXT")String context){
        if(scriptname==null || path==null || context==null){
            return ResponseUtil.SupportCORS(ResultService.Error("1","no such file"));
        }
        Result result = new Result();
        try{
            this.scriptsService.saveScript(path,context);
            result = this.resultService.Success();
        }catch (IOException e){
            result.setErrText(e.getMessage());
            result.setErrCode(String.valueOf(e.hashCode()));
        }
        return ResponseUtil.SupportCORS(result);
    }
}
