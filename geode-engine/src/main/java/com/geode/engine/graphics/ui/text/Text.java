package com.geode.engine.graphics.ui.text;


import com.geode.engine.entity.SingleObject;
import com.geode.engine.entity.components.event.Valuable;

public class Text extends SingleObject implements Valuable
{
    private Font font;
    private String text;
    private float ratio;

    public Text(Font font, String text)
    {
        super(font.getTextureAtlas());
        initFont(font);
        setText(text);
    }

    public Font getFont()
    {
        return font;
    }

    private void initFont(Font font)
    {
        this.font = font;
        setTexture(font.getTextureAtlas());
    }

    public void setFont(Font font)
    {
        initFont(font);
        setText();
    }

    public String getText()
    {
        return text;
    }

    public void setText()
    {
        setText(text);
    }

    public void setText(String text)
    {
        this.text = text.replace("\t", "    ");
        generate();
    }

    private void generate()
    {
        var info = font.generateMesh(text);
        ratio = info.heightRatio;
        setMesh(info.mesh);
        setTextHeight(getTransform().getHeight());
    }

    public void setTextWidth(float width)
    {
        getTransform().setSize2D(width, width * ratio);
    }

    public void setTextHeight(float height)
    {
        getTransform().setSize2D(height / ratio, height);
    }


    public void normalize()
    {
        setTextHeight(getTransform().getHeight());
    }


    @Override
    public Object getValue()
    {
        return getText();
    }

    @Override
    public void setValue(Object value)
    {
        setText((String) value);
    }
}
