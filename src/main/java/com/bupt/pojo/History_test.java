package com.bupt.pojo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class History_test {
    private Integer id;

    private String name;

    private String date;

    private Integer scriptid;

    private Integer paramfileid;

    private Integer tps;

    private Integer maxcourrent;

    private String resourcepath;

    private Integer userid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date == null ? null : date.trim();
    }

    public Integer getScriptid() {
        return scriptid;
    }

    public void setScriptid(Integer scriptid) {
        this.scriptid = scriptid;
    }

    public Integer getParamfileid() {
        return paramfileid;
    }

    public void setParamfileid(Integer paramfileid) {
        this.paramfileid = paramfileid;
    }

    public Integer getTps() {
        return tps;
    }

    public void setTps(Integer tps) {
        this.tps = tps;
    }

    public Integer getMaxcourrent() {
        return maxcourrent;
    }

    public void setMaxcourrent(Integer maxcourrent) {
        this.maxcourrent = maxcourrent;
    }

    public String getResourcepath() {
        return resourcepath;
    }

    public void setResourcepath(String resourcepath) {
        this.resourcepath = resourcepath == null ? null : resourcepath.trim();
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}