package com.innoweaver.TyreTest.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.innoweaver.TyreTest.db.DatabaseHelper;
import com.innoweaver.TyreTest.entity.BrandEntity;

public class BrandService extends BasicService{
	private static Map<String, String> brandProjectionMap;

	static {
		brandProjectionMap = new HashMap<String, String>();
		brandProjectionMap.put(BrandEntity.ID, BrandEntity.ID);
		brandProjectionMap.put(BrandEntity.NAME, BrandEntity.NAME);
		brandProjectionMap.put(BrandEntity.IMG, BrandEntity.IMG);
	}

	public BrandService(Context context) {
		super(context);
	}

	public List<BrandEntity> getAllBrands() throws SQLException {
		// List<BrandEntity> brands = new ArrayList<BrandEntity>();

		// SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		//
		// qb.setTables(BrandEntity.TABLE_NAME);
		// qb.setProjectionMap(brandProjectionMap);
		//
		// // SQLiteOpenHelper oh = new CarDB(context,
		// DBConstants.DABABASE_NAME,
		// // null, DBConstants.VERSION);
		// // SQLiteDatabase db = oh.getReadableDatabase();
		//
		// // SQLiteDatabase db = new CarDB(context).getWritableDatabase();
		// SQLiteDatabase db = CarDB.getInstance(context).getWritableDatabase();
		// Log.e("service", "db==null " + (db == null));
		//
		// Cursor c = qb.query(db, new String[] { BrandEntity.ID,
		// BrandEntity.NAME, BrandEntity.IMG }, null, null, null, null,
		// null);
		//
		// while (c.moveToNext()) {
		// BrandEntity brand = new BrandEntity();
		// brand.setId(c.getInt(0));
		// brand.setName(c.getString(1));
		// brand.setImg(c.getString(2));
		// brands.add(brand);
		// }
		//
		// c.close();
		// db.close();
		DatabaseHelper helper = getDatabaseHelper();
		List<BrandEntity> brands = helper.getBrandDao().queryForAll();
		closeHelper(helper);
		return brands;
	}

	// public BrandEntity findBrandById(int brandId, SQLiteDatabase db) throws
	// SQLException {
	// // String brandName = null;
	// // String img = null;
	// // BrandEntity brand = null;
	// //
	// // SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	// //
	// // qb.setTables(BrandEntity.TABLE_NAME);
	// // qb.setProjectionMap(brandProjectionMap);
	// //
	// // Cursor c = qb.query(db, new String[] { BrandEntity.NAME,
	// // BrandEntity.IMG }, BrandEntity.ID + "=?",
	// // new String[] { brandId + "" }, null, null, null);
	// //
	// // while (c.moveToNext()) {
	// // brandName = c.getString(0);
	// // img = c.getString(1);
	// // }
	// // if (brandName != null) {
	// // brand = new BrandEntity();
	// // brand.setId(brandId);
	// // brand.setName(brandName);
	// // brand.setImg(img);
	// // }
	// //
	// // c.close();
	//
	// DatabaseHelper helper = new DatabaseHelper(context);
	// BrandEntity brand = helper.getBrandDao().queryForId(brandId);
	//
	// return brand;
	// }
}
