package com.eos.pmdm06_localizacion;

import java.text.DecimalFormat;

public class PhoneLocation {

	private final double latitude;
	private final double length;

	private final int MAX_DECIMAL = 15;

	public PhoneLocation(double latitude, double length) {
		this.latitude = latitude;
		this.length = length;
	}

	public String getLatitude() {
		return formatDecimals(latitude, MAX_DECIMAL);
	}

	public String getLength() {
		return formatDecimals(length, MAX_DECIMAL);
	}

	private String formatDecimals(double length, int decimals) {
		StringBuilder decimalFormat = new StringBuilder("#.");
		for (int i = 0; i < decimals; i++) {
			decimalFormat.append("#");
		}
		DecimalFormat df = new DecimalFormat(decimalFormat.toString());
		return df.format(length);
	}

}
