package com.bupt.service;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import org.springframework.stereotype.Service;

@Service
public class HttpScriptsHandle {
    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary)
                Native.loadLibrary(("httpScript"),
                        CLibrary.class);

        void httpHandle(String dirname,String filename,String outputDir);
    }

    /**
     * handle http scripts
     * @param dirname   the dir of original script;ex:/home/xx/xx/csdn.saz
     * @param filename   the name of original script without dir;ex:csdn.saz
     * @param outputDir   the output dir,the address of test script;ex:/home/xx/xx/csdn
     */
    public void httpHandle(String dirname,String filename,String outputDir){

        CLibrary.INSTANCE.httpHandle(dirname,filename,outputDir);
    }

}
