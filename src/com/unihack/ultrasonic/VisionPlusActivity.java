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

import ioio.examples.ultrasonic.R;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class VisionPlusActivity extends Activity implements OnItemSelectedListener {

	private static VisionPlusActivity instance;
	private static Locale locale = Locale.ENGLISH;

	public static void setLocale(Locale locale) {
		VisionPlusActivity.locale = locale;
	}

	public static Locale getLocale() {
		return locale;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		instance = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vision_plus_activity);

		Spinner spinner = (Spinner) findViewById(R.id.languageSpinner);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.language_array, R.layout.spinner_item_view);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
		SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		String item = (String) parent.getItemAtPosition(pos);
		switch (item) {
		case "English":
			locale = Locale.ENGLISH;
			editor.putString(Constants.LANGUAGE, "en");
			break;
		case "French":
			locale = Locale.FRENCH;
			editor.putString(Constants.LANGUAGE, "fr");
			break;
		case "German":
			locale = Locale.GERMAN;
			editor.putString(Constants.LANGUAGE, "de");
			break;
		case "Italian":
			locale = Locale.ITALIAN;
			editor.putString(Constants.LANGUAGE, "it");
			break;
		case "Japanese":
			locale = Locale.JAPANESE;
			editor.putString(Constants.LANGUAGE, "ja");
			break;
		case "Korean":
			locale = Locale.KOREAN;
			editor.putString(Constants.LANGUAGE, "ko");
			break;
		case "Chinese":
			locale = Locale.CHINESE;
			editor.putString(Constants.LANGUAGE, "zh");
			break;
		default:
			return;
		}
		editor.commit();
		findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
		startService(new Intent(this, IOIOUltrasonicSensor.class));
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}

	/**
	 * Finish Activity.</br> We do not need design , because this application is
	 * oriented on service </br> so this method is used to finish Activity.
	 */
	public static void finishActivity() {
		if (instance == null) ;
		instance.finish();
	}
}
