package com.example.android.auditexpense;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;


public interface JsonBlobApi {

    @GET("/api/jsonBlob/56ab65a2e4b01190df4c2221")
    public void getExpenseData(Callback<ExpenseList> response);

    @PUT("/api/jsonBlob/56ab65a2e4b01190df4c2221")
    public void updateExpenseState(@Body ExpenseList list, Callback<ExpenseList> response);
}
