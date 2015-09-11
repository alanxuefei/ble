package com.innoweaver.TyreTest.dataUtil;

@Deprecated
public class RecordControllerImpl implements RecordController {
	Record notifyingRec;

	public void onRecordModified(Record r) {
		if (notifyingRec != null) {
			// noti
		}

		if (r != null) {
			notifyingRec = r;
			r.startNotifying();
		}
	}

	@Override
	public void addRecord(Record r) {
		
	}

}
