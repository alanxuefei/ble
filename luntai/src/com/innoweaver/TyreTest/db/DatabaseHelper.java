package com.innoweaver.TyreTest.db;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.innoweaver.TyreTest.entity.BrandEntity;
import com.innoweaver.TyreTest.entity.CarEntity;
import com.innoweaver.TyreTest.entity.RecordEntity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private Context context;

	private Dao<CarEntity, Integer> carDao;
	private Dao<BrandEntity, Integer> brandDao;
	private Dao<RecordEntity, Integer> recordDao;

	public DatabaseHelper(Context context) {
		super(context, DBConstants.DABABASE_NAME, null, DBConstants.VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db,
			final ConnectionSource connectionSource) {
		Log.i(DatabaseHelper.class.getName(), "onCreate");

		try {
			carDao = getCarDao();
			brandDao = getBrandDao();

			TransactionManager.callInTransaction(connectionSource,
					new Callable<Void>() {
						@Override
						public Void call() throws Exception {
							TableUtils.createTable(connectionSource,
									BrandEntity.class);
							TableUtils.createTable(connectionSource,
									CarEntity.class);
							TableUtils.createTable(connectionSource,
									RecordEntity.class);

							// add brand
							InputStream is = context.getAssets().open(
									"data.xls");
							HSSFWorkbook wbs = new HSSFWorkbook(is);
							HSSFSheet childSheet = wbs.getSheetAt(0);
							// add brand data
							Log.e("DB", "index: " + childSheet.getLastRowNum());
							for (int i = 0; i < childSheet.getLastRowNum() + 1; i++) {
								HSSFRow row = childSheet.getRow(i);
								Log.e("DB", i + " " + (row == null));
								if (row != null) {
									// HSSFCell cell = row.getCell(0);
									String brandName = row.getCell(0)
											.getStringCellValue();
									String img = row.getCell(1)
											.getStringCellValue();

									BrandEntity brand = new BrandEntity();
									brand.setName(brandName);
									brand.setImg(img);

									Log.e("db", "name: " + brandName);

									brandDao.createIfNotExists(brand);
								}
							}
							// add car data
							HSSFSheet chiHssfSheet2 = wbs.getSheetAt(1);
							for (int i = 0; i < chiHssfSheet2.getLastRowNum(); i++) {
								HSSFRow row = chiHssfSheet2.getRow(i);
								if (row != null) {
									String brandName = row.getCell(2)
											.getStringCellValue();

									// Cursor c =
									// db.query(BrandEntity.TABLE_NAME,
									// new String[] { BrandEntity.ID },
									// BrandEntity.NAME + "=?",
									// new String[] { brandName }, null,
									// null, null);
									// int brandId = 0;
									// if (c.moveToNext()) {
									// brandId = c.getInt(0);
									// }
									// Log.e("db", "id = " + brandId);
									// c.close();

									String style = row.getCell(3)
											.getStringCellValue();
									double fStdPressure = row.getCell(4)
											.getNumericCellValue();
									double rStdPressure = row.getCell(5)
											.getNumericCellValue();
									double fMaxPressure = row.getCell(6)
											.getNumericCellValue();
									double rMaxPressure = row.getCell(7)
											.getNumericCellValue();

									CarEntity car = new CarEntity();
									Log.e("db", "name: " + brandName);

									BrandEntity brand = brandDao.queryForEq(
											"name", brandName).get(0);
									car.setBrand(brand);
									car.setStyle(style);
									car.setfStdPressure((float) fStdPressure);
									car.setrStdPressure((float) rStdPressure);
									car.setfMaxPressure((float) fMaxPressure);
									car.setrMaxPressure((float) rMaxPressure);
									// car.setBrand(brand)
									carDao.createIfNotExists(car);
								}
							}

							return null;
						}

					});

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1,
			int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, BrandEntity.class, true);
			TableUtils.dropTable(connectionSource, CarEntity.class, true);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	public Dao<CarEntity, Integer> getCarDao() throws SQLException {
		if (carDao == null) {
			carDao = getDao(CarEntity.class);
		}
		return carDao;
	}

	public Dao<BrandEntity, Integer> getBrandDao() throws SQLException {
		if (brandDao == null) {
			brandDao = getDao(BrandEntity.class);
		}
		return brandDao;
	}

	public Dao<RecordEntity, Integer> getRecordDao() throws SQLException {
		if (recordDao == null) {
			recordDao = getDao(RecordEntity.class);
		}
		return recordDao;
	}

	@Override
	public void close() {
		carDao = null;
		brandDao = null;
		recordDao = null;
		super.close();
	}

}
