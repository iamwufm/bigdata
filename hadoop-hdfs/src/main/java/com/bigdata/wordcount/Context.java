package com.bigdata.wordcount;

import java.util.HashMap;
import java.util.Map;

/**
 * Date:2023/9/4
 * Author:wfm
 * Desc:
 */
public class Context {

    private HashMap<Object, Object> contexMap = new HashMap<>();

    public void write(Object key, Object value) {
        contexMap.put(key, value);
    }

    public Object get(Object key){
        return contexMap.get(key);
    }

    public HashMap<Object,Object> getContexMap(){
        return contexMap;
    }
}
