package com.brianlu.trashme.core;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/** Created by max on 2018/1/11. */
public class URLRetrofitBuilder {

  public URLRetrofitBuilder() {}

  public Retrofit buildRetrofit(String apiPath) {

    // 最後一定要是 "/" 結尾否則會報錯
    // excludeFieldsWithoutExposeAnnotation，Model 中需個別指定序列化、反序列化欄位，加上 @Expose 標籤

    return new Retrofit.Builder()
        .baseUrl(apiPath) // 最後一定要是 "/" 結尾否則會報錯
        .client(new CommonPairs().getUnsafeOkHttpClient())
        // excludeFieldsWithoutExposeAnnotation，Model 中需個別指定序列化、反序列化欄位，加上 @Expose 標籤
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();
  }
}
