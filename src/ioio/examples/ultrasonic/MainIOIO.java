package ioio.examples.ultrasonic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainIOIO extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		startService(new Intent(this, IOIOUltrasonicSensorActivity.class));
	}
	 
}
