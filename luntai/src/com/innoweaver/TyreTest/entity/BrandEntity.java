package com.innoweaver.TyreTest.entity;

import com.j256.ormlite.field.DatabaseField;

import android.os.Parcel;
import android.os.Parcelable;

public class BrandEntity implements Parcelable {
	public final static String TABLE_NAME = "_brand";
	public final static String ID = "_id";
	public final static String NAME = "_name";
	public final static String IMG = "_img";

	@DatabaseField(generatedId =true)
	private int id;
	@DatabaseField
	private String name;
	@DatabaseField
	private String img;

	public BrandEntity() {

	}

	public BrandEntity(Parcel source) {
		id = source.readInt();
		name = source.readString();
		img = source.readString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(img);
	}

	public static Parcelable.Creator<BrandEntity> CREATOR = new Creator<BrandEntity>() {
		public BrandEntity[] newArray(int size) {
			return new BrandEntity[size];
		}

		public BrandEntity createFromParcel(Parcel source) {
			return new BrandEntity(source);
		}
	};

	public boolean equals(Object o) {
		if (o instanceof BrandEntity) {
			if (((BrandEntity) o).getId() == id) {
				return true;
			}
		}
		return false;
	};
}
