package com.geode.engine.entity.components.collision;

import com.geode.engine.entity.Circle;
import com.geode.engine.entity.GameObject;
import com.geode.engine.entity.Rect;
import com.geode.engine.entity.components.Component;
import com.geode.engine.utils.MathUtils;
import org.joml.Vector2f;

public class CollisionComponent extends Component
{
    private boolean dynamic = true;
    private BoundingBox boundingBox;

    public CollisionComponent(GameObject parent, Integer priority)
    {
        super(parent, priority);
    }

    @Override
    public void update()
    {

    }

    public boolean isDynamic()
    {
        return dynamic;
    }

    public void setDynamic(boolean dynamic)
    {
        this.dynamic = dynamic;
    }

    public BoundingBox getBoundingBox()
    {
        if (boundingBox == null || isDynamic())
        {
            var tr = getParent().getTransform();
            var pos = tr.getTopLeftPosition();
            var size = tr.getSizeRef();
            boundingBox = new BoundingBox(pos.x, pos.y, size.x, size.y);
        }
        return boundingBox;
    }

    public boolean contains(Vector2f pos)
    {
        var tr = getParent().getTransform();
        if (getParent() instanceof Rect)
            return getBoundingBox().isCollision(MathUtils.rotateAround(pos, tr.getPosition2D(), -tr.getAngle()));
        else if (getParent() instanceof Circle)
        {
            return tr.getDistance(pos) <= ((Circle) getParent()).getRadius();
        }
        return false;
    }

    public boolean contains(GameObject object)
    {
        if (getParent() instanceof Circle)
        {
            if(object instanceof Circle)
            {
                return getParent().getTransform().getDistance(object.getTransform().getCenterPosition()) <= ((Circle) getParent()).getRadius();
            }
        }
        return getBoundingBox().isCollision(object.collision_c().getBoundingBox());
    }
}
