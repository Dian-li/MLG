package com.bupt.pojo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Testcase {
    private Integer id;

    private String name;

    private String date;

    private String remark;

    private Integer userid;

    private String scriptname;

    private String scriptpath;

    private String paramfilename;

    private String paramfilepath;

    private String address;

    private String status;//状态

    private String testenvpath;

    private String statdatapath;

    private String protocol;//协议

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getScriptname() {
        return scriptname;
    }

    public void setScriptname(String scriptname) {
        this.scriptname = scriptname == null ? null : scriptname.trim();
    }

    public String getScriptpath() {
        return scriptpath;
    }

    public void setScriptpath(String scriptpath) {
        this.scriptpath = scriptpath == null ? null : scriptpath.trim();
    }

    public String getParamfilename() {
        return paramfilename;
    }

    public void setParamfilename(String paramfilename) {
        this.paramfilename = paramfilename == null ? null : paramfilename.trim();
    }

    public String getParamfilepath() {
        return paramfilepath;
    }

    public void setParamfilepath(String paramfilepath) {
        this.paramfilepath = paramfilepath == null ? null : paramfilepath.trim();
    }



    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol == null ? null : protocol.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTestenvpath() {
        return testenvpath;
    }

    public void setTestenvpath(String testenvpath) {
        this.testenvpath = testenvpath;
    }

    public String getStatdatapath() {
        return statdatapath;
    }

    public void setStatdatapath(String statdatapath) {
        this.statdatapath = statdatapath;
    }
}