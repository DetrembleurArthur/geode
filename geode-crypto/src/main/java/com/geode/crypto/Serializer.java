package com.geode.crypto;

import java.io.*;
import java.nio.ByteBuffer;

public class Serializer
{
    public static byte[] serialize(Object obj) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    public static byte[] byteToBytes(byte data)
    {
        return new byte[]{ data };
    }

    public static byte[] shortToByte(short data)
    {
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
        buffer.putShort(data);
        buffer.flip();
        byte[] bytes = buffer.array();
        buffer.clear();
        return bytes;
    }

    public static byte[] intToByte(int data)
    {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(data);
        buffer.flip();
        byte[] bytes = buffer.array();
        buffer.clear();
        return bytes;
    }

    public static byte[] longToByte(long data)
    {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(data);
        buffer.flip();
        byte[] bytes = buffer.array();
        buffer.clear();
        return bytes;
    }

    public static byte[] charToByte(char data)
    {
        ByteBuffer buffer = ByteBuffer.allocate(Character.BYTES);
        buffer.putChar(data);
        buffer.flip();
        byte[] bytes = buffer.array();
        buffer.clear();
        return bytes;
    }

    public static byte[] FloatToByte(float data)
    {
        ByteBuffer buffer = ByteBuffer.allocate(Float.BYTES);
        buffer.putFloat(data);
        buffer.flip();
        byte[] bytes = buffer.array();
        buffer.clear();
        return bytes;
    }

    public static byte[] doubleToByte(double data)
    {
        ByteBuffer buffer = ByteBuffer.allocate(Double.BYTES);
        buffer.putDouble(data);
        buffer.flip();
        byte[] bytes = buffer.array();
        buffer.clear();
        return bytes;
    }

    public static byte[] merge(byte[] ... bytesArrays)
    {
        int size = 0;
        for(byte[] array : bytesArrays)
        {
            size += array.length;
        }
        byte[] output = new byte[size];
        int j = 0;
        for (byte[] bytesArray : bytesArrays)
        {
            for (byte b : bytesArray)
            {
                output[j++] = b;
            }
        }
        return output;
    }

    public static String bytesToString(byte[] bytes)
    {
        StringBuilder buffer = new StringBuilder();
        for (byte b : bytes)
        {
            buffer.append(String.format("%02x", b));
        }
        return buffer.toString();
    }
}
