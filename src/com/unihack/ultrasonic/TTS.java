package com.unihack.ultrasonic;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

public class TTS implements OnInitListener{
	
	private static TextToSpeech tts;
	 
	public TTS() {
		tts = new TextToSpeech(AbstractIOIOService.getInstance(), this);
	}

	@SuppressWarnings("deprecation")
	public void speak(String text) {
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

	@SuppressLint("NewApi")
	/**
	 * Use if Android OS version is 21 or higher.
	 */
	public void speakNew(String text) {
		if(tts.isSpeaking()) return;
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "Vision+");
		tts.shutdown();
	}

	@Override
	public void onInit(int status) {
		Locale locale = VisionPlusActivity.getLocale();
		Log.d("VisionPlus", locale.getLanguage());
		if (status != TextToSpeech.ERROR) tts.setLanguage(locale);
	}

}
