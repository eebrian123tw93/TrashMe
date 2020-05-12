package com.brianlu.trashme.home.remarks;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.brianlu.trashme.R;
import com.brianlu.trashme.core.View.ViewExtension;

public class RemarksActivity extends AppCompatActivity
    implements RemarksView, ViewExtension, View.OnClickListener {

  private RemarksPresenter presenter;

  private Button saveButton;
  private EditText noteEditText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_remarks);

    saveButton = findViewById(R.id.save_button);
    saveButton.setOnClickListener(this);

    presenter = new RemarksPresenter(this);
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);

    setRadius(saveButton, Color.rgb(254, 59, 91));
    saveButton.setTextColor(Color.WHITE);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.save_button:
        String note = noteEditText.getText().toString();
        presenter.saveNote(note);
        break;
    }
  }

  @Override
  public void onSetNote(String note) {
    noteEditText.setText(note);
  }
}
