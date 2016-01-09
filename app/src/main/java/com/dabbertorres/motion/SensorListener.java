package com.dabbertorres.motion;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.HashSet;
import java.util.Set;

public class SensorListener implements SensorEventListener
{
	public interface SensorNotifier
	{
		void changed(SensorEvent event);

		void status(SensorStatus status);
	}

	public enum SensorStatus
	{
		AccuracyHigh,
		AccuracyMedium,
		AccuracyLow,
		NoContact,
		Unreliable,
	}

	private final Sensor sensor;

	private Set<SensorNotifier> notifiers;

	public SensorListener(int sensorType, SensorManager sensorManager)
	{
		sensor = sensorManager.getDefaultSensor(sensorType);

		notifiers = new HashSet<>();
	}

	public final Sensor getSensor()
	{
		return sensor;
	}

	public int getSensorType()
	{
		return sensor.getType();
	}

	public void addNotifier(SensorNotifier runnable)
	{
		notifiers.add(runnable);
	}

	public void removeNotifier(SensorNotifier runnable)
	{
		notifiers.remove(runnable);
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent)
	{
		for(SensorNotifier notifier : notifiers)
		{
			notifier.changed(sensorEvent);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int statusVal)
	{
		SensorStatus sts = intStatusToEnumStatus(statusVal);

		for(SensorNotifier notifier : notifiers)
		{
			notifier.status(sts);
		}
	}

	private static SensorStatus intStatusToEnumStatus(int status)
	{
		switch(status)
		{
			case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
				return SensorStatus.AccuracyHigh;

			case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
				return SensorStatus.AccuracyMedium;

			case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
				return SensorStatus.AccuracyLow;

			case SensorManager.SENSOR_STATUS_NO_CONTACT:
				return SensorStatus.NoContact;

			case SensorManager.SENSOR_STATUS_UNRELIABLE:
				return SensorStatus.Unreliable;

			default:
				return null;
		}
	}

	private static int enumStatusToIntStatus(SensorStatus status)
	{
		switch(status)
		{
			case AccuracyHigh:
				return SensorManager.SENSOR_STATUS_ACCURACY_HIGH;

			case AccuracyMedium:
				return SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM;

			case AccuracyLow:
				return SensorManager.SENSOR_STATUS_ACCURACY_LOW;

			case NoContact:
				return SensorManager.SENSOR_STATUS_NO_CONTACT;

			case Unreliable:
				return SensorManager.SENSOR_STATUS_UNRELIABLE;

			default:
				return -1;
		}
	}
}
