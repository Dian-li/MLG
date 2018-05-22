package com.bupt.dataAnalysis;

import java.util.ArrayList;
import java.util.List;

public class MemData extends ResourceData{
    private List<String> memDate;

    private List<String> memTime;

    private List<String> memRate;

    private List<String> vmemRate;

    public MemData(){}
    public MemData(String filePath, int index, int offset)throws Exception{
        super(filePath, index, offset);
        memRate = new ArrayList<String>();
        memTime = new ArrayList<String>();
        vmemRate = new ArrayList<String>();
        memDate = new ArrayList<String>();
        if(resourceData!=null){
            for(int i=0;i<resourceData.size();i++){
                memTime.add(resourceData.get(i)[1]);
                memRate.add(resourceData.get(i)[2]);
                vmemRate.add(resourceData.get(i)[3]);
                memDate.add(resourceData.get(i)[0]);
            }
        }
    }

    public List<String> getMemTime() {
        return memTime;
    }

    public List<String> getMemRate() {
        return memRate;
    }

    public List<String> getVmemRate() {
        return vmemRate;
    }

    public List<String> getMemDate(){
        return memDate;
    }
}
