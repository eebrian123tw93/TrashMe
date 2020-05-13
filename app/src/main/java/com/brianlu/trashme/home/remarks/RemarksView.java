package com.brianlu.trashme.home.remarks;

import com.brianlu.trashme.base.BaseView;

public interface RemarksView extends BaseView {
  void onSetNote(String note);
  void onSaveNoteSuccess();
}
