package com.dabbertorres.motion;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class OpenGLView extends GLSurfaceView
{
	private final OpenGLRenderer renderer;

	public OpenGLView(Context context)
	{
		this(context, null);
	}

	public OpenGLView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		setRenderer(renderer = new OpenGLRenderer());
	}

	public final OpenGLRenderer getRenderer()
	{
		return renderer;
	}
}
