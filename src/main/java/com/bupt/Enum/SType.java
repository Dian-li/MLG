package com.bupt.Enum;

/**
 * 脚本类型
 */
public enum SType {
    MODULES("modules"),NOTMODULES("notmodules"),OTHERS("others");

    private String sType;
    private SType(String sType){
        this.sType = sType;
    }

    public String getsType() {
        return sType;
    }

    public void setsType(String sType) {
        this.sType = sType;
    }

    public static SType judgeSType(String stype){
        if(stype.equals(MODULES.getsType())){
            return MODULES;
        }else if(stype.equals(NOTMODULES.getsType())){
            return NOTMODULES;
        }else{
            return OTHERS;
        }
    }
}
