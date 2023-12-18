package com.geode.net.examples;

import com.geode.net.communications.TcpJsonPipe;
import com.geode.net.connections.TcpConnection;
import com.geode.net.connections.TcpStickyConnection;

import java.io.Serializable;

public class TcpJsonObjDataTransfert
{
    static class Person implements Serializable
    {
        private String name;
        private int age;

        public Person()
        {
            
        }

        public Person(String name, int age)
        {
            this.name = name;
            this.age = age;
        }

        

        public String getName() {
            return name;
        }



        public int getAge() {
            return age;
        }



        public void setName(String name) {
            this.name = name;
        }



        public void setAge(int age) {
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
        TcpConnection client = TcpConnection.internal(5000);
        TcpConnection clientHandler = server.accept();

        TcpJsonPipe clientPipe = new TcpJsonPipe(client);
        TcpJsonPipe clientHandlerPipe = new TcpJsonPipe(clientHandler);
        clientPipe.prepareRecv(Person.class);
        clientHandlerPipe.prepareRecv(Person.class);
        
        Person jsonObject = new Person("Arthur", 24);

        clientPipe.send(jsonObject);
        jsonObject = (Person) clientHandlerPipe.recv();
        System.out.println(jsonObject);

        clientPipe.close();
        clientHandlerPipe.close();
        server.close();
    }
}
