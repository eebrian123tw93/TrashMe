package com.brianlu.trashme.home.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.brianlu.trashme.R;
import com.brianlu.trashme.core.View.ViewExtension;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class LocationActivity extends AppCompatActivity implements ViewExtension, View.OnClickListener, EasyPermissions.PermissionCallbacks, LocationListener, MapListener {

  private Button saveButton;
  private MapView mapView;
  private LocationManager locationManager;
  private Geocoder geocoder;
  private TextView infoTextView;
  private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.WRITE_EXTERNAL_STORAGE,
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.ACCESS_COARSE_LOCATION};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Context context = getApplicationContext();
    Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));

    setContentView(R.layout.activity_location);
    saveButton = findViewById(R.id.save_button);
    mapView = findViewById(R.id.map);
    infoTextView = findViewById(R.id.info_textView);

    mapView.setTileSource(TileSourceFactory.MAPNIK);
    mapView.setMultiTouchControls(true);
    mapView.addMapListener(this);


    IMapController mapController = mapView.getController();
    mapController.setZoom(10.0);
    //23°58′26″N 120°58′55″E﻿ /
    GeoPoint startPoint = new GeoPoint(25.105497,121.597366);
    mapController.setCenter(startPoint);

    locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
    geocoder = new Geocoder(this);


    saveButton.setOnClickListener(this);

    if (!EasyPermissions.hasPermissions(this, permissions)) {
      EasyPermissions.requestPermissions(this, "地圖需要權限",
          100, permissions);
    }
    Log.i("LocationActivity","getLocation");
    getLastKnownLocation();

  }

  void getLastKnownLocation(){
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      Log.i("LocationActivity","checkSelfPermissionNotGrant");
      return;
    }
    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //<5>
    if (location != null) {
      onLocationChanged(location); //<6>
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    mapView.onResume();
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      Log.i("LocationActivity","checkSelfPermissionNotGrant");
      return;
    }
    Log.i("LocationActivity","requestLocationUpdates");
    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, this); //<7>
  }

  @Override
  protected void onPause() {
    super.onPause();
    mapView.onPause();
    locationManager.removeUpdates(this);
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);

    setRadius(saveButton, Color.parseColor("#4696f7"));
    saveButton.setTextColor(Color.WHITE);

  }


  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.save_button:
        break;
      case R.id.back_button:
        finish();
        break;
    }

  }

  @Override
  public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

  }

  @Override
  public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

  }

  @Override
  public void onLocationChanged(Location location) {
    Log.i("LocationActivity","onLocationChanged");
    IMapController mapController = mapView.getController();
    mapController.setZoom(18.0);
    //23°58′26″N 120°58′55″E﻿ /
    GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
    mapController.animateTo(startPoint);

    try {
      List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10); //<10>
      if (addresses.size() > 0) {
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        infoTextView.setText(address);
      }

    } catch (IOException e) {
      Log.e("LocateMe", "Could not get Geocoder data", e);
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

  @Override
  public boolean onScroll(ScrollEvent event) {
    return false;
  }

  @Override
  public boolean onZoom(ZoomEvent event) {

    return false;
  }
}
