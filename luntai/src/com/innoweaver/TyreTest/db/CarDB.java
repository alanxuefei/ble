package com.innoweaver.TyreTest.db;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.innoweaver.TyreTest.entity.BrandEntity;
import com.innoweaver.TyreTest.entity.CarEntity;

public class CarDB extends SQLiteOpenHelper {
	private final static String TAG = CarDB.class.getSimpleName();

	Context context;

	private static CarDB mHelper;

	public static CarDB getInstance(Context context) {
		if (mHelper == null) {
			mHelper = new CarDB(context);
		}
		return mHelper;
	}

	private CarDB(Context context) {
		this(context, DBConstants.DABABASE_NAME, null, DBConstants.VERSION);
	}

	private CarDB(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);

		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createCarTable = "CREATE TABLE " + CarEntity.TABLE_NAME + " ("
				+ CarEntity.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ CarEntity.BRAND_ID + " INTEGER, " + CarEntity.STYLE
				+ " VARCHAR(50), " + CarEntity.F_STD_PRESSURE + " REAL, "
				+ CarEntity.R_STD_PRESSURE + " REAL, "
				+ CarEntity.F_MAX_PRESSURE + " REAL, "
				+ CarEntity.R_MAX_PRESSURE + " REAL);";
		Log.e("db", "create");
		String createBrandTable = "CREATE TABLE " + BrandEntity.TABLE_NAME
				+ " (" + BrandEntity.ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + BrandEntity.NAME
				+ " VARCHAR(50), " + BrandEntity.IMG + " VARCHAR(50)" + ");";
		Log.e(TAG, createBrandTable);

		try {
			db.beginTransaction();
			// db.execSQL("CREATE TABLE android_metadata (locale TEXT DEFAULT 'zh_CN'");
			db.execSQL(createBrandTable);
			db.execSQL(createCarTable);

			InputStream is = context.getAssets().open("data.xls");
			HSSFWorkbook wbs = new HSSFWorkbook(is);
			HSSFSheet childSheet = wbs.getSheetAt(0);
			// add brand data
			for (int i = 0; i < childSheet.getLastRowNum(); i++) {
				HSSFRow row = childSheet.getRow(i);
				if (row != null) {
					// HSSFCell cell = row.getCell(0);
					String brandName = row.getCell(0).getStringCellValue();
					String img = row.getCell(1).getStringCellValue();

					ContentValues value = new ContentValues();
					value.put(BrandEntity.NAME, brandName);
					value.put(BrandEntity.IMG, img);

					db.insert(BrandEntity.TABLE_NAME, null, value);
				}
			}
			// add car data
			HSSFSheet chiHssfSheet2 = wbs.getSheetAt(1);
			for (int i = 0; i < chiHssfSheet2.getLastRowNum(); i++) {
				HSSFRow row = chiHssfSheet2.getRow(i);
				if (row != null) {
					String brandName = row.getCell(2).getStringCellValue();

					Cursor c = db.query(BrandEntity.TABLE_NAME,
							new String[] { BrandEntity.ID }, BrandEntity.NAME
									+ "=?", new String[] { brandName }, null,
							null, null);
					int brandId = 0;
					if (c.moveToNext()) {
						brandId = c.getInt(0);
					}
					Log.e("db", "id = " + brandId);
					c.close();

					String style = row.getCell(3).getStringCellValue();
					double fStdPressure = row.getCell(4).getNumericCellValue();
					double rStdPressure = row.getCell(5).getNumericCellValue();
					double fMaxPressure = row.getCell(6).getNumericCellValue();
					double rMaxPressure = row.getCell(7).getNumericCellValue();

					ContentValues value = new ContentValues();
					value.put(CarEntity.BRAND_ID, brandId);
					value.put(CarEntity.STYLE, style);
					value.put(CarEntity.F_STD_PRESSURE, fStdPressure);
					value.put(CarEntity.R_STD_PRESSURE, rStdPressure);
					value.put(CarEntity.F_MAX_PRESSURE, fMaxPressure);
					value.put(CarEntity.R_MAX_PRESSURE, rMaxPressure);

					db.insert(CarEntity.TABLE_NAME, null, value);

				}
			}
			db.setTransactionSuccessful();
			db.endTransaction();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
