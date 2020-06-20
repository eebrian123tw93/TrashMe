package com.brianlu.trashme.home.location;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.brianlu.trashme.R;
import com.brianlu.trashme.core.View.ViewExtension;
import com.brianlu.trashme.core.View.dialog.LoadingDialog;
import com.brianlu.trashme.model.LocationModel;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class LocationActivity extends AppCompatActivity implements ViewExtension,
    View.OnClickListener,
    EasyPermissions.PermissionCallbacks,
    LocationListener,
    MapListener,
    LocationView {

  private Button saveButton;
  private MapView mapView;
  private LocationManager locationManager;
  private Geocoder geocoder;
  private TextView infoTextView;
  private Marker myMarker;

  private LoadingDialog loadingDialog;

  private LocationPresenter presenter;
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

    loadingDialog = new LoadingDialog(this);

    presenter = new LocationPresenter(this);

  }

  @Override
  protected void onResume() {
    super.onResume();
    mapView.onResume();

  }

  @Override
  protected void onPause() {
    super.onPause();
    mapView.onPause();

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
        presenter.updateLocation();
        break;
      case R.id.get_location_button:
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
          Log.i("LocationActivity", "checkSelfPermissionNotGrant");
          return;
        }
        Log.i("LocationActivity", "requestLocationUpdates");
        loadingDialog.showLoading();
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER, 100, 10, this); // <7>
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
    loadingDialog.dismissLoading();
    locationManager.removeUpdates(this);
    Log.i("LocationActivity","onLocationChanged");
    IMapController mapController = mapView.getController();
    mapController.setZoom(18.0);
    GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
    mapController.animateTo(startPoint);

    try {
      List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10); //<10>
      if (addresses.size() > 0) {
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        LocationModel model = new LocationModel();
        model.setLongitude(location.getLongitude());
        model.setLatitude(location.getLatitude());
        model.setLocationName(address);
        model.setUserType("CONSUMER");
        presenter.setLocationModel(model);
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

  @Override
  public void onSetLocation(LocationModel model) {
    infoTextView.setText(model.getLocationName());
    IMapController mapController = mapView.getController();
    mapController.setZoom(18.0);
    GeoPoint startPoint = new GeoPoint(model.getLatitude(), model.getLongitude());
    mapController.animateTo(startPoint);


    mapView.getOverlays().remove(myMarker);
    myMarker = new Marker(mapView);
    myMarker.setPosition(startPoint);
    myMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
    mapView.getOverlays().add(myMarker);
  }

  @Override
  public void onSaveLocationSuccess() {
    finish();
  }
}
