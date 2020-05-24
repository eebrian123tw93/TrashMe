package com.brianlu.trashme.base;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;

public class BasePresenter {
  public static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
  private static final String USER_PROFILE = "user_profile";

  protected Handler handler;

  protected Context context;

  protected BasePresenter() {
    this.context = BaseApplication.getContext();
    handler = new Handler();
  }

  public String getRealFilePath( final Uri uri) {
    if (null == uri) return null;
    final String scheme = uri.getScheme();
    String data = null;
    if (scheme == null)
      data = uri.getPath();
    else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
      data = uri.getPath();
    } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
      Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
      if (null != cursor) {
        if (cursor.moveToFirst()) {
          int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
          if (index > -1) {
            data = cursor.getString(index);
          }
        }
        cursor.close();
      }
    }
    return data;
  }
}
