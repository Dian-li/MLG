package com.bupt.Enum;

/**
 * Created by Dian on 2017/8/7.
 */
public enum ScriptType {
    /**
     * 脚本类型，原始脚本、测试脚本
     */
    ORAGINAL("original"),TEST("test"),Socket("socket"),HTTP("http"),NOTFOUND("notfound");


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

}
