package com.geode.engine.entity.components.script;

import com.geode.engine.binding.NotifyProperty;
import lombok.Getter;

public class Script<T> implements Runnable
{
    public static final int DEFAULT_PRIORITY = 0;
    public static final int MIN_PRIORITY = Integer.MIN_VALUE;
    public static final int MAX_PRIORITY = Integer.MAX_VALUE;

    @Getter
    private final NotifyProperty<T> dataProperty;
    private ScriptPolicy<T> abortPolicy;
    private ScriptAction<T> action = (param) -> {

    };
    @Getter
    private final int priority;

    public Script()
    {
        this(DEFAULT_PRIORITY, null);
    }

    public Script(int priority)
    {
        this(priority, null);
    }

    public Script(int priority, T data)
    {
        this.priority = priority;
        dataProperty = NotifyProperty.create(data);
    }

    public Script(T data)
    {
        this(DEFAULT_PRIORITY, data);
    }

    public Script<T> setAction(ScriptAction<T> action)
    {
        this.action = action;
        return this;
    }

    public Script<T> abortIf(ScriptPolicy<T> policy)
    {
        abortPolicy = policy;
        return this;
    }

    public boolean mustBeAborted()
    {
        return abortPolicy != null && abortPolicy.granted(this);
    }

    @Override
    public void run()
    {
        if (action != null)
        {
            if (dataProperty != null)
            {
                action.action(dataProperty.get());
                dataProperty.update();
            } else
                action.action(null);
        }
    }
}
