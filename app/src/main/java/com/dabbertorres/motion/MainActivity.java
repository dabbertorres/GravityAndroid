package com.dabbertorres.motion;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
{
	private OpenGLView openGLView;
	private OpenGLRenderer openGLRenderer;

	private SensorManager sensorManager;
	private SensorListener gravitySensorListener;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		openGLView = (OpenGLView) findViewById(R.id.openGLView);
		openGLRenderer = openGLView.getRenderer();

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		gravitySensorListener = new SensorListener(Sensor.TYPE_GRAVITY, sensorManager);
		gravitySensorListener.addNotifier(gravityNotifier);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		openGLView.onResume();

		sensorManager.registerListener(gravitySensorListener, gravitySensorListener.getSensor(),
									   SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		openGLView.onPause();

		// saves battery and cpu to not listen when app is not active (otherwise, continues to poll and receive events)
		sensorManager.unregisterListener(gravitySensorListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
	}

	private SensorListener.SensorNotifier gravityNotifier = new SensorListener.SensorNotifier()
	{
		@Override
		public void changed(final SensorEvent event)
		{
			openGLView.queueEvent(new Runnable()
			{
				@Override
				public void run()
				{
					openGLRenderer.setVector(event.values[0], event.values[1], event.values[2]);
				}
			});
		}

		@Override
		public void status(SensorListener.SensorStatus status)
		{
			Log.d("Motion SensorStatus:", status.name());
		}
	};
}
