package com.eos.pmdm06_localizacion;

import java.text.DecimalFormat;

public class PhoneLocation {

	private final double latitude;
	private final double length;

	private final int MAX_DECIMAL = 8;

	public PhoneLocation(double latitude, double length) {
		this.latitude = latitude;
		this.length = length;
	}

	public PhoneLocation(String latitude, String length)
			throws IllegalArgumentException {
		this.latitude = validateInput(latitude);
		this.length = validateInput(length);
	}

	private double validateInput(String textNumber) {
		Double formattedNumber = null;
		try {
			formattedNumber = Double.parseDouble(textNumber);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Solo se aceptan números");
		}
		return formattedNumber;
	}

	public String getLatitudeString() {
		return formatDecimals(latitude, MAX_DECIMAL);
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLength() {
		return length;
	}

	public String getLengthString() {
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
