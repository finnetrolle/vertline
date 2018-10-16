package ru.finnetrolle.vertline.pipeline;

import java.util.LinkedHashMap;
import java.util.Map;

public class Context {

    private final Map<String, Object> map = new LinkedHashMap<>();

    public Object get(String name) {
        Object object = map.get(name);
        if (object == null) {
            throw new RuntimeException("No such element");
        }
        return object;
    }

    public void set(String name, Object value) {
        if (map.containsKey(name)) {
            throw new RuntimeException("Already has such element");
        }
        map.put(name, value);
    }

    @Override
    public String toString() {
        return "Context{" +
                "map=" + map +
                '}';
    }
}
