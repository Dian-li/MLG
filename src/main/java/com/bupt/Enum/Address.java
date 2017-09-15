package com.bupt.Enum;

public enum Address {

    ROOT("/home/dian/code/MLG/out/"),
    TEST("/scripts/test/"),
    ORIGINAL("/scripts/original/"),
    PARAMFILES("/paramfiles/");


    private String address;
    private Address(String address){this.address=address;}
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static String getUserTestAddress(String username){
        return ROOT.getAddress()+username+TEST.getAddress();
    }
    public static String getUserOriginalAddress(String username){
        return  ROOT.getAddress()+username+ORIGINAL.getAddress();
    }
    public static String getUserParamFile(String username){
        return ROOT.getAddress()+username+PARAMFILES.getAddress();
    }
    public static String getUserTestcaseAddress(String username,String testcasename){
        return ROOT.getAddress()+username+"/"+"testcases"+"/"+testcasename;
    }
}
