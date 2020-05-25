package com.brianlu.trashme.home.orders;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brianlu.trashme.R;

public class OrdersActivity extends AppCompatActivity implements OrdersView, View.OnClickListener {

  private RecyclerView recyclerView;

  OrdersPresenter presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_orders);

    recyclerView = findViewById(R.id.recycler_view);
    final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setOrientation(RecyclerView.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);

    presenter = new OrdersPresenter(this);
    presenter.getOrders();
  }

  @Override
  public void setAdapter(RecyclerView.Adapter adapter) {
    recyclerView.setAdapter(adapter);
  }

  @Override
  public void onClick(View v) {
    finish();
  }
}
