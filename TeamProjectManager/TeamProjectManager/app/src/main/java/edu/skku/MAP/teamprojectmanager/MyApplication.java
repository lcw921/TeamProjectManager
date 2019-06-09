package edu.skku.MAP.teamprojectmanager;

import android.app.Application;

public class MyApplication extends Application {
    private String userID;
    private String userNAME;
    private String tmpID;

    public String getUserID(){
        return userID;
    }
    public void setUserID(String s){
        userID = s;
    }

    public String getUserNAME(){
        return userNAME;
    }
    public void setUserNAME(String s){
        userNAME = s;
    }

    public String getTmpID(){
        return tmpID;
    }
    public void setTmpID(String s){
        tmpID = s;
    }
}
