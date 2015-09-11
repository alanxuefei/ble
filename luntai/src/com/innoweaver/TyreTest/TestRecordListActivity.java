package com.innoweaver.TyreTest;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.innoweaver.TyreTest.entity.RecordEntity;
import com.innoweaver.TyreTest.service.RecordService;

public class TestRecordListActivity extends BasicActivity implements
		LoaderManager.LoaderCallbacks<List<RecordEntity>> {

	private static final String TAG = TestRecordListActivity.class
			.getSimpleName();
	private RecordListAdapter mAdapter;

//	String[] data = { "2012-11-11 13:00:00", "2012-11-10 11:01:20",
//			"2012-11-09 15:10:00", "2012-11-08 07:01:15",
//			"2012-11-07 19:00:30", "2012-11-06 13:00:00" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.test_record_list);

		getSupportLoaderManager().initLoader(0, null, this);
		
		ListView recordList = (ListView) findViewById(R.id.record_list);
		recordList.setAdapter(mAdapter = new RecordListAdapter(this));
		recordList.setOnItemClickListener(recordClickListener);
	}

	private class ListItemHolder {
		TextView nameTxt;
	}

	@Override
	public android.support.v4.content.Loader<List<RecordEntity>> onCreateLoader(
			int id, Bundle args) {
		return new RecordsLoader(this);
	}

	@Override
	public void onLoadFinished(
			android.support.v4.content.Loader<List<RecordEntity>> loader,
			List<RecordEntity> data) {
		mAdapter.setData(data);
	}

	@Override
	public void onLoaderReset(
			android.support.v4.content.Loader<List<RecordEntity>> loader) {
		mAdapter.setData(null);
	}

	private static class RecordsLoader extends
			AsyncTaskLoader<List<RecordEntity>> {
		List<RecordEntity> mData;

		public RecordsLoader(Context context) {
			super(context);
		}

		@Override
		public List<RecordEntity> loadInBackground() {
			try {
				RecordService service = new RecordService(getContext());
				List<RecordEntity> records = service.getAllRecords();
				Log.e(TAG, "SIZE: "+records.size());
				return records;
			} catch (SQLException e) {
				Log.e(TAG, e.toString());
			}
			return null;
		}

		@Override
		protected void onStartLoading() {
			if (mData != null) {
				deliverResult(mData);
			} else {
				forceLoad();
			}
		}
	}

	private class RecordListAdapter extends ArrayAdapter<RecordEntity> {
		private DateFormat formate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		

		public RecordListAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_2);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				// Log.(TAG,"convertView is NULL");
				convertView = getLayoutInflater().inflate(
						R.layout.test_record_list_item, parent, false);
				TextView nameTxt = (TextView) convertView
						.findViewById(R.id.record_txt);

				ListItemHolder mHolder = new ListItemHolder();
				mHolder.nameTxt = nameTxt;

				convertView.setTag(mHolder);
			}

			RecordEntity record = getItem(position);
			ListItemHolder mHolder = (ListItemHolder) convertView.getTag();
			Date date = record.getCreatedDate();
			mHolder.nameTxt.setText(formate.format(date));
			return convertView;
		}

		public void setData(List<RecordEntity> data) {
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
	
	private OnItemClickListener recordClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent data = getIntent();
			data.putExtra(TyreMainActivity.EXTRA_RECORD_INDEX, mAdapter.getItem(position));
			setResult(RESULT_OK, data);
			finish();
		}
	};

}
