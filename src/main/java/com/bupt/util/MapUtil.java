package com.bupt.util;

import java.util.*;

/**
 * Created by Dian on 2017/8/15.
 */
public class MapUtil {

    public static Map<String,Object> ConvertMap(Map<String,String[]> map, Set<String> set){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        for (String str : set) {
            if(map.containsKey(str) && map.get(str)[0]!=null && map.get(str)[0].length()>0){
                resultMap.put(str,map.get(str)[0]);
            }else{
                resultMap.put(str,null);
            }
        }
        return resultMap;
    }

    /**
     * 设置默认值
     * @param map
     * @param key
     * @param value
     */
    public static void setDefault(Map<String,Object> map,String key,Object value){
        if(map.get(key)!=null){
            return;
        }else{
            map.replace(key,value);
        }
    }
    public static void changeToInt(Map<String,Object> map,String key){
        if(map.get(key)==null){
            return;
        }else{
            map.replace(key,(Integer.parseInt(String.valueOf(map.get(key)))));
        }
    }

    /**
     * 返回列名
     * @param datas
     * @return
     */
    public static List<String> getColumns(List<Map<String,Object>> datas){
        Map<String,Object> data1 = datas.get(0);
        List<String> columns = new ArrayList<String>();
        for(String str:data1.keySet()){
            columns.add(str);
        }
        return columns;
    }
}
