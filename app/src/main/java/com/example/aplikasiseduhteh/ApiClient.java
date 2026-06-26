package com.example.aplikasiseduhteh;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
<<<<<<< HEAD
    public static final String base_url = "http://10.0.2.2:8000/";
>>>>>>> 3bce02dc90321041abe1c3fa9d28a2fd9c5a446b
    public static final String gambar_url = base_url + "gambar_teh/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
