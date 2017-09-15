package com.bupt.pojo;

public class Task {
    private Integer id;

    private Integer testcaseid;

    private String status;

    private String configurepath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTestcaseid() {
        return testcaseid;
    }

    public void setTestcaseid(Integer testcaseid) {
        this.testcaseid = testcaseid;
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
}