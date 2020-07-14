package net.unit8.cohegraph;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class JoinProcessor implements Serializable {
    private final String name;
    private final String cacheName;
    private final String idGetter;
    private final String propertySetter;

    public JoinProcessor(String name, String cacheName,
                         String idGetter,
                         String propertySetter) {
        this.name = name;
        this.cacheName = cacheName;
        this.idGetter = idGetter;
        this.propertySetter = propertySetter;
    }

    public String getName() {
        return name;
    }

    public String getCacheName() {
        return this.cacheName;
    }

    public Object getId(Object model) {
        try {
            return model.getClass().getMethod(idGetter).invoke(model);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void setProperty(Object model, Object property){
        Arrays.stream(model.getClass().getMethods())
                .filter(m -> m.getName().equals(propertySetter) && m.getParameterCount() == 1)
                .filter(m -> m.getParameterTypes()[0].isAssignableFrom(property.getClass()))
                .findFirst()
                .ifPresent(m -> {
                    try {
                        m.invoke(model, property);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                });
    }
}
