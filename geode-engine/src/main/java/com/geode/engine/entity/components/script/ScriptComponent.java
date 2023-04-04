package com.geode.engine.entity.components.script;

import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.Updatable;
import com.geode.engine.entity.components.Component;
import com.geode.engine.utils.LaterList;
import lombok.Getter;

public class ScriptComponent extends Component
{
    @Getter
    private final LaterList<Script<?>> scripts = new LaterList<>();

    public ScriptComponent(GameObject parent, Integer priority)
    {
        super(parent, priority);
    }

    public <T> Script<T> addScript(Script<T> script)
    {
        scripts.addLater(script);
        return script;
    }

    public void removeScript(Script<?> script)
    {
        scripts.removeLater(script);
    }

    @Override
    public void update()
    {
        for(Script<?> script : scripts)
        {
            script.run();
            if(script.mustBeAborted())
            {
                scripts.removeLater(script);
            }
        }
        if(scripts.isHadAdditions())
        {
            scripts.sync();
            scripts.sort((o1, o2) -> o1.getPriority() < o2.getPriority() ? -1 : 1);
        }
        else
        {
            scripts.sync();
        }
    }
}
