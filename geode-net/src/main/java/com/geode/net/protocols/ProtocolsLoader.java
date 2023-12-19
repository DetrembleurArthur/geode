package com.geode.net.protocols;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class ProtocolsLoader {

    public static List<Class<?>> protocolClasses = new ArrayList<>();

    public static void load(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        assert stream != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        protocolClasses.addAll(reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> {
                    try {
                        return Class.forName(packageName + "." + line.replace(".class", ""));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).filter(Objects::nonNull).filter(aClass -> aClass.isAnnotationPresent(Protocol.class))
                .collect(Collectors.toList()));
    }

    public static void load(String... packageNames) {
        for (String packageName : packageNames) {
            load(packageName);
        }
    }

    public static void load(Class<?> class_) {
        if (class_.isAnnotationPresent(Protocol.class) && !protocolClasses.contains(class_))
            protocolClasses.add(class_);
    }

    public static void load(Class<?>... classes_) {
        protocolClasses.addAll(Arrays.stream(classes_)
                .filter(class_ -> class_.isAnnotationPresent(Protocol.class) && !protocolClasses.contains(class_))
                .collect(Collectors.toList()));
    }

    public static List<Class<?>> getProtocols(String... names) {
        return protocolClasses.stream()
                .filter(aClass -> Arrays.stream(names)
                        .anyMatch(name -> name.equals(aClass.getAnnotation(Protocol.class).value())))
                .collect(Collectors.toList());
    }
}
