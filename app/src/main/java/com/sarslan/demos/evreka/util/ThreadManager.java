package com.sarslan.demos.evreka.util;

import java.util.HashMap;
import java.util.Map;

public enum ThreadManager {

    instance;

    private Map<String, MyThread> map;

    ThreadManager(){
        map = new HashMap<String, MyThread>();
    }

    public MyThread create(String name, ThreadUpdateObject o, long updatePeirodMillis){
        MyThread t = new MyThread(o,updatePeirodMillis);
        t.setName(name);
        map.put(name, t);
        return t;
    }
    public void abortAll(){
        for(Map.Entry<String, MyThread> e : map.entrySet()){
            e.getValue().done();
        }
        map.clear();
    }
}