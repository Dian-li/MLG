package com.bupt.Enum;

/**
 * Created by Dian on 2017/8/7.
 */
public enum ScriptType {
    /**
     * 脚本类型，原始脚本、测试脚本
     */
    ORAGINAL("original"),TEST("test"),NOTFOUND("notfound");


    private String type;
    private ScriptType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static String judjeScriptType(String scriptType){
        if(scriptType.equals(ORAGINAL.getType())){
            return ORAGINAL.getType();
        }else if(scriptType.equals(TEST.getType())){
            return TEST.getType();
        }else{
            return NOTFOUND.getType();
        }
    }

    public static ScriptType getScriptType(String filename){
        if(filename.toLowerCase().indexOf("saz")!=-1 || filename.toLowerCase().indexOf("pcap")!=-1) {
            return ScriptType.ORAGINAL;
        }else{
            return ScriptType.TEST;
        }
    }

}
