package com.bupt.service;

import com.sun.jna.Library;
import com.sun.jna.Native;
import org.springframework.stereotype.Service;

@Service
public class SocketScriptsHandle {
    public interface CLibrary extends Library {
        SocketScriptsHandle.CLibrary INSTANCE = (SocketScriptsHandle.CLibrary)
                Native.loadLibrary(("socketHandle"),
                        SocketScriptsHandle.CLibrary.class);

        void socketHandle(String dirname,String filename,String outputDir);
    }

    /**
     * handle http scripts
     * @param dirname   the dir of original script;ex:/home/xx/xx/csdn.saz
     * @param filename   the name of original script without dir;ex:csdn.saz
     * @param outputDir   the output dir,the address of test script;ex:/home/xx/xx/csdn
     */
    public void socketHandle(String dirname,String filename,String outputDir){

        SocketScriptsHandle.CLibrary.INSTANCE.socketHandle(dirname,filename,outputDir);
    }
}
