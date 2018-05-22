package com.bupt.service;

import com.bupt.Enum.Address;
import com.bupt.Enum.Protocol;
import com.bupt.Enum.SType;
import com.bupt.Enum.ScriptType;
import com.bupt.dao.ScriptsMapper;

import com.bupt.pojo.Result;
import com.bupt.pojo.Scripts;
import com.bupt.util.DateUtil;
import com.bupt.util.FileUtil;
import com.bupt.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Dian on 2017/8/7.
 */
@Service
public class ScriptsService {
    @Autowired
    private ScriptsMapper scriptsMapper;
    @Autowired
    private HttpScriptsHandle httpScriptsHandle;
    @Autowired
    private SocketScriptsHandle socketScriptsHandle;
    @Autowired
    private ResultService resultService;
    public int insertScript(Scripts scripts){
        return this.scriptsMapper.insertSelective(scripts);
    }

    /**
     * 根据条件查询脚本、条件可为空
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> selectScript(Map<String,String[]> paramMap){
        Set set = new HashSet();
        set.add("SCRIPTDATE");
        set.add("SCRIPTNAME");
        set.add("SCRIPTTYPE");
        set.add("PROTOCOL");
        set.add("STYPE");
        set.add("S_INDEX");
        set.add("MAX_NUM");
        Map map = MapUtil.ConvertMap(paramMap,set);
        MapUtil.setDefault(map,"S_INDEX",0);
        MapUtil.setDefault(map,"MAX_NUM",10);
       // MapUtil.setDefault(map,"SCRIPTTYPE", ScriptType.ORAGINAL);
        MapUtil.changeToInt(map,"S_INDEX");
        MapUtil.changeToInt(map,"MAX_NUM");
        return this.scriptsMapper.selectScripts(map);
    }

    /**
     * delete script
     * @param scriptName
     * @return
     */
    public int deleteScript(String scriptName){
        String scriptPath = this.selectPathByName(scriptName);
        ScriptType scriptType = ScriptType.getScriptType(scriptName);
        FileUtil.deleteFile(scriptPath);
        if(ScriptType.ORAGINAL.getType().equals(scriptType)){
            String tempFile = FileUtil.getPath(scriptPath);
            FileUtil.deleteDirectory(tempFile);//delete the temp dir
        }
        Scripts scripts = new Scripts();
        scripts.setScriptname(scriptName);
        scripts.setFilepath(scriptPath);
        scripts.setScripttype(scriptType.getType());

        return this.scriptsMapper.deleteByScript(scripts);
    }

    /**
     * handle the scripts
     * @param scriptName
     * @param scriptPath
     * @param username
     * @return
     */
    public Result handleScript(String scriptName,String scriptPath,String username){
        Protocol type = Protocol.getProtocolType(scriptName);
        String outfilename="";
        String output="";
        Scripts scripts = new Scripts();
        if(type.equals(Protocol.HTTP)){
            output = Address.getUserTestAddress(username);
            this.httpScriptsHandle.httpHandle(scriptPath,scriptName,output);
            outfilename= scriptName.replace("saz","xml");
            scripts.setProtocol(Protocol.HTTP.getProtocol());
        }else if(type.equals(Protocol.SOCKET)){
            output = Address.getUserTestAddress(username);
            this.socketScriptsHandle.socketHandle(scriptPath,scriptName,output);
            outfilename= scriptName.replace("pcap","xml");
            scripts.setProtocol(Protocol.SOCKET.getProtocol());
        }
        scripts.setStype(SType.MODULES.getsType());
        scripts.setFilepath(output+outfilename);
        scripts.setScriptname(outfilename);
        scripts.setScripttype(ScriptType.TEST.getType());
        scripts.setScriptdate(DateUtil.getNowTime());
        scripts.setUserid(1);
        Result result = new Result();
        try{
            int code = this.insertScript(scripts);
            if(code>0){
                result = this.resultService.Success();
            }
        }catch(Exception e){
            result = this.resultService.Error(String.valueOf(e.hashCode()),e.getMessage());
        }
        return result;
    }

    /**
     * 读取文件内容
     * @param scriptPath
     * @return
     */
    public String getScriptContext(String scriptPath) throws Exception {
        File file = new File(scriptPath);
        if (file.isFile() && file.exists()) { //判断文件是否存在
            InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file));//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            StringBuilder stringBuilder = new StringBuilder();
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                stringBuilder.append(lineTxt+"\n");
            }
            read.close();
            return stringBuilder.toString();
        } else {
            return "file not exists";
        }
    }

    public void saveScript(String scriptPath,String context) throws IOException{
        File file = new File(scriptPath);
        if(file.isFile() && file.exists()){
            FileUtil.deleteFile(scriptPath);
        }
        FileUtil.createFile(scriptPath);
        FileWriter fw = new FileWriter(scriptPath, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(context);
        bw.close();
        fw.close();
    }


    public String selectPathByName(String scriptname){
        return this.scriptsMapper.selectPathByName(scriptname);
    }

    public int selectScriptTimeByName(String scriptname){ return this.scriptsMapper.selectScriptTimeByName(scriptname);}
}
