package com.unihack.ultrasonic;

import ioio.examples.ultrasonic.R;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class VisionPlusActivity extends Activity implements OnItemSelectedListener {

	private static VisionPlusActivity instance;
	private static Locale locale;

	public static Locale getLocale() {
		return locale;
	}

	public static VisionPlusActivity getInstance() {
		return instance;
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
		String item = (String) parent.getItemAtPosition(pos);
		/*
		 * switch (item) { case "Canada": locale = Locale.CANADA; break; case
		 * "China": locale = Locale.CHINA; break; case "English": locale =
		 * Locale.ENGLISH; break; case "French": locale = Locale.FRENCH; break;
		 * case "German": locale = Locale.GERMAN; break; case "Italian": locale
		 * = Locale.ITALIAN; break; case "Japan": locale = Locale.JAPAN; break;
		 * case "Korea": locale = Locale.KOREA; break; case "Taiwan": locale =
		 * Locale.TAIWAN; break; default: return; }
		 */
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
