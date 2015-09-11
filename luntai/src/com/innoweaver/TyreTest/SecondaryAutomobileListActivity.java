package com.innoweaver.TyreTest;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.innoweaver.TyreTest.entity.BrandEntity;
import com.innoweaver.TyreTest.entity.CarEntity;
import com.innoweaver.TyreTest.service.CarService;
import com.innoweaver.TyreTest.util.PreferenceUtil;

public class SecondaryAutomobileListActivity extends BasicActivity implements
		LoaderManager.LoaderCallbacks<List<CarEntity>> {
	private final static String TAG = SecondaryAutomobileListActivity.class
			.getSimpleName();

	private final static int INVALID_SELECTION = -1;

	private BrandEntity brand;

	// List<String> data;
	private ListView samList;
	private SparseBooleanArray booleanArr;
	private SAMListAdapter mAdapter;
	private CarEntity car;

	private ActionMode mActionMode;
	private boolean inActionMode = false;
	private int checkedItem = INVALID_SELECTION;

	@Override
	protected void onSetTitle() {
		title = getString(R.string.car_title);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// data = new ArrayList<String>();
		// for (int i = 0; i < 30; i++) {
		// char v = (char) i;
		// data.add(String.valueOf(v));
		// }
		brand = getIntent().getParcelableExtra("brand");
		car = getIntent().getParcelableExtra(TyreMainActivity.EXTRA_CAR_INDEX);

		setContentView(R.layout.secondary_automobile_list);

		setupList();

		if (brand != null) {
			// Log.e(TAG, "load");
			getSupportLoaderManager().initLoader(0, null, this);
		}
		// setupBtns();
	}

	private void setupList() {
		booleanArr = new SparseBooleanArray();

		samList = (ListView) findViewById(R.id.automobile_list);
		samList.setAdapter(mAdapter = new SAMListAdapter(this));

		samList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Log.e(TAG, "result ok");

				car = mAdapter.getItem(samList.getCheckedItemPosition());
				Intent intent = getIntent();
				intent.putExtra(TyreMainActivity.EXTRA_CAR_INDEX, car);
				setResult(RESULT_OK, intent);
				Toast.makeText(
						SecondaryAutomobileListActivity.this,
						getString(R.string.your_selection)
								+ car.getBrand().getName() + " "
								+ car.getStyle(), Toast.LENGTH_LONG).show();
				finish();
			}
		});

	}

	private void addFavoriteCar(CarEntity car) {
		if (car != null) {
			new PreferenceUtil(this).saveFavoiteCar(car);
			Toast.makeText(this, "收藏成功", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "����ѡ����", Toast.LENGTH_LONG).show();
		}
	}

	// private void setupBtns() {
	// Button confirmBtn = (Button) findViewById(R.id.confirm_btn);
	// Button defaultButton = (Button) findViewById(R.id.default_btn);
	// Button backButton = (Button) findViewById(R.id.back_btn);
	//
	// confirmBtn.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	//
	// finish();
	// }
	// });
	// defaultButton.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	//
	// }
	// });
	// backButton.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// finish();
	// }
	// });
	// }// end of setupBtn();

	@Override
	public Loader<List<CarEntity>> onCreateLoader(int id, Bundle args) {
		CarsLoader carLoader = new CarsLoader(this);
		carLoader.setBrand(brand);
		return carLoader;
	}

	@Override
	public void onLoadFinished(Loader<List<CarEntity>> loader,
			List<CarEntity> data) {
		//TODO 这里最好是异步执行
		loadFavoriteCar(data);

		mAdapter.setData(data);
		if (car != null) {
			int carId = car.getId();
			int position = -1;
			for (int i = 0; i < data.size(); i++) {
				if (carId == data.get(i).getId()) {
					position = i;
				}
			}
			if (position != -1) {
				samList.setItemChecked(position, true);
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<List<CarEntity>> loader) {
		mAdapter.setData(null);
	}

	private void loadFavoriteCar(List<CarEntity> cars) {
		try {
			PreferenceUtil pu = new PreferenceUtil(this);
			if(car!=null){
			CarEntity car = pu.loadFravoriteCar();
			for (int i = 0; i < cars.size(); i++) {
				if (car.getId() == cars.get(i).getId()) {
					checkedItem = i;
					break;
				}
			}
			}
		} catch (SQLException e) {
			Log.e(TAG, e.toString());
		}
	}

	private static class CarsLoader extends AsyncTaskLoader<List<CarEntity>> {
		List<CarEntity> mData;
		BrandEntity brand;

		public CarsLoader(Context context) {
			super(context);
		}

		@Override
		public List<CarEntity> loadInBackground() {
			try {
				CarService service = new CarService(getContext());
				List<CarEntity> cars = service.findCarsByBrand(brand);
				// Log.e("carsLoader", "cars: " + cars.toString());

				return cars;
			} catch (SQLException e) {
				Log.e(TAG, e.toString());
			}
			return null;
		}

		@Override
		protected void onStartLoading() {
			// super.onStartLoading();
			if (mData != null) {
				deliverResult(mData);
			} else {
				forceLoad();
			}
		}

		public void setBrand(BrandEntity brand) {
			this.brand = brand;
		}

		// @Override
		// public void deliverResult(List<BrandEntity> data) {
		// super.deliverResult(data);
		// }
	}

	private class SAMListAdapter extends ArrayAdapter<CarEntity> {

		public SAMListAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_2);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				// Log.(TAG,"convertView is NULL");
				convertView = getLayoutInflater().inflate(
						R.layout.secondary_automobile_list_item, parent, false);
				// ImageView logoImg = (ImageView) convertView
				// .findViewById(R.id.logo_img);
				TextView nameTxt = (TextView) convertView
						.findViewById(R.id.name_txt);
				CheckBox collectionBtn = (CheckBox) convertView
						.findViewById(R.id.collection_item);
				collectionListener listener = new collectionListener();
				collectionBtn.setOnClickListener(listener);
				listener.position = position;

				ListItemHolder mHolder = new ListItemHolder();
				// mHolder.logoImg = logoImg;
				mHolder.nameTxt = nameTxt;
				mHolder.collectionBtn = collectionBtn;
				mHolder.listener = listener;

				convertView.setTag(mHolder);
			}

			CarEntity car = getItem(position);
			// Log.e(TAG, "brand==null? " + (brand == null));
			ListItemHolder mHolder = (ListItemHolder) convertView.getTag();
			mHolder.listener.position = position;
			mHolder.nameTxt.setText(car.getStyle());
			mHolder.collectionBtn.setChecked(position == checkedItem ? true
					: false);
			// mHolder.collectionBtn.setChecked(booleanArr.get(position,
			// false));
			return convertView;
		}

		public void setData(List<CarEntity> data) {
			clear();
			if (data != null) {
				// addAll(data);
				for (int i = 0; i < data.size(); i++) {
					add(data.get(i));
				}
				Log.e(TAG, "changed");
				notifyDataSetChanged();
			}
		}

	}

	private class ListItemHolder {
		TextView nameTxt;
		CheckBox collectionBtn;
		collectionListener listener;
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getSupportMenuInflater().inflate(R.menu.select_car_menu, menu);
	// return true;
	// }
	//
	// @Override
	// public boolean onMenuItemSelected(int featureId, MenuItem item) {
	// switch (item.getItemId()) {
	// case R.id.default_select:
	// if (car != null) {
	// new PreferenceUtil(this).saveFavoiteCar(car);
	// Toast.makeText(this, "收藏成功", Toast.LENGTH_LONG).show();
	// } else {
	// Toast.makeText(this, "����ѡ����", Toast.LENGTH_LONG).show();
	// }
	//
	// return true;
	// }
	// return super.onMenuItemSelected(featureId, item);
	// }

	private class collectionListener implements OnClickListener {
		private int position;

		@Override
		public void onClick(View v) {

			// booleanArr.clear();
			// booleanArr.put(position, true);
			checkedItem = position;

			// if (!inActionMode) {
			// Log.e(TAG, "actionmode");
			// startActionMode(mActionModeCallback);
			// }
			if (checkedItem != INVALID_SELECTION) {
				addFavoriteCar(mAdapter.getItem(checkedItem));
			}
			mAdapter.notifyDataSetChanged();
		}
	}

	// private ActionMode.Callback mActionModeCallback = new
	// ActionMode.Callback() {
	//
	// @Override
	// public boolean onPrepareActionMode(ActionMode arg0, Menu menu) {
	// inActionMode = true;
	// return false;
	// }
	//
	// @Override
	// public void onDestroyActionMode(ActionMode arg0) {
	// inActionMode = false;
	// checkedItem = INVALID_SELECTION;
	// mAdapter.notifyDataSetChanged();
	// }
	//
	// @Override
	// public boolean onCreateActionMode(ActionMode arg0, Menu menu) {
	// getSupportMenuInflater().inflate(R.menu.select_car_menu, menu);
	// return true;
	// }
	//
	// @Override
	// public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	// switch (item.getItemId()) {
	// case R.id.default_select:
	// // if (car != null) {
	// // new PreferenceUtil(this).saveFavoiteCar(car);
	// // Toast.makeText(this, "收藏成功", Toast.LENGTH_LONG).show();
	// // } else {
	// // Toast.makeText(this, "����ѡ����", Toast.LENGTH_LONG).show();
	// // }
	// addFavoriteCar(mAdapter.getItem(checkedItem));
	//
	// Toast.makeText(SecondaryAutomobileListActivity.this, "收藏成功",
	// Toast.LENGTH_LONG).show();
	// mode.finish();
	// return true;
	// }
	// return false;
	// }
	// };

}
