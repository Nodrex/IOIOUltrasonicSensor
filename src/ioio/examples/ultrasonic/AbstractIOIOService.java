package ioio.examples.ultrasonic;

import ioio.lib.api.IOIO;
import ioio.lib.api.IOIOFactory;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.api.exception.IncompatibilityException;
import ioio.lib.spi.IOIOConnectionBootstrap;
import ioio.lib.spi.IOIOConnectionFactory;
import ioio.lib.util.IOIOConnectionRegistry;
import ioio.lib.util.android.ContextWrapperDependent;

import java.util.Collection;
import java.util.LinkedList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public abstract class AbstractIOIOService extends Service{

	private static final String TAG = "AbstractIOIOActivity";
	
	static {
		IOIOConnectionRegistry
				.addBootstraps(new String[] {
						"ioio.lib.android.accessory.AccessoryConnectionBootstrap",
						"ioio.lib.android.bluetooth.BluetoothIOIOConnectionBootstrap" });
	}

	private Collection<IOIOThread> threads_ = new LinkedList<IOIOThread>();
	private Collection<IOIOConnectionBootstrap> bootstraps_ = IOIOConnectionRegistry
			.getBootstraps();
	private IOIOConnectionFactory currentConnectionFactory_;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		for (IOIOConnectionBootstrap bootstrap : bootstraps_) {
			if (bootstrap instanceof ContextWrapperDependent) {
				((ContextWrapperDependent) bootstrap).onCreate(this);
			}
		}
		
		for (IOIOConnectionBootstrap bootstrap : bootstraps_) {
			if (bootstrap instanceof ContextWrapperDependent) {
				((ContextWrapperDependent) bootstrap).open();
			}
		}
		createAllThreads();
		startAllThreads();
		
		Log.d("VisionPlus", "Service is going ...");
		return START_STICKY;
	}
	
	//TODO may by problem (((((((((
	/*@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if ((intent.getFlags() & Intent.FLAG_ACTIVITY_NEW_TASK) != 0) {
			for (IOIOConnectionBootstrap bootstrap : bootstraps_) {
				if (bootstrap instanceof ContextWrapperDependent) {
					((ContextWrapperDependent) bootstrap).open();
				}
			}
		}
	}*/
	
	protected IOIOThread createIOIOThread() {
		throw new RuntimeException(
				"Client must override on of the createIOIOThread overloads!");
	}
	
	protected IOIOThread createIOIOThread(String connectionType, Object extra) {
		return createIOIOThread();
	}
	
	protected abstract class IOIOThread extends Thread {
		/** Subclasses should use this field for controlling the IOIO. */
		protected IOIO ioio_;
		private boolean abort_ = false;
		private boolean connected_ = true;
		private final IOIOConnectionFactory connectionFactory_ = currentConnectionFactory_;

		/**
		 * Subclasses should override this method for performing operations to
		 * be done once as soon as IOIO communication is established. Typically,
		 * this will include opening pins and modules using the openXXX()
		 * methods of the {@link #ioio_} field.
		 */
		protected void setup() throws ConnectionLostException,
				InterruptedException {
		}

		/**
		 * Subclasses should override this method for performing operations to
		 * be done repetitively as long as IOIO communication persists.
		 * Typically, this will be the main logic of the application, processing
		 * inputs and producing outputs.
		 */
		protected void loop() throws ConnectionLostException,
				InterruptedException {
			sleep(100000);
		}

		/**
		 * Subclasses should override this method for performing operations to
		 * be done once as soon as IOIO communication is lost or closed.
		 * Typically, this will include GUI changes corresponding to the change.
		 * This method will only be called if setup() has been called. The
		 * {@link #ioio_} member must not be used from within this method. This
		 * method should not block for long, since it may cause an ANR.
		 */
		protected void disconnected() {
		}

		/**
		 * Subclasses should override this method for performing operations to
		 * be done if an incompatible IOIO firmware is detected. The
		 * {@link #ioio_} member must not be used from within this method. This
		 * method will only be called once, until a compatible IOIO is connected
		 * (i.e. {@link #setup()} gets called).
		 */
		protected void incompatible() {
		}

		/** Not relevant to subclasses. */
		@Override
		public final void run() {
			super.run();
			while (!abort_) {
				try {
					synchronized (this) {
						if (abort_) {
							break;
						}
						ioio_ = IOIOFactory.create(connectionFactory_
								.createConnection());
					}
				} catch (Exception e) {
					Log.e(TAG, "Failed to create IOIO, aborting IOIOThread!");
					return;
				}
				// if we got here, we have a ioio_!
				try {
					ioio_.waitForConnect();
					connected_ = true;
					setup();
					while (!abort_) {
						loop();
					}
				} catch (ConnectionLostException e) {
				} catch (InterruptedException e) {
					ioio_.disconnect();
				} catch (IncompatibilityException e) {
					Log.e(TAG, "Incompatible IOIO firmware", e);
					incompatible();
					// nothing to do - just wait until physical disconnection
				} catch (Exception e) {
					Log.e(TAG, "Unexpected exception caught", e);
					ioio_.disconnect();
					break;
				} finally {
					try {
						ioio_.waitForDisconnect();
					} catch (InterruptedException e1) {
					}
					synchronized (this) {
						ioio_ = null;
					}
					if (connected_) {
						disconnected();
						connected_ = false;
					}
				}
			}
			Log.d(TAG, "IOIOThread is exiting");
		}

		/** Not relevant to subclasses. */
		public synchronized final void abort() {
			abort_ = true;
			if (ioio_ != null) {
				ioio_.disconnect();
			}
			if (connected_) {
				interrupt();
			}
		}
	}

	private void createAllThreads() {
		threads_.clear();
		Collection<IOIOConnectionFactory> factories = IOIOConnectionRegistry
				.getConnectionFactories();
		for (IOIOConnectionFactory factory : factories) {
			currentConnectionFactory_ = factory;
			IOIOThread thread = createIOIOThread(factory.getType(),
					factory.getExtra());
			if (thread != null) {
				threads_.add(thread);
			}
		}
	}

	private void startAllThreads() {
		for (IOIOThread thread : threads_) {
			thread.start();
		}
	}
	
}
