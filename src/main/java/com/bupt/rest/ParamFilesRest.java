package com.bupt.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bupt.Enum.Address;
import com.bupt.Enum.ScriptType;
import com.bupt.pojo.Paramsfile;
import com.bupt.pojo.Result;
import com.bupt.pojo.Scripts;
import com.bupt.service.ParamFileService;
import com.bupt.service.ResultService;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;
import java.util.Map;

@Component
@Path("/paramfileMana")
public class ParamFilesRest {
    @Autowired
    private ParamFileService paramFileService;
    @Autowired
    private ResultService resultService;

    @POST
    @Path("/uploadParamFiles")
    @Produces("application/json")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    //@Consumes("application/octet-stream")
    public Response upload(@FormDataParam("file") InputStream fileInputStream,
                           @FormDataParam("file") FormDataContentDisposition contentDispositionHeader)throws IOException {
        String fileName = contentDispositionHeader.getFileName();
        String t=contentDispositionHeader.getName();
        //httpServletRequest.g
        System.out.println(fileName+" "+t);

        File file = new File(Address.getUserParamFile("dian")+fileName);
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
        Paramsfile paramsfile = new Paramsfile();
        paramsfile.setFilename(fileName);
        paramsfile.setDate(DateUtil.dateFormat(file.lastModified()));
        System.out.println(file.getAbsolutePath());
        paramsfile.setFilepath(file.getAbsolutePath());
        paramsfile.setUserid(1);

        Result result = new Result();
        try{
            int code = this.paramFileService.insertParamFile(paramsfile);
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
    @Path("/downloadParamFile")
    //@Consumes("application/json")
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON })
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@QueryParam("FILEPATH")String filepath) {
        File file = new File(filepath);
        if (file.isFile() && file.exists()) {
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
    @Path("/listParamFiles")
    @Produces("application/json")
    public Response queryScript(@Context HttpServletRequest httpServletRequest){
        Map<String,String[]> paramMap = httpServletRequest.getParameterMap();
        JSONObject resultJson = new JSONObject();
        Result result = new Result();
        try{
            List<Map<String,Object>> map = this.paramFileService.selectScript(paramMap);
            List<String> columns;
            if(map.size()>0){
                columns = MapUtil.getColumns(map);
            }else{
                columns=null;
            }
            resultJson.put("PARAMFILES",map);
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
    @Path("deleteParamFile")
    @Produces("application/json")
    public Response deleteScript(@FormParam("FILENAME")String filename,@FormParam("FILEPATH")String path){
        if(filename==null || path==null){
            return ResponseUtil.SupportCORS(ResultService.Error("1","no such file"));
        }
        Result result = new Result();
        try{
            int code = this.paramFileService.deleteScript(filename, path);
            if(code>0){
                result = this.resultService.Success();
            }
        }catch(Exception e){
            result = this.resultService.Error(String.valueOf(e.hashCode()),e.getMessage());
        }
        return ResponseUtil.SupportCORS(result);

    }

    @POST
    @Path("editParamFile")
    @Produces("application/json")
    public Response editScript(@FormParam("FILENAME")String filename,@FormParam("FILEPATH")String path){
        if(filename==null || path==null){
            return ResponseUtil.SupportCORS(ResultService.Error("1","no such file"));
        }
        Result result = new Result();
        String context = "";
        try{
            context = this.paramFileService.getParamContext(path);
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
    @Path("saveParamContext")
    @Produces("application/json")
    public Response editScript(@FormParam("FILENAME")String scriptname,@FormParam("FILEPATH")String path,@FormParam("FILECONTEXT")String context){
        if(scriptname==null || path==null || context==null){
            return ResponseUtil.SupportCORS(ResultService.Error("1","no such file"));
        }
        Result result = new Result();
        try{
            this.paramFileService.saveParamContext(path,context);
            result = this.resultService.Success();
        }catch (IOException e){
            result.setErrText(e.getMessage());
            result.setErrCode(String.valueOf(e.hashCode()));
        }
        return ResponseUtil.SupportCORS(result);
    }


}
