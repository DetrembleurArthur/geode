package com.geode.engine.graphics.ui.text;


import com.geode.engine.conf.Configurations;
import com.geode.engine.dispatchers.Resource;
import com.geode.engine.graphics.Mesh;
import com.geode.engine.graphics.Texture;
import com.geode.engine.graphics.prefabs.MeshFactory;
import com.geode.engine.utils.MathUtils;
import com.geode.engine.utils.VariableLoader;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Font implements Resource
{
	/*
	Normaliser: height = height / size
				width = width / size
				+ offsets
	*/
	private final String fontPath;
	private String face;
	private float sizePx;
	private boolean bold;
	private boolean italic;
	private String texFile;
	private Texture textureAtlas;
	private float[] padding;//up-right-down-left
	private float[] spacing;
	private float lineHeight;
	private float base;
	private float scaleW;
	private float scaleH;
	private HashMap<Character, Glyph> glyphs;

	public Font(String fontPath)
	{
		this.fontPath = Configurations.assetsPath + fontPath;
		glyphs = new HashMap<>();
		load();
	}

	public HashMap<Character, Glyph> getGlyphs()
	{
		return glyphs;
	}

	private void setInfoFromLoader(VariableLoader loader)
	{
		face = loader.getString("face").replace("\"", "");
		sizePx = loader.getInt("size");
		bold = loader.getBool("bold");
		italic = loader.getBool("italic");
		texFile = loader.getString("file").replace("\"", "");
		padding = loader.getFloatA("padding", 4);
		spacing = loader.getFloatA("spacing", 2);
		lineHeight = loader.getFloat("lineHeight");
		base = loader.getFloat("base");
		scaleW = loader.getFloat("scaleW");
		scaleH = loader.getFloat("scaleH");
		textureAtlas = new Texture("/"+texFile);
	}

	private void load()
	{
		try (BufferedReader br = new BufferedReader(new FileReader(fontPath)))
		{
			VariableLoader loader = new VariableLoader();
			String buf;
			while((buf = br.readLine()) != null && !buf.startsWith("chars"))
			{
				loader.load(buf);
			}
			setInfoFromLoader(loader);
			while((buf = br.readLine()) != null)
			{
				if(buf.startsWith("char"))
				{
					loader.clean();
					loader.load(buf);
					glyphs.put((char)loader.getInt("id"), new Glyph(loader, textureAtlas));
				}
			}
			loader.clean();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void destroy()
	{
		textureAtlas.destroy();
	}

	static class TextInfo
	{
		Mesh mesh;
		float heightRatio = 0f;
	}

	TextInfo generateMesh(String text)
	{
		text = text.replace("\t", "    ");
		var len = text.length();
		float[] positionsX = new float[len * 4];
		float[] positionsY = new float[len * 4];
		float[] uvs = new float[len * 4 * 2];
		int[] indices = new int[len * 6];
		Vector2i counters = new Vector2i(0, 0);
		Vector2f cursorPos = new Vector2f(0);
		Vector2f pos = new Vector2f();
		Vector2f maxPos = new Vector2f();
		Vector2f[] gluvs;
		int nlPadding = 0; //permet d'ajuster les indexs par rapport au nombre de lignes

		ArrayList<Glyph> mem = new ArrayList<>();
		float maxWidth = 0;
		float maxHeight = 0;

		for(int i = 0; i < len; i++)
		{
			if(text.charAt(i) == '\n')
			{
				cursorPos.y += lineHeight;
				cursorPos.x = 0;
				nlPadding++;
				float w=0, h=0;
				for(var g : mem)
				{
					w += g.getTexWidth();
					h = Math.max(g.getTexHeight() + maxHeight, h);
				}
				maxWidth = Math.max(w, maxWidth);
				maxHeight = Math.max(h, maxHeight);
				mem.clear();
				continue;
			}
			Glyph glyph = glyphs.get(text.charAt(i));
			mem.add(glyph);
			if(glyph == null) continue;
			gluvs = glyph.getUVs();
			pos.x = cursorPos.x + glyph.getXoffset();
			pos.y = cursorPos.y + glyph.getYoffset();
			maxPos.x = pos.x + glyph.getTexWidth();
			maxPos.y = pos.y + glyph.getTexHeight();

			addVertex(counters, positionsX, positionsY, uvs, pos, gluvs[0]); //top left
			addVertex(counters, positionsX, positionsY, uvs, new Vector2f(pos.x, maxPos.y), gluvs[1]); //bottom left
			addVertex(counters, positionsX, positionsY, uvs, maxPos, gluvs[2]); //bottom right
			addVertex(counters, positionsX, positionsY, uvs, new Vector2f(maxPos.x, pos.y), gluvs[3]); //top right

			addIndex(counters, indices, i - nlPadding);

			cursorPos.x += glyph.getXadvance();
		}
		float w=0, h=0;
		for(var g : mem)
		{
			w += g.getTexWidth();
			h = Math.max(g.getTexHeight() + maxHeight, h);
		}
		maxWidth = Math.max(w, maxWidth);
		maxHeight = Math.max(h, maxHeight);

		if(positionsX.length > 0)
		{
			MathUtils.normalize(positionsX);
			MathUtils.normalize(positionsY);
		}
		float[] positions = new float[positionsX.length + positionsY.length];
		for(int i = 0; i < positions.length / 2; i++)
		{
			positions[i * 2] = positionsX[i];
			positions[i * 2 + 1] = positionsY[i];
		}
		var info = new TextInfo();
		info.mesh = MeshFactory.generic(positions, uvs, indices, true);
		info.heightRatio = maxHeight / maxWidth;
		return info;
	}

	private void addVertex(Vector2i counters, float[] positionsX, float[] positionsY, float[] uvs, Vector2f pos, Vector2f uv)
	{
		int i = counters.x;
		counters.x++;
		positionsX[i] = pos.x;
		positionsY[i] = pos.y;
		uvs[i*2] = uv.x;
		uvs[i*2 + 1] = uv.y;
	}

	private void addIndex(Vector2i counters, int[] indices, int i)
	{
		indices[counters.y++] = i * 4;
		indices[counters.y++] = i * 4 + 1;
		indices[counters.y++] = i * 4 + 2;
		indices[counters.y++] = i * 4;
		indices[counters.y++] = i * 4 + 2;
		indices[counters.y++] = i * 4 + 3;
	}

	public String getFontPath()
	{
		return fontPath;
	}

	public String getFace()
	{
		return face;
	}

	public float getSizePx()
	{
		return sizePx;
	}

	public boolean isBold()
	{
		return bold;
	}

	public boolean isItalic()
	{
		return italic;
	}

	public float[] getPadding()
	{
		return padding;
	}

	public float[] getSpacing()
	{
		return spacing;
	}

	public float getLineHeight()
	{
		return lineHeight;
	}

	public float getBase()
	{
		return base;
	}

	public float getScaleW()
	{
		return scaleW;
	}

	public float getScaleH()
	{
		return scaleH;
	}

	public String getTexFile()
	{
		return texFile;
	}

	public Texture getTextureAtlas()
	{
		return textureAtlas;
	}

	public float getSizePt()
	{
		return sizePx / 1.333333f;
	}

	public int fontType()
	{
		if(italic && bold) return 3;
		if(italic) return 1;
		if(bold) return 2;
		return 0;
	}
}
