package ua.com.hakaton;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       super.onCreateOptionsMenu(menu);
       MenuInflater inflater = getMenuInflater();
       inflater.inflate(R.menu.menu, menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
       	case R.id.howto_button:
          startActivity(new Intent(this, Howto.class));
          return true;
       	case R.id.about_button:
       	 startActivity(new Intent(this, About.class));
       	return true;
       	default:
 	     return super.onOptionsItemSelected(item);
       }
    }
}