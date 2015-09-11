package com.innoweaver.TyreTest;

import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.innoweaver.TyreTest.bgService.AudioRecordingService;
import com.innoweaver.TyreTest.bgService.AudioRecordingService.RecordHelper;
import com.innoweaver.TyreTest.dataUtil.RecordExeView;
import com.innoweaver.TyreTest.dataUtil.RecordManager;
import com.innoweaver.TyreTest.entity.Car;
import com.innoweaver.TyreTest.entity.CarEntity;
import com.innoweaver.TyreTest.entity.Record;
import com.innoweaver.TyreTest.entity.RecordEntity;
import com.innoweaver.TyreTest.exception.InvalidPressure;
import com.innoweaver.TyreTest.service.PressureStandardValueHandler;
import com.innoweaver.TyreTest.service.PressureValueHandler;
import com.innoweaver.TyreTest.service.RecordService;
import android.text.StaticLayout;
import static com.innoweaver.TyreTest.service.PressureService.*;

public class TyreMainActivity extends BasicActivity {
	private static final String TAG = TyreMainActivity.class.getSimpleName();

	public static final int AUTOMOBILE_INDEX = 1;
	public static final int TEMPERATURE_INDEX = 2;
	public static final int PRESSURE_INDEX = 3;
	public static final int SAVE_INDEX = 4;
	public static final int LOAD_INDEX = 5;
	public static final String EXTRA_TYRE_INDEX = "typreIndex";
	public static final String PRESSURE_VALUE_INDEX = "pressure";
	public static final String EXTRA_TEMPERATURE_VALUE_INDEX = "temperature";
	public static final String EXTRA_CAR_INDEX = "carIndex";
	public static final String EXTRA_RECORD_INDEX = "recordIndex";
	public static final String EXTRA_VALUE_B = "valueB";
	public static final String EXTRA_VALUE_C = "valueC";

	// public static String kpa;
	// public static String bar;
	// public static String psi;

	private TyreApp appContext;
	// private SelectionRecord mRecord;
	// private RecordController mRecController;
	private List<RecordManager> mManagers;

	private RecordExeView<CarEntity> carRec;
	private RecordExeView<Double> tempRec;
	private Car carProxy;
	private PressureStandardValueHandler carHandler;

	private int[] btnArr = { R.id.tyre_0, R.id.tyre_1, R.id.tyre_2, R.id.tyre_3 };
	private ImageView[] tyreBtns;
	private TextView[] stdTxts;
	private TextView[] testTxts;
	private RecordExeView<Float>[] pressureRecs;
	private RecordEntity record;
	private Record recordProxy;
	private PressureValueHandler recordHanlder;

	// private WindowManager mWindowManager;
	private View mNoticeView;

	// private AudioTestModel mModel;
	private Handler noticeHanlder;
	private long valueB, valueC;
	private UnitChanger changer;

	private AudioConnection mConn;
	private AudioRecordingService.RecordHelper mHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tyre_main_page);

		init();

		setupTabs();
		setupTyreBtns();
		setupBottomBar();
		setupNotice();
		/**
		 * by kid
		 */
		// startCheck();

		// initialize the anim
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < mManagers.size(); i++) {
					mManagers.get(i).onRecordModified(null);
				}
			}
		}, 2000);

		/**
		 * turn on the service
		 */
		Intent service = new Intent(this, AudioRecordingService.class);
		bindService(service, mConn = new AudioConnection(), Service.BIND_AUTO_CREATE);

		// for (int i = 0; i < mManagers.size(); i++) {
		// mManagers.get(i).onRecordModified(null);
		// }
		// mRecController.onRecordModified(carRec);
	}

	@Override
	protected void onResume() {
		Log.e(TAG, "resume");
		// startCheck();
		super.onResume();
		if (mHelper != null) {
			mHelper.resume();
		}
	}

	@Override
	protected void onPause() {
		Log.e(TAG, "pause");
		super.onPause();
		if (mHelper != null) {
			mHelper.pause();
		}
	}

	@Override
	protected void onDestroy() {
		// mWindowManager.removeView(mNoticeView);

		// if (model != null) {
		// model.turnOff();
		// }
		if (noticeHanlder != null) {
			noticeHanlder.removeCallbacksAndMessages(null);
		}
		if (mConn != null) {
			unbindService(mConn);
		}
		// finishActivity(Launcher.LAUNCHER_REQUEST);
		super.onDestroy();
	}

	private void init() {
		appContext = (TyreApp) getApplicationContext();
		mManagers = new ArrayList<RecordManager>();

		valueB = getIntent().getLongExtra(EXTRA_VALUE_B, 0);
		valueC = getIntent().getLongExtra(EXTRA_VALUE_C, 0);
		Log.i(TAG, "b: " + valueB + ",  c: " + valueC);
		// mRecController = new RecordControllerImpl();

		// kpa = getString(R.string.pa_uit);
		// bar = getString(R.string.bar_unit);
		// psi = getString(R.string.psi_uit);
		// mRecord = new SelectionRecordImpl();
		// mRecord.addObserver(this);
	}

	private void setupTabs() {
		Button chooseCarBtn = (Button) findViewById(R.id.choose_car_btn);
		Button setTemperatureBtn = (Button) findViewById(R.id.set_temperature_btn);
		Button saveBtn = (Button) findViewById(R.id.save_btn);
		Button loadBtn = (Button) findViewById(R.id.load_btn);

		chooseCarBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(TyreMainActivity.this, AutomobileSelectActivity.class);
				CarEntity car = carRec.getValue();
				if (car != null) {
					intent.putExtra(TyreMainActivity.EXTRA_CAR_INDEX, car);
				}
				startActivityForResult(intent, AUTOMOBILE_INDEX);

			}
		});

		setTemperatureBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(getString(R.string.temperature_list_action));
				startActivityForResult(intent, TEMPERATURE_INDEX);

			}
		});

		saveBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// PreferenceUtil pu = new
				// PreferenceUtil(TyreMainActivity.this);
				// pu.savePressure(pressureRecs[0].getValue(),
				// pressureRecs[1].getValue(), pressureRecs[2].getValue(),
				// pressureRecs[3].getValue());
				try {
					RecordService recordService = new RecordService(TyreMainActivity.this);
					recordService.addRecord(pressureRecs[0].getValue(), pressureRecs[1].getValue(), pressureRecs[2].getValue(), pressureRecs[3].getValue());
					Toast.makeText(TyreMainActivity.this, "保存成功", Toast.LENGTH_LONG).show();

				} catch (InvalidPressure e) {
					Toast.makeText(TyreMainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
				} catch (SQLException e) {
					// TODO
					Log.e(TAG, e.toString());
				}

			}
		});

		loadBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(TyreMainActivity.this, TestRecordListActivity.class);
				startActivityForResult(intent, LOAD_INDEX);
			}
		});

		RecordManager manager = new RecordManager();

		carRec = new RecordExeView(appContext);
		carRec.assignTo(manager);
		carRec.setViewExecuter(chooseCarBtn);

		tempRec = new RecordExeView(appContext);
		tempRec.assignTo(manager);
		tempRec.setViewExecuter(setTemperatureBtn);

		mManagers.add(manager);
	}

	private void setupTyreBtns() {
		RecordManager pressureManager = new RecordManager();

		tyreBtns = new ImageView[btnArr.length];
		stdTxts = new TextView[btnArr.length];
		testTxts = new TextView[btnArr.length];
		pressureRecs = new RecordExeView[btnArr.length];
		record = new RecordEntity();
		recordHanlder = new PressureValueHandler(record);
		recordProxy = (Record) Proxy.newProxyInstance(record.getClass().getClassLoader(), record.getClass().getInterfaces(), recordHanlder);

		changer = new UnitChanger();

		for (int i = 0; i < btnArr.length; i++) {
			final int index = i;

			View v = findViewById(btnArr[index]);
			// Log.e("ff", "v ==null? " + (v == null));
			tyreBtns[index] = (ImageView) v.findViewById(R.id.tyre_btn);
			// Log.e("ff", "v ==null? " + (tyreBtns[i] == null));
			stdTxts[index] = (TextView) v.findViewById(R.id.std_txt);
			testTxts[index] = (TextView) v.findViewById(R.id.test_txt);
			pressureRecs[index] = new RecordExeView<Float>(appContext);
			pressureRecs[index].assignTo(pressureManager);
			pressureRecs[index].setViewExecuter(tyreBtns[index]);

			stdTxts[index].setOnClickListener(changer);
			testTxts[index].setOnClickListener(changer);

			final RecordExeView<Float> r = pressureRecs[index];

			tyreBtns[index].setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					Intent intent = new Intent();
					intent.setAction(getString(R.string.test_pressure_action));
					Log.e(TAG, "index: " + index);
					intent.putExtra(EXTRA_VALUE_B, getIntent().getLongExtra(EXTRA_VALUE_B, 0));
					intent.putExtra(EXTRA_VALUE_C, getIntent().getLongExtra(EXTRA_VALUE_C, 0));
					intent.putExtra(EXTRA_TYRE_INDEX, index);
					intent.putExtra(EXTRA_CAR_INDEX, carRec.getValue());
					// intent.putE
					startActivityForResult(intent, PRESSURE_INDEX);
				}
			});
			// pressureManager.addRecord(pressureRecs[i]);
		}
		mManagers.add(pressureManager);

	}

	private void setupBottomBar() {
		Button mockBtn = (Button) findViewById(R.id.mock_btn);
		Button aboutUsBtn = (Button) findViewById(R.id.about_us_btn);

		mockBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(EXTRA_CAR_INDEX, carRec.getValue());
				intent.putExtra(PRESSURE_VALUE_INDEX, record);
				intent.putExtra(EXTRA_VALUE_C, valueC);

				intent.setAction(getString(R.string.mock_action));
				startActivity(intent);
			}
		});

		aboutUsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(getString(R.string.introduction_action));
				startActivity(intent);
			}
		});
	}

	private void setupNotice() {
		mNoticeView = getLayoutInflater().inflate(R.layout.connection_notice, null);
		mNoticeView = findViewById(R.id.connection_notice);
		appContext.startAnimation(mNoticeView);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case AUTOMOBILE_INDEX: {
				// Log.e(TAG,
				// "CAR: "
				// + ((CarEntity) data
				// .getParcelableExtra(TyreMainActivity.CAR_INDEX))
				// .toString());
				Car car = (CarEntity) data.getParcelableExtra(TyreMainActivity.EXTRA_CAR_INDEX);

				carRec.setValue((CarEntity) car);
				carHandler = new PressureStandardValueHandler((CarEntity) car, tempRec.getValue() == null ? null : tempRec.getValue().floatValue());
				carProxy = (Car) Proxy.newProxyInstance(car.getClass().getClassLoader(), car.getClass().getInterfaces(), carHandler);
				// carProxy = (Car) Proxy.newProxyInstance(
				// Car.class.getClassLoader(), Car.class.getInterfaces(),
				// carHandler);
				changer.updateData();

				break;
			}
			case TEMPERATURE_INDEX: {
				double temperature = data.getDoubleExtra(EXTRA_TEMPERATURE_VALUE_INDEX, 0);
				if (temperature != 0) {
					tempRec.setValue(temperature);
					Log.e(TAG, "temperature: " + temperature);
				}
				if (carHandler != null) {
					carHandler.setTemperature((float) temperature);
				}

				break;
			}
			case PRESSURE_INDEX: {
				handlePressure(data);

				break;
			}
			case LOAD_INDEX: {
				RecordEntity record = data.getParcelableExtra(EXTRA_RECORD_INDEX);
				RecordService service = new RecordService(this);
				service.loadPressure(pressureRecs, record);
				// testTxts[0].setText(getString(R.string.test_result_tag);
				for (int i = 0; i < pressureRecs.length; i++) {
					testTxts[i].setText(getString(R.string.test_result_tag) + " " + pressureRecs[i].getValue());
				}
			}
			default:
				super.onActivityResult(requestCode, resultCode, data);
				break;
			}
		}
	}

	private void handlePressure(Intent data) {
		int index = data.getIntExtra(EXTRA_TYRE_INDEX, -1);
		if (index >= 0 && index < pressureRecs.length) {
			// Log.e(TAG, "index: " + index);
			float pressure = data.getFloatExtra(PRESSURE_VALUE_INDEX, 0);
			pressureRecs[index].setValue(pressure);
			Log.e(TAG, "pressure: " + pressure);

			switch (index) {
			case 0:
				record.setFlPressure(pressure);
				break;
			case 1:
				record.setFrPressure(pressure);
				break;
			case 2:
				record.setRlPressure(pressure);
				break;
			case 3:
				record.setRrPressure(pressure);
			default:
				break;
			}

			changer.updateData();
		}

	}

	private void setTagText(TextView txt, String tag, float value, String unit) {
		Log.e(TAG, tag + " " + new DecimalFormat("0.0").format(value) + "" + unit);
		if (unit.equals(UNIT_KPA)) {
			txt.setText(tag + " " + (int) value + unit);
		} else {
			txt.setText(tag + " " + new DecimalFormat("0.0").format(value) + unit);
		}
	}

	private class UnitChanger implements OnClickListener {
		private int c = 0;

		// 0: bar, 1: kpa, 2: psi

		@Override
		public void onClick(View v) {
			c++;
			if (c > 2) {
				c = 0;
			}
			if (carHandler != null) {
				carHandler.setCount(c);
			}
			recordHanlder.setCount(c);

			updateData();
		}

		public void updateData() {
			// if (c == 0) {
			String unit = "";
			switch (c) {
			case 0:
				unit = UNIT_BAR;
				break;
			case 1:
				unit = UNIT_KPA;
				break;
			case 2:
				unit = UNIT_PSI;
				break;
			}

			for (int i = 0; i < testTxts.length; i++) {
				float pressure = 0;
				if (pressureRecs[i].getValue() != null) {
					switch (i) {
					case 0:
						pressure = recordProxy.getFlPressure();
						break;
					case 1:
						pressure = recordProxy.getFrPressure();
						break;
					case 2:
						pressure = recordProxy.getRlPressure();
						break;
					case 3:
						pressure = recordProxy.getRrPressure();
						break;
					default:
						break;
					}
					setTagText(testTxts[i], getString(R.string.test_result_tag), pressure, unit);
				}

			}

			// for standard pressures
			for (int i = 0; i < testTxts.length; i++) {
				if (pressureRecs[i].getValue() != null) {
					if (i < 2 && carRec.getValue() != null) {
						setTagText(stdTxts[i], getString(R.string.standard_tag), carProxy.getfStdPressure(), unit);
					} else if (carRec.getValue() != null) {
						setTagText(stdTxts[i], getString(R.string.standard_tag), carProxy.getrStdPressure(), unit);
					}
				}
			}

			Log.e(TAG, "change");
		}
	}

	private class CheckRunnable implements Runnable {
		@Override
		public void run() {
			long value = mHelper.getTestValue();
			// Log.e(TAG, "value: " + value);
			checkIfConnectedToTyre(value);

			noticeHanlder.postDelayed(new CheckRunnable(), 5000);
		}

		private void checkIfConnectedToTyre(long value) {
			if (value > valueC * 2) {
				// Log.e(TAG, "invisible");
				appContext.stopAnimation(mNoticeView);
			} else {
				// Log.e(TAG, "visible");
				appContext.startAnimation(mNoticeView);
			}
		}
	}

	private class AudioConnection implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mHelper = (RecordHelper) service;
			mHelper.resume();
			noticeHanlder = new Handler();
			noticeHanlder.post(new CheckRunnable());
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	}

}
