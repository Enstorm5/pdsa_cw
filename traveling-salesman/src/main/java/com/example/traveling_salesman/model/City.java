package com.example.traveling_salesman.model;

import java.util.Locale;

public enum City {
	A,
	B,
	C,
	D,
	E,
	F,
	G,
	H,
	I,
	J;

	public static City fromName(String value) {
		if (value == null) {
			throw new IllegalArgumentException("City value must not be null");
		}
		return City.valueOf(value.trim().toUpperCase(Locale.ROOT));
	}
}
