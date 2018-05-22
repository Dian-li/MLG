package com.bupt.dataAnalysis;

import java.util.ArrayList;
import java.util.List;

public class NetData extends ResourceData{
    private List<String> netDate;

    private List<String> netTime;

    private List<String> netSend;

    private List<String> netRecv;

    public NetData(){}

    public NetData(String filePath, int index, int offset)throws Exception{
        super(filePath, index, offset);
        netDate = new ArrayList<String>();
        netTime = new ArrayList<String>();
        netSend = new ArrayList<String>();
        netRecv = new ArrayList<String>();
        if(resourceData!=null){
            for(int i=0;i<resourceData.size();i++){
                netDate.add(resourceData.get(i)[0]);
                netTime.add(resourceData.get(i)[1]);
                float recv = 0;
                float send = 0;
                for(int j=2;j<resourceData.get(i).length;j++){
                    String tmp = resourceData.get(i)[j];
                    if(j%2==0){//偶数
                        recv += Float.valueOf(tmp);
                    }else{//奇数
                        send += Float.valueOf(tmp);
                    }
                }
                netSend.add(String.valueOf(send));
                netRecv.add(String.valueOf(recv));
            }
        }

    }

    public List<String> getNetDate() {
        return netDate;
    }

    public List<String> getNetTime() {
        return netTime;
    }

    public List<String> getNetSend() {
        return netSend;
    }

    public List<String> getNetRecv() {
        return netRecv;
    }
}
