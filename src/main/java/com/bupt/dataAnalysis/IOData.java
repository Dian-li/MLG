package com.bupt.dataAnalysis;

import java.util.ArrayList;
import java.util.List;

public class IOData extends ResourceData{
    private List<String> ioDate;

    private List<String> ioTime;

    private List<String> ioRead;

    private List<String> ioWrite;

    public IOData(){}
    public IOData(String filePath, int index, int offset) throws Exception{
        super(filePath, index, offset);
        ioDate = new ArrayList<String>();
        ioTime = new ArrayList<String>();
        ioRead = new ArrayList<String>();
        ioWrite = new ArrayList<String>();
        if(resourceData!=null){
            for(int i=0;i<resourceData.size();i++){
                ioDate.add(resourceData.get(i)[0]);
                ioTime.add(resourceData.get(i)[1]);
                ioRead.add(resourceData.get(i)[2]);
                ioWrite.add(resourceData.get(i)[3]);
            }
        }

    }

    public List<String> getIoDate() {
        return ioDate;
    }

    public List<String> getIoTime() {
        return ioTime;
    }

    public List<String> getIoRead() {
        return ioRead;
    }

    public List<String> getIoWrite() {
        return ioWrite;
    }
}
