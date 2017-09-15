package com.bupt.util;

import javax.ws.rs.core.Response;

/**
 * Created by Dian on 2017/8/7.
 */
public class ResponseUtil {
    public static Response  SupportCORS(Object resopnse){
        Response rp=Response.ok(resopnse).header("Content-type","application/json").header("Access-Control-Allow-Origin", "*").build();
        return rp;
    }

}
