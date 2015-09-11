package com.innoweaver.TyreTest.service;

import java.lang.reflect.Method;


public class PressureStandardValueHandler extends PressureValueHandler {

	private Float temperature;

	public PressureStandardValueHandler(Object target, Float temperature) {
		super(target);
		this.temperature = temperature;
	}

	// P1=（0.000005658247635* (C* 9/5 + 32)3 - 0.001549619138215*(C* 9/5 + 32)2
	// + 0.245528631148265*(C* 9/5 + 32) +
	// 12.425843725392175）*P/24.727033220754237

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Float result = (Float) super.invoke(proxy, method, args);
		// first formula deprecated;
		// if (temperature != null) {
		// result = result * (temperature + 248) / 273;
		// }
		// second formula
		if (temperature != null) {
			result = (float) (/**/(0.000005658247635 * Math.pow(temperature * 9 / 5 + 32, 3) - 0.001549619138215 * Math.pow(temperature * 9 / 5 + 32, 2) + 0.245528631148265
					* (temperature * 9 / 5 + 32) + 12.425843725392175)/**/
					* result / 24.727033220754237);
		}

		return result;
	}

	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}

}
