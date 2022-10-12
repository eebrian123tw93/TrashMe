package com.brianlu.trashme.home.profile;

import android.net.Uri;
import android.view.View;

import com.akiniyalocts.imgur_api.ImgurClient;
import com.akiniyalocts.imgur_api.model.Image;
import com.akiniyalocts.imgur_api.model.ImgurResponse;
import com.brianlu.trashme.api.user.UserService;
import com.brianlu.trashme.base.BasePresenter;
import com.brianlu.trashme.dto.UserProfileEditRequest;
import com.brianlu.trashme.model.User;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.mime.TypedFile;
import retrofit2.Response;

public class ProfilePresenter extends BasePresenter {

  private ProfileView view;
  private String localPhotoPath;

  ProfilePresenter(ProfileView view) {
    this.view = view;
  }

  void logout() {
    UserService.getInstance().logout();
    view.moveToLogin();
  }

  void setProfileData() {
    User user = UserService.getInstance().user;
    view.setProfileData(user);
  }

  void saveProfile(String name) {

    UserProfileEditRequest requestModel = new UserProfileEditRequest();
    requestModel.setName(name);

    if (localPhotoPath != null && !localPhotoPath.isEmpty()) {
      uploadPhoto(requestModel);
    } else {
      saveProfile(requestModel);
    }
  }

  private void saveProfile(UserProfileEditRequest requestModel) {
    view.onSetProgressBarVisibility(View.VISIBLE);
    UserService.getInstance()
        .uploadUser(requestModel, false)
        .subscribe(
            new Observer<Response<ResponseBody>>() {
              @Override
              public void onSubscribe(Disposable d) {}

              @Override
              public void onNext(Response<ResponseBody> responseBodyResponse) {
                view.onSetProgressBarVisibility(View.GONE);
                view.onSetMessage("儲存成功", FancyToast.SUCCESS);
                localPhotoPath = null;
              }

              @Override
              public void onError(Throwable e) {
                view.onSetProgressBarVisibility(View.GONE);
                view.onSetMessage(e.getMessage(), FancyToast.ERROR);
              }

              @Override
              public void onComplete() {}
            });

    if (requestModel.getName() != null)
      UserService.getInstance().user.setName(requestModel.getName());
    if (requestModel.getProfilePicUrl() != null)
      UserService.getInstance().user.setProfilePicUrl(requestModel.getProfilePicUrl());
  }

  private void uploadPhoto(UserProfileEditRequest requestModel) {
    view.onSetProgressBarVisibility(View.VISIBLE);
    File file = new File(localPhotoPath);
    TypedFile typedFile = new TypedFile("image/*", file);

    Callback<ImgurResponse<Image>> callback =
        new Callback<ImgurResponse<Image>>() {
          @Override
          public void success(
              ImgurResponse<Image> imageImgurResponse, retrofit.client.Response response) {

            if (imageImgurResponse.success) {
              view.onSetMessage("圖片上傳成功", FancyToast.SUCCESS);
              String link = imageImgurResponse.data.getLink();
              requestModel.setProfilePicUrl(link);
              saveProfile(requestModel);
            } else {
              view.onSetProgressBarVisibility(View.GONE);
            }
          }

          @Override
          public void failure(RetrofitError error) {
            view.onSetMessage(error.getMessage(), FancyToast.ERROR);
            view.onSetProgressBarVisibility(View.GONE);
          }
        };
    ImgurClient.getInstance()
        .uploadImage(
            typedFile, UUID.randomUUID().toString(), UUID.randomUUID().toString(), callback);
  }

  void setLocalPhotoPath(Uri uri) {
    localPhotoPath = getRealFilePath(uri);
  }

  void setLocalPhotoPath(String path) {
    localPhotoPath = path;
  }
}
