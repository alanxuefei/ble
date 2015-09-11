package com.innoweaver.TyreTest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.innoweaver.TyreTest.entity.CarEntity;
import com.innoweaver.TyreTest.entity.RecordEntity;

public class MockActivity extends BasicActivity {
	private static final String TAG = MockActivity.class.getSimpleName();

	private EditText speedEdit;
	private int[] btnArr = { R.id.tyre_0, R.id.tyre_1, R.id.tyre_2, R.id.tyre_3 };
	private TextView[] stdTxts;
	private TextView[] testTxts;
	private RecordEntity record;
	private long valueC;

	public static String bar;

	private CarEntity car;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mock);

		initData();
		setupEdit();
		setupTags();
	}

	private void initData() {
		bar = getString(R.string.bar_unit);

		Intent data = getIntent();
		car = data.getParcelableExtra(TyreMainActivity.EXTRA_CAR_INDEX);
		record = data.getParcelableExtra(TyreMainActivity.PRESSURE_VALUE_INDEX);
		valueC = data.getLongExtra(TyreMainActivity.EXTRA_VALUE_C, 2);
		Log.e(TAG, "record: " + record);
		Log.e(TAG, "value c: " + valueC);
	}

	private void setupEdit() {
		speedEdit = (EditText) findViewById(R.id.speed_edit);
		((TyreApp) getApplicationContext()).startAnimation(speedEdit, R.anim.sparkle_r_3);
		speedEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				Log.e(TAG, "speed: " + s.toString());
				// TODO
				float speed = 0;
				try {
					speed = Float.valueOf(s.toString());
				} catch (NumberFormatException e) {
					Log.e(TAG, e.toString(), e);
				}

				setTestValues(speed);
			}
		});
		// speedEdit.postDelayed(action, delayMillis)
	}

	private void setupTags() {
		stdTxts = new TextView[btnArr.length];
		testTxts = new TextView[btnArr.length];

		for (int i = 0; i < btnArr.length; i++) {
			final int index = i;

			View v = findViewById(btnArr[index]);
			stdTxts[index] = (TextView) v.findViewById(R.id.std_txt);
			if (car != null) {
				switch (i) {
				case 0: {
				}
				case 1: {
					stdTxts[index].setText(getString(R.string.standard_tag) + " " + car.getfStdPressure() + bar);
					break;
				}
				case 2: {
				}
				case 3: {
					stdTxts[index].setText(getString(R.string.standard_tag) + " " + car.getrStdPressure() + bar);
					break;
				}
				}
			}
			testTxts[index] = (TextView) v.findViewById(R.id.test_txt);
			testTxts[index].setText(getString(R.string.mock_tag));
		}
	}

	private void setTestValues(float speed) {
		testTxts[0].setText(getString(R.string.mock_tag) + " " + getMockValue(record.getFlPressure(), speed) + bar);
		testTxts[1].setText(getString(R.string.mock_tag) + " " + getMockValue(record.getFrPressure(), speed) + bar);
		testTxts[2].setText(getString(R.string.mock_tag) + " " + getMockValue(record.getRlPressure(), speed) + bar);
		testTxts[3].setText(getString(R.string.mock_tag) + " " + getMockValue(record.getRrPressure(), speed) + bar);
	}

	private float getMockValue(float pressure, float speed) {
		float r = pressure * (1 + speed / 900);

		return r;
	}
}
