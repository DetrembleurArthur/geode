package com.geode.engine.binding;


public class Test
{
    static class Person
    {
        private int age;
        private String name;

        public Person(int age, String name)
        {
            this.age = age;
            this.name = name;
        }

        public int getAge()
        {
            return age;
        }

        public void setAge(int age)
        {
            this.age = age;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }
    }

    public static void main(String[] args)
    {
        Person person1 = new Person(22, "Arthur");
        Person person2 = new Person(25, "Jean");

        FieldPropertiesScheme<Person> propertiesScheme = new FieldPropertiesScheme<>(Person.class);
        var scheme = propertiesScheme.<Integer>get("age");

        NotifyProperty<Integer> ageProperty1 = scheme.create(person1);
        NotifyProperty<Integer> ageProperty2 = scheme.create(person2);

        ageProperty1.link(ageProperty2);

        ageProperty1.set(42);


        System.out.println(person1.getAge() + " " + person2.getAge());

    }
}
