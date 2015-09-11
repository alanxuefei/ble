package com.innoweaver.TyreTest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TemperatureActivity extends BasicActivity {
	String[] dataToShow, data;

	@Override
	protected void onSetTitle() {
		title = getString(R.string.temperature_title);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// data = new ArrayList<String>();
		// for (int i = 0; i < 30; i++) {
		// char v = (char) i;
		// data.add(String.valueOf(v));
		// }
		initializeData();

		setContentView(R.layout.temperature_list);

		setupList();
		// setupBtns();
	}

	private void setupList() {
		ListView list = (ListView) findViewById(R.id.temperature_list);
		// list.setItemsCanFocus(true);
		list.setAdapter(new ArrayAdapter<String>(this,
				R.layout.temperature_list_item, R.id.name_txt, dataToShow));

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				double temperature = Double.valueOf(data[position]);
				Intent data = getIntent();
				data.putExtra(TyreMainActivity.EXTRA_TEMPERATURE_VALUE_INDEX,
						temperature);
				setResult(RESULT_OK, getIntent());
				Toast.makeText(TemperatureActivity.this, "选择了"+(dataToShow[position]), Toast.LENGTH_LONG).show();
				finish();
			}
		});

	}

	// private void setupBtns() {
	// Button confirmBtn = (Button) findViewById(R.id.confirm_btn);
	// Button backButton = (Button) findViewById(R.id.back_btn);
	//
	// confirmBtn.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	//
	// finish();
	// }
	// });
	//
	// backButton.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// finish();
	// }
	// });
	// }

	private void initializeData() {
		dataToShow = getResources().getStringArray(R.array.temperatures);
		data = getResources().getStringArray(R.array.temperatures_value);
	}

}
