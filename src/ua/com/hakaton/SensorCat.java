package ua.com.hakaton;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.app.Activity;
import android.content.Context;


public class SensorCat implements SensorEventListener {
	
	private static final String TAG = SensorCat.class.getSimpleName();
	
	private SensorManager mSensorManager;
	private final Sensor mAccelerometer;
	private final Activity activity;
	private final SoundManager soundManager;
	
	private long lastUpdate = -1;
	private float x, y, z;
	private float last_x, last_y, last_z;
	private static final int SHAKE_THRESHOLD = 800;
	
	
	public SensorCat(Activity activity, SoundManager soundManager){
		this.activity = activity;
		this.soundManager = soundManager;
		mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		//do nothing
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
	        switch (event.sensor.getType()){
	            case Sensor.TYPE_ACCELEROMETER:
	            	long curTime = System.currentTimeMillis();
	            	if ((curTime - lastUpdate) > 100) {
	            		long diffTime = (curTime - lastUpdate);
	            		lastUpdate = curTime;
	            		x = event.values[SensorManager.DATA_X];
						y = event.values[SensorManager.DATA_Y];
						z = event.values[SensorManager.DATA_Z];
						float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
						if (speed > SHAKE_THRESHOLD) {
							soundManager.playSensSoundAndVibrate();
						}
						last_x = x;
						last_y = y;
						last_z = z;
					}
	            	break;
	        }
		}
	}

}
