package com.geode.engine.entity.components;

import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.Updatable;
import com.geode.engine.timer.DynamicTimer;
import com.geode.engine.utils.LaterList;

public class TimerComponent extends Component
{
    private final LaterList<DynamicTimer> timers;

    public TimerComponent(GameObject gameObject, Integer priority)
    {
        super(gameObject, priority);
        timers = new LaterList<>();
    }

    public void add(DynamicTimer timer)
    {
        timers.addLater(timer);
        timer.activate();
    }


    @Override
    public void update()
    {
        for (var timer : timers)
        {
            timer.run();
            if (timer.isFinished() && timer.isActionRunned())
                timers.removeLater(timer);
        }
        timers.sync();
    }
}
