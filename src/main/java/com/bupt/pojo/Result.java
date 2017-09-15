package com.bupt.pojo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Result {
    private String index;

    private String errCode;

    private String errText;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index == null ? null : index.trim();
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode == null ? null : errCode.trim();
    }

    public String getErrText() {
        return errText;
    }

    public void setErrText(String errText) {
        this.errText = errText == null ? null : errText.trim();
    }
}