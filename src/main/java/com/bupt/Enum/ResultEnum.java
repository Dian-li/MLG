package com.bupt.Enum;

import com.bupt.pojo.Result;

public enum ResultEnum {
    SUCCESS("0","0","success_utf8"),
    LOGINERROR("-1","003","用户名或密码错误"),
    DELETEERROR("-1","004","删除错误"),
    FILEUPLOADERROR("-1","005","文件上传错误"),
    FILENOTFOUND("-1","006","服务器文件未找到"),
    FILEEND("-2","007","文件读完"),
    PARAMSERROR("-1","008","参数错误");



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
