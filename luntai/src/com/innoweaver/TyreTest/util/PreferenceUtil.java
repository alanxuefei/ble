package com.innoweaver.TyreTest.util;

import java.sql.SQLException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

import com.innoweaver.TyreTest.dataUtil.RecordExeView;
import com.innoweaver.TyreTest.entity.CarEntity;
import com.innoweaver.TyreTest.service.CarService;

public class PreferenceUtil {
	Context context;
	private final static String NAME = "car_pref";
	private final static String CAR = "car";
	public final static int INVALID_INT = -1;

	public PreferenceUtil(Context context) {
		super();
		this.context = context;
	}

	// resource-consuming, should be done in non-UI thread;
	public CarEntity loadFravoriteCar() throws SQLException {
		SharedPreferences pref = getSharedPreferences();
		int carId = pref.getInt(CAR, INVALID_INT);
		Log.e("tag", "carId: " + carId);
		CarService cs = new CarService(context);

		return cs.findCarById(carId);
	}

	public void saveFavoiteCar(CarEntity car) {
		SharedPreferences pref = getSharedPreferences();
		Editor editor = pref.edit();
		editor.putInt(CAR, car.getId());
		editor.commit();
	}

	public void savePressure(Float... pressures) {
		if (!checkIfValid(pressures)) {
			Toast.makeText(context, "��Ǹ, ȥ��ղ�������������ڱ���", Toast.LENGTH_LONG)
					.show();
			return;
		}

		SharedPreferences pref = getSharedPreferences();
		Editor editor = pref.edit();
		editor.putFloat("0", pressures[0]);
		editor.putFloat("1", pressures[1]);
		editor.putFloat("2", pressures[2]);
		editor.putFloat("3", pressures[3]);
		editor.commit();
	}

	public void loadPressure(RecordExeView<Float>[] pressures) {
		SharedPreferences pref = getSharedPreferences();
		pressures[0].setValue(pref.getFloat("0", 0));
		pressures[1].setValue(pref.getFloat("1", 0));
		pressures[2].setValue(pref.getFloat("2", 0));
		pressures[3].setValue(pref.getFloat("3", 0));

	}

	private SharedPreferences getSharedPreferences() {
		return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
	}

	private boolean checkIfValid(Float[] pressures) {
		if (pressures == null || pressures.length < 4) {
			return false;
		}
		for (int i = 0; i < pressures.length; i++) {
			if (pressures[i] == null) {
				return false;
			}
		}
		return true;
	}
}
