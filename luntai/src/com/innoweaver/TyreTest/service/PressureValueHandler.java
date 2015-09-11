package com.innoweaver.TyreTest.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.innoweaver.TyreTest.entity.CarEntity;
import com.innoweaver.TyreTest.util.UnitTransferUtil;

public class PressureValueHandler implements InvocationHandler {
	private Object target;
	private int count;

	public PressureValueHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object raw = method.invoke(target, args);
		float result = (Float) raw;

		switch (count) {
		case 0:
			break;
		case 1:
			result = UnitTransferUtil.fromBarToKpa(result);
			break;
		case 2:
			result = UnitTransferUtil.fromBarToPsi(result);
			break;
		default:
			break;
		}
		return result;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
