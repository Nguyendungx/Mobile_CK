package com.example.appbanthietbidientu.ultil;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import java.util.Map;

public interface ExchangeRateService {
    @GET("latest")
    Call<Map<String, Double>> getLatestExchangeRate(@Query("base") String baseCurrency);
}
