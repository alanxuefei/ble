package com.innoweaver.TyreTest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableListItem extends LinearLayout implements Checkable {

	public CheckableListItem(Context context) {
		super(context);
	}

	public CheckableListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CheckableListItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	@Override
	public void setChecked(boolean checked) {
		setSelected(checked);
	}

	@Override
	public boolean isChecked() {
		return isSelected();
	}

	@Override
	public void toggle() {

	}

	
	
}
