package com.innoweaver.TyreTest.util;

import android.content.Context;
import android.util.Log;

import com.innoweaver.TyreTest.R;

public class UnitTransferUtil {
	private static final String TAG = UnitTransferUtil.class.getSimpleName();

	public static float fromSignalToPressure(Context context, long signal, long valueB, long valueC) {
		float r = 0;
		double valueA = Double.valueOf(context.getString(R.string.value_A));
		double valueK = Double.valueOf(context.getString(R.string.value_K));
		Log.i(TAG, "A: " + valueA + ", K: " + valueK);
		Log.i(TAG, "B: " + valueB + ", single: " + signal);

		r = (float) ((signal - valueC) * valueA / valueB / valueK);

		return r;
	}

	public static float fromBarToKpa(float value) {
		return value * 100;
	}

	public static float fromBarToPsi(float value) {
		return value * 14.5f;
	}
}
