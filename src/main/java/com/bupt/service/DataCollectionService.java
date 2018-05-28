package com.bupt.service;


import com.bupt.Enum.Address;
import com.bupt.client.Httpclient;
import com.bupt.util.FileUtil;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DataCollectionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataCollectionService.class);

    @Autowired
    private Httpclient httpclient;

    private static List<Process> processes = new ArrayList<>();

    /**
     * start dataCollection
     */
    public void startCollect(String username,
                             String testcaseName,
                             String protocol,
                             String remark,
                             String scriptName,
                             String paramfileName,
                             Integer concurrenceMode,
                             Integer timedelay,
                             Integer threshold,
                             Integer pressureMode,
                             Integer tps,
                             Integer step,
                             Integer duration,
                             Integer targetTps,
                             List<String> ipList,
                             List<Float> ratioList) throws Exception {
        String host = "localhost";
        String user = "dian";
        String password = "1234";
        String dataPath = Address.getUserData(username, testcaseName);
        if (ipList == null || ratioList == null || ipList.size() != ratioList.size()) {
            LOGGER.error("params error!");
            return;
        }
        String path = this.getClass().getResource("/python/dataCollection.py").getPath();
        for (int i = 0; i < ipList.size(); i++) {
            String url = ipList.get(i) + "/ptf/new_test";
            Map<String, Object> params = new HashMap<>();
            params.put("testcaseName", testcaseName);
            params.put("protocol", protocol);
            params.put("concurrenceMode", concurrenceMode);
            params.put("timedelay", timedelay);
            params.put("threshold", threshold);
            params.put("pressureMode", pressureMode);
            params.put("tps", tps * ratioList.get(i));
            params.put("step", step);
            params.put("duration", duration);
            params.put("targetTps", targetTps);
            HttpResponse response = httpclient.postRequest(url, scriptName, params);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                LOGGER.info(ipList.get(i) + " start test!");
                Process proc = null;
                startCollectData(proc, path, ipList.get(i), user, password, dataPath, testcaseName);
                if (proc != null) {
                    processes.add(proc);
                }
            } else {
                LOGGER.error("failed:{}", response.getStatusLine().getStatusCode() + "," + FileUtil.inputStreamToString(response.getEntity().getContent()));
            }
        }
        //  proc.waitFor();
    }

    /**
     * stop dataCollection
     *
     * @throws InterruptedException
     */
    public void stopCollect(Process proc) {
        if (proc != null && proc.isAlive()) {
            try {
                killProcessTree(proc);
                proc.destroy();
            } catch (Exception e) {
                LOGGER.error("kill dataCollection process error:{}", e.getMessage());
            }

            LOGGER.info("shut down successful!");
        } else {
            LOGGER.info("shut down failed");
        }

    }

    public void stopCollect() throws InterruptedException {
        processes.forEach(e -> stopCollect(e));
    }

    private void killProcessTree(Process process) {
        try {
            long PID = -1;
            Field field = null;
            Class<?> clazz = Class.forName("java.lang.UNIXProcess");
            field = clazz.getDeclaredField("pid");
            field.setAccessible(true);
            PID = (Integer) field.get(process);
            System.out.println(PID);
            String cmd = getKillProcessTreeCmd(PID);
            Runtime rt = Runtime.getRuntime();
            Process killPrcess = rt.exec(cmd);
            killPrcess.waitFor();
            killPrcess.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getKillProcessTreeCmd(Long Pid) {
        String result = "";
        if (Pid != null)
            result = "kill -15 " + Pid;//和python脚本中捕捉的信号量一致SIGTERM
        return result;
    }

    private void startCollectData(Process proc, String path, String host, String user, String password, String dataPath, String testcaseName) {
        if (proc != null && proc.isAlive()) {
            proc.destroy();
        }
        try {
            proc = Runtime.getRuntime().exec("python  " + path +
                    " " + host +
                    " " + user +
                    " " + password +
                    " " + dataPath +
                    " " + testcaseName);
        } catch (IOException e) {
            LOGGER.error(" open start collection error:{}", e);

        }

    }
}
