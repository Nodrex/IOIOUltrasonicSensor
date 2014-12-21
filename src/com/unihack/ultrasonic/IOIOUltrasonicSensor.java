package com.unihack.ultrasonic;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PulseInput;
import ioio.lib.api.PulseInput.PulseMode;
import ioio.lib.api.exception.ConnectionLostException;
import android.util.Log;

public class IOIOUltrasonicSensor extends AbstractIOIOService {
	
	private int echoSeconds;
	private int echoDistanceCm;
	
	private int counter = 0;

	/**
	 * Primary thread...runs until interrupted
	 */
	class IOIOThread extends AbstractIOIOService.IOIOThread {
		/**
		 * define pin
		 */
		/* ultrasonic sensor */
		private DigitalOutput triggerPin_;
		private PulseInput echoPin_;

		/**
		 * Called every time a connection with IOIO has been established. (opens
		 * pins)
		 * 
		 * @throws ConnectionLostException
		 *             (if IOIO connection is lost)
		 */

		public void setup() throws ConnectionLostException {
			try {
				/* ultrasonic sensor */
				echoPin_ = ioio_.openPulseInput(6, PulseMode.POSITIVE);
				triggerPin_ = ioio_.openDigitalOutput(7);
			} catch (ConnectionLostException e) {
				throw e;
			}
		}

		/**
		 * Loop section
		 */

		public void loop() throws ConnectionLostException {
			try {
				
				// read HC-SR04 ultrasonic sensor
				triggerPin_.write(false);
				sleep(5);
				triggerPin_.write(true);
				sleep(1);
				triggerPin_.write(false);
				echoSeconds = (int) (echoPin_.getDuration() * 1000 * 1000);
				echoDistanceCm = echoSeconds / 29 / 2;
				
				Log.d("VisionPlus", echoDistanceCm + "");
				
				counter++;
				if(echoDistanceCm <=30){
					new TTS().speak("Stop");
				} else if(counter >=100){
						Log.d("VisionPlus", "speak");
						new TTS().speak(""+echoDistanceCm);
						counter = 0;
				}
				
				sleep(20);
			} catch (InterruptedException e) {
				ioio_.disconnect();

			} catch (ConnectionLostException e) {
				throw e;
			}
			
			VisionPlusActivity.finishActivity();
			
		}
	}

	/**
	 * A method to create our IOIO thread.
	 */

	@Override
	protected AbstractIOIOService.IOIOThread createIOIOThread() {
		return new IOIOThread();
	}
	
}
