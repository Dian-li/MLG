package com.bupt.pojo;

public class Task {
    private Integer id;

    private String testcasename;

    private String status;

    private String configurepath;

    private String begintime;

    private Integer userid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTestcasename() {
        return testcasename;
    }

    public void setTestcasename(String testcasename) {
        this.testcasename = testcasename == null ? null : testcasename.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getConfigurepath() {
        return configurepath;
    }

    public void setConfigurepath(String configurepath) {
        this.configurepath = configurepath == null ? null : configurepath.trim();
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime == null ? null : begintime.trim();
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}