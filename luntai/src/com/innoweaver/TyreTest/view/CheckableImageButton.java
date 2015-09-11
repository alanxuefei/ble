package com.innoweaver.TyreTest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageButton;

public class CheckableImageButton extends ImageButton implements Checkable {
	private boolean isChecked;

	public CheckableImageButton(Context context) {
		super(context, null);
	}

	public CheckableImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CheckableImageButton(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setChecked(boolean checked) {
		isChecked = checked;
	}

	@Override
	public boolean isChecked() {
		return isChecked;
	}

	@Override
	public void toggle() {

	}

}
