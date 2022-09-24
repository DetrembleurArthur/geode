package com.geode.engine.entity.components.event;

import com.geode.engine.debug.Log;
import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.components.Component;
import com.geode.engine.graphics.Camera;

import java.util.ArrayList;

public class EventComponent extends Component
{
    private final ArrayList<Event> events;
    private final ArrayList<Event> callLaterEvents = new ArrayList<>();

    public EventComponent(GameObject parent, Integer priority)
    {
        super(parent, priority);
        events = new ArrayList<>();
    }

    public ArrayList<Event> getEvents()
    {
        return events;
    }

    public void sortEvents()
    {
        events.sort(Event::compareTo);
    }

    public void setEventPriority(Class<? extends Event> eventClass, int priority)
    {
        getEvent(eventClass).setPriority(priority);
        sortEvents();
    }

    public void addEvent(Event event)
    {
        events.add(event);
        sortEvents();
    }

    public <T extends Event> T getEvent(Class<T> eventClass)
    {
        for (Event event : events)
        {
            if (event.getClass().equals(eventClass))
            {
                return (T) event;
            }
        }
        return null;
    }

    protected void pollEvent()
    {
        for (Event event : events)
        {
            if (event.isAppend())
            {
                callLaterEvents.add(event);
            }
        }
        if (!callLaterEvents.isEmpty())
        {
            for (Event event : callLaterEvents)
            {
                Log.print("EVENT => " + event.getClass().getSimpleName());
                event.run(this);
            }
            callLaterEvents.clear();
        }
    }

    protected boolean onEvent(Class<? extends Event> eventClass, ActionEvent action)
    {
        for (Event event : getEvents())
        {
            if (event.getClass().equals(eventClass))
            {
                event.addActionEvent(action);
                return true;
            }
        }
        return false;
    }

    public void clearEvents()
    {
        events.clear();
    }

    public void clearEvent(Class<? extends Event> eventClass)
    {
        Event event = getEvent(eventClass);
        if (event != null)
            events.remove(event);
    }


    public void onMouseHovering(ActionEvent action, Camera camera2D)
    {
        if (!onEvent(MouseHoveringEvent.class, action))
        {
            addEvent(new MouseHoveringEvent(getParent(), camera2D).addActionEvent(action));
        }
    }

    public void onMouseEntered(ActionEvent action, Camera camera2D)
    {
        if (!onEvent(MouseEnteredEvent.class, action))
        {
            addEvent(new MouseEnteredEvent(getParent(), camera2D).addActionEvent(action));
        }
    }

    public void onMouseExited(ActionEvent action, Camera camera2D)
    {
        if (!onEvent(MouseExitedEvent.class, action))
        {
            addEvent(new MouseExitedEvent(getParent(), camera2D).addActionEvent(action));
        }
    }

    public void onMouseMoved(ActionEvent action, Camera camera2D)
    {
        if (!onEvent(MouseMoveEvent.class, action))
        {
            addEvent(new MouseMoveEvent(getParent(), camera2D).addActionEvent(action));
        }
    }

    public void onMouseButtonClicked(ActionEvent action, boolean repeated, Camera camera2D)
    {
        if (!onEvent(MouseButtonClickEvent.class, action))
        {
            addEvent(new MouseButtonClickEvent(getParent(), repeated, camera2D).addActionEvent(action));
        }
    }

    public void onMouseLeftButtonClicked(ActionEvent action, boolean repeated, Camera camera2D)
    {
        if (!onEvent(MouseLeftButtonClickEvent.class, action))
        {
            addEvent(new MouseLeftButtonClickEvent(getParent(), repeated, camera2D).addActionEvent(action));
        }
    }

    public void onMouseRightButtonClicked(ActionEvent action, boolean repeated, Camera camera2D)
    {
        if (!onEvent(MouseRightButtonClickEvent.class, action))
        {
            addEvent(new MouseRightButtonClickEvent(getParent(), repeated, camera2D).addActionEvent(action));
        }
    }

    public void onMouseMiddleButtonClicked(ActionEvent action, boolean repeated, Camera camera2D)
    {
        if (!onEvent(MouseMiddleButtonClickEvent.class, action))
        {
            addEvent(new MouseMiddleButtonClickEvent(getParent(), repeated, camera2D).addActionEvent(action));
        }
    }

    public void onMouseButtonReleased(ActionEvent action, Camera camera2D)
    {
        if (!onEvent(MouseButtonReleasedEvent.class, action))
        {
            addEvent(new MouseButtonReleasedEvent(getParent(), camera2D).addActionEvent(action));
        }
    }

    public void onMouseLeftButtonReleased(ActionEvent action, Camera camera2D)
    {
        if (!onEvent(MouseLeftButtonRealeasedEvent.class, action))
        {
            addEvent(new MouseLeftButtonRealeasedEvent(getParent(), camera2D).addActionEvent(action));
        }
    }

    public void onMouseRightButtonReleased(ActionEvent action, Camera camera2D)
    {
        if (!onEvent(MouseRightButtonRealeasedEvent.class, action))
        {
            addEvent(new MouseRightButtonRealeasedEvent(getParent(), camera2D).addActionEvent(action));
        }
    }

    public void onMouseMiddleButtonReleased(ActionEvent action, Camera camera2D)
    {
        if (!onEvent(MouseMiddleButtonRealeasedEvent.class, action))
        {
            addEvent(new MouseMiddleButtonRealeasedEvent(getParent(), camera2D).addActionEvent(action));
        }
    }

    public void onMouseButtonDraged(ActionEvent action, Camera camera2D)
    {
        if (!onEvent(MouseButtonDragEvent.class, action))
        {
            addEvent(new MouseButtonDragEvent(getParent(), camera2D).addActionEvent(action));
        }
    }

    private void enableMouseDragging(int orientation, Camera camera2D)
    {
        MouseButtonDragEvent event = getEvent(MouseButtonDragEvent.class);
        if (event != null)
        {
            event.setDragActionEvent(orientation);
        } else
        {
            event = new MouseButtonDragEvent(getParent(), camera2D);
            event.setDragActionEvent(orientation);
            addEvent(event);
        }
    }

    public void enableMouseDragging(Camera camera2D)
    {
        enableMouseDragging(MouseButtonDragEvent.Orientation.BOTH, camera2D);
    }

    public void enableHorizontalMouseDragging(Camera camera2D)
    {
        enableMouseDragging(MouseButtonDragEvent.Orientation.HORIZONTAL, camera2D);
    }

    public void enableVerticalMouseDragging(Camera camera2D)
    {
        enableMouseDragging(MouseButtonDragEvent.Orientation.VERTICAL, camera2D);
    }

    public void changeDraggingOrientation(int orientation, Camera camera2D)
    {
        clearEvent(MouseButtonDragEvent.class);
        enableMouseDragging(orientation, camera2D);
    }

    public void onPositionChanged(ActionEvent action)
    {
        if (!onEvent(PositionChangedEvent.class, action))
        {
            addEvent(new PositionChangedEvent(getParent().getTransform()).addActionEvent(action));
        }
    }

    public void onMouseButtonDoubleClicked(ActionEvent action, Camera camera2D)
    {
        if (!onEvent(MouseButtonDoubleClickEvent.class, action))
        {
            addEvent(new MouseButtonDoubleClickEvent(getParent(), camera2D).addActionEvent(action));
        }
    }

    public void onColorChanged(ActionEvent action)
    {
        if (!onEvent(FillColorChangedEvent.class, action))
        {
            addEvent(new FillColorChangedEvent(getParent()).addActionEvent(action));
        }
    }

    public void onValueChanged(ActionEvent action)
    {
        Valuable valuable = getParent() instanceof Valuable ? (Valuable) getParent() : null;
        if (valuable != null)
        {
            if (!onEvent(ValueChangedEvent.class, action))
            {
                addEvent(new ValueChangedEvent(valuable, valuable.getValue()).addActionEvent(action));
            }
        }
    }

    public void onCollision(ActionEvent action, GameObject object)
    {
        if (!onEvent(CollisionEvent.class, action))
        {
            addEvent(new CollisionEvent(getParent(), object).addActionEvent(action));
        }
    }

    @Override
    public void update()
    {
        pollEvent();
    }

}
