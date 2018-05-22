package com.bupt.dataAnalysis;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class ResourceData {

    protected List<String[]> resourceData;

    public int getLength() {
        return resourceData.size();
    }

    public void setLength(int length) {
        this.length = length;
    }

    protected int length;

    public ResourceData(){};

    public ResourceData(String filePath,int index,int offset)throws Exception{
        resourceData = new ArrayList<String[]>();
        List<String> rowDatas = this.readAppointedLineNumber(filePath, index, offset);
        if(rowDatas!=null){
            for(String rowData:rowDatas){
                resourceData.add(dataSegmentation(rowData));
            }
        }else {
            resourceData = null;
        }

    }

    protected String[] dataSegmentation(String data){
        return data.split(",");
    };//data shading

    /**
     * 获取文件总行数
     * @param file
     * @return
     * @throws IOException
     */
    public  int getTotalLines(File file) throws IOException {
        FileReader in = new FileReader(file);
        LineNumberReader reader = new LineNumberReader(in);
        String s = reader.readLine();
        int lines = 0;
        while (s != null) {
            lines++;
            s = reader.readLine();
        }
        reader.close();
        in.close();
        return lines;
    }

    // 读取文件指定行。
    public  List<String> readAppointedLineNumber(String filepath, int lineNumber,int offset)
            throws IOException {
        File sourceFile = new File(filepath);
        List<String> result = new ArrayList<String>();
        FileReader in = new FileReader(sourceFile);
        LineNumberReader reader = new LineNumberReader(in);
        String s = "";
        int totalLine = getTotalLines(sourceFile);
        if(offset==-1){
            offset = totalLine-lineNumber+1;
        }
        if (lineNumber <= 0 || lineNumber > totalLine) {
            System.out.println("error is here");
            return null;
        }
        int lines = 0;
        while (s != null && offset!=0) {
            if((lines - lineNumber) < 0){
                lines++;
                s = reader.readLine();
                continue;
            }
            //找到该行
            //System.out.println(s);
            result.add(s);
            offset--;
            lines++;
            s = reader.readLine();
        }
        reader.close();
        in.close();
        return result;
    }
}
