package com.scriptsbundle.adforest.utills;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.scriptsbundle.adforest.utills.Network.AuthenticationInterceptor;


public class UrlController {

    public static boolean loading = false;
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.MINUTES)
            .writeTimeout(60, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .callTimeout(60, TimeUnit.MINUTES)
            .build();

    // Warning for STRING_TOO_LARGE Please ignore it it wil not effect the project functionlaity.


  //  https://hawkapieyes.sook.ma/
   // public static String IP_ADDRESS = "https://faststream.to/";//Enter You Ip_Address here here
    public static String IP_ADDRESS = "https://hawkapieyes.sook.ma/";//Enter You Ip_Address here here
    public static String Purchase_code = "f6d3cb4e-647e-49c6-b845-f9c1f9daeaf4";//Enter the purchase code here
    public static String Custom_Security = "A4:3A:4C:14:FF:31:DA:3C:47:B7:E1:B0:A9:74:01:F6:FF:13:73:BC";//Enter the Custom Security code here

    public static final String LINKEDIN_CLIENT_ID = "Enter Your LINKEDIN_CLIENT_ID here";//Enter Your LINKEDIN_CLIENT_ID here
    public static final String LINKEDIN_CLIENT_SECRET = "Enter Your LINKEDIN_CLIENT_SECRET here";//Enter Your LINKEDIN_CLIENT_SECRET here
    public static final String LINKEDIN_REDIRECT_URL = "Enter Your LINKEDIN_REDIRECT_URL here";//Enter Your LINKEDIN_REDIRECT_URL here


    // Please don't change the below code without proper knowledge
    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    public static String Base_URL = IP_ADDRESS + "wp-json/adforest/v1/";
    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(Base_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(
            Class<S> serviceClass, String username, String password, Context context) {
        if (!TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(username, password);
            Log.d("authtoken1",authToken.toString());

            return createService(serviceClass, authToken, context);
        } else {
            Log.d("authtoken",serviceClass.toString());

            return createService(serviceClass);
        }
    }


    public static <S> S createServiceNoTimeout(Class<S> serviceClass, String authToken, Context context) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken, context);
            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
                httpClient.connectTimeout(0, TimeUnit.MINUTES);
                httpClient.readTimeout(0, TimeUnit.SECONDS);
                httpClient.writeTimeout(0, TimeUnit.SECONDS);
                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }
        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceNoTimeoutUP(
            Class<S> serviceClass, String username, String password, Context context) {
        if (!TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(username, password);
            Log.d("authtoken",authToken.toString());
            return createServiceNoTimeout(serviceClass, authToken, context);
        }

        Log.d("authtoken",serviceClass.toString());


        return createService(serviceClass);
    }

    public static <S> S createService(
            Class<S> serviceClass, final String authToken, Context context) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken, context);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }
        return retrofit.create(serviceClass);
    }

    public static Map<String, String> AddHeaders(Context context) {
        Map<String, String> map = new HashMap<>();
        if (SettingsMain.isSocial(context))
            map.put("AdForest-Login-Type", "social");
        if (SettingsMain.isOTP(context))
            map.put("AdForest-Login-Type", "otp");
        map.put("Purchase-Code", Purchase_code);
        map.put("custom-security", Custom_Security);
        map.put("Adforest-Lang-Locale", SettingsMain.getLanguageCode());
        map.put("Adforest-Request-From", "android");
        map.put("Content-Type", "application/json");
        map.put("Cache-Control", "max-age=640000");
        map.put("Adforest-Location-Id", SettingsMain.getlocationId());
        return map;
    }

    public static Map<String, String> UploadImageAddHeaders(Context context) {
        Map<String, String> map = new HashMap<>();
        if (SettingsMain.isSocial(context)) {
            map.put("AdForest-Login-Type", "social");
        }
        if (SettingsMain.isOTP(context))
            map.put("AdForest-Login-Type", "otp");
        map.put("Purchase-Code", Purchase_code);
        map.put("custom-security", Custom_Security);
        map.put("Adforest-Lang-Locale", SettingsMain.getLanguageCode());
        map.put("Adforest-Request-From", "android");
        map.put("Cache-Control", "max-age=640000");

        return map;
    }

}
