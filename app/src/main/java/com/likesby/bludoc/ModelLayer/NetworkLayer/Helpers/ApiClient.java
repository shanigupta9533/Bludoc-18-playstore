package com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers;


import com.likesby.bludoc.ModelLayer.NetworkLayer.BasicAuthInterceptor;
import com.likesby.bludoc.ServerConnect.ServerConnect;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.likesby.bludoc.constants.ApplicationConstant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient
{
   // private static final String BASE_URL = "http://192.168.1.105/Tracksido/api/Api/";
   // private static final String BASE_URL = "http://192.168.42.166/Tracksido/api/Api/";
   // public static final String BASE_IMAGE_URL = "http://192.168.1.104/Tracksido/assets/uploaded-images/";

  // private static final String BASE_URL = "https://ensivosolutions.com/Tracksido/api/Api/";
    //public static final String BASE_IMAGE_URL = "http://192.168.1.104/Tracksido/assets/uploaded-images/";

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Gson gson = new GsonBuilder()
                    .setLenient().create();

    //region Handle Logging Interceptor
    static {
        httpClient.connectTimeout(20, TimeUnit.SECONDS) // connect timeout
                .writeTimeout(60, TimeUnit.SECONDS) // write timeout
                .callTimeout(60, TimeUnit.SECONDS)   //call timeout
                .readTimeout(60, TimeUnit.SECONDS); // read timeout


        if (ApplicationConstant.DEBUG) {
            //loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            //loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(loggingInterceptor);
        }
        httpClient.addInterceptor(new BasicAuthInterceptor());
        //loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        /*httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                *//*if (context == null) {
                    request = request
                            .newBuilder()
                            .build();
                }else { *//*
                    request = request
                            .newBuilder()
                            .addHeader("Authorization", "Bearer "+ "cw00ggcsw4co0g804gcggwo088g4kokgk88sso4s")
                            .build();
               // }
                return chain.proceed(request);
            }
        });*/


       // loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

    }
    //endregion

    //region Init Retrofit Builder
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ServerConnect.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()); //NOTE: Jon Bott - Add this line for RxRetrofit
    //endregion

    /*//region Init Service Without Auth
    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass);
    }*/
    //endregion

    //region Init Service With Auth
    public static <S> S createService(Class<S> serviceClass) {
       // if (authToken != null && userId !=null) {
            addRequestHeaders();
       // }


        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
    //endregion

    //region Init RequestHeaders
    public static void addRequestHeaders() {
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder builder = originalRequest.newBuilder()
                        .header("X-API-KEY", "cw00ggcsw4co0g804gcggwo088g4kokgk88sso4s")
                        .method(originalRequest.method(), originalRequest.body());
               // builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
               //builder.addHeader("x-api-key", "cw00ggcsw4co0g804gcggwo088g4kokgk88sso4s");
                Request modifiedRequest = builder.build();
                return chain.proceed(modifiedRequest);
            }
        });
    }
    //endregion
}


