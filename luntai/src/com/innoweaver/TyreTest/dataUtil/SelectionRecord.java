package com.innoweaver.TyreTest.dataUtil;

import java.util.Observable;

import com.innoweaver.TyreTest.entity.CarEntity;
import com.innoweaver.TyreTest.entity.Temperature;

public abstract class SelectionRecord extends Observable {
	public abstract void setCar(CarEntity car);

	public abstract CarEntity getSelectedCar();

	public abstract void setTemperature(Temperature temp);

	public abstract Temperature getTemperature();
	
	public abstract void setTyrePressure(int tyreIndex, double pressure);
	
	public abstract double getTyrePressure(int typreIndex);

}
