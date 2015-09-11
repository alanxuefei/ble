package com.innoweaver.TyreTest.dataUtil;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.innoweaver.TyreTest.TyreApp;

public class RecordExeView<T extends Object> extends RecordImpl<T> {
	private final String TAG = RecordExeView.class.getSimpleName();

	private View mView;
	private Context context;

	public RecordExeView(Context context) {
		this.context = context;
	}

	public void setViewExecuter(View v) {
		mView = v;
	}

	@Override
	public void startNotifying() {
		Log.e(TAG, "startNotifying");
		((TyreApp) context).startAnimation(mView);
	}

	@Override
	public void stopNotifying() {
		((TyreApp) context).stopAnimation(mView);
	}

}
