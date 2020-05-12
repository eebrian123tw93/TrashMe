package com.brianlu.trashme.home.remarks;

import com.brianlu.trashme.api.user.UserService;
import com.brianlu.trashme.base.BasePresenter;
import com.brianlu.trashme.model.User;
import com.shashank.sony.fancytoastlib.FancyToast;

public class RemarksPresenter extends BasePresenter {

  private RemarksView view;

  RemarksPresenter(RemarksView view) {
    this.view = view;
    String note = UserService.getInstance().noteRelay.getValue();
    view.onSetNote(note);
  }

  void saveNote(String note) {
    UserService.getInstance().saveNote(note);
    view.onSetMessage("已儲存備註", FancyToast.SUCCESS);
    view.onSaveNoteSuccess();
  }
}
