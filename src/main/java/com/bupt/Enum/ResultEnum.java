package com.bupt.Enum;

import com.bupt.pojo.Result;

public enum ResultEnum {
    SUCCESS("0","0","success_utf8"),
    LOGINERROR("-1","003","用户名或密码错误"),
    DELETEERROR("-1","004","删除错误");



    private Result result;

    private ResultEnum(Result result){
        result = new Result();
        this.result = result;
    }
    private ResultEnum(String index,String errCode,String errText){
        result = new Result();
        result.setIndex(index);
        result.setErrCode(errCode);
        result.setErrText(errText);
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
