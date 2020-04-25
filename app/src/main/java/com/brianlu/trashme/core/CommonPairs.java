package com.brianlu.trashme.core;

import android.annotation.SuppressLint;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

class CommonPairs {

    private static final String TAG = "===CommonPairs";
    private static final int STATUS_CODE_AUTH_FAIL = 401;
    private static final Charset UTF8 = StandardCharsets.UTF_8;

    /**
     * OkHttpClient trustAllCerts.
     *
     * @return OkHttpClient
     */
    OkHttpClient getUnsafeOkHttpClient() {
        try {
            MyTrustManager myTrustManager = new MyTrustManager();
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{myTrustManager};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient =
                    okHttpClient
                            .newBuilder()
                            //              .retryOnConnectionFailure(true)
                            .sslSocketFactory(sslSocketFactory, myTrustManager)
                            .hostnameVerifier(new TrustAllHostnameVerifier())
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 實現 X509TrustManager 接口.
     */
    static class MyTrustManager implements X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    // 實現 HostnameVerifier 接口
    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @SuppressLint("BadHostnameVerifier")
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


}

