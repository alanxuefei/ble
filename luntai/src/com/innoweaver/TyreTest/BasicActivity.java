package com.innoweaver.TyreTest;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class BasicActivity extends SherlockFragmentActivity {
	protected String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// onSetTitle();
		setupActionbar();
	}

	private void setupActionbar() {
		ActionBar ab = getSupportActionBar();
		ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.abs));
		if (!(this instanceof TyreMainActivity)) {
			ab.setHomeButtonEnabled(true);
			ab.setDisplayHomeAsUpEnabled(true);
			// ab.setTitle(title);
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (!(this instanceof TyreMainActivity)) {
				Intent parentActivityIntent = new Intent(this, TyreMainActivity.class);
				parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(parentActivityIntent);
				finish();
			}
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	protected void onSetTitle() {
		this.title = getString(R.string.app_name);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void finish() {
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
		super.finish();
	}

}
