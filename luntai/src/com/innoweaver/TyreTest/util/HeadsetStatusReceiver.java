package com.innoweaver.TyreTest.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;

public class HeadsetStatusReceiver extends BroadcastReceiver {
	private static final String TAG = HeadsetStatusReceiver.class
			.getSimpleName();

	private HeadSetMonitor monitor;

	@Override
	public void onReceive(Context context, Intent intent) {
		// Log.e(TAG, "receiver");
		if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
			AudioManager am = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			if (am.isWiredHeadsetOn()) {
				// Toast.makeText(context, "Connected",
				// Toast.LENGTH_SHORT).show();
				if (monitor != null) {
					monitor.onHeadSetPluggedin();
				}
			} else {
				Toast.makeText(context, "Not Connected", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	public void setMonitor(HeadSetMonitor monitor) {
		this.monitor = monitor;
	}

}
