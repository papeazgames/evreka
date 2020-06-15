package com.sarslan.demos.evreka.container;

import java.util.concurrent.ConcurrentHashMap;

public class ContainerMng {

    private ConcurrentHashMap<Integer, Container> map;

    private volatile boolean volb;

    private ContainerMng() {
        map = new ConcurrentHashMap<>();
    }

    public static ContainerMng getInstance() {
        return HelperHolder.INSTANCE;
    }

    public Container getContainer(Integer containerId) {
        return map.containsKey(containerId) ? map.get(containerId) : null;
    }


    private static class HelperHolder {
        private static final ContainerMng INSTANCE =
                new ContainerMng();
    }

    public void createContainers(int numberOfCont) {
        for (int i = 0; i < numberOfCont; i++) {
            Container con = new Container(i);
            map.put(i, con);
        }
    }
}
