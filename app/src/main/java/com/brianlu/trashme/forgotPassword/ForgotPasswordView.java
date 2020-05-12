package com.brianlu.trashme.forgotPassword;

import com.brianlu.trashme.base.BaseView;

public interface ForgotPasswordView extends BaseView {
    void onForgotPassword(boolean result);

    void onSetProgressBarVisibility(int visibility);

}
