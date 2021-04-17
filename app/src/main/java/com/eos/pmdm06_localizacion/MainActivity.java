package com.eos.pmdm06_localizacion;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.nio.file.AccessDeniedException;

public class MainActivity extends AppCompatActivity {

	private LocationManager locationManager;
	private PhoneLocation currentLocation = new PhoneLocation(0, 0);
	TextView currentLatitude;
	TextView currentLength;
	TextView test;

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String[] permissions, int[] grantResults) {

		if (grantResults.length > 0
				&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			startLocationManager();
		} else {
			Toast.makeText(MainActivity.this, "Si no acepta los permisos no puede usar la aplicación",
						   Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button button = findViewById(R.id.button);
		currentLatitude = findViewById(R.id.current_latitude_number);
		currentLength = findViewById(R.id.current_length_number);

		EditText targetLatitude = findViewById(R.id.target_latitude_number);
		EditText targetLength = findViewById(R.id.target_length_number);
		test = findViewById(R.id.test);
		if (locationManager == null) {
			startLocationManager();
		}
		try {
			currentLocation = updateLocation();
			currentLatitude.setText(currentLocation.getLatitude());
			currentLength.setText(currentLocation.getLength());

			test.setText(String.format("Longitud: %s, latitud: %s", currentLatitude.getText(),
									   currentLength.getText()));
		} catch (AccessDeniedException e) {
			System.exit(1);
		}

		button.setOnClickListener(v -> {
			if (checkInput(targetLatitude, targetLatitude, "No se puede dejar la latitud objetivo en blanco"))
				return;
			if (checkInput(targetLatitude, targetLength, "No se puede dejar la longitud objetivo en blanco"))
				return;

			targetLatitude.clearFocus();
			targetLength.clearFocus();
			Toast.makeText(MainActivity.this, "Ha introducido los datos correctamente",
						   Toast.LENGTH_LONG).show();
		});
	}

	private void startLocationManager() {

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(true);
		criteria.setBearingRequired(false);
		criteria.setHorizontalAccuracy(Criteria.ACCURACY_LOW);
		criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
		criteria.setSpeedRequired(false);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		criteria.setCostAllowed(true);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		String provider = locationManager.getBestProvider(criteria, true);

		locationManager.requestLocationUpdates(provider, 3000, 1, locationListener);
	}

	private PhoneLocation updateLocation() throws AccessDeniedException {

		if (!checkPermission())
			throw new AccessDeniedException("No se tienen los permisos necesarios");

		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location == null) {
			Log.e("Fallo localización", "No recibe señal.");
		}

		currentLocation = new PhoneLocation(location.getLatitude(), location.getLongitude());

		updateCurrentLocation();
		return currentLocation;
	}

	private boolean checkPermission() {
		if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED
				&& checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{
					Manifest.permission.ACCESS_COARSE_LOCATION,
					Manifest.permission.ACCESS_FINE_LOCATION}, 1);
			Toast.makeText(MainActivity.this, "Esta aplicación necesita permisos de ubicación para poder funcionar",
						   Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private void updateCurrentLocation() {
		currentLatitude.setText(currentLocation.getLatitude());
		currentLength.setText(currentLocation.getLength());
		test.setText(String.format("Longitud: %s, latitud: %s", currentLatitude.getText(),
								   currentLength.getText()));

	}

	private boolean checkInput(EditText targetLatitude, EditText targetLength, String s) {
		if (targetLatitude == null
				|| TextUtils.isEmpty(targetLength.getText().toString())) {
			Toast.makeText(MainActivity.this, s,
						   Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

	private final LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			// La ubicación ha cambiado, luego hay que actualizar la información
			// mostrada al usuario
			try {
				Toast.makeText(MainActivity.this, "La ubicación se ha actualizado",
							   Toast.LENGTH_SHORT).show();
				updateLocation();
			} catch (AccessDeniedException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}
	};
}