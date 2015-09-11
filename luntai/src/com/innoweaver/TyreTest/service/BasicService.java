package com.innoweaver.TyreTest.service;

import android.content.Context;

import com.innoweaver.TyreTest.db.DatabaseHelper;

public class BasicService {
	protected Context context;

	public BasicService(Context context) {
		this.context = context;
	}

	protected DatabaseHelper getDatabaseHelper() {
		return new DatabaseHelper(context);
	}

	protected void closeHelper(DatabaseHelper helper) {
		helper.close();
	}
}
