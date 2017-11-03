package org.flippers.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectRegistry {

    private final static ObjectRegistry singleInstance = new ObjectRegistry();
    private Map<Class, Object> registeredClasses;

    private ObjectRegistry() {
        this.registeredClasses = new ConcurrentHashMap<>();
    }

    public static ObjectRegistry getInstance() {
        return singleInstance;
    }

    public void register(Object object) {
        Class<?> registryKey = object.getClass();
        if(this.registeredClasses.containsValue(object)) return;
        if (this.registeredClasses.containsKey(registryKey))
            throw new RuntimeException(String.format("Object for class %s already registered", registryKey));

        this.registeredClasses.put(registryKey, object);
    }

    public <T> T instanceOf(Class<T> className) {
        return (T) this.registeredClasses.get(className);
    }

    public void clear() {
        this.registeredClasses.clear();
    }

}
