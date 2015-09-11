package com.innoweaver.TyreTest.dataUtil;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

@SuppressWarnings("hiding")
public class RecordManager<RecordImpl> implements RecordController {
	private final String TAG = RecordManager.class.getSimpleName();

	// private RecordController mController;
	private List<Record> mRecords;
	private Record notifyingRec;

	public RecordManager() {
		// mController = controller;
		mRecords = new ArrayList<Record>();
	}

	// public void setValue(RecordImpl r) {
	//
	// }
	//
	// public RecordImpl getValue() {
	// return null;
	// }

	// public void startNotifying() {
	// // if (readyToFire == null) {
	// // return;
	// // }
	// // readyToFire.startNotifying();
	// }

	// public void stopNotifying() {
	// // if (readyToFire == null) {
	// // return;
	// // }
	// // readyToFire.stopNotifying();
	// }

	// public Record dataEntered() {
	// for (int i = 0; i < mRecords.size(); i++) {
	// Record r = mRecords.get(i);
	// if (r.dataEntered() == null) {
	// // readyToFire = r;
	// return r;
	// }
	// }
	//
	// return null;
	// }

	@Override
	public void addRecord(Record r) {
		mRecords.add(r);
	}

	public void onRecordModified(Record r) {
		Log.e(TAG, "go");
		if (notifyingRec != null) {
			notifyingRec.stopNotifying();
		}

		for (int i = 0; i < mRecords.size(); i++) {
			Record rec = mRecords.get(i);
			if (rec.needToNotify() != null) {
				// readyToFire = r;
				notifyingRec = rec;
				notifyingRec.startNotifying();
				break;
			}
		}
		// if (r != null) {
		// notifyingRec.startNotifying();
		// }

	}
}
