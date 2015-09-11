package com.innoweaver.TyreTest.entity;

import com.j256.ormlite.field.DatabaseField;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CarEntity implements Parcelable, Car {

	public static final String TABLE_NAME = "_car";
	public static final String ID = "_id";
	public static final String BRAND_ID = "_brand_id";
	public static final String STYLE = "_style";
	public static final String F_STD_PRESSURE = "_f_std_pressure";
	public static final String R_STD_PRESSURE = "_r_std_pressure";
	public static final String F_MAX_PRESSURE = "_f_max_pressure";
	public static final String R_MAX_PRESSURE = "_r_max_pressure";

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "brand_id")
	private BrandEntity brand;
	@DatabaseField
	private String style;
	@DatabaseField
	private float fStdPressure;
	@DatabaseField
	private float rStdPressure;
	@DatabaseField
	private float fMaxPressure;
	@DatabaseField
	private float rMaxPressure;

	public CarEntity() {

	}

	public CarEntity(Parcel source) {
		id = source.readInt();
		brand = source.readParcelable(BrandEntity.class.getClassLoader());
		style = source.readString();
		fStdPressure = source.readFloat();
		rStdPressure = source.readFloat();
		fMaxPressure = source.readFloat();
		rMaxPressure = source.readFloat();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeParcelable(brand, 0);
		dest.writeString(style);
		dest.writeFloat(fStdPressure);
		dest.writeFloat(rStdPressure);
		dest.writeFloat(fMaxPressure);
		dest.writeFloat(rMaxPressure);

	}

	public static Parcelable.Creator<CarEntity> CREATOR = new Creator<CarEntity>() {
		public CarEntity[] newArray(int size) {
			return new CarEntity[size];
		}

		public CarEntity createFromParcel(Parcel source) {
			return new CarEntity(source);
		}
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BrandEntity getBrand() {
		return brand;
	}

	public void setBrand(BrandEntity brand) {
		this.brand = brand;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	@Override
	public float getfStdPressure() {
		return fStdPressure;
	}

	public void setfStdPressure(float fStdPressure) {
		this.fStdPressure = fStdPressure;
	}

	@Override
	public float getrStdPressure() {
		return rStdPressure;
	}

	public void setrStdPressure(float rStdPressure) {
		this.rStdPressure = rStdPressure;
	}

	public float getfMaxPressure() {
		return fMaxPressure;
	}

	public void setfMaxPressure(float fMaxPressure) {
		this.fMaxPressure = fMaxPressure;
	}

	public float getrMaxPressure() {
		return rMaxPressure;
	}

	public void setrMaxPressure(float rMaxPressure) {
		this.rMaxPressure = rMaxPressure;
	}

	@Override
	public String toString() {
		return "CarEntity [id=" + id + ", brand=" + brand + ", style=" + style
				+ ", fStdPressure=" + fStdPressure + ", rStdPressure="
				+ rStdPressure + ", fMaxPressure=" + fMaxPressure
				+ ", rMaxPressure=" + rMaxPressure + "]";
	}

}
