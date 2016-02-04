package com.example.android.auditexpense;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<ExpenseDetail> list;
    private int rechargeCardColor;
    private int taxiCardColor;
    private ExpenseList expenseList;
    private Context context;


    ExpenseAdapter(List<ExpenseDetail> list, Context context) {
        this.list = list;
        this.context = context;
        expenseList = new ExpenseList(list);
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expense_row_layout, viewGroup, false);
        ExpenseViewHolder evh = new ExpenseViewHolder(view);
        return evh;
    }

    @Override
    public void onBindViewHolder(final ExpenseViewHolder viewHolder, final int index) {
        ExpenseDetail expenses = list.get(index);
        viewHolder.expenseId.setText(expenses.getExpenseId());
        viewHolder.amount.setText(Integer.toString(expenses.getAmount()));
        viewHolder.description.setText(expenses.getExpenseDescription());
        viewHolder.timeAndDate.setText(expenses.getTime());
        viewHolder.category.setText(expenses.getCategory());

        if (expenses.getCategory().equalsIgnoreCase("recharge")) {
            rechargeCardColor = Color.parseColor("#BBDEFB");   //color of recharge category recyclerView card
            viewHolder.expenseDetailCard.setCardBackgroundColor(rechargeCardColor);
        } else {
            taxiCardColor = Color.parseColor("#DCEDC8");   //color of taxi category recyclerView card
            viewHolder.expenseDetailCard.setCardBackgroundColor(taxiCardColor);
        }

        if (expenses.getState().equalsIgnoreCase("verified")) {
            viewHolder.verifiedButton.setChecked(true);
        } else if (expenses.getState().equalsIgnoreCase("unverified")) {
            viewHolder.unverifiedButton.setChecked(true);
        } else if (expenses.getState().equalsIgnoreCase("fraud")) {
            viewHolder.fraudButton.setChecked(true);
        }

        viewHolder.verifiedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.verifiedButton.isChecked()) {
                    list.get(index).setState("verified");
                    updateExpenseState();
                }
            }
        });
        viewHolder.unverifiedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.unverifiedButton.isChecked()) {
                    list.get(index).setState("unverified");
                    updateExpenseState();
                }
            }
        });
        viewHolder.fraudButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.fraudButton.isChecked()) {
                    list.get(index).setState("fraud");
                    updateExpenseState();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        CardView expenseDetailCard;
        TextView expenseIdTag;
        TextView expenseId;
        TextView descriptionTag;
        TextView description;
        TextView amountTag;
        TextView amount;
        TextView timeTag;
        TextView timeAndDate;
        TextView categoryTag;
        TextView category;
        RadioButton verifiedButton;
        RadioButton unverifiedButton;
        RadioButton fraudButton;

        ExpenseViewHolder(View expenseView) {
            super(expenseView);
            expenseDetailCard = (CardView) expenseView.findViewById(R.id.expense_detail_card);
            expenseIdTag = (TextView) expenseView.findViewById(R.id.expense_id_tag);
            expenseId = (TextView) expenseView.findViewById(R.id.expense_id);
            descriptionTag = (TextView) expenseView.findViewById(R.id.description_tag);
            description = (TextView) expenseView.findViewById(R.id.description);
            amountTag = (TextView) expenseView.findViewById(R.id.amount_tag);
            amount = (TextView) expenseView.findViewById(R.id.amount);
            timeTag = (TextView) expenseView.findViewById(R.id.time_tag);
            timeAndDate = (TextView) expenseView.findViewById(R.id.time_and_date);
            categoryTag = (TextView) expenseView.findViewById(R.id.category_tag);
            category = (TextView) expenseView.findViewById(R.id.category);
            verifiedButton = (RadioButton) expenseView.findViewById(R.id.verified_button);
            unverifiedButton = (RadioButton) expenseView.findViewById(R.id.unverified_button);
            fraudButton = (RadioButton) expenseView.findViewById(R.id.fraud_button);
        }
    }

    public void updateExpenseState() {
        OkHttpClient okHttpClient = new OkHttpClient();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("https://jsonblob.com")
                .build();

        JsonBlobApi api = restAdapter.create(JsonBlobApi.class);
        Log.d("Test", expenseList.toString());
        api.updateExpenseState(expenseList, new Callback<ExpenseList>() {
            @Override
            public void success(ExpenseList list, Response response) {
            }

            @Override
            public void failure(RetrofitError error) {
                String internetErrorMessage = "Internet connection not available! Please check your internet connection.";
                Toast toast = Toast.makeText(context, internetErrorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
