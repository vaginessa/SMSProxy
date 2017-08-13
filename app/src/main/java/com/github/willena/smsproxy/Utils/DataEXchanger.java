package com.github.willena.smsproxy.Utils;

import java.util.HashMap;

/**
 * Created by guill on 12/08/2017.
 */

public class DataEXchanger extends HashMap<String, Object> {

    private static DataEXchanger instance;

    private DataEXchanger() {
        super();
    }

    private static DataEXchanger getInstance() {
        if (instance == null)
            instance = new DataEXchanger();

        return instance;
    }

    public static void add(String key, Object data) {
        getInstance().put(key, data);
    }

    public static Object getData(String key) {
        Object o = getInstance().get(key);
        getInstance().remove(key);
        return o;
    }
}
