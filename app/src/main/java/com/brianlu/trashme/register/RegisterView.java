package com.brianlu.trashme.register;
import com.brianlu.trashme.base.BaseView;

interface RegisterView extends BaseView {
    void onClearText();

    void onRegisterResult(boolean result);

    void onSetProgressBarVisibility(int visibility);

}
