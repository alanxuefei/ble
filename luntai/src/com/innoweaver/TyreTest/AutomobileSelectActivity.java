package com.innoweaver.TyreTest;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.innoweaver.TyreTest.entity.BrandEntity;
import com.innoweaver.TyreTest.entity.CarEntity;
import com.innoweaver.TyreTest.service.BrandService;
import com.innoweaver.TyreTest.util.PreferenceUtil;

public class AutomobileSelectActivity extends BasicActivity implements
		LoaderManager.LoaderCallbacks<List<BrandEntity>> {
	private final static String TAG = AutomobileSelectActivity.class
			.getSimpleName();

	public final int FIND_CAR = 10;

	private int selection;
	private ListView amList;
	private AmListAdapter mAdapter;
	private Map<String, SoftReference<Bitmap>> imgCache;

	private CarEntity car;

	@Override
	protected void onSetTitle() {
		title = getString(R.string.car_title);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.automobile_list);

		Intent intent = getIntent();
		CarEntity car = intent.getParcelableExtra(TyreMainActivity.EXTRA_CAR_INDEX);
		if (car != null) {
			this.car = car;
		}

		// new LoadFavoriteTask().execute();

		setupList();
		// setupBtns();
		getSupportLoaderManager().initLoader(0, null, this);

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		recycleCache();
	}

	private void setupList() {
		amList = (ListView) findViewById(R.id.automobile_list);

		imgCache = new HashMap<String, SoftReference<Bitmap>>();
		amList.setAdapter(mAdapter = new AmListAdapter(this));

		amList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// amList.setSelection(position);
				selection = position;
				launchSecondaryList();
			}
		});
	}
	
	private void recycleCache() {
		Set<Entry<String, SoftReference<Bitmap>>> set = imgCache.entrySet();
		Iterator<Entry<String, SoftReference<Bitmap>>> i = set.iterator();
		if(i.hasNext()){
			Entry<String, SoftReference<Bitmap>> entry = i.next();
			Bitmap bit = entry.getValue().get();
			if(bit!=null){
				bit.recycle();
			}
		}
		
	}

	// private void setupBtns() {
	// Button confirmBtn = (Button) findViewById(R.id.confirm_btn);
	// Button backBtn = (Button) findViewById(R.id.back_btn);
	//
	// confirmBtn.setOnClickListener(new OnClickListener() {
	// public void onClick(View v) {
	// setResult(Activity.RESULT_OK);
	// // onBackPressed(null);
	// finish();
	// }
	// });
	//
	// backBtn.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// finish();
	// // int s = amList.getSelectedItemPosition();
	// // amList.getChi
	// // Log.e(TAG, "choice: " + s);
	// }
	// });
	// }

	private class ListItemHolder {
		ImageView logoImg;
		TextView nameTxt;
	}

	private void launchSecondaryList() {
		Intent intent = new Intent();
		int position = amList.getCheckedItemPosition();
		BrandEntity brand = mAdapter.getItem(position);
		intent.putExtra("brand", brand);
		if (car != null && (brand.equals(car.getBrand()))) {
			intent.putExtra(TyreMainActivity.EXTRA_CAR_INDEX, car);
		}
		// intent.putExtra("", value)
		intent.setAction(getString(R.string.secondary_automobile_list_action));
		startActivityForResult(intent, FIND_CAR);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			// Log.e(TAG, "REUSLT");
			car = data.getParcelableExtra(TyreMainActivity.EXTRA_CAR_INDEX);
			Log.e(TAG, "REUSLT car is null? " + (car == null));
			if (car != null) {
				// Log.e(TAG, "REUSLT OK");
				setResult(RESULT_OK, data);

				finish();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public Loader<List<BrandEntity>> onCreateLoader(int id, Bundle args) {
		return new BrandLoader(this);
	}

	@Override
	public void onLoadFinished(Loader<List<BrandEntity>> loader,
			List<BrandEntity> data) {
		mAdapter.setData(data);
		new LoadFavoriteTask().execute();

		if (car != null) {
			int brandId = car.getBrand().getId();
			int position = -1;
			for (int i = 0; i < mAdapter.getCount(); i++) {
				if (brandId == mAdapter.getItem(i).getId()) {
					position = i;
					break;
				}
			}
			if (position != -1) {
				amList.setItemChecked(position, true);
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<List<BrandEntity>> loader) {
		mAdapter.setData(null);
	}

	private class AmListAdapter extends ArrayAdapter<BrandEntity> {
		private HashMap<String, SoftReference<Bitmap>> cache;

		public AmListAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_2);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				// Log.(TAG,"convertView is NULL");
				convertView = getLayoutInflater().inflate(
						R.layout.automobile_list_item, parent, false);
				ImageView logoImg = (ImageView) convertView
						.findViewById(R.id.logo_img);
				TextView nameTxt = (TextView) convertView
						.findViewById(R.id.name_txt);
				ListItemHolder mHolder = new ListItemHolder();
				mHolder.logoImg = logoImg;
				mHolder.nameTxt = nameTxt;
				convertView.setTag(mHolder);
			}

			BrandEntity brand = getItem(position);
			// Log.e(TAG, "brand==null? " + (brand == null));
			ListItemHolder mHolder = (ListItemHolder) convertView.getTag();
			mHolder.nameTxt.setText(brand.getName());
			if (imgCache.containsKey(brand.getImg())
					&& imgCache.get(brand.getImg()).get() != null) {
				mHolder.logoImg.setImageBitmap(imgCache.get(brand.getImg())
						.get());
			} else {
				try {
					Bitmap bit = BitmapFactory.decodeStream(getAssets().open(
							brand.getImg()));
					imgCache.put(brand.getImg(), new SoftReference<Bitmap>(bit));
					mHolder.logoImg.setImageBitmap(bit);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return convertView;
		}

		public void setData(List<BrandEntity> data) {
			clear();
			if (data != null) {
				// addAll(data);
				for (int i = 0; i < data.size(); i++) {
					add(data.get(i));
				}
				// Log.e(TAG, "changed");
				notifyDataSetChanged();
			}
		}
	}

	private static class BrandLoader extends AsyncTaskLoader<List<BrandEntity>> {
		List<BrandEntity> mData;

		public BrandLoader(Context context) {
			super(context);
		}

		@Override
		public List<BrandEntity> loadInBackground() {
			try {
				BrandService service = new BrandService(getContext());
				List<BrandEntity> brands = service.getAllBrands();

				Log.e("BrandLoader", "brands: " + brands.toString());
				return brands;
			} catch (SQLException e) {
				Log.e(TAG, e.toString());
			}
			return mData;
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
	}

	private class LoadFavoriteTask extends AsyncTask<Void, Void, CarEntity> {
		@Override
		protected CarEntity doInBackground(Void... params) {
			try {
				CarEntity car = new PreferenceUtil(
						AutomobileSelectActivity.this).loadFravoriteCar();
				Log.e(TAG, "car: " + car);
				return car;
			} catch (SQLException e) {
				Log.e(TAG, e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(final CarEntity result) {
			if (result != null) {
				// findViewById(R.id.automobile_list_header).setVi
				View header = findViewById(R.id.automobile_list_header);
				header.setVisibility(View.VISIBLE);
				TextView txt = (TextView) header.findViewById(R.id.name_txt);
				txt.setText(result.getBrand().getName()+" "+result.getStyle());
				
				header.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// if (v instanceof Checkable) {
						// ((Checkable) v).setChecked(true);
						// }

						Intent data = getIntent();
						data.putExtra(TyreMainActivity.EXTRA_CAR_INDEX, result);
						setResult(RESULT_OK, data);

						finish();
					}
				});
			}
		}

	}

}
