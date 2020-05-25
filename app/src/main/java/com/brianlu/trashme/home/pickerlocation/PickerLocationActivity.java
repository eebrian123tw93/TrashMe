package com.brianlu.trashme.home.pickerlocation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.brianlu.trashme.R;
import com.brianlu.trashme.model.LocationModel;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class PickerLocationActivity extends AppCompatActivity implements MapListener, PickerLocationView, View.OnClickListener {


  private MapView mapView;
  private TextView infoTextView;
  private ItemizedIconOverlay<OverlayItem> myOverlay;
  private ItemizedIconOverlay<OverlayItem> pickerOverlay;
  private PickerLocationPresenter presenter;

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

    presenter = new PickerLocationPresenter(this);


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

    mapView.getOverlays().remove(myOverlay);
    Drawable d = getResources().getDrawable(R.drawable.user_location);
    Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
    Bitmap mmm =  Bitmap.createScaledBitmap(bitmap, (int) (30 * getResources().getDisplayMetrics().density), (int) (30 * getResources().getDisplayMetrics().density),true);
    Drawable marker = new BitmapDrawable(getResources(),mmm);
    myOverlay = new ItemizedIconOverlay<>(
        new ArrayList<>(), marker, null,
        null);
    // gc: last GeoPoint
    OverlayItem overlayItem = new OverlayItem(null, null, startPoint);
    myOverlay.addItem(overlayItem);
    mapView.getOverlays().add(myOverlay);
  }

  @Override
  public void onSetPickerLocation(LocationModel model) {

    GeoPoint startPoint = new GeoPoint(model.getLatitude(), model.getLongitude());
    mapView.getOverlays().remove(pickerOverlay);
    Drawable d = getResources().getDrawable(R.drawable.picker_location);
    Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
    Drawable marker = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, (int) (30 * getResources().getDisplayMetrics().density), (int) (30 * getResources().getDisplayMetrics().density), true));
    pickerOverlay = new ItemizedIconOverlay<>(
        new ArrayList<>(), marker, null,
        null);
    // gc: last GeoPoint
    OverlayItem overlayItem = new OverlayItem(null, null, startPoint);
    pickerOverlay.addItem(overlayItem);
    mapView.getOverlays().add(pickerOverlay);
    mapView.invalidate();

  }

  @Override
  public void onSetInfo(String info) {
    infoTextView.setText(info);
  }


  @Override
  public boolean onScroll(ScrollEvent event) {
    return false;
  }

  @Override
  public boolean onZoom(ZoomEvent event) {
    return false;
  }

  public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
  {
    int width = bm.getWidth();
    int height = bm.getHeight();
    float scaleWidth = ((float) newWidth) / width;
    float scaleHeight = ((float) newHeight) / height;
    // create a matrix for the manipulation
    Matrix matrix = new Matrix();
    // resize the bit map
    matrix.postScale(scaleWidth, scaleHeight);
    // recreate the new Bitmap
    return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
  }

  @Override
  public void onClick(View v) {
    finish();
  }
}
