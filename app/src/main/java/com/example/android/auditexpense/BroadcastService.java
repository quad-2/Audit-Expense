package com.example.android.auditexpense;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class BroadcastService extends Service {

    public static final String BROADCAST_ACTION = "com.example.android.auditexpense";
    private final Handler handler = new Handler();
    Intent intent;


    
    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second

        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            getExpenseData();
            handler.postDelayed(this, 30000); // refreshes after 30 seconds
        }
    };

    private void getExpenseData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("https://jsonblob.com")
                .build();

        JsonBlobApi api = restAdapter.create(JsonBlobApi.class);
        api.getExpenseData(new Callback<ExpenseList>() {
            @Override
            public void success(ExpenseList expenseList, Response response) {
                intent.putParcelableArrayListExtra("UpdatedExpenses", (ArrayList<? extends Parcelable>) expenseList.getExpenses());
                sendBroadcast(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                String errorMessage = "Internet connection not available! Please check your internet connection.";
                Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
