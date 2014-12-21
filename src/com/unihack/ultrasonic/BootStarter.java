package com.unihack.ultrasonic;

import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootStarter extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		SharedPreferences sharedPref = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
		if(sharedPref != null){
			String language = sharedPref.getString(Constants.LANGUAGE, "English");
			VisionPlusActivity.setLocale(new Locale(language));
		}
		context.startService(new Intent(context, IOIOUltrasonicSensor.class));
	}

}
