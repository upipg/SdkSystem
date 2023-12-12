package com.app.sdkupiiii;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AC {



    public static Retrofit PB() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                //.addInterceptor(interceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS).build();

//http://api.rusumitsolutions.com/api/rits/LoginUser
        //https://medijunction.co.in/api/User/RegisterPatient
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GL.UL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                //.client(client)
                .build();


        return retrofit;
    }

}
