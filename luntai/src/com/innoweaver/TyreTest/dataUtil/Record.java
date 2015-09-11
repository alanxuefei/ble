package com.innoweaver.TyreTest.dataUtil;

public interface Record<T extends Object> {

	public void setValue(T r);

	public T getValue();

	public abstract void startNotifying();

	public abstract void stopNotifying();

	public abstract Record needToNotify();
	
	public void assignTo(RecordController c);
}
