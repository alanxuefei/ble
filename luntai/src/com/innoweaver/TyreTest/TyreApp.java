package com.innoweaver.TyreTest;

import android.app.Application;
import android.content.Context;
import android.view.View;

import com.innoweaver.TyreTest.audioUtil.AudioTestModel;
import com.innoweaver.TyreTest.util.AnimationUtil;

public class TyreApp extends Application {
	private AnimationUtil animUtil;

	private AudioTestModel mModel;

	// private

	@Override
	public void onCreate() {
		super.onCreate();
		animUtil = new AnimationUtil(this);
	}

	public void startAnimation(View v) {
		animUtil.startAnimation(v);
	}

	public void stopAnimation(View v) {
		animUtil.stopAnimation(v);
	}

	public void startAnimation(View v, int animRec) {
		animUtil.startAnimation(v, animRec);
	}

	public void startTest(Context context) {
		mModel = new AudioTestModel(context);
		mModel.start();
	}

	public void pauseTest() {
		if (mModel != null) {
			mModel.pause();
		}
	}

	public void resumeTest() {
		if (mModel != null) {
			mModel.resume();
		}
	}

	public void stopTest() {
		if (mModel != null) {
			mModel.turnOff();
		}
	}

	public long getTestValue() {
		return mModel.getTestValueByLong();
	}

}
