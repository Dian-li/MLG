package com.bupt.Enum;

/**
 * task status
 */
public enum TaskStatus {

    READY("Ready"),RUNNING("Running"),FINISH("Finish");


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    private TaskStatus(String status){
        this.status = status;
    }

}
