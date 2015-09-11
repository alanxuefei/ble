package com.innoweaver.TyreTest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.innoweaver.TyreTest.R;

public class MeterView extends RelativeLayout {
	private final String TAG = MeterView.class.getSimpleName();

	private boolean inAnimation = false;

	private float minDegree = 0.0f;
	private float maxDegree = 45.0f * 7;

	private float currentDegree = minDegree;
	private float stdPressure = 0.0f;
	private boolean stdSet = false;

	ImageView needle, nonius;

	public MeterView(Context context) {
		super(context);
	}

	public MeterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MeterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private void init() {
		needle = (ImageView) findViewById(R.id.needle);
		nonius = (ImageView) findViewById(R.id.nonius);
		// needle = getChildAt(0);
		Log.e(TAG, "needle is null? " + (needle == null));

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		init();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		init();
	}

	public void setMeter(float value) {
		final float degree = value * 45.0f;

		Log.e(TAG, "setMeter, degree=" + degree);
		if (needle != null) {
			Animation anim = new RotateAnimation(currentDegree, degree,
					needle.getMeasuredWidth() / 2,
					needle.getMeasuredHeight() / 2);
			// Animation anim = new RotateAnimation(currentDegree, degree,
			// RotateAnimation.RELATIVE_TO_SELF,
			// RotateAnimation.RELATIVE_TO_SELF,
			// needle.getMeasuredWidth() / 2,
			// needle.getMeasuredHeight() / 2);

			anim.setDuration(500);
			anim.setFillAfter(true);
			anim.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					inAnimation = true;
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					inAnimation = false;
					currentDegree = degree;
				}
			});
			needle.startAnimation(anim);
		}
	}

	public void setStdPressure(float stdPressure) {
		this.stdPressure = stdPressure;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {

		// draw the needle
		// if (!inAnimation) {
		// Matrix m = needle.getImageMatrix();
		// Log.e(TAG, "current degree: " + currentDegree);
		// // m.setRotate(currentDegree);
		// // m.preTranslate(-needle.getMeasuredWidth() / 2,
		// // -needle.getMeasuredHeight() / 2);
		// // m.postTranslate(needle.getMeasuredWidth() / 2,
		// // needle.getMeasuredHeight() / 2);
		// m.postRotate(currentDegree, getMeasuredWidth() / 2,
		// getMeasuredHeight() / 2);
		// }

		// draw the nonius
		if (!stdSet) {
			Matrix nm = nonius.getImageMatrix();
			Log.e(TAG, "stdPressure: " + stdPressure);
			nm.postRotate(stdPressure * 45.0f, getMeasuredWidth() / 2,
					getMeasuredHeight() / 2);
			stdSet = true;
		}
		// nm.postRotate(0, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
		// nm.preTranslate(-nonius.getMeasuredWidth() / 2,
		// -nonius.getMeasuredHeight() / 2);
		// nm.postTranslate(nonius.getMeasuredWidth() / 2,
		// nonius.getMeasuredHeight() / 2);
		// nm.preTranslate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
		// nm.postTranslate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);

		super.dispatchDraw(canvas);
	}
}
