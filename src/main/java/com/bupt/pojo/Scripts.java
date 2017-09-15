package com.bupt.pojo;

public class Scripts {
    private Integer id;

    private String scriptname;

    private String scriptdate;

    private String scripttype;

    private String filepath;

    private String remark;

    private Integer userid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScriptname() {
        return scriptname;
    }

    public void setScriptname(String scriptname) {
        this.scriptname = scriptname == null ? null : scriptname.trim();
    }

    public String getScriptdate() {
        return scriptdate;
    }

    public void setScriptdate(String scriptdate) {
        this.scriptdate = scriptdate == null ? null : scriptdate.trim();
    }

    public String getScripttype() {
        return scripttype;
    }

    public void setScripttype(String scripttype) {
        this.scripttype = scripttype == null ? null : scripttype.trim();
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath == null ? null : filepath.trim();
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
}