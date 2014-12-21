/*
*    This file is part of IOIOUltrasonicSensor.
*
*    IOIOUltrasonicSensor is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    IOIOUltrasonicSensor is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with IOIOUltrasonicSensor.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.unihack.ultrasonic;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

/**
 * Text to speech. </br> Converts string text to sound </br> supports multiple
 * languages, more concretely all languages which supports java.util.Locale.
 */
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
