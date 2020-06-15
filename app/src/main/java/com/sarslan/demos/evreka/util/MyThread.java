package com.sarslan.demos.evreka.util;

public class MyThread{

    private Thread t;

    protected boolean isDone;
    private ThreadUpdateObject o;

    private long lastTime;
    private long updatePeirodMillis;

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            update();
        }
    };

    public MyThread(ThreadUpdateObject o, long updatePeirodMillis){
        this.o=o;
        this.updatePeirodMillis=updatePeirodMillis;
        t = new Thread(runnable);
    }
    public void start() {

        lastTime = System.currentTimeMillis();

        t.start();
    }
    protected void update(){
        while (!isDone) {
            long now = System.currentTimeMillis();
            float dt = (now-lastTime)* 0.001f;
            dt = Math.max(0, dt);
            o.update(dt);

            lastTime = now;

            try {
                Thread.sleep(updatePeirodMillis);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
    public void done(){
        isDone = true;
    }
    public void setName(String name) {
        t.setName(name);
    }
}