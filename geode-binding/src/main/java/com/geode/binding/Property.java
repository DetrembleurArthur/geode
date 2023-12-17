package com.geode.binding;

public class Property<T>
{
    private T value;

    public Property()
    {

    }

    public Property(T value)
    {
        this.value = value;
    }

    public void set(T value)
    {
        this.value = value;
    }

    public T get()
    {
        return value;
    }

    public static <U> Property<U> create(U value)
    {
        return new Property<>(value);
    }

    @Override
    public String toString() {
        return "Property [value=" + value + "]";
    }
}
