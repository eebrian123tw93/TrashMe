package com.brianlu.trashme.Register;
import com.brianlu.trashme.Base.BaseView;

public interface RegisterView extends BaseView {
    void onClearText();

    void onRegisterResult(boolean result);

    void onSetProgressBarVisibility(int visibility);

}
