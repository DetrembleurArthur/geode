package com.geode.net.examples;

import com.geode.net.communications.TcpJsonPipe;
import com.geode.net.communications.TcpObjectPipe;
import com.geode.net.connections.TcpConnection;
import com.geode.net.connections.TcpStickyConnection;
import org.json.simple.JSONObject;

import java.io.Serializable;

public class TcpObjectDataTransfertServer
{
    static class Person implements Serializable
    {
        private String name;
        private int age;

        public Person(String name, int age)
        {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString()
        {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    public static void main(String[] args) throws Exception
    {
        TcpStickyConnection server = TcpStickyConnection.internal(5000, 5);
        TcpConnection clientHandler = server.accept();

        TcpObjectPipe clientHandlerPipe = new TcpObjectPipe(clientHandler);




        Person person = (Person) clientHandlerPipe.recv();
        System.out.println(person.toString());

        clientHandlerPipe.close();
        server.close();
    }
}
