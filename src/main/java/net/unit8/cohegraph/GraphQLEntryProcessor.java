package net.unit8.cohegraph;

import com.tangosol.net.BackingMapContext;
import com.tangosol.net.BackingMapManagerContext;
import com.tangosol.util.Binary;
import com.tangosol.util.BinaryEntry;
import com.tangosol.util.Converter;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.processor.AbstractProcessor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphQLEntryProcessor<K, V> extends AbstractProcessor<K, V, V> {
    private final Map<String, JoinProcessor> joinProcessors = new HashMap<>();
    private final graphql.language.Field field;

    public GraphQLEntryProcessor(Class<V> modelClass, graphql.language.Field field) {
        this.field = field;
        Arrays.stream(modelClass.getDeclaredFields())
                .filter(f -> f.getAnnotation(JoinCache.class) != null)
                .map(f -> joinCache(modelClass, f))
                .forEach(jc -> joinProcessors.put(jc.getName(), jc));
    }

    private JoinProcessor joinCache(Class<V> modelClass, Field field) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(modelClass);
            JoinCache joinCache = field.getAnnotation(JoinCache.class);
            Method idGetter = Arrays.stream(beanInfo.getPropertyDescriptors())
                    .filter(pd -> pd.getName().equals(joinCache.idProperty()) && pd.getReadMethod() != null)
                    .map(PropertyDescriptor::getReadMethod)
                    .findFirst()
                    .orElseThrow();
            Method setter = Arrays.stream(beanInfo.getPropertyDescriptors())
                    .filter(pd -> pd.getName().equals(field.getName()) && pd.getWriteMethod() != null)
                    .map(PropertyDescriptor::getWriteMethod)
                    .findFirst()
                    .orElseThrow();

            return new JoinProcessor(field.getName(), joinCache.name(),
                    idGetter.getName(),
                    setter.getName());
        } catch (IntrospectionException e) {
            throw new IllegalStateException(e);
        }
    }
    @Override
    public V process(InvocableMap.Entry<K, V> entry) {
        V value = entry.getValue();
        BackingMapManagerContext managerContext = ((BinaryEntry<K, V>) entry).getContext();
        Converter<Object, Binary> keyToInternalConverter = managerContext.getKeyToInternalConverter();
        List<graphql.language.Field> childFields = field.getSelectionSet().getSelections()
                .stream()
                .filter(graphql.language.Field.class::isInstance)
                .map(graphql.language.Field.class::cast)
                .collect(Collectors.toList());

        joinProcessors.entrySet()
                .stream()
                .filter(e -> childFields.stream()
                        .anyMatch(f -> f.getName().equals(e.getKey())))
                .forEach(e -> {
                    graphql.language.Field field = childFields.stream()
                            .filter(f -> f.getName().equals(e.getKey()))
                            .findFirst()
                            .orElseThrow();
                    BackingMapContext context = managerContext.getBackingMapContext(e.getValue().getCacheName());
                    Object id = e.getValue().getId(value);
                    if (id instanceof List) {
                        List<Object> properties = ((List<Object>) id).stream()
                                .map(i -> {
                                    Binary key = keyToInternalConverter.convert(i);
                                    InvocableMap.Entry childEntry = context.getReadOnlyEntry(key);
                                    return new GraphQLEntryProcessor<>(childEntry.getValue().getClass(), field)
                                            .process(childEntry);
                                }).collect(Collectors.toList());
                        e.getValue().setProperty(value, properties);
                    } else {
                        Binary key = keyToInternalConverter.convert(id);
                        InvocableMap.Entry propertyEntry = context.getReadOnlyEntry(key);
                        Object prop = new GraphQLEntryProcessor<>(propertyEntry.getValue().getClass(), field)
                                .process(propertyEntry);
                        e.getValue().setProperty(value, prop);
                    }
                });
        return value;
    }
}
