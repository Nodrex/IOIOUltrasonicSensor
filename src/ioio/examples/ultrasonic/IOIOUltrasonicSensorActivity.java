package ioio.examples.ultrasonic;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PulseInput;
import ioio.lib.api.PulseInput.PulseMode;
import ioio.lib.api.exception.ConnectionLostException;
import android.util.Log;

public class IOIOUltrasonicSensorActivity extends AbstractIOIOService {
	
	private int echoSeconds;
	private int echoDistanceCm;

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
				//Log.d("VisionPlus ", "loop");
				// read HC-SR04 ultrasonic sensor
				triggerPin_.write(false);
				sleep(5);
				triggerPin_.write(true);
				sleep(1);
				triggerPin_.write(false);
				echoSeconds = (int) (echoPin_.getDuration() * 1000 * 1000);
				echoDistanceCm = echoSeconds / 29 / 2;
				
				sleep(200);
				Log.d("VisionPlus", echoDistanceCm + "");
				//new TTS().speak(""+echoDistanceCm);
				
				/* update UI */
				//updateViews();

				sleep(20);
			} catch (InterruptedException e) {
				ioio_.disconnect();

			} catch (ConnectionLostException e) {
				throw e;

			}
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
