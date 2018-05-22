package com.bupt.api;

import com.sun.jersey.api.core.PackagesResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Created by Dian on 2017/8/4.
 */
public class MyApplication extends PackagesResourceConfig {
    public MyApplication(){
        super("com.bupt.rest");
    }
}
