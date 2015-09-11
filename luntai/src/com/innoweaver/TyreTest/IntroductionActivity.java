package com.innoweaver.TyreTest;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.innoweaver.TyreTest.view.GuidanceFragment;
import com.innoweaver.TyreTest.view.PressureIntroductionFragment;

public class IntroductionActivity extends BasicActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.introduction);

		setupBtns();

	}

	private void setupBtns() {
		Button indroductionBtn = (Button) findViewById(R.id.pressure_btn);
		indroductionBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment f = new PressureIntroductionFragment();
				f.show(getSupportFragmentManager(), "pressure");
			}
		});
		
		Button manualBtn = (Button) findViewById(R.id.guidance_btn);
		manualBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment f = new GuidanceFragment();
				f.show(getSupportFragmentManager(), "guidance");

				// Dialog dialog = f.getDialog();
				// WindowManager.LayoutParams lp = new
				// WindowManager.LayoutParams();
				// lp.copyFrom(dialog.getWindow().getAttributes());
				// lp.width = 500;
				// lp.height = 1000;
				// dialog.getWindow().setAttributes(lp);

			}
		});

		// Button updateBtn = (Button) findViewById(R.id.soft_btn);
		//
		// updateBtn.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// DialogFragment f = new UpdateDialogFragment();
		//
		// f.show(getSupportFragmentManager(), "dialog");
		// }
		// });

	}

}
