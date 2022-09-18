package com.geode.engine.entity.components.animation;

import com.geode.engine.binding.NotifyProperty;
import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.Line;
import com.geode.engine.entity.components.Component;
import com.geode.engine.tweening.TimedTweenAction;
import com.geode.engine.tweening.TweenAction;
import com.geode.engine.tweening.TweenFunction;
import com.geode.engine.tweening.TweenSetter;
import com.geode.engine.utils.LaterList;
import lombok.Getter;
import org.joml.Vector2f;

import java.util.Arrays;

public class AnimationComponent extends Component
{
    @Getter
    private final LaterList<TimedTweenAction> tweenActions = new LaterList<>();

    public AnimationComponent(GameObject parent, Integer priority)
    {
        super(parent, priority);
    }

    @Override
    public void update()
    {
        for(TimedTweenAction action : tweenActions)
        {
            action.run();
            if(action.isFinished())
            {
                if(action.getWhenFinished() != null)
                    action.getWhenFinished().run();
                System.out.println("remove " + action);
                tweenActions.removeLater(action);
            }
        }
        tweenActions.sync();
    }

    private FloatTimedAnimationBuilder to(TweenSetter setter)
    {
        FloatTimedAnimationBuilder builder = new FloatTimedAnimationBuilder();
        TimedTweenAction action = builder.getActions()[0];
        action.setBinder(setter);
        tweenActions.add(action);
        return builder;
    }

    private FloatTimedAnimationBuilder toProperty(NotifyProperty<Float> property)
    {
        FloatTimedAnimationBuilder builder = new FloatTimedAnimationBuilder();
        TimedTweenAction action = builder.getActions()[0];
        builder.property(new NotifyProperty[]{property});
        tweenActions.add(action);
        return builder;
    }

    private Vector2fTimedAnimationBuilder toVector2f(TweenSetter[] setters)
    {
        Vector2fTimedAnimationBuilder builder = new Vector2fTimedAnimationBuilder();
        TimedTweenAction[] actions = builder.getActions();
        for(int i = 0; i < 2; i++)
        {
            actions[i].setBinder(setters[i]);
            tweenActions.add(actions[i]);
        }
        return builder;
    }

    private Vector2fTimedAnimationBuilder toVector2fProperty(NotifyProperty<Float>[] properties)
    {
        Vector2fTimedAnimationBuilder builder = new Vector2fTimedAnimationBuilder();
        TimedTweenAction[] actions = builder.getActions();
        builder.property(properties);
        tweenActions.addAll(Arrays.asList(actions));
        return builder;
    }

    private Vector4fTimedAnimationBuilder toVector4f(TweenSetter[] setters)
    {
        Vector4fTimedAnimationBuilder builder = new Vector4fTimedAnimationBuilder();
        TimedTweenAction[] actions = builder.getActions();
        for(int i = 0; i < 4; i++)
        {
            actions[i].setBinder(setters[i]);
            tweenActions.add(actions[i]);
        }
        return builder;
    }

    private Vector4fTimedAnimationBuilder toVector4fProperty(NotifyProperty<Float>[] properties)
    {
        Vector4fTimedAnimationBuilder builder = new Vector4fTimedAnimationBuilder();
        TimedTweenAction[] actions = builder.getActions();
        builder.property(properties);
        tweenActions.addAll(Arrays.asList(actions));
        return builder;
    }


    public FloatTimedAnimationBuilder toX()
    {
        return to(value -> getParent().getTransform().setX(value));
    }

    public FloatTimedAnimationBuilder toXProperty()
    {
        return toProperty(getParent().properties_c().x());
    }

    public FloatTimedAnimationBuilder toY()
    {
        return to(value -> getParent().getTransform().setY(value));
    }

    public FloatTimedAnimationBuilder toYProperty()
    {
        return toProperty(getParent().properties_c().y());
    }

    public FloatTimedAnimationBuilder toWidth()
    {
        return to(value -> getParent().getTransform().setWidth(value));
    }

    public FloatTimedAnimationBuilder toWidthProperty()
    {
        return toProperty(getParent().properties_c().width());
    }

    public FloatTimedAnimationBuilder toHeight()
    {
        return to(value -> getParent().getTransform().setHeight(value));
    }

    public FloatTimedAnimationBuilder toHeightProperty()
    {
        return toProperty(getParent().properties_c().height());
    }

    public Vector2fTimedAnimationBuilder toPosition2D()
    {
        return toVector2f(new TweenSetter[]{
                value -> getParent().getTransform().setX(value),
                value -> getParent().getTransform().setY(value)});
    }

    public Vector2fTimedAnimationBuilder toPosition2DProperty()
    {
        return toVector2fProperty(new NotifyProperty[]{
                getParent().properties_c().x(),
                getParent().properties_c().y()
        });
    }

    public Vector2fTimedAnimationBuilder toSize2D()
    {
        return toVector2f(new TweenSetter[]{
                value -> getParent().getTransform().setWidth(value),
                value -> getParent().getTransform().setY(value)});
    }

    public Vector2fTimedAnimationBuilder toSize2DProperty()
    {
        return toVector2fProperty(new NotifyProperty[]{
                getParent().properties_c().width(),
                getParent().properties_c().height()
        });
    }

    public FloatTimedAnimationBuilder toRed()
    {
        return to(value -> getParent().setRed(value));
    }

    public FloatTimedAnimationBuilder toRedProperty()
    {
        return toProperty(getParent().properties_c().red());
    }

    public FloatTimedAnimationBuilder toGreen()
    {
        return to(value -> getParent().setGreen(value));
    }

    public FloatTimedAnimationBuilder toGreenProperty()
    {
        return toProperty(getParent().properties_c().green());
    }

    public FloatTimedAnimationBuilder toBlue()
    {
        return to(value -> getParent().setBlue(value));
    }

    public FloatTimedAnimationBuilder toBlueProperty()
    {
        return toProperty(getParent().properties_c().blue());
    }

    public FloatTimedAnimationBuilder toAlpha()
    {
        return to(value -> getParent().setAlpha(value));
    }

    public FloatTimedAnimationBuilder toAlphaProperty()
    {
        return toProperty(getParent().properties_c().alpha());
    }

    public Vector4fTimedAnimationBuilder toColor()
    {
        return toVector4f(new TweenSetter[]{
                value -> getParent().setRed(value),
                value -> getParent().setGreen(value),
                value -> getParent().setBlue(value),
                value -> getParent().setAlpha(value)});
    }

    public Vector4fTimedAnimationBuilder toColorProperty()
    {
        return toVector4fProperty(new NotifyProperty[]{
                getParent().properties_c().red(),
                getParent().properties_c().green(),
                getParent().properties_c().blue(),
                getParent().properties_c().alpha()
        });
    }

    public FloatTimedAnimationBuilder toAngle()
    {
        return to(value -> getParent().getTransform().setAngle(value));
    }

    public FloatTimedAnimationBuilder toAngleProperty()
    {
        return toProperty(getParent().properties_c().angle());
    }

    public void followLine(Line line, float delay)
    {
        int linesize = line.getPointsCache().size();
        delay = delay > 0f ? delay / linesize : delay * -1;
        var builder = toPosition2D().from(getParent().getTransform().getPosition2D())
                .to(line.getPoint(0)).delay(delay);
        var origin = builder;
        for(int i = 1; i < linesize; i++)
        {
            var builder2 = toPosition2D()
                    .from(line.getPoint(i-1))
                    .to(line.getPoint(i)).delay(delay);
            builder.whenFinished(builder2::start);
            builder = builder2;
        }
        origin.start();
    }

    public void followLineProperty(Line line, float delay)
    {
        int linesize = line.getPointsCache().size();
        delay = delay > 0f ? delay / linesize : delay * -1;
        var builder = toPosition2DProperty().from(getParent().getTransform().getPosition2D())
                .to(line.getPoint(0)).delay(delay);
        var origin = builder;
        for(int i = 1; i < linesize; i++)
        {
            var builder2 = toPosition2DProperty()
                    .from(line.getPoint(i-1))
                    .to(line.getPoint(i)).delay(delay);
            builder.whenFinished(builder2::start);
            builder = builder2;
        }
        origin.start();
    }
}
