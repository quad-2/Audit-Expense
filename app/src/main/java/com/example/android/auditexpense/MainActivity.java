package com.example.android.auditexpense;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;
    private Context context;
    private Intent intent;
    private List<ExpenseDetail> expenseDetailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expenseDetailList = new ArrayList<>();
        context = this;
        getExpenseData();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(llm);
        expenseAdapter = new ExpenseAdapter(expenseDetailList, context);
        recyclerView.setAdapter(expenseAdapter);

        intent = new Intent(context, BroadcastService.class);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            expenseDetailList.clear();
            List<ExpenseDetail> newList = intent.getExtras().getParcelableArrayList("UpdatedExpenses");
            assert newList != null;
            //Log.d("test", newList.toString());
            expenseAdapter = new ExpenseAdapter(newList, MainActivity.this);
            recyclerView.setAdapter(expenseAdapter);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        startService(intent);
        registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        stopService(intent);
    }


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
                List<ExpenseDetail> list = expenseList.getExpenses();
                for (ExpenseDetail expenseDetail : list) {
                    expenseDetailList.add(expenseDetail);
                }
                expenseDetailList = list;
                expenseAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                String internetErrorMessage = "Internet connection not available! Please check your internet connection.";
                Toast toast = Toast.makeText(context, internetErrorMessage, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}
