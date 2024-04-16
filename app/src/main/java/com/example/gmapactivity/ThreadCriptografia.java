package com.example.gmapactivity;

import com.example.mylibrary.Region;
import com.google.gson.Gson;

public class ThreadCriptografia extends Thread{
    public ThreadCriptografia(){

    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String TransformaCriptografia(Region region){
        Gson gson = new Gson();
        String regionJson = gson.toJson(region);
        return regionJson;
    }
}
