package ua.com.hakaton;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;

public class SoundManager {

	  private static final String TAG = SoundManager.class.getSimpleName();

	  private static final float SOUND_VOLUME = 1.00f;
	  private static final long VIBRATE_DURATION = 2000L;

	  private final Activity activity;
	  private MediaPlayer mayMediaPlayer, sensMediaPlayer, purrMediaPlayer;
	  private boolean playMay;
	  private boolean canIPlay = true;
	  private boolean vibrate = true;

	  public SoundManager(Activity activity) {
	    this.activity = activity;
	    this.mayMediaPlayer = null;
	    updatePrefs();
	  }

	  public void updatePrefs() {
	    playMay = shouldPlay(activity);
	    if (playMay) {
	    	activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	    	if (mayMediaPlayer == null){
	  	      	mayMediaPlayer = buildMayMediaPlayer(activity);
	    	}
	    	if (sensMediaPlayer == null){
	    		sensMediaPlayer = buildSensMediaPlayer(activity);
	    	}
	    	if (purrMediaPlayer == null){
	    		purrMediaPlayer = buildPurrMediaPlayer(activity);
	    	}
	    }
	  }

	  public void playMaySound() {
	    if (playMay && mayMediaPlayer != null && canIPlay) {
	    	canIPlay = false;
	    	mayMediaPlayer.start();
	    }
	  }
	  
	  public void playSensSoundAndVibrate() {
	    if (playMay && sensMediaPlayer != null && canIPlay) {
	    	canIPlay = false;
	    	sensMediaPlayer.start();
	    	if (vibrate) {
	  	      Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
	  	      vibrator.vibrate(VIBRATE_DURATION);
	  	    }
	    }
	  }
	  
	  public void playPurr() {
	    if (playMay && purrMediaPlayer != null && canIPlay) {
	    	canIPlay = false;
	    	purrMediaPlayer.start();
	    }
	  }

	  private static boolean shouldPlay(Context activity) {
		boolean shouldPlay = true;
		// See if sound settings overrides this
		AudioManager audioService = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			shouldPlay = false;
		}
	    return shouldPlay;
	  }
	  
	  private MediaPlayer buildMayMediaPlayer(Context activity) {
		  AssetFileDescriptor file = activity.getResources().openRawResourceFd(R.raw.miaou);
		  MediaPlayer mediaPlayer = buildMediaPlayer(activity, file);
		  return mediaPlayer;
	  }
	  
	  private MediaPlayer buildSensMediaPlayer(Context activity) {
		  AssetFileDescriptor file = activity.getResources().openRawResourceFd(R.raw.not_like);
		  MediaPlayer mediaPlayer = buildMediaPlayer(activity, file);
		  return mediaPlayer;
	  }
	  
	  private MediaPlayer buildPurrMediaPlayer(Context activity) {
		  AssetFileDescriptor file = activity.getResources().openRawResourceFd(R.raw.purr);
		  MediaPlayer mediaPlayer = buildMediaPlayer(activity, file);
		  return mediaPlayer;
	  }

	  private MediaPlayer buildMediaPlayer(Context activity, AssetFileDescriptor file) {
	    MediaPlayer mediaPlayer = new MediaPlayer();
	    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	    // When the beep has finished playing, rewind to queue up another one.
	    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
	      public void onCompletion(MediaPlayer player) {
	        player.seekTo(0);
	        canIPlay = true;
	      }
	    });
	    
	    try {
	      mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
	      file.close();
	      mediaPlayer.setVolume(SOUND_VOLUME, SOUND_VOLUME);
	      mediaPlayer.prepare();
	    } catch (IOException ioe) {
	      Log.w(TAG, ioe);
	      mediaPlayer = null;
	    }
	    return mediaPlayer;
	  }

	}

