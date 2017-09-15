package com.bupt.service;

import com.bupt.Enum.ScriptType;
import com.bupt.dao.ParamsfileMapper;
import com.bupt.pojo.Paramsfile;
import com.bupt.pojo.Scripts;
import com.bupt.util.FileUtil;
import com.bupt.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ParamFileService {
    @Autowired
    private ParamsfileMapper paramsfileMapper;

    /**
     * insert into paramfiles
     * @param paramsfile
     * @return
     */
    public int insertParamFile(Paramsfile paramsfile){
        return this.paramsfileMapper.insertSelective(paramsfile);
    }


    /**
     * 根据条件查询参数文件、条件可为空
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> selectScript(Map<String,String[]> paramMap){
        Set set = new HashSet();
        set.add("DATE");
        set.add("FILENAME");
        set.add("FILEPATH");
        set.add("S_INDEX");
        set.add("MAX_NUM");
        Map map = MapUtil.ConvertMap(paramMap,set);
        MapUtil.setDefault(map,"S_INDEX",0);
        MapUtil.setDefault(map,"MAX_NUM",10);
        // MapUtil.setDefault(map,"SCRIPTTYPE", ScriptType.ORAGINAL);
        MapUtil.changeToInt(map,"S_INDEX");
        MapUtil.changeToInt(map,"MAX_NUM");
        return this.paramsfileMapper.selectParamFile(map);
    }



    /**
     * 读取文件内容
     * @param scriptPath
     * @return
     */
    public String getParamContext(String scriptPath) throws Exception {
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

    public void saveParamContext(String scriptPath,String context) throws IOException {
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

    /**
     * delete param file
     * @param paramFileName
     * @param paramFilePath
     * @return
     */
    public int deleteScript(String paramFileName,String paramFilePath){
        FileUtil.deleteFile(paramFilePath);
        Paramsfile paramsfile = new Paramsfile();
        paramsfile.setFilename(paramFileName);
        paramsfile.setFilepath(paramFilePath);

        return this.paramsfileMapper.deleteByParamFile(paramsfile);
    }
}
