package com.geode.binding;

import java.util.Random;

public class RandomGenerator extends NotifyProperty<Integer>
{
    private final Random random;

    public RandomGenerator()
    {
        super(0);
        random = new Random();
    }

    @Deprecated
    @Override
    public void set(Integer value)
    {

    }

    public void generate()
    {
        update();
    }

    @Override
    public Integer get()
    {
        return random.nextInt();
    }
}
