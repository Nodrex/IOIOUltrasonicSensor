package ioio.examples.ultrasonic;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

/**
 * Singleton.
 */
public class TTS implements OnInitListener{
	
	private static TextToSpeech tts;
	private TTS instance;
	 
	public TTS() {
		if(instance != null) return;
		//TODO should be send activity
		tts = new TextToSpeech(null, this);
		instance = this;
	}

	@SuppressWarnings("deprecation")
	public void speak(String text) {
		//String toSpeak = inputText.getText().toString();
		//Toast.makeText(this, toSpeak, Toast.LENGTH_SHORT).show();
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

	@SuppressLint("NewApi")
	/**
	 * Use if Android OS version is 21 or gigher.
	 */
	public void speakNew(String text) {
		//String toSpeak = inputText.getText().toString();
		//Toast.makeText(this, "New Api: " + toSpeak, Toast.LENGTH_SHORT).show();
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "Vision+");
	}

	@Override
	public void onInit(int status) {
		//Locale locale = VisionPlusActivity.getLocale();
		if (status != TextToSpeech.ERROR) tts.setLanguage(/*locale*/Locale.UK);
	}

}
