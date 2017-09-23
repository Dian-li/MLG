package com.bupt.Enum;

public enum Address {

    ROOT("/home/dian/code/MLG/out/"),
    TEST("/test/"),
    ORIGINAL("/original/"),
    PARAMFILES("/paramfiles/"),
    SCRIPTS("/scripts/"),
    SCRIPTMODULES("/scriptmodules/"),
    Task("/tasks/");


    private String address;
    private Address(String address){this.address=address;}
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static String getUserTestAddress(String username){
        return getUserModulesScripts(username)+TEST.getAddress();
    }
    public static String getUserOriginalAddress(String username){
        return  getUserModulesScripts(username)+ORIGINAL.getAddress();
    }
    public static String getUserParamFile(String username){
        return ROOT.getAddress()+username+PARAMFILES.getAddress();
    }
    public static String getUserTestcaseAddress(String username,String testcasename){
        return ROOT.getAddress()+username+"/"+"testcases"+"/"+testcasename;
    }

    public static String getUserNotModulesScripts(String username){
        return ROOT.getAddress()+username+SCRIPTS.getAddress();
    }

    public static String getUserModulesScripts(String username){
        return ROOT.getAddress()+username+SCRIPTMODULES.getAddress();
    }

//    public static String getUserTasks(String username,String testcasename){
//        return ROOT.getAddress()+username+Task.getAddress()+testcasename;
//    }
    public static String getUserTestConfAddress(String username,String testcaseName){
        return getUserTestcaseAddress(username, testcaseName)+"/"+testcaseName+".properties";
    }
    public static  String getUserData(String username,String testcaseName){
        return getUserTestcaseAddress(username, testcaseName)+"/data";
    }
}
