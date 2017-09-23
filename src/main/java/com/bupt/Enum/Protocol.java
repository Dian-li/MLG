package com.bupt.Enum;

public enum Protocol {
    SOCKET("socket"),HTTP("http"),FTP("ftp"),DB("db"),MIXTURE("mixture"),NOTFOUND("notfound");

    private String protocol;

    private Protocol(String protocol){
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public static Protocol judjeProtocol(String protocol){
        if(protocol.equals(SOCKET.getProtocol())){
            return SOCKET;
        }else if(protocol.equals(HTTP.getProtocol())){
            return HTTP;
        }else if(protocol.equals(FTP.getProtocol())){
            return FTP;
        }else if(protocol.equals(DB.getProtocol())){
            return DB;
        }else if(protocol.equals(MIXTURE.getProtocol())){
            return MIXTURE;
        }else {
            return NOTFOUND;
        }
    }

    public static Protocol getProtocolType(String filename){
        if(filename.toLowerCase().indexOf("saz")!=-1) {
            return Protocol.HTTP;
        }else if(filename.toLowerCase().indexOf("pcap")!=-1){
            return Protocol.SOCKET;
        }else{
            return Protocol.NOTFOUND;
        }
    }
}
