package com.bupt.service;


import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.lang.reflect.Field;


@Service
public class DataCollectionService {

     private static Process proc = null;
    /**
     * start dataCollection
     */
    public void startCollect()throws Exception{
        if(proc!=null && proc.isAlive()){
            proc.destroy();
        }
        String path = this.getClass().getResource("/python/dataCollection.py").getPath();
        System.out.println(path);
        proc = Runtime.getRuntime().exec("python  "+path);
        //  proc.waitFor();
        System.out.println("start dataCollection");


    }

    /**
     * stop dataCollection
     * @throws InterruptedException
     */
    public void stopCollect()throws InterruptedException{
        if(proc!=null && proc.isAlive()){
            killProcessTree(proc);
            proc.destroy();
            System.out.println("shut down");
        }else{
            System.out.println("shut down failed");
        }


    }

    private static void killProcessTree(Process process)
    {
        try {
            long PID = -1;
            Field field = null;
            Class<?> clazz = Class.forName("java.lang.UNIXProcess");
            field = clazz.getDeclaredField("pid");
            field.setAccessible(true);
            PID = (Integer) field.get(process);
            System.out.println(PID);
            String cmd =getKillProcessTreeCmd(PID);
            Runtime rt =Runtime.getRuntime();
            Process killPrcess = rt.exec(cmd);
            killPrcess.waitFor();
            killPrcess.destroy();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static String getKillProcessTreeCmd(Long Pid)
    {
        String result = "";
        if(Pid !=null)
            result ="kill -9 "+Pid;
        return result;
    }


}
