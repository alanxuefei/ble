package com.innoweaver.TyreTest.dataUtil;

public abstract class RecordImpl<T extends Object> implements Record<T> {
	private T r;
	private RecordController mController;

	// private boolean needToNotify = true;

	public void setValue(T r) {
		this.r = r;
		// needToNotify = false;
		mController.onRecordModified(this);
	};

	public T getValue() {
		return r;
	}

	public abstract void startNotifying();

	public abstract void stopNotifying();

	@Override
	public void assignTo(RecordController c) {
		mController = c;
		mController.addRecord(this);
	}

	public Record needToNotify() {
		// if (needToNotify) {
		// return this;
		// }
		if (r == null) {
			return this;
		}
		return null;
	}
}
