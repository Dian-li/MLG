package com.bupt.dataAnalysis;

import java.util.ArrayList;
import java.util.List;

public class CpuData extends ResourceData {

    private List<String> cpuDate;

    private List<String> cpuTime;

    private List<String> cpuRate;

    public CpuData() {
    }



    public CpuData(String filePath, int index, int offset)throws Exception {
        super(filePath, index, offset);
        cpuDate = new ArrayList<String>();
        cpuTime = new ArrayList<String>();
        cpuRate = new ArrayList<String>();
        if(resourceData!=null){
            for(int i=0;i<resourceData.size();i++){
                cpuDate.add(resourceData.get(i)[0]);
                cpuTime.add(resourceData.get(i)[1]);
                cpuRate.add(resourceData.get(i)[2]);
            }
        }
        }

    public List<String> getCpuDate(){
        return cpuDate;
    }

    public List<String> getCpuTime(){
        return cpuTime;
    }

    public List<String> getCpuRate() {
        return cpuRate;
    }

}



