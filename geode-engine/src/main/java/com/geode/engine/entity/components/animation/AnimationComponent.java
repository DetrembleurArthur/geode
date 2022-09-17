package com.geode.engine.entity.components.animation;

import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.components.Component;
import com.geode.engine.tweening.TweenAction;
import com.geode.engine.utils.LaterList;
import lombok.Getter;

public class AnimationComponent extends Component
{
    @Getter
    private final LaterList<TweenAction> tweenActions = new LaterList<>();

    public AnimationComponent(GameObject parent, Integer priority)
    {
        super(parent, priority);
    }

    @Override
    public void update()
    {
        for(TweenAction action : tweenActions)
        {
            action.run();
            if(action.isFinished())
            {
                System.out.println("remove " + action);
                tweenActions.removeLater(action);
            }
        }
        tweenActions.sync();
    }

    public TimedAnimationBuilder toX()
    {
        TimedAnimationBuilder builder = new TimedAnimationBuilder();
        TweenAction action = builder.getAction();
        action.setBinder(value -> getParent().getTransform().setX(value));
        tweenActions.add(action);
        return builder;
    }

    public TimedAnimationBuilder toXProperty()
    {
        TimedAnimationBuilder builder = new TimedAnimationBuilder();
        TweenAction action = builder.getAction();
        builder.property(getParent().properties_c().x());
        tweenActions.add(action);
        return builder;
    }
}
