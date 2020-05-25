package com.brianlu.trashme.home.pickerlocation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

import com.brianlu.trashme.R;
import com.brianlu.trashme.home.location.LocationPresenter;
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

public class PickerLocationActivity extends AppCompatActivity implements MapListener, PickerLocationView {


  private MapView mapView;
  private TextView infoTextView;
  private Marker myMarker;
  private Marker pickerMarker;
  private PickerPresenter presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_picker_location);

    Context context = getApplicationContext();
    Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
    mapView = findViewById(R.id.map);
    infoTextView = findViewById(R.id.info_textView);

    mapView.setTileSource(TileSourceFactory.MAPNIK);
    mapView.setMultiTouchControls(true);
    mapView.addMapListener(this);

    presenter = new PickerPresenter(this);
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
  public void onSetLocation(LocationModel model) {
    IMapController mapController = mapView.getController();
    mapController.setZoom(18.0);
    //23°58′26″N 120°58′55″E﻿ /
    GeoPoint startPoint = new GeoPoint(model.getLatitude(), model.getLongitude());
    mapController.animateTo(startPoint);


    mapView.getOverlays().remove(myMarker);
    myMarker = new Marker(mapView);
    myMarker.setPosition(startPoint);
    myMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
    mapView.getOverlays().add(myMarker);
  }

  @Override
  public void onSetPickerLocation(LocationModel model) {
    IMapController mapController = mapView.getController();
    mapController.setZoom(18.0);
    GeoPoint startPoint = new GeoPoint(model.getLatitude(), model.getLongitude());
    mapController.animateTo(startPoint);

    mapView.getOverlays().remove(pickerMarker);
    pickerMarker = new Marker(mapView);
    pickerMarker.setPosition(startPoint);
    pickerMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
    mapView.getOverlays().add(pickerMarker);
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
