package com.rlogin.common.frame;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

public class Parameter {

    private LinkedHashMap<String, Object> parameters = Maps.newLinkedHashMap();

    public Parameter() {

    }

    public Parameter put(String key, Object value) {
        parameters.put(key, value);
        return this;
    }

    public void putIfAbsent(String key, Object value) {
        if (!parameters.containsKey(key)) {
            parameters.put(key, value);
        }

    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("parameter: ");
        Iterator<Entry<String, Object>> i = parameters.entrySet().iterator();
        if (!i.hasNext()) {
            return "{}";
        }
        sb.append('{');
        for (;;) {
            Entry<String, Object> e = i.next();
            String key = e.getKey();
            Object value = e.getValue();
            sb.append(key);
            sb.append('=');
            sb.append(value);
            if (!i.hasNext()) {
                return sb.append('}').toString();
            }
            sb.append(", ");
        }
    }
}
