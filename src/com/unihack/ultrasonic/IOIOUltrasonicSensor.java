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

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PulseInput;
import ioio.lib.api.PulseInput.PulseMode;
import ioio.lib.api.exception.ConnectionLostException;
import android.util.Log;

public class IOIOUltrasonicSensor extends AbstractIOIOService {
	
	private int echoSeconds;
	private int echoDistanceCm;
	
	private int counter = 0;
	private int shortCounter=0;

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
				shortCounter++;
				
				if(shortCounter >=10){
					if(echoDistanceCm <=30){
						new TTS().speak("Stop");
					} else if(counter >=100){
							Log.d("VisionPlus", "speak");
							new TTS().speak(""+echoDistanceCm);
							counter = 0;
					}
					shortCounter = 0;
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
