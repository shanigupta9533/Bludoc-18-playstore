package com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers;

import androidx.annotation.NonNull;

import com.likesby.bludoc.ModelLayer.NetworkLayer.BasicAuthInterceptor;
import com.likesby.bludoc.ServerConnect.ServerConnect;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //private static final String BASE_URL = "http://192.168.1.105/Tracksido/api/Api/";
    // private static final String BASE_URL = "http://192.168.42.166/Tracksido/api/Api/";
    // public static final String BASE_IMAGE_URL = "http://192.168.1.104/Tracksido/assets/uploaded-images/";
    // private static final String BASE_URL = "https://ensivosolutions.com/Tracksido/api/Api/";

    private static Retrofit retrofit = null;
    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

    private RetrofitClient(){

    }  //private constructor.


    public static Retrofit getInstance(){
        if (retrofit == null){

            //if there is no instance available... create new one

            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    requestBuilder.header("Content-Type", "application/json");
                    requestBuilder.header("Accept", "application/json");
                    requestBuilder .header("X-API-KEY", "cw00ggcsw4co0g804gcggwo088g4kokgk88sso4s");
                    return chain.proceed(requestBuilder.build());
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(new BasicAuthInterceptor());
            httpClientBuilder.addInterceptor(loggingInterceptor);

            OkHttpClient httpClient = httpClientBuilder.build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(ServerConnect.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient)
                    .build();
        }

        return retrofit;
    }
}
