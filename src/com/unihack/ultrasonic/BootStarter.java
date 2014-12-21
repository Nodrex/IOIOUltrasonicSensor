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
