package com.geode.net.misc;

import com.geode.net.annotations.Dto;

import javax.lang.model.type.NullType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LowLevelSerializer
{
    private static HashMap<Integer, Class<?>> typesMap = new HashMap<>();
    private static HashMap<Class<?>, Integer> idMap = new HashMap<>();
    private static HashMap<Integer, Class<?>> dtoTypesMap = new HashMap<>();
    private static HashMap<Class<?>, Integer> dtoIdMap = new HashMap<>();

    static
    {
        registerPrimitive(0, NullType.class);
        registerPrimitive(1, Byte.class);
        registerPrimitive(2, Short.class);
        registerPrimitive(3, Integer.class);
        registerPrimitive(4, Long.class);
        registerPrimitive(5, Character.class);
        registerPrimitive(6, Float.class);
        registerPrimitive(7, Double.class);
        registerPrimitive(8, Boolean.class);
        registerPrimitive(9, String.class);
        //registerPrimitive(10, ArrayList.class);
    }

    private static void registerPrimitive(int id, Class<?> pClass)
    {
        typesMap.put(id, pClass);
        idMap.put(pClass, id);
    }

    public static void registerDto(Class<?> pClass)
    {
        if(pClass.isAnnotationPresent(Dto.class))
        {
            int id = pClass.getAnnotation(Dto.class).value();
            dtoTypesMap.put(id, pClass);
            dtoIdMap.put(pClass, id);
        }
    }

    public static byte[] serialize(Object ... objects) throws InvocationTargetException, IllegalAccessException
    {
        ArrayList<Byte> bytes = new ArrayList<>();
        for(Object obj : objects)
        {
            serializeOne(obj, bytes);
        }
        byte[] output = new byte[Integer.BYTES + bytes.size()];
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(bytes.size());
        buffer.flip();
        output[0] = buffer.get(0);
        output[1] = buffer.get(1);
        output[2] = buffer.get(2);
        output[3] = buffer.get(3);
        buffer.clear();
        for(int i = 4; i < output.length; i++) output[i] = bytes.get(i - Integer.BYTES);
        return output;
    }

    private static void serializeOne(Object obj, ArrayList<Byte> bytes) throws InvocationTargetException, IllegalAccessException
    {
        if(obj == null) return;
        Integer id = idMap.get(obj.getClass());
        if(id != null)
        {
            serializePrim(obj, bytes, id);
        }
        else
        {
            id = dtoIdMap.get(obj.getClass());
            if(id != null)
            {
                serializeDto(obj, bytes, id);
            }
        }
    }

    private static void serializeDto(Object obj, ArrayList<Byte> bytes, int id) throws InvocationTargetException, IllegalAccessException
    {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(id);
        buffer.flip();
        for(byte b : buffer.array()) bytes.add(b);
        buffer.clear();
        Stream<Method> methods = Arrays.stream(obj.getClass().getMethods()).filter(method -> method.isAnnotationPresent(Dto.get.class));
        List<Method> methodList = methods.sorted((o1, o2) -> o1.getAnnotation(Dto.get.class).value() > o2.getAnnotation(Dto.get.class).value() ? 1 : -1).collect(Collectors.toList());
        for(Method getter : methodList)
        {
            if(getter.isAnnotationPresent(Dto.get.class))
            {
                serializeOne(getter.invoke(obj), bytes);
            }
        }
    }

    private static void serializePrim(Object obj, ArrayList<Byte> bytes, Integer id)
    {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES + size(obj));
        buffer.putInt(id);
        Class<?> _class = obj.getClass();
        if(_class.equals(Byte.class)) buffer.put((Byte) obj);
        else if(_class.equals(Short.class)) buffer.putShort((Short) obj);
        else if(_class.equals(Integer.class)) buffer.putInt((Integer) obj);
        else if(_class.equals(Boolean.class)) buffer.putInt((boolean)obj ? 1 : 0);
        else if(_class.equals(Long.class)) buffer.putLong((Long) obj);
        else if(_class.equals(Float.class)) buffer.putFloat((Float) obj);
        else if(_class.equals(Double.class)) buffer.putDouble((Double) obj);
        else if(_class.equals(Character.class)) buffer.putChar((Character) obj);
        else if(_class.equals(String.class))
        {
            buffer.put(((String) obj).getBytes()); //charset à spécifier
            buffer.put((byte)0);
        }
        buffer.flip();
        for(byte b : buffer.array()) bytes.add(b);
        buffer.clear();
    }

    public static ArrayList<Object> deserialize(byte[] bytes) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException
    {
        ArrayList<Object> objects = new ArrayList<>();
        DeserializedMeta meta = new DeserializedMeta();
        meta.offset = 0;
        while (meta.offset < bytes.length)
        {
            meta.offset = deserializeOne(bytes, meta);
            objects.add(meta.deserialized);
            meta.deserialized = null;
        }

        return objects;
    }

    static class DeserializedMeta
    {
        public Object deserialized;
        public int offset;
    }

    public static int deserializeOne(byte[] bytes, DeserializedMeta meta) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException
    {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(bytes, meta.offset, Integer.BYTES);
        buffer.flip();
        int id = buffer.getInt();
        buffer.clear();
        meta.offset += Integer.BYTES;
        Class<?> pClass = typesMap.get(id);

        if(pClass != null)
        {
            meta.offset = deserializePrim(bytes, meta, pClass);
        }
        else
        {
            pClass = dtoTypesMap.get(id);
            if(pClass != null)
            {
                meta.offset = deserializeDto(bytes, meta, pClass);
            }
        }
        return meta.offset;
    }

    private static int deserializeDto(byte[] bytes, DeserializedMeta meta, Class<?> pClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException
    {
        Object obj = pClass.getConstructor().newInstance();
        Stream<Method> methods = Arrays.stream(obj.getClass().getMethods()).filter(method -> method.isAnnotationPresent(Dto.set.class));
        List<Method> methodList = methods.sorted((o1, o2) -> o1.getAnnotation(Dto.set.class).value() > o2.getAnnotation(Dto.set.class).value() ? 1 : -1).collect(Collectors.toList());
        for(Method setter : methodList)
        {
            if(setter.isAnnotationPresent(Dto.set.class))
            {
               // System.out.println(setter.getName());
                if(meta.offset >= bytes.length)
                {
                    break;
                }
                meta.offset = deserializeOne(bytes, meta);
                setter.invoke(obj, meta.deserialized);
            }
        }
        meta.deserialized = obj;
        return meta.offset;
    }

    private static int deserializePrim(byte[] bytes, DeserializedMeta meta, Class<?> pClass)
    {
        ByteBuffer buffer;
        if(pClass.equals(Byte.class))
        {
            meta.deserialized = bytes[meta.offset];
            return meta.offset + Byte.BYTES;
        }
        else if(pClass.equals(Short.class))
        {
            buffer = ByteBuffer.allocate(Short.BYTES);
            buffer.put(bytes, meta.offset, Short.BYTES);
            buffer.flip();
            short data = buffer.getShort();
            buffer.clear();
            meta.deserialized = data;
            return meta.offset + Short.BYTES;
        }
        else if(pClass.equals(Integer.class))
        {
            buffer = ByteBuffer.allocate(Integer.BYTES);
            buffer.put(bytes, meta.offset, Integer.BYTES);
            buffer.flip();
            int data = buffer.getInt();
            buffer.clear();
            meta.deserialized = data;
            return meta.offset + Integer.BYTES;
        }
        else if(pClass.equals(Boolean.class))
        {
            buffer = ByteBuffer.allocate(Integer.BYTES);
            buffer.put(bytes, meta.offset, Integer.BYTES);
            buffer.flip();
            boolean data = buffer.getInt() > 0;
            buffer.clear();
            meta.deserialized = data;
            return meta.offset + Integer.BYTES;
        }
        else if(pClass.equals(Long.class))
        {
            buffer = ByteBuffer.allocate(Long.BYTES);
            buffer.put(bytes, meta.offset, Long.BYTES);
            buffer.flip();
            long data = buffer.getLong();
            buffer.clear();
            meta.deserialized = data;
            return meta.offset + Long.BYTES;
        }
        else if(pClass.equals(Float.class))
        {
            buffer = ByteBuffer.allocate(Float.BYTES);
            buffer.put(bytes, meta.offset, Float.BYTES);
            buffer.flip();
            float data = buffer.getFloat();
            buffer.clear();
            meta.deserialized = data;
            return meta.offset + Float.BYTES;
        }
        else if(pClass.equals(Double.class))
        {
            buffer = ByteBuffer.allocate(Double.BYTES);
            buffer.put(bytes, meta.offset, Double.BYTES);
            buffer.flip();
            double data = buffer.getDouble();
            buffer.clear();
            meta.deserialized = data;
            return meta.offset + Double.BYTES;
        }
        else if(pClass.equals(Character.class))
        {
            buffer = ByteBuffer.allocate(Character.BYTES);
            buffer.put(bytes, meta.offset, Character.BYTES);
            buffer.flip();
            char data = buffer.getChar();
            buffer.clear();
            meta.deserialized = data;
            return meta.offset + Character.BYTES;
        }
        else if(pClass.equals(String.class))
        {
            int size = 0;
            for(int i = meta.offset; bytes[i] != 0; i++)
            {
                size++;
            }
            buffer = ByteBuffer.allocate(size);
            buffer.put(bytes, meta.offset, size);
            buffer.flip();
            String data = new String(buffer.array());
            buffer.clear();
            meta.deserialized = data;
            return meta.offset + size + 1;
        }
        return 0;
    }

    public static int size(Object obj)
    {
        if(obj.getClass().equals(Byte.class))
            return Byte.BYTES;
        else if(obj.getClass().equals(Short.class))
            return Short.BYTES;
        else if(obj.getClass().equals(Integer.class))
            return Integer.BYTES;
        else if(obj.getClass().equals(Long.class))
            return Long.BYTES;
        else if(obj.getClass().equals(Float.class))
            return Float.BYTES;
        else if(obj.getClass().equals(Double.class))
           return Double.BYTES;
        else if(obj.getClass().equals(Character.class))
            return Character.BYTES;
        else if(obj.getClass().equals(Boolean.class))
            return Integer.BYTES;
        else if(obj.getClass().equals(String.class))
            return ((String) obj).getBytes().length + 1;
        return 0;
    }

    public static String bytesToString(byte[] bytes)
    {

        StringBuilder buffer = new StringBuilder();
        for (byte b : bytes)
        {
            buffer.append(String.format("%02x ", b));
        }
        return buffer.toString();
    }

    public static void showBytes(byte[] bytes)
    {
        System.out.println(bytesToString(bytes));
    }
}
