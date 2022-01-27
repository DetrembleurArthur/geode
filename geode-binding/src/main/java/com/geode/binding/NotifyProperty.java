package com.geode.binding;

import java.util.ArrayList;

public class NotifyProperty <T> extends Property<T>
{
    private final ArrayList<Trigger> triggers = new ArrayList<>();
    private boolean triggered = false;

    public NotifyProperty(T value)
    {
        super(value);
    }

    public void bind(Property<T> property)
    {
        bind(property, true);
    }

    public void bind(Property<T> property, boolean update)
    {
        Trigger trigger = new Trigger();
        trigger.setDest(property);
        trigger.setRunnable(() -> property.set(get()));
        triggers.add(trigger);
        if(update)
            update();
    }

    public void link(NotifyProperty<T> property)
    {
        property.bind(this, false);
        bind(property, true);
    }

    public <U> void bind(Property<U> property, Converter<T, U> converter)
    {
        bind(property, converter, true);
    }

    public <U> void bind(Property<U> property, Converter<T, U> converter, boolean update)
    {
        Trigger trigger = new Trigger();
        trigger.setDest(property);
        trigger.setRunnable(() -> property.set(converter.convert(get())));
        triggers.add(trigger);
        if(update)
            update();
    }

    public <U> void link(NotifyProperty<U> property, Converter<T, U> converter1, Converter<U, T> converter2)
    {
        property.bind(this, converter2, false);
        bind(property, converter1, true);
    }

    public void unbind(Property<?> property)
    {
        triggers.removeIf(trigger -> trigger.getDest() == property);
    }

    @Override
    public void set(T value)
    {
        if(!triggered)
        {
            super.set(value);
            update();
        }
    }

    public void update()
    {
        triggered = true;
        for(Trigger trigger : triggers)
        {
            trigger.trigger();
        }
        triggered = false;
    }

    public Property<T> generateProperty()
    {
        Property<T> property = new Property<>(get());
        bind(property);
        return property;
    }

    public static <U> NotifyProperty<U> create(U value)
    {
        return new NotifyProperty<>(value);
    }
}
