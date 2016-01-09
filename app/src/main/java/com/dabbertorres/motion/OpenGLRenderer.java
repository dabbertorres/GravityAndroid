package com.dabbertorres.motion;

import android.opengl.GLES11;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements GLSurfaceView.Renderer
{
	private final float farPlane = 10f;
	private final float nearPlane = 1f;

	private FloatBuffer vertices;
	private FloatBuffer colors;
	private float scale = 1.5f;
	private float frustumCenter = 0;

	public OpenGLRenderer()
	{
		// 2 vertices for a line, 3 components of a vertex, size of a float in bytes
		vertices = ByteBuffer.allocateDirect(2 * 3 * Float.SIZE / Byte.SIZE)
							 .order(ByteOrder.nativeOrder()).asFloatBuffer();

		vertices.put(new float[]{0f, 0f, 0f, 0f, -scale, 0f});

		// 2 vertices for a line, 4 components of a color (RGBA), size of a float in bytes
		colors = ByteBuffer.allocateDirect(2 * 4 * Float.SIZE / Byte.SIZE)
						   .order(ByteOrder.nativeOrder()).asFloatBuffer();

		// first vertex is blue, second is red
		colors.put(new float[]{0, 0, 255, 255, 255, 0, 0, 255});
	}

	public void setVector(float x, float y, float z)
	{
		float mag = (float) Math.sqrt(x * x + y * y + z * z);
		float unitX = x / mag;
		float unitY = y / mag;
		float unitZ = z / mag;

		// jump to second vertex (first is always (0, 0, 0))
		vertices.position(3);
		vertices.put(-unitX * scale);
		vertices.put(-unitY * scale);
		vertices.put(unitZ * scale);
	}

	@Override
	public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig)
	{
		GLES11.glClearColor(0f, 0f, 0f, 1f);

		GLES11.glLineWidth(5f);

		GLES11.glEnableClientState(GLES11.GL_VERTEX_ARRAY);
		GLES11.glEnableClientState(GLES11.GL_COLOR_ARRAY);
	}

	@Override
	public void onSurfaceChanged(GL10 gl10, int width, int height)
	{
		GLES11.glViewport(0, 0, width, height);

		// scale to viewport aspect ratio
		float ratio = (float) width / height;

		GLES11.glMatrixMode(GL10.GL_PROJECTION);
		GLES11.glLoadIdentity();

		GLES11.glFrustumf(-ratio, ratio, -1, 1, nearPlane, farPlane);

		// keep for drawing vector in center of viewport
		frustumCenter = -(farPlane - nearPlane) / 2f - nearPlane;
	}

	@Override
	public void onDrawFrame(GL10 gl10)
	{
		GLES11.glClear(GLES11.GL_COLOR_BUFFER_BIT | GLES11.GL_DEPTH_BUFFER_BIT);

		GLES11.glMatrixMode(GLES11.GL_MODELVIEW);
		GLES11.glLoadIdentity();

		GLES11.glTranslatef(0f, 0f, frustumCenter);

		vertices.position(0);
		colors.position(0);
		GLES11.glVertexPointer(3, GLES11.GL_FLOAT, 0, vertices);
		GLES11.glColorPointer(4, GLES11.GL_FLOAT, 0, colors);

		GLES11.glDrawArrays(GLES11.GL_LINES, 0, 2);
	}
}
