package com.innoweaver.TyreTest.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.innoweaver.TyreTest.db.CarDB;
import com.innoweaver.TyreTest.db.DatabaseHelper;
import com.innoweaver.TyreTest.entity.BrandEntity;
import com.innoweaver.TyreTest.entity.CarEntity;

public class CarService extends BasicService {
	private static Map<String, String> carProjectionMap;

	public CarService(Context context) {
		super(context);
	}

	static {
		carProjectionMap = new HashMap<String, String>();
		carProjectionMap.put(CarEntity.ID, CarEntity.ID);
		carProjectionMap.put(CarEntity.BRAND_ID, CarEntity.BRAND_ID);
		carProjectionMap.put(CarEntity.STYLE, CarEntity.STYLE);
		carProjectionMap
				.put(CarEntity.F_STD_PRESSURE, CarEntity.F_STD_PRESSURE);
		carProjectionMap
				.put(CarEntity.R_STD_PRESSURE, CarEntity.R_STD_PRESSURE);
		carProjectionMap
				.put(CarEntity.F_MAX_PRESSURE, CarEntity.F_MAX_PRESSURE);
		carProjectionMap
				.put(CarEntity.R_MAX_PRESSURE, CarEntity.R_MAX_PRESSURE);
	}

	public List<CarEntity> findCarsByBrand(BrandEntity brand)
			throws SQLException {
		// List<CarEntity> cars = new ArrayList<CarEntity>();
		//
		// int brandId = brand.getId();
		//
		// SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		//
		// qb.setTables(CarEntity.TABLE_NAME);
		// qb.setProjectionMap(carProjectionMap);
		//
		// SQLiteDatabase db = CarDB.getInstance(context).getWritableDatabase();
		// // SQLiteDatabase db = new CarDB(context).getWritableDatabase();
		//
		// Cursor c = qb.query(db, new String[] { CarEntity.ID,
		// CarEntity.BRAND_ID, CarEntity.STYLE, CarEntity.F_STD_PRESSURE,
		// CarEntity.R_STD_PRESSURE, CarEntity.F_MAX_PRESSURE,
		// CarEntity.R_MAX_PRESSURE }, CarEntity.BRAND_ID + "=?",
		// new String[] { brandId + "" }, null, null, null);
		//
		// while (c.moveToNext()) {
		// CarEntity car = new CarEntity();
		// car.setId(c.getInt(c.getColumnIndex(CarEntity.ID)));
		// car.setBrand(brand);
		// car.setStyle(c.getString(c.getColumnIndex(CarEntity.STYLE)));
		// car.setfStdPressure(c.getFloat(c
		// .getColumnIndex(CarEntity.F_STD_PRESSURE)));
		// car.setrStdPressure(c.getFloat(c
		// .getColumnIndex(CarEntity.R_STD_PRESSURE)));
		// car.setfMaxPressure(c.getFloat(c
		// .getColumnIndex(CarEntity.F_MAX_PRESSURE)));
		// car.setrMaxPressure(c.getFloat(c
		// .getColumnIndex(CarEntity.R_MAX_PRESSURE)));
		//
		// cars.add(car);
		// }
		//
		// c.close();
		// db.close();

		// BrandEntity brand = new BrandService(context).findBrandById(brandId,
		// db);

		DatabaseHelper helper = getDatabaseHelper();
		List<CarEntity> cars = helper.getCarDao().queryForEq("brand_id",
				brand.getId());
		closeHelper(helper);
		return cars;
	}

	public CarEntity findCarById(int carId) throws SQLException {
		// CarEntity car = null;
		//
		// SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		//
		// qb.setTables(CarEntity.TABLE_NAME);
		// qb.setProjectionMap(carProjectionMap);
		//
		// SQLiteDatabase db = CarDB.getInstance(context).getReadableDatabase();
		// // SQLiteDatabase db = new CarDB(context).getReadableDatabase();
		//
		// Cursor c = qb.query(db, new String[] { CarEntity.ID,
		// CarEntity.BRAND_ID, CarEntity.STYLE, CarEntity.F_STD_PRESSURE,
		// CarEntity.R_STD_PRESSURE, CarEntity.F_MAX_PRESSURE,
		// CarEntity.R_MAX_PRESSURE }, CarEntity.ID + "=?",
		// new String[] { carId + "" }, null, null, null);
		// if (c.moveToNext()) {
		// car = new CarEntity();
		//
		// car.setId(c.getInt(c.getColumnIndex(CarEntity.ID)));
		//
		// int brandId = c.getInt(c.getColumnIndex(CarEntity.BRAND_ID));
		// BrandService bs = new BrandService(context);
		// BrandEntity brand = bs.findBrandById(brandId, db);
		//
		// car.setBrand(brand);
		// car.setStyle(c.getString(c.getColumnIndex(CarEntity.STYLE)));
		// car.setfStdPressure(c.getFloat(c
		// .getColumnIndex(CarEntity.F_STD_PRESSURE)));
		// car.setrStdPressure(c.getFloat(c
		// .getColumnIndex(CarEntity.R_STD_PRESSURE)));
		// car.setfMaxPressure(c.getFloat(c
		// .getColumnIndex(CarEntity.F_MAX_PRESSURE)));
		// car.setrMaxPressure(c.getFloat(c
		// .getColumnIndex(CarEntity.R_MAX_PRESSURE)));
		// }
		//
		// c.close();
		// db.close();

		DatabaseHelper helper = getDatabaseHelper();
		CarEntity car = helper.getCarDao().queryForId(carId);
		closeHelper(helper);
		return car;
	}

}
