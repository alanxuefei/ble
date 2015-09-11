package com.innoweaver.TyreTest.util;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.innoweaver.TyreTest.R;

public class AnimationUtil {
	private Context context;
	private Animation anim;

	public AnimationUtil(Context context) {
		if (context == null) {
			throw new RuntimeException("context can't be null");
		} else {
			this.context = context;
			anim = AnimationUtils.loadAnimation(context, R.anim.sparkle);
		}
	}

	public void startAnimation(View v) {
		v.setSelected(true);
		v.startAnimation(anim);
	}

	public void stopAnimation(View v) {
		v.setSelected(false);
		v.clearAnimation();
	}

	public void startAnimation(View v, int animRec) {
		Animation anim = AnimationUtils.loadAnimation(context, animRec);
		v.startAnimation(anim);
	}

}
