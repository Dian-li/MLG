package com.bupt.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bupt.Enum.Address;
import com.bupt.Enum.Protocol;
import com.bupt.Enum.SType;
import com.bupt.Enum.ScriptType;
import com.bupt.pojo.Result;
import com.bupt.pojo.Scripts;
import com.bupt.service.ResultService;
import com.bupt.service.HttpScriptsHandle;
import com.bupt.service.ScriptsService;
import com.bupt.util.DateUtil;
import com.bupt.util.FileUtil;
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
    @Path("/uploadNotModuleScripts")
    @Produces("application/json")
    @Consumes({MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON })
    //@Consumes("application/octet-stream")
    public Response upload(@FormDataParam("file") InputStream fileInputStream,
                           @FormDataParam("file") FormDataContentDisposition contentDispositionHeader,@FormDataParam("SCRIPTPROTOCOL")InputStream scriptProtocol,@FormDataParam("EXETIME")InputStream exeTime,@FormDataParam("REMARK")InputStream remark)throws IOException{
        //System.out.println(scriptProtocol.getName()+scriptProtocol.getFileName());


        String fileName = contentDispositionHeader.getFileName();
        String t=contentDispositionHeader.getName();
        //httpServletRequest.g
        System.out.println(fileName+" "+t);

        File file = new File(Address.getUserNotModulesScripts("dian")+"/" + fileName);
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
        scripts.setScripttype(ScriptType.TEST.getType());
        scripts.setFilepath(file.getAbsolutePath());
        scripts.setProtocol(Protocol.judjeProtocol(FileUtil.inputStreamToString(scriptProtocol)).getProtocol());
        scripts.setExetime(Integer.parseInt(FileUtil.inputStreamToString(exeTime)));
        scripts.setStype(SType.NOTMODULES.getsType());
        scripts.setRemark(FileUtil.inputStreamToString(remark));
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

    /**
     * add module script files
     * @param fileInputStream file inputstream
     * @param contentDispositionHeader file disposition
     * @param scriptProtocol scriptProtocol(HTTP/FTP/Socket)
     * @param scriptType script type (test/original)
     * @param remark remark
     * @return
     * @throws IOException
     */
    @POST
    @Path("/uploadModuleScripts")
    @Produces("application/json")
    @Consumes({MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON })
    //@Consumes("application/octet-stream")
    public Response uploadModules(@FormDataParam("file") InputStream fileInputStream,
                           @FormDataParam("file") FormDataContentDisposition contentDispositionHeader,@FormDataParam("SCRIPTPROTOCOL")InputStream scriptProtocol,@FormDataParam("SCRIPTTYPE")InputStream scriptType,@FormDataParam("REMARK")InputStream remark)throws IOException{
        String fileName = contentDispositionHeader.getFileName();
        String t=contentDispositionHeader.getName();
        //httpServletRequest.g
        System.out.println(fileName+" "+t);
        String myScriptType = ScriptType.judjeScriptType(FileUtil.inputStreamToString(scriptType));
        String filepath = "";
        if(myScriptType.equals(ScriptType.ORAGINAL.getType())){
            filepath = Address.getUserOriginalAddress("dian")+fileName;
        }else{
            filepath = Address.getUserTestAddress("dian")+fileName;
        }
        File file = new File(filepath);
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
        scripts.setScripttype(myScriptType);
        scripts.setFilepath(file.getAbsolutePath());
        scripts.setProtocol(Protocol.judjeProtocol(FileUtil.inputStreamToString(scriptProtocol)).getProtocol());
        scripts.setStype(SType.MODULES.getsType());
        scripts.setRemark(FileUtil.inputStreamToString(remark));
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
    public Response downloadFile(@QueryParam("SCRIPTNAME")String scriptname) {
        String filepath = this.scriptsService.selectPathByName(scriptname);
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

    /**
     * query the noModules scripts
     * @param httpServletRequest
     * @return
     */
    @GET
    @Path("/listScripts")
    @Produces("application/json")
    public Response queryNotModuleScript(@Context HttpServletRequest httpServletRequest){
        Map<String,String[]> paramMap = httpServletRequest.getParameterMap();
        JSONObject resultJson = new JSONObject();
        Result result = new Result();
        try{
            List<Map<String,Object>> map = this.scriptsService.selectScript(paramMap);
            resultJson.put("SCRIPTS",map);
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
    public Response exchangeScript(@FormParam("SCRIPTNAME")String scriptname){
        //String scriptname = request.getParameter("SCRIPTNAME");
        //String path = request.getParameter("SCRIPTPATH");
        if(scriptname==null){
            return ResponseUtil.SupportCORS(ResultService.Error("1","no such file"));
        }
        String path = this.scriptsService.selectPathByName(scriptname);
        Result result = this.scriptsService.handleScript(scriptname,path,"dian");
        return ResponseUtil.SupportCORS(result);

    }

    @POST
    @Path("deleteScripts")
    @Produces("application/json")
    public Response deleteScript(@FormParam("SCRIPTNAME")String scriptname){
        if(scriptname==null){
            return ResponseUtil.SupportCORS(ResultService.Error("1","no such file"));
        }
        Result result = new Result();
        try{
            int code = this.scriptsService.deleteScript(scriptname);
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
    public Response editScript(@FormParam("SCRIPTNAME")String scriptname){
        String path = this.scriptsService.selectPathByName(scriptname);
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
    public Response editScript(@FormParam("SCRIPTNAME")String scriptname,@FormParam("SCRIPTCONTEXT")String context){
        String path = this.scriptsService.selectPathByName(scriptname);
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

    @POST
    @Path("getScriptPath")
    @Produces("application/json")
    public Response getScriptPath(@FormParam("SCRIPTNAME")String scriptname){
        if(scriptname==null){
            return ResponseUtil.SupportCORS(ResultService.Error("1","no such file"));
        }
        JSONObject object = new JSONObject();
        Result result = new Result();
        String path = "";
        try{
            path = this.scriptsService.selectPathByName(scriptname);
            object.put("PATH",path);
            result = this.resultService.Success();
        }catch (Exception e){
            result.setErrText(e.getMessage());
            result.setErrCode(String.valueOf(e.hashCode()));
        }
        object.put("RET_INFO",result);
        return ResponseUtil.SupportCORS(JSON.toJSONString(object));
    }

    @GET
    @Path("getScriptTime")
    @Produces("application/json")
    public Response getScriptTime(@QueryParam("SCRIPTNAME")String scriptname){
        if(scriptname==null){
            return ResponseUtil.SupportCORS(ResultService.Error("1","no such file"));
        }
        JSONObject object = new JSONObject();
        Result result = new Result();
        int time = 0;
        try{
            time = this.scriptsService.selectScriptTimeByName(scriptname);
            object.put("EXETIME",time);
            result = this.resultService.Success();
        }catch (Exception e){
            result.setErrText(e.getMessage());
            result.setErrCode(String.valueOf(e.hashCode()));
        }
        object.put("RET_INFO",result);
        return ResponseUtil.SupportCORS(JSON.toJSONString(object));
    }

}
