package com.virtualStudyRoom.utils;

public class OSType {

    private static final String os = System.getProperty("os.name").toLowerCase();
    public  boolean isWindows(){
        return os.contains("win") || os.contains("Win");
    }
    public  boolean isMac(){
        return os.contains("mac");
    }
    public  boolean isLinux(){
        return os.contains("nix") || os.contains("nux");
    }
    public String getOsName(){
        return os;
    }
}