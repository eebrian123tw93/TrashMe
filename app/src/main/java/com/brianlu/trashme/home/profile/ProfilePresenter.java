package com.brianlu.trashme.home.profile;

import com.brianlu.trashme.api.user.UserService;
import com.brianlu.trashme.base.BasePresenter;
import com.brianlu.trashme.dto.UserProfileEditRequest;
import com.shashank.sony.fancytoastlib.FancyToast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ProfilePresenter extends BasePresenter {

  private ProfileView view;

  ProfilePresenter(ProfileView view) {
    this.view = view;
  }

  void logout() {
    UserService.getInstance().logout();
    view.moveToLogin();
  }

  void saveProfile(String userPassword, String name, String profilePicUrl) {

    UserProfileEditRequest userProfileEditRequest = new UserProfileEditRequest();
    userProfileEditRequest.setPassword(userPassword);
    userProfileEditRequest.setName(name);
    userProfileEditRequest.setProfilePicUrl(profilePicUrl);

    UserService.getInstance()
        .uploadUser(userProfileEditRequest, false)
        .subscribe(
            new Observer<Response<ResponseBody>>() {
              @Override
              public void onSubscribe(Disposable d) {}

              @Override
              public void onNext(Response<ResponseBody> responseBodyResponse) {
                view.onSetMessage("儲存成功", FancyToast.SUCCESS);
              }

              @Override
              public void onError(Throwable e) {
                view.onSetMessage(e.getMessage(), FancyToast.ERROR);
              }

              @Override
              public void onComplete() {}
            });
  }
}
