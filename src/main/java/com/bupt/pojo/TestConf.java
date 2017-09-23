package com.bupt.pojo;

public class TestConf {

    private String confName;//名称,与测试用力名称相同

    private String mode;//并发模式

    private String timeDelayThreshold;//时延阈值

    private String successThreshold;//成功率阈值

    private String pressureMode;//压力模式

    private String initPressure;//初始压力

    private String timeInterval;//调节间隔

    private String step;//变化步长

    private String testTime;//测试时长

    private String virtualUser;//并发用户数

    private String targetPressure;//目标压力

    public String getConfName() {
        return confName;
    }

    public void setConfName(String confName) {
        this.confName = confName;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTimeDelayThreshold() {
        return timeDelayThreshold;
    }

    public void setTimeDelayThreshold(String timeDelayThreshold) {
        this.timeDelayThreshold = timeDelayThreshold;
    }

    public String getSuccessThreshold() {
        return successThreshold;
    }

    public void setSuccessThreshold(String successThreshold) {
        this.successThreshold = successThreshold;
    }

    public String getPressureMode() {
        return pressureMode;
    }

    public void setPressureMode(String pressureMode) {
        this.pressureMode = pressureMode;
    }

    public String getInitPressure() {
        return initPressure;
    }

    public void setInitPressure(String initPressure) {
        this.initPressure = initPressure;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getTargetPressure() {
        return targetPressure;
    }

    public void setTargetPressure(String targetPressure) {
        this.targetPressure = targetPressure;
    }



    public String getVirtualUser() {
        return virtualUser;
    }

    public void setVirtualUser(String virtualUser) {
        this.virtualUser = virtualUser;
    }
}
