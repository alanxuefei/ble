package com.innoweaver.TyreTest.entity;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class RecordEntity implements Parcelable, Record {

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private float flPressure;
	@DatabaseField
	private float frPressure;
	@DatabaseField
	private float rlPressure;
	@DatabaseField
	private float rrPressure;
	@DatabaseField(dataType = DataType.DATE_STRING, format = "yyyy-MM-dd HH:mm:ss")
	private Date createdDate;

	public RecordEntity() {

	}

	public RecordEntity(Parcel source) {
		id = source.readInt();
		flPressure = source.readFloat();
		frPressure = source.readFloat();
		rlPressure = source.readFloat();
		rrPressure = source.readFloat();
		createdDate = (Date) source.readSerializable();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public float getFlPressure() {
		return flPressure;
	}

	public void setFlPressure(float flPressure) {
		this.flPressure = flPressure;
	}

	@Override
	public float getFrPressure() {
		return frPressure;
	}

	public void setFrPressure(float frPressure) {
		this.frPressure = frPressure;
	}

	@Override
	public float getRlPressure() {
		return rlPressure;
	}

	public void setRlPressure(float rlPressure) {
		this.rlPressure = rlPressure;
	}

	@Override
	public float getRrPressure() {
		return rrPressure;
	}

	public void setRrPressure(float rrPressure) {
		this.rrPressure = rrPressure;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeFloat(flPressure);
		dest.writeFloat(frPressure);
		dest.writeFloat(rlPressure);
		dest.writeFloat(rrPressure);
		dest.writeSerializable(createdDate);
	}

	public static Parcelable.Creator<RecordEntity> CREATOR = new Creator<RecordEntity>() {
		public RecordEntity[] newArray(int size) {
			return new RecordEntity[size];
		}

		public RecordEntity createFromParcel(Parcel source) {
			return new RecordEntity(source);
		}
	};

	@Override
	public String toString() {
		return "RecordEntity [id=" + id + ", flPressure=" + flPressure + ", frPressure=" + frPressure + ", rlPressure=" + rlPressure + ", rrPressure=" + rrPressure
				+ ", createdDate=" + createdDate + "]";
	}

}
