package com.innoweaver.TyreTest.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.innoweaver.TyreTest.dataUtil.RecordExeView;
import com.innoweaver.TyreTest.db.DatabaseHelper;
import com.innoweaver.TyreTest.entity.RecordEntity;
import com.innoweaver.TyreTest.exception.InvalidPressure;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class RecordService extends BasicService {
	private static final int MAX_COUNT = 3;

	public RecordService(Context context) {
		super(context);
	}

	public void addRecord(RecordEntity record) throws SQLException {
		DatabaseHelper helper = new DatabaseHelper(context);
		Dao<RecordEntity, Integer> recordDao = helper.getRecordDao();

		long count = recordDao.countOf();
		if (count >= 3) {
			QueryBuilder<RecordEntity, Integer> builder = recordDao
					.queryBuilder();
			builder.orderBy("id", true);
			RecordEntity firstRecord = builder.queryForFirst();
			recordDao.delete(firstRecord);
			// RecordEntity lastRecord = recordDao.
		}

		recordDao.create(record);
		closeHelper(helper);
	}

	public void addRecord(Float... pressures) throws InvalidPressure,
			SQLException {
		checkIfValid(pressures);
		RecordEntity record = new RecordEntity();
		record.setFlPressure(pressures[0]);
		record.setFrPressure(pressures[1]);
		record.setRlPressure(pressures[2]);
		record.setRrPressure(pressures[3]);
		record.setCreatedDate(new Date());
		addRecord(record);
	}

	public List<RecordEntity> getAllRecords() throws SQLException {
		DatabaseHelper helper = new DatabaseHelper(context);
		Dao<RecordEntity, Integer> recordDao = helper.getRecordDao();
		List<RecordEntity> records = recordDao.queryForAll();
		Log.e("record service", "SIZE: " + records.size());
		return records;
	}
	
	public void loadPressure(RecordExeView<Float>[] pressures, RecordEntity record) {
		pressures[0].setValue(record.getFlPressure());
		pressures[1].setValue(record.getFrPressure());
		pressures[2].setValue(record.getRlPressure());
		pressures[3].setValue(record.getRrPressure());
	}

	private void checkIfValid(Float[] pressures) throws InvalidPressure {
		if (pressures == null || pressures.length < 4) {
			throw new InvalidPressure("请先测量完");
		}
		for (int i = 0; i < pressures.length; i++) {
			if (pressures[i] == null) {
				throw new InvalidPressure("请先测量完");
			}
		}
	}

}
