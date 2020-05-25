package com.brianlu.trashme.home.orders.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brianlu.trashme.R;
import com.brianlu.trashme.dto.PickupOrderInfo;

import java.util.List;

public class OrdersRVAdapter extends RecyclerView.Adapter<OrdersRVAdapter.ViewHolder> {

  private OrdersRVPresenter presenter;

  public OrdersRVAdapter() {

    presenter = new OrdersRVPresenter();
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v =
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order, viewGroup, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
    presenter.onBindViewHolder(viewHolder, position);
  }

  @Override
  public int getItemCount() {
    return presenter.getItemCount();
  }

  public void addModel(PickupOrderInfo model) {
    presenter.addModel(model);
    notifyDataSetChanged();
  }

  public void addModels(List<PickupOrderInfo> models) {
    presenter.addModels(models);
    notifyDataSetChanged();
  }

  public void clearAll() {
    presenter.clearAll();
    notifyDataSetChanged();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder implements OrdersVHView {

    private TextView timeTextView;
    private TextView statusTextView;
    private TextView typeTextView;
    private TextView weightTextView;
    private TextView pickerUserTextView;
    private ImageView imageView;

    ViewHolder(@NonNull View itemView) {
      super(itemView);

      timeTextView = itemView.findViewById(R.id.time_textView);
      statusTextView = itemView.findViewById(R.id.status_textView);
      typeTextView = itemView.findViewById(R.id.type_textView);
      weightTextView = itemView.findViewById(R.id.weight_textView);
      pickerUserTextView = itemView.findViewById(R.id.picker_user_textView);
      imageView = itemView.findViewById(R.id.imageView);
    }

    @Override
    public void setTime(String title) {
      timeTextView.setText(title);
    }

    @Override
    public void setStatus(String status) {
      statusTextView.setText(status);
    }

    @Override
    public void setStatusColor(int color) {
      statusTextView.setTextColor(color);
    }

    @Override
    public void setType(String type) {
      typeTextView.setText(type);
    }

    @Override
    public void setTypeImage(int resource) {
      imageView.setImageResource(resource);
    }

    @Override
    public void setPickerUser(String pickerUser) {
      pickerUserTextView.setText(pickerUser);
    }

    @Override
    public void setWeight(String weight) {
      weightTextView.setText(weight);
    }
  }
}
