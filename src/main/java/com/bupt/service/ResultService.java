package com.bupt.service;

import com.bupt.pojo.Result;
import org.springframework.stereotype.Service;

/**
 * Created by Dian on 2017/8/7.
 */
@Service
public class ResultService {
    /**
     * 结果为成功
     * @return
     */
    public static Result Success(){
        Result result = new Result();
        result.setIndex("0");
        result.setErrCode("0");
        result.setErrText("success_utf8");
        return result;
    }

    public static Result Error(String code,String errText){
        Result result = new Result();
        result.setIndex("-1");
        result.setErrCode(code);
        result.setErrText(errText);
        return result;
    }
}
