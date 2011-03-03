package ua.com.hakaton;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Cats extends Activity {
	private SoundManager soundManager;
	private SensorCat sensors;
	private boolean playPurr = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        soundManager = new SoundManager(this);
        
        final ImageButton image = (ImageButton) findViewById(R.id.cats);
        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if (playPurr){
            		playPurr = false;
            	} else {
            		soundManager.playMaySound();
            	}
            }
        });
        image.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				soundManager.playPurr();
				playPurr = true;
				return false;
			}
        });
        
        sensors = new SensorCat(this, soundManager);
    }
    
    @Override
    protected void onResume() {
       super.onResume();
       sensors.resumeSensors();
    }

    @Override
    protected void onPause() {
       super.onPause();
       sensors.pauseSensors();
    }
}